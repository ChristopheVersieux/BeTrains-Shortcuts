package be.irail.liveboards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Date;

import be.irail.liveboards.adapter.StationInfoAdapter;
import be.irail.liveboards.bo.Station;


public class LiveboardActivity extends AppCompatActivity {
    String id;
    private Station currentStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("Name");
        setTitle(name);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        id = getIntent().getExtras().getString("ID");

        String langue = getString(R.string.lan);

        Log.d("CVE", "ID = " + id);

        if (id != null)
            Ion.with(this).load("http://res.cloudinary.com/dywgd02hq/image/upload/" + id.replace("BE.NMBS.", "") + ".jpg").intoImageView((ImageView) findViewById(R.id.headerpic));

        String url;
        if (id != null && id.length() > 0)
            url = "http://api.irail.be/liveboard.php/?id="
                    + id +
                    "&format=JSON&fast=true" + "&lang=" + langue;
        else
            url = "http://api.irail.be/liveboard.php/?station="
                    + name.replace(" ", "%20")
                    + "&format=JSON&fast=true" + "&lang=" + langue;

        Log.e("CVE", url);
        Ion.with(this).load(url).userAgent("WazaBe: BeTrains Shortcuts " + BuildConfig.VERSION_NAME + " for Android").as(new TypeToken<Station>() {
        }).setCallback(new FutureCallback<Station>() {
            @Override
            public void onCompleted(Exception e, Station result) {
                currentStation = result;
                long timestamp = System.currentTimeMillis();
                // if (pd != null)
                //     pd.dismiss();
                if (currentStation != null)
                    if (currentStation.getStationDepartures() != null) {

                        if (id == null) {
                            try {
                                Log.e("CVE","http:");
                                Log.e("CVE","http://res.cloudinary.com/dywgd02hq/image/upload/" + currentStation.getStationInfo().id.replace("BE.NMBS.", "") + ".jpg");
                                Ion.with(LiveboardActivity.this).load("http://res.cloudinary.com/dywgd02hq/image/upload/" + currentStation.getStationInfo().id.replace("BE.NMBS.", "") + ".jpg").intoImageView((ImageView) findViewById(R.id.headerpic));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }

                        StationInfoAdapter stationInfoAdapter = new StationInfoAdapter(
                                currentStation.getStationDepartures()
                                        .getStationDeparture()
                        );
                        RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.list));
                        recyclerView.setLayoutManager(new LinearLayoutManager(LiveboardActivity.this));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(stationInfoAdapter);
                        setTitle(Utils.formatDate(new Date(timestamp),
                                "dd MMM HH:mm"));
                        //Log.e("CVE", "OK");
                    } else {

                        Toast.makeText(LiveboardActivity.this, R.string.txt_no_result,
                                Toast.LENGTH_LONG).show();
                        setTitle(Utils.formatDate(new Date(timestamp),
                                "dd MMM HH:mm"));

                    }
                else {
                    Toast.makeText(LiveboardActivity.this, R.string.txt_connection,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

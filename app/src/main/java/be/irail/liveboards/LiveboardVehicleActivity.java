package be.irail.liveboards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import be.irail.liveboards.adapter.TrainInfoAdapter;
import be.irail.liveboards.bo.Vehicle;


public class LiveboardVehicleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("Name");
        setTitle(name);
        ((ImageView) findViewById(R.id.headerpic)).setImageResource(R.drawable.train);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final String url = "http://api.irail.be/vehicle.php/?id=" + name
                + "&lang=" + getString(R.string.lan) + "&format=JSON";//&fast=true";
        Log.e("CVE", url);
        Ion.with(this).load(url).userAgent("WazaBe: BeTrains Shortcuts " + BuildConfig.VERSION_NAME + " for Android").as(new TypeToken<Vehicle>() {
        }).withResponse().setCallback(new FutureCallback<Response<Vehicle>>() {
            @Override
            public void onCompleted(Exception e, Response<Vehicle> result) {
                if(result==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LiveboardVehicleActivity.this);
                    builder.setMessage("Please send and email to christophe.versieux+betrains@gmail.com with a screenshot of this screen or this url:\n"+url)
                            .setTitle("ERROR");
                    builder.create().show();
                }
                Vehicle currentVehicle = result.getResult();

                if (currentVehicle != null
                        && currentVehicle.getVehicleStops() != null) {
                    TrainInfoAdapter trainInfoAdapter = new TrainInfoAdapter(currentVehicle
                            .getVehicleStops().getVehicleStop());
                    RecyclerView recyclerView = ((RecyclerView) findViewById(R.id.list));
                    recyclerView.setLayoutManager(new LinearLayoutManager(LiveboardVehicleActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

                    recyclerView.setAdapter(trainInfoAdapter);
                    //setTitle(Utils.formatDate(new Date(timestamp),
                    //        "dd MMM HH:mm"));
                } else {
                    if (e != null) {
                        Toast.makeText(LiveboardVehicleActivity.this, e.getLocalizedMessage(),
                                Toast.LENGTH_LONG).show();
                        LiveboardVehicleActivity.this.finish();
                    } else {
                        if (result.getHeaders().code() == 502) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LiveboardVehicleActivity.this);
                            builder.setTitle(R.string.irailissue);
                            builder.setMessage(R.string.irailissueDetail);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    LiveboardVehicleActivity.this.finish();
                                }
                            });
                            builder.setNegativeButton(R.string.report, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(LiveboardVehicleActivity.this);
                                    builder.setType("message/rfc822");
                                    builder.addEmailTo("iRail@list.iRail.be");
                                    builder.setSubject("Issue with iRail API");
                                    builder.setText("Hello, I am currently using the Android application BeTrains, and I get an error while using the iRail API.\n\n" +
                                            "I get this message: 'Could not get data. Please report this problem to iRail@list.iRail.be' while trying to query :\n" + url + "\n\n" +
                                            "I hope you can fix that soon.\nHave a nice day.");
                                    builder.setChooserTitle("Send Email");
                                    builder.startChooser();

                                    //getActivity().finish();
                                }
                            });
                            builder.create().show();
                        }

                    }


                }
            }
        });
    }
}

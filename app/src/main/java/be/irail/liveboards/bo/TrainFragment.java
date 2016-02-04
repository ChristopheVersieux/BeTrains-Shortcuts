package be.irail.liveboards.bo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import be.irail.liveboards.LiveboardActivity;
import be.irail.liveboards.R;


public class TrainFragment extends Fragment {

    String lan;
    SharedPreferences mPrefs;
    AutoCompleteTextView autocompleteStation;
    EditText editStation;
    ArrayList<String> validWords = new ArrayList<>();
    String stationId;
    String stationName;

    public TrainFragment() {
        // Required empty public constructor
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
                getActivity().findViewById(R.id.headerpic).setBackgroundResource(R.drawable.train);
        }
    }

    public static TrainFragment newInstance() {
        TrainFragment fragment = new TrainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_train, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ObservableScrollView mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
        autocompleteStation = ((AutoCompleteTextView) getView().findViewById(R.id.autocompleteStation));
        editStation = ((EditText) getView().findViewById(R.id.editStation));

        editStation.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    doShortcut();
                    return true;
                }
                return false;
            }
        });

        getView().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stationId != null)
                    doShortcut();
                else
                    Snackbar.make(getView(), R.string.error_select, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void doShortcut() {
        Intent shortcutIntent = new Intent(TrainFragment.this.getContext(), LiveboardActivity.class);
        shortcutIntent.putExtra("Name", stationName);
        shortcutIntent.putExtra("ID", stationId);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, editStation.getText().toString());
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getActivity(), R.mipmap.ic_launcher));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        TrainFragment.this.getActivity().sendBroadcast(intent);
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lan = getString(R.string.lan);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        long delta = System.currentTimeMillis() - mPrefs.getLong("stationsDate" + lan, 0);
        if (delta > (10 * DateUtils.DAY_IN_MILLIS)) {
            Ion.with(this)
                    .load("http://api.irail.be/stations.php?format=json&lang=" + R.string.lan)
                    .as(new TypeToken<StationLocationApi>() {
                    })
                    .setCallback(new FutureCallback<StationLocationApi>() {
                        @Override
                        public void onCompleted(Exception e, StationLocationApi apiList) {
                            if (e != null)
                                Log.e("CVE", e.getLocalizedMessage());
                            else if (apiList != null && apiList.station != null) {
                                SharedPreferences.Editor ed = mPrefs.edit();
                                Gson gson = new Gson();
                                //ed.putString("stations" + lan, gson.toJson(apiList));
                                //ed.putLong("stationsDate" + lan, System.currentTimeMillis());
                                //ed.putString("stationsLan" + lan, getString(R.string.lan));
                                ed.apply();
                                fillAutoComplete(apiList);
                            }
                        }
                    });
        }
        StationLocationApi cache = new Gson().fromJson(mPrefs.getString("stations", ""), StationLocationApi.class);
        if (cache != null)
            fillAutoComplete(cache);
    }

    private void fillAutoComplete(StationLocationApi cache) {

        validWords.clear();

        for (StationLocation aStation : cache.station)
            validWords.add(aStation.getName());

        ArrayAdapter<StationLocation> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.simple_dropdown_item_1line, cache.station);
        autocompleteStation.setAdapter(adapter);
        autocompleteStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StationLocation item = (StationLocation) parent.getItemAtPosition(position);
                stationId = item.getId();
                stationName = item.getName();
                //Log.d("CVE","http://res.cloudinary.com/dywgd02hq/image/upload/" + item.getId().replace("BE.NMBS.", "") + ".jpg");
                Ion.with(getActivity()).load("http://res.cloudinary.com/dywgd02hq/image/upload/" + item.getId().replace("BE.NMBS.", "") + ".jpg").intoImageView((ImageView) getActivity().findViewById(R.id.headerpic));
                editStation.setEnabled(true);
                editStation.setText(item.getName());
                editStation.requestFocus();
                autocompleteStation.setError(null);

            }
        });

        autocompleteStation.setValidator(new Validator());
        autocompleteStation.setOnFocusChangeListener(new FocusListener());
    }

    class Validator implements AutoCompleteTextView.Validator {

        @Override
        public boolean isValid(CharSequence text) {
            if (validWords.contains(text.toString())) {
                editStation.setEnabled(true);
                editStation.setText(text);
                editStation.requestFocus();
                autocompleteStation.setError(null);
                return true;
            }
            autocompleteStation.clearComposingText();
            editStation.setEnabled(false);
            return false;
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            autocompleteStation.setError(getString(R.string.error_station));
            editStation.setEnabled(false);
            return invalidText;
        }
    }

    class FocusListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            //Log.v("CVE", "Focus changed");
            if (v.getId() == R.id.autocompleteStation && !hasFocus) {
                //Log.v("CVE", "Performing validation");
                ((AutoCompleteTextView) v).performValidation();
            }
        }
    }
}

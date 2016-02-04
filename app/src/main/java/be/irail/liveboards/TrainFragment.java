package be.irail.liveboards;

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

import be.irail.liveboards.bo.StationLocation;
import be.irail.liveboards.bo.StationLocationApi;


public class TrainFragment extends Fragment {

    String lan;
    SharedPreferences mPrefs;
    EditText editTrain;
    EditText trainShort;

    public TrainFragment() {
        // Required empty public constructor
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            ((ImageView)getActivity().findViewById(R.id.headerpic)).setImageResource(R.drawable.train);
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

        editTrain = ((EditText) getView().findViewById(R.id.editTrain));

        editTrain.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    trainShort.setText(editTrain.getText().toString());
                    return true;
                }
                return false;
            }
        });

        trainShort = ((EditText) getView().findViewById(R.id.trainShort));

        getView().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trainShort.getText().toString() != null)
                    doShortcut();
                else
                    Snackbar.make(getView(), R.string.error_select, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void doShortcut() {
        Intent shortcutIntent = new Intent(TrainFragment.this.getContext(), LiveboardVehicleActivity.class);
        shortcutIntent.putExtra("Name", editTrain.getText().toString());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, trainShort.getText().toString());
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

    }



}

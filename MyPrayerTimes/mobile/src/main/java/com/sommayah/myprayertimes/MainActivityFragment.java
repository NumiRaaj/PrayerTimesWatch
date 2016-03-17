package com.sommayah.myprayertimes;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sommayah.myprayertimes.dataModels.PrayTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnSharedPreferenceChangeListener{
    RecyclerView mRecyclerView;
    private boolean mUseNextPrayerLayout;
    private PrayerAdapter mPrayerAdapter;
    private PrayTime mPraytime;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_prayer);

        // Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View emptyView = rootView.findViewById(R.id.recyclerview_prayer_empty);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //get prayer data:
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);


        ArrayList<String> prayerTimes;
        prayerTimes = Utility.getPrayTimes(cal,getContext());

        if(!Utility.isAlarmInitiated(getContext())){
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.pref_alarm_initiated), true);
            PrayerAlarmReceiver prayerAlarmReceiver = new PrayerAlarmReceiver();
            prayerAlarmReceiver.addPrayerAlarm(getContext());

        }


        //ss:temp adapter that don't user cursor
        PrayerAdapter mAdapter = new PrayerAdapter(prayerTimes,getActivity(), emptyView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        // The PrayerAdapter will take data from a source and
//        // use it to populate the RecyclerView it's attached to.
//        mPrayerAdapter = new PrayerAdapter(getActivity(), new PrayerAdapter.PrayerAdapterOnClickHandler() {
//            @Override
//            public void onClick(int prayerPos, PrayerAdapter.PrayerAdapterViewHolder vh) {
//                //leave it empty now till i impelement the click action
//            }
//        }, emptyView);
//
//        // specify an adapter (see also next example)
//        mRecyclerView.setAdapter(mPrayerAdapter);
////        if (savedInstanceState != null) {
////            mPrayerAdapterAdapter.onRestoreInstanceState(savedInstanceState);
////        }



        //ss:havent set the boolean value yet
       // mPrayerAdapter.setUseNextPrayerLayout(mUseNextPrayerLayout);


        return rootView;
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();

    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

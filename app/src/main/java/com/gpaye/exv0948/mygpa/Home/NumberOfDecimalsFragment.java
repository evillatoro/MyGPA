package com.gpaye.exv0948.mygpa.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.gpaye.exv0948.mygpa.R;

import static android.content.Context.MODE_PRIVATE;
import static com.gpaye.exv0948.mygpa.Home.HomeFragment.MY_PREFS_NAME;

/**
 * Created by EXV0948 on 7/28/2017.
 */

public class NumberOfDecimalsFragment extends Fragment {
    private static final String TAG = "NumberOfDecimalsFragmen";

    private RadioGroup radiogroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_of_decimals, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Number Of Decimals");
        radiogroup = (RadioGroup) view.findViewById(R.id.decimalRadioGroup);
        setUpButton();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                numberOfDecimals();
            }
        });
        return view;
    }

    private void setUpButton() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int numberOfDecimals = prefs.getInt("numberOfDecimals", 2);
        if (numberOfDecimals == 1) {
            radiogroup.check(R.id.oneDecimal);
        } else if (numberOfDecimals == 2) {
            radiogroup.check(R.id.twoDecimals);
        } else {
            radiogroup.check(R.id.threeDecimals);
        }
    }

    private void numberOfDecimals() {
        int checkedRadioButtonId = radiogroup.getCheckedRadioButtonId();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        if (checkedRadioButtonId == R.id.oneDecimal) {
            Log.d(TAG, "******* ONE RADIO BUTTON");
            editor.putInt("numberOfDecimals", 1);
        } else if (checkedRadioButtonId == R.id.twoDecimals){
            Log.d(TAG, "******* TWO RADIO BUTTON");
            editor.putInt("numberOfDecimals", 2);
        } else {
            Log.d(TAG, "******* THREE RADIO BUTTON");
            editor.putInt("numberOfDecimals", 3);
        }

        editor.apply();
    }
}

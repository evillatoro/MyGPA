package com.gpaye.exv0948.mygpa.Home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.Home.adapters.SemesterAdapter;
import com.gpaye.exv0948.mygpa.model.AndroidDatabaseManager;
import com.gpaye.exv0948.mygpa.model.DatabaseHelper;
import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Semester;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by EXV0948 on 7/18/2017.
 */

public class HomeFragment extends Fragment implements AddSemesterFragment.OnSemesterAddedListener, SemesterAdapter.SemesterCallBack {
    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private DatabaseHelper mDatabaseHelper;
    private TextView cumulativeGPALabel, addSemesterLabel;
    private ArrayList<Semester> listData;
    boolean testerMode = false;
    private Button button;
    private SemesterAdapter mAdapter;
    private AlertDialog alertDialog;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.semesterRecyclerView);
        cumulativeGPALabel = (TextView) view.findViewById(R.id.cumulativeGPALabel);
        addSemesterLabel = (TextView) view.findViewById(R.id.addSemesterTextView);
        addSemesterLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).goToAddSemesterFragment();
            }
        });
        mDatabaseHelper = new DatabaseHelper(getActivity());

        setHasOptionsMenu(true);

        if (testerMode) {
            button =(Button) view.findViewById(R.id.dbButton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent dbmanager = new Intent(v.getContext().getApplicationContext(), AndroidDatabaseManager.class);
                    startActivity(dbmanager);
                }
            });
            button.setVisibility(View.VISIBLE);
        }

        // set up swiping for semester items
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                        toastMessage("Swiped");
                        if (alertDialog == null || !alertDialog.isShowing()) {
                            alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Delete Semester")
                                    .setMessage("Delete " + listData.get(viewHolder.getAdapterPosition()).getSemesterName() + "?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            toastMessage("Deleted " + listData.get(viewHolder.getAdapterPosition()).getSemesterName());
                                            deleteSemester(viewHolder.getAdapterPosition());
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mRecyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int restoredText = prefs.getInt("numberOfDecimals", 2);
        Log.d(TAG, "onCreateView: Number of decimals " + restoredText);

        populateList();
        return view;
    }

    private void populateList() {
        Log.d(TAG, "populateList: Display semesters in the RecyclerView");

        // get the data and append to a list
        listData = mDatabaseHelper.getSemesters();

        cumulativeGPALabel.setText("CUMULATIVE GPA: N/A");

        if (listData.size() == 0) {
            addSemesterLabel.setVisibility(View.VISIBLE);
        } else {
            addSemesterLabel.setVisibility(View.GONE);
            double cumulativeGPA;
            double cumulativeSemesterCredits = 0.0;
            double cumulativeQualityPoints = 0.0;
            for (Semester s : listData) {
                cumulativeQualityPoints += s.getQualityPoints();
                cumulativeSemesterCredits += s.getSemesterCredits();
            }

            if (cumulativeSemesterCredits != 0) {
                cumulativeGPA = cumulativeQualityPoints / cumulativeSemesterCredits;

                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                int numberOfDecimals = prefs.getInt("numberOfDecimals", 2);
                String cumulativeGPAString;
                double semesterGPA;

                if (numberOfDecimals == 1) {
                    semesterGPA = Math.round(cumulativeGPA * 10.0) / 10.0;
                    cumulativeGPAString = String.format( "%.1f", semesterGPA );
                } else if (numberOfDecimals == 2) {
                    semesterGPA = Math.round(cumulativeGPA * 100.0) / 100.0;
                    cumulativeGPAString = String.format( "%.2f", semesterGPA );
                } else {
                    semesterGPA = Math.round(cumulativeGPA * 1000.0) / 1000.0;
                    cumulativeGPAString = String.format( "%.3f", semesterGPA );
                }

                cumulativeGPALabel.setText("CUMULATIVE GPA: " + cumulativeGPAString);
            }
        }

        mAdapter = new SemesterAdapter(listData, getContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAdapter.setSemesterCallBack(this);

    }

    @Override
    public void OnSemesterClick(int p) {
        ((HomeActivity)getActivity()).goToCourseListFragment(listData.get(p).getSemesterName());
    }

    @Override
    public void OnSemesterLongClick(final int p) {
        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Semester")
                .setMessage("Delete " + listData.get(p).getSemesterName() + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        toastMessage("Deleted " + listData.get(p).getSemesterName());
                        deleteSemester(p);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRecyclerView.getAdapter().notifyItemChanged(p);
                    }
                }).show();
    }

    @Override
    public void refreshList() {
        populateList();
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void deleteSemester(int position) {
        deleteSemesterFromDatabase(position);
    }

    private void deleteSemesterFromDatabase(int position) {
        String semesterToDelete = listData.get(position).getSemesterName();
        mDatabaseHelper.deleteSemester(semesterToDelete);
        populateList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem add = menu.findItem(R.id.action_add_semester);
        add.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_semester) {
            ((HomeActivity)getActivity()).goToAddSemesterFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.gpaye.exv0948.mygpa.Home;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.Home.adapters.CourseAdapter;
import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Course;
import com.gpaye.exv0948.mygpa.model.DatabaseHelper;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.gpaye.exv0948.mygpa.Home.HomeFragment.MY_PREFS_NAME;

/**
 * Created by EXV0948 on 7/19/2017.
 */

public class CourseListFragment extends Fragment implements AddEditCourseFragment.OnCourseAddedListener, CourseAdapter.CourseCallBack {
    private static final String TAG = "CourseListFragment";

    private DatabaseHelper mDatabaseHelper;
    private RecyclerView mRecyclerView;
    private TextView semesterGPALabel, addCourseLabel;
    private ArrayList<Course> listData;
    private CourseAdapter mAdapter;
    private String semesterName;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        mDatabaseHelper = new DatabaseHelper(view.getContext());
        semesterGPALabel = (TextView) view.findViewById(R.id.semesterGPALabel);
        setHasOptionsMenu(true);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.courseRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        semesterName = getArguments().getString("semesterName");
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle(semesterName);

        addCourseLabel = (TextView) view.findViewById(R.id.addCourseTextView);
        addCourseLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).goToAddCourseFragment(CourseListFragment.this, semesterName);
            }
        });

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
                                    .setTitle("Delete Course")
                                    .setMessage("Delete " + listData.get(viewHolder.getAdapterPosition()).getName() + "?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            toastMessage("Deleted " + listData.get(viewHolder.getAdapterPosition()).getName());
                                            deleteCourseFromDatabase(viewHolder.getAdapterPosition());
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

        populateList(semesterName);

        return view;
    }

    private void populateList(String semesterName) {
        Log.d(TAG, "populateList: Display courses in the RecyclerView");

        // get the courses
        Cursor data = mDatabaseHelper.getCourses(semesterName);
        listData = new ArrayList<>();
        while(data.moveToNext()) {
            Course course = new Course(data.getString(0), data.getString(1), data.getDouble(2), data.getDouble(3), data.getInt(4));
            listData.add(0, course);
        }

        semesterGPALabel.setText("SEMESTER GPA: N/A");

        if (listData.size() == 0) {
            addCourseLabel.setVisibility(View.VISIBLE);
        } else {
            addCourseLabel.setVisibility(View.GONE);
            double cumulativeGPA;
            double cumulativeSemesterCredits = 0.0;
            double cumulativeQualityPoints = 0.0;
            int numberOfCoursesThatCount = 0;
            for (Course s : listData) {
                if (s.getCountTowardsGPA() == 1) {
                    numberOfCoursesThatCount++;
                    cumulativeQualityPoints += s.getQualityPoints();
                    cumulativeSemesterCredits += s.getCredits();
                }
            }

            if (numberOfCoursesThatCount != 0) {
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

                semesterGPALabel.setText("SEMESTER GPA: " + cumulativeGPAString);
            }
        }

        mAdapter = new CourseAdapter(listData, getContext());
        mAdapter.setCourseCallBack(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void deleteCourseFromDatabase(int position) {
        Course courseToDelete = listData.get(position);
        mDatabaseHelper.deleteCourse(courseToDelete);
        populateList(semesterName);
    }

    @Override
    public void refreshList() {
        populateList(semesterName);
    }

    @Override
    public void OnCourseClick(int p) {
        Course course = listData.get(p);
        ((HomeActivity)getActivity()).goToEditCourseFragment(CourseListFragment.this, course);
    }

    @Override
    public void OnCourseLongClick(final int p) {
        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Course")
                .setMessage("Delete " + listData.get(p).getName() + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        toastMessage("Deleted " + listData.get(p).getName());
                        deleteCourseFromDatabase(p);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRecyclerView.getAdapter().notifyItemChanged(p);
                    }}).show();
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem add = menu.findItem(R.id.action_add_course);
        add.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_course) {
            ((HomeActivity)getActivity()).goToAddCourseFragment(this, semesterName);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


package com.gpaye.exv0948.mygpa.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Course;
import com.gpaye.exv0948.mygpa.model.DatabaseHelper;
import com.gpaye.exv0948.mygpa.model.Grade;
import com.gpaye.exv0948.mygpa.model.GradeScale;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by EXV0948 on 7/20/2017.
 */

public class AddEditCourseFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddEditCourseFragment";

    private DatabaseHelper mDatabaseHelper;
    private EditText courseNameEditText, courseCreditEditText, courseGradeEditText;
    private String oldCourseName;
    private RadioGroup radioGroup;
    private MaterialSpinner spinner;
    private ArrayAdapter<Grade> adapter;
    private List<Grade> listItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_course, container, false);
        mDatabaseHelper = new DatabaseHelper(view.getContext());
        radioGroup = (RadioGroup) view.findViewById(R.id.countTowardsGPARadioGroup);

        setHasOptionsMenu(true);
        courseNameEditText = (EditText) view.findViewById(R.id.courseNameEditText);
        courseCreditEditText = (EditText) view.findViewById(R.id.courseCreditEditText);
        courseGradeEditText = (EditText) view.findViewById(R.id.courseGradeEditText);
        /**
         * Set pointer to end of text in edittext when user clicks Next on KeyBoard.
         */
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ((EditText) view).setSelection(((EditText) view).getText().length());
                }
            }
        };

        courseNameEditText.setOnFocusChangeListener(onFocusChangeListener);
        courseCreditEditText.setOnFocusChangeListener(onFocusChangeListener);
        courseGradeEditText.setOnFocusChangeListener(onFocusChangeListener);

        int type = getArguments().getInt("type");
        if (type == 1) {
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("EDIT COURSE");
            addCourseDetail();

        } else {
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("ADD COURSE");
            showKeyboardOnStartUp();
        }

        initItems();
        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<Grade>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        // Spinner element
//        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
//
//        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
//
//        // Spinner Drop down elements
//        ArrayList<Grade> categories = new GradeScale().getGradeScaleGrades();
//
//        // Creating adapter for spinner
//        ArrayAdapter<Grade> dataAdapter = new ArrayAdapter<Grade>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);

        return view;
    }

    private void addCourse() {
        String courseName = courseNameEditText.getText().toString();
        String semesterName = getArguments().getString("semesterName");
        try {
            if(courseName.equals("")) {
                throw new IllegalArgumentException();
            }
            double courseCredit = Double.parseDouble(courseCreditEditText.getText().toString());
            double courseGrade = Double.parseDouble(courseGradeEditText.getText().toString());
            int countTowardsGPA = getCountTowardsGPA();

            Course course = new Course(courseName, semesterName, courseCredit, courseGrade, countTowardsGPA);
            if (mDatabaseHelper.addCourse(course)) {
                toastMessage(courseName + " added.");
                //meAddListener.refreshList();
                getActivity().onBackPressed();
            } else {
                toastMessage("Course already exist in this semester.");
            }
        } catch (NumberFormatException e) {
            toastMessage("Please enter all fields.");
        } catch (IllegalArgumentException e) {
            toastMessage("Please enter a name.");
        }
    }

    private void updateCourse() {
        String courseName = courseNameEditText.getText().toString();
        String semesterName = getArguments().getString("semesterName");
        try {
            if(courseName.equals("")) {
                throw new IllegalArgumentException();
            }
            double courseCredit = Double.parseDouble(courseCreditEditText.getText().toString());
            double courseGrade = Double.parseDouble(courseGradeEditText.getText().toString());
            int countTowardsGPA = getCountTowardsGPA();

            Course course = new Course(courseName, semesterName, courseCredit, courseGrade, countTowardsGPA);
            if (mDatabaseHelper.updateCourse(course, oldCourseName)) {
                toastMessage(oldCourseName + " updated to " + course);
                //meAddListener.refreshList();
                getActivity().onBackPressed();
            } else {
                toastMessage("Course already exist in this semester.");
            }
        } catch (NumberFormatException e) {
            toastMessage("Please enter all fields.");
        } catch (IllegalArgumentException e) {
            toastMessage("Please enter a name.");
        }
    }

    private int getCountTowardsGPA() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.yesRadioButton) {
            Log.d(TAG, "******* YES RADIO BUTTON ");
            return 1;
        } else {
            Log.d(TAG, "******* NO RADIO BUTTON ");
            return 0;
        }
    }

    private void addCourseDetail() {
        String courseName = getArguments().getString("courseName");
        Double courseGrade = getArguments().getDouble("courseGrade");
        Double courseCredits = getArguments().getDouble("courseCredits");

        int countTowardsGPA = getArguments().getInt("courseCountTowardsGPA");
        if (countTowardsGPA == 1) {
            radioGroup.check(R.id.yesRadioButton);
        } else {
            radioGroup.check(R.id.noRadioButton);
        }

        oldCourseName = courseName;

        courseNameEditText.setText(courseName);
        courseCreditEditText.setText(courseCredits + "");
        courseGradeEditText.setText(courseGrade + "");
    }

//    private void hideKeyBoard() {
//        try  {
//            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);
//        } catch (Exception e) {
//            Log.d(TAG, "hideKeyBoard: " + e.getMessage());
//        }
//    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnCourseAddedListener {
        void refreshList();
    }

    private static OnCourseAddedListener meAddListener;

    public void registerListener(OnCourseAddedListener listener) {
        meAddListener = listener;
    }

    private void showKeyboardOnStartUp() {
        courseNameEditText.requestFocus();

        ((InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.action_save);
        menuItem.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            int type = getArguments().getInt("type");
            if (type == 1) {
                updateCourse();
            } else {
                addCourse();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void initItems() {
        GradeScale gradeScale = new GradeScale();
        listItems.addAll(gradeScale.getGradeScaleGrades());
    }
}

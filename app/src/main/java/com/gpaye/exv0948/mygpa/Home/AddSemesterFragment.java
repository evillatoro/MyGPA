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
import android.widget.EditText;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.model.DatabaseHelper;
import com.gpaye.exv0948.mygpa.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by EXV0948 on 7/18/2017.
 */

public class AddSemesterFragment extends Fragment {
    private static final String TAG = "AddSemesterFragment";

    private DatabaseHelper mDatabaseHelper;
    private EditText semesterEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_semester, container, false);
        mDatabaseHelper = new DatabaseHelper(view.getContext());
        setHasOptionsMenu(true);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("ADD SEMESTER");

        semesterEditText = (EditText) view.findViewById(R.id.semesterNameEditText);

        showKeyboardOnStartUp();
        return view;
    }

    private boolean addSemester() {
        String semesterName = semesterEditText.getText().toString();

        if (semesterName.equals("")) {
            toastMessage("Please enter a name.");
        } else {
            if (mDatabaseHelper.addSemester(semesterName)) {
                toastMessage(semesterName + " added.");
                meAddListener.refreshList();
                hideKeyBoard();
                getActivity().onBackPressed();
                return true;
            } else {
                toastMessage("Semester already exist.");
            }
        }
        return false;
    }

    private void hideKeyBoard() {
        try  {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d(TAG, "hideKeyBoard: " + e.getMessage());
        }
    }

    private void showKeyboardOnStartUp() {
        semesterEditText.requestFocus();
        ((InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        );
    }

    /**
     * customizable toast
     * @param message message to display
     */
    private void toastMessage(String message){
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnSemesterAddedListener {
        void refreshList();
    }

    private static OnSemesterAddedListener meAddListener;

    public void registerListener(OnSemesterAddedListener listener) {
        meAddListener = listener;
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
            addSemester();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

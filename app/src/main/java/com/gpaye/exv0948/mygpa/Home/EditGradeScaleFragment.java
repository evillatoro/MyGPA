package com.gpaye.exv0948.mygpa.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.Home.adapters.GradeScaleAdapter;
import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.DatabaseHelper;
import com.gpaye.exv0948.mygpa.model.Grade;
import com.gpaye.exv0948.mygpa.model.GradeScale;

import java.util.ArrayList;

/**
 * Created by EXV0948 on 7/19/2017.
 */

public class EditGradeScaleFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private DatabaseHelper mDatabaseHelper;
    private EditText semsterEditText;
    private Button addSemesterBtn;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_grade_scale, container, false);
        mDatabaseHelper = new DatabaseHelper(view.getContext());
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.letterGradeRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("EDIT GRADE SCALE");

        populateList();

        return view;
    }

    private void populateList() {
        Log.d(TAG, "populateList: Display grade letters in the RecyclerView");

        GradeScale defaultScale = new GradeScale();
        final ArrayList<Grade> listData = defaultScale.getGradeScaleGrades();

        final RecyclerView.Adapter mAdapter = new GradeScaleAdapter(listData, getContext());
//                new RecyclerView.Adapter<LetterGradeCustomHolder>() {
//            @Override
//            public LetterGradeCustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.list_item_letter_grade, parent, false);
//                return new LetterGradeCustomHolder(view);
//            }
//
//            @Override
//            public void onBindViewHolder(LetterGradeCustomHolder holder, final int position) {
//                holder.gradeLetter.setText(listData.get(position).getLetter());
//                holder.gradePoints.setText(listData.get(position).getPoints() + "");
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                        alertDialog.setTitle("Change Point Value");
//                        alertDialog.setMessage("Enter a new weight for " + listData.get(position).getLetter() + ".");
//                        final EditText input = new EditText(getActivity());
//                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.MATCH_PARENT);
//                        input.setLayoutParams(lp);
//                        alertDialog.setView(input);
//                        alertDialog.setIcon(R.drawable.ic_menu_edit);
//                        alertDialog.setPositiveButton("Change",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        listData.get(position).setPoints(Double.parseDouble(input.getText().toString()));
//                                    }
//                                });
//                        alertDialog.setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        alertDialog.show();
//                    }
//                });
//            }
//
//            @Override
//            public int getItemCount() {
//                return listData.size();
//            }
//        };

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private class LetterGradeCustomHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "LetterGradeCustomHolder";

        private TextView gradeLetter;
        private TextView gradePoints;

        public LetterGradeCustomHolder(View itemView) {
            super(itemView);
            gradeLetter = (TextView) itemView.findViewById(R.id.gradeLetter);
            gradePoints = (TextView) itemView.findViewById(R.id.gradePoints);
        }
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

//        MenuItem add = menu.findItem(R.id.action_add_semester);
//        add.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showKeyboard() {
        ((InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        );
    }
}

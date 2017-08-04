package com.gpaye.exv0948.mygpa.Home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Grade;
import com.gpaye.exv0948.mygpa.model.Semester;

import java.util.List;

/**
 * Created by EXV0948 on 7/21/2017.
 */

public class GradeScaleAdapter extends RecyclerView.Adapter<GradeScaleAdapter.LetterGradeCustomHolder> {

    private List<Grade> listData;
    private LayoutInflater inflater;
    private Context context;

    public GradeScaleAdapter(List<Grade> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.context = c;
    }

    @Override
    public LetterGradeCustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_letter_grade, parent, false);
        return new LetterGradeCustomHolder(view);
    }

    @Override
    public void onBindViewHolder(LetterGradeCustomHolder holder, int position) {
        holder.gradeLetter.setText(listData.get(position).getLetter());
        holder.gradePoints.setText(listData.get(position).getPoints() + "");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class LetterGradeCustomHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "LetterGradeCustomHolder";

        private TextView gradeLetter;
        private TextView gradePoints;

        public LetterGradeCustomHolder(View itemView) {
            super(itemView);
            gradeLetter = (TextView) itemView.findViewById(R.id.gradeLetter);
            gradePoints = (TextView) itemView.findViewById(R.id.gradePoints);
        }
    }
}

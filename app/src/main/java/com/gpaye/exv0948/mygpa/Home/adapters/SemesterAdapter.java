package com.gpaye.exv0948.mygpa.Home.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Semester;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.gpaye.exv0948.mygpa.Home.HomeFragment.MY_PREFS_NAME;

/**
 * Created by EXV0948 on 7/21/2017.
 */

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private List<Semester> listData;
    private LayoutInflater inflater;
    private Context context;

    private SemesterCallBack semesterCallBack;

    public void setSemesterCallBack(final SemesterCallBack semesterCallBack) {
        this.semesterCallBack = semesterCallBack;
    }

    public SemesterAdapter(List<Semester> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.context = c;
    }

    @Override
    public SemesterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_semester, parent, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SemesterViewHolder holder, final int position) {
        holder.semesterName.setText(listData.get(position).getSemesterName());
        holder.semesterCredits.setText(listData.get(position).getSemesterCredits() + " credits");

        if (listData.get(position).getSemesterCredits() == 0) {
            holder.semesterGPA.setText("N/A");
        } else {
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int numberOfDecimals = prefs.getInt("numberOfDecimals", 2);
            String semesterString;
            double semesterGPA;

            if (numberOfDecimals == 1) {
                semesterGPA = Math.round(listData.get(position).getSemesterGpa() * 10.0) / 10.0;
                semesterString = String.format( "%.1f", semesterGPA );
            } else if (numberOfDecimals == 2) {
                semesterGPA = Math.round(listData.get(position).getSemesterGpa() * 100.0) / 100.0;
                semesterString = String.format( "%.2f", semesterGPA );
            } else {
                semesterGPA = Math.round(listData.get(position).getSemesterGpa() * 1000.0) / 1000.0;
                semesterString = String.format( "%.3f", semesterGPA );
            }

            holder.semesterGPA.setText(semesterString + "");
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface SemesterCallBack {
        void OnSemesterClick(int p);
        void OnSemesterLongClick(int p);
    }

    class SemesterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView semesterName;
        private TextView semesterGPA;
        private TextView semesterCredits;
        private View container;

        public SemesterViewHolder(View itemView) {
            super(itemView);
            semesterName = (TextView) itemView.findViewById(R.id.semesterName);
            semesterGPA = (TextView) itemView.findViewById(R.id.semesterGrade);
            semesterCredits = (TextView) itemView.findViewById(R.id.semesterCredits);

            container = itemView.findViewById(R.id.semesterContainer);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            semesterCallBack.OnSemesterClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            toastMessage("Hello Long Click");
            semesterCallBack.OnSemesterLongClick(getAdapterPosition());
            return true;
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

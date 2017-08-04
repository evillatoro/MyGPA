package com.gpaye.exv0948.mygpa.Home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gpaye.exv0948.mygpa.Home.CourseListFragment;
import com.gpaye.exv0948.mygpa.Home.HomeActivity;
import com.gpaye.exv0948.mygpa.R;
import com.gpaye.exv0948.mygpa.model.Course;

import java.util.List;

/**
 * Created by EXV0948 on 7/25/2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseCustomHolder> {

    private List<Course> listData;
    private LayoutInflater inflater;
    private Context context;

    private CourseCallBack courseCallBack;

    public void setCourseCallBack(final CourseCallBack courseCallBack) {
        this.courseCallBack = courseCallBack;
    }

    public CourseAdapter(List<Course> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
        this.context = c;
    }

    @Override
    public CourseCustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_course, parent, false);
        return new CourseCustomHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseCustomHolder holder, int position) {
        holder.courseName.setText(listData.get(position).getName());
        holder.courseCredits.setText(listData.get(position).getCredits() + " credits");
        holder.courseGrade.setText(listData.get(position).getGrade() + "");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface CourseCallBack {
        void OnCourseClick(int p);
        void OnCourseLongClick (int p);
    }

    class CourseCustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = "CourseCustomHolder";

        private TextView courseName;
        private TextView courseCredits;
        private TextView courseGrade;
        private View container;


        public CourseCustomHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            courseCredits = (TextView) itemView.findViewById(R.id.courseCredits);
            courseGrade = (TextView) itemView.findViewById(R.id.courseGrade);

            container = itemView.findViewById(R.id.courseContainer);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            courseCallBack.OnCourseClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            courseCallBack.OnCourseLongClick(getAdapterPosition());
            return true;
        }
    }
}

package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Course.CourseListData;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    Context mCxt;
    List<CourseListData>courseListDataList=new ArrayList<>();
    CourseClickEvents courseClickEvents;

    public CourseListAdapter(Context mCxt, CourseClickEvents courseClickEvents) {
        this.mCxt = mCxt;
        this.courseClickEvents = courseClickEvents;
    }

    public void setCourseList(List<CourseListData>courseList){
        courseListDataList=courseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_tutor_post,parent,false);
        return new CourseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        final CourseListData courseListData=courseListDataList.get(position);
        holder.txtTitle.setText(courseListData.getCourse_title());
        holder.txtDescription.setText(courseListData.getCourse_details());
        holder.txtPriceQuote.setText(courseListData.getCourse_fee());
        if (courseListData.getCourse_image()!=null)
            Glide.with(mCxt).load(courseListData.getCourse_image()).into(holder.imgPost);

        holder.cardTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseClickEvents.onCourseClick(courseListData.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseListDataList.size();
    }

    public class CourseListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPost;
        TextView txtTitle,txtDescription,txtPriceQuote;
        CardView cardTutor;

        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPost=itemView.findViewById(R.id.imgPost);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            txtDescription=itemView.findViewById(R.id.txtDescription);
            txtPriceQuote=itemView.findViewById(R.id.txtPriceQuote);
            cardTutor=itemView.findViewById(R.id.cardTutor);
        }
    }

    public interface CourseClickEvents{
        void onCourseClick(Integer courseId);
    }
}

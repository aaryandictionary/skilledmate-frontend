package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Course.CourseContentData;
import com.app.miniproject.R;

import java.util.ArrayList;
import java.util.List;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.CourseContentViewHolder> {

    Context mCxt;
    List<CourseContentData>courseContentDataList=new ArrayList<>();

    public CourseContentAdapter(Context mCxt) {
        this.mCxt = mCxt;
    }

    public void setCourseContent(List<CourseContentData>courseContent){
        courseContentDataList=courseContent;
        notifyDataSetChanged();
    }

    public void addCourseContent(CourseContentData courseContentData){
        courseContentDataList.add(courseContentData);
        notifyItemInserted(courseContentDataList.size());
    }

    @NonNull
    @Override
    public CourseContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_course_content,parent,false);
        return new CourseContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseContentViewHolder holder, int position) {
        CourseContentData courseContentData=courseContentDataList.get(position);
        holder.txtContentTitle.setText(courseContentData.getContent_title());
        holder.txtContentTimeline.setText(courseContentData.getContent_time());
        holder.txtContentDescription.setText(courseContentData.getContent_details());

        if (courseContentData.isExpandable()){
            holder.relExpandable.setVisibility(View.VISIBLE);
            holder.imgExpandable.setImageDrawable(mCxt.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24));
        }else {
            holder.relExpandable.setVisibility(View.GONE);
            holder.imgExpandable.setImageDrawable(mCxt.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24));
        }
    }

    @Override
    public int getItemCount() {
        return courseContentDataList.size();
    }

    public class CourseContentViewHolder extends RecyclerView.ViewHolder{

        TextView txtContentTitle,txtContentTimeline,txtContentDescription;
        ImageView imgExpandable;
        RelativeLayout relExpandable,relTab;

        public CourseContentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContentTitle=itemView.findViewById(R.id.txtContentTitle);
            txtContentTimeline=itemView.findViewById(R.id.txtContentTimeline);
            txtContentDescription=itemView.findViewById(R.id.txtContentDescription);
            imgExpandable=itemView.findViewById(R.id.imgExpandable);
            relExpandable=itemView.findViewById(R.id.relExpandable);
            relTab=itemView.findViewById(R.id.relTab);

            relTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (courseContentDataList.get(getAdapterPosition()).isExpandable())
                    courseContentDataList.get(getAdapterPosition()).setExpandable(false);
                    else courseContentDataList.get(getAdapterPosition()).setExpandable(true);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}

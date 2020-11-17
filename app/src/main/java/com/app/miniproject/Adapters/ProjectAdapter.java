package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Models.ProjectModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    Context mCxt;
    List<ProjectModel>projectModelList=new ArrayList<>();
    Integer[]overlays={R.drawable.bg_circle_black,R.drawable.bg_circle_blue,R.drawable.bg_circle_green,R.drawable.bg_circle_light_blue,R.drawable.bg_circle_pink,R.drawable.bg_circle_red,R.drawable.bg_circle_white,R.drawable.bg_circle_yellow};
    ProjectEvents projectEvents;

    public ProjectAdapter(Context mCxt, ProjectEvents projectEvents) {
        this.mCxt = mCxt;
        this.projectEvents = projectEvents;
    }

    public void setProjectList(List<ProjectModel>projectModels){
        projectModelList=projectModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_project,parent,false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        ProjectModel projectModel=projectModelList.get(position);
        holder.txtProjectName.setText(projectModel.getProjectName());
        Glide.with(mCxt).load(projectModel.getProjectImage()).into(holder.circularProjectImage);
        int rnd=new Random().nextInt(overlays.length);
        Glide.with(mCxt).load(overlays[rnd]).into(holder.imgProjectOverlay);
    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularProjectImage;
        ImageView imgProjectOverlay;
        TextView txtProjectName;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            circularProjectImage=itemView.findViewById(R.id.circularProjectImage);
            imgProjectOverlay=itemView.findViewById(R.id.imgProjectOverlay);
            txtProjectName=itemView.findViewById(R.id.txtProjectName);
        }
    }

    public interface ProjectEvents{
        void  onProjectCardClick();
    }
}

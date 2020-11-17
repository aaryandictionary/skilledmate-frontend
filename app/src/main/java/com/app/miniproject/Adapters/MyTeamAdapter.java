package com.app.miniproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Teams.MyTeamData;
import com.app.miniproject.Models.MyTeamsModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyTeamAdapter extends RecyclerView.Adapter<MyTeamAdapter.MyTeamViewHolder> {

    Context context;
    List<MyTeamData>myTeamDataList=new ArrayList<>();
    MyTeamEvents myTeamEvents;

    public MyTeamAdapter(Context context, MyTeamEvents myTeamEvents) {
        this.context = context;
        this.myTeamEvents = myTeamEvents;
    }

    public void setMyTeams(List<MyTeamData>myTeams){
        myTeamDataList=myTeams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.items_my_team,parent,false);
        return new MyTeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamViewHolder holder, int position) {
        final MyTeamData myTeamsModel=myTeamDataList.get(position);
        holder.txtProjectTitle.setText(myTeamsModel.getTeamTitle());
        holder.txtProjectRole.setText(myTeamsModel.getTeamTagline());
        holder.txtProjectDescription.setText(myTeamsModel.getRoleTitle());
        holder.txtDeadline.setText(myTeamsModel.getRole());
        Glide.with(context).load(myTeamsModel.getTeamIcon()).into(holder.imgProject);

        holder.relTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTeamEvents.onMyTeamClick(myTeamsModel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTeamDataList.size();
    }

    public class MyTeamViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProject;
        TextView txtProjectTitle,txtProjectRole,txtProjectDescription,txtDeadline;
        RelativeLayout relTeam;

        public MyTeamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProject=itemView.findViewById(R.id.imgProject);
            txtProjectTitle=itemView.findViewById(R.id.txtProjectTitle);
            txtProjectRole=itemView.findViewById(R.id.txtProjectRole);
            txtProjectDescription=itemView.findViewById(R.id.txtProjectDescription);
            txtDeadline=itemView.findViewById(R.id.txtDeadline);
            relTeam=itemView.findViewById(R.id.relTeam);
        }
    }

    public interface MyTeamEvents{
        void onMyTeamClick(Integer teamId);
    }
}

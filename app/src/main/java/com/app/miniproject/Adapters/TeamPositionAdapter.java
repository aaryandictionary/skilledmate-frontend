package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.R;

import java.util.ArrayList;
import java.util.List;

public class TeamPositionAdapter extends RecyclerView.Adapter<TeamPositionAdapter.TeamPositionViewHolder> {

    Context mCxt;
    List<String>teamPosition=new ArrayList<>();
    TeamPositionClickEvents teamPositionClickEvents;
    int callType=0;

    public TeamPositionAdapter(Context mCxt, TeamPositionClickEvents teamPositionClickEvents,int callType) {
        this.mCxt = mCxt;
        this.teamPositionClickEvents = teamPositionClickEvents;
        this.callType=callType;
    }

    public void setList(List<String>s){
        teamPosition=s;
        notifyDataSetChanged();
    }

    public void addTeamPosition(String s){
        teamPosition.add(s);
        notifyItemInserted(teamPosition.size());
    }


    @NonNull
    @Override
    public TeamPositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_team_position_skill,parent,false);
        return new TeamPositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamPositionViewHolder holder, final int position) {
        final String teamT=teamPosition.get(position);
        holder.txtTitle.setText(teamT);
        holder.imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamPositionClickEvents.onPositionEditClick(position,teamT,callType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamPosition.size();
    }

    public class TeamPositionViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        ImageButton imgBtnEdit;
        RelativeLayout relMain;
        public TeamPositionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            imgBtnEdit=itemView.findViewById(R.id.imgBtnEdit);
            relMain=itemView.findViewById(R.id.relMain);
        }
    }

    public interface TeamPositionClickEvents{
        void onPositionEditClick(int position,String title,int callType);
    }
}

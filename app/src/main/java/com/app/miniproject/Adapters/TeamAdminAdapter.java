package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Teams.TeamAdminData;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamAdminAdapter extends RecyclerView.Adapter<TeamAdminAdapter.TeamAdminViewHolder> {

    Context mCxt;
    List<TeamAdminData>teamAdminLists=new ArrayList<>();
    TeamAdminClickEvents teamAdminClickEvents;

    public TeamAdminAdapter(Context mCxt, TeamAdminClickEvents teamAdminClickEvents) {
        this.mCxt = mCxt;
        this.teamAdminClickEvents = teamAdminClickEvents;
    }

    public void setTeamAdmin(List<TeamAdminData>teamAdmin){
        teamAdminLists=teamAdmin;
        notifyDataSetChanged();
    }

    public void updateTeamAdmin(String role,String roleTitle,Integer position){
        teamAdminLists.get(position).setRole(role);
        teamAdminLists.get(position).setRoleTitle(roleTitle);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public TeamAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_team_admin,parent,false);
        return new TeamAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdminViewHolder holder, final int position) {
        final TeamAdminData teamAdminList=teamAdminLists.get(position);
        holder.txtName.setText(teamAdminList.getName());
        holder.txtAbout.setText(teamAdminList.getRoleTitle());
        holder.txtDesignation.setText(teamAdminList.getRole());

        Glide.with(mCxt).load(teamAdminList.getUserImage()).into(holder.circularImgUser);

        holder.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamAdminClickEvents.onTeamUserClick(teamAdminList.getId());
            }
        });

        holder.relMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                teamAdminClickEvents.onTeamLongClick(teamAdminList,position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamAdminLists.size();
    }

    public class TeamAdminViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularImgUser;
        TextView txtName,txtAbout,txtDesignation;
        RelativeLayout relMain;

        public TeamAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImgUser=itemView.findViewById(R.id.circularImgUser);
            txtName=itemView.findViewById(R.id.txtName);
            txtAbout=itemView.findViewById(R.id.txtAbout);
            txtDesignation=itemView.findViewById(R.id.txtDesignation);
            relMain=itemView.findViewById(R.id.relMain);
        }
    }

    public interface TeamAdminClickEvents{
        void onTeamLongClick(TeamAdminData teamAdminData,Integer position);
        void onTeamUserClick(Integer userId);
    }
}

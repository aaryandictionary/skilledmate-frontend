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

import com.app.miniproject.Database.Teams.UserTeamData;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserTeamAdapter extends RecyclerView.Adapter<UserTeamAdapter.UserTeamViewHolder> {

    Context mCxt;
    List<UserTeamData>userTeamDataList=new ArrayList<>();
    UserTeamClickEvents userTeamClickEvents;
    Integer[]overlays={R.drawable.bg_circle_black,R.drawable.bg_circle_blue,R.drawable.bg_circle_green,R.drawable.bg_circle_light_blue,R.drawable.bg_circle_pink,R.drawable.bg_circle_red,R.drawable.bg_circle_white,R.drawable.bg_circle_yellow};


    public UserTeamAdapter(Context mCxt, UserTeamClickEvents userTeamClickEvents) {
        this.mCxt = mCxt;
        this.userTeamClickEvents = userTeamClickEvents;
    }

    public void setUserTeamList(List<UserTeamData>userTeamList){
        userTeamDataList=userTeamList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_project,parent,false);
        return new UserTeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTeamViewHolder holder, int position) {
        final UserTeamData userTeamData=userTeamDataList.get(position);
        holder.txtProjectName.setText(userTeamData.getTeam_title());
        if (userTeamData.getTeam_icon()!=null)
            Glide.with(mCxt).load(userTeamData.getTeam_icon()).into(holder.circularProjectImage);

        holder.relUserTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTeamClickEvents.onUserTeamClick(userTeamData.getId());
            }
        });

        int rnd=new Random().nextInt(overlays.length);
        Glide.with(mCxt).load(overlays[rnd]).into(holder.imgProjectOverlay);
    }

    @Override
    public int getItemCount() {
        return userTeamDataList.size();
    }

    public class UserTeamViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularProjectImage;
        TextView txtProjectName;
        ImageView imgProjectOverlay;
        RelativeLayout relUserTeam;

        public UserTeamViewHolder(@NonNull View itemView) {
            super(itemView);
            circularProjectImage=itemView.findViewById(R.id.circularProjectImage);
            txtProjectName=itemView.findViewById(R.id.txtProjectName);
            imgProjectOverlay=itemView.findViewById(R.id.imgProjectOverlay);
            relUserTeam=itemView.findViewById(R.id.relUserTeam);
        }
    }

    public interface UserTeamClickEvents{
        void onUserTeamClick(Integer teamId);
    }
}

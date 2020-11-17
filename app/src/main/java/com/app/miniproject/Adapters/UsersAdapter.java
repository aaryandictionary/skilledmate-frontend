package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Models.UserModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    Context mCxt;
    List<UserModel> userModelList=new ArrayList<>();
    UserClickEvents userClickEvents;

    public UsersAdapter(Context mCxt, UserClickEvents userClickEvents) {
        this.mCxt = mCxt;
        this.userClickEvents = userClickEvents;
    }

    public void setUsers(List<UserModel>users){
        userModelList=users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_user_post,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel userModel=userModelList.get(position);
        holder.txtUserName.setText(userModel.getUserName());
        holder.txtUserSkills.setText(userModel.getUserDescription());
        Glide.with(mCxt).load(userModel.getUserImage()).into(holder.imgUser);
        holder.relSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickEvents.onSendMessageClick();
            }
        });

        holder.cardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClickEvents.onUserClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUser;
        CardView cardUser;
        TextView txtUserName,txtUserSkills;
        RelativeLayout relSendMessage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser=itemView.findViewById(R.id.imgUser);
            cardUser=itemView.findViewById(R.id.cardUser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtUserSkills=itemView.findViewById(R.id.txtUserSkills);
            relSendMessage=itemView.findViewById(R.id.relSendMessage);
        }
    }

    public interface UserClickEvents{
        void onUserClick();
        void onSendMessageClick();
    }
}

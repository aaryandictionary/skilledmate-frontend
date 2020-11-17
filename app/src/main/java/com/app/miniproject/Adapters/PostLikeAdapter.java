package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.PostLike.PostLikeListData;
import com.app.miniproject.Models.PostLikeModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostLikeAdapter extends RecyclerView.Adapter<PostLikeAdapter.PostLikeViewHolder> {

    Context mCxt;
    List<PostLikeListData>postLikeModels=new ArrayList<>();
    PostLikeClickEvents postLikeClickEvents;
    Integer myId;

    public PostLikeAdapter(Context mCxt, PostLikeClickEvents postLikeClickEvents,Integer myId) {
        this.mCxt = mCxt;
        this.postLikeClickEvents = postLikeClickEvents;
        this.myId=myId;
    }

    public void setPostLike(List<PostLikeListData>postLike){
        postLikeModels=postLike;
        notifyDataSetChanged();
    }

    public void insertPostLike(PostLikeListData postLikeModel){
        postLikeModels.add(0,postLikeModel);
        notifyItemInserted(0);
    }


    @NonNull
    @Override
    public PostLikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_post_like,parent,false);
        return new PostLikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostLikeViewHolder holder, int position) {
        final PostLikeListData postLikeModel=postLikeModels.get(position);
        holder.txtUserName.setText(postLikeModel.getName());
        holder.txtUserSkills.setText(postLikeModel.getCollege_name());
        Glide.with(mCxt).load(postLikeModel.getUser_image()).into(holder.circularImgUser);

        holder.relBtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLikeClickEvents.onMessageClick(postLikeModel.getId(),postLikeModel.getName(),postLikeModel.getUser_image());
            }
        });

        holder.relUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLikeClickEvents.onUserClick(postLikeModel.getId());
            }
        });

        if (postLikeModel.getId()==myId){
            holder.relBtnMessage.setVisibility(View.GONE);
        }else {
            holder.relBtnMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return postLikeModels.size();
    }

    public class PostLikeViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularImgUser;
        TextView txtUserName,txtUserSkills;
        RelativeLayout relUser,relBtnMessage;

        public PostLikeViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImgUser=itemView.findViewById(R.id.circularImgUser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtUserSkills=itemView.findViewById(R.id.txtUserSkills);
            relBtnMessage=itemView.findViewById(R.id.relBtnMessage);
            relUser=itemView.findViewById(R.id.relUser);
        }
    }

    public interface PostLikeClickEvents{
        void onUserClick(Integer userId);
        void onMessageClick(Integer userId,String name,String image);
    }
}

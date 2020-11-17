package com.app.miniproject.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Models.ExpertiseModel;
import com.app.miniproject.Models.HomeModel;
import com.app.miniproject.Models.PostModel;
import com.app.miniproject.Models.UserModel;
import com.app.miniproject.R;
import com.app.miniproject.Utils.SquareImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter implements ExpertiseAdapter.ExpertiseClickEvents,UsersAdapter.UserClickEvents{

    Context mCxt;
    List<HomeModel>homeModelList=new ArrayList<>();
    PostClickEvent postClickEvent;

    public HomeAdapter(Context mCxt, PostClickEvent postClickEvent) {
        this.mCxt = mCxt;
        this.postClickEvent = postClickEvent;
    }

    public void setHomeList(List<HomeModel>homeList){
        homeModelList=homeList;
        notifyDataSetChanged();
    }

    public void addHomeList(List<HomeModel>homeModels){
        int preSize=homeModelList.size();
        homeModelList.addAll(homeModels);
        notifyItemRangeInserted(preSize,homeModels.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view= LayoutInflater.from(mCxt).inflate(R.layout.items_post,parent,false);
            return new PostViewHolder(view);
        }else if (viewType==1){
            View view=LayoutInflater.from(mCxt).inflate(R.layout.items_expertise_holer,parent,false);
            return new ExpertiseViewHolder(view);
        }else if (viewType==2){
            View view=LayoutInflater.from(mCxt).inflate(R.layout.items_user_holder,parent,false);
            return new UsersViewHolder(view);
        }else {
            View view=LayoutInflater.from(mCxt).inflate(R.layout.items_team_post,parent,false);
            return new TeamsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeModel homeModel=homeModelList.get(position);
        if (getItemViewType(position)==0){
            PostModel postModel=homeModel.getPost();
            Glide.with(mCxt).load(postModel.getUserImage()).into(((PostViewHolder)holder).img_user);
            ((PostViewHolder)holder).txtName.setText(postModel.getUserName());
            ((PostViewHolder)holder).txtPostTime.setText(postModel.getPostTime());
            ((PostViewHolder)holder).txtCollegeName.setText(postModel.getUserCollege());
            ((PostViewHolder)holder).txtPostText.setText(postModel.getPostContent());
            ((PostViewHolder)holder).txtLiked.setText(postModel.getPostLike());
            ((PostViewHolder)holder).txtLike.setText(postModel.getLikeCount()+" Likes");
            ((PostViewHolder)holder).txtComment.setText(postModel.getCommentCount()+" Comments");
            /*if (TextUtils.isEmpty(postModel.getPostImage())){
                ((PostViewHolder)holder).imgPost.setVisibility(View.GONE);
            }else {
                ((PostViewHolder)holder).imgPost.setVisibility(View.VISIBLE);*/
                Glide.with(mCxt).load(postModel.getPostImage()).into(((PostViewHolder)holder).imgPost);
//            }
            ((PostViewHolder)holder).relPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickEvent.onPostClick();
                }
            });
            ((PostViewHolder)holder).relUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickEvent.onUserClick();
                }
            });
            ((PostViewHolder)holder).linLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickEvent.onLikeClick();
                }
            });
            ((PostViewHolder)holder).linComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickEvent.onCommentClick();
                }
            });
            ((PostViewHolder)holder).linShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postClickEvent.onShareClick();
                }
            });
        }else if (getItemViewType(position)==1){
            List<ExpertiseModel> expertiseModels=homeModel.getExpertise();
            ExpertiseAdapter expertiseAdapter=new ExpertiseAdapter(mCxt,this);
            ((ExpertiseViewHolder)holder).recyclerExpertise.setAdapter(expertiseAdapter);
            expertiseAdapter.setExpertiseList(expertiseModels);
        }else if (getItemViewType(position)==2){
            List<UserModel>userModelList=homeModel.getUsers();
            UsersAdapter usersAdapter=new UsersAdapter(mCxt,this);
            ((UsersViewHolder)holder).recyclerUsers.setAdapter(usersAdapter);
            usersAdapter.setUsers(userModelList);
        }else {

        }

    }

    @Override
    public int getItemCount() {
        return homeModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return homeModelList.get(position).getType();
    }

    @Override
    public void onExpertiseClick() {
        postClickEvent.onExpertiseClick();
    }

    @Override
    public void onUserClick() {
        postClickEvent.onUserCardClick();
    }

    @Override
    public void onSendMessageClick() {
        postClickEvent.onSendMessageClick();
    }

    public class TeamsViewHolder extends RecyclerView.ViewHolder{

        public TeamsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerUsers;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerUsers=itemView.findViewById(R.id.recyclerUsers);
            recyclerUsers.setHasFixedSize(true);
            recyclerUsers.setLayoutManager(new LinearLayoutManager(mCxt,LinearLayoutManager.HORIZONTAL,false));
        }
    }

    public class ExpertiseViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerExpertise;
        public ExpertiseViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerExpertise=itemView.findViewById(R.id.recyclerExpertise);
            recyclerExpertise.setHasFixedSize(true);
            recyclerExpertise.setLayoutManager(new LinearLayoutManager(mCxt,LinearLayoutManager.HORIZONTAL,false));
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img_user;
        TextView txtName,txtCollegeName,txtPostTime,txtPostText;
        SquareImageView imgPost;
        TextView txtLiked,txtLike,txtComment,txtShare;
        LinearLayout linLike,linComment,linShare;

        RelativeLayout relUser,relPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user=itemView.findViewById(R.id.img_user);
            txtName=itemView.findViewById(R.id.txtName);
            txtCollegeName=itemView.findViewById(R.id.txtCollegeName);
            imgPost=itemView.findViewById(R.id.imgPost);
            txtPostText=itemView.findViewById(R.id.txtPostText);
            txtPostTime=itemView.findViewById(R.id.txtPostTime);
            txtLiked=itemView.findViewById(R.id.txtLiked);
            txtLike=itemView.findViewById(R.id.txtLike);
            txtComment=itemView.findViewById(R.id.txtComment);
            txtShare=itemView.findViewById(R.id.txtShare);
            linComment=itemView.findViewById(R.id.linComment);
            linLike=itemView.findViewById(R.id.linLike);
            linShare=itemView.findViewById(R.id.linShare);

            relUser=itemView.findViewById(R.id.relUser);
            relPost=itemView.findViewById(R.id.relPost);

        }
    }
    public interface PostClickEvent{
        void onLikeClick();
        void onCommentClick();
        void onShareClick();
        void onUserClick();
        void onPostClick();

        void onExpertiseClick();

        void onUserCardClick();
        void onSendMessageClick();
    }
}

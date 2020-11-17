package com.app.miniproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.PostComment.PostCommentListData;
import com.app.miniproject.Models.PostCommentModel;
import com.app.miniproject.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.PostCommentViewHolder> {

    Context mCxt;
    List<PostCommentListData> postCommentListDataList=new ArrayList<>();

    public PostCommentAdapter(Context mCxt) {
        this.mCxt = mCxt;
    }

    public void setPostCommentList(List<PostCommentListData>postCommentList){
        postCommentListDataList=postCommentList;
        notifyDataSetChanged();
    }

    public void insetCommentAtTop(PostCommentListData postCommentModel){
        postCommentListDataList.add(0,postCommentModel);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_post_comment,parent,false);
        return new PostCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentViewHolder holder, int position) {
        PostCommentListData postCommentModel=postCommentListDataList.get(position);
        holder.txtUserName.setText(postCommentModel.getName());
        holder.txtComment.setText(postCommentModel.getComment());
//        holder.txtCommentTime.setText(postCommentModel.get());
        Glide.with(mCxt).load(postCommentModel.getUser_image()).into(holder.circularImgUser);
    }

    @Override
    public int getItemCount() {
        return postCommentListDataList.size();
    }

    public class PostCommentViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularImgUser;
        TextView txtUserName,txtComment,txtCommentTime;
        public PostCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImgUser=itemView.findViewById(R.id.circularImgUser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtComment=itemView.findViewById(R.id.txtComment);
            txtCommentTime=itemView.findViewById(R.id.txtCommentTime);

        }
    }
}

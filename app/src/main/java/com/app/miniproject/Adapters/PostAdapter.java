package com.app.miniproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Post.PostData;
import com.app.miniproject.Database.Post.PostUserData;
import com.app.miniproject.R;
import com.app.miniproject.Utils.SquareImageView;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context mCxt;
    List<PostData>postDataList=new ArrayList<>();
    PostClickEvents postClickEvents;
    Integer type=0;

    public PostAdapter(Context mCxt, PostClickEvents postClickEvents,Integer type) {
        this.mCxt = mCxt;
        this.postClickEvents = postClickEvents;
        this.type=type;
    }

    public void addPostToList(List<PostData>postList){
        postDataList=postList;
        notifyDataSetChanged();
    }

    public void swapLike(Integer position){
        if (TextUtils.equals(postDataList.get(position).getIsLiked(),"true")){
            postDataList.get(position).setIsLiked("false");
            postDataList.get(position).setLikesCount(postDataList.get(position).getLikesCount()-1);
        }else {
            postDataList.get(position).setIsLiked("true");
            postDataList.get(position).setLikesCount(postDataList.get(position).getLikesCount()+1);
        }
        notifyItemChanged(position);
    }

    public void updatePostItem(Integer position,PostData postData){
        postDataList.set(position,postData);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        final PostData postData=postDataList.get(position);
        PostUserData postUserData=postData.getUser();

        if (TextUtils.equals(postData.getPostType(),"USERPOST")||type==1){
            holder.txtName.setText(postUserData.getName());
            if (postUserData.getUserImage()!=null)
                Glide.with(mCxt).load(postUserData.getUserImage()).into(holder.img_user);
            if (type==0)
            holder.txtCollegeName.setText(postUserData.getCollegeName());
            else holder.txtCollegeName.setText(postData.getRole_title());
        }else {
            if (postData.getTeam()!=null){
                holder.txtName.setText(postData.getTeam().getTeam_title());
                if (postData.getTeam().getTeam_icon()!=null&&!TextUtils.isEmpty(postData.getTeam().getTeam_icon()))
                    Glide.with(mCxt).load(postData.getTeam().getTeam_icon()).into(holder.img_user);
                holder.txtCollegeName.setText(postData.getTeam().getTeam_tagline());
            }
        }


        holder.txtPostText.setText(postData.getPostContent());
        if (postData.getPostImage()!=null){
            Glide.with(mCxt).load(postData.getPostImage()).into(holder.imgPost);
            holder.imgPost.setVisibility(View.VISIBLE);
        } else{
           holder.imgPost.setVisibility(View.GONE);
        }
        holder.txtPostTime.setText(getTimeStamp(postData.getUpdatedAt()));
        if (postData.getLikesCount()==0){
            holder.txtLike.setText("Like");
        }else if (postData.getLikesCount()==1){
            holder.txtLike.setText(postData.getLikesCount()+" Like");
        }else {
            holder.txtLike.setText(postData.getLikesCount()+" Likes");
        }
        if (postData.getCommentsCount()==0){
            holder.txtComment.setText("Comment");
        }else if (postData.getCommentsCount()==1){
            holder.txtComment.setText(postData.getCommentsCount()+" Comment");
        }else {
            holder.txtComment.setText(postData.getCommentsCount()+" Comments");
        }

        if (TextUtils.equals(postData.getIsLiked(),"true")){
            holder.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_enabled,0,0,0);
        }else {
            holder.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24,0,0,0);
        }

        if (TextUtils.equals(postData.getPostType(),"TEAMEVENT")){
            holder.txtViewDetails.setVisibility(View.VISIBLE);
        }else {
            holder.txtViewDetails.setVisibility(View.GONE);
        }

        holder.relUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(postData.getPostType(),"USERPOST")||type==1)
                postClickEvents.onUserClick(postData.getUserId());
                else postClickEvents.onTeamClick(postData.getTeam().getId());
            }
        });

        holder.relPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickEvents.onPostClick(postData);
            }
        });

        holder.linLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickEvents.onLikeClick(postData.getId(),position);
            }
        });

        holder.linComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickEvents.onCommentClick(postData);
            }
        });

        holder.linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickEvents.onShareClick(postData.getId());
            }
        });

        holder.txtViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickEvents.onEventDetailsClick(postData.getEventId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return postDataList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_user;
        TextView txtName,txtCollegeName,txtPostTime,txtPostText;
        ImageView imgPost;
        TextView txtLiked,txtLike,txtComment,txtShare,txtViewDetails;
        LinearLayout linLike,linComment,linShare;

        RelativeLayout relUser,relPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtCollegeName=itemView.findViewById(R.id.txtCollegeName);
            img_user=itemView.findViewById(R.id.img_user);
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
            txtViewDetails=itemView.findViewById(R.id.txtViewDetails);
        }
    }

    private String getTimeStamp(String  dateCreated){
        int difference = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        Date today = calendar.getTime();
        sdf.format(today);
        Date timeStamp = today;
        final String photoTimeStamp = dateCreated;
        try{
            timeStamp = sdf.parse(photoTimeStamp);
            //Log.d(TAG, "getTimeStamp: timestamp " + timeStamp);
            difference = Math.round((today.getTime() - timeStamp.getTime())/1000 / 60);

        } catch (ParseException e){

            difference = 0;
        }
        if(difference < 1){
            return "Just Now";
        } else if(difference <= 59) {
            return (difference + " mins ago");
        } else if(difference >= 60 && difference < 1440) {
            return ((Math.round(difference / 60)) + " hours ago");
        } else  {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yy 'at' K:mm a");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        }
    }


    public interface PostClickEvents{
        void onLikeClick(Integer postId,Integer position);
        void onCommentClick(PostData postData);
        void onShareClick(Integer postId);
        void onUserClick(Integer userId);
        void onPostClick(PostData postData);
        void onTeamClick(Integer teamId);
        void onEventDetailsClick(Integer eventId);
    }
}

package com.app.miniproject.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Message.MessageData;
import com.app.miniproject.Database.Message.MessageModel;
import com.app.miniproject.R;
import com.app.miniproject.SqliteDb.Entity.SqliteMessageData;
import com.bumptech.glide.Glide;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter {

    Context mCxt;
    List<MessageData>messageModelList=new ArrayList<>();
    long userId;

    public MessageAdapter(Context mCxt,long id) {
        this.mCxt = mCxt;
        userId=id;
    }

    public void addMessageToTop(List<MessageData> messageModel){
        int pos=messageModelList.size();
        messageModelList.addAll(pos,messageModel);
        notifyItemRangeChanged(pos,messageModel.size());
    }

    public void addMessageToBottom(MessageData messageData){
        int size=messageModelList.size();
        messageModelList.add(0,messageData);
        notifyItemInserted(0);
//        notifyItemRangeChanged(0,size);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType!=-1){
            if (viewType==0){
                View view= LayoutInflater.from(mCxt).inflate(R.layout.items_text_message_sent,parent,false);
                return new MessageTextSentViewHolder(view);
            }else if (viewType==1){
                View view=LayoutInflater.from(mCxt).inflate(R.layout.items_image_message_sent,parent,false);
                return new MessageImageSentViewHolder(view);
            }else if (viewType==2){
                View view=LayoutInflater.from(mCxt).inflate(R.layout.items_text_message_received,parent,false);
                return new MessageTextReceivedViewHolder(view);
            }else{
                View view=LayoutInflater.from(mCxt).inflate(R.layout.items_image_message_received,parent,false);
                return new MessageImageReceivedViewHolder(view);
            }
        }else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageData messageModel=messageModelList.get(position);
        if (holder.getItemViewType()==0){
            ((MessageTextSentViewHolder)holder).txtName.setText(messageModel.getSenderName());
//            ((MessageTextSentViewHolder)holder).txtGroupName.setText(messageModel.get());
            ((MessageTextSentViewHolder)holder).txtMessage.setText(messageModel.getTextMsg());
            ((MessageTextSentViewHolder)holder).txtTime.setText(getTimeStamp(messageModel.getCreatedAt()));
        }else if (holder.getItemViewType()==2){
            ((MessageTextReceivedViewHolder)holder).txtName.setText(messageModel.getSenderName());
//            ((MessageTextReceivedViewHolder)holder).txtGroupName.setText(messageModel.getGroupName());
            ((MessageTextReceivedViewHolder)holder).txtMessage.setText(messageModel.getTextMsg());
            ((MessageTextReceivedViewHolder)holder).txtTime.setText(getTimeStamp(messageModel.getCreatedAt()));
        }else if (holder.getItemViewType()==1){
            ((MessageImageSentViewHolder)holder).txtTime.setText(getTimeStamp(messageModel.getCreatedAt()));
//            ((MessageImageSentViewHolder)holder).txtGroupName.setText(messageModel.getGroupName());
            ((MessageImageSentViewHolder)holder).txtName.setText(messageModel.getSenderName());
            Glide.with(mCxt).load(messageModel.getContent()).into(((MessageImageSentViewHolder)holder).imgPhoto);
            if (!TextUtils.isEmpty(messageModel.getTextMsg())){
                ((MessageImageSentViewHolder)holder).txtMessage.setVisibility(View.VISIBLE);
                ((MessageImageSentViewHolder)holder).txtMessage.setText(messageModel.getTextMsg());
            }else{
                ((MessageImageSentViewHolder)holder).txtMessage.setVisibility(View.GONE);
            }
        }else if (holder.getItemViewType()==3){
            ((MessageImageReceivedViewHolder)holder).txtTime.setText(getTimeStamp(messageModel.getCreatedAt()));
//            ((MessageImageReceivedViewHolder)holder).txtGroupName.setText(messageModel.getGroupName());
            ((MessageImageReceivedViewHolder)holder).txtName.setText(messageModel.getSenderName());
            Glide.with(mCxt).load(messageModel.getContent()).into(((MessageImageReceivedViewHolder)holder).imgPhoto);
            if (!TextUtils.isEmpty(messageModel.getTextMsg())){
                ((MessageImageReceivedViewHolder)holder).txtMessage.setVisibility(View.VISIBLE);
                ((MessageImageReceivedViewHolder)holder).txtMessage.setText(messageModel.getTextMsg());
            }else{
                ((MessageImageReceivedViewHolder)holder).txtMessage.setVisibility(View.GONE);
            }
        }
    }

    private String getTimeStamp(String  dateCreated){
        int difference = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
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
        if(difference >= 0 && difference < 1440) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("K:mm a");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        } else if (difference>=1440&&difference<525600){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("K:mm a dd MMM");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("K:mm a dd MMM\'yy");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageData messageModel=messageModelList.get(position);
        if (messageModel!=null){
            if (messageModel.getSenderId()==userId){
                if (TextUtils.equals(messageModel.getContentType(),"TEXT")){
                    return 0;
                }else if (TextUtils.equals(messageModel.getContentType(),"IMAGE")){
                    return 1;
                }else{
                    return -1;
                }
            }else{
                if (TextUtils.equals(messageModel.getContentType(),"TEXT")){
                    return 2;
                }else if (TextUtils.equals(messageModel.getContentType(),"IMAGE")){
                    return 3;
                }else{
                    return -1;
                }
            }
        }else{
            return -1;
        }
    }

    public class MessageTextSentViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtTime,txtMessage,txtGroupName;
        RelativeLayout relMessage;

        public MessageTextSentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtTime=itemView.findViewById(R.id.txtTime);
            txtMessage=itemView.findViewById(R.id.txtMessage);
            txtGroupName=itemView.findViewById(R.id.txtGroupName);
            relMessage=itemView.findViewById(R.id.relMessage);
        }
    }

    public class MessageTextReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtTime,txtMessage,txtGroupName;
        RelativeLayout relMessage;

        public MessageTextReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtTime=itemView.findViewById(R.id.txtTime);
            txtMessage=itemView.findViewById(R.id.txtMessage);
            txtGroupName=itemView.findViewById(R.id.txtGroupName);
            relMessage=itemView.findViewById(R.id.relMessage);
        }
    }

    public class MessageImageSentViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtTime,txtGroupName,txtMessage;
        ImageView imgPhoto;
        RelativeLayout relMessage;

        public MessageImageSentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtTime=itemView.findViewById(R.id.txtTime);
            imgPhoto=itemView.findViewById(R.id.imgPhoto);
            txtGroupName=itemView.findViewById(R.id.txtGroupName);
            relMessage=itemView.findViewById(R.id.relMessage);
            txtMessage=itemView.findViewById(R.id.txtMessage);
        }
    }

    public class MessageImageReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtTime,txtGroupName,txtMessage;
        ImageView imgPhoto;
        RelativeLayout relMessage;

        public MessageImageReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtTime=itemView.findViewById(R.id.txtTime);
            imgPhoto=itemView.findViewById(R.id.imgPhoto);
            txtGroupName=itemView.findViewById(R.id.txtGroupName);
            relMessage=itemView.findViewById(R.id.relMessage);
            txtMessage=itemView.findViewById(R.id.txtMessage);
        }
    }
}

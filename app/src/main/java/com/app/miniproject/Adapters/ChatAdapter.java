package com.app.miniproject.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Conversation.ConversationData;
import com.app.miniproject.Models.ChatModel;
import com.app.miniproject.R;
import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    Context context;
    List<ConversationData>chatModelList=new ArrayList<>();
    ChatEvents chatEvents;

    public ChatAdapter(Context context, ChatEvents chatEvents) {
        this.context = context;
        this.chatEvents = chatEvents;
    }

    public void setChatList(List<ConversationData>chatList){
        chatModelList=chatList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.items_user_messages,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final ConversationData chatModel=chatModelList.get(position);
        holder.txtUserName.setText(chatModel.getConvTitle());
        if (chatModel.getConversationLastMessageData()!=null){
            if (TextUtils.equals(chatModel.getConversationLastMessageData().getContentType(),"TEXT"))
            holder.txtLastMessage.setText(chatModel.getConversationLastMessageData().getTextMsg());
            else holder.txtLastMessage.setText(chatModel.getConversationLastMessageData().getContentType().charAt(0)+chatModel.getConversationLastMessageData().getContentType().toLowerCase().substring(1));
            holder.txtLastSeen.setText(getTimeStamp(chatModel.getConversationLastMessageData().getCreatedAt()));
        }
        Glide.with(context).load(chatModel.getConvIcon()).into(holder.circularImgUser);

        holder.relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatEvents.onChatClick(chatModel);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circularImgUser;
        TextView txtUserName,txtLastMessage,txtLastSeen;
        RelativeLayout relMain;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            circularImgUser=itemView.findViewById(R.id.circularImgUser);
            txtUserName=itemView.findViewById(R.id.txtUserName);
            txtLastMessage=itemView.findViewById(R.id.txtLastMessage);
            txtLastSeen=itemView.findViewById(R.id.txtLastSeen);
            relMain=itemView.findViewById(R.id.relMain);
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
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
        }
    }


    public interface ChatEvents{
        void onChatClick(ConversationData conversationData);
    }
}

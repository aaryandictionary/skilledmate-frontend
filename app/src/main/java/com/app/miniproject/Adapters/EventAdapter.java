package com.app.miniproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.miniproject.Database.Event.EventListData;
import com.app.miniproject.R;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHoler> {

    Context mCxt;
    List<EventListData>eventListDataList=new ArrayList<>();
    EventClickEvent eventClickEvent;

    public EventAdapter(Context mCxt, EventClickEvent eventClickEvent) {
        this.mCxt = mCxt;
        this.eventClickEvent = eventClickEvent;
    }

    public void setEventList(List<EventListData>eventList){
        eventListDataList=eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mCxt).inflate(R.layout.items_upcoming_event,parent,false);
        return new EventViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHoler holder, int position) {
        final EventListData eventListData=eventListDataList.get(position);
        holder.txtEventName.setText(eventListData.getEvent_title());
        holder.txtTeamName.setText(eventListData.getTeam_title());
        holder.txtEventTime.setText(eventListData.getEvent_time());

        if (eventListData.getEvent_image()!=null)
            Glide.with(mCxt).load(eventListData.getEvent_image()).into(holder.circularImgEvent);

        holder.cardEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventClickEvent.onEventClick(eventListData.getId(),eventListData.getTeam_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventListDataList.size();
    }

    public class EventViewHoler extends RecyclerView.ViewHolder{
        CircleImageView circularImgEvent;
        TextView txtTeamName,txtEventTime,txtEventName;
        CardView cardEvent;

        public EventViewHoler(@NonNull View itemView) {
            super(itemView);
            circularImgEvent=itemView.findViewById(R.id.circularImgEvent);
            txtTeamName=itemView.findViewById(R.id.txtTeamName);
            txtEventTime=itemView.findViewById(R.id.txtEventTime);
            txtEventName=itemView.findViewById(R.id.txtEventName);
            cardEvent=itemView.findViewById(R.id.cardEvent);
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM\'yy 'at' K:mm a");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;
    }

    public interface EventClickEvent{
        void onEventClick(Integer eventId, Integer teamId);
    }
}

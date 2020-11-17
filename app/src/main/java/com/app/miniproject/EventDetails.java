package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Event.ApplyEventModel;
import com.app.miniproject.Database.Event.ApplyEventResponse;
import com.app.miniproject.Database.Event.EventDetailsData;
import com.app.miniproject.Database.Event.EventDetailsResponse;
import com.app.miniproject.Database.Event.EventTeamData;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetails extends AppCompatActivity {

    CircleImageView imgTeam;
    TextView txtTeamName,txtTeamTagline,txtPostTime;
    ImageView imgEvent;
    TextView txtEventName,txtParticipants,txtEventTime,txtEventDeadline,txtEventTitle,txtEventDescription;
    RelativeLayout relApplyEvent,relTeam,relaed;
    ImageButton imgBtnEdit,imgBtnShare;

    ContentLoadingProgressBar progress;

    int eventId,teamId=-1;

    DataProcessor dataProcessor;

    boolean isAdmin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        initializeView();

        eventId=getIntent().getIntExtra("eventId",-1);
        dataProcessor=DataProcessor.getInstance(this);

        relaed.setVisibility(View.GONE);

        getEventDetails();

        relApplyEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyEvent();
            }
        });

        txtParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdmin){
                    Intent intent=new Intent(EventDetails.this,UserList.class);
                    intent.putExtra("type",2);
                    intent.putExtra("eventId",eventId);
                    intent.putExtra("teamId",teamId);
                    startActivity(intent);
                }
            }
        });

        relTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EventDetails.this,TeamDetails.class);
                intent.putExtra("teamId",teamId);
                startActivity(intent);
            }
        });

        imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EventDetails.this,CreateEvent.class);
                intent.putExtra("type","EDIT");
                intent.putExtra("eventId",eventId);
                startActivity(intent);
            }
        });

        imgBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             createLink("EVENT",String.valueOf(eventId));
            }
        });
    }


    private void createLink(String type,String id){
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("mpgcet.page.link"))
                .setDomainUriPrefix("kvgames.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        String shareLinkText="https://mpgcet.page.link/?" +
                "link=https://www.kvgames.com/".concat(type).concat("/").concat(id)+
                "&apn="+this.getPackageName() +
                "&st=KV Games" +
                "&sd=Download App and Get Rs 5" +
                "&si=http://kvgames.com/logo.jpg";

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinkText))
                .buildShortDynamicLink()
                .addOnCompleteListener(EventDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        }
                    }
                });
    }

    private void applyEvent(){
        ApplyEventModel applyEventModel=new ApplyEventModel();
        applyEventModel.setEvent_id(eventId);
        applyEventModel.setUser_id(dataProcessor.getInteger(DataProcessor.USER_ID));

        TeamInterface teamInterface=Database.getClient(this).create(TeamInterface.class);
        teamInterface.applyEvent(applyEventModel).enqueue(new Callback<ApplyEventResponse>() {
            @Override
            public void onResponse(Call<ApplyEventResponse> call, Response<ApplyEventResponse> response) {
                if (response.isSuccessful()){
                    relApplyEvent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApplyEventResponse> call, Throwable t) {

            }
        });
    }

    private void getEventDetails(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);
        teamInterface.getEventDetails(eventId,dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                if (response.isSuccessful()){
                    setData(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {

            }
        });
    }

    private void setData(EventDetailsData eventDetailsData){
        EventTeamData eventTeamData=eventDetailsData.getEventTeamData();
        txtTeamName.setText(eventTeamData.getTeamTitle());
        txtTeamTagline.setText(eventTeamData.getTeamTagline());
        Glide.with(this).load(eventTeamData.getTeamIcon()).into(imgTeam);
        txtPostTime.setText(getTimeStamp(eventDetailsData.getUpdated_at()));

        if (eventDetailsData.getUserId()==dataProcessor.getInteger(DataProcessor.USER_ID)){
            imgBtnEdit.setVisibility(View.VISIBLE);
        }else {
            imgBtnEdit.setVisibility(View.GONE);
        }

        txtEventTitle.setText(eventDetailsData.getEventTitle());
        txtEventDescription.setText(eventDetailsData.getEventDetails());
        txtEventDeadline.setText(getTimeStampEvent(eventDetailsData.getEventDeadline()));
        txtEventTime.setText(eventDetailsData.getEventTime());
        if (eventDetailsData.getParticipants_count()==0){
            txtParticipants.setText("Participant");
        }else if (eventDetailsData.getParticipants_count()==1){
            txtParticipants.setText(eventDetailsData.getParticipants_count()+" Participant");
        }else {
            txtParticipants.setText(eventDetailsData.getParticipants_count()+" Participants");
        }
        txtEventName.setText(eventDetailsData.getEventTitle());
        Glide.with(this).load(eventDetailsData.getEventImage()).into(imgEvent);

        if (TextUtils.equals(eventDetailsData.getMy_event(),"true")){
            relApplyEvent.setVisibility(View.GONE);
        }else {
            relApplyEvent.setVisibility(View.VISIBLE);
        }

        if (TextUtils.equals(eventDetailsData.getRole(),"false")){
            isAdmin=false;
        }else {
            isAdmin=true;
        }
        teamId=eventDetailsData.getTeamId();

        relaed.setVisibility(View.VISIBLE);
        progress.hide();
    }


    private String getTimeStamp(String  dateCreated){
        int difference = 0;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
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

    private String getTimeStampEvent(String  dateCreated){
        int difference = 0;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getDefault());
        Date today = calendar.getTime();
        sdf.format(today);

        Date timeStamp = today;
        final String photoTimeStamp = dateCreated;
        try{
            timeStamp = sdf.parse(photoTimeStamp);
            //Log.d(TAG, "getTimeStamp: timestamp " + timeStamp);
            difference = Math.round((timeStamp.getTime()-today.getTime())/1000 / 60);


        } catch (ParseException e){

            difference = 0;

        }
        if(timeStamp.compareTo(today)>0){
            if(difference < 1){
                return "Few Seconds left";
            } else if(difference <= 59) {
                return (difference + " mins left");
            } else if(difference >= 60 && difference < 1440) {
                return ((difference / 60)+":"+(difference%60) + " hours left");
            } else  {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM\'yy 'till' K:mm a");
                String res = simpleDateFormat.format(timeStamp.getTime());
                return res;
            }
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM 'at' K:mm a");
            String res = simpleDateFormat.format(timeStamp.getTime());
            return res;

        }

    }


    private void initializeView(){
        imgTeam=findViewById(R.id.imgTeam);
        txtTeamName=findViewById(R.id.txtTeamName);
        txtTeamTagline=findViewById(R.id.txtTeamTagline);
        txtPostTime=findViewById(R.id.txtPostTime);
        imgEvent=findViewById(R.id.imgEvent);
        txtEventName=findViewById(R.id.txtEventName);
        txtParticipants=findViewById(R.id.txtParticipants);
        txtEventTime=findViewById(R.id.txtEventTime);
        txtEventDeadline=findViewById(R.id.txtEventDeadline);
        txtEventTitle=findViewById(R.id.txtEventTitle);
        txtEventDescription=findViewById(R.id.txtEventDescription);
        relApplyEvent=findViewById(R.id.relApplyEvent);
        relTeam=findViewById(R.id.relTeam);
        relaed=findViewById(R.id.relaed);
        progress=findViewById(R.id.progress);

        imgBtnEdit=findViewById(R.id.imgBtnEdit);
        imgBtnShare=findViewById(R.id.imgBtnShare);
    }
}
package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Event.EventDetailsData;
import com.app.miniproject.Database.Event.EventDetailsResponse;
import com.app.miniproject.Database.PostInterface;
import com.app.miniproject.Database.SkillsList.SkillsListData;
import com.app.miniproject.Database.SkillsList.SkillsListResponse;
import com.app.miniproject.Database.TeamEvent.TeamEventResponse;
import com.app.miniproject.Database.TeamInterface;
import com.app.miniproject.Database.UserInterface;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    TextView btnCreateEvent;
    EditText textEventTitle,textEventDescription,textEventOrganiser;
    Spinner spinnerPrivacy;
    RelativeLayout relEventImage;
    TextView txtEventTime,txtEventDeadline,txtTitle;
    ImageView imgEvent;
    File selectedFile = null;

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;

    int ChooserType=0;

    int teamId;
    ChipGroup chipGroup;

    String eventTime,eventDeadline,eventPrivacy="PUBLIC";

    ArrayAdapter<String>privacyAdapter;
    ProgressDialog progressDialog;

    DataProcessor dataProcessor;
    ArrayList<Integer> tags=new ArrayList<>();

    EventDetailsData eventDetailsData;

    String type;
    Integer eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initializeView();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        dataProcessor=DataProcessor.getInstance(this);

        type=getIntent().getStringExtra("type");
        if (TextUtils.equals(type,"ADD")){
            teamId=getIntent().getIntExtra("teamId",-1);
            getTagList();
            btnCreateEvent.setText("Create");
            txtTitle.setText("Create Event");
        }else {
            eventId=getIntent().getIntExtra("eventId",-1);
//            getNonTags();
            btnCreateEvent.setText("Save");
            txtTitle.setText("Edit Event");
            getEventDetails();
        }

        txtEventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooserType=0;
                openDateChooser();
            }
        });

        txtEventDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooserType=1;
                openDateChooser();
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    if (TextUtils.equals(type,"ADD")){
                        createEvent();
                    }else {
                     updateEvent();
                    }
                }
            }
        });

        ArrayList<String>privacy=new ArrayList<>();
        privacy.add("Public");
        privacy.add("Private");

        privacyAdapter=new ArrayAdapter<>(CreateEvent.this,android.R.layout.simple_spinner_dropdown_item,privacy);
        spinnerPrivacy.setAdapter(privacyAdapter);

        spinnerPrivacy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    eventPrivacy="PUBLIC";
                }else if (i==1){
                    eventPrivacy="PRIVATE";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        relEventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(CreateEvent.this);
            }
        });

    }


    private void updateEvent(){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);

        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(eventId));
        RequestBody ConversationId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(eventDetailsData.getConversation_id()));
        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody TeamId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(teamId));
        RequestBody EventTitle = RequestBody.create(MediaType.parse("text/plain"), textEventTitle.getText().toString());
        RequestBody EventDetails = RequestBody.create(MediaType.parse("text/plain"), textEventDescription.getText().toString());
        RequestBody EventOrganiser = RequestBody.create(MediaType.parse("text/plain"), textEventOrganiser.getText().toString());
        RequestBody EventTime = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody EventDeadline = RequestBody.create(MediaType.parse("text/plain"), eventDeadline);
        RequestBody EventPrivacy = RequestBody.create(MediaType.parse("text/plain"), eventPrivacy);


        Call<TeamEventResponse> call;

        if (selectedFile != null) {
            MultipartBody.Part ImgEvent = MultipartBody.Part.createFormData("event_image",
                    selectedFile.getName(), RequestBody.create(MediaType.parse("image/*"), selectedFile));
            call = teamInterface.updateTeamEvent(Id,ConversationId,TeamId,UserId,EventTitle,EventDetails,EventTime,EventPrivacy,EventDeadline,EventOrganiser,tags,ImgEvent);
        } else {
            call = teamInterface.updateTeamEvent(Id,ConversationId,TeamId,UserId,EventTitle,EventDetails,EventTime,EventPrivacy,EventDeadline,EventOrganiser,tags);
        }

        call.enqueue(new Callback<TeamEventResponse>() {
            @Override
            public void onResponse(Call<TeamEventResponse> call, Response<TeamEventResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(CreateEvent.this,"Event updated successfully",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<TeamEventResponse> call, Throwable t) {

            }
        });

    }


    private void getEventDetails(){
        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);
        teamInterface.getEventDetails(eventId,dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<EventDetailsResponse>() {
            @Override
            public void onResponse(Call<EventDetailsResponse> call, Response<EventDetailsResponse> response) {
                if (response.isSuccessful()){
                    eventDetailsData=response.body().getData();
                    if (eventDetailsData!=null){
                        textEventTitle.setText(eventDetailsData.getEventTitle());
                        textEventDescription.setText(eventDetailsData.getEventDetails());
                        textEventOrganiser.setText(eventDetailsData.getEventOrganiser());
                        txtEventTime.setText(eventDetailsData.getEventTime());
                        txtEventDeadline.setText(eventDetailsData.getEventDeadline());
                        Glide.with(CreateEvent.this).load(eventDetailsData.getEventImage()).into(imgEvent);
                        eventTime=eventDetailsData.getEventTime();
                        eventDeadline=eventDetailsData.getEventDeadline();
                        eventPrivacy=eventDetailsData.getEvent_privacy();
                        if (TextUtils.equals(eventDetailsData.getEvent_privacy(),"PUBLIC")){
                            spinnerPrivacy.setSelection(0);
                        }else {
                            spinnerPrivacy.setSelection(1);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EventDetailsResponse> call, Throwable t) {

            }
        });
    }


    private void createEvent(){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        TeamInterface teamInterface= Database.getClient(this).create(TeamInterface.class);

        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody TeamId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(teamId));
        RequestBody EventTitle = RequestBody.create(MediaType.parse("text/plain"), textEventTitle.getText().toString());
        RequestBody EventDetails = RequestBody.create(MediaType.parse("text/plain"), textEventDescription.getText().toString());
        RequestBody EventOrganiser = RequestBody.create(MediaType.parse("text/plain"), textEventOrganiser.getText().toString());
        RequestBody EventTime = RequestBody.create(MediaType.parse("text/plain"), eventTime);
        RequestBody EventDeadline = RequestBody.create(MediaType.parse("text/plain"), eventDeadline);
        RequestBody EventPrivacy = RequestBody.create(MediaType.parse("text/plain"), eventPrivacy);


        Call<TeamEventResponse> call;

        if (selectedFile != null) {
            MultipartBody.Part ImgEvent = MultipartBody.Part.createFormData("event_image",
                    selectedFile.getName(), RequestBody.create(MediaType.parse("image/*"), selectedFile));
            call = teamInterface.createTeamEvent(TeamId,UserId,EventTitle,EventDetails,EventTime,EventPrivacy,EventDeadline,EventOrganiser,tags,ImgEvent);
        } else {
            call = teamInterface.createTeamEvent(TeamId,UserId,EventTitle,EventDetails,EventTime,EventPrivacy,EventDeadline,EventOrganiser,tags);
        }

        call.enqueue(new Callback<TeamEventResponse>() {
            @Override
            public void onResponse(Call<TeamEventResponse> call, Response<TeamEventResponse> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    if (response.body().getCode()==1){
                        Toast.makeText(CreateEvent.this,"Event created successfully",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CreateEvent.this,"CODE "+response.body().getCode(),Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(CreateEvent.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamEventResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateEvent.this,"Something went wrong. Try again later",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean validateFields(){
        if (TextUtils.isEmpty(textEventTitle.getText().toString())){
            Toast.makeText(CreateEvent.this,"Enter event title",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(eventTime)){
            Toast.makeText(CreateEvent.this,"Event time is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void openDateChooser(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, CreateEvent.this,year, month,day);
        datePickerDialog.show();
    }

    private void initializeView(){
        chipGroup=findViewById(R.id.chipGroup);
        btnCreateEvent=findViewById(R.id.btnCreateEvent);
        textEventTitle=findViewById(R.id.textEventTitle);
        textEventDescription=findViewById(R.id.textEventDescription);
        textEventOrganiser=findViewById(R.id.textEventOrganiser);
        spinnerPrivacy=findViewById(R.id.spinnerPrivacy);
        relEventImage=findViewById(R.id.relEventImage);
        imgEvent=findViewById(R.id.imgEvent);
        txtEventTime=findViewById(R.id.txtEventTime);
        txtEventDeadline=findViewById(R.id.txtEventDeadline);
        txtTitle=findViewById(R.id.txtTitle);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, CreateEvent.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        if (ChooserType==0){
            txtEventTime.setText(myHour+":"+myMinute+" "+myday+"-"+(myMonth+1)+"-"+myYear);
            eventTime=myYear+"-"+myMonth+"-"+myday+" "+myHour+":"+myMinute+":00";
        }else {
            txtEventDeadline.setText(myHour+":"+myMinute+" "+myday+"-"+(myMonth+1)+"-"+myYear);
            eventDeadline=myYear+"-"+myMonth+"-"+myday+" "+myHour+":"+myMinute+":00";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert data != null;
//                imgContent.setImageURI(null);
                imgEvent.setImageURI(result.getUri());
                selectedFile = new File(Objects.requireNonNull(result.getUri().getPath()));

//                relImage.setVisibility(View.VISIBLE);
//                Glide.with(CreatePost.this).load(data.getData()).into(imgContent);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "File selection Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getTagList(){
        UserInterface userInterface= Database.getClient(this).create(UserInterface.class);
        userInterface.getSkillsList().enqueue(new Callback<SkillsListResponse>() {
            @Override
            public void onResponse(Call<SkillsListResponse> call, Response<SkillsListResponse> response) {
                if (response.isSuccessful()){
                    /*tagListAdapter=new TagListAdapter(AddSkills.this,R.layout.items_tag_list_selection,response.body().getData());
                    autoCompleteSkills.setAdapter(tagListAdapter);*/
                    renderTags(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SkillsListResponse> call, Throwable t) {

            }
        });
    }

    private void getNonTags(){
        PostInterface postInterface=Database.getClient(this).create(PostInterface.class);
        postInterface.getNontags("EVENT",teamId).enqueue(new Callback<SkillsListResponse>() {
            @Override
            public void onResponse(Call<SkillsListResponse> call, Response<SkillsListResponse> response) {
                if (response.isSuccessful()){
                    renderTags(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<SkillsListResponse> call, Throwable t) {

            }
        });
    }


    private void renderTags(List<SkillsListData> skillsListData){
        for (SkillsListData skills:skillsListData){
            final Chip chip=new Chip(CreateEvent.this);
            chip.setText(skills.getSkillName());
            chip.setTag(String.valueOf(skills.getId()));
            chip.setCloseIconVisible(false);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.colorAccent);
            chip.setCheckable(true);
            chip.setClickable(true);
            if (skills.getMy_tag()!=null){
                if (TextUtils.equals(skills.getMy_tag(),"true")){
                    chip.setChecked(true);
                    tags.add(skills.getId());
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (chip.isChecked()){
                        tags.add(Integer.valueOf(String.valueOf(chip.getTag())));
                    }else {
                        tags.remove(Integer.valueOf(String.valueOf(chip.getTag())));
                    }
                }
            });

//            selectedSkills.add(tagListAdapter.getItem(position).getId());
            chipGroup.addView(chip);
        }
    }
}
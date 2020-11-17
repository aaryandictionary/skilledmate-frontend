package com.app.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.miniproject.Database.ConversationInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Message.SingleMessageResponse;
import com.app.miniproject.Utils.DataProcessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatImageSelector extends AppCompatActivity {

    ImageButton imgBtnBack;
    CircleImageView circularImageUser;
    ImageView image;
    EditText textMessage;
    FloatingActionButton floatingSend;
    File selectedFile=null;

    Integer conversationId,receiverId;
    String convType;

    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_image_selector);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);

        conversationId=getIntent().getIntExtra("conversationId",-1);
        receiverId=getIntent().getIntExtra("receiverId",-1);
        convType=getIntent().getStringExtra("convType");

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CropImage.activity().start(ChatImageSelector.this);

        floatingSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFile!=null)
                    uploadImage();
            }
        });
    }

    private void uploadImage(){
        ConversationInterface conversationInterface= Database.getClient(this).create(ConversationInterface.class);

        RequestBody ConversationId=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(conversationId));
        RequestBody ReceiverId=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(receiverId));
        RequestBody SenderId=RequestBody.create(MediaType.parse("text/plain"),String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
        RequestBody ConvType=RequestBody.create(MediaType.parse("text/plain"),convType);
        RequestBody ContentType=RequestBody.create(MediaType.parse("text/plain"),"IMAGE");
        RequestBody TextMessage;
        if (TextUtils.isEmpty(textMessage.getText().toString())){
             TextMessage=null;
        }else {
             TextMessage=RequestBody.create(MediaType.parse("text/plain"),textMessage.getText().toString());
        }

        Call<SingleMessageResponse> call;

            MultipartBody.Part Content=MultipartBody.Part.createFormData("content",
                    selectedFile.getName(),RequestBody.create(MediaType.parse("image/*"),selectedFile));
            call=conversationInterface.sendImageMessage(ConversationId,SenderId,ReceiverId,TextMessage,ConvType,Content,ContentType);

        call.enqueue(new Callback<SingleMessageResponse>() {
            @Override
            public void onResponse(Call<SingleMessageResponse> call, Response<SingleMessageResponse> response) {
                Toast.makeText(ChatImageSelector.this,"CODE "+response.code(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    Intent intent=new Intent();
                    intent.putExtra("conversationId",response.body().getData().getConversation_id());
                    intent.putExtra("content",response.body().getData().getContent());
                    intent.putExtra("contentType",response.body().getData().getContentType());
                    intent.putExtra("textMsg",response.body().getData().getTextMsg());
                    intent.putExtra("messageId",String.valueOf(response.body().getData().getId()));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SingleMessageResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){
                assert data != null;
//                imgTeam.setImageURI(null);
//                Glide.with(CreateTeam.this).load(data.getData()).into(imgTeam);
                image.setImageURI(result.getUri());
                selectedFile=new File(Objects.requireNonNull(result.getUri().getPath()));

            }else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e=result.getError();
                Toast.makeText(this,"File selection Failed",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else {
                onBackPressed();
            }
        }
    }
    private void initializeView(){
        imgBtnBack=findViewById(R.id.imgBtnBack);
        circularImageUser=findViewById(R.id.circularImageUser);
        image=findViewById(R.id.image);
        textMessage=findViewById(R.id.textMessage);
        floatingSend=findViewById(R.id.floatingSend);
    }
}
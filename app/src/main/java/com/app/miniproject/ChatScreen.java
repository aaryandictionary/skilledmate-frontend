package com.app.miniproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.miniproject.Adapters.MessageAdapter;
import com.app.miniproject.Database.ConversationInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Message.LastSeenModel;
import com.app.miniproject.Database.Message.MessageData;
import com.app.miniproject.Database.Message.MessageDataWSResponse;
import com.app.miniproject.Database.Message.MessageModel;
import com.app.miniproject.Database.Message.MessageResponse;
import com.app.miniproject.Utils.DataProcessor;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreen extends AppCompatActivity {

    private static final Integer MESSEGE_COUNT = 20;
    ImageButton imgBtnAttach,imgBtnSend;
    EditText textMessage;
    RecyclerView recyclerChat;
    private OkHttpClient client;
    WebSocket ws;

    CircleImageView circularImgUser;
    TextView txtUserName,txtLastSeen;

    RelativeLayout relProfile;

    DataProcessor dataProcessor;
    MessageAdapter messageAdapter;

    private Integer lastMessageId=-1;

    private Integer conversationId;
    private Integer receiverId;

    private String convType="MONO",receiverName,receiverImage;
    SwipeRefreshLayout swipeTorefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        initializeView();

        dataProcessor=DataProcessor.getInstance(this);
        conversationId=getIntent().getIntExtra("conversationId",-1);
        receiverId=getIntent().getIntExtra("receiverId",-1);
        convType=getIntent().getStringExtra("convType");
        receiverName=getIntent().getStringExtra("receiverName");
        receiverImage=getIntent().getStringExtra("receiverImage");

        txtUserName.setText(receiverName);
        Glide.with(this).load(receiverImage).into(circularImgUser);

        messageAdapter=new MessageAdapter(this,dataProcessor.getInteger(DataProcessor.USER_ID));
        recyclerChat.setAdapter(messageAdapter);

        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(textMessage.getText().toString())){
                    MessageModel messageModel=new MessageModel();
                    messageModel.setAction("onMessage");
                    messageModel.setCont("");
                    messageModel.setContType("TEXT");
                    messageModel.setConversationId(String.valueOf(conversationId));
                    messageModel.setConvType(convType);
                    messageModel.setReceiverId(String.valueOf(receiverId));
                    messageModel.setSenderId(String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
                    messageModel.setMsg(textMessage.getText().toString());
                    messageModel.setMessageId("-1");
                    Gson gson=new Gson();
                    ws.send(gson.toJson(messageModel));
                    textMessage.setText("");
                }
            }
        });

        relProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatScreen.this,UserProfile.class);
                startActivity(intent);
            }
        });

        client = new OkHttpClient();
        start();
        getMessages();

        imgBtnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatScreen.this,ChatImageSelector.class);
                intent.putExtra("conversationId",conversationId);
                intent.putExtra("receiverId",receiverId);
                intent.putExtra("convType",convType);
                startActivityForResult(intent,1);
            }
        });

        swipeTorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessages();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    conversationId=data.getIntExtra("conversationId",-1);
                    MessageModel messageModel=new MessageModel();
                    messageModel.setAction("onMessage");
                    messageModel.setCont(data.getStringExtra("content"));
                    messageModel.setContType(data.getStringExtra("contentType"));
                    messageModel.setConversationId(String.valueOf(conversationId));
                    messageModel.setConvType(convType);
                    messageModel.setReceiverId(String.valueOf(receiverId));
                    messageModel.setSenderId(String.valueOf(dataProcessor.getInteger(DataProcessor.USER_ID)));
                    messageModel.setMsg(data.getStringExtra("textMsg"));
                    messageModel.setMessageId(data.getStringExtra("messageId"));
                    Gson gson=new Gson();
                    ws.send(gson.toJson(messageModel));
                }
            }
        }
    }

    private void getMessages(){
        ConversationInterface conversationInterface= Database.getClient(this).create(ConversationInterface.class);
        conversationInterface.getMessages(conversationId,dataProcessor.getInteger(DataProcessor.USER_ID),lastMessageId,MESSEGE_COUNT).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()){
                    int size=response.body().getData().size();
                    if (size!=0){
                        int lastmsg=response.body().getData().get(size-1).getId();
//                        Toast.makeText(ChatScreen.this,"MSG "+size+" LMID "+lastmsg,Toast.LENGTH_SHORT).show();
                        int tempLmp=lastMessageId;
                        if (lastmsg<lastMessageId||lastMessageId==-1){
                            messageAdapter.addMessageToTop(response.body().getData());
                            lastMessageId=lastmsg;
                        }
                        if (tempLmp==-1)
                            recyclerChat.smoothScrollToPosition(0);
                    }
                }
                if (swipeTorefresh.isRefreshing())
                    swipeTorefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

                if (swipeTorefresh.isRefreshing())
                    swipeTorefresh.setRefreshing(false);
            }
        });

    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
//            output("Start : "+response.message());
            if (TextUtils.equals(convType,"MONO"))
            ws.send("{\"action\":\"seenStatus\",\"userId\":\""+dataProcessor.getInteger(DataProcessor.USER_ID)+"\",\"lrId\":\""+receiverId+"\"}");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
//            textMessage.setText(text);

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            output("Receiving bytes : " + bytes.hex());
//            textMessage.setText(String.valueOf(bytes));
//            Toast.makeText(ChatScreen.this,String.valueOf(bytes),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            ws.send("{\"action\":\"seenStatus\",\"userId\":\""+dataProcessor.getInteger(DataProcessor.USER_ID)+"\",\"lrId\":\""+receiverId+"\"}");
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
//            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
//            output("Error : " + t.getMessage());
//            textMessage.setText(t.getMessage());
//            Toast.makeText(ChatScreen.this,String.valueOf(t.getMessage()),Toast.LENGTH_SHORT).show();
        }
    }

    private void start() {
        Request request = new Request.Builder().url("wss://g5961tky75.execute-api.ap-south-1.amazonaws.com/production?userId="+dataProcessor.getInteger(DataProcessor.USER_ID)).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }

    private void initializeView(){
        swipeTorefresh=findViewById(R.id.swipeTorefresh);
        circularImgUser=findViewById(R.id.circularImgUser);
        txtUserName=findViewById(R.id.txtUserName);
        txtLastSeen=findViewById(R.id.txtLastSeen);

        relProfile=findViewById(R.id.relProfile);
        imgBtnAttach=findViewById(R.id.imgBtnAttach);
        textMessage=findViewById(R.id.textMessage);
        imgBtnSend=findViewById(R.id.imgBtnSend);
        recyclerChat=findViewById(R.id.recyclerChat);
        recyclerChat.setHasFixedSize(false);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textMessage.setText(txt);
                Gson gson=new Gson();
                MessageData messageData=gson.fromJson(txt,MessageData.class);
                if (messageData.getLastSeen()==null){
                    messageAdapter.addMessageToBottom(messageData);
                    recyclerChat.smoothScrollToPosition(0);
                }else {
                    txtLastSeen.setText(messageData.getLastSeen());
                }


            }
        });
    }
}
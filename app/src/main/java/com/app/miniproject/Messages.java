package com.app.miniproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.miniproject.Adapters.ChatAdapter;
import com.app.miniproject.Database.Conversation.ConversationData;
import com.app.miniproject.Database.Conversation.ConversationResponse;
import com.app.miniproject.Database.ConversationInterface;
import com.app.miniproject.Database.Database;
import com.app.miniproject.Database.Message.MessageModel;
import com.app.miniproject.Models.ChatModel;
import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;
import com.app.miniproject.SqliteDb.ViewModel.ConversationViewModel;
import com.app.miniproject.Utils.DataProcessor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Messages extends Fragment implements ChatAdapter.ChatEvents{

    RecyclerView recyclerChats;
    ChatAdapter chatAdapter;

    DataProcessor dataProcessor;
    ConversationViewModel conversationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerChats=view.findViewById(R.id.recyclerChats);
        recyclerChats.setHasFixedSize(true);
        recyclerChats.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter=new ChatAdapter(getContext(),Messages.this);
        recyclerChats.setAdapter(chatAdapter);
        dataProcessor=DataProcessor.getInstance(getContext());

        conversationViewModel=new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ConversationViewModel.class);

//        getConversationFromSqlite();
        getConversations();
        return view;
    }

    /*private void getConversationFromSqlite(){
        conversationViewModel.getConversations().observe(this, new Observer<List<SqliteConversationData>>() {
            @Override
            public void onChanged(List<SqliteConversationData> sqliteConversationData) {
                if (sqliteConversationData!=null){
                    chatAdapter.setChatList(sqliteConversationData);
                }
            }
        });
    }*/

    private void getConversations(){
        ConversationInterface conversationInterface= Database.getClient(getContext()).create(ConversationInterface.class);
        conversationInterface.getMyConversations(dataProcessor.getInteger(DataProcessor.USER_ID)).enqueue(new Callback<ConversationResponse>() {
            @Override
            public void onResponse(Call<ConversationResponse> call, Response<ConversationResponse> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    if (response.body().getData()!=null){
//                        new UpdateConversationTableAsyncTask().execute(response.body().getData());
                        chatAdapter.setChatList(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<ConversationResponse> call, Throwable t) {

            }
        });
    }


    private class UpdateConversationTableAsyncTask extends AsyncTask<List<ConversationData>,Void,Void>{

        @Override
        protected Void doInBackground(List<ConversationData>... lists) {
            for (ConversationData conversationData:lists[0]){
                SqliteConversationData sqliteConversationData=new SqliteConversationData();
                sqliteConversationData.setUser_id(conversationData.getUserId());
                sqliteConversationData.setLast_message(conversationData.getConversationLastMessageData().getTextMsg());
                sqliteConversationData.setCreated_at(conversationData.getConversationLastMessageData().getCreatedAt());
                sqliteConversationData.setConversation_id(conversationData.getConversationId());
                sqliteConversationData.setConv_type(conversationData.getConvType());
                sqliteConversationData.setConv_title(conversationData.getConvTitle());
                sqliteConversationData.setConv_icon(conversationData.getConvIcon());
                conversationViewModel.insertConversation(sqliteConversationData);
            }
            return null;
        }
    }


    @Override
    public void onChatClick(ConversationData conversationData) {
        Intent intent=new Intent(getContext(),ChatScreen.class);
        intent.putExtra("conversationId",conversationData.getConversationId());
        intent.putExtra("receiverId",conversationData.getUserId());
        intent.putExtra("receiverName",conversationData.getConvTitle());
        intent.putExtra("receiverImage",conversationData.getConvIcon());
        intent.putExtra("convType",conversationData.getConvType());
        startActivity(intent);
    }
}
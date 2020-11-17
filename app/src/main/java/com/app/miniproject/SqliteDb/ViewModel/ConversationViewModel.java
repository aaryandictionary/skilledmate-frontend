package com.app.miniproject.SqliteDb.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;
import com.app.miniproject.SqliteDb.Repository.ConversationRepository;

import java.util.List;

public class ConversationViewModel extends AndroidViewModel {
    private ConversationRepository conversationRepository;
    private LiveData<List<SqliteConversationData>>listLiveData;

    public ConversationViewModel(@NonNull Application application) {
        super(application);
        conversationRepository=new ConversationRepository(application);
        listLiveData=conversationRepository.getConversationDataLiveData();
    }

    public void insertConversation(SqliteConversationData sqliteConversationData){
        conversationRepository.insertConversation(sqliteConversationData);
    }

    public void updateConversation(SqliteConversationData sqliteConversationData){
        conversationRepository.updateTransaction(sqliteConversationData);
    }

    public void deleteConversation(Long conversationId){
        conversationRepository.deleteTransaction(conversationId);
    }

    public LiveData<List<SqliteConversationData>> getConversations() {
        listLiveData=conversationRepository.getConversationDataLiveData();
        return listLiveData;
    }
}

package com.app.miniproject.SqliteDb.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.app.miniproject.SqliteDb.Dao.ConversationDao;
import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;
import com.app.miniproject.SqliteDb.SqliteDatabase;

import java.util.List;

public class ConversationRepository {
    private ConversationDao conversationDao;
    private LiveData<List<SqliteConversationData>>conversationDataLiveData;

    SqliteDatabase sqliteDatabase;

    public ConversationRepository(Application application) {
        sqliteDatabase=SqliteDatabase.getInstance(application);
        conversationDao=sqliteDatabase.conversationDao();
        conversationDataLiveData=conversationDao.getConversations();
    }

    public LiveData<List<SqliteConversationData>> getConversationDataLiveData() {
        return conversationDataLiveData;
    }

    public void insertConversation(SqliteConversationData sqliteConversationData){
        new InsertConversationAsyncTask(conversationDao).execute(sqliteConversationData);
    }

    public static class InsertConversationAsyncTask extends AsyncTask<SqliteConversationData,Void,Void>{
        private ConversationDao conversationDao;

        public InsertConversationAsyncTask(ConversationDao conversationDao) {
            this.conversationDao = conversationDao;
        }

        @Override
        protected Void doInBackground(SqliteConversationData... sqliteConversationData) {
            long conversationId= conversationDao.insertConversation(sqliteConversationData[0]);
            if (conversationId==-1){
                conversationDao.updateConversation(sqliteConversationData[0]);
            }
             return null;
        }
    }
    public void updateTransaction(SqliteConversationData sqliteConversationData){
        new UpdateConversationAsyncTask(conversationDao).execute(sqliteConversationData);
    }
    public static class UpdateConversationAsyncTask extends AsyncTask<SqliteConversationData,Void,Void>{
        private ConversationDao conversationDao;

        public UpdateConversationAsyncTask(ConversationDao conversationDao) {
            this.conversationDao = conversationDao;
        }

        @Override
        protected Void doInBackground(SqliteConversationData... sqliteConversationData) {
            conversationDao.updateConversation(sqliteConversationData[0]);
            return null;
        }
    }

    public void deleteTransaction(long conversationId){
        new DeleteConversationAsyncTask(conversationDao).execute(conversationId);
    }

    public static class DeleteConversationAsyncTask extends AsyncTask<Long,Void,Void>{
        private ConversationDao conversationDao;

        public DeleteConversationAsyncTask(ConversationDao conversationDao) {
            this.conversationDao = conversationDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            conversationDao.deleteConversation(longs[0]);
            return null;
        }
    }

}

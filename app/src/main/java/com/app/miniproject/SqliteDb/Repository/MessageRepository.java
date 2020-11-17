package com.app.miniproject.SqliteDb.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.app.miniproject.SqliteDb.Dao.MessageDao;
import com.app.miniproject.SqliteDb.Entity.SqliteMessageData;
import com.app.miniproject.SqliteDb.SqliteDatabase;

import java.util.List;

public class MessageRepository  {
    private MessageDao messageDao;
    private LiveData<List<SqliteMessageData>>getMessages;

    SqliteDatabase sqliteDatabase;

    public MessageRepository(Application application) {
        sqliteDatabase=SqliteDatabase.getInstance(application);
        messageDao=sqliteDatabase.messageDao();

    }

    public LiveData<List<SqliteMessageData>> getGetMessages(long lastMsgId,long conversationId) {
        getMessages=messageDao.getMessages(lastMsgId,conversationId);
        return getMessages;
    }

    public void insertMessage(SqliteMessageData sqliteMessageData){

    }

//    public static class InsertMessageAsyncTask extends AsyncTask<SqliteMessageData,Void,Void>{
//        private MessageDao messageDao;
//
//        public InsertMessageAsyncTask(MessageDao messageDao) {
//            this.messageDao = messageDao;
//        }
//
//        @Override
//        protected Void doInBackground(SqliteMessageData... sqliteMessageData) {
//            messageDao.insertMessage()
//            return null;
//        }
//    }
}

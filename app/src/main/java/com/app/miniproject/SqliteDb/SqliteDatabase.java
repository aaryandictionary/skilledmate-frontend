package com.app.miniproject.SqliteDb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.app.miniproject.SqliteDb.Dao.ConversationDao;
import com.app.miniproject.SqliteDb.Dao.MessageDao;
import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;
import com.app.miniproject.SqliteDb.Entity.SqliteMessageData;

@Database(entities = {SqliteConversationData.class, SqliteMessageData.class},version = 1,exportSchema = false)
public abstract class SqliteDatabase extends RoomDatabase {
    private static SqliteDatabase instance;

    public abstract ConversationDao conversationDao();
    public abstract MessageDao messageDao();

    public static synchronized SqliteDatabase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    SqliteDatabase.class,"skilled_mate")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public void closeDb(){
        if (instance!=null){
            if (instance.isOpen()){
                instance.close();
            }
            instance=null;
        }
    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {


        private PopulateDbAsyncTask(SqliteDatabase database){
        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }

}

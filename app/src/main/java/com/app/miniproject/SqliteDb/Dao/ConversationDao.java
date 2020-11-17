package com.app.miniproject.SqliteDb.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.miniproject.SqliteDb.Entity.SqliteConversationData;

import java.util.List;

@Dao
public interface ConversationDao {

    @Query("SELECT * FROM conversations")
    LiveData<List<SqliteConversationData>> getConversations();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertConversation(SqliteConversationData sqliteConversationData);

    @Update
    void updateConversation(SqliteConversationData sqliteConversationData);

    @Query("DELETE FROM conversations WHERE conversation_id=:conversationId")
    void deleteConversation(long conversationId);

}

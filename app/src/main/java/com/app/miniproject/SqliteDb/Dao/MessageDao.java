package com.app.miniproject.SqliteDb.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.app.miniproject.SqliteDb.Entity.SqliteMessageData;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM messages WHERE conversation_id=:conversationId AND message_id<=:lastMsgId ORDER BY message_id DESC LIMIT 20")
    LiveData<List<SqliteMessageData>>getMessages(long lastMsgId,long conversationId);

    @Insert
    long insertMessage(SqliteMessageData sqliteMessageData);

    @Query("DELETE FROM messages WHERE message_id=:messageId")
    void deleteMessage(long messageId);

    @Query("SELECT id FROM messages WHERE conversation_id=:conversationId ORDER BY message_id DESC LIMIT 1")
    long getLastMessageId(long conversationId);

    @Query("SELECT message_id FROM messages WHERE conversation_id=:conversationId ORDER BY message_id DESC LIMIT 1")
    long getLastMTId(long conversationId);

    @Query("SELECT * FROM messages WHERE conversation_id=:conversationId AND send_status=0 ORDER BY message_id ASC")
    List<SqliteMessageData>getUnsentMessages(long conversationId);

    @Query("UPDATE messages SET send_status=1 WHERE message_id=:messageId")
    int updateMessageStatus(long messageId);
}

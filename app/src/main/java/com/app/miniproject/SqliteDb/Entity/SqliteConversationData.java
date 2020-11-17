package com.app.miniproject.SqliteDb.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversations")
public class SqliteConversationData {

    @PrimaryKey(autoGenerate = false)
    private long conversation_id;

    private int user_id;

    private String conv_title;

    private String conv_icon;

    private String conv_type;

    private String last_message;

    private String created_at;

    public long getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(long conversation_id) {
        this.conversation_id = conversation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getConv_title() {
        return conv_title;
    }

    public void setConv_title(String conv_title) {
        this.conv_title = conv_title;
    }

    public String getConv_icon() {
        return conv_icon;
    }

    public void setConv_icon(String conv_icon) {
        this.conv_icon = conv_icon;
    }

    public String getConv_type() {
        return conv_type;
    }

    public void setConv_type(String conv_type) {
        this.conv_type = conv_type;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

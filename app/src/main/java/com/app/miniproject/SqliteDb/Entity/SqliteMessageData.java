package com.app.miniproject.SqliteDb.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class SqliteMessageData {

    @PrimaryKey(autoGenerate = true)
    private long message_id;

    private long id;

    private long sender_id;

    private String content_type;

    private String content;

    private String conversation_id;

    private String text_msg;

    private String sender_name;

    private String created_at;

    private int send_status=0;

    public int getSend_status() {
        return send_status;
    }

    public void setSend_status(int send_status) {
        this.send_status = send_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getText_msg() {
        return text_msg;
    }

    public void setText_msg(String text_msg) {
        this.text_msg = text_msg;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }
}

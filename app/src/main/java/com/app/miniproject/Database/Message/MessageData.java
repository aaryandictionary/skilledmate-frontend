package com.app.miniproject.Database.Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("conversation_id")
    @Expose
    private String conversation_id;
    @SerializedName("text_msg")
    @Expose
    private String textMsg;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("last_seen")
    @Expose
    private String lastSeen;

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}

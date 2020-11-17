package com.app.miniproject.Database.Conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationLastMessageData {
    @SerializedName("text_msg")
    @Expose
    private String textMsg;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("conversation_id")
    @Expose
    private Integer conversationId;

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }
}

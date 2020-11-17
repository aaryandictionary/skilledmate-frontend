package com.app.miniproject.Database.Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDataWSResponse {

    @SerializedName("last_seen")
    @Expose
    private LastSeenModel lastSeenModel;

    @SerializedName("message")
    @Expose
    private MessageData messageData;

    public LastSeenModel getLastSeenModel() {
        return lastSeenModel;
    }

    public void setLastSeenModel(LastSeenModel lastSeenModel) {
        this.lastSeenModel = lastSeenModel;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }
}

package com.app.miniproject.Database.Message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("convType")
    @Expose
    private String convType;
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("cont")
    @Expose
    private String cont;
    @SerializedName("contType")
    @Expose
    private String contType;
    @SerializedName("receiverId")
    @Expose
    private String receiverId;
    @SerializedName("messageId")
    @Expose
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConvType() {
        return convType;
    }

    public void setConvType(String convType) {
        this.convType = convType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}

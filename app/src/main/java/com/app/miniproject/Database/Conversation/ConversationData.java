package com.app.miniproject.Database.Conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationData {

    @SerializedName("conv_title")
    @Expose
    private String convTitle;
    @SerializedName("conv_icon")
    @Expose
    private String convIcon;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("conv_type")
    @Expose
    private String convType;
    @SerializedName("id")
    @Expose
    private Integer conversationId;
    @SerializedName("lastmessage")
    @Expose
    private ConversationLastMessageData conversationLastMessageData;

    public String getConvTitle() {
        return convTitle;
    }

    public void setConvTitle(String convTitle) {
        this.convTitle = convTitle;
    }

    public String getConvIcon() {
        return convIcon;
    }

    public void setConvIcon(String convIcon) {
        this.convIcon = convIcon;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getConvType() {
        return convType;
    }

    public void setConvType(String convType) {
        this.convType = convType;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationLastMessageData getConversationLastMessageData() {
        return conversationLastMessageData;
    }

    public void setConversationLastMessageData(ConversationLastMessageData conversationLastMessageData) {
        this.conversationLastMessageData = conversationLastMessageData;
    }
}

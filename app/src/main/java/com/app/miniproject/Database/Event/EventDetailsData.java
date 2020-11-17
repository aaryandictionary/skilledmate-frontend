package com.app.miniproject.Database.Event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventDetailsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_details")
    @Expose
    private String eventDetails;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("event_organiser")
    @Expose
    private String eventOrganiser;
    @SerializedName("event_privacy")
    @Expose
    private String event_privacy;
    @SerializedName("event_time")
    @Expose
    private String eventTime;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_image")
    @Expose
    private String eventImage;
    @SerializedName("event_deadline")
    @Expose
    private String eventDeadline;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("conversation_id")
    @Expose
    private Integer conversation_id;
    @SerializedName("participants_count")
    @Expose
    private Integer participants_count;
    @SerializedName("my_event")
    @Expose
    private String my_event;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("team")
    @Expose
    private EventTeamData eventTeamData;

    public String getEvent_privacy() {
        return event_privacy;
    }

    public void setEvent_privacy(String event_privacy) {
        this.event_privacy = event_privacy;
    }

    public Integer getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMy_event() {
        return my_event;
    }

    public void setMy_event(String my_event) {
        this.my_event = my_event;
    }

    public Integer getParticipants_count() {
        return participants_count;
    }

    public void setParticipants_count(Integer participants_count) {
        this.participants_count = participants_count;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEventOrganiser() {
        return eventOrganiser;
    }

    public void setEventOrganiser(String eventOrganiser) {
        this.eventOrganiser = eventOrganiser;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventDeadline() {
        return eventDeadline;
    }

    public void setEventDeadline(String eventDeadline) {
        this.eventDeadline = eventDeadline;
    }

    public EventTeamData getEventTeamData() {
        return eventTeamData;
    }

    public void setEventTeamData(EventTeamData eventTeamData) {
        this.eventTeamData = eventTeamData;
    }
}

package com.app.miniproject.Database.DashboardPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DPostData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("post_category")
    @Expose
    private String postCategory;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @SerializedName("user")
    @Expose
    private DPostUser user;
    @SerializedName("images")
    @Expose
    private List<DPostImages> images = null;
    @SerializedName("likes")
    @Expose
    private List<DPostLike> likes = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public DPostUser getUser() {
        return user;
    }

    public void setUser(DPostUser user) {
        this.user = user;
    }

    public List<DPostImages> getImages() {
        return images;
    }

    public void setImages(List<DPostImages> images) {
        this.images = images;
    }

    public List<DPostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<DPostLike> likes) {
        this.likes = likes;
    }
}

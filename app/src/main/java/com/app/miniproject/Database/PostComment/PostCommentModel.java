package com.app.miniproject.Database.PostComment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostCommentModel {

    @SerializedName("post_id")
    @Expose
    private Integer post_id;

    @SerializedName("commenter_id")
    @Expose
    private Integer commenter_id;

    @SerializedName("comment")
    @Expose
    private String comment;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getCommenter_id() {
        return commenter_id;
    }

    public void setCommenter_id(Integer commenter_id) {
        this.commenter_id = commenter_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

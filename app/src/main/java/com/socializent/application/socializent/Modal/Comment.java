package com.socializent.application.socializent.Modal;



/**
 * Created by Toshıba on 9.05.2017.
 */

public class Comment {

    private String creatorName;
    private long creationDate;
    private String content;

    public Comment() {
        content = "Empty Content";
        creatorName = "Empty Content";
        creationDate = 0L;
    }

    public Comment(String creatorName, long creationDate, String content) {
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.content = content;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }





}

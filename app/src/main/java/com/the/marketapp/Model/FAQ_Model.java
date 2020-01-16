package com.the.marketapp.Model;

public class FAQ_Model {
    String title;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    Boolean status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String content;

    public FAQ_Model(String title, String content,Boolean status) {

        this.title = title;
        this.content = content;
        this.status=status;
    }
}

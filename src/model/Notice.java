package model;

import java.sql.Date;

public class Notice {
    private int noticeId;
    private String title;
    private String content;
    private Date postedDate;
    private boolean isActive;

    public Notice() {
    }

    public Notice(int noticeId, String title, String content, Date postedDate, boolean isActive) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.postedDate = postedDate;
        this.isActive = isActive;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

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

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

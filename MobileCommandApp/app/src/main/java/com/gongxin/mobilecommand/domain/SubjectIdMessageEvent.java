package com.gongxin.mobilecommand.domain;

public class SubjectIdMessageEvent {
    private int parentId;
    private Boolean isCommonlyUsed;
    private String title;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Boolean getCommonlyUsed() {
        return isCommonlyUsed;
    }

    public void setCommonlyUsed(Boolean commonlyUsed) {
        isCommonlyUsed = commonlyUsed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
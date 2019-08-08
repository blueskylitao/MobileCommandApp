package com.gongxin.mobilecommand.domain;

public class SubjectIdMessageEvent {
    private int parentId;
    private Boolean isCommonlyUsed;

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
}
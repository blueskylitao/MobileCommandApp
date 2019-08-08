package com.gongxin.mobilecommand.domain;

import java.io.Serializable;

/**
 *
 */
public class DecisionSubject implements Serializable {

    /**
     * id : 25
     * name : 社会消费品零售总额
     * url : /abc
     * description : 社会消费品零售总额
     * buildUnit : xxx研究院
     * dutyDept : xx局
     * publishTime : 2019-08-06 16:07:16
     * updateTime : 2019-08-07 11:17:48
     * approveNum : 100
     * visitNum : 100
     */

    private int id;
    private String name;
    private String url;
    private String description;
    private String buildUnit;
    private String dutyDept;
    private String publishTime;
    private String updateTime;
    private String approveNum;
    private String visitNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildUnit() {
        return buildUnit;
    }

    public void setBuildUnit(String buildUnit) {
        this.buildUnit = buildUnit;
    }

    public String getDutyDept() {
        return dutyDept;
    }

    public void setDutyDept(String dutyDept) {
        this.dutyDept = dutyDept;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getApproveNum() {
        return approveNum;
    }

    public void setApproveNum(String approveNum) {
        this.approveNum = approveNum;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }
}

package com.uchain.remarksystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Project implements Serializable {
    private Long id;

    private String name;

    private Integer hasText = 0;

    //-1表示字数没有限制
    private Integer wordsNum = -1;

    private Integer dataNum = 0;

    private Integer packageNum = 200;

    private Integer checkNum = 30;

    //项目当前的状态,0:未启动,1:进行中,2:全部完成
    private Integer status = 0;

    @JsonIgnore
    private Long createdBy;

    @JsonIgnore
    private Long updatedBy;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getHasText() {
        return hasText;
    }

    public void setHasText(Integer hasText) {
        this.hasText = hasText;
    }

    public Integer getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(Integer wordsNum) {
        this.wordsNum = wordsNum;
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public Integer getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(Integer packageNum) {
        this.packageNum = packageNum;
    }

    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", hasText=").append(hasText);
        sb.append(", wordsNum=").append(wordsNum);
        sb.append(", dataNum=").append(dataNum);
        sb.append(", packageNum=").append(packageNum);
        sb.append(", checkNum=").append(checkNum);
        sb.append(", status=").append(status);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
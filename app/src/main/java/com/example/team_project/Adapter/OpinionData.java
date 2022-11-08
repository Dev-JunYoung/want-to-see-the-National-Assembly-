package com.example.team_project.Adapter;

public class OpinionData {
    private String content;
    private String regName;
    private String regDate;

    public OpinionData(String content, String regName, String regDate) {
        this.content = content;
        this.regName = regName;
        this.regDate = regDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "OpinionData{" +
                "content='" + content + '\'' +
                ", regName='" + regName + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}

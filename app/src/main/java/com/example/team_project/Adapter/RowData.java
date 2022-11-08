package com.example.team_project.Adapter;

import java.io.Serializable;

public class RowData implements Serializable {
    private String bill_id;
    private String bill_no;
    private String bill_name; //법률안제목
    private String proposer; //제안자
    private String propose_dt; //제안일
    private String curr_committee_id; // 소관위원회 id
    private String curr_committee; // 소관위원회
    private String link_url;
    private int step; // 입법 과정 절차

    public RowData(String bill_no, String bill_name, String proposer, String propose_dt, String curr_committee_id,String curr_committee, String link_url, int step) {
        this.bill_no = bill_no;
        this.bill_name = bill_name;
        this.proposer = proposer;
        this.propose_dt = propose_dt;
        this.curr_committee_id = curr_committee_id;
        this.curr_committee = curr_committee;
        this.link_url = link_url;
        this.step = step;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getCurr_committee_id() {
        return curr_committee_id;
    }

    public void setCurr_committee_id(String curr_committee_id) {
        this.curr_committee_id = curr_committee_id;
    }

    public String getCurr_committee() {
        return curr_committee;
    }

    public void setCurr_committee(String curr_committee) {
        this.curr_committee = curr_committee;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getBill_name() {
        return bill_name;
    }

    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }


    public void setPropose(String propose) {
        this.proposer = propose;
    }

    public String getPropose_dt() {
        return propose_dt;
    }

    public void setPropose_dt(String propose_dt) {
        this.propose_dt = propose_dt;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    // 법률안 입법과정
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "RowData{" +
                "bill_id='" + bill_id + '\'' +
                ", bill_no='" + bill_no + '\'' +
                ", bill_name='" + bill_name + '\'' +
                ", proposer='" + proposer + '\'' +
                ", propose_dt='" + propose_dt + '\'' +
                ", curr_committee_id='" + curr_committee_id + '\'' +
                ", curr_committee='" + curr_committee + '\'' +
                ", link_url='" + link_url + '\'' +
                ", step='" + step + '\'' + // 법률안의 입법 과정
                '}';
    }
}

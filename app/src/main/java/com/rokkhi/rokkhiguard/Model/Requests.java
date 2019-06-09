package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class Requests {

    private String responder; // jar kase kortese /userid
    private String requester; // jar kase kortese /userid
    private String autoid;
    private String response;
    private String reqType;
    private String flat_id;
    private String f_no;
    private Date r_time;
    private String build_id;

    public Requests(){
    }

    public Requests(String responder, String requester, String autoid, String response, String reqType, String flat_id, String f_no, Date r_time, String build_id) {
        this.responder = responder;
        this.requester = requester;
        this.autoid = autoid;
        this.response = response;
        this.reqType = reqType;
        this.flat_id = flat_id;
        this.f_no = f_no;
        this.r_time = r_time;
        this.build_id = build_id;
    }


    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAutoid() {
        return autoid;
    }

    public void setAutoid(String autoid) {
        this.autoid = autoid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getF_no() {
        return f_no;
    }

    public void setF_no(String f_no) {
        this.f_no = f_no;
    }

    public Date getR_time() {
        return r_time;
    }

    public void setR_time(Date r_time) {
        this.r_time = r_time;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }
}

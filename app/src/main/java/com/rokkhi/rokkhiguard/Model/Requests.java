package com.rokkhi.rokkhiguard.Model;

public class Requests {

    private String responder; // jar kase kortese /userid
    private String requester; // jar kase kortese /userid
    private String autoid;
    private int response;
    private int reqType;
    private String flat_id;
    private String build_id;

    public Requests(){
    }

    public Requests(String responder, String requester, String autoid, int response, int reqType, String flat_id, String build_id) {
        this.responder = responder;
        this.requester = requester;
        this.autoid = autoid;
        this.response = response;
        this.reqType = reqType;
        this.flat_id = flat_id;
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

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public String getFlat_id() {
        return flat_id;
    }

    public void setFlat_id(String flat_id) {
        this.flat_id = flat_id;
    }

    public String getBuild_id() {
        return build_id;
    }

    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }
}

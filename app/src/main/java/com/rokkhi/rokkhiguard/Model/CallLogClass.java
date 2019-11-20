package com.rokkhi.rokkhiguard.Model;

import java.util.Date;

public class CallLogClass {
   String docID;
   String  buildID;
   String mobileUID;
   String mobielNumberCaller;
   String mobileNumberReceiver;
   Date callStart;
   Date callEnd;
   boolean received;

    public CallLogClass() {
    }

    public CallLogClass(String docID, String buildID, String mobileUID, String mobielNumberCaller, String mobileNumberReceiver, Date callStart, Date callEnd, boolean received) {
        this.docID = docID;
        this.buildID = buildID;
        this.mobileUID = mobileUID;
        this.mobielNumberCaller = mobielNumberCaller;
        this.mobileNumberReceiver = mobileNumberReceiver;
        this.callStart = callStart;
        this.callEnd = callEnd;
        this.received = received;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getMobileUID() {
        return mobileUID;
    }

    public void setMobileUID(String mobileUID) {
        this.mobileUID = mobileUID;
    }

    public String getMobielNumberCaller() {
        return mobielNumberCaller;
    }

    public void setMobielNumberCaller(String mobielNumberCaller) {
        this.mobielNumberCaller = mobielNumberCaller;
    }

    public String getMobileNumberReceiver() {
        return mobileNumberReceiver;
    }

    public void setMobileNumberReceiver(String mobileNumberReceiver) {
        this.mobileNumberReceiver = mobileNumberReceiver;
    }

    public Date getCallStart() {
        return callStart;
    }

    public void setCallStart(Date callStart) {
        this.callStart = callStart;
    }

    public Date getCallEnd() {
        return callEnd;
    }

    public void setCallEnd(Date callEnd) {
        this.callEnd = callEnd;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}

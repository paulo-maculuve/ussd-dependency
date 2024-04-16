package com.bluteki.gateway.ussd;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ussd {
    private String msisdn;
    private String sessionId;
    private String type;
    private String msg;

    public Ussd() {
    }

    public Ussd(String msisdn, String sessionId, String type, String msg) {
        this.msisdn = msisdn;
        this.sessionId = sessionId;
        this.type = type;
        this.msg = msg;
    }

    @XmlElement
    public String getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @XmlElement
    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlElement
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement
    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
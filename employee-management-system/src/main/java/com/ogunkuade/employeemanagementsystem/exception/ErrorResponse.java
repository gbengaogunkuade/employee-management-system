package com.ogunkuade.employeemanagementsystem.exception;


import java.util.Date;

public class ErrorResponse {

    private Date timestamp;
    private String subject;
    private String message;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(Date timestamp, String subject, String message) {
        this.timestamp = timestamp;
        this.subject = subject;
        this.message = message;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}

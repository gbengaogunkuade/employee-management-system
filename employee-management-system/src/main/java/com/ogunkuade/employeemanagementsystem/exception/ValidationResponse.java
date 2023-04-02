package com.ogunkuade.employeemanagementsystem.exception;

import java.util.Date;
import java.util.List;

public class ValidationResponse {

    private Date timestamp;
    private String message;
    private List<String> errorList;

    public ValidationResponse() {
    }

    public ValidationResponse(Date timestamp, String message, List<String> errorList) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.errorList = errorList;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", errorList=" + errorList +
                '}';
    }

}
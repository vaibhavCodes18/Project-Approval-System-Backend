package com.bit.ProjectApprovalSystem.response;

import java.time.LocalDateTime;

public class ErrorResponse {
    private Integer status;
    private String msg;
    private LocalDateTime localDateTime;

    public ErrorResponse() {
    }

    public ErrorResponse(Integer status, String msg, LocalDateTime localDateTime) {
        this.status = status;
        this.msg = msg;
        this.localDateTime = LocalDateTime.now();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = LocalDateTime.now();
    }
}

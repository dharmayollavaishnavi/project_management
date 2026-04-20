package com.example.dto;

public class TaskProgressDTO {

    private String status;
    private Integer completion;
    private Integer pending;

    private String date;   // already present
    private String time;   // ✅ ADD THIS

    // getters & setters

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCompletion() { return completion; }
    public void setCompletion(Integer completion) { this.completion = completion; }

    public Integer getPending() { return pending; }
    public void setPending(Integer pending) { this.pending = pending; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // ✅ NEW GETTER & SETTER
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
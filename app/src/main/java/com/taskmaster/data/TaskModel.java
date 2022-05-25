package com.taskmaster.data;

public class TaskModel {

    private final String title;
    private final String body;
    private String state;

    public enum State{
        NEW,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETE
    }

    public TaskModel(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

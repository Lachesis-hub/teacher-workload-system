package com.it.pojo;

//todo: 工作量模型
public class Workload {
    private String id;
    private String teacher;
    private String workDate;
    private float hours;
    private String description;

    public Workload() {
    }
    public Workload(String id, String teacher, String workDate, float hours, String description) {
        this.id = id;
        this.teacher = teacher;
        this.workDate = workDate;
        this.hours = hours;
        this.description = description;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }


    public String getWorkDate() {
        return workDate;
    }
    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public float getHours() {
        return hours;
    }
    public void setHours(float hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
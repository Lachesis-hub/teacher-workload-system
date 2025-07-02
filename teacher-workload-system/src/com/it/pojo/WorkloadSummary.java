package com.it.pojo;

public class WorkloadSummary {
    private String teacher;
    private String month;
    private float totalHours;

    public WorkloadSummary(String teacher, String month, float totalHours) {
        this.teacher = teacher;
        this.month = month;
        this.totalHours = totalHours;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(float totalHours) {
        this.totalHours = totalHours;
    }
}

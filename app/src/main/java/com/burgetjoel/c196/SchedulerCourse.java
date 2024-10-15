package com.burgetjoel.c196;

public class SchedulerCourse {
    private String name;
    private String startDate;
    private String endDate;
    private String color;

    public SchedulerCourse(String name, String startDate, String endDate, String color) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getColor() {
        return color;
    }
}

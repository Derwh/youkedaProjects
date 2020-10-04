package pers.Derwh.courseScheduling.model;

import java.util.List;

public class Course {

    private String id;
    private String name;
    private int sections;
    private List<WeekPlan> weekPlans;

    public List<WeekPlan> getWeekPlans() {
        return weekPlans;
    }

    public void setWeekPlans(List<WeekPlan> weekPlans) {
        this.weekPlans = weekPlans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSections() {
        return sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }
}

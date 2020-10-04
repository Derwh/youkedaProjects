package pers.Derwh.courseScheduling.Service.impl;

import pers.Derwh.courseScheduling.Service.CourseService;
import pers.Derwh.courseScheduling.model.Course;

import java.util.HashMap;
import java.util.Map;

public class CourseServiceImpl implements CourseService {

    //存储课程
    private Map<String, Course> COURSES = new HashMap<>();

    @Override
    public void add(Course course) {
        COURSES.put(course.getId(), course);
    }

    @Override
    public Course get(String courseId) {
        return COURSES.get(courseId);
    }
}

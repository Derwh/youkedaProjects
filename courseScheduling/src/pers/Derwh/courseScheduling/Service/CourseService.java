package pers.Derwh.courseScheduling.Service;

import pers.Derwh.courseScheduling.model.Course;

public interface CourseService {

    //添加课程
    void add(Course course);
    //根据课程id获取课程
    Course get(String courseId);
}

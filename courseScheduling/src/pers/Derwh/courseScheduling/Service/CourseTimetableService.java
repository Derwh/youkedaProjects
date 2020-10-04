package pers.Derwh.courseScheduling.Service;

import pers.Derwh.courseScheduling.model.CourseTimetable;

import java.time.LocalDateTime;

public interface CourseTimetableService {

    //进行课程的课表安排
    void invoke(CourseTimetable courseTimetable, LocalDateTime startDay, LocalDateTime endDay);
    //查询某个班的课表安排
    CourseTimetable queryForClasses(String classesId);
}

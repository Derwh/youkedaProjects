package pers.Derwh.courseScheduling.Service.impl;

import pers.Derwh.courseScheduling.Service.CourseTimetableService;
import pers.Derwh.courseScheduling.model.CourseTimetable;
import pers.Derwh.courseScheduling.model.Timetable;
import pers.Derwh.courseScheduling.model.WeekPlan;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CourseTimetableServiceImpl implements CourseTimetableService {

    //存储所有的课程安排
    private List<CourseTimetable> COURSE_TIMETABLE = new ArrayList<>();

    @Override
    public void invoke(CourseTimetable courseTimetable, LocalDateTime startDay, LocalDateTime endDay) {

        if (courseTimetable.getTimetables() == null) {
            courseTimetable.setTimetables(new ArrayList<>());
        }

        //获取上课的总天数
        long days = Duration.between(startDay, endDay).toDays();

        //待分配课程的总节数
        int sections = courseTimetable.getCourse().getSections();

        for (int i = 0; i < days; i++) {
            //课程分配完则跳出
            if (sections == 0) {
                break;
            }

            //每次循环递进的当前时间
            LocalDateTime now = startDay.plusDays(i);

            for (WeekPlan weekPlan : courseTimetable.getCourse().getWeekPlans()) {
                //当前日期的星期数若与课程上课时间的星期数相同，则排课
                if (now.getDayOfWeek().getValue() == weekPlan.getWeekDay()) {

                    Timetable timetable = new Timetable();
                    //随机获取课表id
                    timetable.setId(UUID.randomUUID().toString());

                    LocalDateTime begin = now;

                    if (weekPlan.getTime().equals("am")) {
                        begin = begin.withHour(8).minusMinutes(0);
                    } else {
                        begin = begin.withHour(14).minusMinutes(0);
                    }

                    timetable.setStartTime(begin);
                    timetable.setEndTime(begin.plusHours(weekPlan.getCount()));

                    courseTimetable.getTimetables().add(timetable);
                    sections = sections - weekPlan.getCount();
                }
            }
        }

        COURSE_TIMETABLE.add(courseTimetable);
    }

    @Override
    public CourseTimetable queryForClasses(String classesId) {
        for (CourseTimetable courseTimetable : COURSE_TIMETABLE) {
            if (courseTimetable.getClasses().getId().equals(classesId)) {
                return courseTimetable;
            }
        }
        return null;
    }
}

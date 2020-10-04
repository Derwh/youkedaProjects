package pers.Derwh.courseScheduling.test;

import pers.Derwh.courseScheduling.Service.ClassesService;
import pers.Derwh.courseScheduling.Service.CourseService;
import pers.Derwh.courseScheduling.Service.CourseTimetableService;
import pers.Derwh.courseScheduling.Service.impl.ClassesServiceImpl;
import pers.Derwh.courseScheduling.Service.impl.CourseServiceImpl;
import pers.Derwh.courseScheduling.Service.impl.CourseTimetableServiceImpl;
import pers.Derwh.courseScheduling.model.Classes;
import pers.Derwh.courseScheduling.model.Course;
import pers.Derwh.courseScheduling.model.CourseTimetable;
import pers.Derwh.courseScheduling.model.WeekPlan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CourseTimetableTest {

    private static ClassesService classesService = new ClassesServiceImpl();
    private static CourseService courseService = new CourseServiceImpl();
    private static CourseTimetableService courseTimetableService = new CourseTimetableServiceImpl();

    public static void main(String[] args) {

        initClasses();
        initCourse();

        LocalDateTime startDay = LocalDateTime.of(2019, 9, 1, 8, 0, 0);
        LocalDateTime endDay = LocalDateTime.of(2020, 1, 15, 18, 0, 0);

        //仅以1班的高等数学为例
        courseTimetableService.invoke(initCourseTimetable(classesService.get("A"), courseService.get("1"), "王五"), startDay, endDay);
        courseTimetableService.invoke(initCourseTimetable(classesService.get("A"), courseService.get("2"), "王六"), startDay, endDay);

        System.out.println(classesService.get("A").getName());
        System.out.println(courseService.get("1").getName());
        int last = courseTimetableService.queryForClasses("A").getTimetables().size() - 1;
        System.out.println("第四次上课时间为：" + courseTimetableService.queryForClasses("A").getTimetables().get(3).getStartTime());
        System.out.println("最后一次上课时间为：" + courseTimetableService.queryForClasses("A").getTimetables().get(last).getStartTime());
    }

    //初始化班级数据
    public static void initClasses() {

        setClassesData("A", "18级计算机1班", 2018);
        setClassesData("B", "19级计算机2班", 2019);
    }

    //设置班级数据
    public static void setClassesData(String id, String name, int startYear) {
        Classes classes = new Classes();
        classes.setId(id);
        classes.setName(name);
        classes.setStartYear(startYear);
        classesService.add(classes);
    }

    //初始化课程数据
    public static void initCourse() {

        List<WeekPlan> weekPlans = new ArrayList<>();
        weekPlans.add(setWeekPlanData(2, 2, "am"));
        weekPlans.add(setWeekPlanData(4, 2, "pm"));
        setCourseData("1", "高等数学", 48, weekPlans);

        weekPlans = new ArrayList<>();
        weekPlans.add(setWeekPlanData(1, 2, "am"));
        weekPlans.add(setWeekPlanData(4, 2, "am"));
        setCourseData("2", "C语言", 50, weekPlans);

        weekPlans = new ArrayList<>();
        weekPlans.add(setWeekPlanData(3, 2, "am"));
        setCourseData("3", "编译原理", 20, weekPlans);

        weekPlans = new ArrayList<>();
        weekPlans.add(setWeekPlanData(1, 2, "am"));
        weekPlans.add(setWeekPlanData(4, 2, "pm"));
        setCourseData("4", "Java语言", 56, weekPlans);
    }

    //设置课程数据
    public static void setCourseData(String id, String name, int sections, List<WeekPlan> weekPlans) {

        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setSections(sections);
        course.setWeekPlans(weekPlans);
        courseService.add(course);
    }

    //设置weekplan数据
    public static WeekPlan setWeekPlanData(int weekDay, int count, String time) {
        WeekPlan weekPlan = new WeekPlan();
        weekPlan.setWeekDay(weekDay);
        weekPlan.setTime(time);
        weekPlan.setCount(count);
        return weekPlan;
    }

    //初始化课表安排
    public static CourseTimetable initCourseTimetable(Classes classes, Course course, String teacher) {

        CourseTimetable courseTimetable = new CourseTimetable();
        courseTimetable.setClasses(classes);
        courseTimetable.setCourse(course);
        courseTimetable.setTeacher(teacher);

        return courseTimetable;
    }
}

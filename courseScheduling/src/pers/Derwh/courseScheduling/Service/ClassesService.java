package pers.Derwh.courseScheduling.Service;

import pers.Derwh.courseScheduling.model.Classes;

import java.util.List;

public interface ClassesService {

    //添加班级
    void add(Classes classes);
    //获取所有班级记录
    List<Classes> getAll();
    //获取班级数据
    default Classes get(String classesId) {
        List<Classes> CLASSES = this.getAll();

        for (Classes classes : CLASSES) {
            if (classes.getId().equals(classesId)) {
                return classes;
            }
        }
        return null;
    }
}

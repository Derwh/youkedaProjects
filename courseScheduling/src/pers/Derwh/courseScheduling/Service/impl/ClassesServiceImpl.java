package pers.Derwh.courseScheduling.Service.impl;

import pers.Derwh.courseScheduling.Service.ClassesService;
import pers.Derwh.courseScheduling.model.Classes;

import java.util.ArrayList;
import java.util.List;

public class ClassesServiceImpl implements ClassesService {

    //存放班级数据
    private List<Classes> CLASSES = new ArrayList<>();
    @Override
    public void add(Classes classes) {
        CLASSES.add(classes);
    }

    @Override
    public List<Classes> getAll() {
        return CLASSES;
    }
}

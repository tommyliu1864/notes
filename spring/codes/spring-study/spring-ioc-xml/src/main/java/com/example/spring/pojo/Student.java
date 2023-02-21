package com.example.spring.pojo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private String sex;
    private School school;
    private String[] hobbies;
    private List<Teacher> teachers;
    //最喜欢的三个老师
    private Map<String, Teacher> favoriteTeachers;

    public Student() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Map<String, Teacher> getFavoriteTeachers() {
        return favoriteTeachers;
    }

    public void setFavoriteTeachers(Map<String, Teacher> favoriteTeachers) {
        this.favoriteTeachers = favoriteTeachers;
    }

    public Student(Integer id, String name, Integer age, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", school=" + school +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", teachers=" + teachers +
                ", favoriteTeachers=" + favoriteTeachers +
                '}';
    }
}

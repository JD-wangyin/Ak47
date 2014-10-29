package com.wangyin.ak47.common;

public class Person {
    private String name;
    private int age;
    private String job;
    private String sex;
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    @Override
    public String toString(){
        return "Person("+hashCode()+"): "+name+","+sex+","+age+","+job;
    }
}
package com.wangyin.ak47.common;

import junit.framework.Assert;

import org.junit.Test;

import com.wangyin.ak47.common.YmlUtil;

public class YmlUtilTest {
    
    public static class Person {
        private String name;
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
        public boolean getSex() {
            return sex;
        }
        public void setSex(boolean sex) {
            this.sex = sex;
        }
        private int age;
        private boolean sex;
    }

    @Test
    public void testme(){
        Person p1 = new Person();
        p1.setName("HanMeimei");
        p1.setAge(32);
        p1.setSex(false);
        
        String s1 = YmlUtil.obj2Yml(p1);
        Assert.assertEquals("!!com.wangyin.qa.ak47.common.YmlUtilTest$Person {age: 32, name: HanMeimei, sex: false}\n".length(), s1.length());
        
        Person p2 = (Person) YmlUtil.yml2Obj(s1);
        Assert.assertEquals(p1.getName(), p2.getName());
        Assert.assertEquals(p1.getAge(), p2.getAge());
        Assert.assertEquals(p1.getSex(), p2.getSex());
        
    }
}

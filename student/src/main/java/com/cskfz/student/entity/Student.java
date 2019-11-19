package com.cskfz.student.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 11:38 2019/11/19
 * @Modified By:
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private int sno;
    private String sname;
    private boolean isMale;
    private Date birth;
    private String imageUrl;

    public Student() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Student(int sno, String sname, boolean isMale, Date birth, String imageUrl) {
        super();
        this.sno = sno;
        this.sname = sname;
        this.isMale = isMale;
        this.birth = birth;
        this.imageUrl = imageUrl;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}



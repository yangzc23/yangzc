package com.cskfz.student.pojo;

import java.io.Serializable;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 17:24 2019/11/23
 * @Modified By:
 */
public class StudentVO implements Serializable {
    private String sid;
    private String sname;
    private String gender;
    private String birth;
    private String filePath;

    public StudentVO() {
    }

    public StudentVO(String sid, String sname, String gender, String birth, String filePath) {
        this.sid = sid;
        this.sname = sname;
        this.gender = gender;
        this.birth = birth;
        this.filePath = filePath;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

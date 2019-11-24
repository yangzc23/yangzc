package com.cskfz.student.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 17:24 2019/11/23
 * @Modified By:
 */
public class StudentVO implements Serializable {
    @ApiModelProperty(value = "学号", name = "sid", example = "1001")
    private String sid;

    @ApiModelProperty(value = "姓名", name = "sname", required = true, example = "王鸥")
    private String sname;

    @ApiModelProperty(value = "性别", name = "gender", required = true, example = "0")
    private String gender;

    @ApiModelProperty(value = "生日", name = "birth", required = true, example = "1999-01-01")
    private String birth;

    @ApiModelProperty(value = "图片访问路径", name = "filePath", required = true, example = "upload/123.jpg")
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

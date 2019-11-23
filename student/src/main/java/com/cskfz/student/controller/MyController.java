package com.cskfz.student.controller;

import com.cskfz.student.entity.Student;
import com.cskfz.student.pojo.ActionResult;
import com.cskfz.student.pojo.StudentVO;
import com.cskfz.student.utils.DBUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 11:39 2019/11/19
 * @Modified By:
 */
@Api(value = "学生管理")
@Controller
@RequestMapping("/")
public class MyController {

    @Autowired
    private DBUtil dbUtil;

    @Value("${upload.file.path}")
    private String savePath;

    /**
     * 学生列表
     * @param params
     * @return
     */
    @ApiOperation(value = "学生列表", notes = "根据指定的页码和行数返回学生列表")
    @ApiImplicitParam(name = "params", paramType = "body", examples =
    @Example(value = {
            @ExampleProperty(mediaType = "application/json",value = "{\n" +
                    "\"page\": \"1\",\n" +
                    "\"rows\": \"5\"\n" +
                    "}")
    })
    )
    @PostMapping("/welcome")
    @ResponseBody
    public ActionResult getStudents(@RequestBody Map<String,String> params) {
        //PrintWriter pw = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        //
        Map<String,Object> data = new HashMap<String,Object>();
        List<Student> list = new ArrayList<Student>();
        ActionResult result = null;
        int rows = Integer.parseInt(params.get("rows"));
        int page = Integer.parseInt(params.get("page"));
        int begin = (page-1)*rows;
        try {
            conn = dbUtil.getConnection();
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM STUDENT");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            data.put("total", rs.getInt(1));
            stmt = conn.prepareStatement("SELECT * FROM STUDENT ORDER BY SNO LIMIT ?,?");
            stmt.setInt(1, begin);
            stmt.setInt(2, rows);
            rs = stmt.executeQuery();
            while(rs.next()) {
                list.add(new Student(rs.getInt(1),rs.getString(2),rs.getString(3).equals("男"),rs.getDate(4),rs.getString(5)));
            }
            data.put("rows", list);
            result = ActionResult.ok(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 学生编辑
     * @param sid
     * @return
     */
    @ApiOperation(value = "学生信息加载", notes = "根据学号获取该学生的信息")
    @ApiImplicitParam(name = "sid", value = "学号", dataType = "int", paramType = "path")
    @GetMapping("/edit/{sid}")
    @ResponseBody
    public ActionResult loadStudent(@PathVariable Integer sid){
        Connection conn = null;
        PreparedStatement stmt = null;
        ActionResult result = null;
        try {
            conn = dbUtil.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM STUDENT WHERE SNO=?");
            stmt.setInt(1, sid);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                result = ActionResult.ok(new Student(rs.getInt(1),rs.getString(2),rs.getString(3).equals("男"),rs.getDate(4),rs.getString(5)));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 学生保存
     * @param stu
     * @return
     */
    @ApiOperation(value = "学生信息保存", notes = "将输入的学生信息保存到数据库")
    @ApiImplicitParam(name = "Student", value = "学生对象")
    @PostMapping("/save")
    @ResponseBody
    public ActionResult saveStudent(StudentVO stu){
        Connection conn = null;
        PreparedStatement stmt = null;
        ActionResult result = null;
        String sno = stu.getSid();
        //
        String sname = stu.getSname();
        //
        String gender = stu.getGender().equals("0")?"女":"男";
        //
        String birth = stu.getBirth();
        //
        String filePath = stu.getFilePath();
        //
        try {
            conn = dbUtil.getConnection();
            if(sno==null||sno.equals("")) {
                stmt = conn.prepareStatement("INSERT INTO STUDENT(SNAME,GENDER,BIRTH,PHOTO_URL) VALUES(?,?,?,?)");
                stmt.setString(1, sname);
                stmt.setString(2, gender);
                stmt.setString(3, birth);
                stmt.setString(4, filePath);
            }else {
                stmt = conn.prepareStatement("UPDATE STUDENT SET SNAME=?,GENDER=?,BIRTH=?,PHOTO_URL=? WHERE SNO=?");
                stmt.setString(1, sname);
                stmt.setString(2, gender);
                stmt.setString(3, birth);
                stmt.setString(4, filePath);
                stmt.setInt(5, Integer.parseInt(sno));
            }
            stmt.executeUpdate();
            result = ActionResult.ok();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                conn.close();//关闭数据库的连接
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 学生删除
     * @param sid
     * @return
     */
    @ApiOperation(value = "删除学生信息", notes = "根据学号删除该学生的信息")
    @ApiImplicitParam(name = "sid", value = "学号", dataType = "int", paramType = "path")
    @DeleteMapping("/delete/{sid}")
    @ResponseBody
    public ActionResult delStudent(@PathVariable Integer sid){
        Connection conn = null;
        PreparedStatement stmt = null;
        ActionResult result = null;
        try {
            conn = dbUtil.getConnection();
            stmt = conn.prepareStatement("DELETE FROM STUDENT WHERE SNO = ?");
            stmt.setInt(1, sid);
            stmt.executeUpdate();
            result = ActionResult.ok();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                conn.close();//关闭数据库的连接
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 文件上传
     * @param file
     * @param req
     * @return
     */
    @ApiOperation(value = "头像上传", notes = "文件上传")
    @ApiImplicitParam(name = "source", value = "图片", dataType = "__file", required = true, paramType = "form")
    @PostMapping("/upload/file")
    @ResponseBody
    public ActionResult uploadFile(@RequestParam("source") MultipartFile file, HttpServletRequest req) {
        ActionResult result = null;
        try {
            // 截取不同类型的文件需要自行判断
            String filename = file.getOriginalFilename();
            if (!file.isEmpty()) {
                String extName = filename.substring(filename.indexOf("."));// 取文件格式后缀名
                String uuid = UUID.randomUUID().toString().replace("-", "");
                // 新名称
                String newName = uuid + extName;// 在这里用UUID来生成新的文件夹名字，这样就不会导致重名
                file.transferTo(new File(savePath+"/"+newName));
                result = ActionResult.ok("upload/"+newName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}



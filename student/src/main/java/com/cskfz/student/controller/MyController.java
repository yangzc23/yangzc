package com.cskfz.student.controller;

import com.cskfz.student.entity.Student;
import com.cskfz.student.pojo.ActionResult;
import com.cskfz.student.utils.DBUtil;
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
@Controller
@RequestMapping("/")
public class MyController {

    @Autowired
    private DBUtil dbUtil;

    @Value("${upload.file.path}")
    private String savePath;

    /**
     * 学生列表
     * @param map
     * @return
     */
    @RequestMapping("/welcome")
    @ResponseBody
    public ActionResult getStudents(@RequestBody Map<String,String> map) {
        //PrintWriter pw = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        //
        Map<String,Object> data = new HashMap<String,Object>();
        List<Student> list = new ArrayList<Student>();
        ActionResult result = null;
        int rows = Integer.parseInt(map.get("rows"));
        int page = Integer.parseInt(map.get("page"));
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
    @RequestMapping("/edit/{sid}")
    @ResponseBody
    public ActionResult loadStudent(@PathVariable int sid){
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
     * @param req
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ActionResult saveStudent(HttpServletRequest req){
        Connection conn = null;
        PreparedStatement stmt = null;
        ActionResult result = null;
        String sno = req.getParameter("sid");
        //
        String sname = req.getParameter("sname");
        //
        String gender = req.getParameter("gender").equals("0")?"女":"男";
        //
        String birth = req.getParameter("birth");
        //
        String filePath = req.getParameter("filePath");
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
    @RequestMapping("/delete/{sid}")
    @ResponseBody
    public ActionResult delStudent(@PathVariable int sid){
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
    @RequestMapping("/upload/file")
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



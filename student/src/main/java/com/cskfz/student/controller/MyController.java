package com.cskfz.student.controller;

import com.cskfz.student.controller.agent.annotation.ApiJsonObject;
import com.cskfz.student.controller.agent.annotation.ApiJsonProperty;
import com.cskfz.student.entity.Student;
import com.cskfz.student.pojo.ActionResult;
import com.cskfz.student.pojo.StudentVO;
import com.cskfz.student.utils.DBUtil;
import com.cskfz.student.utils.DownloadUtil;
import io.swagger.annotations.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: yangzc
 * @Description:
 * @Date: Created on 11:39 2019/11/19
 * @Modified By:
 */
@Api(value = "/", tags = {"学生管理接口"}, description = "主要功能：学生信息的增删改查")
@Controller
@RequestMapping("/")
public class MyController {

    @Autowired
    private DBUtil dbUtil;

    @Value("${upload.file.path}")
    private String savePath;

    @Autowired
    private ServletContext servletContext;


    /**
     * 学生列表
     * @param params
     * @return
     */
    @ApiOperation(value = "学生列表", notes = "根据指定的页码和行数返回学生列表")
    @PostMapping(value = "/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionResult getStudents(@ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(key = "page", example = "1", description = "页码"),
            @ApiJsonProperty(key = "rows", example = "5", description = "行数")
    }) @RequestBody Map<String,String> params) {
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
    @ApiImplicitParam(name = "sid", value = "学号", dataType = "int", paramType = "path", example = "1001")
    @GetMapping(value = "/edit/{sid}", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @ApiImplicitParam(name = "sid", value = "学号", dataType = "int", paramType = "path", example = "1001")
    @DeleteMapping(value = "/delete/{sid}",  produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PostMapping(value = "/upload/file", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(value = "/export")
    public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{
        //1.创建工作簿
        //String path = servletContext.getRealPath("/");
        //path = path+"/make/students.xls";   //得到模板文件所在位置

        File file = ResourceUtils.getFile("classpath:make/students.xls");

        InputStream is = new FileInputStream(file);     //根据文件，得到指定的文件流

        //根据文件流，加载指定的工作簿
        //它只能操作excel2003版本
        Workbook wb = new HSSFWorkbook(is);

        //2.读取工作表
        Sheet sheet = wb.getSheetAt(0);   //0代表工作表的下标

        //抽取出一些公用变量
        Row nRow=null;
        Cell nCell = null;

        int rowNo=1;//行号
        int cellNo=0;//列号

        //===========================================数据内容
        nRow = sheet.getRow(rowNo);//读取第2行

        //
        CellStyle snoCellStyle = nRow.getCell(cellNo++).getCellStyle();//读取单元格的样式
        String str = nRow.getCell(cellNo).getStringCellValue();//读取单元格的内容
        System.out.println(str);

        CellStyle snameCellStyle = nRow.getCell(cellNo++).getCellStyle();//读取单元格的样式
        CellStyle isMaleCellStyle = nRow.getCell(cellNo++).getCellStyle();//读取单元格的样式
        CellStyle birthCellStyle = nRow.getCell(cellNo++).getCellStyle();//读取单元格的样式

        Connection conn = null;
        PreparedStatement stmt = null;
        List<Student> list = new ArrayList<Student>();
        try {
            conn = dbUtil.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM STUDENT ORDER BY SNO");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                list.add(new Student(rs.getInt(1),rs.getString(2),rs.getString(3).equals("男"),rs.getDate(4),rs.getString(5)));
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

        //遍历学生列表
        for(Student stu :list){
            //产生一个新行
            nRow = sheet.createRow(rowNo++);
            //nRow.setHeightInPoints(24f);//设置行高

            cellNo=0;

            nCell = nRow.createCell(cellNo++);//创建单元格
            nCell.setCellValue(stu.getSno());//设置单元格内容
            nCell.setCellStyle(snoCellStyle);    //设置单元格样式

            nCell = nRow.createCell(cellNo++);//创建单元格
            nCell.setCellValue(stu.getSname());//设置单元格内容
            nCell.setCellStyle(snameCellStyle);    //设置单元格样式

            nCell = nRow.createCell(cellNo++);//创建单元格
            nCell.setCellValue(stu.isMale());//设置单元格内容
            nCell.setCellStyle(isMaleCellStyle);    //设置单元格样式

            nCell = nRow.createCell(cellNo++);//创建单元格
            nCell.setCellValue(stu.getBirth());//设置单元格内容
            nCell.setCellStyle(birthCellStyle);    //设置单元格样式

        }

        //输出
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//内存的缓冲区
        wb.write(byteArrayOutputStream);

        DownloadUtil downloadUtil = new DownloadUtil();
        Calendar cal = Calendar.getInstance();
        //response.setHeader("Transfer-Encoding", "chunked");
        String returnName = "students" + cal.get(Calendar.YEAR) +
                (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DAY_OF_MONTH) +
                cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) +
                cal.get(Calendar.SECOND) + ".xls";
        downloadUtil.download(byteArrayOutputStream, response, returnName);
    }


    @PostMapping("import")
    @ResponseBody
    public ActionResult upload(@PathVariable("file") MultipartFile file) throws Exception {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
            List<StudentVO> getData = readOldExcel(hssfWorkbook);
            if (getData == null) {
                return new ActionResult(-1,"解析文件失败",null);
            }
            file.getInputStream().close();

        Connection conn = null;
        PreparedStatement stmt = null;
        //
        try {
            conn = dbUtil.getConnection();
            for(StudentVO stu:getData){
                stmt = conn.prepareStatement("INSERT INTO STUDENT(SNAME,GENDER,BIRTH) VALUES(?,?,?)");
                stmt.setString(1, stu.getSname());
                stmt.setString(2, stu.getGender());
                stmt.setString(3, stu.getBirth());
                stmt.executeUpdate();
            }
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
        return ActionResult.ok(getData);
    }

    //处理2007之前的excel
    private List<StudentVO> readOldExcel(HSSFWorkbook hssfWorkbook) {
        List<StudentVO> students = new ArrayList<StudentVO>();
        HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
        HSSFCell cell = null;
        HSSFRow row = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = sheetAt.getFirstRowNum()+1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
            row = sheetAt.getRow(i);
            if (row == null) {
                continue;
            }
            Object[] objects = new Object[row.getLastCellNum()];
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        objects[j] = cell.getStringCellValue();
                        System.out.println(cell.getStringCellValue());
                        break;
                    case _NONE:
                        objects[j] = "";
                        break;
                    case BOOLEAN:
                        objects[j] = cell.getBooleanCellValue();
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case NUMERIC:
                        //处理double类型的  1.0===》1
                        DecimalFormat df = new DecimalFormat("0");
                        String s = df.format(cell.getNumericCellValue());
                        objects[j] = s;
                        System.out.println(s);
                        break;
                    default:
                        objects[j] = cell.toString();
                }
            }
            //处理数据
            if (objects != null) {
                StudentVO stu = new StudentVO();
                stu.setSname((String) objects[1]);
                stu.setGender((String)objects[2]);
                stu.setBirth(sdf.format(row.getCell(3).getDateCellValue()));
                students.add(stu);
            }
        }
        return students;
    }

}



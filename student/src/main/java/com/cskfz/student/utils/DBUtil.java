package com.cskfz.student.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBUtil {

	@Value("${spring.datasource.driver-class-name}")
	private String driver;//数据库驱动名

	@Value("${spring.datasource.url}")
	private String url;//数据库连接地址

	@Value("${spring.datasource.username}")
	private String user;//数据库账号

	@Value("${spring.datasource.password}")
	private String passwd;//数据库密码



	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection()
		throws Exception{
		Connection con = null;
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,passwd);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return con;
	}
	
	/**
	 * 关闭数据库连接
	 * @param con
	 * @throws SQLException
	 */
	public void close(Connection con) throws SQLException{
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
}

/*
 * Basic IMP table entries for new database setup
 * insert into `tbluser` (`userid`,`username`,`password`,`userlevel`,`usertype`,`status`,`designation`,`nooffilerecperpage`,`admintools`,`usermanagement`,`createuser`,`edituser`,`viewuser`,`deactivateuser`,`cloneuser`,`groupmanagement`,
`creategroup`,`editgroup`,`viewgroup`,`deletegroup`,`deptmanagement`,`createdept`,`editdept`,`viewdept`,`deletedept`,`accessmanagement`,`approver`,`uidcreatedby`,`uidcreatedatetime`,`uideditedby`,`uideditdatetime`,
`uiddeletedby`,`uiddeletedatetime`,`userfirstaccess`,`passwordgenerationdatetime`) 
values 
(1,'superadmin','superadmin','65165','78495','A','Administrator',5,'19149','23173','10265','16384','49408','52514','60512','60612','98773','74857','15930','49132','39220','80381','45422','89578','63674','99783','30853',1,now(),0,'0000-00-00 00:00:00',0,'0000-00-00 00:00:00','0000-00-00 00:00:00','0000-00-00 00:00:00');

//=============================
insert into `tbluserpersonal` (`empid`,`userid`,`firstname`,`middlename`,`lastname`,`address`,`city`,`state`,`pincode`,`landmark`,`email`,`phone`) values (1,1,'super','admin','admin','office','office','office','office','office','office@office.com','123456');
//=============================

insert into `tblfolders` (`folderid`,`foldername`,`parentid`,`createtimestamp`,`createdby`,`edittimestamp`,`editedby`,`curnoncur`,`comment`,`pathid`,`departmentid`) 
values 
(1,'TechspeedDMS',-1,now(),1,'0000-00-00 00:00:00',NULL,'C','This is root folder',1,0);
//=============================
 
 insert into `tblpath` (`pathid`,`path`,`curnoncur`,`folderid`) values ( NULL,'TechspeedDMSTest/','C','1')

 * 
 */

package com.techspeed.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionManager {

	// static Connection con;
	protected Connection con;
	static String url;
	private static String dbname;

	public Connection dbConnection() {
		
		dbname = "projectmanagement9";
			try{  
				Class.forName("com.mysql.jdbc.Driver");  
				con=DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmanagement9","root","");  

		} catch (Exception ne) {
			ne.printStackTrace();
		}
			return con;

	}

	public static String getDBName() {
		return dbname;
	}

	// public static Connection getConnection(){
	public Connection getConnection() {

		try {
			// Following url line format is used for Mysql database, Driver version is mysql-connector-java-5.1.21-bin.jar
			String url = "jdbc:mysql://localhost:3306/mysql"; // Development Server
			//String url = "jdbc:mysql://192.168.1.78:3306/mysql"; // Development Server
			//String url = "jdbc:mysql://192.168.1.22:3306/mysql"; // Yogesh Development Server
			//String url = "jdbc:mysql://192.168.1.217:3306/mysql"; // www 217 Server
			//String url = "jdbc:mysql://192.168.1.15:3306/mysql"; // master

			// Following line is used for MariaDB database, Driver version is
			// mysql-connector-java-5.1.21-bin.jar
			// Class.forName("org.mariadb.jdbc.Driver");
			// Class.forName("com.mariadb.jdbc.Driver");
			// Following line is used for Mysql database, Driver version is mysql-connector-java-5.1.21-bin.jar
			Class.forName("com.mysql.jdbc.Driver");
			// following line is for older mysql driver may be for version3.something
			// Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("class is loaded");
			try {
				con = DriverManager.getConnection(url, "root", "");
				//con = DriverManager.getConnection(url, "root", "yogesh");
				//con = DriverManager.getConnection(url, "root", "Mahesh#321");
				//con = DriverManager.getConnection(url, "root", "miracle");//master
				
			} catch (SQLException ex) {
				System.out.println("SQLException");
				ex.getMessage();
				ex.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		return con;
	}
}

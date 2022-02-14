package com.techspeed.project.export;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.techspeed.dbconnection.*;
import com.techspeed.model.DetailEntry;
import com.techspeed.model.ExportDetails;

public class ExportReportDAO {

	public ArrayList<ExportDetails> getExportReport(String fdate, String todate, String user,String batch) throws SQLException {

		System.out.println("In ExportDAO.getExportReport() to getdata.");

		ArrayList<ExportDetails> objlstExportDetails = new ArrayList<ExportDetails>();

		String query = "SELECT t1.projectid,t1.projectname,t1.entrytype,t2.userid,t3.batchname,t3.batchid  FROM "
				+ " projectmanagement9.tblprojectdetails t1 "
				+ " INNER JOIN projectmanagement9.tblprojectallocation t2 ON t2.projectid = t1.projectid,  "
				+ "`oo_berelectric`.tblbatchdetails t3 WHERE t2.projectid=t3.projectid  "
				+ " AND t1.activeflag='Y' "
				+ "GROUP BY t1.projectid,t1.projectname,t1.entrytype,t2.userid,t3.batchname,t3.batchid "
				+ "ORDER BY t1.projectid,t3.batchid,t2.userid  ";

		System.out.println(query);

		ResultSet rs = null;
		PreparedStatement psmt = null;
		ConnectionManager objConnectionManager = new ConnectionManager();
		Connection currentCon = objConnectionManager.dbConnection();
		System.out.println(":-->" + currentCon);
		psmt = currentCon.prepareStatement(query);
		rs = psmt.executeQuery();

		while (rs.next()) {

			ExportDetails objExportDetails = new ExportDetails();

			objExportDetails.setUserid(rs.getInt("userid"));
			objExportDetails.setProjectid(rs.getInt("projectid"));
			objExportDetails.setProjectname(rs.getString("projectname"));
			objExportDetails.setBatchname(rs.getString("batchname"));
			objExportDetails.setBatchid(rs.getInt("batchid"));
			objlstExportDetails.add(objExportDetails);
		}

		rs.close();
		psmt.close();
		currentCon.close();
		return objlstExportDetails;
	}

	public HashMap<String, ArrayList<DetailEntry>> getAddEntry(String batch, String user,String fdate,String todate) {
		HashMap<String, ArrayList<DetailEntry>> addEntry = new HashMap<String, ArrayList<DetailEntry>>();
		
		String query=" SELECT IFNULL(df.addedby,0) as addedby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`addeddatetime`,df.`addedindatetime`)))) `Total TIME(Hrs) FIRST added`, "
				+ " COUNT(df.dataid) `Total Entries added FIRST`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.addeddatetime,df.addedindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) added FIRST` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid "
				+ " WHERE df.addedby!=0"
				+ " AND df.addedindatetime >='"+fdate+"' "
				+ " AND df.addedindatetime <='"+todate+"' AND df.addedby=30 AND df.batchid='"+batch+"'  ";

		
		System.out.println(query);

		ResultSet rs = null;
		PreparedStatement psmt = null;
		ConnectionManager objConnectionManager = new ConnectionManager();
		Connection currentCon = objConnectionManager.dbConnection();
		System.out.println(":-->" + currentCon);
		try {
			psmt = currentCon.prepareStatement(query);
			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("addedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries added FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries added FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}
		} catch (Exception e) {
			
		}

		// Edit Query

		query = "SELECT IFNULL(df.editedby,0) as editedby,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`editdatetime`,df.`editindatetime`)))) `Total TIME(Hrs) FIRST edit` ,"
				+ " COUNT(df.dataid) `Total Entries edit FIRST`, "
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.editdatetime,df.editindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) edit FIRST` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid WHERE df.editedby!=0 "
				+ " AND df.editindatetime >='"+fdate+"' "
				+ " AND df.editindatetime <='"+todate+"' AND df.editedby=86  AND df.batchid='"+batch+"' ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("editedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries edit FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries edit FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// viewby first Query

		query = "SELECT IFNULL(df.viewedby,0) as viewedby ,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewdatetime`,df.`viewindatetime`)))) `Total TIME(Hrs) FIRST viewedby`,"
				+ " COUNT(df.dataid) `Total Entries viewedby FIRST`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewdatetime,df.viewindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewedby FIRST`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid WHERE df.viewedby!=0 "
				+ " AND df.viewindatetime >='"+fdate+"' "
				+ " AND df.viewindatetime <='"+todate+"' AND df.viewedby=30  AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewedby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewedby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		// viewblur first Query

		query = "SELECT IFNULL(df.viewblurby,0) as viewblurby ,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblurdatetime`,df.`viewblurindatetime`)))) `Total TIME(Hrs) FIRST viewblurby`,"
				+ " COUNT(df.dataid) `Total Entries viewblurby FIRST`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblurdatetime,df.viewblurindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblurby FIRST`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblurby!=0 "
				+ " AND df.viewblurindatetime >='"+fdate+"' "
				+ " AND df.viewblurindatetime <='"+todate+"'  AND df.viewblurby=42 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblurby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View blur");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewblurby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View blur");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewblurby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// viewblank first Query

		query = "SELECT IFNULL(df.viewblankby,0) as viewblankby ,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblankdatetime`,df.`viewblankindatetime`)))) `Total TIME(Hrs) FIRST viewblank`,"
				+ " COUNT(df.dataid) `Total Entries viewblank FIRST` ,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblankdatetime,df.viewblankindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblank FIRST`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblankby!=0 "
				+ " AND df.viewblankindatetime >= '"+fdate+"' "
				+ " AND df.viewblankindatetime <='"+todate+"' AND df.viewblankby=42  AND df.batchid='"+batch+"' ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblankby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View blank");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewblank FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View blank");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewblank FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// viewncby first Query

		query = "SELECT IFNULL(df.viewncby,0) as viewncby ,IFNULL(df.batchid,0) as batchid,bd.batchname, "
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewncdatetime`,df.`viewncindatetime`)))) `Total TIME(Hrs) FIRST viewncby`,"
				+ " COUNT(df.dataid) `Total Entries viewncby FIRST`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewncdatetime,df.viewncindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewncby FIRST` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_first df "
				+ " ON bd.batchid=df.batchid WHERE df.viewncby!=0 "
				+ " AND df.viewncindatetime >='"+fdate+"' "
				+ " AND df.viewncindatetime <='"+todate+"' AND df.viewncby=30  AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewncby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewncby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("First");
					data.setTotalEntry(rs.getString("Total Entries viewncby FIRST"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FIRST viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby FIRST"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// Add RE Query

		query = "SELECT IFNULL(df.addedby,0) as addedby ,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`addeddatetime`,df.`addedindatetime`)))) `Total TIME(Hrs) RE added`, "
				+ " COUNT(df.dataid) `Total Entries added RE`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.addeddatetime,df.addedindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) added RE`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re  df "
				+ " ON bd.batchid=df.batchid WHERE df.addedby!=0 "
				+ " AND df.addedindatetime >='"+fdate+"' "
				+ " AND df.addedindatetime <='"+todate+"' AND df.addedby=42  AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("addedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries added RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries added RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// edit RE Query

		query = "SELECT IFNULL(df.editedby,0) as editedby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`editdatetime`,df.`editindatetime`)))) `Total TIME(Hrs) RE edit` ,"
				+ " COUNT(df.dataid) `Total Entries edit RE`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.editdatetime,df.editindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) edit RE`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re df "
				+ " ON bd.batchid=df.batchid WHERE df.editedby!=0 "
				+ " AND df.editindatetime >='"+fdate+"' "
				+ " AND df.editindatetime <='"+todate+"' AND df.editedby=2 AND df.batchid='"+batch+"' ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("editedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries edit RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries edit RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// viewby RE Query
		 query = "SELECT IFNULL(df.viewedby,0) as viewedby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
					+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewdatetime`,df.`viewindatetime`)))) `Total TIME(Hrs) RE viewedby`,"
					+ " COUNT(df.dataid) `Total Entries viewedby RE`,"
					+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewdatetime,df.viewindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewedby RE` "
					+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re df "
					+ " ON bd.batchid=df.batchid WHERE df.viewedby!=0 "
					+ " AND df.viewindatetime >='"+fdate+"' "
					+ " AND df.viewindatetime <='"+todate+"'  AND df.viewedby=2 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewedby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewedby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		// viewblankby RE Query

		query = "SELECT IFNULL(df.viewblankby,0) as viewblankby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblankdatetime`,df.`viewblankindatetime`)))) `Total TIME(Hrs) RE viewblank`, "
				+ " COUNT(df.dataid) `Total Entries viewblank RE`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblankdatetime,df.viewblankindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblank RE` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblankby!=0 "
				+ " AND df.viewblankindatetime >='"+fdate+"' "
				+ " AND df.viewblankindatetime <='"+todate+"' AND df.viewblankby=2 AND df.batchid= '"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblankby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View blank");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewblank RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View blankby");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewblank RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		// viewblurby RE Query

		query = "SELECT IFNULL(df.viewblurby,0) as viewblurby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblurdatetime`,df.`viewblurindatetime`)))) `Total TIME(Hrs) RE viewblurby`,"
				+ " COUNT(df.dataid) `Total Entries viewblurby RE`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblurdatetime,df.viewblurindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblurby RE`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblurby!=0 "
				+ " AND df.viewblurindatetime >='"+fdate+"' "
				+ " AND df.viewblurindatetime <='"+todate+"' AND df.viewblurby=2 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblurby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View blur");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewblurby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View blur");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewblurby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// viewncby RE Query

		query = "SELECT IFNULL(df.viewncby,0) as viewncby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewncdatetime`,df.`viewncindatetime`)))) `Total TIME(Hrs) RE viewncby`,"
				+ " COUNT(df.dataid) `Total Entries viewncby RE`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewncdatetime,df.viewncindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewncby RE` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_re df "
				+ " ON bd.batchid=df.batchid WHERE df.viewncby!=0 "
				+ " AND df.viewncindatetime >='"+fdate+"' "
				+ " AND df.viewncindatetime <='"+fdate+"' AND df.viewncby=3 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewncby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewncby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby RE"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("RE");
					data.setTotalEntry(rs.getString("Total Entries viewncby RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add final query

		query = "SELECT IFNULL(df.addedby,0) as addedby,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`addeddatetime`,df.`addedindatetime`)))) `Total TIME(Hrs) FINAL added`,"
				+ " COUNT(df.dataid) `Total Entries added FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.addeddatetime,df.addedindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) added FINAL` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df "
				+ " ON bd.batchid=df.batchid WHERE df.addedby!=0 "
				+ " AND df.addedindatetime >='"+fdate+"' "
				+ " AND df.addedindatetime <='"+fdate+"' AND df.addedby=50 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("addedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries added FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Add");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries added FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL added"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) added FINAL"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		// edit final Query

		query = "SELECT IFNULL(df.editedby,0) as editedby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`editdatetime`,df.`editindatetime`)))) `Total TIME(Hrs) FINAL edit` ,"
				+ " COUNT(df.dataid) `Total Entries edit FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.editdatetime,df.editindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) edit FINAL` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df  "
				+ " ON bd.batchid=df.batchid WHERE df.editedby!=0 "
				+ " AND df.editindatetime >='"+fdate+"' "
				+ " AND df.editindatetime <='"+todate+"' AND df.editedby=2 AND df.batchid='"+batch+"' ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("editedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries edit FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("Edit");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries edit RE"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) RE edit"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) edit RE"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// viewby Final Query

		query = "SELECT IFNULL(df.viewedby,0) as viewedby,IFNULL(df.batchid,0) as batchid,bd.batchname ,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewdatetime`,df.`viewindatetime`)))) `Total TIME(Hrs) FINAL viewedby`,"
				+ " COUNT(df.dataid) `Total Entries viewedby FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewdatetime,df.viewindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewedby FINAL` "
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df "
				+ " ON bd.batchid=df.batchid WHERE df.viewedby!=0 "
				+ " AND df.viewindatetime >='"+fdate+"' "
				+ " AND df.viewindatetime <='"+todate+"' AND df.viewedby=2 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewedby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewedby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewedby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewedby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewedby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// viewblankby Final Query
		query = "SELECT IFNULL(df.viewblurby,0) as viewblurby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblurdatetime`,df.`viewblurindatetime`)))) `Total TIME(Hrs) FINAL viewblurby`,"
				+ " COUNT(df.dataid) `Total Entries viewblurby FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblurdatetime,df.viewblurindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblurby FINAL`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblurby!=0 "
				+ " AND df.viewblurindatetime >='"+fdate+"' "
				+ " AND df.viewblurindatetime <='"+todate+"' AND df.viewblurby=2 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblurby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("view blur");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewblurby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("view blur");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewblurby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewblurby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblurby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		// viewblank Final Query

		query = "SELECT IFNULL(df.viewblankby,0) as viewblankby,IFNULL(df.batchid,0) as batchid,bd.batchname,"
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewblankdatetime`,df.`viewblankindatetime`)))) `Total TIME(Hrs) FINAL viewblank`,"
				+ " COUNT(df.dataid) `Total Entries viewblank FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewblankdatetime,df.viewblankindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewblank FINAL`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df "
				+ " ON bd.batchid=df.batchid WHERE df.viewblankby!=0 "
				+ " AND df.viewblankindatetime >='"+fdate+"' "
				+ " AND df.viewblankindatetime <='"+todate+"'  AND df.viewblankby=2 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewblankby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View blank");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewblank FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View blank");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewblank FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewblank"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewblank FINAL"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// viewncby Final Query

		query = "SELECT IFNULL(df.viewncby,0) as viewncby,IFNULL(df.batchid,0) as batchid,bd.batchname, "
				+ " SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(df.`viewncdatetime`,df.`viewncindatetime`)))) `Total TIME(Hrs) FINAL viewncby`,"
				+ " COUNT(df.dataid) `Total Entries viewncby FINAL`,"
				+ " SUBSTRING_INDEX(SEC_TO_TIME((SUM(TIME_TO_SEC(TIMEDIFF(df.viewncdatetime,df.viewncindatetime)))/COUNT(df.dataid))),'.',1) `Avereage TIME per Entry(Hrs) viewncby FINAL`"
				+ " FROM `oo_berelectric`.tblbatchdetails bd INNER JOIN  `oo_berelectric`.tbldata_final df "
				+ " ON bd.batchid=df.batchid WHERE df.viewncby!=0 "
				+ " AND df.viewncindatetime >='"+fdate+"' "
				+ " AND df.viewncindatetime <='"+todate+"' AND df.viewncby=3 AND df.batchid='"+batch+"'  ";

		System.out.println(query);
		try {
			psmt = currentCon.prepareStatement(query);

			rs = psmt.executeQuery();
			while (rs.next()) {
				String key = rs.getString("batchid").trim() + "@@" + rs.getString("viewncby").trim();
				System.out.println(addEntry.containsKey(key));
				if (addEntry.containsKey(key)) {
					ArrayList<DetailEntry> list = addEntry.get(key);
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewncby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				} else {
					ArrayList<DetailEntry> list = new ArrayList<DetailEntry>();
					DetailEntry data = new DetailEntry();
					data.setType("View nc");
					data.setProcess("Final");
					data.setTotalEntry(rs.getString("Total Entries viewncby FINAL"));
					data.setTotalTime(rs.getString("Total TIME(Hrs) FINAL viewncby"));
					data.setTotalAvg(rs.getString("Avereage TIME per Entry(Hrs) viewncby FINAL"));
					list.add(data);
					addEntry.put(key, list);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				psmt.close();
				currentCon.close();
			} catch (Exception e) {

			}
		}

		return addEntry;
	}

}

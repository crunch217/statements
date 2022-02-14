package com.techspeed.model;

public class ExportDetails {
	
	public int userid;
	public int batchid;
	public int projectid;
	public String projectname;
	public String entrytype;
	public String batchname;
	
	// Getters and Setters...
	
	
	public String getBatchname() {
		return batchname;
	}

	public void setBatchname(String batchname) {
		this.batchname = batchname;
	}

	public String getEntrytype() {
		return entrytype;
	}

	public void setEntrytype(String entrytype) {
		this.entrytype = entrytype;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getProjectid() {
		return projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	
	public int getBatchid() {
		return batchid;
	}

	public void setBatchid(int batchid) {
		this.batchid = batchid;
	}

	@Override
	public String toString() {
		return "ExportDetails [userid=" + userid + ", batchid=" + batchid + ", projectid=" + projectid
				+ ", projectname=" + projectname + ", entrytype=" + entrytype + ", batchname=" + batchname + "]";
	}

	

}

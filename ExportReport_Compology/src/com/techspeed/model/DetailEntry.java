package com.techspeed.model;
public class DetailEntry {
private String type;
private String process;
private String totalEntry;
private String totalTime;
private String totalAvg;
public String getTotalEntry() {
	return totalEntry;
}
public void setTotalEntry(String totalEntry) {
	this.totalEntry = totalEntry;
}
public String getTotalTime() {
	return totalTime;
}
public void setTotalTime(String totalTime) {
	this.totalTime = totalTime;
}
public String getTotalAvg() {
	return totalAvg;
}
public void setTotalAvg(String totalAvg) {
	this.totalAvg = totalAvg;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getProcess() {
	return process;
}
public void setProcess(String process) {
	this.process = process;
}
@Override
public String toString() {
	return "DetailEntry [type=" + type + ", process=" + process + ", totalEntry=" + totalEntry + ", totalTime="
			+ totalTime + ", totalAvg=" + totalAvg + "]";
}


}

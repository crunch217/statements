<%@page import="com.techspeed.model.DetailEntry"%>
<%@page import="com.techspeed.model.ExportDetails"%>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="Cache-Control" content="private">
<meta http-equiv="Cache-Control" content="no-store">
<meta http-equiv="Cache-Control" content="max-stale=0">
<meta http-equiv="Cache-Control" content="must-revalidate">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="plugins/jquery/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="bootstrap/css/jquery.datetimepicker.min.css"/>
<script src="bootstrap/js/jquery.datetimepicker.js" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {
$('#datetimepicker').datetimepicker({
     format:'Y-m-d H:i:s ',
     timepicker:true,
     
});
$('#datetimepicker1').datetimepicker({
    format:'Y-m-d H:i:s ',
    timepicker:true,
    
});
});
</script>
<style>
h1 {
	font-size: 28px;
	font-family: "Times New Roman", Times, serif;
	text-align: center;
}

.ExportReportform {
	background-color: #FFF8DC;
}

label {
	font-family: "Times New Roman", Times, serif;
	padding-left: 30px;
}

.btnsubmit {
	font-size: 14px;
	font-family: "Times New Roman", Times, serif;
	height: 24px;
	width: 100px;
	margin-left: 28px;
	border: 1px solid black;
}

input[type="date" i] {
	height: 24px;
	font-size: 14px;
	font-family: Times New Roman;
}

.entryTable {
	width: 100%;
	margin-top: 45px;
	font-family: Times New Roman;

}

.place {
	margin-left: 105px
}
</style>

</head>
<body>
	<form method="post" action="ReportTimeServlet"  name="ExportReport"
		id="idExportReport" class="ExportReportform">
		<h1>Entry Time Report</h1>
		<label for="form Date">Form Date:</label>
		<!--<input type="text" id="datepicker" name="fdate" value="2020-01-01 23:59:59"> --> 
		<input id="datetimepicker" type="text" name="fdate">
		<label for="To Date">ToDate:</label>
		<input id="datetimepicker1" type="text" name="todate" >
		<!--<input type="text"  id="datepicker1" name="todate" value="2022-01-01 23:59:59">-->   
		<input type="submit" id="idsubmit" name="btnSubmit" value="Submit" class="btnsubmit" ><br>
		<br>
			<div class="place">
			<input type="text" name="projectname" placeholder="oo_compology">
			<label>BatchID:</label>
			<input type="text" name="batchid" placeholder="BatchId" style="margin-left: 1px;">
		</div>

		<table style="border-collapse: collapse;" border="1"  id="entryTable" class="entryTable" width="100%">
		<thead>
			<tr>
				<th>Project Name</th>
				<th>Batch Name</th>
				<th>Entry Process</th>
				<th>Entry Type</th> 
				<th>Total Entries</th>
				<th>Total Time(Hrs)</th>
				<th>Average Time Per Entry(Hrs)</th>
				<!-- <th>IsDump</th> -->
			</tr>
			</thead>
			<tbody>
			<%
		
			ArrayList<ExportDetails> list=(ArrayList)request.getAttribute("list");
			if(list==null)
				list=new ArrayList<ExportDetails>();
			
			HashMap<String, ArrayList<DetailEntry>>addEntry=(HashMap<String, ArrayList<DetailEntry>>)request.getAttribute("addEntry");
			%>
			<%for(ExportDetails ex:list){ %>
			<tr>
			<%if(addEntry.containsKey(ex.getBatchid()+"@@"+ex.getUserid())){ 
				ArrayList<DetailEntry> dataList=addEntry.get(ex.getBatchid()+"@@"+ex.getUserid());
			%>
				<td rowspan="<%=dataList.size()+1%>"><%=ex.getProjectname() %></td>
				<td rowspan="<%=dataList.size()+1%>"><%=ex.getBatchname() %></td>
				<%-- <td rowspan="<%=dataList.size()+1%>"><%=ex.getEntrytype()%></td> --%>
					<%
				for(DetailEntry data:dataList)
				{
					
			%>	<tr>
			    <td><%=data.getProcess() %></td>
				<td><%=data.getType() %></td>
				<td><%=data.getTotalEntry()%></td>
				<td><%=data.getTotalTime()%></td>
				<td><%=data.getTotalAvg()%></td>
				<!-- <td></td> -->
				</tr>
				<%}%>
				<%}%>
			</tr>
			<%}%>
			</tbody>
		</table>
	</form>
</body>
</html>
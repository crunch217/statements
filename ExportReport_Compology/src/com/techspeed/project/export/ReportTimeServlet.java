package com.techspeed.project.export;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.techspeed.model.DetailEntry;
import com.techspeed.model.ExportDetails;

/**
 * Servlet implementation class ReportTimeServlet
 */
@WebServlet("/ReportTimeServlet")
public class ReportTimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public ReportTimeServlet() {
        super();
    
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		ExportReportDAO objExportReportDAO = new ExportReportDAO();
				try {
					String fdate=request.getParameter("fdate");
					String todate=request.getParameter("todate");
					String user=request.getParameter("userid");
					String batch=request.getParameter("batchid");
					ArrayList<ExportDetails> list=objExportReportDAO.getExportReport(fdate,todate, user, batch);
					request.setAttribute("list", list);
					HashMap<String, ArrayList<DetailEntry>>addEntry=objExportReportDAO.getAddEntry(batch, user, fdate, todate);
					//System.out.println(addEntry.toString());
					request.setAttribute("addEntry", addEntry);
					//System.out.println(list.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		RequestDispatcher dispatcher = request.getRequestDispatcher("pages/ExportReport.jsp");
		dispatcher.forward(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}


}

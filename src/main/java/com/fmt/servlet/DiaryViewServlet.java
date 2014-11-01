package com.fmt.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Hello world!
 *
 */
@SuppressWarnings("serial")
public class DiaryViewServlet extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	response.getWriter().println("Hello Diary!");
    }
}

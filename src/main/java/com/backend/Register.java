package com.backend;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/regform")
public class Register extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) {
		
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String city = req.getParameter("city");
		
		System.out.println(name + email+ password + city);
		
		Connection conn = createDbConnection();
		
		String query = "INSERT INTO register(name, password, email, city) values(?,?,?,?);";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setString(4, city);
			
			int result = ps.executeUpdate();
			
			if(result>0) {
				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				out.print("<h3>User has been registered </h3>");
				
				RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
				rd.include(req, res);
			}
			else {
				res.setContentType("text/html");
				
				PrintWriter out = res.getWriter();
				out.print("<h3>User wasn't registered </h3>");
				RequestDispatcher rd = req.getRequestDispatcher("/Register.jsp");
				rd.include(req, res);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection createDbConnection(){
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/taskmanagement","postgres","1234");
			System.out.println("DB Connected Reg");
		}catch(Exception e){
			System.out.println(e);
		}
		return conn;
	}
	
}
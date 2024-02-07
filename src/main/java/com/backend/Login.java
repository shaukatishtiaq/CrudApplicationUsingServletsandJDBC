package com.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/loginForm")
public class Login extends HttpServlet{
	public Connection dbConnection(){
		Connection conn = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/taskmanagement", "postgres","1234");
			
			System.out.println("Db connected login");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		System.out.println(email + " " +password);
		
		try {
			Connection conn = dbConnection();

			String query = "SELECT * FROM register WHERE email = ? AND password = ?;";
			PreparedStatement ps = conn.prepareStatement(query);
			
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			String dbEmail, dbPassword;
			
			if(rs.next()) {
				res.setContentType("text/html");
				String dbName = rs.getString("name");
				out.print("<h3>You are logged in. </h3>");
				req.setAttribute("name",dbName);
				req.setAttribute("email", rs.getString("email"));
				req.setAttribute("city", rs.getString("city"));
				RequestDispatcher rd = req.getRequestDispatcher("/profile.jsp");
				rd.include(req, res);
			}
			
			else {
				res.setContentType("text/html");
				
				out.print("<h3>Email or Password incorrect</h3>");
				RequestDispatcher rd = req.getRequestDispatcher("/login.jsp");
				rd.include(req, res);
			}
			conn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}

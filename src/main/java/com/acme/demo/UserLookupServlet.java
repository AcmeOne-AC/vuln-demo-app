package com.acme.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Demo servlet for the Acme security demo. Looks up a user by name.
 * Intentionally vulnerable: used to show detection and automated remediation.
 */
public class UserLookupServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:h2:mem:demo";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, email FROM users WHERE username = '" + username + "'");
            while (rs.next()) {
                out.println(rs.getInt("id") + " " + rs.getString("email"));
            }
        } catch (SQLException e) {
            response.setStatus(500);
            out.println("lookup failed");
        }
    }
}

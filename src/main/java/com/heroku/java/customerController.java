package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<<<<<<<<< Temporary merge branch 1
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

@Controller
public class customerController {
    private final DataSource dataSource;

    @Autowired
    public customerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
<<<<<<<<< Temporary merge branch 1
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    //insert cust into database
    @PostMapping("/signup")
    public String addAccount(HttpSession session, @ModelAttribute("signup")Customer customer) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO customer (fullname, address, phonenum,icnumber,licensecard,username,password) VALUES (?,?,?,?,?,?,?)";
      final var statement = connection.prepareStatement(sql);

      statement.setString(1, customer.getFullname());
      statement.setString(2, customer.getAddress());
      statement.setString(3, customer.getPhonenum());
      statement.setString(4, customer.getIcnumber());
      statement.setDate(5, customer.getLicensecard());
      statement.setString(6, customer.getUsername());
      statement.setString(7, customer.getPassword());

      statement.executeUpdate();

      connection.close();
<<<<<<<<< Temporary merge branch 1
      
=========

>>>>>>>>> Temporary merge branch 2
      return "redirect:/login";

    } catch (SQLException sqe) {
      System.out.println("Error Code = " + sqe.getErrorCode());
      System.out.println("SQL state = " + sqe.getSQLState());
      System.out.println("Message = " + sqe.getMessage());
      System.out.println("printTrace /n");
      sqe.printStackTrace();

      return "redirect:/";
    } catch (Exception e) {
      System.out.println("E message : " + e.getMessage());
      return "redirect:/";
    }

  }
  @PostMapping("/login") 
    public String HomePage(HttpSession session, @ModelAttribute("login") Customer customer) { 
        try (
            Connection connection = dataSource.getConnection()) { 
            final var statement = connection.createStatement(); 
            String sql ="SELECT username, password FROM customer"; 
            final var resultSet = statement.executeQuery(sql); 
 
            String returnPage = ""; 
 
            while (resultSet.next()) { 
                String username = resultSet.getString("username"); 
                String password = resultSet.getString("password");  
 
                if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) { 
                    session.setAttribute("username",customer.getUsername());
                    returnPage = "redirect:/homecustomer"; 
                    break; 
                } else { 
                    returnPage = "/login"; 
                } 
            } 
        
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            return "/login"; 
        } 
 
    }


}

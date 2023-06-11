package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Controller
public class customerController {
    private final DataSource dataSource;

    @Autowired
    public customerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    //insert cust into database
    @PostMapping("/addAccount")
    public String addAccount(HttpSession session, @ModelAttribute("account")Customer customer) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO staff(fullname, address, phonenum,icnumber,licensecard,username,password) VALUES (?,?,?,?,?,?,?)";
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

      return "redirect:/accounts";

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
}


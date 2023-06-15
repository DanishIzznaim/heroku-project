package com.heroku.java;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
// import java.text.SimpleDateFormat;

@Controller
public class customerController {
    private final DataSource dataSource;

    @Autowired
    public customerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    // @GetMapping("/login")
    // public String login() {
    //     return "login";
    // }

    // @GetMapping("/signup")
    // public String signup() {
    //     return "signup";
    // }

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
    public String HomePage(HttpSession session, @ModelAttribute("login") Customer customer, Model model) { 
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
            connection.close();
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            return "/login"; 
        } 
 
    }
        @GetMapping("/profilecust")
        public String viewProfile(HttpSession session, Customer customer, Model model) {
        String username = (String) session.getAttribute("username");

        if (username != null) { 
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("SELECT fullname, address, phonenum, icnumber, licensecard, username, password FROM customer WHERE username = ? ");
            statement.setString(1, username);
            final var resultSet = statement.executeQuery();

            while(resultSet.next()){
                String fullname = resultSet.getString("fullname");
                String address = resultSet.getString("address");
                String phonenum = resultSet.getString("phonenum");
                String icnumber = resultSet.getString("icnumber");
                Date licensecard = resultSet.getDate("licensecard");
                String password = resultSet.getString("password");

                System.out.println("fullname from db: " + fullname);
                Customer profilecust = new Customer(fullname, address, phonenum, icnumber, licensecard, username, password);
                model.addAttribute("profilecust", profilecust);
                System.out.println("Session profileCust : " + model.getAttribute("profilecust"));
                // Return the view name for displaying customer details
            }   
                return "profilecust";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        // Customer not found or username is null, handle accordingly (e.g., redirect to an error page)
        return "error";
        }

        //Update Profile Customer
        @PostMapping("/updateProf") 
        public String updateProfile(HttpSession session, @ModelAttribute("profilecust") Customer customer, Model model) { 
            String username = customer.getUsername();
            String password = customer.getPassword();
            String fullname = customer.getFullname();
            String phonenum = customer.getPhonenum();
            String icnumber = customer.getIcnumber();
            String address =  customer.getAddress();
            Date licensecard = customer.getLicensecard ();
            try (
            Connection connection = dataSource.getConnection()) { 
            String sql = "UPDATE customer SET fullname=? ,address=?, phonenum=?, icnumber=? , licensecard=?, username=?,password=? WHERE username=?";
            final var statement = connection.prepareStatement(sql);
            // String fullname = customer.getFullname();
            // String address = customer.getAddress();
            // String phonenum = customer.getPhonenum();
            // String icnumber = customer.getIcnumber();
            // Date licensecard = customer.getLicensecard();
            // String password = customer.getPassword();

            statement.setString(1, fullname);
            statement.setString(2, address);
            statement.setString(3, phonenum);
            statement.setString(4, icnumber);
            statement.setDate(5, licensecard);
            statement.setString(6, username);
            statement.setString(7, password);
            statement.setString(8, username);

            statement.executeUpdate();
                
            String returnPage = "profilecust"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "/login"; 
        } 
 
    }
    //delete controller
    @PostMapping("/deleteProf")
    public String deleteProfile(HttpSession session, Model model) {
    String username = (String) session.getAttribute("username");

    if (username != null) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM customer WHERE username=?");
            statement.setString(1, username);

            // Execute the delete statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Profile deleted successfully
                // You can redirect to a success page or perform any other desired actions
                return "login";
            } else {
                // Profile not found or deletion failed
                // You can redirect to an error page or perform any other desired actions
                // return "deleteError";
                System.out.println("delete fail");
            }
        } catch (SQLException e) {
            // Handle any potential exceptions (e.g., log the error, display an error page)
            e.printStackTrace();
            return "deleteError";
        }
    }

    // Username is null, handle accordingly (e.g., redirect to an error page)
    return "deleteError";
}



}

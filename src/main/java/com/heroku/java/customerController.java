package com.heroku.java;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

// import com.heroku.java.MODEL.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;

@Controller
public class customerController {   
    private final DataSource dataSource;

    public customerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/profilecust")
    public String profilecust() {
        return "profilecust";
    }
    @GetMapping("/feedback")
    public String feedback() {
        return "feedback";
    }
    //insert cust into database
    @PostMapping("/signup")
    public String addAccount(HttpSession session, @ModelAttribute("signup")Customer customer, User user) {
    try {
      Connection connection = dataSource.getConnection();
      String sql1= "INSERT INTO users (fullname, username, password) VALUES (?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);
      statement1.setString(1, user.getName());
      statement1.setString(2, user.getUsername());
      statement1.setString(3, user.getPassword());
      statement1.executeUpdate();
  
    //   Get id from database for sql 2 from sql 1
      String sql = "SELECT * FROM users where username=?";
      final var stmt = connection.prepareStatement(sql);
      stmt.setString(1, user.getUsername());
      final var resultSet = stmt.executeQuery();
      int id_db = 0;
      while(resultSet.next()){
        id_db = resultSet.getInt("userid");
      }

      System.out.println("id database : " + id_db);
      
      String sql2= "INSERT INTO customer (userid,licensedate,icnumber,phonenum) VALUES (?, ?,?,?)";
      final var statement2 = connection.prepareStatement(sql2);
      statement2.setInt(1, id_db);
      statement2.setDate(2, customer.getLicensedate());
      statement2.setString(3, customer.getIcnumber());
      statement2.setString(4, customer.getPhonenum());
      statement2.executeUpdate();

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
  
        //view Account
        @GetMapping("/profilecust")
        public String viewProfile(HttpSession session, Customer customer, Model model) {
        String username = (String) session.getAttribute("username");
        int userid = (int) session.getAttribute("userid");
        if (username != null) { 
        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.prepareStatement(
                "SELECT users.userid,users.fullname, users.username, users.password,users.usertype, customer.licensedate, customer.icnumber, customer.phonenum FROM users JOIN customer ON (users.userid = customer.userid) WHERE users.userid = ? ");
            statement.setInt(1, userid);
            final var resultSet = statement.executeQuery();

            // ArrayList <Customer> profilecust = new ArrayList<>();
            while(resultSet.next()){
                
                String fname = resultSet.getString("fullname");
                String usernamecust = resultSet.getString("username");
                String password = resultSet.getString("password");
                // int managerid = resultSet.getInt("managerid");
                String usertype =resultSet.getString("usertype");
                Date licensedate = resultSet.getDate("licensedate");
                String icnumber = resultSet.getString("icnumber");
                String phonenum = resultSet.getString("phonenum");
                // System.out.println("userid from db: "+userid); -- debug
                System.out.println("fullname from db: " +fname);

                Customer profilecust = new Customer(userid,fname, usernamecust, password,usertype, licensedate, icnumber, phonenum);

                model.addAttribute("profilecust", profilecust);
                System.out.println("fullname "+ profilecust.fname);
                // Return the view name for displaying customer details --debug
                System.out.println("Session profileCust : " + model.getAttribute("profilecust"));
                }   
                return "customer/profilecust";
            } catch (SQLException e) {
            e.printStackTrace();
            }
            }else{
                return "login";
            }
            return "login";
        }

        //Update Profile Customer
        @PostMapping("/updateProf") 
        public String updateProfile(HttpSession session, @ModelAttribute("profilecust") Customer customer, Model model, User user) { 

            String fullname = customer.getName();
            String phonenum = customer.getPhonenum();
            String icnumber = customer.getIcnumber();
            Date licensedate = customer.getLicensedate();
            String username = customer.getUsername();
            String password = customer.getPassword();
        
            try { 
            Connection connection = dataSource.getConnection();
            String sql1 = "UPDATE users SET fullname=? ,username=?, password=? WHERE username=?";
            final var statement = connection.prepareStatement(sql1);

            statement.setString(1, fullname);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, username);
            statement.executeUpdate();
            System.out.println("debug= "+fullname+" "+username+" "+password);

            String sql = "SELECT * FROM users where username=?";
            final var stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            final var resultSet = stmt.executeQuery();
            int id_db = 0;
            while(resultSet.next()){
            id_db = resultSet.getInt("userid");
            }
            System.out.println("id database : " + id_db);
      
            String sql2= "UPDATE customer SET licensedate=?, icnumber=?, phonenum=? WHERE userid=?";
            final var statement2 = connection.prepareStatement(sql2);
            // statement2.setInt(1, id_db);
            statement2.setDate(1, licensedate);
            statement2.setString(2, icnumber);
            statement2.setString(3, phonenum);
            statement2.setInt(4,id_db);
            statement2.executeUpdate();
            System.out.println("debug= "+licensedate+" "+icnumber+" "+phonenum);
         
                
            String returnPage = "customer/profilecust"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } 
 
    }
    //delete controller
        @GetMapping("/deleteCust")
        public String deleteProfileCust(HttpSession session, Customer customer,Model model) {
            String username = (String) session.getAttribute("username");
            int userid = (int) session.getAttribute("userid");

            if (username != null) {
                try (Connection connection = dataSource.getConnection()) {
                    // Delete customer record
                    final var deleteCustomerStatement = connection.prepareStatement("DELETE FROM customer WHERE userid=?");
                    deleteCustomerStatement.setInt(1, userid);
                    int customerRowsAffected = deleteCustomerStatement.executeUpdate();

                    // Delete user record
                    final var deleteUserStatement = connection.prepareStatement("DELETE FROM users WHERE userid=?");
                    deleteUserStatement.setInt(1, userid);
                    int userRowsAffected = deleteUserStatement.executeUpdate();

                    if (customerRowsAffected > 0 && userRowsAffected > 0) {
                        // Deletion successful
                        // You can redirect to a success page or perform any other desired actions
                        session.invalidate();
                        return "redirect:/login";
                    } else {
                        // Deletion failed
                        // You can redirect to an error page or perform any other desired actions
                        System.out.println("Delete Failed");
                    }
                } catch (SQLException e) {
                    // Handle any potential exceptions (e.g., log the error, display an error page)
                    e.printStackTrace();

                    // Deletion failed
                    // You can redirect to an error page or perform any other desired actions
                    System.out.println("Error");
                }
            }
            // Username is null or deletion failed, handle accordingly (e.g., redirect to an error page)
            return "deleteError";
        }

    
}

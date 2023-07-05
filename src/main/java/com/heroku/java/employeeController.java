package com.heroku.java;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.heroku.java.MODEL.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
import java.util.ArrayList;

@Controller
public class employeeController {   
    private final DataSource dataSource;

    public employeeController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @GetMapping("/addStaff")
    public String addstaffPage(HttpSession session) {
        return "admin/addStaff";
        // if(session.getAttribute("username") != null){ 
            
        // }else{ 
        //     System.out.println("Session expired or invalid");
        //     return "login"; 
        // } 
    }
    @PostMapping("/addStaff")
    public String addAccount(HttpSession session, @ModelAttribute("signup")Employee emp, User user) {
    try {
      Connection connection = dataSource.getConnection();
      String sql1= "INSERT INTO users (fullname, username, password, managerid, usertype) VALUES (?,?,?,?,?)";
      final var statement1 = connection.prepareStatement(sql1);
      statement1.setString(1, user.getName());
      statement1.setString(2, user.getUsername());
      statement1.setString(3, user.getPassword());
      statement1.setInt(4, emp.getManagerid());
      statement1.setString(5, user.getUsertype());
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
      
      String sql2= "INSERT INTO employee (userid, emprole) VALUES (?, ?)";
      final var statement2 = connection.prepareStatement(sql2);
      statement2.setInt(1, id_db);
      statement2.setString(2,emp.getRoles());
      statement2.executeUpdate();

      connection.close();
      return "redirect:/listStaff";

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

    //list staff
    @GetMapping("/listStaff")
    public String listStaff(HttpSession session, Employee emp, Model model, User user) {
    String usernamemp = (String) session.getAttribute("username");
    // int userid = (int) session.getAttribute("userid");
    
    if (usernamemp != null) { 
    ArrayList <Employee> employees = new ArrayList<>();
    try(Connection connection = dataSource.getConnection()){
        final var statement = connection.prepareStatement(
        "SELECT * FROM users JOIN employee ON (users.userid = employee.userid) WHERE emprole = 'Normal Staff' or emprole='Supervisor' ");
        final var resultSet = statement.executeQuery();

        
        while(resultSet.next()){  
            int userid = resultSet.getInt("userid");
            String fname = resultSet.getString("fullname");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            int managerid = resultSet.getInt("managerid");
            String usertype =resultSet.getString("usertype");
            String emprole = resultSet.getString("emprole");
            
            System.out.println("fullname from db: " +fname);
            Employee employee = new Employee(userid,fname, username, password,usertype,managerid,emprole);
            employees.add(employee);
            
        }
            model.addAttribute("employees", employees);
            
            return "admin/listStaff";
        }catch (SQLException sqe) {
        System.out.println("Error Code = " + sqe.getErrorCode());
        System.out.println("SQL state = " + sqe.getSQLState());
        System.out.println("Message = " + sqe.getMessage());
        System.out.println("printTrace /n");
        sqe.printStackTrace();

        return "redirect:/";
        }catch (Exception e) {
        System.out.println("E message : " + e.getMessage());
        return "redirect:/";
        }
    }else{
        return "redirect:/";
    } 
  }

  @GetMapping("/saveChanges")
  public String saveChanges(@RequestParam("userid") int userid, Model model, Employee employee, User user) {
    String emprole = employee.getRoles();
    System.out.println(emprole);
    try { 
            
            Connection connection = dataSource.getConnection();
      
            String sql2= "UPDATE employee SET emprole=? WHERE userid=?";
            final var statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, emprole);
            statement2.setInt(2,userid);
            statement2.executeUpdate();
            System.out.println("emprole="+emprole);
            // System.out.println("debug= "+id_db+" "+emprole)
            statement2.close();
            connection.close();
        
            String returnPage = "redirect:/listStaff"; 
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } 
    
  }

  //delete staff
  @GetMapping("/deleteStaff")
  public String deleteStaff(@RequestParam("userid") int userid, Model model, Employee employee, User user) {
    try (Connection connection = dataSource.getConnection()) {
         // Delete employee record
        final var deleteCustomerStatement = connection.prepareStatement("DELETE FROM employee WHERE userid=?");
        deleteCustomerStatement.setInt(1, userid);
        int employeeRowsAffected = deleteCustomerStatement.executeUpdate();

        // Delete user record
        final var deleteUserStatement = connection.prepareStatement("DELETE FROM users WHERE userid=?");
        deleteUserStatement.setInt(1, userid);
        int userRowsAffected = deleteUserStatement.executeUpdate();

        if (employeeRowsAffected > 0 && userRowsAffected > 0) {
         // Deletion successful
                       
        return "redirect:/listStaff";
        } else {
          // Deletion failed
                        
          System.out.println("Delete Failed");
        }
        } catch (SQLException e) {
       // Handle any potential exceptions (e.g., log the error, display an error page)
        e.printStackTrace();
        System.out.println("Error");
        }
        // Username is null or deletion failed, handle accordingly (e.g., redirect to an error page)
        return "deleteError";
        }
}
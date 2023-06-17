package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "homepage";
    }
    @GetMapping("/sedan")
    public String sedan() {
        return "sedan";
    }
    @GetMapping("/book")
    public String book() {
        return "book";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }


    @GetMapping("/login") 
    public String login(HttpSession session, Customer customer) { 
        
        if(session.getAttribute("username") != null ){ 
            System.out.println(session.getAttribute("username"));
            return "customer/homecustomer"; 
        }else{ 
            return "login"; 
        } 
    }
    @PostMapping("/login") 
    public String HomePage(HttpSession session, @ModelAttribute("login") Customer customer, User user, Model model, Employee emp) { 

        try {
            Connection connection = dataSource.getConnection();
            final var statement = connection.createStatement(); 
            String sql ="SELECT userid, username, password FROM users"; 
            final var resultSet = statement.executeQuery(sql); 
            

            String returnPage = ""; 
 
            while (resultSet.next()) { 
                int userid = resultSet.getInt("userid");
                String username = resultSet.getString("username"); 
                String password = resultSet.getString("password");
                String usertype = user.getUsertype();  
                
                //if they choose custoke
                if (usertype.equals("customer")){
                    if (username.equals(customer.getUsername()) && password.equals(customer.getPassword())) { 
                    session.setAttribute("username",customer.getUsername());
                    session.setAttribute("userid",userid);
                    System.out.println("userid: "+userid);
                    returnPage = "redirect:/homecustomer"; 
                    break; 
                } else { 
                    returnPage = "/login"; 
                } 

                //if they choose employee
                }
                else if (usertype.equals("employee")){
                    if (username.equals(emp.getUsername()) && password.equals(emp.getPassword())) { 
                    session.setAttribute("username",customer.getUsername());
                    returnPage = "redirect:/homeadmin"; 
                    break; 
                } else { 
                    returnPage = "/login"; 
                } 
                }
                else{
                    System.out.println("Username does not match password");
                }
            }
            return returnPage; 
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            return "/login"; 
        } 
 
    }

    @GetMapping("/homecustomer")
    public String homecustomer(HttpSession session) {
        if(session.getAttribute("username") != null){ 
            return "customer/homecustomer"; 
        }else{ 
            System.out.println("Session expired or invalid");
            return "login"; 
        } 
    }

    
    @GetMapping("/homeadmin")
    public String homeadmin() {
        return "admin/homeadmin";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
       session.invalidate();
      return("redirect:/login");
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (
            Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}

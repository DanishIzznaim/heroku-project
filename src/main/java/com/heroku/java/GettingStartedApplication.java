package com.heroku.java;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.DAO.LoginDAO;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

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
    @GetMapping("/sedanBook")
    public String book() {
        return "sedanBook";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }


    @GetMapping("/login") 
    public String login(HttpSession session) { 
            return "login"; 
    }
    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam("username") String username,
                    @RequestParam("password") String password, Model model) {
    try {
        LoginDAO loginDAO = new LoginDAO(dataSource);
        
        boolean isCustomer = loginDAO.checkCustomer(username, password);
        boolean isEmployee = loginDAO.checkEmployee(username, password);
        
        if (isCustomer) {
            session.setAttribute("username", username);
            return "redirect:/homecustomer"; // Replace with the appropriate customer home page URL
        } else if (isEmployee) {
            
            session.setAttribute("username", username);
            return "redirect:/homeadmin";
        } else {
            System.out.println("Invalid username or password");
            model.addAttribute("error", true); 
            return "login"; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
        model.addAttribute("error", true); 
        return "login";
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
        @GetMapping("/test")
    public String test() {
        return "test";
    }


    @GetMapping("/custdetail")
    public String custdetail() {
        return "admin/custdetail";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
       session.invalidate();
      return("redirect:/login");
    }

     @GetMapping("/feedback")
    public String feedback() {
        return "feedback"; 
    }

    @GetMapping("/about")
    public String about() {
        return "about";
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

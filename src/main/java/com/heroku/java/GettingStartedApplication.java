package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    // @GetMapping("/login")
    // public String login() {
    //     return "login";
    // }

    @GetMapping("/login") 
    public String login(HttpSession session) { 
        if(session.getAttribute("username") != null){ 
            return "customer/homecustomer"; 
        }else{ 
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

    @GetMapping("/profileadmin")
    public String profileadmin() {
        return "admin/profileadmin";
    }

    // @GetMapping("/profilecust")
    // public String profilecust() {
    //     return "profilecust";
    // }
    
    // @GetMapping("/profilecust")
    // public String profilecust(HttpSession session) {
    //     if(session.getAttribute("username") != null){ 
    //         System.out.println("Session username : " + session.getAttribute("username"));
    //         return "profilecust";
    //     }else{ 
    //         System.out.println("Session expired or invalid");
    //         return "login"; 
    //     } 
    // }

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

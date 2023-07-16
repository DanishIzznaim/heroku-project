package com.heroku.java.controller;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.DAO.CustomerDAO;
import com.heroku.java.bean.Customer;

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
    
    //insert cust into database
    @PostMapping("/signup")
    public String addAccount(HttpSession session, @ModelAttribute("signup")Customer customer) {
        try {
        CustomerDAO customerDAO = new CustomerDAO(dataSource);
        customerDAO.addCustomer(customer);

        return "redirect:/login";
    } catch (SQLException e) {
        e.printStackTrace();
        return "error";
    }
    }
  
        //view Account
        @GetMapping("/profilecust")
        public String viewProfile(HttpSession session, Model model) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                CustomerDAO customerDAO = new CustomerDAO(dataSource); 
                try {
                    Customer customer = customerDAO.getCustomerByUsername(username);
                    model.addAttribute("profilecust", customer);
                } catch (SQLException e) {
                    System.out.println("Error");
                    e.printStackTrace();
                    
                }

            } else {
                return "login";
            }
            return "/customer/profilecust";
        }

        //Update Profile Customer
        @PostMapping("/updateProf") 
        public String updateProfile(HttpSession session, @ModelAttribute("profilecust") Customer customer, Model model) { 
            try {
                CustomerDAO customerDAO = new CustomerDAO(dataSource);
                customerDAO.updateCustomer(customer);
                session.setAttribute("customer", customer);
                return "redirect:/profilecust";
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed update");
                return "error";
            }
            
 
        }

        //delete controller
        @GetMapping("/deleteCust")
        public String deleteProfileCust(HttpSession session, @ModelAttribute("profilecust") Customer customer,Model model) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                try {
                    CustomerDAO customerDAO = new CustomerDAO(dataSource);
                    customerDAO.deleteCustomer(username);
                    session.invalidate();
                    return "redirect:/login";
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "error";
                }
            } else {
                return "error";
            }
        }

    
}

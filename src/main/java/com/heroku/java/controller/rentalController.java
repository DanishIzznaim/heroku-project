package com.heroku.java.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.DAO.CarDAO;
import com.heroku.java.DAO.CustomerDAO;
// import com.heroku.java.DAO.PaymentDAO;
// import com.heroku.java.DAO.PaymentDAO;
import com.heroku.java.DAO.RentalDAO;
import com.heroku.java.bean.Cars;
import com.heroku.java.bean.Customer;
// import com.heroku.java.bean.Payment;
// import com.heroku.java.bean.Payment;
import com.heroku.java.bean.Rental;

import jakarta.servlet.http.HttpSession;

// import jakarta.servlet.http.HttpSession;
@Controller
public class rentalController {
    private final DataSource dataSource;

    public rentalController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/searchCars")
    public String searchCars(@RequestParam("day") int day,
            @RequestParam("datestart") Date datestart,
            @RequestParam("dateend") Date dateend,
            @RequestParam("cartype") String cartype, Model model) {
        try {
            System.out.println("masuk controller");
            CarDAO carDAO = new CarDAO(dataSource);
            List<Cars> availableCars = carDAO.searchAvailableCars(day, datestart, dateend, cartype);

            model.addAttribute("cartype", cartype);
            model.addAttribute("datestart", datestart);
            model.addAttribute("dateend", dateend);
            model.addAttribute("day", day);
            model.addAttribute("availableCars", availableCars);

            return "customer/listrentalcar";
        } catch (Exception e) {
            System.out.println("Error searching cars: " + e.getMessage());
            return "redirect:/";
        }
    }

    // @GetMapping("/rentalform")
    // public String rentalForm(){
    // return "customer/rentalform";
    // }

    @GetMapping("/rentalform")
    public String showRentalForm(@RequestParam("carid") int carid,
            @RequestParam("datestart") String datestart,
            @RequestParam("dateend") String dateend,
            @RequestParam("day") int day,
            Model model) {
        try {
            CarDAO carDAO = new CarDAO(dataSource);
            Cars car = carDAO.getCarById(carid);
            // Add the necessary data to the model for display on the rental form page
            System.out.println("carprice controller" + car.getCarprice());
            model.addAttribute("car", car);
            model.addAttribute("datestart", datestart);
            model.addAttribute("dateend", dateend);
            model.addAttribute("day", day);

            // double totalPrice = day * car.getCarprice();
            return "customer/rentalform";
        } catch (Exception e) {
            System.out.println("Error searching cars: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/rentalform")
    public String rentNow(Model model, Rental rent, HttpSession session) {
        String username = (String) session.getAttribute("username");
        try {
            CustomerDAO customerDAO = new CustomerDAO(dataSource);
            Customer customer = customerDAO.getCustomerByUsername(username);
            int customerid = customer.getUserid();
            System.out.println("customerid " + customerid);
            int carid = rent.getCarid();
            rent.setCarid(carid);
            rent.setCustomerid(customerid);
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            int rentid = rentalDAO.addRental(rent);
            return "redirect:/payment?rentid=" + rentid + "&carid=" + carid;
        } catch (Exception e) {
            System.out.println("Error searching cars: " + e.getMessage());
            return "redirect:/";
        }
    }

    // @GetMapping("/rentaldetail")
    // public String rentDetail(HttpSession session, Rental rental, Model model,
    // @RequestParam("rentid") int rentid,
    // @RequestParam("paymentid") int paymentid) {
    // // String username = (String) session.getAttribute("username");
    // try {
    // RentalDAO rentalDAO = new RentalDAO(dataSource);
    // rental = rentalDAO.getRentbyId(rentid);

    // int customerid = rental.getCustomerid();
    // System.out.println("customerid " + customerid);
    // int carid = rental.getCarid();
    // rental.setCarid(carid);
    // rental.setCustomerid(customerid);

    // CarDAO carDAO = new CarDAO(dataSource);
    // Cars car = carDAO.getCarById(carid);

    // model.addAttribute("car", car);
    // model.addAttribute("rent", rental);
    // session.setAttribute("rent", rental);
    // session.setAttribute("car", car);

    // return "customer/rentaldetail";
    // } catch (Exception e) {
    // System.out.println("Error searching cars: " + e.getMessage());
    // return "redirect:/";
    // }
    // }

    //

    @GetMapping("/rentaldetailC")
    public String showRentalDetail(HttpSession session, Model model) {
        Rental rental = (Rental) session.getAttribute("rent");
        int paymentId = (int) session.getAttribute("paymentId");
        try {
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            Rental rentalDetails = rentalDAO.getRentbyId(rental.getRentid());

            CarDAO carDAO = new CarDAO(dataSource);
            Cars carDetails = carDAO.getCarById(rentalDetails.getCarid());

            model.addAttribute("rent", rentalDetails);
            model.addAttribute("car", carDetails);

            model.addAttribute("paymentId", paymentId);
            return "customer/rentaldetailC";

        } catch (Exception e) {
            System.out.println("Error retrieving rental details: " + e.getMessage());
            return "redirect:/";
        }
    }

}

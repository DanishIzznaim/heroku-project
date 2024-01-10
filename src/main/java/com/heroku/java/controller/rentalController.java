package com.heroku.java.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.heroku.java.DAO.CarDAO;
import com.heroku.java.DAO.CustomerDAO;
import com.heroku.java.DAO.EmployeeDAO;
// import com.heroku.java.DAO.PaymentDAO;
// import com.heroku.java.DAO.PaymentDAO;
import com.heroku.java.DAO.RentalDAO;
import com.heroku.java.bean.Cars;
import com.heroku.java.bean.Customer;
import com.heroku.java.bean.Employee;
// import com.heroku.java.bean.Payment;
// import com.heroku.java.bean.Payment;
import com.heroku.java.bean.Rental;

import jakarta.servlet.http.HttpSession;
// import org.springframework.web.bind.annotation.RequestBody;

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

            if (availableCars.isEmpty()) {
                model.addAttribute("noAvailableCarsMessage",
                        "Sorry, There is no available cars for the specified date and type.");
            } else {
                model.addAttribute("availableCars", availableCars);
            }

            return "customer/listrentalcar";
        } catch (Exception e) {
            System.out.println("Error searching cars: " + e.getMessage());
            return "redirect:/";
        }
    }

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

    @GetMapping("/rentaldetailC")
    public String showRentalDetail(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        try {
            CustomerDAO customerDAO = new CustomerDAO(dataSource);
            Customer customerDetails = customerDAO.getCustomerByUsername(username);

            int customerId = customerDetails.getUserid();
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            Rental rentalDetails = rentalDAO.getLatestRentalByCustId(customerId);// by customerid,will retrieve the
                                                                         // day,datestart,dateend,statusrent,totalrentprice
            String statusrent = rentalDetails.getStatusrent();

            if("Finished".equals(statusrent) || rentalDetails == null){
                model.addAttribute("noBooking", true);
                return "customer/rentaldetailC";
            }

            // if (rentalDetails == null) {
            //     model.addAttribute("noBooking", true);
            //     return "customer/rentaldetailC";
            // }

            int carId = rentalDetails.getCarid();
            CarDAO carDAO = new CarDAO(dataSource);
            Cars carDetails = carDAO.getCarById(carId);// by id it will retrieve the car image,carname,cartype

            model.addAttribute("car", carDetails);
            model.addAttribute("customer", customerDetails);
            model.addAttribute("rental", rentalDetails);

            return "customer/rentaldetailC";
        } catch (Exception e) {
            System.out.println("Error retrieving rental details: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/viewBooking")
    public String viewBooking(Model model) {
        try {
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            CustomerDAO customerDAO = new CustomerDAO(dataSource);
            List<Rental> rentals = rentalDAO.getAllRentals();
            List<Customer> customers = customerDAO.getAllCustomers();
            model.addAttribute("rentals", rentals);
            model.addAttribute("customers", customers);

            return "admin/viewBooking";
        } catch (SQLException e) {
            // Handle exceptions or errors
            model.addAttribute("error", "Error retrieving rentals: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/updateBooking")
    public String updateBooking(@RequestParam("rentid") int rentid,
            @RequestParam("returndate") Date returndate, @RequestParam("statusrent") String statusrent, Model model) {
        try {
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            Rental existingRental = rentalDAO.getRentbyId(rentid);
            if (existingRental == null) {
                throw new IllegalArgumentException("Rental record not found.");
            }
            existingRental.setReturndate(returndate);
            existingRental.setStatusrent(statusrent);
            rentalDAO.updateRental(existingRental);
            model.addAttribute("rental", existingRental);
            return "redirect:/viewBooking";
        } catch (Exception e) {
            // Handle exceptions or errors
            model.addAttribute("error", "Error updating booking: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PostMapping("/saveRental")
    public String saveRental(@RequestParam("returndate") Date returnDate,
            @RequestParam("rentid") int rentid,
            @RequestParam("statusrent") String statusRent, Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");

        try {
            EmployeeDAO employeeDAO = new EmployeeDAO(dataSource);
            Employee employeeDetails = employeeDAO.getEmployeeByUsername(username);
            int employeeid = employeeDetails.getUserid();

            RentalDAO rentalDAO = new RentalDAO(dataSource);
            Rental rental = new Rental();
            rental.setRentid(rentid);
            rental.setEmployeeId(employeeid);
            rental.setReturndate(returnDate);
            rental.setStatusrent(statusRent);
            rentalDAO.saveReturnDateStatus(rental);

            return "redirect:/viewBooking";
        } catch (Exception e) {
            // Handle exceptions or errors
            model.addAttribute("error", "Error updating booking: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @GetMapping("/getBookedDates")
    public String getBookedDates() {
        RentalDAO rentalDAO = new RentalDAO(dataSource);
        try {

            System.out.println("test date" + rentalDAO.getBookedDates());
            java.util.Iterator<Rental> iterator = rentalDAO.getBookedDates().iterator();
            while (iterator.hasNext()) {
                Rental element = iterator.next();
                System.out.println("Element: " + element);
            }
            return "redirect:/login";

        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/login";
        }
    }

}

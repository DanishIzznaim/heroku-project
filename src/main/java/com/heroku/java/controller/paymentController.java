package com.heroku.java.controller;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;

import com.heroku.java.DAO.CarDAO;
import com.heroku.java.DAO.PaymentDAO;
import com.heroku.java.DAO.RentalDAO;
import com.heroku.java.bean.Cars;
import com.heroku.java.bean.Payment;
import com.heroku.java.bean.Rental;

import jakarta.servlet.http.HttpSession;

@Controller
public class paymentController {
    private final DataSource dataSource;

    public paymentController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/payment")
    public String payment(Model model, @RequestParam("rentid") int rentid, @RequestParam("carid") int carid) {
        // Use the rentid as needed in your payment logic
        try {
            CarDAO carDAO = new CarDAO(dataSource);
            Cars car = carDAO.getCarById(carid);
            model.addAttribute("car", car);

            RentalDAO rentDAO = new RentalDAO(dataSource);
            Rental rental = rentDAO.getRentbyId(rentid);
            model.addAttribute("rental", rental);
            System.out.println("renttotalprice: " + rental.getTotalrentprice());
            return "customer/payment";
        } catch (Exception e) {
            System.out.println("Error searching cars: " + e.getMessage());
            return "redirect:/";
        }

    }

    // @PostMapping("/payment")
    // public String makePayment(Payment payment,@RequestParam("rentid") int
    // rentid,Model model){
    // try{
    // MultipartFile paymentreceipt = payment.getPaymentreceipt();
    // byte[] paymentBytes = paymentreceipt.getBytes();
    // payment.setPaymentbyte(paymentBytes);
    // String paymentmethod = payment.getPaymentmethod();
    // System.out.println("rentid: "+rentid);
    // payment.setRentid(rentid);
    // PaymentDAO paymentDAO = new PaymentDAO(dataSource);
    // int paymentId;

    // if(paymentmethod.equals("Cash")){
    // paymentId = paymentDAO.addCashPayment(payment);
    // }else if(paymentmethod.equals("Online Transaction")){
    // paymentId = paymentDAO.addPayment(payment);
    // }else {
    // System.out.println("failed to insert payment");
    // throw new IllegalArgumentException("Invalid payment method");
    // }

    // return "redirect:/rentaldetail?paymentid="+paymentId+"&rentid="+rentid;
    // }catch (SQLException e) {
    // // Handle exceptions or errors
    // model.addAttribute("error", "Error processing payment: " + e.getMessage());
    // return "redirect:/";
    // } catch (IOException e) {
    // e.printStackTrace();
    // return "redirect:/";
    // }
    // }

    @PostMapping("/payment")
    public String makePayment(Payment payment, @RequestParam("rentid") int rentid, Model model, HttpSession session) {
        try {
            RentalDAO rentalDAO = new RentalDAO(dataSource);
            Rental rent = rentalDAO.getRentbyId(rentid);

            MultipartFile paymentreceipt = payment.getPaymentreceipt();
            byte[] paymentBytes = paymentreceipt.getBytes();
            payment.setPaymentbyte(paymentBytes);
            String paymentmethod = payment.getPaymentmethod();
            System.out.println("rentid: " + rentid);
            payment.setRentid(rentid);
            PaymentDAO paymentDAO = new PaymentDAO(dataSource);
            int paymentId;

            if (paymentmethod.equals("Cash")) {
                paymentId = paymentDAO.addCashPayment(payment);
            } else if (paymentmethod.equals("Online Transaction")) {
                paymentId = paymentDAO.addPayment(payment);
            } else {
                System.out.println("failed to insert payment");
                throw new IllegalArgumentException("Invalid payment method");
            }

            session.setAttribute("rent", rent);
            session.setAttribute("paymentId", paymentId);

            return "redirect:/rentaldetailC";
        } catch (SQLException e) {
            // Handle exceptions or errors
            model.addAttribute("error", "Error processing payment: " + e.getMessage());
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

}

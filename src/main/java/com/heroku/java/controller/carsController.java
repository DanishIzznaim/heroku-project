package com.heroku.java.controller;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.heroku.java.DAO.CarDAO;
import com.heroku.java.bean.Cars;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

// import java.sql.*;
// import org.springframework.web.multipart.MultipartFile;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;

@Controller
public class carsController {
    private final DataSource dataSource;

    public carsController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


        @GetMapping("/addCar")
        public String addSedan(HttpSession session) {
            return "admin/addCar";  
        }

        @PostMapping("/addCar")
        public String addCar(@ModelAttribute("car") Cars car) {
        try {    
            CarDAO carDAO = new CarDAO(dataSource);
        
            MultipartFile carimage = car.getCarimage();
            car.setCarimagebyte(carimage.getBytes());
            carDAO.addCar(car);

            if(car.getCartype().equals("Sedan")){
                return "redirect:/viewSedan";
            }else if(car.getCartype().equals("MPV")){
                return "redirect:/viewMpv";
            }else{
                return "redirect:/viewCompact";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/";
        }
        }

        @GetMapping("/viewSedan")
        public String viewSedan(HttpSession session, Model model) {
            try {
                CarDAO carDAO = new CarDAO(dataSource);
                ArrayList<Cars> cars = carDAO.getSedanCars();
                model.addAttribute("cars", cars);
                return "admin/viewSedan";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("E message : " + e.getMessage());
                return "redirect:/";
            }
        }

        @GetMapping("/viewMpv")
        public String viewMPV(HttpSession session, Model model) {
            try {
                CarDAO carDAO = new CarDAO(dataSource);
                ArrayList<Cars> cars = carDAO.getMPVCars();
                model.addAttribute("cars", cars);
                return "admin/viewMPV";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("E message : " + e.getMessage());
                return "redirect:/";
            }
        }

        @GetMapping("/viewCompact")
        public String viewCompact(HttpSession session, Model model) {
            try {
                CarDAO carDAO = new CarDAO(dataSource);
                ArrayList<Cars> cars = carDAO.getCompactCars();
                model.addAttribute("cars", cars);
                return "admin/viewCompact";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("E message : " + e.getMessage());
                return "redirect:/";
            }
        }

        @GetMapping("/updateCar")
        public String updateCar(@RequestParam("carid") int carid, Model model) {
            try {
                CarDAO carDAO = new CarDAO(dataSource);
                Cars car = carDAO.getCarById(carid);

                if (car != null) {
                    model.addAttribute("car", car);
                }

                return "admin/updateCar";
            } catch (SQLException sqe) {
                System.out.println("Error Code = " + sqe.getErrorCode());
                System.out.println("SQL state = " + sqe.getSQLState());
                System.out.println("Message = " + sqe.getMessage());
                System.out.println("printTrace /n");
                sqe.printStackTrace();
                return "redirect:/";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("E message : " + e.getMessage());
                return "redirect:/";
            }
        }


        @PostMapping("/updateCar")
        public String updateCar(@ModelAttribute("car") Cars car, @RequestParam("carimage") MultipartFile carimage) {
            CarDAO carDAO = new CarDAO(dataSource);
            boolean success = carDAO.updateCar(car, carimage);
            if (success) {
                if(car.getCartype().equals("Sedan")){
                    return "redirect:/viewSedan";
                }else if(car.getCartype().equals("MPV")){
                    return "redirect:/viewMpv";
                }else{
                    return "redirect:/viewCompact";
                }
            } else {
                return "car-not-found";
            }
        }


    // @GetMapping("/deleteCar")
    // public String deleteCar(@RequestParam("carid") int carid, Model model) {
    //     CarDAO carDAO = new CarDAO(dataSource);
    //     boolean success = carDAO.deleteCar(carid);
    //     System.out.println("sucess value: "+ success);
    //     //get details car by id
    //     //if cartype = sedan, go to page viewSedan,
    //     //if cartype = compact, go to page viewCompact,
    //     //if cartype = mpv, go to page viewMpv
    //      return "redirect:/viewSedan?success=" + String.valueOf(success);
    // }
      
    @GetMapping("/deleteCar")
    public String deleteCar(@RequestParam("carid") int carid, Model model) throws SQLException {
    CarDAO carDAO = new CarDAO(dataSource);
    boolean success = carDAO.deleteCar(carid);
    System.out.println("success value: " + success);

    
        // If deletion is successful, get details of the car by id
        Cars car = carDAO.getCarById(carid);

        if (car != null) {
            String carType = car.getCartype();

            // Redirect based on car type
            if ("sedan".equalsIgnoreCase(carType)) {
               return "redirect:/viewSedan?success=" + String.valueOf(success);
            } else if ("compact".equalsIgnoreCase(carType)) {
                return "redirect:/viewCompact?success=" + String.valueOf(success);
            } else if ("mpv".equalsIgnoreCase(carType)) {
                return "redirect:/viewMpv?success=" + String.valueOf(success);
            }
        }
    

    // If there was an issue or car type is unknown, redirect to a default page
    return "redirect:/defaultPage?success=" + String.valueOf(success);
    }
}

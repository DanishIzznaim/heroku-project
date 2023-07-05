package com.heroku.java;

import javax.sql.DataSource;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.util.StreamUtils;
import jakarta.servlet.http.HttpSession;

// import java.io.ByteArrayInputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

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
        // if(session.getAttribute("username") != null){ 
            return "admin/addCar";
        // }else{ 
        //     System.out.println("Session expired or invalid");
        //     return "login"; 
        // } 
    }

    @GetMapping("/updateCar")
    public String updateCar(@RequestParam("carid") int carid, Model model) {
    try {
        Connection connection = dataSource.getConnection();
        String sql = "SELECT cartype, carname, condition, carprice, carimage FROM cars WHERE carid = ?";
        final var statement = connection.prepareStatement(sql);
        statement.setInt(1, carid);
        final var resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            String cartype = resultSet.getString("cartype");
            String carname = resultSet.getString("carname");
            String condition = resultSet.getString("condition");
            double carprice = resultSet.getDouble("carprice");

            byte[] carimageBytes = resultSet.getBytes("carimage");
            String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
            String imageSrc = "data:image/jpeg;base64," + base64Image;

            Cars car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc,null);
            model.addAttribute("car", car);
        }
        
        return "admin/updateCar";
    } catch (Throwable t) {
        System.out.println("Error: " + t.getMessage());
        return "index";
    }
}

    @PostMapping("/updateCar")
    public String updateAccountString(HttpSession session, @ModelAttribute("cars") Cars car, Model model, @RequestParam("carid") int carid) { 
        
        String carname = car.getCarname();
        String cartype = car.getCartype();
        String condition = car.getCondition();
        double carprice = car.getCarprice();
            try (
            Connection connection = dataSource.getConnection()) { 
            String sql = "UPDATE cars SET carname=?, cartype=? , condition=?, carprice=? WHERE carid=?";
            final var statement = connection.prepareStatement(sql);
            

            statement.setString(1, carname);
            statement.setString(2, cartype);
            statement.setString(3, condition);
            statement.setDouble(4, carprice);
            statement.setInt(5,carid);

            statement.executeUpdate();
                 
            return "redirect:/viewSedan";  
 
        } catch (Throwable t) { 
            System.out.println("message : " + t.getMessage()); 
            System.out.println("error");
            return "redirect:/login"; 
        } 
    }

    @GetMapping("/deleteCar")
    public String deleteCar(@RequestParam("carid") int carid) {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "DELETE FROM cars WHERE carid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, carid);
        statement.executeUpdate();
        return "redirect:/viewSedan";
    } catch (Throwable t) {
        System.out.println("message: " + t.getMessage());
        return "redirect:/login";
    }
}


    @GetMapping("/viewSedan")
    public String viewSedan(HttpSession session, Model model) {
    String usernamemp = (String) session.getAttribute("username");
    // int userid = (int) session.getAttribute("userid");
    
    if (usernamemp != null) { 
    ArrayList <Cars> cars = new ArrayList<>();
    try(Connection connection = dataSource.getConnection()){
        final var statement = connection.prepareStatement(
        "SELECT carid,cartype,carname,condition,carprice,carimage FROM cars WHERE cartype = 'Sedan' ");
        final var resultSet = statement.executeQuery();
        while(resultSet.next()){  
            int carid = resultSet.getInt("carid");  
            String cartype = resultSet.getString("cartype");
            String carname = resultSet.getString("carname");
            String condition = resultSet.getString("condition");

            double carprice = resultSet.getDouble("carprice");
            String carprice2dp = String.format("%.2f", carprice);

            byte[] carimageBytes = resultSet.getBytes("carimage");
            String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
            String imageSrc = "data:image/jpeg;base64," + base64Image;
            
            // System.out.println("carprice from db: " +carprice);
            Cars car = new Cars(carid,cartype,carname,condition,carprice,null,imageSrc,carprice2dp);
            cars.add(car);
            
        }
            model.addAttribute("cars",cars);
            
            return "admin/viewSedan";
        }catch (SQLException sqe) {
        System.out.println("Error Code = " + sqe.getErrorCode());
        System.out.println("SQL state = " + sqe.getSQLState());
        System.out.println("Message = " + sqe.getMessage());
        System.out.println("printTrace /n");
        sqe.printStackTrace();

        return "redirect:/";
        }catch (Exception e) {
        e.printStackTrace();
        System.out.println("E message : " + e.getMessage());
        
        return "redirect:/";
        }
        }else{
        return "redirect:/";
        } 
    }

    @PostMapping("/addCar")
    public String addCar(Cars car, @RequestParam("carimage") MultipartFile carimage) {
    try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO cars (cartype, carname, condition, carprice, carimage) VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, car.getCartype());
            statement.setString(2, car.getCarname());
            statement.setString(3, car.getCondition());
            statement.setDouble(4, car.getCarprice());
            statement.setBytes(5, carimage.getBytes());
            statement.executeUpdate();
            
            
            connection.close();
        
        
        return "redirect:/viewSedan";
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
    }

}

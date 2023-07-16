package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;


import com.heroku.java.bean.Customer;

public class CustomerDAO {
    private final DataSource dataSource;

    public CustomerDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    

    public Customer addCustomer(Customer customer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO customer (custname, username, password, licensedate, icnumber, phonenumc) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getFullname());
            statement.setString(2, customer.getUsername());
            statement.setString(3, customer.getPassword());
            statement.setDate(4, customer.getLicensedate());
            statement.setString(5, customer.getIcnumber());
            statement.setInt(6, customer.getPhonenumC());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            customer.setUserid(generatedId);
        }
            connection.close();
            return customer;
        } catch (SQLException e) {
            // Handle any exceptions or errors that occurred during the database operation
            e.printStackTrace();
            throw e;
        }
    }

    public Customer getCustomerByUsername(String username) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM customer WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
    
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setUserid(resultSet.getInt("customerid"));
                customer.setFullname(resultSet.getString("custname"));
                customer.setUsername(resultSet.getString("username"));
                customer.setPassword(resultSet.getString("password"));
                customer.setLicensedate(resultSet.getDate("licensedate"));
                customer.setIcnumber(resultSet.getString("icnumber"));
                customer.setPhonenumC(resultSet.getInt("phonenumc"));
                // Set any other properties of the Customer object based on the ResultSet
    
                return customer;
            }
            connection.close();
            return null; // Return null if the customer is not found
        } catch (SQLException e) {
            // Handle any exceptions or errors that occurred during the database operation
            e.printStackTrace();
            throw e;
        }
    }

    public Customer updateCustomer(Customer customer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE customer SET custname=?, username=?, password=?, licensedate=?, icnumber=?, phonenumc=? WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getFullname());
            statement.setString(2, customer.getUsername());
            statement.setString(3, customer.getPassword());
            statement.setDate(4, customer.getLicensedate());
            statement.setString(5, customer.getIcnumber());
            statement.setInt(6, customer.getPhonenumC());
            statement.setString(7, customer.getUsername());
    
            statement.executeUpdate();
            connection.close();
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
      
        public void deleteCustomer(String username) throws SQLException {
            System.out.println("Lalu deleteDAO");
            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM customer WHERE username=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
    
                statement.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    
}

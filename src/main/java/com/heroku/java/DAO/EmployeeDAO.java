package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.heroku.java.bean.Customer;
import com.heroku.java.bean.Employee;

public class EmployeeDAO {
    private final DataSource dataSource;

    public EmployeeDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addEmployee(Employee employee) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO employee (fullname, username, password, phonenumE, managerid, emprole) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employee.getFullname());
            statement.setString(2, employee.getUsername());
            statement.setString(3, employee.getPassword());
            statement.setInt(4, employee.getPhonenumE());
            statement.setInt(5, employee.getManagerid());
            statement.setString(6, employee.getEmprole());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Employee> listEmployee() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee ORDER BY employeeid";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setUserid(resultSet.getInt("employeeid"));
                employee.setFullname(resultSet.getString("fullname"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPassword(resultSet.getString("password"));
                employee.setPhonenumE(resultSet.getInt("phonenumE"));
                employee.setManagerid(resultSet.getInt("managerid"));
                employee.setEmprole(resultSet.getString("emprole"));

                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return employees;
    }

    //getEmployee by ID
    public Employee getEmployeeById(int employeeId) throws SQLException {
        Employee employee = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee WHERE employeeid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setUserid(resultSet.getInt("employeeid"));
                employee.setFullname(resultSet.getString("fullname"));
                employee.setUsername(resultSet.getString("username"));
                employee.setPassword(resultSet.getString("password"));
                employee.setEmprole(resultSet.getString("emprole"));
                employee.setPhonenumE(resultSet.getInt("phonenumE"));
                employee.setManagerid(resultSet.getInt("managerid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return employee;
    }

    //update employee
    public void updateEmployee(Employee employee) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE employee SET fullname=?, username=?, emprole=?, phonenumE=?, managerid=? "
                    + "WHERE employeeid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employee.getFullname());
            statement.setString(2, employee.getUsername());
            statement.setString(3, employee.getEmprole());
            statement.setInt(4, employee.getPhonenumE());
            statement.setInt(5, employee.getManagerid());
            statement.setInt(6, employee.getUserid());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
        //deleteEmployee
    public void deleteEmployee(int employeeId) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM employee WHERE employeeid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employeeId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Employee getEmployeeByUsername(String username) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM employee WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int employeeid = resultSet.getInt("employeeid");
                String fullname = resultSet.getString("fullname");
                String usernameEmp = resultSet.getString("username");
                String password = resultSet.getString("password");
                String emprole = resultSet.getString("emprole");
                int phonenumE = resultSet.getInt("phonenumE");
                int managerid = resultSet.getInt("managerid");
                Employee employee = new Employee(employeeid, fullname, usernameEmp,password,emprole,phonenumE,managerid);
                return employee;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
    List<Customer> customers = new ArrayList<>();
    
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT * FROM customer";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        
        while (resultSet.next()) {
            int customerid = resultSet.getInt("customerid");
            String custname = resultSet.getString("custname");
            String username = resultSet.getString("username");
            Date licensedate = resultSet.getDate("licensedate");
            String icnumber = resultSet.getString("icnumber");
            int phonenumC = resultSet.getInt("phonenumC");

            
            Customer customer = new Customer(customerid, custname, username,null,licensedate,icnumber,phonenumC);
            customers.add(customer);
        }
    }
    
    return customers;
}
}


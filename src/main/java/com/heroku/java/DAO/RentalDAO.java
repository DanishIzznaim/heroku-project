package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.heroku.java.bean.Rental;

public class RentalDAO {
    private DataSource dataSource;

    public RentalDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int addRental(Rental rental) throws SQLException {
        
        int rentid = 0;
        System.out.println("rentid: "+rentid);
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO rental (day, datestart, dateend,totalrentprice, customerid,carid) VALUES (?, ?, ?, ?, ?, ?) RETURNING rentid;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, rental.getDay());
            statement.setDate(2, rental.getDatestart());
            statement.setDate(3, rental.getDateend());
            statement.setDouble(4, rental.getTotalrentprice());
            statement.setInt(5, rental.getCustomerid());
            statement.setInt(6, rental.getCarid());
            ResultSet parentResultSet = statement.executeQuery();

            if (parentResultSet.next()) {
                int parentId = parentResultSet.getInt("rentid");
                rentid = parentId;
                System.out.println("rentid : " + rentid);
            }
            connection.close();
        }catch (SQLException e) {
            System.out.println("Error add Rental: " + e.getMessage());
        }
        return rentid;
    }

    public Rental getRentbyId(int rentid){
        Rental rental = null;
        try{
            Connection conn = dataSource.getConnection();
            String sql = "SELECT * FROM rental WHERE rentid = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, rentid);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                rental = new Rental();
                rental.setRentid(rs.getInt("rentid"));
                rental.setDay(rs.getInt("day"));
                rental.setDatestart(rs.getDate("datestart"));
                rental.setDateend(rs.getDate("dateend"));
                rental.setReturndate(rs.getDate("returndate"));
                rental.setStatusrent(rs.getString("statusrent"));
                rental.setTotalrentprice(rs.getDouble("totalrentprice"));
                rental.setCarid(rs.getInt("carid"));
                rental.setCustomerid(rs.getInt("customerid"));
            }
            conn.close();
        }catch (SQLException e) {
            System.out.println("Error add Rental: " + e.getMessage());
        }
        return rental;
    }


    public Rental getRentbyCustId(int customerId) throws SQLException {
        Rental rental = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM rental WHERE customerid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                int day = resultSet.getInt("day");
                int rentId = resultSet.getInt("rentid");
                int carId = resultSet.getInt("carid");
                Date dateStart = resultSet.getDate("datestart");
                Date dateEnd = resultSet.getDate("dateend");
                String statusRent = resultSet.getString("statusrent");
                double totalRentPrice = resultSet.getDouble("totalrentprice");
                rental = new Rental(day, rentId, carId, customerId,dateStart, dateEnd, statusRent, totalRentPrice);
            }
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Error retrieving rental details: " + e.getMessage());
        }
        return rental;
    }

    public List<Rental> getAllRentals() throws SQLException {
    List<Rental> rentals = new ArrayList<>();

    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT * FROM rental order by rentid";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Rental rental = new Rental();
            rental.setRentid(resultSet.getInt("rentid"));
            rental.setDay(resultSet.getInt("day"));
            rental.setDatestart(resultSet.getDate("datestart"));
            rental.setDateend(resultSet.getDate("dateend"));
            rental.setReturndate(resultSet.getDate("returndate"));
            rental.setStatusrent(resultSet.getString("statusrent"));
            rental.setTotalrentprice(resultSet.getDouble("totalrentprice"));
            rental.setCarid(resultSet.getInt("carid"));
            rental.setCustomerid(resultSet.getInt("customerid"));

            rentals.add(rental);
        }
        connection.close();
    } catch (SQLException e) {
        throw new SQLException("Error retrieving rentals: " + e.getMessage());
    }

    return rentals;
}

public void updateRental(Rental rental) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "UPDATE rental SET returndate = ?, statusrent = ? WHERE rentid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, rental.getReturndate());
        statement.setString(2, rental.getStatusrent());
        statement.setInt(3, rental.getRentid());
        statement.executeUpdate();
    } catch (SQLException e) {
        throw new SQLException("Error updating rental: " + e.getMessage());
    }
}
   public void saveReturnDateStatus(Rental rental)throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "UPDATE rental SET returndate = ?, statusrent = ?, employeeid = ? WHERE rentid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, rental.getReturndate());
        statement.setString(2, rental.getStatusrent());
        statement.setInt(3, rental.getEmployeeId());
        statement.setInt(4, rental.getRentid());
        statement.executeUpdate();
    } catch (SQLException e) {
        throw new SQLException("Error updating rental: " + e.getMessage());
    }
  } 

    public List<Rental> getBookedDates() throws SQLException{
    List<Rental> rentals = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
        String sql = "SELECT datestart, dateend FROM RENTAL";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Rental rental = new Rental();
            rental.setDatestart(resultSet.getDate("datestart"));
            rental.setDateend(resultSet.getDate("dateend"));

            rentals.add(rental);
        }
        connection.close();
        
        } catch (SQLException e) {
            throw new SQLException("Error updating rental: " + e.getMessage());
        }
        return rentals;
    } 

    public Rental getLatestRentalByCustId(int customerId) throws SQLException {
       Rental rental = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT * FROM rental WHERE customerid = ? ORDER BY rentid DESC LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                int day = resultSet.getInt("day");
                int rentId = resultSet.getInt("rentid");
                int carId = resultSet.getInt("carid");
                Date dateStart = resultSet.getDate("datestart");
                Date dateEnd = resultSet.getDate("dateend");
                String statusRent = resultSet.getString("statusrent");
                double totalRentPrice = resultSet.getDouble("totalrentprice");
                rental = new Rental(day, rentId, carId, customerId,dateStart, dateEnd, statusRent, totalRentPrice);
            }
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Error retrieving rental details: " + e.getMessage());
        }
        return rental;
    }
}


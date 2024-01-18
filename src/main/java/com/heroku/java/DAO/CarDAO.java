package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.multipart.MultipartFile;

import com.heroku.java.bean.Cars;

public class CarDAO {
    private DataSource dataSource; // Assuming you have a DataSource instance

    public CarDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addCar(Cars car) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO cars (cartype, carname, condition, carprice, carimage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, car.getCartype());
            statement.setString(2, car.getCarname());
            statement.setString(3, car.getCondition());
            statement.setDouble(4, car.getCarprice());
            statement.setBytes(5, car.getCarimagebyte());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<Cars> getSedanCars() throws SQLException {
        ArrayList<Cars> cars = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT carid, cartype, carname, condition, carprice, carimage FROM cars WHERE cartype = 'Sedan'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int carid = resultSet.getInt("carid");
                String cartype = resultSet.getString("cartype");
                String carname = resultSet.getString("carname");
                String condition = resultSet.getString("condition");
                double carprice = resultSet.getDouble("carprice");

                byte[] carimageBytes = resultSet.getBytes("carimage");
                String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
                String carprice2dp = String.format("%.2f", carprice);

                Cars car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc, carprice2dp);
                cars.add(car);

            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return cars;
    }

    public ArrayList<Cars> getMPVCars() throws SQLException {
        ArrayList<Cars> cars = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT carid, cartype, carname, condition, carprice, carimage FROM cars WHERE cartype = 'MPV'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int carid = resultSet.getInt("carid");
                String cartype = resultSet.getString("cartype");
                String carname = resultSet.getString("carname");
                String condition = resultSet.getString("condition");
                double carprice = resultSet.getDouble("carprice");

                byte[] carimageBytes = resultSet.getBytes("carimage");
                String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
                String carprice2dp = String.format("%.2f", carprice);

                Cars car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc, carprice2dp);
                cars.add(car);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return cars;
    }

    public ArrayList<Cars> getCompactCars() throws SQLException {
        ArrayList<Cars> cars = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT carid, cartype, carname, condition, carprice, carimage FROM cars WHERE cartype = 'Compact'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int carid = resultSet.getInt("carid");
                String cartype = resultSet.getString("cartype");
                String carname = resultSet.getString("carname");
                String condition = resultSet.getString("condition");
                double carprice = resultSet.getDouble("carprice");

                byte[] carimageBytes = resultSet.getBytes("carimage");
                String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
                String carprice2dp = String.format("%.2f", carprice);

                Cars car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc, carprice2dp);
                cars.add(car);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return cars;
    }

    public Cars getCarById(int carid) throws SQLException {
        Cars car = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT cartype, carname, condition, carprice, carimage FROM cars WHERE carid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, carid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String cartype = resultSet.getString("cartype");
                String carname = resultSet.getString("carname");
                String condition = resultSet.getString("condition");
                double carprice = resultSet.getDouble("carprice");
                System.out.println("carprice: " + carprice);
                byte[] carimageBytes = resultSet.getBytes("carimage");
                String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;

                car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc, null);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return car;
    }

    public boolean updateCar(Cars car, MultipartFile carImage) {
        try (Connection connection = dataSource.getConnection()) {
            String sql;
            PreparedStatement statement;

            // Check if a new image is provided
            if (!carImage.isEmpty()) {
                sql = "UPDATE cars SET cartype=?, carname=?, condition=?, carprice=?, carimage=? WHERE carid=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, car.getCartype());
                statement.setString(2, car.getCarname());
                statement.setString(3, car.getCondition());
                statement.setDouble(4, car.getCarprice());
                statement.setBytes(5, carImage.getBytes());
                statement.setInt(6, car.getCarid());
            } else {
                sql = "UPDATE cars SET cartype=?, carname=?, condition=?, carprice=? WHERE carid=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, car.getCartype());
                statement.setString(2, car.getCarname());
                statement.setString(3, car.getCondition());
                statement.setDouble(4, car.getCarprice());
                statement.setInt(5, car.getCarid());
            }

            // execute update
            int rowsAffected = statement.executeUpdate();
            connection.close();
            return rowsAffected > 0;

        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCar(int carid) {
        try (Connection connection = dataSource.getConnection()) {
            // Check if the car is currently rented
            if (isCarRented(connection, carid)) {
                System.out.println("Cannot delete the car because it is currently rented.");
                return false;
            }
            String sql = "DELETE FROM cars WHERE carid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, carid);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
        }
        return false;
    }

    private boolean isCarRented(Connection connection, int carid) throws SQLException {
        String rentalCheckSql = "SELECT COUNT(*) FROM rental WHERE carid = ? AND statusrent = 'Booked'";
        try (PreparedStatement rentalCheckStatement = connection.prepareStatement(rentalCheckSql)) {
            rentalCheckStatement.setInt(1, carid);
            try (ResultSet resultSet = rentalCheckStatement.executeQuery()) {
                if (resultSet.next()) {
                    int rentalCount = resultSet.getInt(1);
                    return rentalCount > 0;
                }
            }
        }
        return false;
    }

    public List<Cars> searchAvailableCars(int day, Date datestart, Date dateend, String carType) throws SQLException {
        List<Cars> availableCars = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT c.carid, c.cartype, c.carname, c.condition, c.carprice, c.carimage " +
                    "FROM cars c " +
                    "WHERE c.cartype = ? AND c.carid NOT IN (" +
                    "    SELECT r.carid FROM rental r WHERE r.datestart <= ? AND r.dateend >= ?" +
                    ")";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, carType);
            statement.setDate(2, datestart);
            statement.setDate(3, dateend);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carid = resultSet.getInt("carid");
                String cartype = resultSet.getString("cartype");
                String carname = resultSet.getString("carname");
                String condition = resultSet.getString("condition");
                double carprice = resultSet.getDouble("carprice");
                byte[] carimageBytes = resultSet.getBytes("carimage");
                String base64Image = Base64.getEncoder().encodeToString(carimageBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
                String carprice2dp = String.format("%.2f", carprice);
                System.out.println("cartype: " + cartype);
                Cars car = new Cars(carid, cartype, carname, condition, carprice, null, imageSrc, carprice2dp);
                availableCars.add(car);
            }
            connection.close();
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
        }
        return availableCars;
    }

    public boolean isCarAvailableForDates(int carid, Date startDate, Date endDate) {
        try (
            Connection connection = dataSource.getConnection()){
            String sql = "SELECT COUNT(*) FROM rental " +
                    "WHERE carid = ? " +
                    "AND statusrent = 'Booked' " +
                    "AND ("+
                    "(? <= datestart AND ? >= dateend) OR" +
                    "(? >= datestart AND ? <= dateend)"+
                    ")";
                    // "AND ("+
                    // "(? <= datestart AND ? >= dateend) OR " +
                    // "(? >= datestart AND ? >= dateend) OR " +
                    // "(? <= datestart AND ? <= dateend) OR "+
                    // "(? <= datestart AND ? >= dateend))";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, carid);
                        // statement.setString(2, statusrent);
                        statement.setDate(2, startDate);
                        statement.setDate(3, endDate);
                        statement.setDate(4, startDate);
                        statement.setDate(5, endDate);
            
                        System.out.println("Executing SQL query: " + sql);
                        System.out.println("Parameters: carid=" + carid +", datestart=" + startDate + ", dateend=" + endDate);
            
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                int count = resultSet.getInt(1);
                                System.out.println("count: " + count);
                                return count == 0;
                            }
                        }
                    }
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
        }
        return false;
    }

}

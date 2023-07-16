package com.heroku.java.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.heroku.java.bean.Payment;

public class PaymentDAO {
    private DataSource dataSource;

    public PaymentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int addPayment(Payment payment) throws SQLException {
        int paymentId = 0;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO payment (payamount, paymentmethod,paymentreceipt,rentid) VALUES (?, ?, ?, ?) RETURNING paymentid;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, payment.getPayamount());
            statement.setString(2, payment.getPaymentmethod());
            statement.setBytes(3, payment.getPaymentbyte());
            statement.setInt(4,payment.getRentid());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                paymentId = resultSet.getInt("paymentid");
            }
        } catch (SQLException e) {
            throw new SQLException("Error adding payment: " + e.getMessage());
        }
        return paymentId;
    }

    public int addCashPayment(Payment payment) throws SQLException {
        System.out.println("masuk DAO");
        int paymentId = 0;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO payment (payamount, paymentmethod,rentid) VALUES ( ?, ?, ?) RETURNING paymentid;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, payment.getPayamount());
            statement.setString(2, payment.getPaymentmethod());
            statement.setInt(3,payment.getRentid());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                paymentId = resultSet.getInt("paymentid");
            }

            String sql2 = "INSERT INTO cashpayment (paymentid) VALUES (?)";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1,paymentId);
            statement2.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error adding payment: " + e.getMessage());
        }
        return paymentId;
    }

    
}

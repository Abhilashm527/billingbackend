package com.abhilsh.billingbackend.Repository;

import com.abhilsh.billingbackend.Model.Farmer;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class FarmerImpl {
    private final JdbcTemplate jdbcTemplate;
    private static final String insertSQl = "INSERT INTO farmers(\n" +
            "\tid, name, phone_number, address, createddate)\n" +
            "\tVALUES (?, ?, ?, ?, ?);";


    private static final String insertHistorySQl = "INSERT INTO schedule_history(\n" +
            "\tschedulehistoryid, scheduleid, target, active, targettype, lastrun, lastrunstate)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String selectHistoryByIdSQl = "select * from schedule_history where scheduleid= ?;";

    private static final String updateSQl = "UPDATE farmers\n" +
            "\tSET name=?, phone_number=?, address=?\n" +
            "\tWHERE id= ?;";
    private static final String getAllFarmerSQl = "select * from farmers";
    private static final String getFarmerSQl = "select * from farmers where id = ?";
    private static final String deleteFarmerSQl = "delete from farmers where id = ?";


    public FarmerImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Connection jdbcTemplateConnection;

    @PostConstruct
    public void prepareQueries() throws SQLException {
        jdbcTemplateConnection = jdbcTemplate.getDataSource().getConnection();
    }
    public Farmer create(Farmer farmer) throws SQLException {
        String code="";
        PreparedStatement insertStmt = jdbcTemplateConnection.prepareStatement(insertSQl);
        farmer.setCreationDate(LocalDateTime.now());
        if(farmer.getAddress()!=null && !farmer.getAddress().isBlank()) {
            code = farmer.getAddress().substring(0,3);
            insertStmt.setString(4,farmer.getAddress().substring(3));
            farmer.setAddress(farmer.getAddress().substring(3));
        }
        farmer.setId(generateIdNumber(code));
        insertStmt.setString(1, farmer.getId());
        insertStmt.setString(2, farmer.getName());
        insertStmt.setString(3, farmer.getPhoneNumber());
        insertStmt.setTimestamp(5, Timestamp.valueOf(farmer.getCreationDate()));
        insertStmt.execute();
        return farmer;
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        long min = (long) Math.pow(10, 6 - 1);
        long max = (long) Math.pow(10, 6) - 1;

        long randomNumber = random.nextLong();
        randomNumber = Math.abs(randomNumber);
        randomNumber = randomNumber % (max - min + 1) + min;

        return (int) randomNumber;
    }
    public static String generateIdNumber(String code) {
        Random random = new Random();
        long min = (long) Math.pow(10, 3 - 1);
        long max = (long) Math.pow(10, 3) - 1;

        long randomNumber = random.nextLong();
        randomNumber = Math.abs(randomNumber);
        randomNumber = randomNumber % (max - min + 1) + min;
        String id = code+randomNumber;
        return id;
    }


    public Farmer getFarmer(String id) throws SQLException {
        PreparedStatement getFarmerbyId = jdbcTemplateConnection.prepareStatement(getFarmerSQl);
        getFarmerbyId.setString(1, id);
        ResultSet resultSet = getFarmerbyId.executeQuery();
        Farmer farmer = null;
        while (resultSet.next()) {
            farmer = new Farmer();
            farmer.setId(resultSet.getString("id"));
            farmer.setName(resultSet.getString("name"));
            farmer.setAddress(resultSet.getString("address"));
            farmer.setPhoneNumber(resultSet.getString("phone_number"));
            farmer.setCreationDate(resultSet.getTimestamp("createddate").toLocalDateTime());

        }
        if (farmer == null)
            return null;
        return farmer;
    }

    public boolean delete(String id) {
        try {
            PreparedStatement deleteScheduleById = jdbcTemplateConnection.prepareStatement(deleteFarmerSQl);
            deleteScheduleById.setString(1, id);
            int rows = deleteScheduleById.executeUpdate();
            if (rows <= 0)
                return false;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Farmer updateFarmer(String id, Farmer farmer) throws SQLException {
        PreparedStatement updateStmt = jdbcTemplateConnection.prepareStatement(updateSQl);
        // Set other parameters for the update query

        PreparedStatement getScheduleByid = jdbcTemplateConnection.prepareStatement(getFarmerSQl);
        getScheduleByid.setString(1,id);
        ResultSet resultSet = getScheduleByid.executeQuery();
        if (resultSet.next()) {
            updateStmt.setString(1, farmer.getName());
            updateStmt.setString(2, farmer.getPhoneNumber());
            updateStmt.setString(3, farmer.getAddress());
            updateStmt.setString(4, id);
            updateStmt.executeUpdate();
            farmer.setId(id);
            farmer.setCreationDate(resultSet.getTimestamp(5).toLocalDateTime());
        }
        return farmer;
    }
    public List<Farmer> getAllFarmers() {
        try {
            PreparedStatement getAllSchedules = jdbcTemplateConnection.prepareStatement(getAllFarmerSQl);
            ResultSet resultSet = getAllSchedules.executeQuery();
            List<Farmer> scheduleList = new ArrayList<>();
            while (resultSet.next()) {
                Farmer farmer = new Farmer();
                farmer.setId(resultSet.getString("id"));
                farmer.setName(resultSet.getString("name"));
                farmer.setAddress(resultSet.getString("address"));
                farmer.setPhoneNumber(resultSet.getString("phone_number"));
                farmer.setCreationDate(resultSet.getTimestamp("createddate").toLocalDateTime());
                scheduleList.add(farmer);
            }
            return scheduleList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

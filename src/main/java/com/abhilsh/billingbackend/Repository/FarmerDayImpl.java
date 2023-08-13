package com.abhilsh.billingbackend.Repository;

import com.abhilsh.billingbackend.Model.BuyMethod;
import com.abhilsh.billingbackend.Model.Farmer;
import com.abhilsh.billingbackend.Model.FarmerDayData;
import com.abhilsh.billingbackend.Model.ItemDetails;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class FarmerDayImpl {
    private final JdbcTemplate jdbcTemplate;
    private static final String insertSQl = "INSERT INTO expenses(\n" +
            "\tid, date, farmer_id, expense_item, expense_quality, expense_amount, expense_is_cash)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";


    private static final String insertHistorySQl = "INSERT INTO schedule_history(\n" +
            "\tschedulehistoryid, scheduleid, target, active, targettype, lastrun, lastrunstate)\n" +
            "\tVALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String selectFarmerDetailsByIdSQl = "select * from expenses where farmer_id=?;";

    private static final String selectAllFarmerDetailsSQl = "select * from expenses ;";

    private static final String updateSQl = "UPDATE farmers\n" +
            "\tSET name=?, phone_number=?, address=?\n" +
            "\tWHERE id= ?;";
    private static final String getAllFarmerSQl = "select * from farmers";
    private static final String getFarmerSQl = "select * from farmers where id = ?";
    private static final String deleteFarmerSQl = "delete from farmers where id = ?";


    public FarmerDayImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Connection jdbcTemplateConnection;

    @PostConstruct
    public void prepareQueries() throws SQLException {
        jdbcTemplateConnection = jdbcTemplate.getDataSource().getConnection();
    }

    public FarmerDayData createFramerDaydetails(FarmerDayData farmerDayData) throws SQLException {
        PreparedStatement insertStmt = jdbcTemplateConnection.prepareStatement(insertSQl);
        for (int i = 0; i < farmerDayData.getItemDetailsList().size(); i++) {
            insertStmt.setInt(1, generateRandomNumber());
            insertStmt.setString(3, farmerDayData.getId());
            insertStmt.setTimestamp(2, Timestamp.valueOf(farmerDayData.getDateTime()));
            insertStmt.setString(5, farmerDayData.getItemDetailsList().get(i).getQuality());
            insertStmt.setString(4, farmerDayData.getItemDetailsList().get(i).getItem());
            insertStmt.setDouble(6, farmerDayData.getItemDetailsList().get(i).getQuantity());
            insertStmt.setBoolean(7,farmerDayData.getItemDetailsList().get(i).isMode());
            insertStmt.execute();
        }
        return farmerDayData;
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

    public List<LinkedHashMap<String,Object>> getFarmerDetailsById(String id) throws SQLException {
        PreparedStatement getScheduleHistoryByid = jdbcTemplateConnection.prepareStatement(selectFarmerDetailsByIdSQl);
        getScheduleHistoryByid.setString(1, id);
        ResultSet resultSet = getScheduleHistoryByid.executeQuery();
        List<LinkedHashMap<String,Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
            data.put("date", resultSet.getTimestamp(2).toLocalDateTime());
            data.put("item", resultSet.getString(4));
            data.put("quality", resultSet.getString(5));
            data.put("amount", resultSet.getString(6));
            if(resultSet.getBoolean(7)){
                data.put("isCash", true);
            } else {
                data.put("isCash", false);
            }
          list.add(data);
        }

        if (!list.isEmpty()) {
            return list;
        }
        return null;

    }

    public List<LinkedHashMap<String, Object>> getFarmerAllFarmersDetails() throws SQLException {
        PreparedStatement getScheduleHistoryByid = jdbcTemplateConnection.prepareStatement(selectAllFarmerDetailsSQl);
        ResultSet resultSet = getScheduleHistoryByid.executeQuery();
        List<LinkedHashMap<String,Object>> list = new ArrayList<>();
        while (resultSet.next()) {
            LinkedHashMap<String,Object> data = new LinkedHashMap<>();
            data.put("date", resultSet.getTimestamp(2).toLocalDateTime());
            data.put("farmerId", resultSet.getString(3));
            data.put("item", resultSet.getString(4));
            data.put("quality", resultSet.getString(5));
            data.put("amount", resultSet.getString(6));
            if(resultSet.getBoolean(7)){
                data.put("isCash", true);
            } else {
                data.put("isCash", false);
            }
            list.add(data);
        }

        if (!list.isEmpty()) {
            return list;
        }
        return new ArrayList<LinkedHashMap<String, Object>>() ;

    }
}

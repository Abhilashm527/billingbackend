package com.abhilsh.billingbackend.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class FarmerDayData {
    @JsonProperty("farmerId")
    private String id;
    @JsonProperty("date")
    private LocalDateTime dateTime;
    @JsonProperty("data")
    private List<ItemDetails> itemDetailsList;

}

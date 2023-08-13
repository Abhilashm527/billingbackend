package com.abhilsh.billingbackend.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDetails {
    @JsonProperty("item")
    private String item;
    @JsonProperty("quality")
    private String quality;
    @JsonProperty("isCash")
    private boolean mode;
    @JsonProperty("amount")
    private Double quantity = null;
}

package com.example.vitality.retrofit;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchResponse {
    //No need to map all keys, only those the elements you need
    @SerializedName("items")
    public List<Items> items;
    public List<Items> getItems() {
        return items;
    }
    public void setItems(List<Items> items) {
        this.items = items;
    }
}

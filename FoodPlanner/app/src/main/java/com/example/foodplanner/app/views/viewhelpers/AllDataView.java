package com.example.foodplanner.app.views.viewhelpers;

import com.example.foodplanner.data.pojos.Data;

import java.util.List;

public interface AllDataView {
    public void showData(List<Data> data);
    public void showError(String error);
}

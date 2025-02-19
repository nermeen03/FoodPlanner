package com.example.foodplanner.data.pojos;

public interface Data {
    public default String getIngredient(){return "";}
    public default void setInfo(String info){}
    public default String getStrMeal() {
        return "";
    }
}

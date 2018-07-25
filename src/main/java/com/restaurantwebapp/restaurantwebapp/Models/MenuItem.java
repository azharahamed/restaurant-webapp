package com.restaurantwebapp.restaurantwebapp.Models;

public class MenuItem {
    private float price;
    private String name;
    private String description;
    private String category;

    @Override
    public String toString(){
        String returnString = new String();
        returnString = "-->" + this.name +
                "-" + this.description +
                "-" + this.category +
                "-" + this.price;

        return returnString;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public MenuItem(float price, String name, String description, String category) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.category = category;
    }

}

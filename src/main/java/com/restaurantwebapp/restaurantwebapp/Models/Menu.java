package com.restaurantwebapp.restaurantwebapp.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private String restuarant;
    private LocalDateTime lastUpdated;
    private int totalItem;

    public Boolean addAnItem(MenuItem menuItem){
        this.menuItems.add(menuItem);
        this.lastUpdated = LocalDateTime.now();

        return true;
    }

    public Menu(){
    }

    public Menu(String name){
        this.restuarant = name;
        this.lastUpdated = LocalDateTime.now();
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public String getRestuarant() {
        return restuarant;
    }

    public String getLastUpdated() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.lastUpdated.format(dateFormatter);
    }

    @Override
    public String toString() {
        String returnString = new String();
        returnString = "*** Menu : "+this.restuarant+" ***\n";
        for(MenuItem menuItem: menuItems){
            returnString += menuItem + "\n";
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        returnString += "**** Last Updated On :" + this.lastUpdated.format(dateFormatter) +" ****\n";
        return returnString;
    }
}

package com.example.foodyrestaurant;

import java.util.ArrayList;

public class Reservation {

    enum  prepStatus {
        PENDING,
        DOING,
        DONE,
    }

    private String reservationID;
    private ArrayList<Dish> dishesOrdered;
    private boolean accepted;
    private prepStatus preparationStatus;

    //User useful data
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userAddress;

    private String resNote;
    private String orderTime;

    public Reservation(String identifier, ArrayList<Dish> dishes, prepStatus preparationStatus, String orderTime){
        this.reservationID = identifier;
        this.dishesOrdered = dishes;
        this.preparationStatus = preparationStatus;
        this.orderTime = orderTime;
    }

    public String getReservationID() {
        return " "+reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public ArrayList<Dish> getDishesOrdered() {
        return dishesOrdered;
    }

    public void setDishesOrdered(ArrayList<Dish> dishesOrdered) {
        this.dishesOrdered = dishesOrdered;
    }

    public prepStatus getPreparationStatus() {
        return preparationStatus;
    }

    public String getPreparationStatusString(){
        String ret;
        switch (this.preparationStatus){
            case PENDING:
                ret = "Pending";
                break;
            case DONE:
                ret = "Done";
                break;
            case DOING:
                ret="Doing";
                break;
            default:
                ret="Status Unknown";
                break;
        }
        return ret;
    }

    public void setPreparationStatus(prepStatus preparationStatus) {
        this.preparationStatus = preparationStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getResNote() {
        return resNote;
    }

    public void setResNote(String resNote) {
        this.resNote = resNote;
    }
}
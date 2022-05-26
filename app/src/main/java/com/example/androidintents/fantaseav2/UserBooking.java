package com.example.androidintents.fantaseav2;
//transaction class
public class UserBooking {

    String date_booked,date_scheduled,agency_name,destination_name,activities,destination_province,pump_boat_name,seating_capacity,payID,ticketStatus;
    int price;

    public UserBooking(){

    }
    public UserBooking(String date_booked, String date_scheduled, String agency_name, String destination_name, String activities, String destination_province, String pump_boat_name, String seating_capacity, String payID, String ticketStatus, int price) {
        this.date_booked = date_booked;
        this.date_scheduled = date_scheduled;
        this.agency_name = agency_name;
        this.destination_name = destination_name;
        this.activities = activities;
        this.destination_province = destination_province;
        this.pump_boat_name = pump_boat_name;
        this.seating_capacity = seating_capacity;
        this.payID = payID;
        this.ticketStatus = ticketStatus;
        this.price = price;
    }

    public String getDate_booked() {
        return date_booked;
    }

    public String getDate_scheduled() {
        return date_scheduled;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getActivities() {
        return activities;
    }

    public String getDestination_province() {
        return destination_province;
    }

    public String getPump_boat_name() {
        return pump_boat_name;
    }

    public String getSeating_capacity() {
        return seating_capacity;
    }

    public String getPayID() {
        return payID;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public int getPrice() {
        return price;
    }
}

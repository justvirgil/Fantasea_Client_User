package com.example.androidintents.fantaseav2;

public class UserPending {
    String username, activities, agency_name, destination_name, destination_province, boat_name,capacity,date_sched,date_sent,ticketStatus,payID;
    int base_price;

    public UserPending(String username, String activities, String agency_name, String destination_name, String destination_province, String boat_name, String capacity, String date_sched, String date_sent, String ticketStatus, String payID, int base_price) {
        this.username = username;
        this.activities = activities;
        this.agency_name = agency_name;
        this.destination_name = destination_name;
        this.destination_province = destination_province;
        this.boat_name = boat_name;
        this.capacity = capacity;
        this.date_sched = date_sched;
        this.date_sent = date_sent;
        this.ticketStatus = ticketStatus;
        this.payID = payID;
        this.base_price = base_price;
    }

    public String getUsername() {
        return username;
    }

    public String getActivities() {
        return activities;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getDestination_province() {
        return destination_province;
    }

    public String getBoat_name() {
        return boat_name;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDate_sched() {
        return date_sched;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getPayID() {
        return payID;
    }

    public int getBase_price() {
        return base_price;
    }
}

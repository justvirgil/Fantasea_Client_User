package com.example.androidintents.fantaseav2;

public class UserRating {

    String username, messageToTxt, subjectTxt, messageTxt,date_sent,time_sent,rating;

    public UserRating(){

    }

    public UserRating(String username, String messageToTxt, String subjectTxt, String messageTxt, String date_sent, String time_sent, String rating) {
        this.username = username;
        this.messageToTxt = messageToTxt;
        this.subjectTxt = subjectTxt;
        this.messageTxt = messageTxt;
        this.date_sent = date_sent;
        this.time_sent = time_sent;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getMessageToTxt() {
        return messageToTxt;
    }

    public String getSubjectTxt() {
        return subjectTxt;
    }

    public String getMessageTxt() {
        return messageTxt;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public String getTime_sent() {
        return time_sent;
    }

    public String getRating() {
        return rating;
    }
}

package com.example.androidintents.fantaseav2;
//used for creating message class
public class UserMessage {

    String username, messageToTxt, subjectTxt, messageTxt,date_sent,time_sent;

    public UserMessage() {

    }

    public UserMessage(String username, String messageToTxt, String subjectTxt, String messageTxt, String date_sent, String time_sent) {
        this.username = username;
        this.messageToTxt = messageToTxt;
        this.subjectTxt = subjectTxt;
        this.messageTxt = messageTxt;
        this.date_sent = date_sent;
        this.time_sent = time_sent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessageToTxt() {
        return messageToTxt;
    }

    public void setMessageToTxt(String messageToTxt) {
        this.messageToTxt = messageToTxt;
    }

    public String getSubjectTxt() {
        return subjectTxt;
    }

    public void setSubjectTxt(String subjectTxt) {
        this.subjectTxt = subjectTxt;
    }

    public String getMessageTxt() {
        return messageTxt;
    }

    public void setMessageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(String time_sent) {
        this.time_sent = time_sent;
    }
}

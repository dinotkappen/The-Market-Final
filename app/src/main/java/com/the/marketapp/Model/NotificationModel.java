package com.the.marketapp.Model;

public class NotificationModel {

    public String getNotificationOrderID() {
        return notificationOrderID;
    }

    public void setNotificationOrderID(String notificationOrderID) {
        this.notificationOrderID = notificationOrderID;
    }

    String notificationOrderID;
    String idNotification;
    String titleNotification;
    String messageNotification;

    public String getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(String idNotification) {
        this.idNotification = idNotification;
    }

    public String getTitleNotification() {
        return titleNotification;
    }

    public void setTitleNotification(String titleNotification) {
        this.titleNotification = titleNotification;
    }

    public String getMessageNotification() {
        return messageNotification;
    }

    public void setMessageNotification(String messageNotification) {
        this.messageNotification = messageNotification;
    }





    public NotificationModel(String notificationOrderID,String idNotification,String titleNotification,String messageNotification) {

        this.notificationOrderID=notificationOrderID;
        this.idNotification=idNotification;
        this.titleNotification=titleNotification;
        this.messageNotification=messageNotification;


    }

}

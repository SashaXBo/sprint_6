package ru.praktikum.qa_scooter.model;

public class OrderData {
    public String firstName;
    public String lastName;
    public String address;
    public String station;
    public String phone;
    public String date;
    public String rentalPeriod;
    public String color;
    public String comment;

    public OrderData(String firstName, String lastName, String address, String station,
                     String phone, String date, String rentalPeriod, String color, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.station = station;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }
}

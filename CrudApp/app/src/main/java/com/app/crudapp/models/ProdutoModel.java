package com.app.crudapp.models;

import java.io.Serializable;

public class ProdutoModel implements Serializable {
    private int id;
    private String ProductName;
    private String Description;
    private double Price;
    private int UserID;

    public ProdutoModel()
    {

    }

    public ProdutoModel(String productName, String description) {
        ProductName = productName;
        Description = description;
    }

    public ProdutoModel(int id, String productName, String description) {
        this.id = id;
        ProductName = productName;
        Description = description;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return Price;
    }

    public String getPriceStr() {
        return String.valueOf(Price);
    }

    public void setPrice(double price) {
        Price = price;
    }
}

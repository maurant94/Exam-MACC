package it.uniroma1.dis.exam;

import java.util.Calendar;

/**
 * Created by duca on 10/01/18.
 */

public class Products {
    private Integer id;
    private String name;
    private String quantity;
    private Calendar buyDate;
    private Calendar expDate;

    public Products() {}

    public Products(Integer id, String name, String quantity, Calendar buyDate, Calendar expDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.buyDate = buyDate;
        this.expDate = expDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Calendar getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Calendar buyDate) {
        this.buyDate = buyDate;
    }

    public Calendar getExpDate() {
        return expDate;
    }

    public void setExpDate(Calendar expDate) {
        this.expDate = expDate;
    }
}

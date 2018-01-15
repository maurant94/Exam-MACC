package it.uniroma1.dis.exam;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by duca on 10/01/18.
 */

public class Products {
    private Integer id;
    private String name;
    private String quantity;
    private Date buyDate;
    private Date expDate;

    public Products(Integer id, String name, String quantity, Date buyDate, Date expDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.buyDate = buyDate;
        this.expDate = expDate;
    }

    public Products(String name, String quantity, String buyDate, String expDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.buyDate = sdf.parse(buyDate);
        } catch (ParseException e) {
            Log.d("DATA", "ERRORE DATE BUY" + e.getMessage());
            this.buyDate = new Date();
        }
        try {
            this.expDate = sdf.parse(expDate);
        } catch (ParseException e) {
            Log.d("DATA", "ERRORE DATE EXP" + e.getMessage());
            this.expDate = new Date();
        }
        this.name = name;
        this.quantity = quantity;
    }

    public Products() {
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

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
}
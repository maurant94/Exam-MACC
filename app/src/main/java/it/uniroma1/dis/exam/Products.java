package it.uniroma1.dis.exam;

import java.util.Calendar;

/**
 * Created by duca on 10/01/18.
 */

public class Products {
    private String name;
    private Calendar buyDate;
    private Calendar expDate;

    public Products(String name, Calendar buyDate, Calendar expDate) {
        this.name = name;
        this.buyDate = buyDate;
        this.expDate = expDate;
    }

    public Products(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Calendar getBuyDate() {
        return buyDate;
    }

    public Calendar getExpDate() {
        return expDate;
    }
}

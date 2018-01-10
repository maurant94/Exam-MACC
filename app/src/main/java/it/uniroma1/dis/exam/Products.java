package it.uniroma1.dis.exam;

import java.util.Date;

/**
 * Created by duca on 10/01/18.
 */

public class Products {
    private String name;
    private Date buyDate;
    private Date expDate;

    public Products(String name, Date buyDate, Date expDate) {
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

    public Date getBuyDate() {
        return buyDate;
    }

    public Date getExpDate() {
        return expDate;
    }
}

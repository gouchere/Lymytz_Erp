/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hp Elite 8300
 */
public class BeanDate {

    private int id;
    private Date date;
    private String jour;

    public BeanDate() {
    }

    public BeanDate(int id, Date date) {
        this.id = id;
        this.date = date;
    }

    public BeanDate(int id, Date date, String jour) {
        this.id = id;
        this.date = date;
        this.jour = jour;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public static List<BeanDate> listParamDate = new ArrayList<>();

    public static List<BeanDate> fillDate() {
        return listParamDate;
    }
}

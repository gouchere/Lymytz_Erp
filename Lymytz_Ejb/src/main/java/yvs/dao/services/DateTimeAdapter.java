/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Lymytz Dowes
 */
public class DateTimeAdapter extends XmlAdapter<String, Date> {

    private static final String PATTERN = "dd-MM-yyyy HH:mm:ss";
    private static final String PATTERN_DATE = "dd-MM-yyyy";
    private static final String PATTERN_TIME = "HH:mm:ss";
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(PATTERN);
    private static final SimpleDateFormat DATEFORMAT_DATE = new SimpleDateFormat(PATTERN_DATE);
    private static final SimpleDateFormat DATEFORMAT_TIME = new SimpleDateFormat(PATTERN_TIME);

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        }
        try {
            return DATEFORMAT.format(v);
        } catch (Exception ex1) {
            try {
                return DATEFORMAT_DATE.format(v);
            } catch (Exception ex2) {
                try {
                    return DATEFORMAT_TIME.format(v);
                } catch (Exception ex) {
                    System.err.println("Date Send.... " + v);
                    Logger.getLogger(DateTimeAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        if (v != null ? v.trim().isEmpty() : true) {
            return null;
        }
        try {
            return DATEFORMAT.parse(v);
        } catch (Exception ex1) {
            try {
                return DATEFORMAT_DATE.parse(v);
            } catch (Exception ex2) {
                try {
                    return DATEFORMAT_TIME.parse(v);
                } catch (Exception ex) {
                    System.err.println("Format Send.... " + v);
                    Logger.getLogger(DateTimeAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
    }
}

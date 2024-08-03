/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.util.Date;

/**
 *
 * @author Lymytz-Invité
 */
public class SpecificDate extends Date {

    public SpecificDate() {
        super();
    }

    public SpecificDate(Date d) {
        super(d.getTime());
    }

    public Date addDate(Date d) {
        long l = d.getTime() + this.getTime();
        this.setTime(l);
        return this;
    }  

    @Override
    public boolean after(Date when) {
        return super.after(when); //To change body of generated methods, choose Tools | Templates.
    }
}

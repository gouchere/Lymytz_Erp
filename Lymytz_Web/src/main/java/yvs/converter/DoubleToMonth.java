/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("DM")
public class DoubleToMonth implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            value = value.trim().replace(" ", "");
        }
        double num = 0;
        if ("".equals(value) || value == null) {
            return 0;
        } else {
            try {
                num = Double.valueOf(value);
            } catch (Exception ex) {
                value = value.trim().replace(" ", "");
                char[] t = value.toCharArray();
                StringBuilder val = new StringBuilder("");
                for (char c : t) {
                    String s = String.valueOf(c);
                    if (s.hashCode() != 160) {
                        val.append(s);
                    }
                }
                try {
                    num = Double.valueOf(val.toString());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return num;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = String.valueOf(value);
        double d = ("".equals(str) ? 0 : Double.parseDouble(str));
        if (d == 0) {
            return "-- mois";
        } else {
            int y = (int) (d / 12);
            String ry = y > 0 ? y + " An(s)" : "";
            d = ((double)(d / 12) - y) * 12;
            int m = (int) (d / 30);
            String rm = m > 0 ? m + " Mois" : "";
            d = ((double)(d / 30) - m) * 30;
            int j = (int) (d / 30);
            String rj = j > 0 ? j + " Jour(s)" : "";
            return (ry + " " + rm + " " + rj).trim();
        }
    }
}

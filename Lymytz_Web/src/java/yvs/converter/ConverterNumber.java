/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.text.DecimalFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("CN")
public class ConverterNumber implements Converter {

    private int converter = 0;

    public ConverterNumber(int converter) {
        this.converter = converter;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            value = value.trim();
            value = value.replace(" ", "");
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
        String pattern = "#,##0";
        if (converter > 0) {
            pattern += ".";
            for (int i = 0; i < converter; i++) {
                pattern += "0";
            }
        }
        DecimalFormat df = new DecimalFormat(pattern);
        if (d == 0) {
            return "0";
        } else {
            return df.format(d);
        }
    }
}

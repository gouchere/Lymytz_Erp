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
@FacesConverter("DNA")
public class NumberConverterArrondi implements Converter {

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
        String str = value != null ? String.valueOf(value) : "";
        double d = ("".equals(str) ? 0 : Double.parseDouble(str));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String num = df.format(d) != null ? df.format(d) : "";
        if (num.charAt(num.length() - 2) == '0' && num.charAt(num.length() - 1) == '0') {
            df = new DecimalFormat("#,##0.000000000000000");
            num = df.format(d) != null ? df.format(d) : "";
//            String[] tabs = num.split("[,.]");
//            String resultDecimal = "";
//            if (tabs.length > 1) {
//                String decimal = tabs[1];
//                for (int i = 0; i < decimal.length(); i++) {
//                    char n = decimal.charAt(i);
//                    resultDecimal += n;
//                    if (n != '0') {
//                        break;
//                    }
//                }
//                Double d1 = Double.parseDouble(resultDecimal);
//                if (d1 == 0) {
//                    resultDecimal = "0";
//                }
//            }
//            num = num.substring(0, tabs[0].length() + 1) + resultDecimal;
        }
        while (num.length() > 0 && String.valueOf(num.charAt(num.length() - 1)).equals("0")) {
            num = num.substring(0, num.length() - 1);
        }
        if (num.charAt(num.length() - 1) == '.' || num.charAt(num.length() - 1) == ',') {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }
}

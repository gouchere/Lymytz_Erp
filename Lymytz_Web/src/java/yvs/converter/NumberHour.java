/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.util.Calendar;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("NBH")
public class NumberHour implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        if (value != null) {
//            value = value.trim();
//            value = value.replace(" ", "");
//        }
//        double num = 0;
//        if ("".equals(value) || value == null) {
//            return 0;
//        } else {
//            try {
//                num = Double.valueOf(value);
//            } catch (Exception ex) {
//                value = value.trim().replace(" ", "");
//                char[] t = value.toCharArray();
//                StringBuilder val = new StringBuilder("");
//                for (char c : t) {
//                    String s = String.valueOf(c);
//                    if (s.hashCode() != 160) {
//                        val.append(s);
//                    }
//                }
//                try {
//                    num = Double.valueOf(val.toString());
//                } catch (Exception e) {
//                    return 0;
//                }
//            }
//        }
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String str = String.valueOf(value);
        double d = ("".equals(str) ? 0 : Double.parseDouble(str));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, (int) d);
//        if (d == 0) {
//            return "";
//        } else {
        return Managed.dft.format(c.getTime());
//        }
    }
}

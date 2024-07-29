/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Yves
 * 
 * ce converter est utilis� pour l'affichage des double � valeur nul.
 */
@FacesConverter("LDM")
public class ConvertDateLM implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        if ("".equals(value) || value == null) {
//            return null;
//
//        } else {
//            try {
//                int num = Integer.valueOf(value).intValue();
//                return new Comptes(num);
//            } catch (Exception ex) {
//                return null;
//            }
//        }
        return null; 
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Date d =((Date)value);   
        DateFormat df = new SimpleDateFormat("MMMM yyyy");          
        return df.format(d);
    }

}

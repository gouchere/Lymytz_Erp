/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Yves
 * 
 * ce converter est utilisé pour l'affichage des double à valeur nul.
 */
@FacesConverter("DATETIME")
public class ConvertDateTime implements Converter {

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
        SimpleDateFormat formater=new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.FRANCE);            
        return formater.format(d);
    }

}

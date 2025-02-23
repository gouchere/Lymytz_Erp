/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.util.regex.Pattern;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("PHONE")
public class PhoneConverter implements Converter {

    Pattern pattern = Pattern.compile("^(\\(\\+237\\))? ?([57923]{1}[0-9]{1}[-. ]?){1}([0-9]{2}[-. ]?){3}$");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String numero = String.valueOf(value);
        if (numero != null ? numero.trim().length() > 0 : false) {
            String phone = numero.trim();
            int taille = phone.length();
            if (taille > 8) {
                numero = phone.substring(taille - 2, taille);
                numero = phone.substring(taille - 4, taille - 2) + " " + numero;
                numero = phone.substring(taille - 6, taille - 4) + " " + numero;
                numero = phone.substring(taille - 9, taille - 6) + " " + numero;
            }
            if (taille > 9) {
                numero = phone.substring(0, taille - 9) + " " + numero;
            }
        }
        return numero;
    }
}

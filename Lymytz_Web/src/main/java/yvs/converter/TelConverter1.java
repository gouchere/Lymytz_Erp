/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("TEL")
public class TelConverter1 implements Converter {

    Pattern p = Pattern.compile("^(\\(\\+237\\))? ?([57923]{1}[0-9]{1}[-. ]?){1}([0-9]{2}[-. ]?){3}$");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null ? value.trim().length() > 0 : false) {
            if (p.matcher(value).matches()) {
                return value;
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "format téléphone incorrect !", "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }
}

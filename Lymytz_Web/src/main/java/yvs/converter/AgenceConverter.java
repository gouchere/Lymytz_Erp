/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.parametrage.agence.Agence;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("AgenceC")
public class AgenceConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        //l'id du beanest un long        
        if ("".equals(value.trim()) || value == null) {
            return null;
        } else {
            long id = Long.valueOf(value);
            return new Agence(id);
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else {
            return ((Agence) value).getIdSave()+"";
        }
    }
}

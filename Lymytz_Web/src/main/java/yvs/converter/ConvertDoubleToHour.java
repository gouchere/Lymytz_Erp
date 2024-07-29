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
 * @author Yves
 *
 * ce converter est utilisé pour l'affichage des double à valeur nul.
 */
@FacesConverter("DBH")
public class ConvertDoubleToHour implements Converter {

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
        Double hour = ((Double) value);
        int i = hour.intValue(); //recuperer la partie entiere
        int min = (int) ((hour - i) * 60);
        String stH = (i < 9) ? "0" + i : "" + i;
        String stM = (min < 9) ? "0" + min : "" + min;
        return stH + ":" + stM;
    }

}

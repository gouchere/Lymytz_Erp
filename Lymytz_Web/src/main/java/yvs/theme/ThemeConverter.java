/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.util.Util;

@FacesConverter("themeConverter")
public class ThemeConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                ThemeService service = (ThemeService) fc.getExternalContext().getSessionMap().get("themeService");
                if (service.getThemes().contains(new Theme(Integer.parseInt(value)))) {
                    return service.getThemes().get(service.getThemes().indexOf((new Theme(Integer.parseInt(value)))));
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Theme) object).getId());
        } else {
            return null;
        }
    }
    
}

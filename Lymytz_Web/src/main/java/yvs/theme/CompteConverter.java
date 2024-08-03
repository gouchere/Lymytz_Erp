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
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.util.Util;

@FacesConverter("compteConverter")
public class CompteConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                CompteService service = (CompteService) fc.getExternalContext().getSessionMap().get("compteService");
                if (service.getComptes().contains(new YvsBasePlanComptable(Long.parseLong(value)))) {
                    return service.getComptes().get(service.getComptes().indexOf((new YvsBasePlanComptable(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsBasePlanComptable) object).getId());
        } else {
            return null;
        }
    }
    
}

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
import yvs.entity.base.YvsBaseFournisseur;
import yvs.util.Util;

@FacesConverter("fournisseurConverter")
public class FournisseurConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                FournisseurService service = (FournisseurService) fc.getExternalContext().getSessionMap().get("fournisseurService");
                if (service.getFournisseurs().contains(new YvsBaseFournisseur(Long.parseLong(value)))) {
                    return service.getFournisseurs().get(service.getFournisseurs().indexOf((new YvsBaseFournisseur(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsBaseFournisseur) object).getId());
        } else {
            return null;
        }
    }
    
}

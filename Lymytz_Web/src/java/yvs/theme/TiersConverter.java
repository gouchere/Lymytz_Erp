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
import yvs.entity.tiers.YvsBaseTiers;
import yvs.util.Util;

@FacesConverter("tiersConverter")
public class TiersConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                TiersService service = (TiersService) fc.getExternalContext().getSessionMap().get("tiersService");
                if (service.getTiers().contains(new YvsBaseTiers(Long.parseLong(value)))) {
                    return service.getTiers().get(service.getTiers().indexOf((new YvsBaseTiers(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsBaseTiers) object).getId());
        } else {
            return null;
        }
    }

}

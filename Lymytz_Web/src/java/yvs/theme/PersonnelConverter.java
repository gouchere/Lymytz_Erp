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
import yvs.commercial.Personnel;
import yvs.util.Util;

@FacesConverter("personnelConverter")
public class PersonnelConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                PersonnelService service = (PersonnelService) fc.getExternalContext().getSessionMap().get("personnelService");
                if (service.getPersonnels().contains(new Personnel(Long.parseLong(value)))) {
                    return service.getPersonnels().get(service.getPersonnels().indexOf((new Personnel(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Personnel) object).getId());
        } else {
            return null;
        }
    }

}

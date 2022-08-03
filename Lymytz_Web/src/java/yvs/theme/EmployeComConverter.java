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
import yvs.entity.users.YvsUsers;
import yvs.util.Util;

@FacesConverter("employeComConverter")
public class EmployeComConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                EmployeComService service = (EmployeComService) fc.getExternalContext().getSessionMap().get("employeComService");
                if (service.getEmployes().contains(new YvsUsers(Long.parseLong(value)))) {
                    return service.getEmployes().get(service.getEmployes().indexOf((new YvsUsers(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsUsers) object).getId());
        } else {
            return null;
        }
    }
    
}

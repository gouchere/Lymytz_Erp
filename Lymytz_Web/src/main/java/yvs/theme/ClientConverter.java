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
import yvs.entity.commercial.client.YvsComClient;
import yvs.util.Util;

@FacesConverter("clientConverter")
public class ClientConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                ClientService service = (ClientService) fc.getExternalContext().getSessionMap().get("clientService");
                if (service.getClients().contains(new YvsComClient(Long.parseLong(value)))) {
                    return service.getClients().get(service.getClients().indexOf((new YvsComClient(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }
    
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsComClient) object).getId());
        } else {
            return null;
        }
    }
    
}

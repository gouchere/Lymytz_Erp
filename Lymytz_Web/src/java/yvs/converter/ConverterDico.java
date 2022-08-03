/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.entity.param.YvsDictionnaire;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.societe.UtilSte;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("DICOV")
public class ConverterDico implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            return loadDictionnaire(context, value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object value) {
        if (value != null) {
            try {
                ManagedDico service = (ManagedDico) fc.getExternalContext().getSessionMap().get("Mdico");
                if (service != null) {
                    for (YvsDictionnaire d : service.getListVille()) {
                        if (d.getLibele().equals(value)) {
                            return d.getLibele();
                        }
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private Dictionnaire loadDictionnaire(FacesContext context, String ref) {
        try {
            if (context != null) {
                ManagedDico service = (ManagedDico) context.getExternalContext().getSessionMap().get("Mdico");
                if (service != null) {
                    for (YvsDictionnaire d : service.getListVille()) {
                        if (d.getLibele().equals(ref)) {
                            return UtilSte.buildBeanDictionnaire(d);
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référece dictionnaire incorecte !", ""));
            Logger.getLogger(ConverterDico.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }
}

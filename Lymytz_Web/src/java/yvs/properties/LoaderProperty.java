/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.properties;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author hp Elite 8300
 */
public class LoaderProperty {

    public LoaderProperty() {
    }

    public static String loadPropertie(String cle) {
        FacesContext fc=FacesContext.getCurrentInstance();
        Locale locale=fc.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("yvs.properties.Label",locale);
        return bundle.getString(cle);
    }
}

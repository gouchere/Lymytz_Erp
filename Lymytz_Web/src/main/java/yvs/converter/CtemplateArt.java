/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.entity.prod.YvsBaseArticlesTemplate;

/**
 *
 * @author hp Elite 8300
 */
@FacesConverter("TemplateArt")
public class CtemplateArt implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new YvsBaseArticlesTemplate(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((YvsBaseArticlesTemplate) value).getRefArt();
        } else {
            return " ";
        }
    }

}

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
import yvs.entity.produits.group.YvsBaseFamilleArticle;

/**
 *
 * @author hp Elite 8300
 */
@FacesConverter("FamArt")
public class CFamilleArt implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new YvsBaseFamilleArticle(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((YvsBaseFamilleArticle) value).getReferenceFamille();
        } else {
            return " ";
        }
    }

}

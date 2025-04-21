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
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.Util;

@FacesConverter("articleComConverter")
public class ArticleComConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                ArticleComService service = (ArticleComService) fc.getExternalContext().getSessionMap().get("articleComService");
                if (service.getArticles().contains(new YvsBaseArticles(Long.parseLong(value)))) {
                    return service.getArticles().get(service.getArticles().indexOf((new YvsBaseArticles(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsBaseArticles) object).getId());
        } else {
            return null;
        }
    }

}

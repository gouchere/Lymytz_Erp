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
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.util.Util;

@FacesConverter("articleDepotConverter")
public class ArticleDepotConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (Util.correctStringToInt(value)) {
                ArticleDepotService service = (ArticleDepotService) fc.getExternalContext().getSessionMap().get("articleDepotService");
                if (service.getArticles().contains(new YvsBaseArticleDepot(Long.parseLong(value)))) {
                    return service.getArticles().get(service.getArticles().indexOf((new YvsBaseArticleDepot(Long.parseLong(value)))));
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((YvsBaseArticleDepot) object).getId());
        } else {
            return null;
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.converter;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.parametrage.catTarif.CategorieTarifaire;

/**
 *
 * @author GOUCHERE YVES
 */
@FacesConverter("CcatT")
public class GroupeProduitConverter implements Converter {

    List<CategorieTarifaire> list;
    @EJB
    DaoInterfaceLocal<YvsBaseCategorieClient> dao;

    public GroupeProduitConverter() {
        list = new ArrayList<>();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            return loadCategorie(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }

    private CategorieTarifaire loadCategorie(String ref) {
        String[] ch = {"designation"};
        Object[] val = {ref};
        dao.setEntityClass(YvsBaseCategorieClient.class);
        YvsBaseCategorieClient c = (YvsBaseCategorieClient) dao.getOne(ch, val);
        CategorieTarifaire ct = new CategorieTarifaire(c.getLibelle());
        ct.setId(c.getId());
        return ct;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;
import yvs.entity.production.pilotage.YvsProdComposantOF;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.production.pilotage.YvsProdOperationsOF;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
@Local
public interface DaoInterfaceUtil<T extends Serializable> {
 
    public YvsProdOrdreFabrication saveOF(YvsProdOrdreFabrication of, List<YvsProdComposantOF> lof, List<YvsProdOperationsOF> lph, YvsUsersAgence author);

    public boolean changeStateOF(String state, long id);

    public boolean updateQuatiteNomenclature(double quantite, long id);  
}

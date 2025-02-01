/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComContenuDocVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComContenuDocVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComContenuDocVente> toString(String value);

    ResultatAction<YvsComContenuDocVente> controle(YvsComContenuDocVente entity);

    ResultatAction<YvsComContenuDocVente> save(YvsComContenuDocVente entity);

    ResultatAction<YvsComContenuDocVente> update(YvsComContenuDocVente entity);

    ResultatAction<YvsComContenuDocVente> delete(YvsComContenuDocVente entity);
    
    ResultatAction<YvsComContenuDocVente> returnQuantiteLivre(YvsComContenuDocVente entity);
}

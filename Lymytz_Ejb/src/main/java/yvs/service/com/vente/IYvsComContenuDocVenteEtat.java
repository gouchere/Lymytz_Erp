/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComContenuDocVenteEtat;
import yvs.service.InterfaceEntity;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComContenuDocVenteEtat extends InterfaceEntity{

    ResultatAction<YvsComContenuDocVenteEtat> toString(String value);

    ResultatAction<YvsComContenuDocVenteEtat> controle(YvsComContenuDocVenteEtat entity);

    ResultatAction<YvsComContenuDocVenteEtat> save(YvsComContenuDocVenteEtat entity);

    ResultatAction<YvsComContenuDocVenteEtat> update(YvsComContenuDocVenteEtat entity);

    ResultatAction<YvsComContenuDocVenteEtat> delete(YvsComContenuDocVenteEtat entity);
}

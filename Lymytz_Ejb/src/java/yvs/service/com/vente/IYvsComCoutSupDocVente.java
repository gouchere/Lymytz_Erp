/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.service.InterfaceEntity;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComCoutSupDocVente extends InterfaceEntity{

    ResultatAction<YvsComCoutSupDocVente> toString(String value);

    ResultatAction<YvsComCoutSupDocVente> controle(YvsComCoutSupDocVente entity);

    ResultatAction<YvsComCoutSupDocVente> save(YvsComCoutSupDocVente entity);

    ResultatAction<YvsComCoutSupDocVente> update(YvsComCoutSupDocVente entity);

    ResultatAction<YvsComCoutSupDocVente> delete(YvsComCoutSupDocVente entity);
}

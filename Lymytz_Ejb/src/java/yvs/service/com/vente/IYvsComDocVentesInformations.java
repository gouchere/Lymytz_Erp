/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.service.InterfaceEntity;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComDocVentesInformations extends InterfaceEntity{

    ResultatAction<YvsComDocVentesInformations> toString(String value);

    ResultatAction<YvsComDocVentesInformations> controle(YvsComDocVentesInformations entity);

    ResultatAction<YvsComDocVentesInformations> save(YvsComDocVentesInformations entity);

    ResultatAction<YvsComDocVentesInformations> update(YvsComDocVentesInformations entity);

    ResultatAction<YvsComDocVentesInformations> delete(YvsComDocVentesInformations entity);
}

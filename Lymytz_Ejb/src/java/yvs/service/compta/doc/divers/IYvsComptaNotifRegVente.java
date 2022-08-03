/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaNotifReglementVente;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaNotifRegVente {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaNotifReglementVente> toString(String value);

    ResultatAction<YvsComptaNotifReglementVente> controle(YvsComptaNotifReglementVente entity);

    ResultatAction<YvsComptaNotifReglementVente> save(YvsComptaNotifReglementVente entity);

    ResultatAction<YvsComptaNotifReglementVente> update(YvsComptaNotifReglementVente entity);

    ResultatAction<YvsComptaNotifReglementVente> delete(YvsComptaNotifReglementVente entity);
}

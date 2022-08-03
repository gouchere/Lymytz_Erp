/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import java.util.Date;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComContratsClient {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComContratsClient> controle(YvsComContratsClient entity);

    ResultatAction<YvsComContratsClient> save(YvsComContratsClient entity);

    ResultatAction<YvsComContratsClient> update(YvsComContratsClient entity);

    ResultatAction<YvsComContratsClient> delete(YvsComContratsClient entity);

    ResultatAction<YvsComDocVentes> genereFacture(YvsComContratsClient entity, Date date, YvsUsers vendeur, YvsNiveauAcces niveau, YvsAgences agence, YvsUsersAgence author);
    
    ResultatAction<YvsComDocVentes> genereFacture(YvsComContratsClient entity, Date date, YvsUsers vendeur, YvsBasePointVente point, YvsGrhTrancheHoraire tranche, YvsNiveauAcces niveau, YvsAgences agence, YvsUsersAgence author);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComDocVentes {

    void setDao(DaoInterfaceLocal dao);

    public void setAgence(YvsAgences agence);

    public void setNiveauAcces(YvsNiveauAcces agence);

    ResultatAction<YvsComDocVentes> toString(String value);

    ResultatAction<YvsComDocVentes> controle(YvsComDocVentes entity);

    ResultatAction<YvsComDocVentes> save(YvsComDocVentes entity);

    ResultatAction<YvsComDocVentes> update(YvsComDocVentes entity);

    ResultatAction<YvsComDocVentes> delete(YvsComDocVentes entity);

    ResultatAction<YvsComDocVentes> annulerOrder(YvsComDocVentes entity);

    ResultatAction<YvsWorkflowValidFactureVente> valideEtape(YvsWorkflowValidFactureVente entity);

    ResultatAction<YvsComDocVentes> validerFacture(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currenAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint);

    void reglement(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint, YvsAgences currenAgence);

    //Entity est le bon de livraison
    ResultatAction<YvsComDocVentes> livraison(YvsComDocVentes entity);

    ResultatAction<YvsComDocVentes> livraison(List<YvsComDocVentes> lv, YvsComDocVentes selectDoc, YvsUsersAgence currentUser, YvsSocietes currentScte, YvsAgences currentAgence, YvsBaseDepots currentDepot, YvsBasePointVente currentPoint);

    public ResultatAction validerOrder(YvsComDocVentes selectDoc, YvsUsersAgence currentUser, List<YvsComContenuDocVente> contents);
}

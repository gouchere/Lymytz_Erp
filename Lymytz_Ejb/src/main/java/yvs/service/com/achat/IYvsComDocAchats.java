/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComDocAchats {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComDocAchats> toString(String value);

    ResultatAction<YvsComDocAchats> controle(YvsComDocAchats entity);

    ResultatAction<YvsComDocAchats> save(YvsComDocAchats entity);

    ResultatAction<YvsComDocAchats> update(YvsComDocAchats entity);

    ResultatAction<YvsComDocAchats> delete(YvsComDocAchats entity);

    ResultatAction<YvsWorkflowValidFactureAchat> valideEtape(YvsWorkflowValidFactureAchat entity);

    ResultatAction<YvsComDocAchats> livraison(YvsComDocAchats entity);    
    
    ResultatAction<YvsComDocAchats> annulerReception(YvsComDocAchats entity, YvsNiveauAcces niveau);

    //Entity est le bon de reception
    ResultatAction<YvsComDocAchats> reception(YvsComDocAchats entity);
}

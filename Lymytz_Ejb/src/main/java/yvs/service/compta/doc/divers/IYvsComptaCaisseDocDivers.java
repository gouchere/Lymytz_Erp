/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComptaCaisseDocDivers {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComptaCaisseDocDivers> toString(String value);

    ResultatAction<YvsComptaCaisseDocDivers> controle(YvsComptaCaisseDocDivers entity);

    ResultatAction<YvsComptaCaisseDocDivers> save(YvsComptaCaisseDocDivers entity);

    ResultatAction<YvsComptaCaisseDocDivers> update(YvsComptaCaisseDocDivers entity);

    ResultatAction<YvsComptaCaisseDocDivers> delete(YvsComptaCaisseDocDivers entity);

    ResultatAction<YvsWorkflowValidDocCaisse> validEtapeOrdre(YvsWorkflowValidDocCaisse entity);
}

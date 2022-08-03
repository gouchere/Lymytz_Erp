/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Local;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.operation.YvsMutMouvementCaisse;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
@Local
public interface DaoInterfaceStateFull<T extends Serializable> {

    public boolean saveTransactionEpargne(YvsMutOperationCompte op1, YvsMutOperationCompte op2, YvsMutOperationCompte op3);

    public boolean saveTransactionRegMensualite(YvsMutOperationCompte op1, List<YvsMutReglementMensualite> lmens);

    public boolean saveTransactionRetenue(YvsMutOperationCompte op1, YvsMutMouvementCaisse mvtC);
}

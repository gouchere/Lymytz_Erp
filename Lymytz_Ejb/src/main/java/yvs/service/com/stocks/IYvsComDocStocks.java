/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.stocks;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsComDocStocks {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsComDocStocks> toString(String value);

    ResultatAction<YvsComDocStocks> controle(YvsComDocStocks entity);

    ResultatAction<YvsComDocStocks> save(YvsComDocStocks entity);

    ResultatAction<YvsComDocStocks> update(YvsComDocStocks entity);

    ResultatAction<YvsComDocStocks> delete(YvsComDocStocks entity);

    ResultatAction<YvsComDocStocks> saveInventaire(YvsComDocStocks entity);

    public ResultatAction<YvsWorkflowValidDocStock> valideEtape(YvsWorkflowValidDocStock entity);
}

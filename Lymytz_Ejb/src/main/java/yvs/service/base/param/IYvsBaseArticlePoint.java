/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticlePoint;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseArticlePoint {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseArticlePoint> toString(String value);

    ResultatAction<YvsBaseArticlePoint> controle(YvsBaseArticlePoint entity);

    ResultatAction<YvsBaseArticlePoint> save(YvsBaseArticlePoint entity);

    ResultatAction<YvsBaseArticlePoint> update(YvsBaseArticlePoint entity);

    ResultatAction<YvsBaseArticlePoint> delete(YvsBaseArticlePoint entity);
}

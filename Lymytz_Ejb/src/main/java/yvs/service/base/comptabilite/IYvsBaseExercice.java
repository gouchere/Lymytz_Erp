/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service.base.comptabilite;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseExercice;


/**
 *
 * @author Lymytz-pc
 */
public interface IYvsBaseExercice {
    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsBaseExercice> toString(String value);

    ResultatAction<YvsBaseExercice> controle(YvsBaseExercice entity);

    ResultatAction<YvsBaseExercice> save(YvsBaseExercice entity);

    ResultatAction<YvsBaseExercice> update(YvsBaseExercice entity);

    ResultatAction<YvsBaseExercice> delete(YvsBaseExercice entity);
     
}

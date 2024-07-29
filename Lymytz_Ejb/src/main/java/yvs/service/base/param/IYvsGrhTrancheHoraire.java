/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;

/**
 *
 * @author Lymytz-pc
 */
public interface IYvsGrhTrancheHoraire {

    void setDao(DaoInterfaceLocal dao);

    ResultatAction<YvsGrhTrancheHoraire> toString(String value);

    ResultatAction<YvsGrhTrancheHoraire> controle(YvsGrhTrancheHoraire entity);

    ResultatAction<YvsGrhTrancheHoraire> save(YvsGrhTrancheHoraire entity);

    ResultatAction<YvsGrhTrancheHoraire> update(YvsGrhTrancheHoraire entity);

    ResultatAction<YvsGrhTrancheHoraire> delete(YvsGrhTrancheHoraire entity);

    YvsGrhTrancheHoraire getTrancheHoraire(YvsGrhEmployes employe, List<YvsGrhTrancheHoraire> tranches, List<YvsGrhTrancheHoraire> chevauches, Date heure);

    YvsGrhTrancheHoraire getTrancheHoraire(YvsGrhEmployes employes, Date heure);
}

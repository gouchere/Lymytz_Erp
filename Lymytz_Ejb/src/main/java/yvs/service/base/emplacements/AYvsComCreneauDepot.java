/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCreneauDepot extends AbstractEntity {

    public AYvsComCreneauDepot() {
    }

    public AYvsComCreneauDepot(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCreneauDepot> controle(YvsComCreneauDepot entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getDepot() != null ? entity.getDepot().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un depot");
            }

            if (entity.getJour() != null ? entity.getJour().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un jour");
            }
            if (entity.getTranche() != null ? entity.getTranche().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner une tranche");
            }
        } catch (Exception e) {
        }
        return null;
    }

}

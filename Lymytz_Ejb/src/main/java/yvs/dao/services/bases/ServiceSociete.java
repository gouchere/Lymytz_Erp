/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.bases;

import yvs.dao.DaoInterfaceLocal;
import yvs.dao.InterfaceDaoGeneric;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author hp Elite 8300
 * @param <YvsSocietes>
 * @param <T>
 */
public class ServiceSociete extends InterfaceDaoGeneric<YvsSocietes> {

    DaoInterfaceLocal dao;
    ResultatAction resultat;
    YvsSocietes entitySociete;

    public ServiceSociete(DaoInterfaceLocal dao) {
        this.dao = dao;
        resultat = new ServiceSocieteResult();
        this.entitySociete = entitySociete;
    }

    @Override
    public ResultatAction checkEntity(YvsSocietes entity) {
        if (entity == null) {
            return resultat.emptyEntity(entity.getClass().getSimpleName());
        }
        if (entity.getCodeAbreviation() == null || entity.getCodeAbreviation().equals("")) {
            return resultat.fail("Vous devez indiquer l'abbreviation");
        }
        if (entity.getName() == null || entity.getCodeAbreviation().equals("")) {
            return resultat.fail("Vous devez indiquer la raison social de cette nouvelle Société");
        }
        if (entity.getVenteOnline()) {
            if (entity.getInfosVente().getTypeLivraison() != null ? entity.getInfosVente().getTypeLivraison().getId() < 1 : true) {
                return resultat.fail("Vous devez précisez le type de cout pour les frais de livraison");
            }
        }
        return resultat.succes();
    }

    @Override
    public void save(YvsSocietes entity) {
        if (checkEntity(entity).isResult()) {
            dao.save(entity);
            resultat.setEntity(entity);
        }
    }

    @Override
    public ResultatAction save1(YvsSocietes entity) {
        if (checkEntity(entity).isResult()) {
            entity = (YvsSocietes) dao.save1(entity);
            resultat.setEntity(entity);
        }
        return resultat;
    }

    @Override
    public YvsSocietes update(YvsSocietes entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public YvsSocietes getOne(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(YvsSocietes entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResultatAction getResultat() {
        return resultat;
    }

}

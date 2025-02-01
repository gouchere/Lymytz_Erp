/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.commercial.vente.MensualiteFactureVente;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedMensualiteVente extends Managed<MensualiteFactureVente, YvsComMensualiteFactureVente> implements Serializable {

    private MensualiteFactureVente mensualite = new MensualiteFactureVente();
    private YvsComMensualiteFactureVente entity;

    private YvsComDocVentes docVente = new YvsComDocVentes();

    public ManagedMensualiteVente() {
    }

    public MensualiteFactureVente getMensualite() {
        return mensualite;
    }

    public void setMensualite(MensualiteFactureVente mensualite) {
        this.mensualite = mensualite;
    }

    public YvsComMensualiteFactureVente getEntity() {
        return entity;
    }

    public void setEntity(YvsComMensualiteFactureVente entity) {
        this.entity = entity;
    }

    public YvsComDocVentes getDocVente() {
        return docVente;
    }

    public void setDocVente(YvsComDocVentes docVente) {
        this.docVente = docVente;
    }

    @Override
    public void loadAll() {
        if (docVente.getModelReglement() == null) {
            docVente.setModelReglement(new YvsBaseModelReglement());
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (w != null) {
            int oldOffset = w.getOffset();
            w.setOffset((avancer) ? (w.getOffset() + 1) : (w.getOffset() - 1));
            if (w.getOffset() < 0 || w.getOffset() >= (w.paginator.getNbPage() * w.getNbMax())) {
                w.setOffset(0);
            }
            List<YvsComDocVentes> re = w.paginator.parcoursDynamicData("YvsComDocVentes", "y", "y.enteteDoc.dateEntete DESC, y.heureDoc DESC, y.numDoc DESC", w.getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            } else {
                w.setOffset(oldOffset);
            }
        }
    }

    @Override
    public boolean controleFiche(MensualiteFactureVente bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getFacture() != null ? bean.getFacture().getId() < 1 : true) {
            getErrorMessage("Facture inexistante");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez precisez le montant");
            return false;
        }
        champ = new String[]{"facture", "statut"};
        val = new Object[]{new YvsComDocVentes(bean.getFacture().getId()), Constantes.STATUT_DOC_PAYER};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
        Double a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double avance = (a != null ? a : 0);
        double somme = 0;
        boolean deja = false;
        for (YvsComMensualiteFactureVente m : docVente.getMensualites()) {
            if (!m.getId().equals(bean.getId())) {
                if (!deja) {
                    avance -= m.getMontant();
                } else if (m.getAvance() > 0) {
                    getErrorMessage("Vous ne pouvez pas modifier cette mensualité... car les mensualités suivantes sont engagées");
                    return false;
                }
                somme += m.getMontant();
            } else {
                deja = true;
            }
        }
        if (avance <= 0) {
            bean.setAvance(0);
            bean.setEtat(Constantes.ETAT_ATTENTE);
        } else if (avance < bean.getMontant()) {
            bean.setAvance(avance);
            bean.setEtat(Constantes.ETAT_ENCOURS);
        } else {
            bean.setAvance(bean.getMontant());
            bean.setEtat(Constantes.ETAT_REGLE);
        }
        if (somme + bean.getMontant() > docVente.getMontantTotal()) {
            getErrorMessage("Vous ne pouvez pas entrer ce montant");
            return false;
        }

        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(true);
    }

    public void resetFiche(boolean all) {
        mensualite = new MensualiteFactureVente();
        if (all) {
            docVente = new YvsComDocVentes();
            update("blog_mens_vente");
        } else {
            update("form_mens_vente");
        }
    }

    @Override
    public boolean saveNew() {
        try {
            mensualite.setFacture(new DocVente(docVente.getId()));
            if (controleFiche(mensualite)) {
                entity = UtilCompta.buildMensualiteVente(mensualite, currentUser);
                if (mensualite.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsComMensualiteFactureVente) dao.save1(entity);
                    mensualite.setId(entity.getId());
                } else {
                    dao.update(entity);;
                }
                int idx = docVente.getMensualites().indexOf(entity);
                if (idx > -1) {
                    docVente.getMensualites().set(idx, entity);
                } else {
                    docVente.getMensualites().add(0, entity);
                }
                ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
                if (w != null) {
                    idx = w.getDocuments().indexOf(docVente);
                    if (idx > -1) {
                        w.getDocuments().set(idx, docVente);
                    }
                }
                actionOpenOrResetAfter(this);
                update("data_mensualite_mens_vente");
                succes();
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                dao.delete(entity);
                docVente.getMensualites().remove(entity);
                ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
                if (w != null) {
                    int idx = w.getDocuments().indexOf(docVente);
                    if (idx > -1) {
                        w.getDocuments().set(idx, docVente);
                    }
                }
                update("data_mensualite_mens_vente");
                succes();
            }
        } catch (Exception ex) {

        }
    }

    @Override
    public void onSelectObject(YvsComMensualiteFactureVente y) {
        entity = y;
        mensualite = UtilCompta.buildBeanMensualiteVente(y);
        update("form_mens_vente");
    }

    public void onSelectObject(YvsComDocVentes y) {
        docVente = y;
        if (docVente.getModelReglement() == null) {
            docVente.setModelReglement(new YvsBaseModelReglement());
        }
        setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        update("blog_mens_vente");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComDocVentes) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComMensualiteFactureVente) ev.getObject());
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFiche(false);
    }

    public void chooseModelReglement() {
        if (docVente.getModelReglement() != null ? docVente.getModelReglement().getId() > 0 : false) {
            ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (w != null) {
                int idx = w.getModels().indexOf(docVente.getModelReglement());
                if (idx > -1) {
                    YvsBaseModelReglement y = w.getModels().get(idx);
                    docVente.setModelReglement(y);
                }
            }
        }
    }

    public void generatedEcheancierReg() {
        ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (w != null) {
            if (w.generatedEcheancierReg(docVente, true)) {
                update("data_mensualite_mens_vente");
                succes();
            }
        }
    }

    public void equilibre() {
        docVente.setMensualites(equilibre(docVente));
    }

    public List<YvsComMensualiteFactureVente> equilibre(YvsComDocVentes y) {
        List<YvsComMensualiteFactureVente> list = new ArrayList<>();
        if (y != null ? y.getId() > 0 : false) {
            list = dao.loadNameQueries("YvsComMensualiteFactureVente.findByFacture", new String[]{"facture"}, new Object[]{y});
            List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            for (YvsComMensualiteFactureVente m : list) {
                double somme = 0;
                for (YvsComptaCaissePieceVente p : pieces) {
                    if (p.getMontant() > 0) {
                        if (p.getMontant() > m.getMontant()) {
                            somme += m.getMontant();
                            p.setMontant(p.getMontant() - m.getMontant());
                        } else {
                            double reste = m.getMontant() - somme;
                            if (p.getMontant() > reste) {
                                somme += reste;
                                p.setMontant(p.getMontant() - reste);
                            } else {
                                somme += p.getMontant();
                                p.setMontant(0.0);
                            }
                        }
                    }
                    if (somme == m.getMontant()) {
                        break;
                    }
                }
                if (m.getMontant() <= somme) {
                    m.setEtat(Constantes.ETAT_REGLE);
                } else if (somme > 0) {
                    m.setEtat(Constantes.ETAT_ENCOURS);
                } else {
                    m.setEtat(Constantes.ETAT_ATTENTE);
                }
                dao.update(m);
            }
        }
        return list;
    }
}

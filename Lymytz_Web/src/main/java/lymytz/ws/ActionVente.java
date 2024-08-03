/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
public class ActionVente extends ManagedWS implements Serializable {

    public ActionVente() {
    }

    public boolean validerFacture(long facture, long users) {
        try {
            load(users);
            insertCommercial(facture);
            return validFirstEtapeFacture(facture);
        } catch (Exception ex) {
            return false;
        }
    }

    public void insertCommercial(long facture) {
        if (currentUsers != null ? (currentUsers.getId() != null ? currentUsers.getId() > 0 : false) : false) {
            if (currentUsers.getCommercial() != null ? (currentUsers.getCommercial().getId() != null ? currentUsers.getCommercial().getId() > 0 : false) : false) {
                champ = new String[]{"facture", "commercial"};
                val = new Object[]{new YvsComDocVentes(facture), currentUsers.getCommercial()};
                YvsComCommercialVente y = (YvsComCommercialVente) dao.loadOneByNameQueries("YvsComCommercialVente.findByFactureCommercial", champ, val);
                if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                    y = new YvsComCommercialVente(null);
                    y.setAuthor(currentUser);
                    y.setCommercial(currentUsers.getCommercial());
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setFacture(new YvsComDocVentes(facture));
                    y.setResponsable(true);
                    y.setTaux(100.0);
                    y = (YvsComCommercialVente) dao.save1(y);
                }
            }
        }
    }

    public boolean validFirstEtapeFacture(long facture) {
        if (facture > 0) {
            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{facture});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<YvsWorkflowValidFactureVente> list = saveEtapesValidation(y);
                if (list != null ? !list.isEmpty() : false) {
                    return validEtapeFacture(y, list, list.get(0), (list.size() <= 1));
                }
            }
        }
        return false;
    }

    private List<YvsWorkflowValidFactureVente> saveEtapesValidation(YvsComDocVentes m) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureVente> re = new ArrayList<>();
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, currentScte};
        List<YvsWorkflowEtapeValidation> model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureVente vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidFactureVente w = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureVente();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureVente(m);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm = (YvsWorkflowValidFactureVente) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidFactureVente> ordonneEtapes(List<YvsWorkflowValidFactureVente> l) {
        return YvsWorkflowValidFactureVente.ordonneEtapes(l);
    }

    public boolean controleValidation(YvsComDocVentes selectDoc) {
        if (selectDoc == null) {
            return false;
        }
        for (YvsComContenuDocVente c : selectDoc.getContenus()) {
            //controle les quantités déjà facturé
            Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{selectDoc, Constantes.ETAT_VALIDE, Constantes.TYPE_FV, c.getArticle(), c.getConditionnement()});
            qteFacture = (qteFacture != null) ? qteFacture : 0;
            //trouve la livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByDocLivre", new String[]{"facture", "article", "typeDoc", "statut", "unite"}, new Object[]{selectDoc, c.getArticle(), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, c.getConditionnement()});
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            if (qteFacture < (qteLivre)) {
                return false;
            }
        }
        return true;
    }

    public boolean validEtapeFacture(YvsComDocVentes current, List<YvsWorkflowValidFactureVente> list, YvsWorkflowValidFactureVente etape, boolean lastEtape) {
        if (!asDroitValideEtape(etape.getEtape(), currentUser.getUsers())) {
            return false;
        } else {
            int idx = list.indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                String query = "UPDATE yvs_workflow_valid_facture_vente SET author = " + currentUser.getId() + ", etape_valid = true WHERE id = " + etape.getId();
                if (etape.getEtapeSuivante() == null) {
                    if (validerOrder(current)) {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        if (list.size() > (idx + 1)) {
                            list.get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                    }
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setEtapeActive(false);
                    if (list.size() > (idx + 1)) {
                        list.get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);

                    if (changeStatut(Constantes.ETAT_ENCOURS, current)) {
                        current.setStatut(Constantes.ETAT_ENCOURS);
                        current.setAuthor(currentUser);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validerOrder(YvsComDocVentes y) {
        if (!controleValidation(y)) {
            return false;
        }
        if (changeStatut(Constantes.ETAT_VALIDE, y)) {
            saveEcheancierReg(y);
            return true;
        }
        return false;
    }

    private boolean saveEcheancierReg(YvsComDocVentes y) {
        if (y != null) {
            List<YvsComMensualiteFactureVente> list = generatedEcheancierReg(y);
            for (YvsComMensualiteFactureVente e : list) {
                e.setId(null);
                dao.save(e);
            }
        }
        return true;
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y) {
        double total = dao.loadCaVente(y.getId());
        return generatedEcheancierReg(y, total);
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, double total) {
        return generatedEcheancierReg(y, y.getModelReglement(), total);
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsBaseModelReglement m, double total) {
        List<YvsComMensualiteFactureVente> list = new ArrayList<>();
        if (y != null) {
            if (total > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(y.getDateValider() != null ? y.getDateValider() : new Date());
                //Comptabilise echéancier
                YvsComMensualiteFactureVente e;
                if (m != null ? m.getId() > 0 : false) {
                    if (m.getTranches() != null ? !m.getTranches().isEmpty() : false) {
                        double somme = 0;
                        YvsBaseTrancheReglement t;
                        for (int i = 0; i < m.getTranches().size(); i++) {
                            t = m.getTranches().get(i);
                            double montant = arrondi(total * t.getTaux() / 100);
                            c.add(Calendar.DAY_OF_MONTH, t.getIntervalJour());

                            e = new YvsComMensualiteFactureVente();
                            e.setAuthor(currentUser);
                            e.setDateMensualite(c.getTime());
                            e.setEtat(Constantes.ETAT_ATTENTE);
                            e.setFacture(y);
                            e.setModeReglement(t.getMode());
                            e.setMontant(montant);
                            list.add(e);

                            somme += montant;
                        }
                        if (total > somme) {
                            e = new YvsComMensualiteFactureVente();
                            e.setAuthor(currentUser);
                            e.setDateMensualite(c.getTime());
                            e.setEtat(Constantes.ETAT_ATTENTE);
                            e.setFacture(y);
                            e.setModeReglement(null);
                            e.setMontant(total - somme);
                            list.add(e);
                        }
                    } else {
                        e = new YvsComMensualiteFactureVente();
                        e.setAuthor(currentUser);
                        e.setDateMensualite(c.getTime());
                        e.setEtat(Constantes.ETAT_ATTENTE);
                        e.setFacture(y);
                        e.setModeReglement(null);
                        e.setMontant(total);
                        list.add(e);
                    }
                } else {
                    e = new YvsComMensualiteFactureVente();
                    e.setAuthor(currentUser);
                    e.setDateMensualite(c.getTime());
                    e.setEtat(Constantes.ETAT_ATTENTE);
                    e.setFacture(y);
                    e.setModeReglement(null);
                    e.setMontant(total);
                    list.add(e);
                }
            }
        }
        return list;
    }

    public boolean changeStatut(String etat, YvsComDocVentes doc) {
        if (!etat.equals("")) {
            if (doc.getCloturer()) {
                return false;
            }
            doc.setStatut(etat);
            dao.update(doc);

            Map<String, String> statuts = dao.getEquilibreVente(doc.getId());
            if (statuts != null) {
                doc.setStatutLivre(statuts.get("statut_livre"));
                doc.setStatutRegle(statuts.get("statut_regle"));
            }
            if (doc.getDocumentLie() != null ? doc.getDocumentLie().getId() > 0 : false) {
                statuts = dao.getEquilibreVente(doc.getDocumentLie().getId());
                if (statuts != null) {
                    doc.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    doc.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String getPathArticle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

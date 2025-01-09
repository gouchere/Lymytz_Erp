/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.compta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Query;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Fonctions;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.GenericService;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCompensation;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaJournauxPeriode;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.YvsComptaPhasePieceVirement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.client.YvsComptaPhaseReglementCreditClient;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.compta.fournisseur.YvsComptaPhaseReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.compta.saisie.YvsComptaContentJournalAbonnementDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteBulletin;
import yvs.entity.compta.saisie.YvsComptaContentJournalEnteteFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeAcompteVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapePieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalEtapeReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceAchat;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceMission;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVente;
import yvs.entity.compta.saisie.YvsComptaContentJournalPieceVirement;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditFournisseur;
import yvs.entity.compta.saisie.YvsComptaContentJournalRetenueSalaire;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public class ServiceComptabilite extends GenericService {

    String ERROR;
    List<YvsAgences> agences;
    public Fonctions fonction = new Fonctions();

    public ServiceComptabilite() {
        agences = new ArrayList<>();
    }

    public ServiceComptabilite(YvsNiveauAcces niveau, YvsUsersAgence user, DaoInterfaceLocal dao) {
        this.niveau = niveau;
        this.currentUser = user;
        this.dao = dao;
        this.fonction.loadInfos(user.getAgence().getSociete(), user.getAgence(), currentUser, null, null, null);
    }

    public ServiceComptabilite(YvsNiveauAcces niveau, YvsUsersAgence user, List<YvsAgences> agences, DaoInterfaceLocal dao) {
        this(niveau, user, dao);
        this.agences = agences;
    }

    public double giveSoePieces(List<YvsComptaContentJournal> contenus) {
        double re, cr = 0, db = 0;
        for (YvsComptaContentJournal cc : contenus) {
            db += cc.getDebit();
            cr += cc.getCredit();
        }
        re = db - cr;
        return Constantes.arrondi(re, 3);
    }

    public ResultatAction controleContenu(YvsComptaContentJournal bean) {
        if (bean == null) {
            return new ResultatAction(false, 110, "Contenu non trouvé !");
        }
        if (bean.getCompteGeneral() != null ? (bean.getCompteGeneral().getId() != null ? bean.getCompteGeneral().getId() < 1 : true) : true) {
            return new ResultatAction().emptyCompteGeneral();
        }
        YvsBasePlanComptable c = bean.getCompteGeneral();
        if (c.getSaisieAnalytique() && false) {
        }
        if (c.getSaisieCompteTiers() && bean.getCompteTiers() == null) {
            return new ResultatAction().emptyCompteTiers();
        }
        if (c.getSaisieEcheance() && bean.getEcheance() == null) {
            return new ResultatAction().emptyEcheancier();
        }
        return new ResultatCompta(true, 0, "Succes !");
    }

    public ResultatAction controleExercice(Date date) {
        return controleExercice(date, null);
    }

    public ResultatAction controleExercice(Date date, YvsComptaJournaux jrn) {
        ResultatAction result = new ResultatCompta();
        YvsBaseExercice exo = null;
        if (jrn != null ? jrn.getId() > 0 : false) {
            String query = "SELECT e.id, e.reference, e.date_debut, e.date_fin, e.actif, COALESCE(e.cloturer, FALSE), p.id, p.reference_periode, p.date_debut, p.date_fin, p.actif, p.cloture, y.id, COALESCE(y.cloture, FALSE) "
                    + "FROM yvs_base_exercice e LEFT JOIN yvs_mut_periode_exercice p ON (e.id = p.exercice AND ? BETWEEN p.date_debut AND p.date_fin) "
                    + "LEFT JOIN yvs_compta_journaux_periode y ON (p.id = y.periode AND y.journal = ?) "
                    + "WHERE e.societe = ? AND ? BETWEEN e.date_debut AND e.date_fin ORDER BY p.date_debut";
            Options[] params = new Options[]{new Options(date, 1), new Options(jrn.getId(), 2), new Options(this.currentUser.getAgence().getSociete().getId(), 3), new Options(date, 4)};
            YvsComptaJournauxPeriode r = null;
            YvsMutPeriodeExercice p = null;
            Object[] data = (Object[]) dao.loadObjectBySqlQuery(query, params);
            if (data != null ? data.length > 0 : false) {
                exo = new YvsBaseExercice((Long) data[0], (String) data[1], new Date(((java.sql.Date) data[2]).getTime()), new Date(((java.sql.Date) data[3]).getTime()), (Boolean) data[4], (Boolean) data[5]);
                p = new YvsMutPeriodeExercice((Long) data[6], (String) data[7], new Date(((java.sql.Date) data[8]).getTime()), new Date(((java.sql.Date) data[9]).getTime()), (Boolean) data[10], (Boolean) data[11], exo);
                r = new YvsComptaJournauxPeriode((Long) data[12], (Boolean) data[13], jrn, p);
            }
            if (p != null ? p.getId() > 0 : false) {
                if (r != null ? r.getId() > 0 ? r.getCloture() : false : false) {
                    return result.journalClose(jrn.getCodeJournal(), p.getReferencePeriode());
                }
                if (!p.getActif()) {
                    return result.periodeInactif(p.getReferencePeriode());
                }
                if (p.getCloture()) {
                    return result.periodeClose(p.getReferencePeriode());
                }
            }
        } else {
            exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{this.currentUser.getAgence().getSociete(), date});
        }
        if (exo != null ? exo.getId() < 1 : true) {
            return result.emptyExercice(Constantes.df.format(date));
        }
        if (!exo.getActif()) {
            return result.exerciceInactif(exo.getReference());
        }
        if (exo.getCloturer()) {
            return result.exerciceClose(exo.getReference());
        }
        result.setData(exo);
        result.setResult(true);
        return result;
    }

    public ResultatAction controleComptabiliseMission(YvsComptaCaissePieceMission pm, boolean all, boolean statut) {
        if (pm != null ? (pm.getId() != null ? pm.getId() < 1 : true) : true) {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car les frais de mission n'existent pas");
        }
        if (all || (!all && statut)) {
            if (pm.getStatutPiece() != Constantes.STATUT_DOC_PAYER && pm.getStatutPiece() != Constantes.STATUT_DOC_CLOTURE) {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car les frais de mission doivent être payés ou cloturés");
            }
            if (pm.getMission().getStatutMission() != Constantes.STATUT_DOC_VALIDE && pm.getMission().getStatutMission() != Constantes.STATUT_DOC_CLOTURE) {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car la mission doit etre validée ou terminée");
            }
        }
        if (all || (!all && !statut)) {
            if (pm.getMission().getObjetMission() == null) {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car cette mission n'est pas rattachée à un objet de mission");
            } else {
                //la grille doit avoir un numéro de compte
                if (pm.getMission().getObjetMission().getCompteCharge() == null) {
                    return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car cette mission est rattachée à un objet de mission qui n'a pas de compte général");
                }
            }
            if (pm.getCaisse() == null) {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car ces frais de mission ne sont pas rattachés à une caisse");
            } else {
                if (pm.getCaisse().getCompte() == null) {
                    return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car ces frais de mission sont rattachés à une caisse qui n'a pas de compte");
                }
                if (pm.getCaisse().getJournal() == null) {
                    return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car ces frais de mission sont rattachés à une caisse qui n'a pas de journal");
                }
            }
        }
        return new ResultatAction(true, null, 0L, "Succes");
    }

    public ResultatAction canDeletePieceComptable(List<YvsComptaPiecesComptable> list) {
        try {
            if (list != null ? !list.isEmpty() : false) {
                for (YvsComptaPiecesComptable d : list) {
                    if (d.getStatutPiece().equals(Constantes.STATUT_DOC_VALIDE)) {
                        return new ResultatAction(false, null, 0L, "La comptabilisation de cette pièce est déjà validée");
                    }
                    ResultatAction result = controleExercice(d.getDatePiece(), d.getJournal());
                    if (result != null ? !result.isResult() : true) {
                        return result != null ? result : new ResultatAction(false, null, 0L, "Action impossible!!!");
                    }
                }
                for (YvsComptaPiecesComptable d : list) {
                    dao.delete(d);
                }
            }
            return new ResultatAction(true, null, 0L, "Succès");
        } catch (Exception ex) {
            getException("ManagedSaisiePiece (deletePieceComptable)", ex);
        }
        return new ResultatAction(false, null, 0L, "Action impossible!!!");
    }

    public YvsBaseExercice giveExercice(Date date) {
        return (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByDate", new String[]{"societe", "date"}, new Object[]{this.currentUser.getAgence().getSociete(), date});
    }

    public String getLibelleExtourne(YvsComptaPiecesComptable p) {
        return "EXTOURNE OP";
    }

    public boolean extournePiece(YvsComptaPiecesComptable p, Date date) {
        if (p != null) {
            YvsComptaPiecesComptable piece = new YvsComptaPiecesComptable(p);
            date = date != null ? date : new Date();
            piece.setDatePiece(date);
            piece.setDateSaisie(new Date());
            piece.setJournal(p.getJournal());
            piece.setDateSave(new Date());
            piece.setDateUpdate(new Date());
            piece.setAuthor(currentUser);
            piece.setExtourne(true);
            ResultatAction result = controleExercice(date, p.getJournal());
            YvsBaseExercice exo = result != null ? result.isResult() ? (YvsBaseExercice) result.getData() : null : null;
            if (exo != null ? exo.getId() < 1 : true) {
                return false;
            }
            String num = dao.genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, date, p.getJournal().getId(), currentScte, currentUser.getAgence());
            if (num == null || num.trim().length() < 1) {
                return false;
            }
            piece.setExercice(exo);
            piece.setNumPiece(num);
            piece.setId(null);
            dao.save(piece);
            List<YvsComptaContentJournal> contenus = new ArrayList<>();
            YvsComptaContentJournal c;
            for (YvsComptaContentJournal cj : p.getContentsPiece()) {
                cj = (YvsComptaContentJournal) dao.loadOneByNameQueries("YvsComptaContentJournal.findById", new String[]{"id"}, new Object[]{cj.getId()});
                c = new YvsComptaContentJournal(cj);
                c.setId(null);
                c.setCompteGeneral(cj.getCompteGeneral());
                c.setAuthor(currentUser);
                if (cj.getCredit() > 0) {
                    c.setDebit(cj.getCredit());
                    c.setCredit(0d);
                } else {
                    c.setCredit(cj.getDebit());
                    c.setDebit(0d);
                }
                c.setLibelle(getLibelleExtourne(p));
                c.setPiece(piece);
                c.setLettrage(null);
                piece.setDateSave(new Date());
                piece.setDateUpdate(new Date());
                dao.save(c);
                contenus.add(c);
            }
            return true;
        }
        return false;
    }

    public ResultatAction saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, List<YvsComptaContentJournal> contenus) {
        return saveNewPieceComptable(dateDoc, jrn, -1, contenus);
    }

    public ResultatAction saveNewPieceComptable(Date dateDoc, YvsComptaJournaux jrn, int numero, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                if (jrn != null ? jrn.getId() < 1 : true) {
                    return result.emptyJournal();
                }
                dateDoc = dateDoc != null ? dateDoc : new Date();
                result = controleExercice(dateDoc, jrn);
                YvsBaseExercice exo = null;
                if (result.isResult()) {
                    exo = (YvsBaseExercice) result.getData();
                    if (exo != null ? exo.getId() < 1 : true) {
                        return new ResultatAction().fail("Aucun exercice n'a été trouvé !");
                    }
                } else {
                    return result;
                }
                dao.loadInfos(currentUser.getAgence().getSociete(), currentUser.getAgence(), currentUser, null, null, exo);
                String num = dao.genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, dateDoc, jrn.getId(), jrn.getAgence().getSociete(), jrn.getAgence());
                if (num == null || num.trim().length() < 1) {
                    return new ResultatAction().fail("Le numéro de pièce n'a pas été généré !");
                }
                YvsComptaPiecesComptable p = new YvsComptaPiecesComptable();
                p.setAuthor(this.currentUser);
                p.setDatePiece(dateDoc);
                p.setDateSaisie(new Date());
                p.setExercice(exo);
                p.setJournal(jrn);
                p.setNumPiece(num);
                p.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                p = (YvsComptaPiecesComptable) dao.save1(p);
                for (YvsComptaContentJournal c : contenus) {
                    if (numero > 0 ? c.getNumero().equals(numero) : true) {
                        List<YvsComptaContentAnalytique> lista = new ArrayList<>();
                        lista.addAll(c.getAnalytiques());
                        c.getAnalytiques().clear();

                        c.setId(null);
                        c.setAuthor(currentUser);
                        c.setPiece(p);
                        c = (YvsComptaContentJournal) dao.save1(c);

                        for (YvsComptaContentAnalytique a : lista) {
                            if (a.getCentre() != null ? a.getCentre().getId() > 0 : false) {
                                a.setId(null);
                                a.setContenu(c);
                                a = (YvsComptaContentAnalytique) dao.save1(a);
                                c.getAnalytiques().add(a);
                            }
                        }
                        p.getContentsPiece().add(c);
                    }
                }
                result.setData(p);
                result.setResult(true);
                return result;
            }
        } catch (Exception ex) {
            getException("saveNewPieceComptable Error..", ex);
            return new ResultatAction().fail(ex.getMessage());
        }
        return null;
    }

    public ResultatAction saveNewPieceComptable(YvsComptaPiecesComptable p, List<YvsComptaContentJournal> contenus) {
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                for (YvsComptaContentJournal c : contenus) {
                    List<YvsComptaContentAnalytique> lista = new ArrayList<>();
                    lista.addAll(c.getAnalytiques());
                    c.getAnalytiques().clear();

                    c.setId(null);
                    c.setAuthor(currentUser);
                    c.setPiece(p);
                    c = (YvsComptaContentJournal) dao.save1(c);

                    for (YvsComptaContentAnalytique a : lista) {
                        if (a.getCentre() != null ? a.getCentre().getId() > 0 : false) {
                            a.setId(null);
                            a.setContenu(c);
                            a = (YvsComptaContentAnalytique) dao.save1(a);
                            c.getAnalytiques().add(a);
                        }
                    }
                    p.getContentsPiece().add(c);
                }
            }
        } catch (Exception ex) {
            getException("saveNewPieceComptable Error..", ex);
        }
        return new ResultatAction(true, p, p.getId(), "Succès");
    }

    public ResultatAction saveNewPieceComptable(YvsComptaPiecesComptable pc) {
        List<YvsComptaContentJournal> contents = new ArrayList<>();
        contents.addAll(pc.getContentsPiece());
        pc.getContentsPiece().clear();
        if (pc.getId() > 0) {
            dao.update(pc);
        } else {
            pc.setId(null);
            pc = (YvsComptaPiecesComptable) dao.save1(pc);
        }
        List<YvsComptaContentAnalytique> analytiques;
        YvsComptaContentJournal c;
        for (int i = 0; i < contents.size(); i++) {
            c = contents.get(i);
            c.setPiece(pc);
            analytiques = new ArrayList<>();
            analytiques.addAll(c.getAnalytiques());
            c.getAnalytiques().clear();
            if (c.getId() != null ? c.getId() > 0 : false) {
                dao.update(c);
            } else {
                c.setId(null);
                c = (YvsComptaContentJournal) dao.save1(c);
            }
            c.setAnalytiques(analytiques);
            saveAffectationLine(c);

            contents.set(i, c);
        }
        pc.getContentsPiece().addAll(contents);
        return new ResultatAction(true, pc, pc.getId(), "Succès");
    }

    public void saveAffectationLine(YvsComptaContentJournal y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getAnalytiques() != null ? !y.getAnalytiques().isEmpty() : false) {
                    List<YvsComptaContentAnalytique> analytiques = new ArrayList<>();
                    analytiques.addAll(y.getAnalytiques());
                    for (YvsComptaContentAnalytique c : analytiques) {
                        c.setContenu(y);
                        int idx = y.getAnalytiques().indexOf(c);
                        if (c.getId() > 0) {
                            dao.update(c);
                        } else {
                            c.setId(null);
                            c = (YvsComptaContentAnalytique) dao.save1(c);
                        }
                        y.getAnalytiques().set(idx, c);
                    }
                }
            }
        } catch (Exception ex) {
            getException("Error (saveAllAffectation) ", ex);
        }
    }

    public YvsComptaJournaux giveJournalVente(YvsAgences agence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreVente.findJournalByAgence", new String[]{"agence"}, new Object[]{agence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            String[] champ = new String[]{"agence", "type", "default"};
            Object[] val = new Object[]{agence, Constantes.VENTE, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    public YvsComptaJournaux giveJournalAchat(YvsAgences agence) {
        YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComParametreAchat.findJournalByAgence", new String[]{"agence"}, new Object[]{agence});
        if (jrn != null ? jrn.getId() < 1 : true) {
            String[] champ = new String[]{"agence", "type", "default"};
            Object[] val = new Object[]{agence, Constantes.ACHAT, true};
            jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
        }
        return jrn;
    }

    public YvsComptaJournaux giveJournalSalaire(YvsAgences agence) {
        String[] champ = new String[]{"agence", "type", "default"};
        Object[] val = new Object[]{agence, Constantes.SALAIRE, true};
        return (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
    }

    public YvsComptaJournaux giveJournalDivers(YvsAgences agence) {
        String[] champ = new String[]{"agence", "type", "default"};
        Object[] val = new Object[]{agence, Constantes.TRESORERIE, true};
        return (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
    }

    public String nextLettre(YvsBaseExercice exercice) {
        String value = "A";
        try {
            if (exercice != null) {
                Integer length = (Integer) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLength", new String[]{"societe", "exercice"}, new Object[]{this.currentUser.getAgence().getSociete(), exercice});
                if (length != null ? length > 0 : false) {
                    String lettre = (String) dao.loadObjectByNameQueries("YvsComptaContentJournal.findMaxLetter", new String[]{"societe", "length", "exercice"}, new Object[]{this.currentUser.getAgence().getSociete(), length, exercice});
                    value = Util.nextValue(lettre);
                }
            }
        } catch (Exception ex) {
            getException("nextLettre Error..", ex);
        }
        return value;
    }

    public TempContent convertTabToObjectGain(Object[] line, boolean sum) {
        TempContent re = new TempContent();
        re.setElement(line.length > 1 ? (String) line[1] : null);
        if (sum) {
            re.setCompteCollectif(line.length > 2 ? (Long) line[2] : null);
        } else {
            re.setCompteCharge(line.length > 2 ? (Long) line[2] : null);
        }
        re.setMontantGains(line.length > 3 ? (Double) line[3] : null);
        re.setSaisieAnalytique(line.length > 4 ? line[4] != null ? (Boolean) line[4] : false : false);
        re.setExterne(line.length > 5 ? (Long) line[5] : null);
        re.setMode(line.length > 6 ? (String) line[6] : null);
        re.setCompteCotisation(null);
        re.setCompteTiers(null);
        re.setRetenueP(null);
        re.setRetenueS(null);
        re.setSaisieTiers(false);
        return re;
    }

    public TempContent convertTabToObjectRetenu(Object[] line, boolean salarial, boolean saisie_tiers, boolean sum, boolean others) {
        TempContent re = new TempContent();
        re.setElement(line.length > 1 ? (String) line[1] : null);
        if (salarial) {
            if (saisie_tiers) {
                re.setCompteCollectif(line.length > 2 ? (Long) line[2] : null);
                re.setCompteCharge(line.length > 3 ? (Long) line[3] : null);
                re.setRetenueS(line.length > 4 ? (Double) line[4] : null);
                re.setAutre(line.length > 5 ? (String) line[5] : null);
                re.setCompteTiers(line.length > 6 ? (Long) line[6] : null);
                re.setSaisieAnalytique(line.length > 7 ? line[7] != null ? (Boolean) line[7] : false : false);
                re.setExterne(line.length > 8 ? (Long) line[8] : null);
                re.setMode(line.length > 9 ? (String) line[9] : null);
            } else {
                if (sum) {
                    re.setCompteCollectif(line.length > 2 ? (Long) line[2] : null);
                } else {
                    re.setCompteCotisation(line.length > 2 ? (Long) line[2] : null);
                }
                re.setRetenueS(line.length > 3 ? (Double) line[3] : null);
                re.setCompteTiers(null);
                if (others) {
                    re.setAutre(line.length > 4 ? (String) line[4] : null);
                    re.setCompteTiers(line.length > 5 ? (Long) line[5] : null);
                    re.setSaisieAnalytique(line.length > 6 ? line[6] != null ? (Boolean) line[6] : false : false);
                    re.setExterne(line.length > 7 ? (Long) line[7] : null);
                    re.setMode(line.length > 8 ? (String) line[8] : null);
                } else {
                    re.setSaisieAnalytique(line.length > 4 ? line[4] != null ? (Boolean) line[4] : false : false);
                    re.setExterne(line.length > 5 ? (Long) line[5] : null);
                    re.setMode(line.length > 6 ? (String) line[6] : null);
                }
            }
            re.setRetenueP(null);
        } else {
            re.setCompteCharge(line.length > 2 ? (Long) line[2] : null);
            re.setCompteCotisation(line.length > 3 ? (Long) line[3] : null);
            re.setRetenueP(line.length > 4 ? (Double) line[4] : null);
            re.setSaisieAnalytique(line.length > 5 ? line[5] != null ? (Boolean) line[5] : false : false);
            re.setExterne(line.length > 6 ? (Long) line[6] : null);
            re.setMode(line.length > 7 ? (String) line[7] : null);
            re.setCompteTiers(null);
        }
        re.setSaisieTiers(saisie_tiers);
        re.setMontantGains(null);
        return re;
    }

    public YvsComptaContentJournal factoryContentJ(TempContent t) {
        YvsComptaContentJournal cc = new YvsComptaContentJournal(-t.getId());
        cc.setAuthor(currentUser);
        cc.setJour(1);
        cc.setLibelle(t.getElement());
        cc.setNumPiece(null);
        cc.setNumRef(null);
        cc.setPiece(null);
        cc.setRefExterne((long) 0);
        return cc;
    }

    public List<YvsComptaContentAnalytique> buildAnalytiqueToComptabilise(TempContent t, long entete, long compte, double valeur, boolean debit) {
        return buildAnalytiqueToComptabilise(t, entete, compte, valeur, debit, false, false, false);
    }

    public List<YvsComptaContentAnalytique> buildAnalytiqueToComptabilise(TempContent t, long entete, long compte, double valeur, boolean debit, boolean salarial, boolean saisie_tiers) {
        return buildAnalytiqueToComptabilise(t, entete, compte, valeur, debit, true, salarial, saisie_tiers);
    }

    public List<YvsComptaContentAnalytique> buildAnalytiqueToComptabilise(TempContent t, long entete, long compte, double valeur, boolean debit, boolean retenue, boolean salarial, boolean saisie_tiers) {
        List<YvsComptaContentAnalytique> re = new ArrayList<>();
        if (t.isSaisieAnalytique() && valeur > 0) {
            String rq;
            if (t.getExterne() != null ? t.getExterne() < 1 : true) {
                rq = "select ap.centre_analytique, (" + valeur + ") *(COALESCE(ap.coefficient, 1)/100)), ap.coefficient from yvs_compta_affectation_gen_anal ap "
                        + "where (COALESCE(ap.coefficient,0)!=0) and ap.compte = " + compte + " GROUP BY ap.id";
            } else {
                switch (t.getMode().trim()) {
                    case "E": {
                        rq = "select ap.centre, SUM((SELECT COALESCE(SUM(_db." + (retenue ? (salarial ? "retenu_salariale" : "montant_employeur") : "montant_payer") + "),1) from yvs_grh_detail_bulletin _db inner join yvs_grh_bulletins _b on _b.id=_db.bulletin inner join yvs_grh_contrat_emps _c on _c.id=_b.contrat "
                                + "where _b.entete = b.entete  and db.element_salaire =" + t.getExterne() + " and _c.employe= e.id) *(COALESCE(ap.coeficient, 1)/100)), ap.coeficient "
                                + "from yvs_compta_affec_anal_emp ap inner join yvs_grh_employes e on e.id=ap.employe INNER join yvs_agences ag on e.agence=ag.id "
                                + "inner join yvs_grh_contrat_emps c on c.employe=e.id inner join yvs_grh_bulletins b on c.id=b.contrat "
                                + "inner join yvs_compta_affectation_gen_anal ca on ap.centre=ca.centre_analytique "
                                + "where (COALESCE(ap.coeficient,0)!=0) and ca.compte = " + compte + " and b.entete = " + entete + " and ag.id = " + currentUser.getAgence().getId() + " ";
                        if (saisie_tiers) {
                            rq += "and e.compte_tiers =" + t.getCompteTiers() + " ";
                        }
                        rq += "GROUP BY ap.id";
                        break;
                    }
                    default: {
                        rq = "select ap.centre, SUM((SELECT COALESCE(SUM(_db." + (retenue ? (salarial ? "retenu_salariale" : "montant_employeur") : "montant_payer") + "),1) from yvs_grh_detail_bulletin _db inner join yvs_grh_bulletins _b on _b.id=_db.bulletin inner join yvs_grh_contrat_emps _c on _c.id=_b.contrat "
                                + "inner join yvs_grh_employes _e on _e.id=_c.employe inner join yvs_grh_poste_employes _pe on _e.id=_pe.employe inner join yvs_grh_poste_de_travail _po on _po.id = _pe.poste "
                                + "where _b.entete = b.entete and _db.element_salaire = " + t.getExterne() + " and _po.departement in (select grh_get_sous_service(d.id, true))) *(COALESCE(ap.coeficient, 1)/100)), ap.coeficient "
                                + "from yvs_compta_affec_anal_departement ap "
                                + "inner join yvs_grh_departement d on d.id=ap.departement inner join yvs_grh_poste_de_travail po on po.departement = d.id "
                                + "inner join yvs_grh_poste_employes pe on pe.poste = po.id inner join yvs_grh_employes e on e.id=pe.employe "
                                + "inner join yvs_grh_contrat_emps c on c.employe=e.id inner join yvs_grh_bulletins b on c.id=b.contrat "
                                + "inner join yvs_compta_affectation_gen_anal ca on ap.centre=ca.centre_analytique "
                                + "inner join yvs_agences ag on e.agence=ag.id "
                                + "where (COALESCE(ap.coeficient,0)!=0) and ca.compte = " + compte + " and b.entete = " + entete + " and ag.id = " + currentUser.getAgence().getId() + " ";
                        if (saisie_tiers) {
                            rq += "and e.compte_tiers =" + t.getCompteTiers() + " ";
                        }
                        rq += "GROUP BY ap.id";
                        break;
                    }
                }
            }
            if (rq != null ? rq.trim().length() > 0 : false) {
                YvsComptaContentAnalytique ca;
                List<Object[]> data = dao.loadListBySqlQuery(rq, new Options[]{});
                for (Object[] v : data) {
                    Long centre = v.length > 0 ? (Long) v[0] : null;
                    Double montant = v.length > 1 ? (Double) v[1] : null;
                    if ((centre != null ? centre > 0 : false) && (montant != null ? montant > 0 : false)) {
                        Double coefficient = v.length > 2 ? (Double) v[2] : null;
                        ca = new YvsComptaContentAnalytique((long) -(re.size() + 1));
                        ca.setCentre((YvsComptaCentreAnalytique) dao.loadOneByNameQueries("YvsComptaCentreAnalytique.findById", new String[]{"id"}, new Object[]{centre}));
                        ca.setCoefficient(coefficient);
                        ca.setDebit(debit ? montant : 0);
                        ca.setCredit(debit ? 0 : montant);
                        ca.setAuthor(currentUser);
                        ca.setDateSaisie(new Date());
                        ca.setDateSave(new Date());
                        ca.setDateUpdate(new Date());

                        re.add(ca);
                    }
                }
            }
        }
        return re;
    }

    public ResultatAction convertTabToContenu(Object[] line, Long id) {
        YvsComptaContentJournal re = null;
        if (line != null ? line.length > 0 : false) {
            Long _id = line[0] != null ? Long.valueOf(line[0].toString()) : null;
            Integer _jour = line[1] != null ? Integer.valueOf(line[1].toString()) : 0;
            String _num_piece = line[2] != null ? line[2].toString() : "";
            String _num_ref = line[3] != null ? line[3].toString() : "";
            Long _compte_general = line[4] != null ? Long.valueOf(line[4].toString()) : 0;
            Long _compte_tiers = line[5] != null ? Long.valueOf(line[5].toString()) : 0;
            String _libelle = line[6] != null ? line[6].toString() : "";
            Double _debit = line[7] != null ? Double.valueOf(line[7].toString()) : 0;
            Double _credit = line[8] != null ? Double.valueOf(line[8].toString()) : 0;
            Date _echeance = line[9] != null ? (Date) line[9] : new Date();
            Long _ref_externe = line[10] != null ? Long.valueOf(line[10].toString()) : id;
            String _table_externe = line[11] != null ? line[11].toString() : Constantes.SCR_VENTE;
            String _statut = line[12] != null ? line[12].toString() : Constantes.ETAT_VALIDE;
            String _error = line[13] != null ? line[13].toString() : "";
            Long _contenu = line[14] != null ? Long.valueOf(line[14].toString()) : 0;
            Long _centre = line[15] != null ? Long.valueOf(line[15].toString()) : 0;
            Double _coefficient = line[16] != null ? Double.valueOf(line[16].toString()) : 0;
            Integer _numero = line[17] != null ? Integer.valueOf(line[17].toString()) : 0;
            Long _agence = line[18] != null ? Long.valueOf(line[18].toString()) : 0;
            String _warning = line[19] != null ? line[19].toString() : "";
            String _table_tiers = line[20] != null ? line[20].toString() : "";

            if (_error != null ? _error.trim().length() > 0 : false) {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible.. car " + _error);
            }
            if (_warning != null ? _warning.trim().length() > 0 : false) {
                return new ResultatAction(false, null, 0L, _warning);
            }
            re = new YvsComptaContentJournal(_id, _jour, _num_piece, _num_ref, _libelle, _debit, _credit, _echeance, _compte_general, _compte_tiers, _table_tiers, _ref_externe, _table_externe, _statut.charAt(0), _numero);
            if (re.getCompteGeneral() != null ? re.getCompteGeneral().getId() > 0 : false) {
                re.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{re.getCompteGeneral().getId()}));
            }
            if (_contenu != null ? _contenu != 0 : false) {
                YvsComptaContentAnalytique ca = new YvsComptaContentAnalytique(_id);
                if (_centre != null ? _centre > 0 : false) {
                    ca.setCentre((YvsComptaCentreAnalytique) dao.loadOneByNameQueries("YvsComptaCentreAnalytique.findById", new String[]{"id"}, new Object[]{_centre}));
                }
                ca.setCoefficient(_coefficient);
                ca.setDebit(_debit);
                ca.setCredit(_credit);
                ca.setAuthor(currentUser);
                ca.setDateSaisie(new Date());
                ca.setDateSave(new Date());
                ca.setDateUpdate(new Date());

                re.getAnalytiques().add(ca);
            }
        }
        return new ResultatAction(true, re, re.getId(), "Succès");
    }

    public ResultatAction buildElementGainToComptabilise(List<Object[]> data, int jour, boolean sum, long entete) {
        ResultatAction result = new ResultatAction();
        List<YvsComptaContentJournal> cr = new ArrayList<>();
        List<YvsComptaContentJournal> db = new ArrayList<>();
        YvsComptaContentJournal cc;
        YvsComptaContentJournal cd;
        Object[] line;
        TempContent t;
        for (int i = 0; i < data.size(); i++) {
            line = data.get(i);
            t = convertTabToObjectGain(line, sum);
            if (sum) {
                t.setId(-i);
                cc = factoryContentJ(t);
                if (t.getCompteCollectif() != null ? t.getCompteCollectif() > 0 : false) {
                    cc.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCollectif()}));
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible car le gain " + t.getElement() + " est rattaché à des employes qui n'ont pas de compte collectif");
                }
                cc.setLibelle("Gains liées à '" + cc.getLibelle() + "'");
                cc.setJour(jour);
                cc.setCredit(t.getMontantGains());
                cc.setDebit(0.0);

                if (cc.getCredit() > 0) {
                    cc.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCollectif(), cc.getCredit(), false));
                    cr.add(cc);
                }
            } else {
                t.setId(-(i + 1));
                cd = factoryContentJ(t);
                if (t.getCompteCharge() != null ? t.getCompteCharge() > 0 : false) {
                    cd.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCharge()}));
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible car le gain " + t.getElement() + " n'est rattaché à aucun compte de charge");
                }
                cd.setJour(jour);
                cd.setCompteTiers(null);
                cd.setCredit(0.0);
                cd.setDebit(t.getMontantGains());

                if (cd.getDebit() > 0) {
                    cd.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCharge(), cd.getDebit(), true));
                    db.add(cd);
                }
            }
        }
        List<YvsComptaContentJournal> re = new ArrayList<>();
        re.addAll(cr);
        re.addAll(db);
        return new ResultatCompta(true, re, re, "Succès");
    }

    public ResultatAction buildElementRetenueToComptabilise(List<Object[]> data, int jour, boolean salarial, boolean saisie_tiers, boolean sum, boolean others, long entete) {
        ResultatAction result = new ResultatAction();
        List<YvsComptaContentJournal> cr = new ArrayList<>();
        List<YvsComptaContentJournal> db = new ArrayList<>();
        YvsComptaContentJournal cc;
        YvsComptaContentJournal cd;
        TempContent t;
        Object[] line;
        for (int i = 0; i < data.size(); i++) {
            line = data.get(i);
            t = convertTabToObjectRetenu(line, salarial, saisie_tiers, sum, others);
            if ((t.getRetenueP() != null) ? t.getRetenueP() > 0 : false) {
                t.setId(-i);
                cc = factoryContentJ(t);
                if (t.getCompteCotisation() != null ? t.getCompteCotisation() > 0 : false) {
                    cc.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCotisation()}));
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible car la retenue " + t.getElement() + " n'a pas de compte de cotisation");
                }
                cc.setJour(jour);
                cc.setCompteTiers(null);
                cc.setCredit(t.getRetenueP());
                cc.setDebit(0.0);
                if (cc.getCredit() > 0) {
                    cc.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCotisation(), cc.getCredit(), false, salarial, saisie_tiers));
                    cr.add(cc);
                }

                t.setId(-(i + 1));
                cd = factoryContentJ(t);
                if (t.getCompteCharge() != null ? t.getCompteCharge() > 0 : false) {
                    cd.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCharge()}));
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible car la retenue " + t.getElement() + " n'a pas de compte de charge");
                }
                cd.setJour(jour);
                cd.setCompteTiers(null);
                cd.setCredit(0.0);
                cd.setDebit(t.getRetenueP());
                if (cd.getDebit() > 0) {
                    cd.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCharge(), cd.getDebit(), true, salarial, saisie_tiers));
                    db.add(cd);
                }
            }
            if ((t.getRetenueS() != null) ? t.getRetenueS() > 0 : false) {
                if (t.isSaisieTiers()) {
                    t.setId(-(i + 1));
                    cd = factoryContentJ(t);
                    if (t.getCompteCollectif() != null ? t.getCompteCollectif() > 0 : false) {
                        cd.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCollectif()}));
                    } else {
                        return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " n'a pas de compte collectif");
                    }
                    cd.setLibelle(cd.getLibelle() + (t.getAutre() != null ? " de " + t.getAutre() : ""));
                    if (t.getCompteTiers() != null ? t.getCompteTiers() > 0 : false) {
                        cd.setCompteTiers(t.getCompteTiers());
                    } else {
                        return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " n'a pas de compte tiers");
                    }
                    cd.setJour(jour);
                    cd.setCredit(0.0);
                    cd.setDebit(t.getRetenueS());
                    if (cd.getDebit() > 0) {
                        cd.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCollectif(), cd.getDebit(), true, salarial, saisie_tiers));
                        db.add(cd);
                    }

                    t.setId(-i);
                    cc = factoryContentJ(t);
                    if (t.getCompteTiers() != null ? t.getCompteTiers() > 0 : false) {
                        cc.setCompteTiers(t.getCompteTiers());
                    } else {
                        return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " n'a pas de compte tiers");
                    }
                    if (t.getCompteCharge() != null ? t.getCompteCharge() > 0 : false) {//Compte collectif du tiers
                        cc.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCharge()}));
                    } else {
                        return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " est rattaché à un tiers qui n'a pas de compte collectif");
                    }
                    cc.setLibelle(cc.getLibelle() + (t.getAutre() != null ? " de " + t.getAutre() : ""));
                    cc.setJour(jour);
                    cc.setCredit(t.getRetenueS());
                    cc.setDebit(0.0);
                    if (cc.getCredit() > 0) {
                        cc.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCharge(), cc.getCredit(), false, salarial, saisie_tiers));
                        cr.add(cc);
                    }
                } else {
                    if (sum) {
                        t.setId(-(i + 1));
                        cd = factoryContentJ(t);
                        if (t.getCompteCollectif() != null ? t.getCompteCollectif() > 0 : false) {
                            cd.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCollectif()}));
                        } else {
                            return new ResultatAction().fail("Comptabilisation impossible car la retenue " + t.getElement() + " rattaché à des employés sans compte collectif");
                        }
                        if (others) {
                            cd.setLibelle(cd.getLibelle() + (t.getAutre() != null ? " de " + t.getAutre() : ""));
                            if (t.getCompteTiers() != null ? t.getCompteTiers() > 0 : false) {
                                cd.setCompteTiers(t.getCompteTiers());
                            } else {
                                return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " n'a pas de compte tiers");
                            }
                        }
                        cd.setLibelle("Retenues salariales liées à '" + cd.getLibelle() + "'");
                        cd.setJour(jour);
                        cd.setCompteTiers(null);
                        cd.setCredit(0.0);
                        cd.setDebit(t.getRetenueS());
                        if (cd.getDebit() > 0) {
                            cd.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCollectif(), cd.getDebit(), true, salarial, saisie_tiers));
                            db.add(cd);
                        }
                    } else {
                        t.setId(-i);
                        cc = factoryContentJ(t);
                        if (t.getCompteCotisation() != null ? t.getCompteCotisation() > 0 : false) {
                            cc.setCompteGeneral((YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{t.getCompteCotisation()}));
                        } else {
                            return new ResultatAction().fail("Comptabilisation impossible car la retenue " + t.getElement() + " n'a pas de compte de cotisation");
                        }
                        if (others) {
                            cc.setLibelle(cc.getLibelle() + (t.getAutre() != null ? " de " + t.getAutre() : ""));
                            if (t.getCompteTiers() != null ? t.getCompteTiers() > 0 : false) {
                                cc.setCompteTiers(t.getCompteTiers());
                            } else {
                                return new ResultatAction().fail("Comptabilisation impossible car l'employé " + t.getAutre() + " n'a pas de compte tiers");
                            }
                        }
                        cc.setJour(jour);
                        cc.setCompteTiers(null);
                        cc.setCredit(t.getRetenueS());
                        cc.setDebit(0.0);
                        if (cc.getCredit() > 0) {
                            cc.setAnalytiques(buildAnalytiqueToComptabilise(t, entete, t.getCompteCotisation(), cc.getCredit(), false, salarial, saisie_tiers));
                            cr.add(cc);
                        }
                    }
                }
            }
        }
        List<YvsComptaContentJournal> re = new ArrayList<>();
        re.addAll(cr);
        re.addAll(db);
        return new ResultatCompta(true, re, re, "Succès");
    }

    public ResultatAction buildContentJournal(long id, String table) {
        return buildContentJournal(id, table, null, false);
    }

    public ResultatAction buildContentJournal(String ids, String table, List<YvsAgences> agences, boolean action) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        dao.getEntityManager().clear();
        String query = "select y.* from public.yvs_compta_content_journal(?,?,?,?,?) y";
        String agence = "";
        if (agences != null ? !agences.isEmpty() : false) {
            agence = "0";
            if (!action) {
                for (YvsAgences a : agences) {
                    if (!agences.contains(a)) {
                        agence += "," + a.getId();
                    }
                }
            } else {
                for (YvsAgences a : agences) {
                    agence += "," + a.getId();
                }
            }
        }
        Options[] param = new Options[]{new Options(currentUser.getAgence().getSociete().getId(), 1), new Options(agence, 2), new Options(ids, 3), new Options(table, 4), new Options(true, 5)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        YvsComptaContentJournal line;
        ResultatAction result;
        for (Object o : qr.getResultList()) {
            Double _debit = ((Object[]) o)[7] != null ? Double.valueOf(((Object[]) o)[7].toString()) : 0;
            Double _credit = ((Object[]) o)[8] != null ? Double.valueOf(((Object[]) o)[8].toString()) : 0;
            if (_debit != 0 || _credit != 0) {
                result = convertTabToContenu((Object[]) o, 0L);
                if (result != null ? !result.isResult() : true) {
                    return result != null ? result : new ResultatAction(false, null, null, "Action impossible!!!");
                }
                line = (YvsComptaContentJournal) result.getData();
                result = controleContenu(line);
                if (result != null ? !result.isResult() : true) {
                    return result != null ? result : new ResultatAction(false, null, null, "Action impossible!!!");
                }
                int idx = list.indexOf(line);
                if (idx > -1 && list.get(idx).getRefExterne().equals(line.getRefExterne())) {
                    list.get(idx).getAnalytiques().addAll(line.getAnalytiques());
                } else {
                    list.add(line);
                }
            }
        }
        return new ResultatCompta(true, list, list, "Succes");
    }

    public ResultatAction buildContentJournal(long id, String table, List<YvsAgences> agences, boolean action) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        dao.getEntityManager().clear();
        String query = "select y.* from public.yvs_compta_content_journal(?,?,?,?,?) y";
        String ids = "";
        if (agences != null ? !agences.isEmpty() : false) {
            ids = "0";
            if (!action) {
                for (YvsAgences a : agences) {
                    if (!agences.contains(a)) {
                        ids += "," + a.getId();
                    }
                }
            } else {
                for (YvsAgences a : agences) {
                    ids += "," + a.getId();
                }
            }
        }
        Options[] param = new Options[]{new Options(currentUser.getAgence().getSociete().getId(), 1), new Options(ids, 2), new Options(id, 3), new Options(table, 4), new Options(true, 5)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        YvsComptaContentJournal line;
        ResultatAction result;
        for (Object o : qr.getResultList()) {
            Double _debit = ((Object[]) o)[7] != null ? Double.valueOf(((Object[]) o)[7].toString()) : 0;
            Double _credit = ((Object[]) o)[8] != null ? Double.valueOf(((Object[]) o)[8].toString()) : 0;
            if (_debit != 0 || _credit != 0) {
                result = convertTabToContenu((Object[]) o, id);
                if (result != null ? !result.isResult() : true) {
                    return result != null ? result : new ResultatAction(false, null, null, "Action impossible!!!");
                }
                line = (YvsComptaContentJournal) result.getData();
                result = controleContenu(line);
                if (result != null ? !result.isResult() : true) {
                    return result != null ? result : new ResultatAction(false, null, null, "Action impossible!!!");
                }
                int idx = list.indexOf(line);
                if (idx > -1) {
                    list.get(idx).getAnalytiques().addAll(line.getAnalytiques());
                } else {
                    list.add(line);
                }
            }
        }
        return new ResultatCompta(true, list, list, "Succes");
    }

    //id= id pièce de caisse mision
    public ResultatAction buildMissionToComptabilise(YvsComptaCaissePieceMission y) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        //1. trouvons la pièce de caisse qui correspond à cette mission
        ResultatAction result = controleComptabiliseMission(y, true, false);
        if (result != null ? result.isResult() : false) {
            result = buildContentJournal(y.getId(), Constantes.SCR_FRAIS_MISSIONS);
            if (result != null ? result.isResult() : false) {
                list = (List<YvsComptaContentJournal>) result.getData();
            }
        }
        result.setData(list);
        return result;
    }

    public ResultatAction buildEtapeCaisseVenteToComptabilise(YvsComptaPhasePiece y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE);
    }

    public ResultatAction buildEtapeAcompteVenteToComptabilise(YvsComptaPhaseAcompteVente y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE);
    }

    public ResultatAction buildEtapeCaisseCreditVenteToComptabilise(YvsComptaPhaseReglementCreditClient y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE);
    }

    public ResultatAction buildEtapeCaisseAchatToComptabilise(YvsComptaPhasePieceAchat y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT);
    }

    public ResultatAction buildEtapeCaisseDiversToComptabilise(YvsComptaPhasePieceDivers y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS);
    }

    public ResultatAction buildEtapeCaisseVirementToComptabilise(YvsComptaPhasePieceVirement y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_VIREMENT);
    }

    public ResultatAction buildEtapeAcompteAchatToComptabilise(YvsComptaPhaseAcompteAchat y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT);
    }

    public ResultatAction buildEtapeCaisseCreditAchatToComptabilise(YvsComptaPhaseReglementCreditFournisseur y) {
        return buildContentJournal(y.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT);
    }

    public ResultatAction buildCaisseVenteToComptabilise(YvsComptaCaissePieceVente y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CAISSE_VENTE);
    }

    public ResultatAction buildCaisseAchatToComptabilise(YvsComptaCaissePieceAchat y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CAISSE_ACHAT);
    }

    public ResultatAction buildCaisseCreditVenteToComptabilise(YvsComptaReglementCreditClient y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE);
    }

    public ResultatAction buildCaisseCreditAchatToComptabilise(YvsComptaReglementCreditFournisseur y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT);
    }

    public ResultatAction buildAcompteClientToComptabilise(YvsComptaAcompteClient y) {
        return buildContentJournal(y.getId(), Constantes.SCR_ACOMPTE_VENTE);
    }

    public ResultatAction buildAcompteFournisseurToComptabilise(YvsComptaAcompteFournisseur y) {
        return buildContentJournal(y.getId(), Constantes.SCR_ACOMPTE_ACHAT);
    }

    public ResultatAction buildCreditClientToComptabilise(YvsComptaCreditClient y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CREDIT_VENTE);
    }

    public ResultatAction buildCreditFournisseurToComptabilise(YvsComptaCreditFournisseur y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CREDIT_ACHAT);
    }

    public ResultatAction buildCaisseDiversToComptabilise(YvsComptaCaissePieceDivers y) {
        return buildContentJournal(y.getId(), Constantes.SCR_CAISSE_DIVERS);
    }

    public ResultatAction buildCaisseVirementToComptabilise(YvsComptaCaissePieceVirement y) {
        return buildContentJournal(y.getId(), Constantes.SCR_VIREMENT);
    }

    public ResultatAction buildCaisseMissionToComptabilise(YvsComptaCaissePieceMission y) {
        return buildContentJournal(y.getId(), Constantes.SCR_VIREMENT);
    }

    public ResultatAction buildRetenueToComptabilise(YvsGrhElementAdditionel y) {
        return buildContentJournal(y.getId(), Constantes.SCR_RETENUE);
    }

    public ResultatAction buildBulletinToComptabilise(YvsGrhBulletins y) {
        return buildContentJournal(y.getId(), Constantes.SCR_BULLETIN);
    }

    public ResultatAction buildHeaderVenteToComptabilise(YvsComEnteteDocVente y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            y.setFactures(dao.loadNameQueries("YvsComDocVentes.findByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_FV}));
            if (y.getFactures() != null ? !y.getFactures().isEmpty() : false) {
                if (y.canComptabilise()) {
                    return buildContentJournal(y.getId(), Constantes.SCR_HEAD_VENTE);
                } else {
                    return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car ce journal de vente de vente n'est rattaché à aucune facture valide");
                }
            } else {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car ce journal de vente de vente n'est rattaché à aucune facture");
            }
        } else {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car ce journal de vente de vente n'existe pas");
        }
    }

    public ResultatAction buildAchatToComptabilise(YvsComDocAchats y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_achat co "
                    + "inner join yvs_com_doc_achats dv on co.doc_achat = dv.id "
                    + "where dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(y.getId(), 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        return new ResultatAction(false, null, 0L, "Cette facture est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                    }
                }
                return new ResultatAction(false, null, 0L, "Aucun article de cette facture n'est rattaché à la categorie comptable");
            }
            return buildContentJournal(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT);
        } else {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car cette facture d'achat n'existe pas");
        }
    }

    public ResultatAction buildAvoirAchatToComptabilise(YvsComDocAchats y) {
        List<YvsComptaContentJournal> list = new ArrayList<>();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "select distinct(co.article) from yvs_com_contenu_doc_achat co "
                    + "inner join yvs_com_doc_achats dv on co.doc_achat = dv.id "
                    + "where dv.id = ? and co.article not in (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(y.getId(), 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        return new ResultatAction(false, null, 0L, "Cet avoir est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                    }
                }
                return new ResultatAction(false, null, 0L, "Aucun article de cette facture n'est rattaché à la categorie comptable");
            }
            return buildContentJournal(y.getId(), Constantes.SCR_AVOIR_ACHAT);
        } else {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car cet avoir sur achat n'existe pas");
        }
    }

    public ResultatAction buildSalaireToComptabilise(YvsGrhOrdreCalculSalaire y, List<YvsAgences> agences, boolean action) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getBulletins() != null ? !y.getBulletins().isEmpty() : false) {
                return buildContentJournal(y.getId(), Constantes.SCR_SALAIRE, agences, action);
            } else {
                return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car cet ordre de calcul des salaires n'est rattaché à aucun bulletin");
            }
        } else {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car cet ordre de calcul des salaires n'existe pas");
        }
    }

    public ResultatAction buildVenteToComptabilise(YvsComDocVentes y) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            // Verification si les articles de la facture sont parametré avec la categorie comptable de la facture
            String req = "SELECT DISTINCT(co.article) FROM yvs_com_contenu_doc_vente co INNER JOIN yvs_com_doc_ventes dv ON co.doc_vente = dv.id "
                    + "WHERE dv.id = ? AND co.article NOT IN (select distinct(ca.article) "
                    + "from yvs_base_article_categorie_comptable ca where dv.categorie_comptable = ca.categorie)";
            List<Object> ids = dao.loadListBySqlQuery(req, new Options[]{new Options(y.getId(), 1)});
            if (ids != null ? !ids.isEmpty() : false) {
                Object o = ids.get(0);
                if (o != null) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{(Long) o});
                    if (a != null ? (a.getId() != null ? a.getId() > 0 : false) : false) {
                        return new ResultatAction(false, null, 0L, "Cette facture est rattachée à l'article " + a.getDesignation() + " qui n'est pas rattaché à la catégorie de cette facture");
                    }
                }
                return new ResultatAction(false, null, 0L, "Aucun article de cette facture n'est rattaché à la categorie comptable");
            }
            return buildContentJournal(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE);
        } else {
            return new ResultatAction(false, null, 0L, "Comptabilisation impossible... Car cette facture de vente n'existe pas");
        }
    }

    public String lettrageCompte(List<YvsComptaContentJournal> contenus, YvsBaseExercice exercice) {
        String lettrage = "";
        try {
            if (contenus != null ? !contenus.isEmpty() : false) {
                for (YvsComptaContentJournal c : contenus) {
                    if (!Objects.equals(c.getPiece().getExercice().getId(), exercice.getId())) {
                        return lettrage;
                    }
                }
                lettrage = nextLettre(exercice);
                for (YvsComptaContentJournal c : contenus) {
                    c.setLettrage(lettrage);
                    c.setAuthor(currentUser);
                    c.setDateUpdate(new Date());
                    dao.update(c);
                }
            }
        } catch (Exception ex) {
            getException("ManagedSaisiePiece (lettrageCompte)", ex);
        }
        return lettrage;
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsComDocVentes y) {
        YvsBaseExercice e = giveExercice(y.getEnteteDoc().getDateEntete());
        return lettrerVente(e, y);
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsBaseExercice e, YvsComDocVentes y) {
        List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerVente(e, y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerVente(YvsBaseExercice e, YvsComDocVentes y, List<YvsComptaCaissePieceVente> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            if (e != null ? e.getId() < 1 : true) {
                return null;
            }
            String isLettrer = null;
            List<YvsComptaContentJournal> credits, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementVente.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true)) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, Constantes.SCR_CAISSE_VENTE, e.getDateDebut(), e.getDateFin()});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE});
                    }
                }
            } else {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_VENTE, e.getDateDebut(), e.getDateFin()});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementVente.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_VENTE});
                    }
                }
            }
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        lettrageCompte(list, e);
                        result.addAll(list);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsComDocAchats y) {
        YvsBaseExercice e = giveExercice(y.getDateDoc());
        return lettrerAchat(e, y);
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsBaseExercice e, YvsComDocAchats y) {
        List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerAchat(e, y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerAchat(YvsBaseExercice e, YvsComDocAchats y, List<YvsComptaCaissePieceAchat> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            if (e != null ? e.getId() < 1 : true) {
                return null;
            }
            String isLettrer = null;
            List<YvsComptaContentJournal> credits = null, debits = null;
            YvsComptaAcompteClient acompte = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaNotifReglementAchat.findAcompteByFactureNature", new String[]{"facture", "nature"}, new Object[]{y, 'D'});
            if ((acompte != null ? acompte.getId() < 1 : true) || true) {
                List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT, e.getDateDebut(), e.getDateFin()});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACHAT});
                    }
                }
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{acompte.getId(), Constantes.SCR_ACOMPTE_ACHAT, e.getDateDebut(), e.getDateFin()});
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{acompte});
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_ACHAT});
                    }
                }
            }
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        lettrageCompte(list, e);
                        result.addAll(list);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsComptaCaisseDocDivers y) {
        YvsBaseExercice e = giveExercice(y.getDateDoc());
        return lettrerDivers(e, y);
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsBaseExercice e, YvsComptaCaisseDocDivers y) {
        List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
        return lettrerDivers(e, y, pieces);
    }

    public List<YvsComptaContentJournal> lettrerDivers(YvsBaseExercice e, YvsComptaCaisseDocDivers y, List<YvsComptaCaissePieceDivers> pieces) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (pieces != null ? !pieces.isEmpty() : false) {
            if (e != null ? e.getId() < 1 : true) {
                return null;
            }
            String isLettrer = null;
            List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS, e.getDateDebut(), e.getDateFin()});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS, e.getDateDebut(), e.getDateFin()});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_DIVERS});
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!Util.asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            lettrageCompte(list, e);
                            result.addAll(list);
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerCaisseVente(YvsComptaCaissePieceVente y) {
        YvsBaseExercice e = giveExercice(y.getDatePaiement());
        return lettrerCaisseVente(e, y);
    }

    public List<YvsComptaContentJournal> lettrerCaisseVente(YvsBaseExercice e, YvsComptaCaissePieceVente y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (e != null ? e.getId() < 1 : true) {
            return null;
        }
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceVente.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getVente(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE});
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getVente().getId(), (y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE), e.getDateDebut(), e.getDateFin()});
                    if (!debits.isEmpty()) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                lettrageCompte(list, e);
                                result.addAll(list);
                            }
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerCaisseAchat(YvsComptaCaissePieceAchat y) {
        YvsBaseExercice e = giveExercice(y.getDatePaiement());
        return lettrerCaisseAchat(e, y);
    }

    public List<YvsComptaContentJournal> lettrerCaisseAchat(YvsBaseExercice e, YvsComptaCaissePieceAchat y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (e != null ? e.getId() < 1 : true) {
            return null;
        }
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceAchat.findIdByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y.getAchat(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_ACHAT});
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getAchat().getId(), Constantes.SCR_ACHAT, e.getDateDebut(), e.getDateFin()});
                    if (!credits.isEmpty()) {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        } else {
                            List<YvsComptaContentJournal> list = new ArrayList<>();
                            list.addAll(debits);
                            list.addAll(credits);
                            if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                lettrageCompte(list, e);
                                result.addAll(list);
                            }
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerCaisseDivers(YvsComptaCaissePieceDivers y) {
        YvsBaseExercice e = giveExercice(y.getDateValider());
        return lettrerCaisseDivers(e, y);
    }

    public List<YvsComptaContentJournal> lettrerCaisseDivers(YvsBaseExercice e, YvsComptaCaissePieceDivers y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (e != null ? e.getId() < 1 : true) {
            return null;
        }
        String isLettrer = null;
        List<Long> ids = dao.loadNameQueries("YvsComptaCaissePieceDivers.findIdByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y.getDocDivers(), Constantes.STATUT_DOC_PAYER});
        if (ids != null ? !ids.isEmpty() : false) {
            List<YvsComptaContentJournal> debits = new ArrayList<>();
            List<YvsComptaContentJournal> credits = new ArrayList<>();
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            } else {
                debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_DIVERS});
            }
            if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? (credits != null ? !credits.isEmpty() : false) : (debits != null ? !debits.isEmpty() : false)) {
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS, e.getDateDebut(), e.getDateFin()});
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    }
                } else {
                    credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getDocDivers().getId(), Constantes.SCR_DIVERS, e.getDateDebut(), e.getDateFin()});
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    }
                }
                if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? !debits.isEmpty() : !credits.isEmpty()) {
                    if (y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE)) {
                        if (Util.asString(debits.get(0).getLettrage())) {
                            isLettrer = debits.get(0).getLettrage();
                        }
                    } else {
                        if (Util.asString(credits.get(0).getLettrage())) {
                            isLettrer = credits.get(0).getLettrage();
                        }
                    }
                    if (!Util.asString(isLettrer)) {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            lettrageCompte(list, e);
                            result.addAll(list);
                        }
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerAcompteFournisseur(YvsComptaAcompteFournisseur y) {
        YvsBaseExercice e = giveExercice(y.getDatePaiement() != null ? y.getDatePaiement() : y.getDateAcompte());
        return lettrerAcompteFournisseur(e, y);
    }

    public List<YvsComptaContentJournal> lettrerAcompteFournisseur(YvsBaseExercice e, YvsComptaAcompteFournisseur y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (e != null ? e.getId() < 1 : true) {
            return null;
        }
        String isLettrer = null;
        if (y.getNature().equals('D')) {
            List<YvsComptaContentJournal> credits = null, debits = null;
            debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_ACHAT});
            if (debits != null ? !debits.isEmpty() : false) {
                if (Util.asString(debits.get(0).getLettrage())) {
                    isLettrer = debits.get(0).getLettrage();
                } else {
                    List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdFactureByAcompte", new String[]{"acompte"}, new Object[]{y});
                    if (ids != null && !ids.isEmpty()) {
                        credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, Constantes.SCR_ACHAT, e.getDateDebut(), e.getDateFin()});
                    }
                }
            }
            if (credits != null ? !credits.isEmpty() : false) {
                if (Util.asString(credits.get(0).getLettrage())) {
                    isLettrer = credits.get(0).getLettrage();
                } else {
                    List<YvsComptaContentJournal> list = new ArrayList<>();
                    list.addAll(debits);
                    list.addAll(credits);
                    if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                        lettrageCompte(list, e);
                        result.addAll(list);
                    }
                }
            }
            if (Util.asString(isLettrer)) {
                result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
            }
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerAcompteClient(YvsComptaAcompteClient y) {
        YvsBaseExercice e = giveExercice(y.getDatePaiement() != null ? y.getDatePaiement() : y.getDateAcompte());
        return lettrerAcompteClient(e, y);
    }

    public List<YvsComptaContentJournal> lettrerAcompteClient(YvsBaseExercice e, YvsComptaAcompteClient y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        if (e != null ? e.getId() < 1 : true) {
            return null;
        }
        String isLettrer = null;
        List<YvsComptaContentJournal> credits, debits = null;
        credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterne", new String[]{"id", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_VENTE});
        if (credits != null ? !credits.isEmpty() : false) {
            if (Util.asString(credits.get(0).getLettrage())) {
                isLettrer = credits.get(0).getLettrage();
            } else {
                String nameQueri = y.getNature().equals('D') ? "YvsComptaNotifReglementVente.findIdFactureByAcompte" : "YvsComptaNotifReglementVente.findIdPieceByAcompte";
                List<Long> ids = dao.loadNameQueries(nameQueri, new String[]{"acompte"}, new Object[]{y});
                if (ids != null && !ids.isEmpty()) {
                    debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternesDates", new String[]{"ids", "table", "dateDebut", "dateFin"}, new Object[]{ids, (y.getNature().equals('D') ? Constantes.SCR_VENTE : Constantes.SCR_CAISSE_VENTE), e.getDateDebut(), e.getDateFin()});
                }
            }
        }
        if (debits != null ? !debits.isEmpty() : false) {
            if (Util.asString(debits.get(0).getLettrage())) {
                isLettrer = debits.get(0).getLettrage();
            } else {
                List<YvsComptaContentJournal> list = new ArrayList<>();
                list.addAll(debits);
                list.addAll(credits);
                if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                    lettrageCompte(list, e);
                    result.addAll(list);
                }
            }
        }
        if (Util.asString(isLettrer)) {
            result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
        }
        return result;
    }

    public List<YvsComptaContentJournal> lettrerVirement(YvsComptaCaissePieceVirement y) {
        List<YvsComptaContentJournal> result = new ArrayList<>();
        try {
            String isLettrer = null;
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                List<YvsComptaContentJournal> credits, debits = null;
                YvsComptaContentJournalPieceVirement sortie = (YvsComptaContentJournalPieceVirement) dao.loadOneByNameQueries("YvsComptaContentJournalPieceVirement.findByFactureSource", new String[]{"reglement", "source"}, new Object[]{y, yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0)});
                if (sortie != null ? sortie.getId() < 1 : true) {
                    return result;
                }
                YvsBaseExercice e = sortie.getPiece().getExercice();
                if (e != null ? e.getId() < 1 : true) {
                    return null;
                }
                credits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitPiece", new String[]{"piece"}, new Object[]{sortie.getPiece()});
                if (credits != null ? !credits.isEmpty() : false) {
                    if (Util.asString(credits.get(0).getLettrage())) {
                        isLettrer = credits.get(0).getLettrage();
                    } else {
                        YvsComptaContentJournalPieceVirement entree = (YvsComptaContentJournalPieceVirement) dao.loadOneByNameQueries("YvsComptaContentJournalPieceVirement.findByFactureSource", new String[]{"reglement", "source"}, new Object[]{y, yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0)});
                        if (entree != null ? entree.getId() < 1 : true) {
                            return result;
                        }
                        debits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditPieceDates", new String[]{"piece", "dateDebut", "dateFin"}, new Object[]{entree.getPiece(), e.getDateDebut(), e.getDateFin()});
                    }
                }
                if (debits != null ? !debits.isEmpty() : false) {
                    if (Util.asString(debits.get(0).getLettrage())) {
                        isLettrer = debits.get(0).getLettrage();
                    } else {
                        List<YvsComptaContentJournal> list = new ArrayList<>();
                        list.addAll(debits);
                        list.addAll(credits);
                        if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                            lettrageCompte(list, e);
                            result.addAll(list);
                        }
                    }
                }
                if (Util.asString(isLettrer)) {
                    result = dao.loadNameQueries("YvsComptaContentJournal.findByLettrage", new String[]{"societe", "lettrage"}, new Object[]{currentUser.getAgence().getSociete(), isLettrer});
                }
            }
        } catch (Exception ex) {
            getException("lettrerVirement", ex);
        }
        return result;
    }

    public ResultatAction majComptaCaisseVente(YvsComptaCaissePieceVente y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = dao.isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_VENTE);
                            if (!comptabilise) {
                                return result.acompteNotComptabilise();
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                result.setData(p);
                                result.setResult(true);
                                return result;
                            }
                            return new ResultatAction().fail("Erreur...");
                        }
                    }
                }
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePiece> phases = dao.loadNameQueries("YvsComptaPhasePiece.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        for (YvsComptaPhasePiece r : phases) {
                            result = comptabiliserPhaseCaisseVente(r);
                            if (!result.isResult()) {
                                return result;
                            }
                        }
                        return result;
                    } else {
//                        return result.phaseChequesIsEmpty();
                    }
                }
                boolean noCaisse = y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION) || y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE);
                if (noCaisse ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                    YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : null;
                    if (jrn == null) {
                        switch (y.getModel().getTypeReglement()) {
                            case Constantes.MODE_PAIEMENT_COMPENSATION:
                                jrn = giveJournalVente(y.getVente().getEnteteDoc().getAgence());
                                break;
                            case Constantes.MODE_PAIEMENT_SALAIRE:
                                jrn = giveJournalSalaire(y.getVente().getEnteteDoc().getAgence());
                                break;
                            default:
                                jrn = y.getCaisse().getJournal();
                                break;
                        }
                    }
                    result = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus);
                    YvsComptaPiecesComptable p = null;
                    if (result != null ? result.isResult() : false) {
                        p = (YvsComptaPiecesComptable) result.getData();
                    }
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    //Debut du lettrage
                    lettrerCaisseVente(p.getExercice(), y);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        ResultatAction re;
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            re = buildContentJournal(o.getAchat().getId(), Constantes.SCR_CAISSE_ACHAT, null, false);
                            if (re != null ? re.isResult() : false) {
                                comptabiliserCaisseAchat(o.getAchat(), (List<YvsComptaContentJournal>) re.getData());
                            }
                        }
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return null;
    }

    public ResultatAction majComptaCaisseAchat(YvsComptaCaissePieceAchat y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getNotifs() != null ? y.getNotifs().getId() > 0 : false) {// Verification si le reglement est rattaché a un acompre
                    if (y.getNotifs().getAcompte() != null ? y.getNotifs().getAcompte().getId() > 0 : false) {
                        if (y.getNotifs().getAcompte().getNature().equals('D')) {// Verification si l'acompte est un dépot
                            boolean comptabilise = dao.isComptabilise(y.getNotifs().getAcompte().getId(), Constantes.SCR_ACOMPTE_ACHAT);
                            if (!comptabilise) {
                                return result.acompteNotComptabilise();
                            }
                            if (y.getNotifs().getAcompte().getPieceContenu() != null ? y.getNotifs().getAcompte().getPieceContenu().getId() > 0 : false) {
                                YvsComptaPiecesComptable p = y.getNotifs().getAcompte().getPieceContenu().getPiece();
                                YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                dao.save(c);
                                result.setData(p);
                                result.setResult(true);
                                return result;
                            }
                            return new ResultatAction().fail("Erreur...");
                        }
                    }
                }
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhasePieceAchat> phases = dao.loadNameQueries("YvsComptaPhasePieceAchat.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        for (YvsComptaPhasePieceAchat r : phases) {
                            result = comptabiliserPhaseCaisseAchat(r);
                            if (!result.isResult()) {
                                return result;
                            }
                        }
                        return result;
                    } else {
//                        return result.phaseChequesIsEmpty();
                    }
                }
                boolean noCaisse = y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION);
                if (noCaisse ? true : (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false)) {
                    YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) ? giveJournalAchat(y.getAchat().getAgence()) : y.getCaisse().getJournal();
                    result = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    //Debut du lettrage
                    lettrerCaisseAchat(p.getExercice(), y);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        ResultatAction re;
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            re = buildContentJournal(o.getVente().getId(), Constantes.SCR_CAISSE_VENTE, null, false);
                            if (re != null ? re.isResult() : false) {
                                comptabiliserCaisseVente(o.getVente(), (List<YvsComptaContentJournal>) re.getData());
                            }
                        }
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur....");
    }

    public ResultatAction majComptaCaisseMission(YvsComptaCaissePieceMission y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            //1. trouvons la pièce de caisse qui correspond à cette mission
            result = controleComptabiliseMission(y, false, true);
            if (result != null ? result.isResult() : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal();
                result = saveNewPieceComptable(y.getDatePaiement(), jrn, contenus);
                YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                if (p != null ? p.getId() < 1 : true) {
                    return result != null ? result : new ResultatAction().fail("Erreur...");
                }
                y.setComptabilise(result.isResult());
                YvsComptaContentJournalPieceMission c = new YvsComptaContentJournalPieceMission(y, p);
                c.setAuthor(currentUser);
                dao.save(c);
                return result;
            }
            return result;
        }
        return new ResultatAction().fail("Erreur....");
    }

    public ResultatAction majComptaCaisseCreditAchat(YvsComptaReglementCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhaseReglementCreditFournisseur> phases = dao.loadNameQueries("YvsComptaPhaseReglementCreditFournisseur.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        for (YvsComptaPhaseReglementCreditFournisseur r : phases) {
                            result = comptabiliserPhaseCaisseCreditAchat(r);
                            if (!result.isResult()) {
                                return result;
                            }
                        }
                        return result;
                    } else {
//                        return result.phaseChequesIsEmpty();
                    }
                }
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDatePaiement(), y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalReglementCreditFournisseur c = new YvsComptaContentJournalReglementCreditFournisseur(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    //Debut du lettrage
                    List<Long> ids = dao.loadNameQueries("YvsComptaReglementCreditFournisseur.findIdByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y.getCredit(), Constantes.STATUT_DOC_PAYER});
                    if (ids != null ? !ids.isEmpty() : false) {
                        YvsBaseExercice e = p.getExercice();
                        if (e != null ? e.getId() < 1 : true) {
                            return null;
                        }
                        List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_CREDIT_ACHAT});
                        if (debits != null ? !debits.isEmpty() : false) {
                            if (debits.get(0).getLettrage() != null ? debits.get(0).getLettrage().trim().length() > 0 : false) {
                                return result;
                            }
                            List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getCredit().getId(), Constantes.SCR_CREDIT_ACHAT, e.getDateDebut(), e.getDateFin()});
                            if (!credits.isEmpty()) {
                                if (credits.get(0).getLettrage() != null ? credits.get(0).getLettrage().trim().length() > 0 : false) {
                                    return result;
                                }
                                List<YvsComptaContentJournal> list = new ArrayList<>();
                                list.addAll(debits);
                                list.addAll(credits);
                                if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                    lettrageCompte(list, e);
                                }
                            }
                        }
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur....");
    }

    public ResultatAction majComptaCaisseCreditVente(YvsComptaReglementCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhaseReglementCreditClient> phases = dao.loadNameQueries("YvsComptaPhaseReglementCreditClient.findByPiece", new String[]{"piece"}, new Object[]{y});
                    if (phases != null ? !phases.isEmpty() : false) {
                        for (YvsComptaPhaseReglementCreditClient r : phases) {
                            result = comptabiliserPhaseCaisseCreditVente(r);
                            if (!result.isResult()) {
                                return result;
                            }
                        }
                        return result;
                    } else {
//                        return result.phaseChequesIsEmpty();
                    }
                }
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDatePaiement(), y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalReglementCreditClient c = new YvsComptaContentJournalReglementCreditClient(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    //Debut du lettrage
                    YvsBaseExercice e = p.getExercice();
                    if (e != null ? e.getId() < 1 : true) {
                        return null;
                    }
                    List<Long> ids = dao.loadNameQueries("YvsComptaReglementCreditClient.findIdByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y.getCredit(), Constantes.STATUT_DOC_PAYER});
                    if (ids != null ? !ids.isEmpty() : false) {
                        List<YvsComptaContentJournal> credits = dao.loadNameQueries("YvsComptaContentJournal.findByCreditExternes", new String[]{"ids", "table"}, new Object[]{ids, Constantes.SCR_CAISSE_CREDIT_VENTE});
                        if (credits != null ? !credits.isEmpty() : false) {
                            if (credits.get(0).getLettrage() != null ? credits.get(0).getLettrage().trim().length() > 0 : false) {
                                return result;
                            }
                            List<YvsComptaContentJournal> debits = dao.loadNameQueries("YvsComptaContentJournal.findByDebitExterneDates", new String[]{"id", "table", "dateDebut", "dateFin"}, new Object[]{y.getCredit().getId(), Constantes.SCR_CREDIT_VENTE, e.getDateDebut(), e.getDateFin()});
                            if (!debits.isEmpty()) {
                                if (debits.get(0).getLettrage() != null ? debits.get(0).getLettrage().trim().length() > 0 : false) {
                                    return result;
                                }
                                List<YvsComptaContentJournal> list = new ArrayList<>();
                                list.addAll(debits);
                                list.addAll(credits);
                                if (YvsComptaPiecesComptable.getSolde(list) == 0) {
                                    lettrageCompte(list, e);
                                }
                            }
                        }
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur....");
    }

    public ResultatAction majComptaCaisseVirement(YvsComptaCaissePieceVirement y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() ? contenus.size() > 1 : false : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getSource() != null ? (y.getSource().getId() != null ? y.getSource().getId() > 0 : false) : false) {
                    if (y.getCible() != null ? (y.getCible().getId() != null ? y.getCible().getId() > 0 : false) : false) {
                        //récupération du journal
                        List<YvsComptaContentJournal> sources = new ArrayList<>();
                        List<YvsComptaContentJournal> cibles = new ArrayList<>();
                        for (YvsComptaContentJournal c : contenus) {
                            if (c.getNumero().equals(1)) {
                                c.setNumero(c.getDebit() > 0 ? 1 : 2);
                                cibles.add(c);
                            } else {
                                c.setNumero(c.getDebit() > 0 ? 1 : 2);
                                sources.add(c);
                            }
                        }
                        boolean soumis = y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) ? (!dao.isComptabilise(y.getId(), Constantes.SCR_VIREMENT, true, Constantes.MOUV_CAISS_SORTIE.charAt(0))) : y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS);
                        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS) || soumis) {
                            YvsComptaJournaux journal = y.getJournalSource() != null ? y.getJournalSource() : y.getSource().getJournal();
                            result = saveNewPieceComptable(y.getDatePaimentPrevu(), journal, sources);
                            YvsComptaPiecesComptable p2 = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                            if (p2 != null ? p2.getId() < 1 : true) {
                                return result;
                            }
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(y, p2);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c.setSensCompta(yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0));
                            dao.save(c);
                            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) {
                                return result;
                            }
                        }
                        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            YvsComptaJournaux journal = y.getJournalCible() != null ? y.getJournalCible() : y.getCible().getJournal();
                            result = saveNewPieceComptable(y.getDatePaiement(), journal, cibles);
                            YvsComptaPiecesComptable p1 = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                            if (p1 != null ? p1.getId() < 1 : true) {
                                return result;
                            }
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(y, p1);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            c.setSensCompta(yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0));
                            dao.save(c);
                            //Debut du lettrage
                            lettrerVirement(y);
                            y.setComptabilise(true);
                            return result;
                        }
                    } else {
                        return result.emptyCaisse();
                    }
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaCaisseDivers(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    //Debut du lettrage
                    lettrerCaisseDivers(p.getExercice(), y);
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaEtapeCaisseAchat(YvsComptaPhasePieceAchat y) {
        return majComptaEtapeCaisseAchat(y, buildEtapeCaisseAchatToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseAchat(YvsComptaPhasePieceAchat y, List<YvsComptaContentJournal> contenus) {
        ResultatAction re = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return re.alreadyComptabilise();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    re = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceAchat n = (YvsComptaPhasePieceAchat) dao.loadOneByNameQueries("YvsComptaPhasePieceAchat.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceAchat(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceAchat c = new YvsComptaContentJournalPieceAchat(new YvsComptaCaissePieceAchat(y.getPieceAchat().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceAchat c = new YvsComptaContentJournalEtapePieceAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                    }
                    return re;
                } else {
                    return re.emptyCaisse();
                }
            }
        }
        return null;
    }

    public ResultatAction majComptaEtapeCaisseVente(YvsComptaPhasePiece y) {
        return majComptaEtapeCaisseVente(y, buildEtapeCaisseVenteToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseVente(YvsComptaPhasePiece y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePiece n = (YvsComptaPhasePiece) dao.loadOneByNameQueries("YvsComptaPhasePiece.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceVente(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceVente c = new YvsComptaContentJournalPieceVente(new YvsComptaCaissePieceVente(y.getPieceVente().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceVente c = new YvsComptaContentJournalEtapePieceVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeAcompteVente(YvsComptaPhaseAcompteVente y) {
        return majComptaEtapeAcompteVente(y, buildEtapeAcompteVenteToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeAcompteVente(YvsComptaPhaseAcompteVente y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseAcompteVente n = (YvsComptaPhaseAcompteVente) dao.loadOneByNameQueries("YvsComptaPhaseAcompteVente.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceVente(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalAcompteClient c = new YvsComptaContentJournalAcompteClient(new YvsComptaAcompteClient(y.getPieceVente().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeAcompteVente c = new YvsComptaContentJournalEtapeAcompteVente(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeCaisseDivers(YvsComptaPhasePieceDivers y) {
        return majComptaEtapeCaisseDivers(y, buildEtapeCaisseDiversToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseDivers(YvsComptaPhasePieceDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceDivers n = (YvsComptaPhasePieceDivers) dao.loadOneByNameQueries("YvsComptaPhasePieceDivers.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceDivers(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(new YvsComptaCaissePieceDivers(y.getPieceDivers().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceDivers c = new YvsComptaContentJournalEtapePieceDivers(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeCaisseVirement(YvsComptaPhasePieceVirement y) {
        return majComptaEtapeCaisseVirement(y, buildEtapeCaisseVirementToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseVirement(YvsComptaPhasePieceVirement y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhasePieceVirement n = (YvsComptaPhasePieceVirement) dao.loadOneByNameQueries("YvsComptaPhasePieceVirement.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getVirement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalPieceVirement c = new YvsComptaContentJournalPieceVirement(new YvsComptaCaissePieceVirement(y.getVirement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapePieceVirement c = new YvsComptaContentJournalEtapePieceVirement(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeAcompteAchat(YvsComptaPhaseAcompteAchat y) {
        return majComptaEtapeAcompteAchat(y, buildEtapeAcompteAchatToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeAcompteAchat(YvsComptaPhaseAcompteAchat y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseAcompteAchat n = (YvsComptaPhaseAcompteAchat) dao.loadOneByNameQueries("YvsComptaPhaseAcompteAchat.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getPieceAchat(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalAcompteFournisseur c = new YvsComptaContentJournalAcompteFournisseur(new YvsComptaAcompteFournisseur(y.getPieceAchat().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeAcompteAchat c = new YvsComptaContentJournalEtapeAcompteAchat(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y) {
        return majComptaEtapeCaisseCreditAchat(y, buildEtapeCaisseCreditAchatToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseReglementCreditFournisseur n = (YvsComptaPhaseReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditFournisseur.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getReglement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalReglementCreditFournisseur c = new YvsComptaContentJournalReglementCreditFournisseur(new YvsComptaReglementCreditFournisseur(y.getReglement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeReglementCreditFournisseur c = new YvsComptaContentJournalEtapeReglementCreditFournisseur(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaEtapeCaisseCreditVente(YvsComptaPhaseReglementCreditClient y) {
        return majComptaEtapeCaisseCreditVente(y, buildEtapeCaisseCreditVenteToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaEtapeCaisseCreditVente(YvsComptaPhaseReglementCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateValider(), y.getCaisse().getJournal(), contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    if (p != null ? p.getId() > 0 : false) {
                        //Recherche de la prochaine etape apres celle en cours
                        YvsComptaPhaseReglementCreditClient n = (YvsComptaPhaseReglementCreditClient) dao.loadOneByNameQueries("YvsComptaPhaseReglementCreditClient.findNextByPiece", new String[]{"piece", "numero"}, new Object[]{y.getReglement(), y.getPhaseReg().getNumeroPhase()});
                        if (n != null ? n.getId() < 1 : true) {
                            YvsComptaContentJournalReglementCreditClient c = new YvsComptaContentJournalReglementCreditClient(new YvsComptaReglementCreditClient(y.getReglement().getId()), p);
                            c.setAuthor(currentUser);
                            c.setId(null);
                            dao.save(c);
                        }
                        YvsComptaContentJournalEtapeReglementCreditClient c = new YvsComptaContentJournalEtapeReglementCreditClient(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        y.setComptabilise(true);
                    }
                    return result;
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaHeaderVente(YvsComEnteteDocVente y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<Long> factures = dao.loadNameQueries("YvsComDocVentes.findIdByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_FV});
                if (factures != null ? !factures.isEmpty() : false) {
//                    boolean comptabilise = dao.isComptabilise(factures, Constantes.SCR_VENTE);
//                    if (comptabilise) {
//                        if (true) {
//                            getErrorMessage("Comptabilisation impossible... car ce document est rattaché à des factures déjà comptabilisées");
//                        }
//                        return null;
//                    }
                }
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalVente(y.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateEntete(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalEnteteFactureVente cp = new YvsComptaContentJournalEnteteFactureVente(y, p);
                    cp.setAuthor(currentUser);
                    cp.setId(null);
                    dao.save(cp);

                    YvsComptaContentJournalFactureVente c;
                    for (YvsComDocVentes d : y.getDocuments()) {
                        boolean comptabilise = dao.isComptabilise(d.getId(), Constantes.SCR_VENTE);
                        if (comptabilise) {
                            continue;
                        }
                        if (d.getTypeDoc().equals(Constantes.TYPE_FV) && d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            if ((d.getTiers() != null ? (d.getTiers().getId() > 0 ? (d.getTiers().getCompte() != null ? d.getTiers().getCompte().getId() > 0 : false) : false) : false)) {
                                c = new YvsComptaContentJournalFactureVente(d, p);
                                c.setAuthor(currentUser);
                                c.setId(null);
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                dao.save(c);

                                for (YvsComptaCaissePieceVente r : d.getReglements()) {
                                    if (r.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                        comptabiliserCaisseVente(r);
                                    }
                                }
                                d.setComptabilise(true);
                            }
                        }
                    }
                    y.setComptabilise(true);
                    return result;
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible...car le journal par defaut des ventes n'existe pas");
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaVente(YvsComDocVentes y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalVente(y.getEnteteDoc().getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getEnteteDoc().getDateEntete(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalFactureVente c = new YvsComptaContentJournalFactureVente(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    ResultatAction s;
                    for (YvsComptaCaissePieceVente r : pieces) {
                        s = buildContentJournal(r.getId(), Constantes.SCR_CAISSE_VENTE, null, false);
                        if (s != null ? s.isResult() : false) {
                            comptabiliserCaisseVente(r, (List<YvsComptaContentJournal>) s.getData());
                        }
                    }

                    //Debut du lettrage
                    lettrerVente(p.getExercice(), y, pieces);
                    y.setComptabilise(true);
                    result.setData(p);
                    result.setResult(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction majComptaAchat(YvsComDocAchats y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalAchat(y.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateDoc(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalFactureAchat c = new YvsComptaContentJournalFactureAchat(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureStatut", new String[]{"facture", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                    for (YvsComptaCaissePieceAchat r : pieces) {
                        comptabiliserCaisseAchat(r);
                    }

                    //Debut du lettrage
                    lettrerAchat(p.getExercice(), y, pieces);
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return null;
    }

    public ResultatAction majComptaDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts) {
        ResultatAction result = new ResultatAction();
        YvsComptaPiecesComptable p = null;
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<YvsComptaContentJournal> contenus_abs;
                for (YvsComptaAbonementDocDivers r : abs) {
                    contenus_abs = fonction.buildDiversToComptabilise(r, secs, taxs, couts, dao);
                    if (contenus_abs != null ? contenus_abs.isEmpty() : true) {
                        return result.fail(dao.getRESULT());
                    }
                    r.setContenus(contenus_abs);
                }
                List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                if (y.getIdTiers() != null ? y.getIdTiers() < 1 : true) {
                    List<YvsComptaContentJournal> restes = new ArrayList<>();
                    for (YvsComptaContentJournal c : contenus) {
                        int index = pieces.indexOf(new YvsComptaCaissePieceDivers(c.getRefExterne()));
                        if (index > -1) {
                            pieces.get(index).getContenus().add(c);
                        } else {
                            restes.add(c);
                        }
                    }
                    YvsComptaJournaux jrn;
                    for (YvsComptaCaissePieceDivers r : pieces) {
                        jrn = r.getCaisse().getJournal();
                        result = saveNewPieceComptable(r.getDateValider(), jrn, r.getContenus());
                        p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                        if (p != null ? p.getId() < 1 : true) {
                            return result != null ? result : new ResultatAction().fail("Erreur...");
                        }
                        YvsComptaContentJournalPieceDivers c = new YvsComptaContentJournalPieceDivers(r, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                    }
                    result = saveNewPieceComptable(p, restes);
                    p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                } else {
                    YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalDivers(y.getAgence());
                    if (jrn != null ? (jrn.getId() != null ? jrn.getId() < 1 : true) : true) {
                        return result.emptyJournal();
                    }
                    result = saveNewPieceComptable(y.getDateDoc(), jrn, contenus);
                    p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    for (YvsComptaCaissePieceDivers r : pieces) {
                        comptabiliserCaisseDivers(r);
                    }
                }
                YvsComptaContentJournalDocDivers c = new YvsComptaContentJournalDocDivers(y, p);
                c.setAuthor(currentUser);
                c.setId(null);
                dao.save(c);

                //Debut du lettrage
                lettrerDivers(p.getExercice(), y, pieces);
                y.setComptabilise(true);
                for (YvsComptaAbonementDocDivers r : abs) {
                    comptabiliserAbonnementDivers(r, r.getContenus());
                }
            }
        }
        return result;
    }

    public ResultatAction majComptaSalaire(YvsGrhOrdreCalculSalaire y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalSalaire(currentUser.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getFinMois(), jrn, 5, contenus);
                    YvsComptaPiecesComptable p1 = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p1 != null ? p1.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    List<YvsComptaContentJournal> suite = new ArrayList<>(contenus);
                    suite.removeAll(p1.getContentsPiece());
                    result = saveNewPieceComptable(y.getFinMois(), jrn, suite);
                    YvsComptaPiecesComptable p2 = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p2 != null ? p2.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }

                    YvsComptaContentJournalEnteteBulletin cp = new YvsComptaContentJournalEnteteBulletin(y, p1);
                    cp.setAuthor(currentUser);
                    cp.setId(null);
                    dao.save(cp);
                    cp = new YvsComptaContentJournalEnteteBulletin(y, p2);
                    cp.setAuthor(currentUser);
                    cp.setId(null);
                    dao.save(cp);

                    YvsComptaContentJournalBulletin c;
                    YvsComptaContentJournalPieceVente v;
                    List<YvsGrhDetailPrelevementEmps> retenues;
                    for (YvsGrhBulletins d : y.getBulletins()) {
                        c = new YvsComptaContentJournalBulletin(d, p1);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);
                        c = new YvsComptaContentJournalBulletin(d, p2);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        String[] champ = new String[]{"contrat", "debut", "fin"};
                        Object[] val = new Object[]{d.getContrat(), y.getDateDebutTraitement(), y.getDateFinTraitement()};
                        retenues = dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findForVenteByDates", champ, val);
                        for (YvsGrhDetailPrelevementEmps r : retenues) {
                            if (r.getRetenue().getPiceReglement() != null ? r.getRetenue().getPiceReglement().getId() > 0 : false) {
                                v = new YvsComptaContentJournalPieceVente(r.getRetenue().getPiceReglement(), p1);
                                v.setAuthor(currentUser);
                                v.setId(null);
                                dao.save(v);
                                v = new YvsComptaContentJournalPieceVente(r.getRetenue().getPiceReglement(), p2);
                                v.setAuthor(currentUser);
                                v.setId(null);
                                dao.save(v);
                            }
                        }
                        d.setComptabilise(true);
                    }
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaBulletin(YvsGrhBulletins y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalSalaire(currentUser.getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getEntete().getFinMois(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalBulletin c = new YvsComptaContentJournalBulletin(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaRetenue(YvsGrhElementAdditionel y) {
        return majComptaRetenue(y, buildRetenueToComptabilise(y).getListContent());
    }

    public ResultatAction majComptaRetenue(YvsGrhElementAdditionel y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalSalaire(y.getContrat().getEmploye().getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateElement(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalRetenueSalaire c = new YvsComptaContentJournalRetenueSalaire(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);

                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaAbonnementDivers(YvsComptaAbonementDocDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                YvsComptaJournaux jrn = y.getJournal() != null ? y.getJournal() : giveJournalDivers(y.getDocDivers().getAgence());
                if (jrn != null ? (jrn.getId() != null ? jrn.getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getEcheance(), jrn, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalAbonnementDivers c = new YvsComptaContentJournalAbonnementDivers(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaAcompteFournisseur(YvsComptaAcompteFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        List<YvsComptaPhaseAcompteAchat> phases = dao.loadNameQueries("YvsComptaPhaseAcompteAchat.findByPiece", new String[]{"piece"}, new Object[]{y});
                        if (phases != null ? !phases.isEmpty() : false) {
                            for (YvsComptaPhaseAcompteAchat r : phases) {
                                result = comptabiliserPhaseAcompteAchat(r);
                                if (!result.isResult()) {
                                    return result;
                                }
                            }
                            return result;
                        } else {
//                            return result.phaseChequesIsEmpty();
                        }
                    }
                    if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                        result = saveNewPieceComptable(y.getDatePaiement() != null ? y.getDatePaiement() : y.getDateAcompte(), y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal(), contenus);
                        YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                        if (p != null ? p.getId() < 1 : true) {
                            return result != null ? result : new ResultatAction().fail("Erreur...");
                        }
                        YvsComptaContentJournalAcompteFournisseur c = new YvsComptaContentJournalAcompteFournisseur(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        List<YvsComptaCaissePieceAchat> pieces_achat = dao.loadNameQueries("YvsComptaNotifReglementAchat.findPieceNotComptabiliseByAcompte", new String[]{"acompte"}, new Object[]{y});
                        for (YvsComptaCaissePieceAchat r : pieces_achat) {
                            comptabiliserCaisseAchat(r);
                        }

                        List<YvsComptaCaissePieceDivers> pieces_divers = dao.loadNameQueries("YvsComptaNotifReglementDocDivers.findPieceNotComptabiliseByAcompteAchat", new String[]{"acompte"}, new Object[]{y});
                        for (YvsComptaCaissePieceDivers r : pieces_divers) {
                            comptabiliserCaisseDivers(r);
                        }

                        //Debut du lettrage
                        lettrerAcompteFournisseur(p.getExercice(), y);
                        y.setComptabilise(true);
                        return result;
                    } else {
                        return result.emptyCaisse();
                    }
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaAcompteClient(YvsComptaAcompteClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                    if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        List<YvsComptaPhaseAcompteVente> phases = dao.loadNameQueries("YvsComptaPhaseAcompteVente.findByPiece", new String[]{"piece"}, new Object[]{y});
                        if (phases != null ? !phases.isEmpty() : false) {
                            for (YvsComptaPhaseAcompteVente r : phases) {
                                result = comptabiliserPhaseAcompteVente(r);
                                if (!result.isResult()) {
                                    return result;
                                }
                            }
                            return result;
                        } else {
//                            return result.phaseChequesIsEmpty();
                        }
                    }
                    if (y.getCaisse() != null ? (y.getCaisse().getId() != null ? y.getCaisse().getId() > 0 : false) : false) {
                        result = saveNewPieceComptable(y.getDatePaiement() != null ? y.getDatePaiement() : y.getDateAcompte(), y.getJournal() != null ? y.getJournal() : y.getCaisse().getJournal(), contenus);
                        YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                        if (p != null ? p.getId() < 1 : true) {
                            return result != null ? result : new ResultatAction().fail("Erreur...");
                        }
                        YvsComptaContentJournalAcompteClient c = new YvsComptaContentJournalAcompteClient(y, p);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        dao.save(c);

                        List<YvsComptaCaissePieceVente> pieces_vente = dao.loadNameQueries("YvsComptaNotifReglementVente.findPieceNotComptabiliseByAcompte", new String[]{"acompte"}, new Object[]{y});
                        for (YvsComptaCaissePieceVente r : pieces_vente) {
                            comptabiliserCaisseVente(r);
                        }

                        List<YvsComptaCaissePieceDivers> pieces_divers = dao.loadNameQueries("YvsComptaNotifReglementDocDivers.findPieceNotComptabiliseByAcompteVente", new String[]{"acompte"}, new Object[]{y});
                        for (YvsComptaCaissePieceDivers r : pieces_divers) {
                            comptabiliserCaisseDivers(r);
                        }
                        //Debut du lettrage
                        lettrerAcompteClient(p.getExercice(), y);
                        y.setComptabilise(true);
                        return result;
                    } else {
                        return result.emptyCaisse();
                    }
                } else {
                    return result.emptyCaisse();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaCreditFournisseur(YvsComptaCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getJournal() != null ? (y.getJournal().getId() != null ? y.getJournal().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateCredit(), y.getJournal(), contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalCreditFournisseur c = new YvsComptaContentJournalCreditFournisseur(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    if (contenus != null ? !contenus.isEmpty() : false) {
                        List<YvsComptaReglementCreditFournisseur> pieces = dao.loadNameQueries("YvsComptaReglementCreditFournisseur.findByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                        for (YvsComptaReglementCreditFournisseur r : pieces) {
                            comptabiliserCaisseCreditAchat(r);
                        }
                    }
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction majComptaCreditClient(YvsComptaCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            double solde = giveSoePieces(contenus);
            if (solde != 0) {
                return result.pieceComptaNotEquilibre();
            }
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getJournal() != null ? (y.getJournal().getId() != null ? y.getJournal().getId() > 0 : false) : false) {
                    result = saveNewPieceComptable(y.getDateCredit(), y.getJournal(), contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    if (p != null ? p.getId() < 1 : true) {
                        return result != null ? result : new ResultatAction().fail("Erreur...");
                    }
                    YvsComptaContentJournalCreditClient c = new YvsComptaContentJournalCreditClient(y, p);
                    c.setAuthor(currentUser);
                    c.setId(null);
                    dao.save(c);
                    if (contenus != null ? !contenus.isEmpty() : false) {
                        List<YvsComptaReglementCreditClient> pieces = dao.loadNameQueries("YvsComptaReglementCreditClient.findByCreditStatut", new String[]{"credit", "statut"}, new Object[]{y, Constantes.STATUT_DOC_PAYER});
                        for (YvsComptaReglementCreditClient r : pieces) {
                            comptabiliserCaisseCreditVente(r);
                        }
                    }
                    y.setComptabilise(true);
                    return result;
                } else {
                    return result.emptyJournal();
                }
            }
        }
        return new ResultatAction().fail("Erreur.....");
    }

    public ResultatAction unComptabiliserSalaire(YvsGrhOrdreCalculSalaire y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_SALAIRE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                y.setComptabilise(false);
                y.setComptabilised(false);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserHeaderVente(YvsComEnteteDocVente y) {
        return unComptabiliserHeaderVente(y, true);
    }

    public ResultatAction unComptabiliserHeaderVente(YvsComEnteteDocVente y, boolean unComptabilisedPieceVente) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_HEAD_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                if (unComptabilisedPieceVente) {
//                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_HEAD_VENTE, 2)});
//                     query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente j "
//                        + "WHERE j.piece=p.id AND j.abonnement=?";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});

                    String query = "SELECT DISTINCT y.id FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_compta_content_journal_facture_vente j ON y.vente = j.facture "
                            + "INNER JOIN yvs_compta_content_journal c ON j.piece = c.piece WHERE y.statut_piece = 'P' AND c.ref_externe = ? AND c.table_externe = ?";
                    List<Long> pieces = dao.loadListBySqlQuery(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_HEAD_VENTE, 2)});
                    YvsComptaCaissePieceVente p;
                    for (Long id : pieces) {
                        p = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{id});
                        unComptabiliserCaisseVente(p);
                    }
                }
                y.setComptabilised(false);
                y.setComptabilise(false);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserVente(YvsComDocVentes y) {
        ResultatAction result = new ResultatAction();
        try {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    return result.notAcces();
                }
                String[] champ = new String[]{"id", "table"};
                Object[] val = new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE};
                String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
                List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
                result = canDeletePieceComptable(list);
                if (result != null ? result.isResult() : false) {
                    if (list != null ? list.isEmpty() : true) {
                        String query = "SELECT DISTINCT c.table_externe FROM yvs_compta_content_journal c INNER JOIN yvs_compta_content_journal_facture_vente p ON p.piece = c.piece WHERE p.facture = ?";
                        List<String> tables = dao.loadListBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
                        for (String table : tables) {
                            if (table.equals(Constantes.SCR_HEAD_VENTE)) {
                                return new ResultatAction().fail("Cette facture est comptabilisée à partir de son journal de vente");
                            }
                        }
                    }
                    try {
                        //Supprimme tous les contenu en rapport avec la facture
                        String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_VENTE, 2)});
                        query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_vente j WHERE j.piece=p.id AND j.facture=?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                        for (YvsComptaCaissePieceVente p : y.getReglements()) {
                            unComptabiliserCaisseVente(p);
                        }
                        y.setComptabilised(false);
                        y.setComptabilise(false);
                        y.setPieceContenu(null);
                    } catch (Exception ex) {
                        getException(ex.getMessage(), ex);
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            getException("ManagedSaisiePiece (unComptabiliserVente)", ex);
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserAchat(YvsComDocAchats y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                try {
                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACHAT, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_facture_achat j "
                            + "WHERE j.piece=p.id AND j.facture=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    for (YvsComptaCaissePieceAchat p : y.getReglements()) {
                        unComptabiliserCaisseAchat(p);
                    }
                } catch (Exception ex) {
                    getException(ex.getMessage(), ex);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserDivers(YvsComptaCaisseDocDivers y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_DIVERS};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                list.clear();
                List<YvsComptaPiecesComptable> sous = null;
                for (YvsComptaCaissePieceDivers p : y.getReglements()) {
                    val = new Object[]{p.getId(), Constantes.SCR_CAISSE_DIVERS};
                    sous = dao.loadNameQueries(nameQueri, champ, val);
                    if (sous != null ? !sous.isEmpty() : false) {
                        list.addAll(sous);
                    }
                }
                result = canDeletePieceComptable(list);
                if (result != null ? result.isResult() : false) {
                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_DIVERS, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_doc_divers j "
                            + "WHERE j.piece=p.id AND j.divers=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    for (YvsComptaCaissePieceDivers p : y.getReglements()) {
                        if (unComptabiliserCaisseDivers(p, false).isResult()) {
                            p.setPieceContenu(null);
                        }
                    }
                    for (YvsComptaAbonementDocDivers p : y.getAbonnements()) {
                        unComptabiliserAbonnementDivers(p);
                    }
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseAchat(YvsComptaCaissePieceAchat y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CAISSE_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                try {
                    String query = "DELETE FROM yvs_compta_content_journal "
                            + "WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_ACHAT, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_achat j "
                            + "WHERE j.piece=p.id AND j.reglement=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            boolean comptabilise = dao.isComptabilise(o.getVente().getId(), Constantes.SCR_CAISSE_VENTE);
                            if (!comptabilise) {
                                continue;
                            }
                            unComptabiliserCaisseVente(o.getVente());
                        }
                    }
                } catch (Exception ex) {
                    getException(ex.getMessage(), ex);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, Date dateExtourne, boolean controle, boolean extourne) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceVente().getPhasesReglement().indexOf(y);
            YvsComptaPhasePiece pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceVente().getPhasesReglement().size()) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceVente().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? (!pSvt.equals(y) && !extourne) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterneOrder";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            if (!extourne) {
                result = canDeletePieceComptable(list);
                if (result != null ? result.isResult() : false) {
                    String query = "DELETE FROM yvs_compta_content_journal "
                            + "WHERE ref_externe=? AND table_externe=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_VENTE, 2)});
                    query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_vente j "
                            + "WHERE j.piece=p.id AND j.etape=?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    y.setPieceContenu(null);
                }
            } else {
                if (list != null ? !list.isEmpty() : false) {
                    YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) dao.loadOneByNameQueries("YvsComptaPiecesComptable.findById", new String[]{"id"}, new Object[]{list.get(0).getId()});
                    extournePiece(p, dateExtourne);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, Date dateExtourne, boolean controle) {
        return unComptabiliserPhaseCaisseVente(y, dateExtourne, controle, false);
    }

    public ResultatAction unComptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y, boolean controle) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceAchat().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceAchat pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceAchat().getPhasesReglement().size()) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceAchat().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_achat j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers y, boolean controle) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite") && controle) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceDivers().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceDivers pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceDivers().getPhasesReglement().size()) {
                pSvt = y.getPieceDivers().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceDivers().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceDivers().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CAISSE_DIVERS, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_piece_divers j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseVirement(YvsComptaPhasePieceVirement y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_cheq_reg_vente_annul_comptabilite")) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getVirement().getPhasesReglement().indexOf(y);
            YvsComptaPhasePieceVirement pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getVirement().getPhasesReglement().size()) {
                pSvt = y.getVirement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getVirement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getVirement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_VIREMENT)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_VIREMENT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_etape_piece_virement p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.etape = ?)";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceAchat().getPhasesReglement().indexOf(y);
            YvsComptaPhaseAcompteAchat pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceAchat().getPhasesReglement().size()) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceAchat().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceAchat().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_ACOMPTE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_acompte_achat j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getPieceVente().getPhasesReglement().indexOf(y);
            YvsComptaPhaseAcompteVente pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getPieceVente().getPhasesReglement().size()) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getPieceVente().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getPieceVente().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_ACOMPTE_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_acompte_vente j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getReglement().getPhasesReglement().indexOf(y);
            YvsComptaPhaseReglementCreditFournisseur pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getReglement().getPhasesReglement().size()) {
                pSvt = y.getReglement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getReglement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getReglement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_reglement_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserPhaseCaisseCreditVente(YvsComptaPhaseReglementCreditClient y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            //l'étape suivante ne doit pas être validé
            int idx = y.getReglement().getPhasesReglement().indexOf(y);
            YvsComptaPhaseReglementCreditClient pSvt = null;
            if (idx >= 0 && (idx + 1) < y.getReglement().getPhasesReglement().size()) {
                pSvt = y.getReglement().getPhasesReglement().get(idx + 1);
            } else if (idx == (y.getReglement().getPhasesReglement().size() - 1) || idx == 0) {
                pSvt = y.getReglement().getPhasesReglement().get(idx);
            }
            if (pSvt != null ? !pSvt.equals(y) : false) {
                if (dao.isComptabilise(pSvt.getId(), Constantes.SCR_PHASE_CREDIT_VENTE)) {
                    return new ResultatAction().fail("Vous ne pouvez annuler cette étape car la suivante est encore comptabilisée !");
                }
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_PHASE_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_etape_reglement_credit_client j "
                        + "WHERE j.piece=p.id AND j.etape=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            //Vérifié avant tout que la période n'est pas clôturé            
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            if (y.getVerouille()) {
                return result.documentVerouille();
            }
            String table_ = y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE;
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), table_};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                try {
//                    String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe = ? AND table_externe = ?";
//                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(table_, 2)});
                    String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c WHERE c.piece=p.id AND ref_externe = ? AND table_externe = ?";
                    dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(table_, 2)});
                    y.setPieceContenu(null);

                    y.setComptabilised(false);
                    y.setComptabilise(false);
                    //Comptabilisation des compensation
                    if (y.getCompensations() != null ? !y.getCompensations().isEmpty() : false) {
                        for (YvsComptaCaissePieceCompensation o : y.getCompensations()) {
                            boolean comptabilise = dao.isComptabilise(o.getAchat().getId(), Constantes.SCR_CAISSE_ACHAT);
                            if (!comptabilise) {
                                continue;
                            }
                            unComptabiliserCaisseAchat(o.getAchat());
                        }
                    }
                } catch (Exception ex) {
                    getException(ex.getMessage(), ex);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_reglement_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_reglement_credit_client j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseDivers(YvsComptaCaissePieceDivers y) {
        return unComptabiliserCaisseDivers(y, true);
    }

    public ResultatAction unComptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, boolean control) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (control) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    return result.notAcces();
                }
                String[] champ = new String[]{"id", "table"};
                Object[] val = new Object[]{y.getId(), Constantes.SCR_CAISSE_DIVERS};
                String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
                List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
                result = canDeletePieceComptable(list);
            }
            if (control ? (result != null ? result.isResult() : false) : true) {
                try {
                    if (y.getDocDivers().getIdTiers() != null ? y.getDocDivers().getIdTiers() < 1 : true) {
                        String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c INNER JOIN yvs_compta_caisse_piece_divers y ON c.ref_externe = y.id AND c.table_externe = ? WHERE c.piece = p.id AND y.doc_divers = ?";
                        dao.requeteLibre(query, new Options[]{new Options(Constantes.SCR_CAISSE_DIVERS, 1), new Options(y.getDocDivers().getId(), 2)});
                    } else {
                        String query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal c WHERE c.piece = p.id AND c.ref_externe = ? AND c.table_externe = ?";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CAISSE_DIVERS, 2)});
                        if (y.getPieceContenu() != null) {
                            dao.delete(y.getPieceContenu());
                        }
                        y.setComptabilised(false);
                        y.setComptabilise(false);
                        y.setPieceContenu(null);
                    }
                } catch (Exception ex) {
                    getException(ex.getMessage(), ex);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_ABONNEMENT_DIVERS};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ABONNEMENT_DIVERS, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_abonnement_divers j "
                        + "WHERE j.piece=p.id AND j.abonnement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
//                String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
//                        + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_abonnement_divers p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.abonnement = ?)";
//                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
//                if (y.getPieceContenu() != null) {
//                    dao.delete(y.getPieceContenu());
//                }
//                if (y.isUnComptabiliseAll() && (y.getPieceContenu() != null ? y.getPieceContenu().getPiece() != null : false)) {
//                    dao.delete(y.getPieceContenu().getPiece());
//                }
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserAcompteClient(YvsComptaAcompteClient y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_ACOMPTE_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACOMPTE_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_acompte_client j "
                        + "WHERE j.piece=p.id AND j.acompte=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);

                List<YvsComptaCaissePieceVente> pieces_vente = dao.loadNameQueries("YvsComptaNotifReglementVente.findPieceComptabiliseByAcompte", new String[]{"acompte"}, new Object[]{y});
                for (YvsComptaCaissePieceVente r : pieces_vente) {
                    unComptabiliserCaisseVente(r);
                }

                List<YvsComptaCaissePieceDivers> pieces_divers = dao.loadNameQueries("YvsComptaNotifReglementDocDivers.findPieceComptabiliseByAcompteVente", new String[]{"acompte"}, new Object[]{y});
                for (YvsComptaCaissePieceDivers r : pieces_divers) {
                    unComptabiliserCaisseDivers(r);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserAcompteFournisseur(YvsComptaAcompteFournisseur y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_ACOMPTE_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_ACOMPTE_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_acompte_fournisseur j "
                        + "WHERE j.piece=p.id AND j.acompte=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);

                List<YvsComptaCaissePieceAchat> pieces_achat = dao.loadNameQueries("YvsComptaNotifReglementAchat.findPieceComptabiliseByAcompte", new String[]{"acompte"}, new Object[]{y});
                for (YvsComptaCaissePieceAchat r : pieces_achat) {
                    unComptabiliserCaisseAchat(r);
                }

                List<YvsComptaCaissePieceDivers> pieces_divers = dao.loadNameQueries("YvsComptaNotifReglementDocDivers.findPieceComptabiliseByAcompteAchat", new String[]{"acompte"}, new Object[]{y});
                for (YvsComptaCaissePieceDivers r : pieces_divers) {
                    unComptabiliserCaisseDivers(r);
                }
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCreditFournisseur(YvsComptaCreditFournisseur y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CREDIT_ACHAT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CREDIT_ACHAT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_credit_fournisseur j "
                        + "WHERE j.piece=p.id AND j.credit=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCreditClient(YvsComptaCreditClient y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_CREDIT_VENTE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_CREDIT_VENTE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_credit_client j "
                        + "WHERE j.piece=p.id AND j.credit=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseVirement(YvsComptaCaissePieceVirement y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_VIREMENT};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal "
                        + "WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_VIREMENT, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_piece_virement j "
                        + "WHERE j.piece=p.id AND j.reglement=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilised(false);
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserCaisseMission(YvsComptaCaissePieceMission y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_FRAIS_MISSIONS};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                y.setComptabilised(false);
                y.setComptabilise(false);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserBulletin(YvsGrhBulletins y) {
        ResultatAction result = new ResultatAction();
        try {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    return result.notAcces();
                }
                String[] champ = new String[]{"id", "table"};
                Object[] val = new Object[]{y.getId(), Constantes.SCR_BULLETIN};
                String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
                List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
                result = canDeletePieceComptable(list);
                if (result != null ? result.isResult() : false) {
                    if (list != null ? list.isEmpty() : true) {
                        String query = "SELECT DISTINCT c.table_externe FROM yvs_compta_content_journal c INNER JOIN yvs_compta_content_journal_bulletin p ON p.piece = c.piece WHERE p.bulletin = ?";
                        List<String> tables = dao.loadListBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
                        for (String table : tables) {
                            if (table.equals(Constantes.SCR_SALAIRE)) {
                                return new ResultatAction().fail("Ce bulletin est comptabilisée à partir de son ordre de salaire");
                            }
                        }
                    }
                    try {
                        //Supprimme tous les contenu en rapport avec la facture
                        String query = "DELETE FROM yvs_compta_pieces_comptable WHERE id IN "
                                + "(SELECT DISTINCT p.piece FROM yvs_compta_content_journal_bulletin p LEFT JOIN yvs_compta_content_journal c ON p.piece = c.piece WHERE c.id IS NULL AND p.bulletin = ?)";
                        dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                        y.setComptabilised(false);
                        y.setComptabilise(false);
                    } catch (Exception ex) {
                        getException(ex.getMessage(), ex);
                    }
                }
                return result;
            }
        } catch (Exception ex) {
            getException("ManagedSaisiePiece (unComptabiliserVente)", ex);
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction unComptabiliserRetenue(YvsGrhElementAdditionel y) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                return result.notAcces();
            }
            String[] champ = new String[]{"id", "table"};
            Object[] val = new Object[]{y.getId(), Constantes.SCR_RETENUE};
            String nameQueri = "YvsComptaContentJournal.findPieceByExterne";
            List<YvsComptaPiecesComptable> list = dao.loadNameQueries(nameQueri, champ, val);
            result = canDeletePieceComptable(list);
            if (result != null ? result.isResult() : false) {
                String query = "DELETE FROM yvs_compta_content_journal WHERE ref_externe=? AND table_externe=? ";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1), new Options(Constantes.SCR_RETENUE, 2)});
                query = "DELETE FROM yvs_compta_pieces_comptable p USING yvs_compta_content_journal_retenue_salaire j WHERE j.piece=p.id AND j.retenue=?";
                dao.requeteLibre(query, new Options[]{new Options(y.getId(), 1)});
                y.setComptabilise(false);
                y.setPieceContenu(null);
            }
            return result;
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserSalaire(YvsGrhOrdreCalculSalaire y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!y.getCloture()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_SALAIRE);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaSalaire(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                return result.documentVerouille();
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseVente(YvsComptaPhasePiece y) {
        if (y != null) {
            return comptabiliserPhaseCaisseVente(y, buildEtapeCaisseVenteToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseVente(YvsComptaPhasePiece y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_VENTE);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseVente(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                return result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Phase non comptabilisé !");
    }

    public ResultatAction comptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y) {
        if (y != null) {
            return comptabiliserPhaseCaisseAchat(y, buildEtapeCaisseAchatToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseAchat(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers y) {
        if (y != null) {
            return comptabiliserPhaseCaisseDivers(y, buildEtapeCaisseDiversToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseDivers(YvsComptaPhasePieceDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_CAISSE_DIVERS);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseDivers(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseCaisseVirement(YvsComptaPhasePieceVirement y) {
        if (y != null) {
            return comptabiliserPhaseCaisseVirement(y, buildEtapeCaisseVirementToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseVirement(YvsComptaPhasePieceVirement y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_VIREMENT);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseVirement(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat y) {
        if (y != null) {
            return comptabiliserPhaseAcompteAchat(y, buildEtapeAcompteAchatToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeAcompteAchat(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente y) {
        if (y != null) {
            return comptabiliserPhaseAcompteVente(y, buildEtapeAcompteVenteToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeAcompteVente(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseCaisseCreditVente(YvsComptaPhaseReglementCreditClient y) {
        if (y != null) {
            return comptabiliserPhaseCaisseCreditVente(y, buildEtapeCaisseCreditVenteToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseCreditVente(YvsComptaPhaseReglementCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseCreditVente(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserPhaseCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y) {
        if (y != null) {
            return comptabiliserPhaseCaisseCreditAchat(y, buildEtapeCaisseCreditAchatToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserPhaseCaisseCreditAchat(YvsComptaPhaseReglementCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getPhaseOk()) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaEtapeCaisseCreditAchat(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseVente(YvsComptaCaissePieceVente y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhasePiece a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseVente(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseVente(y, buildCaisseVenteToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseVente(YvsComptaCaissePieceVente y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatCompta();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), y.getVente().getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_CAISSE_AVOIR_VENTE : Constantes.SCR_CAISSE_VENTE);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseVente(y, contenus);
                    YvsComptaPiecesComptable p = result != null ? result.isResult() ? (YvsComptaPiecesComptable) result.getData() : null : null;
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhasePieceAchat a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseAchat(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseAchat(y, buildCaisseAchatToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseAchat(YvsComptaCaissePieceAchat y, List<YvsComptaContentJournal> contenus) {
        if (contenus != null ? !contenus.isEmpty() : false) {
            ResultatAction result = new ResultatCompta();
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseAchat(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseVirement(YvsComptaCaissePieceVirement y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhasePieceVirement a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseVirement(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseVirement(y, buildCaisseVirementToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseVirement(YvsComptaCaissePieceVirement y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) || y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) {
                List<Long> ids = new ArrayList<>();
                ids.add(y.getId());
                boolean comptabilise = dao.isComptabilise(ids, Constantes.SCR_VIREMENT, true, (y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS) ? Constantes.MOUV_CAISS_SORTIE.charAt(0) : Constantes.MOUV_CAISS_ENTREE.charAt(0)));
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaCaisseVirement(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                result.documentNotValide();
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhasePieceDivers a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseDivers(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseDivers(y, buildCaisseDiversToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseDivers(YvsComptaCaissePieceDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_DIVERS);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseDivers(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseMission(YvsComptaCaissePieceMission y) {
        if (y != null) {
            return comptabiliserCaisseMission(y, buildCaisseMissionToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseMission(YvsComptaCaissePieceMission y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_FRAIS_MISSIONS);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseMission(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y) {
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y.getDocDivers()});
        return comptabiliserAbonnementDivers(y, fonction.buildDiversToComptabilise(y, secs, taxs, couts, dao));
    }

    public ResultatAction comptabiliserAbonnementDivers(YvsComptaAbonementDocDivers y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getDocDivers().getId(), Constantes.SCR_DIVERS);
                    if (!comptabilise) {
                        return result.alreadyComptabilise();
                    }
                    comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_ABONNEMENT_DIVERS);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    System.err.println("contenus : " + contenus);
                    result = majComptaAbonnementDivers(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserAcompteAchat(YvsComptaAcompteFournisseur y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhaseAcompteAchat a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseAcompteAchat(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserAcompteAchat(y, buildAcompteFournisseurToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserAcompteAchat(YvsComptaAcompteFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaAcompteFournisseur(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserAcompteVente(YvsComptaAcompteClient y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhaseAcompteVente a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseAcompteVente(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserAcompteVente(y, buildAcompteClientToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserAcompteVente(YvsComptaAcompteClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaAcompteClient(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCreditVente(YvsComptaCreditClient y) {
        if (y != null) {
            return comptabiliserCreditVente(y, buildCreditClientToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCreditVente(YvsComptaCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCreditClient(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCreditAchat(YvsComptaCreditFournisseur y) {
        if (y != null) {
            return comptabiliserCreditAchat(y, buildCreditFournisseurToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCreditAchat(YvsComptaCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCreditFournisseur(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhaseReglementCreditClient a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseCreditVente(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseCreditVente(y, buildCaisseCreditVenteToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseCreditVente(YvsComptaReglementCreditClient y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseCreditVente(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y) {
        if (y != null) {
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                ResultatAction correct = new ResultatAction().fail("Erreur...");
                for (YvsComptaPhaseReglementCreditFournisseur a : y.getPhasesReglement()) {
                    if (Objects.equals(a.getStatut(), Constantes.STATUT_DOC_VALIDE)) {
                        correct = comptabiliserPhaseCaisseCreditAchat(a);
                    }
                }
                return correct;
            } else {
                return comptabiliserCaisseCreditAchat(y, buildCaisseCreditAchatToComptabilise(y).getListContent());
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserCaisseCreditAchat(YvsComptaReglementCreditFournisseur y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaCaisseCreditAchat(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...!");
    }

    public ResultatAction comptabiliserHeaderVente(YvsComEnteteDocVente y) {
        if (y != null) {
            return comptabiliserHeaderVente(y, buildHeaderVenteToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserHeaderVente(YvsComEnteteDocVente y, List<YvsComptaContentJournal> contenus) {
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            return new ResultatAction().fail("Désactivé");
        }
        ResultatAction result = new ResultatAction();
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            if (!y.getEtat().equals(Constantes.ETAT_EDITABLE)) {
                boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_HEAD_VENTE);
                if (comptabilise) {
                    //Met à jour le champ comptabilisé
                    if (!y.getComptabilise()) {
                        y.setComptabilise(true);
                        dao.update(y);
                    }
                    return result.alreadyComptabilise();
                }
                result = majComptaHeaderVente(y, contenus);
                YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                if (result != null) {
                    result.setResult(reponse);
                }
                y.setComptabilised(reponse);
                y.setComptabilise(reponse);
                return result;
            } else {
                return new ResultatAction().fail("Comptabilisation impossible... car ce journal de vente est editable");
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserVente(YvsComDocVentes y) {
        if (y != null) {
            return comptabiliserVente(y, buildVenteToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Document non comptabilisé !");
    }

    public ResultatAction comptabiliserVente(YvsComDocVentes y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (!y.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                    if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        boolean comptabilise = dao.isComptabilise(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAV) ? Constantes.SCR_AVOIR_VENTE : Constantes.SCR_VENTE);
                        if (comptabilise) {
                            //Met à jour le champ comptabilisé
                            if (!y.getComptabilise()) {
                                y.setComptabilise(true);
                                dao.update(y);
                            }
                            return result.alreadyComptabilise();
                        }
                        result = majComptaVente(y, contenus);
                        YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                        boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                        if (result != null) {
                            result.setResult(reponse);
                        }
                        y.setComptabilised(reponse);
                        y.setComptabilise(reponse);
                        return result;
                    } else {
                        return result.documentNotValide();
                    }
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserAchat(YvsComDocAchats y) {
        if (y != null) {
            return comptabiliserAchat(y, buildAchatToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserAchat(YvsComDocAchats y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (!y.getTypeDoc().equals(Constantes.TYPE_BCA)) {
                    if (y.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        boolean comptabilise = dao.isComptabilise(y.getId(), y.getTypeDoc().equals(Constantes.TYPE_FAA) ? Constantes.SCR_AVOIR_ACHAT : Constantes.SCR_ACHAT);
                        if (comptabilise) {
                            //Met à jour le champ comptabilisé
                            if (!y.getComptabilise()) {
                                y.setComptabilise(true);
                                dao.update(y);
                            }
                            return result.alreadyComptabilise();
                        }
                        result = majComptaAchat(y, contenus);
                        YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                        boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                        if (result != null) {
                            result.setResult(reponse);
                        }
                        y.setComptabilised(reponse);
                        y.setComptabilise(reponse);
                        return result;
                    } else {
                        return new ResultatAction().fail("Comptabilisation impossible... car cette facture n'est pas validée");
                    }
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserDivers(YvsComptaCaisseDocDivers y) {
        ResultatAction result = new ResultatAction();
        List<YvsComptaAbonementDocDivers> abonnements = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> sections = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxes = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        if (y.getCompteGeneral() != null ? y.getCompteGeneral().getSaisieAnalytique() && sections.isEmpty() : false) {
            return new ResultatAction().fail("Vous devez effectuer la répartition analytique !");
        }
        List<YvsComptaContentJournal> contenus = fonction.buildDiversToComptabilise(y.getId(), abonnements, sections, taxes, couts, dao);
        if (contenus != null ? contenus.isEmpty() : true) {
            return result.fail(dao.getRESULT());
        }
        return comptabiliserDivers(y, contenus, abonnements, sections, taxes, couts);
    }

    public ResultatAction comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus) {
        List<YvsComptaAbonementDocDivers> abs = dao.loadNameQueries("YvsComptaAbonementDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCentreDocDivers> secs = dao.loadNameQueries("YvsComptaCentreDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaTaxeDocDivers> taxs = dao.loadNameQueries("YvsComptaTaxeDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        List<YvsComptaCoutSupDocDivers> couts = dao.loadNameQueries("YvsComptaCoutSupDocDivers.findByDocDivers", new String[]{"docDivers"}, new Object[]{y});
        return comptabiliserDivers(y, contenus, abs, secs, taxs, couts);
    }

    public ResultatAction comptabiliserDivers(YvsComptaCaisseDocDivers y, List<YvsComptaContentJournal> contenus, List<YvsComptaAbonementDocDivers> abs, List<YvsComptaCentreDocDivers> secs, List<YvsComptaTaxeDocDivers> taxs, List<YvsComptaCoutSupDocDivers> couts) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_DIVERS);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaDivers(y, contenus, abs, secs, taxs, couts);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible... car cette opération n'est pas validée");
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserBulletin(YvsGrhBulletins y) {
        if (y != null) {
            return comptabiliserBulletin(y, buildBulletinToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserBulletin(YvsGrhBulletins y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_BULLETIN);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaBulletin(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilised(reponse);
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return result.documentNotValide();
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserRetenue(YvsGrhElementAdditionel y) {
        if (y != null) {
            return comptabiliserRetenue(y, buildRetenueToComptabilise(y).getListContent());
        }
        return new ResultatAction().fail("Erreur...");
    }

    public ResultatAction comptabiliserRetenue(YvsGrhElementAdditionel y, List<YvsComptaContentJournal> contenus) {
        ResultatAction result = new ResultatAction();
        if (contenus != null ? !contenus.isEmpty() : false) {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                if (y.getDocDivers() != null ? y.getDocDivers().getId() < 1 : true) {
                    boolean comptabilise = dao.isComptabilise(y.getId(), Constantes.SCR_RETENUE);
                    if (comptabilise) {
                        //Met à jour le champ comptabilisé
                        if (!y.getComptabilise()) {
                            y.setComptabilise(true);
                            dao.update(y);
                        }
                        return result.alreadyComptabilise();
                    }
                    result = majComptaRetenue(y, contenus);
                    YvsComptaPiecesComptable p = ((YvsComptaPiecesComptable) (result != null ? result.isResult() ? result.getData() : null : null));
                    boolean reponse = (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false);
                    if (result != null) {
                        result.setResult(reponse);
                    }
                    y.setComptabilise(reponse);
                    return result;
                } else {
                    return new ResultatAction().fail("Comptabilisation impossible... car cette retenue est associée à une operation diverse (Allez Comptabiliser l'OD)");
                }
            }
        }
        return new ResultatAction().fail("Erreur...");
    }
}

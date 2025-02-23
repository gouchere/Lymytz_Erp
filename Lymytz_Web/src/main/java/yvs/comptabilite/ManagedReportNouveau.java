/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.ManagedJournaux;
import yvs.base.compta.UtilCompta;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.saisie.YvsComptaReportCompte;
import yvs.entity.param.YvsAgences;
import yvs.etats.ValeurComptable;
import yvs.mutuelle.Exercice;
import yvs.mutuelle.ManagedExercice;
import yvs.mutuelle.UtilMut;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.ManagedAgence;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedReportNouveau extends Managed<PiecesCompta, YvsComptaPiecesComptable> implements Serializable {

    private String libelle;
    private Agence agence = new Agence();
    private Exercice exercice = new Exercice();
    private Journaux journal = new Journaux();
    private Comptes compteGain = new Comptes();
    private Comptes comptePerte = new Comptes();
    private List<YvsBasePlanComptable> selectComptes;
    private List<ValeurComptable> contenus;
    private boolean compteBenefice = true;

    private YvsComptaParametre currentParam;

    public ManagedReportNouveau() {
        selectComptes = new ArrayList<>();
        contenus = new ArrayList<>();
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isCompteBenefice() {
        return compteBenefice;
    }

    public void setCompteBenefice(boolean compteBenefice) {
        this.compteBenefice = compteBenefice;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public Comptes getCompteGain() {
        return compteGain;
    }

    public void setCompteGain(Comptes compteGain) {
        this.compteGain = compteGain;
    }

    public Comptes getComptePerte() {
        return comptePerte;
    }

    public void setComptePerte(Comptes comptePerte) {
        this.comptePerte = comptePerte;
    }

    public List<YvsBasePlanComptable> getSelectComptes() {
        return selectComptes;
    }

    public void setSelectComptes(List<YvsBasePlanComptable> selectComptes) {
        this.selectComptes = selectComptes;
    }

    public List<ValeurComptable> getContenus() {
        return contenus;
    }

    public void setContenus(List<ValeurComptable> contenus) {
        this.contenus = contenus;
    }

    public YvsComptaParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComptaParametre currentParam) {
        this.currentParam = currentParam;
    }

    @Override
    public void loadAll() {
        if (currentParam != null ? currentParam.getId() < 1 : true) {
            currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParam != null ? currentParam.getId() > 0 : false) {
            if (compteGain != null ? compteGain.getId() < 1 : true) {
                compteGain = UtilCompta.buildSimpleBeanCompte(currentParam.getCompteBeneficeReport());
            }
            if (comptePerte != null ? comptePerte.getId() < 1 : true) {
                comptePerte = UtilCompta.buildSimpleBeanCompte(currentParam.getComptePerteReport());
            }
            if (journal != null ? journal.getId() < 1 : true) {
                journal = UtilCompta.buildBeanJournaux(currentParam.getJournalReport());
            }
        }
    }

    @Override
    public boolean controleFiche(PiecesCompta bean) {
        if (!Util.asString(libelle)) {
            getErrorMessage("Vous devez indiquer le libelle");
            return false;
        }
        if (compteGain != null ? compteGain.getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le compte de gain");
            return false;
        }
        if (comptePerte != null ? comptePerte.getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le compte de perte");
            return false;
        }
        if (journal != null ? journal.getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le journal de report");
            return false;
        }
        if (exercice != null ? exercice.getId() < 1 : true) {
            getErrorMessage("Vous devez preciser l'exercice à reporter");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        contenus.clear();
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(null)) {
                ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
                YvsBaseExercice next = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findNext", new String[]{"societe", "dateFin"}, new Object[]{currentAgence.getSociete(), exercice.getDateFin()});
                if (next != null ? next.getId() < 1 : true) {
                    getErrorMessage("Il n'existe pas de prochain exercice");
                    return false;
                }
                String numero = genererReference(Constantes.TYPE_PIECE_COMPTABLE_NAME, next.getDateDebut(), journal.getId(), Constantes.JOURNAL);
                if (numero == null || numero.trim().length() < 1) {
                    return false;
                }
                List<YvsBasePlanComptable> comptes;
                if (selectComptes != null ? !selectComptes.isEmpty() : false) {
                    comptes = new ArrayList<>(selectComptes);
                } else {
//                    comptes = dao.loadNameQueries("YvsBasePlanComptable.findWithReport", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                    comptes = dao.loadNameQueries("YvsComptaContentJournal.findCompteWithReport", new String[]{"societe", "dateFin"}, new Object[]{currentAgence.getSociete(), exercice.getDateFin()});
                }
                YvsComptaPiecesComptable piece = new YvsComptaPiecesComptable();
                piece.setAuthor(currentUser);
                piece.setDatePiece(next.getDateDebut());
                piece.setDateSaisie(new Date());
                piece.setExercice(next);
                piece.setJournal(new YvsComptaJournaux(journal.getId()));
                piece.setNumPiece(numero);
                piece.setStatutPiece(Constantes.STATUT_DOC_EDITABLE);
                piece = (YvsComptaPiecesComptable) dao.save1(piece);

                Calendar time = Calendar.getInstance();
                time.setTime(next.getDateDebut());
                contenus.clear();
                ValeurComptable valeur;
                YvsComptaContentJournal content;
                YvsComptaReportCompte report;
                Double credit = 0D, debit = 0D;
                int i = 1;
                for (YvsBasePlanComptable y : comptes) {
                    if (agence != null ? agence.getId() > 0 : false) {
                        report = (YvsComptaReportCompte) dao.loadOneByNameQueries("YvsComptaReportCompte.findOne", new String[]{"agence", "exercice", "compte"}, new Object[]{new YvsAgences(agence.getId()), new YvsBaseExercice(exercice.getId()), y});
                    } else {
                        report = (YvsComptaReportCompte) dao.loadOneByNameQueries("YvsComptaReportCompte.findOneBySociete", new String[]{"societe", "exercice", "compte"}, new Object[]{currentAgence.getSociete(), new YvsBaseExercice(exercice.getId()), y});
                    }
                    if (report != null ? report.getId() > 0 : false) {
                        continue;
                    }
                    if (selectComptes != null ? !selectComptes.isEmpty() : false) {
                        String query = "SELECT COUNT(y.id) FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id WHERE y.compte_general = ? AND a.societe = ? AND p.date_piece <= ?";
                        List<Options> options = new ArrayList<>();
                        options.add(new Options(y.getId(), 1));
                        options.add(new Options(currentAgence.getSociete().getId(), 2));
                        options.add(new Options(exercice.getDateFin(), 3));
                        if (agence != null ? agence.getId() > 0 : false) {
                            query += " AND a.id = ?";
                            options.add(new Options(agence.getId(), 4));
                        }
                        Long count = (Long) dao.loadObjectBySqlQuery(query, options.toArray(new Options[options.size()]));
                        if (count != null ? count < 1 : true) {
                            continue;
                        }
                    }
                    valeur = new ValeurComptable(contenus.size() + 1, y.getNumCompte(), y.getIntitule());
                    if (y.getTypeReport().equals("SOLDE")) {
                        Options[] params = new Options[]{new Options(agence.getId(), 1), new Options(currentAgence.getSociete().getId(), 2), new Options(y.getId(), 3), new Options(exercice.getDateFin(), 4)};
                        Double solde = dao.callFonction("SELECT compta_et_solde(?, ?, ?, null::date, ?, 0, 'C', false, '')", params);
                        if (solde != 0) {
                            content = new YvsComptaContentJournal();
                            content.setAuthor(currentUser);
                            content.setCompteGeneral(y);
                            content.setCredit(solde < 0 ? -solde : 0);
                            content.setDebit(solde > 0 ? solde : 0);
                            content.setEcheance(next.getDateDebut());
                            content.setJour(time.get(Calendar.DATE));
                            content.setLibelle(libelle);
                            content.setNumPiece(numero);
                            content.setPiece(piece);
                            content.setNumRef(numero + "-" + (i > 9 ? i++ : "0" + i++));
                            content.setReport(true);
                            content.setStatut(Constantes.STATUT_DOC_EDITABLE);
                            content = (YvsComptaContentJournal) dao.save1(content);
                            valeur.getContents().add(content);
                        }
                        credit += (solde < 0 ? -solde : 0);
                        debit += (solde > 0 ? solde : 0);
                    } else if (y.getTypeReport().equals("DETAIL")) {
                        List<YvsComptaContentJournal> list;
                        if (agence != null ? agence.getId() > 0 : false) {
                            list = dao.loadNameQueries("YvsComptaContentJournal.findNoLetterByCompteGeneralAgence", new String[]{"exercice", "compte", "agence"}, new Object[]{new YvsBaseExercice(exercice.getId()), y, new YvsAgences(agence.getId())});
                        } else {
                            list = dao.loadNameQueries("YvsComptaContentJournal.findNoLetterByCompteGeneral", new String[]{"exercice", "compte"}, new Object[]{new YvsBaseExercice(exercice.getId()), y});
                        }
                        for (YvsComptaContentJournal c : list) {
                            content = new YvsComptaContentJournal();
                            content.setAuthor(currentUser);
                            content.setCompteGeneral(y);
                            content.setCredit(c.getCredit());
                            content.setDebit(c.getDebit());
                            content.setEcheance(next.getDateDebut());
                            content.setJour(time.get(Calendar.DATE));
                            content.setLibelle(c.getLibelle());
                            content.setNumPiece(c.getNumPiece());
                            content.setNumRef(c.getNumRef());
                            content.setNumero(c.getNumero());
                            content.setCompteTiers(c.getCompteTiers());
                            content.setRefExterne(c.getRefExterne());
                            content.setTableExterne(c.getTableExterne());
                            content.setTableTiers(c.getTableTiers());
                            content.setPiece(piece);
                            content.setReport(true);
                            content.setStatut(Constantes.STATUT_DOC_EDITABLE);
                            content = (YvsComptaContentJournal) dao.save1(content);
                            valeur.getContents().add(content);

                            credit += c.getCredit();
                            debit += c.getDebit();
                        }
                    }
                    if (!valeur.getContents().isEmpty()) {
                        if (agence != null ? agence.getId() > 0 : false) {
                            report = new YvsComptaReportCompte(new YvsAgences(agence.getId()), new YvsBaseExercice(exercice.getId()), y, currentUser);
                            dao.save(report);
                        } else if (w != null) {
                            for (YvsAgences a : w.getListAgence()) {
                                report = new YvsComptaReportCompte(a, new YvsBaseExercice(exercice.getId()), y, currentUser);
                                dao.save(report);
                            }
                        }
                        contenus.add(valeur);
                    }
                }
                Double solde = (debit != null ? debit : 0) - (credit != null ? credit : 0);
                if (solde != null ? solde != 0 : false) {
                    valeur = new ValeurComptable();
                    if (solde > 0) {
                        valeur = new ValeurComptable(contenus.size() + 1, compteGain.getNumCompte(), compteGain.getIntitule());
                    } else if (solde < 0) {
                        valeur = new ValeurComptable(contenus.size() + 1, comptePerte.getNumCompte(), comptePerte.getIntitule());
                    }
                    content = new YvsComptaContentJournal();
                    content.setAuthor(currentUser);
                    content.setEcheance(next.getDateDebut());
                    content.setJour(time.get(Calendar.DATE));
                    content.setLibelle(libelle);
                    content.setNumPiece(numero);
                    content.setNumRef(numero + "-" + (i > 9 ? i++ : "0" + i++));
                    content.setReport(true);
                    content.setStatut(Constantes.STATUT_DOC_EDITABLE);
                    content.setPiece(piece);
                    if (solde > 0) {
                        content.setCredit(solde);
                        content.setCompteGeneral(new YvsBasePlanComptable(compteGain.getId()));
                    } else if (solde < 0) {
                        content.setDebit(-solde);
                        content.setCompteGeneral(new YvsBasePlanComptable(comptePerte.getId()));
                    }
                    content = (YvsComptaContentJournal) dao.save1(content);
                    valeur.getContents().add(content);
                    contenus.add(valeur);
                }
                if (contenus.isEmpty()) {
                    dao.delete(piece);
                }
                return true;
            }
        } catch (Exception ex) {
            getException("saveNew", ex);
        }
        return false;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBasePlanComptable y = (YvsBasePlanComptable) ev.getObject();
            if (compteBenefice) {
                compteGain = UtilCompta.buildBeanCompte(y);
                update("blog-comptes_benefice");
            } else {
                comptePerte = UtilCompta.buildBeanCompte(y);
                update("blog-comptes_perte");
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseExercice() {
        if (exercice != null ? exercice.getId() > 0 : false) {
            ManagedExercice w = (ManagedExercice) giveManagedBean(ManagedExercice.class);
            if (w != null) {
                int index = w.getExercices().indexOf(new YvsBaseExercice(exercice.getId()));
                if (index > -1) {
                    exercice = UtilMut.buildBeanExercice(w.getExercices().get(index));
                }
            }
        }
    }

    public void chooseAgence() {
        ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
        if (w != null ? autoriser("journaux_view_all") : false) {
            if (agence != null ? agence.getId() > 0 : false) {
                w.loadAllActifByAgence(agence.getId());
            } else {
                w.loadAllActif();
            }
        }
    }

    private void addParamNoIdsCompte(ManagedCompte w) {
        String value = null;
        for (YvsBasePlanComptable c : selectComptes) {
            if (value == null) {
                value = c.getId().toString();
            } else {
                value += ";" + c.getId().toString();
            }
        }
        w.addParamNoIds(value);
    }

    public void drollable(DragDropEvent ev) {
        YvsBasePlanComptable y = ((YvsBasePlanComptable) ev.getData());
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.getListComptes().remove(y);
            selectComptes.add(0, y);
            addParamNoIdsCompte(w);
        }
    }

    public void drollableAll() {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            for (YvsBasePlanComptable y : w.getListComptes()) {
                selectComptes.add(0, y);
            }
            w.getListComptes().clear();
            addParamNoIdsCompte(w);
        }
    }

    public void removeDrollable(YvsBasePlanComptable y) {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            w.getListComptes().add(0, y);
            selectComptes.remove(y);
            addParamNoIdsCompte(w);
        }
    }

    public void removeAllDrollable() {
        ManagedCompte w = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (w != null) {
            for (YvsBasePlanComptable y : selectComptes) {
                w.getListComptes().add(0, y);
            }
            selectComptes.clear();
            addParamNoIdsCompte(w);
        }
    }

    public void saisieCompte(boolean compteBenefice) {
        this.compteBenefice = compteBenefice;
        String code;
        if (compteBenefice) {
            code = compteGain.getNumCompte();
        } else {
            code = comptePerte.getNumCompte();
        }
        if (Util.asString(code)) {
            //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(code);
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (compteBenefice) {
                        compteGain.setError(false);
                    } else {
                        comptePerte.setError(false);
                    }
                    if (service.getListComptes().size() == 1) {
                        if (compteBenefice) {
                            compteGain = UtilCompta.buildBeanCompte(service.getListComptes().get(0));
                            update("blog-comptes_benefice");
                        } else {
                            comptePerte = UtilCompta.buildBeanCompte(service.getListComptes().get(0));
                            update("blog-comptes_perte");
                        }
                    } else {
                        openDialog("dlgListCompte");
                        update("data-compte_report");
                    }
                } else {
                    if (compteBenefice) {
                        compteGain.setError(true);
                    } else {
                        comptePerte.setError(true);
                    }
                }
            }
        }
    }

}

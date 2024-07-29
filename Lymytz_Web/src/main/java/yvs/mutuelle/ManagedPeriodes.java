/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutPosteEmploye;
import yvs.entity.mutuelle.base.YvsMutPrimePoste;
import yvs.entity.mutuelle.salaire.YvsMutInteret;
import yvs.entity.mutuelle.salaire.YvsMutReglementPrime;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedPeriodes extends Managed<PeriodeExercice, YvsMutPeriodeExercice> {

    private List<YvsMutPeriodeExercice> periodes;
    private YvsMutPeriodeExercice periodeSelect;
    private List<YvsMutReglementPrime> primes;
    private List<YvsMutInteret> interets;

    private YvsMutParametre params;

    private long exerciceSearch;
    private Boolean cloturerSearch, actifSearch;

    private double soldeCaissePeriode;

    public ManagedPeriodes() {
        periodes = new ArrayList<>();
        primes = new ArrayList<>();
        interets = new ArrayList<>();
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsMutParametre getParams() {
        return params;
    }

    public void setParams(YvsMutParametre params) {
        this.params = params;
    }

    public YvsMutPeriodeExercice getPeriodeSelect() {
        return periodeSelect;
    }

    public void setPeriodeSelect(YvsMutPeriodeExercice periodeSelect) {
        this.periodeSelect = periodeSelect;
    }

    public List<YvsMutReglementPrime> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsMutReglementPrime> primes) {
        this.primes = primes;
    }

    public List<YvsMutInteret> getInterets() {
        return interets;
    }

    public void setInterets(List<YvsMutInteret> interets) {
        this.interets = interets;
    }

    public long getExerciceSearch() {
        return exerciceSearch;
    }

    public void setExerciceSearch(long exerciceSearch) {
        this.exerciceSearch = exerciceSearch;
    }

    public Boolean getCloturerSearch() {
        return cloturerSearch;
    }

    public void setCloturerSearch(Boolean cloturerSearch) {
        this.cloturerSearch = cloturerSearch;
    }

    public List<YvsMutPeriodeExercice> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsMutPeriodeExercice> periodes) {
        this.periodes = periodes;
    }

    public double getSoldeCaissePeriode() {
        return soldeCaissePeriode;
    }

    public void setSoldeCaissePeriode(double soldeCaissePeriode) {
        this.soldeCaissePeriode = soldeCaissePeriode;
    }

    @Override
    public void loadAll() {
        params = (YvsMutParametre) dao.loadOneByNameQueries("YvsMutParametre.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        loadAll(true, true);
    }

    public void loadAll(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.exercice.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        periodes = paginator.executeDynamicQuery("YvsMutPeriodeExercice", "y.dateDebut DESC", avancer, init, dao);
    }

    public void paginer(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    @Override
    public void resetFiche() {
        periodeSelect = new YvsMutPeriodeExercice();
        primes.clear();
        interets.clear();
        update("txt_reference_periode");
    }

    public void saveInteret(YvsMutInteret y) {
        try {
            if (y != null) {
                int idx = interets.indexOf(y);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsMutInteret) dao.save1(y);
                } else {
                    dao.update(y);
                }
                interets.set(idx, y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("Error", ex);
        }
    }

    public void savePrime(YvsMutReglementPrime y) {
        try {
            if (y != null) {
                for (YvsMutReglementPrime s : y.getReglements()) {
                    saveSousPrime(y, s, false);
                }
                int idx = primes.indexOf(y);
                if (y.getId() < 0) {
                    y.setId(y.getId() * -10000);
                }
                primes.set(idx, y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("Error", ex);
        }
    }

    public void saveSousPrime(YvsMutReglementPrime parent, YvsMutReglementPrime y) {
        saveSousPrime(parent, y, true);
    }

    public void saveSousPrime(YvsMutReglementPrime parent, YvsMutReglementPrime y, boolean msg) {
        try {
            if (parent != null ? y != null : false) {
                int idx = parent.getReglements().indexOf(y);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsMutReglementPrime) dao.save1(y);
                } else {
                    dao.update(y);
                }
                parent.getReglements().set(idx, y);
                if (msg) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("Error", ex);
        }
    }

    @Override
    public boolean controleFiche(PeriodeExercice bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean saveNew() {
        if (soldeCaissePeriode > 0) {
            saveAllInteret();
            saveAllPrime();
        } else {
            getErrorMessage("Le solde de caisse à repartir est nul");
        }
        return true;
    }

    private List<Long> loadPosteRemunere() {
        List<Long> re;
        String rq = "SELECT DISTINCT p.id FROM yvs_mut_poste_employe pe INNER JOIN yvs_mut_poste p ON pe.poste=p.id "
                + "LEFT OUTER JOIN yvs_mut_prime_poste pp ON p.id=pp.poste "
                + "WHERE pp.id IS NOT null and p.mutuelle=?";
        re = dao.loadListBySqlQuery(rq, new Options[]{new Options(currentMutuel.getId(), 1)});
        return re;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        resetFiche();
        //Calcul le solde de la caisse pour la période        
        if (ev != null ? ev.getObject() != null : false) {
            periodeSelect = (YvsMutPeriodeExercice) ev.getObject();
            soldeCaissePeriode = findSoldeMutuellePeriodeADistribuer(periodeSelect.getId());
            List<YvsMutPosteEmploye> postes = dao.loadNameQueries("YvsMutPosteEmploye.findByOccupationSociete", new String[]{"date", "postes"}, new Object[]{periodeSelect.getDateFin(), loadPosteRemunere()});
            if (postes != null ? !postes.isEmpty() : false) {
                YvsMutReglementPrime y;
                YvsMutReglementPrime s = null;
                for (YvsMutPosteEmploye p : postes) {
                    double montant = 0;
                    y = new YvsMutReglementPrime((long) -(primes.size() + 1));
                    for (YvsMutPrimePoste i : p.getPoste().getPrimes()) {
                        // si le mutualiste a déjà eu une prime se mois
//                        s = (YvsMutReglementPrime) dao.loadOneByNameQueries("YvsMutReglementPrime.findOne", new String[]{"mutualiste", "prime", "periode"}, new Object[]{p.getMutualiste(), i, periodeSelect});
                        if (s == null) {
                            if (i.getMontant() > 0) {
                                s = new YvsMutReglementPrime((long) -(y.getReglements().size() + 1));
                                s.setDatePrime(periodeSelect.getDateDebut());
                                s.setMontant(i.getMontant());
                                s.setMutualiste(p.getMutualiste());
                                s.setPoste(p.getPoste());
                                s.setPrime(i);
                                s.setPeriode(periodeSelect);
                                s.setDateSave(new Date());
                                s.setDateUpdate(new Date());
                            }
                        } else {
                            s.setDateUpdate(new Date());
                            s.setMontant(i.getMontant());
                        }
                        y.getReglements().add(s);
                        montant += i.getMontant();
                    }
                    if (montant > 0) {
                        y.setDatePrime(periodeSelect.getDateDebut());
                        y.setMontant(montant);
                        y.setMutualiste(p.getMutualiste());
                        y.setPoste(p.getPoste());
                        primes.add(y);
                        periodeSelect.setPrimeTotal(periodeSelect.getPrimeTotal() + montant);
                    }
                }
            }

            List<YvsMutMutualiste> mutualistes = dao.loadNameQueries("YvsMutMutualiste.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
            if (mutualistes != null ? !mutualistes.isEmpty() : false) {
                Calendar debut = Calendar.getInstance();
                debut.setTime(periodeSelect.getDateDebut());
//                Calendar fin = Calendar.getInstance();
//                fin.setTime(periodeSelect.getDateDebut());
//                fin.add(Calendar.DATE, 7);
//                if (params != null ? params.getId() > 0 : false) {
//                    debut.set(Calendar.DATE, params.getDebutEpargne());
//                    fin.set(Calendar.DATE, params.getFinEpargne());
//                }
//                champ = new String[]{"mutuelle", "nature", "date", "mouvement"};
//                val = new Object[]{currentMutuel, Constantes.MUT_TYPE_COMPTE_EPARGNE, periodeSelect.getDateFin(), Constantes.MUT_SENS_OP_DEPOT};
//                Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSumDate", champ, val);
//                val = new Object[]{currentMutuel, Constantes.MUT_TYPE_COMPTE_EPARGNE, periodeSelect.getDateFin(), Constantes.MUT_SENS_OP_RETRAIT};
//                Double r = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSumDate", champ, val);
                double caisse = findSoldeByOperationMutuellePeriode(currentMutuel.getId(), Constantes.MUT_TYPE_COMPTE_EPARGNE, periodeSelect.getDateDebut(), periodeSelect.getDateFin(), null);
                periodeSelect.setEpargneTotal(caisse);
                if (caisse > 0) {
                    YvsMutInteret y;
                    for (YvsMutMutualiste e : mutualistes) {
                        y = (YvsMutInteret) dao.loadOneByNameQueries("YvsMutInteret.findOne", new String[]{"mutualiste", "periode"}, new Object[]{e, periodeSelect});
                        if (y != null ? y.getId() > 0 : false) {
                            interets.add(y);
                        } else {
                            double solde = findSoldeMutualisteByTypeCompteAtDate(e.getId(), Constantes.MUT_NATURE_OP_EPARGNE, periodeSelect.getDateFin());
                            if (solde > 0) {
                                e.setMontantTotalEpargne(solde);
                                y = new YvsMutInteret((long) -(interets.size() + 1));
                                y.setDateInteret(debut.getTime());
                                y.setMutualiste(e);
                                y.setPeriode(periodeSelect);
                                y.setTaux((solde / caisse) * 100);
                                y.setMontant(arrondi(y.getTaux() * soldeCaissePeriode / 100));
                                interets.add(y);
                            }
                        }
                    }
                }
            }
            update("txt_reference_periode");
        }
    }

    public void saveAllInteret() {
        YvsMutInteret old;
        champ = new String[]{"mutualiste", "periode"};
        for (YvsMutInteret in : interets) {
            val = new Object[]{in.getMutualiste(), in.getPeriode()};
            old = (YvsMutInteret) dao.loadOneByNameQueries("YvsMutInteret.findByMutualistePeriode", champ, val);
            if (old != null) {
                if (!old.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                    old.setDateUpdate(new Date());
                    old.setTaux(in.getTaux());
                    old.setMontant(in.getMontant());
                    old.setStatutPaiement(Constantes.ETAT_ATTENTE);
                    old.setAuthor(currentUser);
                    old.setCompte((YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findByMutualisteOpInteret", new String[]{"mutualiste"}, new Object[]{in.getMutualiste()}));
                    dao.update(old);
                }
            } else {
                in.setStatutPaiement(Constantes.ETAT_ATTENTE);
                in.setId(null);
                in.setDateSave(new Date());
                in.setDateUpdate(new Date());
                in.setAuthor(currentUser);
                in.setCompte((YvsMutCompte) dao.loadOneByNameQueries("YvsMutCompte.findByMutualisteOpInteret", new String[]{"mutualiste"}, new Object[]{in.getMutualiste()}));
                    in.setId(null);
                in = (YvsMutInteret) dao.save1(in);
            }
        }
        succes();
    }

    public void saveAllPrime() {
        YvsMutReglementPrime old;
        champ = new String[]{"mutualiste", "periode"};
        for (YvsMutReglementPrime rp : primes) {
            old = (YvsMutReglementPrime) dao.loadOneByNameQueries("YvsMutReglementPrime.findOne", new String[]{"mutualiste", "periode"}, new Object[]{rp.getMutualiste(), rp.getPeriode()});
            if (old != null) {
                if (rp.getMontant() > 0) {
                    old.setDateUpdate(new Date());
                    old.setMontant(rp.getMontant());
                    old.setAuthor(currentUser);
                    dao.update(old);
                } else {
                    dao.delete(old);
                }
            } else {
                rp.setStatutPaiement(Constantes.ETAT_ATTENTE);
                rp.setDateSave(new Date());
                rp.setDateUpdate(new Date());
                rp.setId(null);
                rp.setAuthor(currentUser);
                rp = (YvsMutReglementPrime) dao.save1(rp);
            }
        }

    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void addParamExercice() {
        ParametreRequete p = new ParametreRequete("y.exercice", "exercice", null, "=", "AND");
        if (exerciceSearch > 0) {
            p = new ParametreRequete("y.exercice", "exercice", new YvsBaseExercice(exerciceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCloturer() {
        paginator.addParam(new ParametreRequete("y.cloture", "cloture", cloturerSearch, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.salaire.YvsMutInteret;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.UtilMut;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedInteret extends Managed<Interet, YvsMutInteret> implements Serializable {

    private List<YvsMutInteret> interets;
    private List<YvsBaseExercice> exercices;
    private List<YvsMutPeriodeExercice> periodes;
    private List<YvsMutCompte> comptes;
    private CaisseMutuelle caisse = new CaisseMutuelle();
    private String modePaiement = Constantes.MUT_MODE_PAIEMENT_ESPECE;

    private String operateur = "=";
    private String matriculeMut;
    private String statut;
    private long exercice, periode;
    int lineSelect;

    public ManagedInteret() {
        interets = new ArrayList<>();
        exercices = new ArrayList<>();
        periodes = new ArrayList<>();
        comptes = new ArrayList<>();
    }

    public List<YvsMutInteret> getInterets() {
        return interets;
    }

    public void setInterets(List<YvsMutInteret> interets) {
        this.interets = interets;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public List<YvsMutPeriodeExercice> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsMutPeriodeExercice> periodes) {
        this.periodes = periodes;
    }

    public String getMatriculeMut() {
        return matriculeMut;
    }

    public void setMatriculeMut(String matriculeMut) {
        this.matriculeMut = matriculeMut;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public long getExercice() {
        return exercice;
    }

    public void setExercice(long exercice) {
        this.exercice = exercice;
    }

    public long getPeriode() {
        return periode;
    }

    public void setPeriode(long periode) {
        this.periode = periode;
    }

    public List<YvsMutCompte> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsMutCompte> comptes) {
        this.comptes = comptes;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public void loadAll() {
        loadAllInteret(true, true);   //charge la liste des intérêts calculé et payé
        exercices = dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findBySociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        YvsMutCaisse c = (YvsMutCaisse) dao.loadOneByNameQueries("YvsMutCaisse.findCaissePrincipale", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        if (c != null) {
            caisse = UtilMut.buildBeanCaisse(c);
        }
    }

    public void loadAllInteret(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.mutualiste.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        interets = paginator.executeDynamicQuery("YvsMutInteret", "y.dateInteret DESC", avance, init, (int) imax, dao);
    }

    public void paginer(boolean next) {
        loadAllInteret(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllInteret(true, true);
    }

    @Override
    public Interet recopieView() {
        return null;
    }

    @Override
    public boolean controleFiche(Interet bean) {
        return true;
    }

    @Override
    public void populateView(Interet bean) {

    }

    @Override
    public void resetFiche() {

    }

    @Override
    public boolean saveNew() {

        return true;
    }

    @Override
    public void deleteBean() {

    }

    @Override
    public void updateBean() {

    }

    @Override
    public void loadOnView(SelectEvent ev) {

    }

    public void chooseExerice() {

        update("data_interet_mutualiste");
    }

    public void saveNewAll() {

    }

    public boolean saveNew(Interet bean, boolean all) {

        return false;
    }

    public void choiCompte(String ref, YvsMutInteret i) {
        if ((ref != null) ? !ref.trim().isEmpty() : false) {
            List<YvsMutCompte> l = dao.loadNameQueries("YvsMutCompte.findByMutualisteActif", new String[]{"mutualiste"}, new Object[]{i.getMutualiste()});
            System.err.println(" Comptes mutualiste --- " + l.size());
            for (YvsMutCompte c : l) {
                if (c.getReference().startsWith(ref)) {
                    System.err.println(" Here choice...");
                    i.setCompte(c);
                    return;
                }
            }
            lineSelect = interets.indexOf(i);
            comptes = i.getMutualiste().getComptes();
            update("table_compte_mutualiste");
            openDialog("mutCompte");
        }
    }

    public void choixCompte(SelectEvent ev) {
        YvsMutCompte c = (YvsMutCompte) ev.getObject();
        if (lineSelect >= 0) {
            interets.get(lineSelect).setCompte(c);
            update("data_interet");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamMutualiste(String matricule) {
        ParametreRequete p0 = new ParametreRequete(null, "mutualiste", "XXXXX", " LIKE ", "AND");
        if ((matricule != null) ? !matricule.trim().isEmpty() : false) {
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.matricule)", "matricule", matricule.toUpperCase() + "%", "LIKE", "OR"));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.nom)", "nom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR"));
            p0.getOtherExpression().add(new ParametreRequete("UPPER(y.mutualiste.employe.prenom)", "prenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p0.setObjet(null);
        }
        paginator.addParam(p0);
        loadAllInteret(true, true);
    }

    public void addParamDates() {

        loadAllInteret(true, true);
    }

    public void addParamExercice(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.periode.exercice", "exercice", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            p.setObjet(new YvsBaseExercice(id));
            periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findByExercice", new String[]{"exercice"}, new Object[]{new YvsBaseExercice(id)});
        } else {
            periodes = dao.loadNameQueries("YvsMutPeriodeExercice.findBySociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        paginator.addParam(p);
        loadAllInteret(true, true);
    }

    public void addParamPeriode(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.periode", "periode", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            p.setObjet(new YvsMutPeriodeExercice(id));
        }
        paginator.addParam(p);
        loadAllInteret(true, true);
    }

    public void addParamStatut(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statutPaiement", "statut", null, operateur, "AND");
        if (ev.getNewValue() != null) {
            String etat = (String) ev.getNewValue();
            p.setObjet(etat);
        }
        paginator.addParam(p);
        loadAllInteret(true, true);
    }

    public void changeOperateur(ValueChangeEvent ev) {
        operateur = (String) ev.getNewValue();
        loadAllInteret(true, true);
    }

    private YvsMutCompte findCompte(YvsMutMutualiste m, boolean interet, boolean prime) {
        if ((m != null) ? m.getComptes() != null : false) {
            for (YvsMutCompte c : m.getComptes()) {
                if (c.getInteret() && interet) {
                    return c;
                }
                if (c.getPrime() && prime) {
                    return c;
                }
            }
        }
        return null;
    }

    public boolean confirmPaiementOneInteret(YvsMutInteret i, boolean all) {
        // find compte intérêt
        if (!((i.getCompte() != null) ? i.getCompte().getId() != null ? i.getCompte().getId() > 0 : false : false)) {
            YvsMutCompte c = findCompte(i.getMutualiste(), true, false);
            i.setCompte(c);
        }
        if (modePaiement.equals(Constantes.MUT_MODE_PAIEMENT_ESPECE)) {
            if (caisse.getId() > 0) {
                i.setCaisse(new YvsMutCaisse(caisse.getId()));
                i.setCompte(null);
            } else {
                if (!all) {
                    getErrorMessage("Veuillez choisir une caisse...");
                }
                return false;
            }
        } else {
            if (i.getCompte() == null) {
                if (!all) {
                    getErrorMessage("Aucun compte n'a été trouvé !");
                }
                return false;
            } else {
                i.setCaisse(null);
            }
        }
        i.setDateUpdate(new Date());
        i.setAuthor(currentUser);
        i.setStatutPaiement(Constantes.ETAT_REGLE);
        i.setDateInteret(new Date());
        dao.update(i);
        return true;
    }

    public void confirmPaiementAllInteret() {
        boolean re = true;
        boolean result = true;
        for (YvsMutInteret i : interets) {
            re = confirmPaiementOneInteret(i, true);
            if (!re) {
                result = false;
            }
        }
        if (result) {
            succes();
        } else {
            getWarningMessage("Toutes les intérêts n'ont pas été payé !");
        }
    }
}

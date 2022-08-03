/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.grh.statistique.YvsStatGRhElementDipe;
import yvs.entity.grh.statistique.YvsStatGrhEtat;
import yvs.entity.grh.statistique.YvsStatGrhGrilleDipe;
import yvs.entity.grh.statistique.YvsStatGrhGroupeElement;
import yvs.entity.grh.statistique.YvsStatGrhTauxContribution;
import yvs.entity.param.YvsAgences;
import yvs.etats.Dashboards;
import yvs.grh.UtilGrh;
import yvs.grh.paie.ElementSalaire;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.ManagedExercice;
import yvs.parametrage.agence.ManagedAgence;
import yvs.util.Managed;
import yvs.util.Options;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedParamStat extends Managed<ParametrageStat, YvsBaseCaisse> implements Serializable {

    private List<YvsGrhElementSalaire> sourceElts;
    private List<YvsGrhElementSalaire> allElements;
    private List<YvsStatGrhEtat> listEtat;
    private List<YvsStatGrhGroupeElement> groupes;
    private YvsStatGrhEtat paramStat = new YvsStatGrhEtat(-1L);
    private YvsStatGrhGroupeElement groupe = new YvsStatGrhGroupeElement();
    private YvsStatGrhGrilleDipe grille = new YvsStatGrhGrilleDipe();
    private YvsStatGrhTauxContribution tauxContr = new YvsStatGrhTauxContribution();
    private List<Options> etatsRh;
    private List<Options> etatsTresors;
    private List<Options> etatsLivresP = new ArrayList<>();
    private List<Options> etatsCnps = new ArrayList<>();
    private List<Options> etatsTc = new ArrayList<>();
    private List<Options> etatsFne = new ArrayList<>();
    private List<Options> etatsRav = new ArrayList<>();
    private List<Options> etatsG;
    private List<Options> etatsOthers = new ArrayList<>();
//    private String rapportG;
    private boolean etatAnnuel = false;
    private boolean oneEmploye = false;
    private String etatTresor = "DIPE_IRPP";
    private String etatGen = "RECAP";
    private String etatLivreP = "LP";
    private String etatCnps = "DIPE_CNPS";
    private String etatFne = "DIPE_FNE";
    private String etatTc = "DIPE_TC";
    private String etatRav = "DIPE_RAV";
    private String etatOther = "CSTC";
    private long idAgence;
    private List<YvsAgences> agenceSelect;
    private List<YvsGrhOrdreCalculSalaire> selectPeriode;

    private YvsStatGRhElementDipe selectedLine = new YvsStatGRhElementDipe(-1L);

    private Dashboards masseSalariale = new Dashboards();

    private String codeEltSearch;

    public ManagedParamStat() {
        etatsRh = new ArrayList<>();
        agenceSelect = new ArrayList<>();
        selectPeriode = new ArrayList<>();
        sourceElts = new ArrayList<>();
        listEtat = new ArrayList<>();
        etatsTresors = new ArrayList<>();
        etatsG = new ArrayList<>();
        periodes = new ArrayList<>();
        employes = new ArrayList<>();
        groupes = new ArrayList<>();
        departements = new ArrayList<>();

        etatsTresors.add(new Options("Récap.", "DIPE_IRPP"));
        etatsTresors.add(new Options("Détaillé", "DIPE_IRPP_2"));

        etatsRav.add(new Options("Récap.", "DIPE_RAV"));
        etatsRav.add(new Options("Détaillé", "DIPE_RAV2"));

        etatsTc.add(new Options("Récap.", "DIPE_TC"));
        etatsTc.add(new Options("Détaillé", "DIPE_TC2"));

        etatsFne.add(new Options("Recap. FNE", "DIPE_FNE"));
        etatsFne.add(new Options("Détail FNE", "DIPE_FNE2"));
        etatsFne.add(new Options("Récap. CF", "DIPE_CF"));
        etatsFne.add(new Options("Détaillé CF", "DIPE_CF2"));

        etatsCnps.add(new Options("Récap.", "DIPE_CNPS"));
        etatsCnps.add(new Options("Détaillé.", "DIPE_CNPS2"));
//        etatsTresors.add(new Options("RESUME DES COTISATIONS", "RES_COT"));
//        etatsTresors.add(new Options("DIPE TELEDECLARATION", "RECAP_PAYE"));
//        etatsTresors.add(new Options("LIVRE DE PAIE", "LIVRE_PAIE"));
//        etatsTresors.add(new Options("JOURNAL SALAIRE", "J_SALAIRE"));
        etatsOthers.add(new Options("Recap. C.S.T.C", "CSTC"));
        etatsOthers.add(new Options("C.S.T.C Détaillé", "CSTC2"));
        etatsOthers.add(new Options("Journal de paie par catégorie", "JRN_PAIE_CONV"));

        etatsLivresP.add(new Options("Livre de paie", "LP"));
        etatsLivresP.add(new Options("Livre de paie par service", "LPS"));
        etatsLivresP.add(new Options("Résumé des cotisations", "RES_COT"));
        etatsLivresP.add(new Options("DIPE Télédéclaration", "RECAP_PAYE"));

        etatsG.add(new Options("Récapitulatif Paie", "RECAP"));
        etatsG.add(new Options("Fiche Individuel", "GEN_FI"));
        etatsG.add(new Options("Inf. Personnel", "GEN_IP"));
        etatsG.add(new Options("Liste Salarié", "GEN_LS"));
        etatsG.add(new Options("Journal Gain", "GEN_JS"));
        etatsG.add(new Options("Journal Retenues", "GEN_JSR"));
        etatsG.add(new Options("Situation MO", "GEN_SMO"));

        etatsRh.add(new Options("Trésor Récap.", "DIPE_IRPP"));
        etatsRh.add(new Options("Trésor Détaillé.", "DIPE_IRPP_2"));
        etatsRh.add(new Options("RAV", "DIPE_RAV"));
        etatsRh.add(new Options("Taxe Dev. local", "DIPE_TC"));
        etatsRh.add(new Options("DIPE. FNE", "DIPE_FNE"));
        etatsRh.add(new Options("Récap. CF", "DIPE_CF"));
        etatsRh.add(new Options("Détaillé CF", "DIPE_CF2"));
        etatsRh.add(new Options("CNPS Récap.", "DIPE_CNPS"));
        etatsRh.add(new Options("CNPS Détail", "DIPE_CNPS2"));
        etatsRh.add(new Options("ETAT C.S.T.C", "CSTC"));
        etatsRh.add(new Options("Livre de paie", "LIVRE_PAIE"));
        etatsRh.add(new Options("Résumé des cotisations", "RES_COT"));
        etatsRh.add(new Options("DIPE Télédéclaration", "RECAP_PAYE"));
        etatsRh.add(new Options("Récapitulatif Paie", "RECAP"));
        etatsRh.add(new Options("Fiche Individuel", "GEN_FI"));
        etatsRh.add(new Options("Inf. Personnel", "GEN_IP"));
        etatsRh.add(new Options("Liste Salarié", "GEN_LS"));
        etatsRh.add(new Options("Journal Salaire Gain", "GEN_JS"));
        etatsRh.add(new Options("Journal Salaire Retenues", "GEN_JSR"));
        etatsRh.add(new Options("Journal de paie par catégorie", "JRN_PAIE_CONV"));
        etatsRh.add(new Options("Recapitulatif du bulletin", "RECAP_BUL"));
        etatsRh.add(new Options("Situation MO", "GEN_SMO"));
        etatsRh.add(new Options("Récapitulatif Paie", "RECAP"));

    }

    public String getCodeEltSearch() {
        return codeEltSearch;
    }

    public void setCodeEltSearch(String codeEltSearch) {
        this.codeEltSearch = codeEltSearch;
    }

    public List<YvsGrhElementSalaire> getAllElements() {
        return allElements;
    }

    public void setAllElements(List<YvsGrhElementSalaire> allElements) {
        this.allElements = allElements;
    }

    public List<YvsGrhOrdreCalculSalaire> getSelectPeriode() {
        return selectPeriode;
    }

    public void setSelectPeriode(List<YvsGrhOrdreCalculSalaire> selectPeriode) {
        this.selectPeriode = selectPeriode;
    }

    public boolean isOneEmploye() {
        return oneEmploye;
    }

    public void setOneEmploye(boolean oneEmploye) {
        this.oneEmploye = oneEmploye;
    }

    public boolean isEtatAnnuel() {
        return etatAnnuel;
    }

    public void setEtatAnnuel(boolean etatAnnuel) {
        this.etatAnnuel = etatAnnuel;
    }

    public List<YvsGrhOrdreCalculSalaire> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsGrhOrdreCalculSalaire> periodes) {
        this.periodes = periodes;
    }

    public Dashboards getMasseSalariale() {
        return masseSalariale;
    }

    public void setMasseSalariale(Dashboards masseSalariale) {
        this.masseSalariale = masseSalariale;
    }

    public List<YvsAgences> getAgenceSelect() {
        return agenceSelect;
    }

    public void setAgenceSelect(List<YvsAgences> agenceSelect) {
        this.agenceSelect = agenceSelect;
    }

    public List<Options> getEtatsRh() {
        return etatsRh;
    }

    public void setEtatsRh(List<Options> etatsRh) {
        this.etatsRh = etatsRh;
    }

    public List<Options> getEtatsLivresP() {
        return etatsLivresP;
    }

    public void setEtatsLivresP(List<Options> etatsLivresP) {
        this.etatsLivresP = etatsLivresP;
    }

    public List<Options> getEtatsCnps() {
        return etatsCnps;
    }

    public void setEtatsCnps(List<Options> etatsCnps) {
        this.etatsCnps = etatsCnps;
    }

    public List<Options> getEtatsTc() {
        return etatsTc;
    }

    public void setEtatsTc(List<Options> etatsTc) {
        this.etatsTc = etatsTc;
    }

    public List<Options> getEtatsFne() {
        return etatsFne;
    }

    public void setEtatsFne(List<Options> etatsFne) {
        this.etatsFne = etatsFne;
    }

    public List<Options> getEtatsRav() {
        return etatsRav;
    }

    public void setEtatsRav(List<Options> etatsRav) {
        this.etatsRav = etatsRav;
    }

    public List<Options> getEtatsOthers() {
        return etatsOthers;
    }

    public void setEtatsOthers(List<Options> etatsOthers) {
        this.etatsOthers = etatsOthers;
    }

    public String getEtatOther() {
        return etatOther;
    }

    public void setEtatOther(String etatOther) {
        this.etatOther = etatOther;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    public YvsStatGrhGroupeElement getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsStatGrhGroupeElement groupe) {
        this.groupe = groupe;
    }

    public List<YvsStatGrhGroupeElement> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<YvsStatGrhGroupeElement> groupes) {
        this.groupes = groupes;
    }

    public long getIdGrille() {
        return idGrille;
    }

    public void setIdGrille(long idGrille) {
        this.idGrille = idGrille;
    }

    public List<YvsGrhDepartement> getDepartements() {
        return departements;
    }

    public void setDepartements(List<YvsGrhDepartement> departements) {
        this.departements = departements;
    }

//    public String getRapportG() {
//        return rapportG;
//    }
//
//    public void setRapportG(String rapportG) {
//        this.rapportG = rapportG;
//    }
    public List<Options> getEtatsG() {
        return etatsG;
    }

    public void setEtatsG(List<Options> etatsG) {
        this.etatsG = etatsG;
    }

    public List<Options> getEtatsTresors() {
        return etatsTresors;
    }

    public void setEtatsTresors(List<Options> etatsTresors) {
        this.etatsTresors = etatsTresors;
    }

    public List<YvsGrhElementSalaire> getSourceElts() {
        return sourceElts;
    }

    public void setSourceElts(List<YvsGrhElementSalaire> sourceElts) {
        this.sourceElts = sourceElts;
    }

    public List<YvsStatGrhEtat> getListEtat() {
        return listEtat;
    }

    public void setListEtat(List<YvsStatGrhEtat> listEtat) {
        this.listEtat = listEtat;
    }

    public YvsStatGrhEtat getParamStat() {
        return paramStat;
    }

    public void setParamStat(YvsStatGrhEtat paramStat) {
        this.paramStat = paramStat;
    }

    public YvsStatGrhGrilleDipe getGrille() {
        return grille;
    }

    public void setGrille(YvsStatGrhGrilleDipe grille) {
        this.grille = grille;
    }

    public YvsStatGrhTauxContribution getTauxContr() {
        return tauxContr;
    }

    public void setTauxContr(YvsStatGrhTauxContribution tauxContr) {
        this.tauxContr = tauxContr;
    }

    public YvsStatGRhElementDipe getSelectedLine() {
        return selectedLine;
    }

    public void setSelectedLine(YvsStatGRhElementDipe selectedLine) {
        this.selectedLine = selectedLine;
    }

    @Override
    public void loadAll() {
        //charge les éléments de salaire
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        sourceElts = dao.loadNameQueries("YvsGrhElementSalaire.findAll", champ, val);
        allElements = new ArrayList<>(sourceElts);
        //charge les éléments statistique paramétré
        buildEtatDipe();
    }

    private void buildEtatDipe() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listEtat = dao.loadNameQueries("YvsStatGrhEtat.findAll", champ, val);
    }

    private ParamStatPaie buildEtat(YvsStatGrhEtat e) {
        Etats et = new Etats(e.getId(), e.getCode(), e.getLibelle());
        ParamStatPaie p = new ParamStatPaie();
        p.setEtat(et);
        p.setId(e.getId());
        //Charge les éléments de salaires
        ElementSalaire es;
        for (YvsStatGRhElementDipe st : e.getElements()) {
            es = UtilGrh.buildElementSalaire(st.getElementSalaire());
//            es.setSelectActif(true);
            p.getElements().add(es);
        }
        //charge les grilles d'intervalle
        champ = new String[]{"etat"};
        val = new Object[]{e};
        for (Iterator it = dao.loadNameQueries("YvsStatGrhGrilleDipe.findByEtat", champ, val).iterator(); it.hasNext();) {
            YvsStatGrhGrilleDipe gr = (YvsStatGrhGrilleDipe) it.next();
            p.getGrillesDipe().add(builGrilleDipe(gr));
        }
        //charge les tauxContr
        TauxContribution t;
        for (YvsStatGrhTauxContribution st : e.getTauxContributions()) {
            t = new TauxContribution(st.getId(), st.getLibelle(), st.getTaux(), et);
            p.getListTaux().add(t);
        }

        return p;
    }

    private GrilleDipe builGrilleDipe(YvsStatGrhGrilleDipe di) {
        GrilleDipe g = new GrilleDipe();
        g.setBase(new ParamStatPaie(di.getEtat().getId(), new Etats(di.getId(), di.getEtat().getCode(), di.getEtat().getLibelle())));
        g.setId(di.getId());
        g.setMontant(di.getMontant());
        g.setTrancheMax(di.getTrancheMax());
        g.setTrancheMin(di.getTrancheMin());
        return g;
    }

    long id = -1000;

    public void deplaceELt(DragDropEvent ddEvent) {
        if (paramStat != null) {
            YvsGrhElementSalaire elt = ((YvsGrhElementSalaire) ddEvent.getData());
            YvsStatGRhElementDipe eltD = new YvsStatGRhElementDipe();
            if (paramStat.getId() > 0) {
                eltD.setEtat(paramStat);
                eltD.setLibelle(elt.getNom());
                eltD.setElementSalaire(elt);
                eltD.setAuthor(currentUser);
                eltD.setOrdre(0);
                eltD = (YvsStatGRhElementDipe) dao.save1(eltD);
            } else {
                eltD.setElementSalaire(elt);
                eltD.setId(id++);
            }
            paramStat.getElements().add(eltD);
            sourceElts.remove(elt);
        } else {
            getErrorMessage("Veuillez choisir l'Etat à paramétrer !");
        }
    }

    public void deleteOneEltDipe(YvsStatGRhElementDipe elt) {
        if (elt != null) {
            if (elt.getId() > 0) {
                try {
                    dao.delete(elt);
                    paramStat.getElements().remove(elt);
                } catch (Exception ex) {

                }
            } else {
                paramStat.getElements().remove(elt);
            }
        }
    }

    public void selectEtat(ValueChangeEvent ev) {
        if ((ev != null) ? (ev.getNewValue() != null) : false) {
            String v = (String) ev.getNewValue();
            paramStat = (YvsStatGrhEtat) dao.loadOneByNameQueries("YvsStatGrhEtat.findByCode", new String[]{"code", "societe"}, new Object[]{v, currentUser.getAgence().getSociete()});
            if (paramStat == null) {
                paramStat = new YvsStatGrhEtat(v);
                paramStat.setId(id++);
            }
        }
    }

    public void saveElementStat() {
        YvsStatGRhElementDipe dipe;
        YvsStatGrhGrilleDipe gril;
        if (paramStat.getId() <= 0) {
            paramStat.setId(null);
            paramStat.setSociete(currentAgence.getSociete());
            paramStat.setAuthor(currentUser);
            paramStat.setLibelle(etatsRh.get(etatsRh.indexOf(new Options(paramStat.getCode()))).getValeur());
            List<YvsStatGRhElementDipe> l = new ArrayList<>(paramStat.getElements());
            List<YvsStatGrhGrilleDipe> lg = new ArrayList<>(paramStat.getGrillesDipe());
            List<YvsStatGrhTauxContribution> lt = new ArrayList<>(paramStat.getTauxContributions());
            paramStat.getElements().clear();
            paramStat.getTauxContributions().clear();
            paramStat.getGrillesDipe().clear();
            paramStat = (YvsStatGrhEtat) dao.save1(paramStat);
            paramStat.setElements(l);
            paramStat.setGrillesDipe(lg);
            paramStat.setTauxContributions(lt);
        }
        for (YvsStatGRhElementDipe e : paramStat.getElements()) {
            e.setEtat(paramStat);
            e.setLibelle(e.getElementSalaire().getNom());
            e.setElementSalaire(e.getElementSalaire());
            if (e.getId() <= 0) {
                e.setId(null);
                e = (YvsStatGRhElementDipe) dao.save1(e);
            }
        }
        //sauvegarde la grille
        for (YvsStatGrhGrilleDipe g : paramStat.getGrillesDipe()) {
            if (g.getId() < 0) {
                g.setEtat(paramStat);
                g.setId(null);
                g = (YvsStatGrhGrilleDipe) dao.save1(g);
            }
        }
        //sauvegarde les tauxContr
        for (YvsStatGrhTauxContribution t : paramStat.getTauxContributions()) {
            if (t.getId() < 0) {
                YvsStatGrhTauxContribution tt = new YvsStatGrhTauxContribution();
                tt.setLibelle(t.getLibelle());
                tt.setEtat(paramStat);
                tt.setTaux(t.getTaux());
                tt = (YvsStatGrhTauxContribution) dao.save1(tt);
                t.setId(tt.getId());
            }
        }
        succes();
    }

    //Enregistrer la grille de la dipe
    private long idGrille = -1000;

    public void addTrancheVAl() {
        if (paramStat != null) {
            if (paramStat.getElements().size() == 1) {
                grille.setEtat(paramStat);
                grille.setId(idGrille);
                idGrille++;
                if (paramStat.getId() > 0) {
                    grille.setEtat(paramStat);
                    grille.setId(null);
                    grille = (YvsStatGrhGrilleDipe) dao.save1(grille);
                }
                paramStat.getGrillesDipe().add(grille);
                grille = new YvsStatGrhGrilleDipe();
            } else {
                getErrorMessage("La grille doit être basé sur un unique élément de salaire !");

            }
        } else {
            getErrorMessage("Aucun Etat n'est selctionné !");
        }
    }

    public void deleteLineGrille(YvsStatGrhGrilleDipe g) {
        if (g.getId() > 0) {
            try {
                dao.delete(new YvsStatGrhGrilleDipe(g.getId()));
                paramStat.getGrillesDipe().remove(g);
                succes();
            } catch (Exception ex) {
                Logger.getLogger(ManagedParamStat.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            paramStat.getGrillesDipe().remove(g);
        }
    }

    public void addTaux() {
        if (paramStat != null) {
            tauxContr.setEtat(paramStat);
            tauxContr.setId(idGrille);
            idGrille++;
            paramStat.getTauxContributions().add(tauxContr);
            tauxContr = new YvsStatGrhTauxContribution();

        } else {
            getErrorMessage("Aucun Etat n'est selctionné !");
        }
    }

    public void deleteLineTaux(YvsStatGrhTauxContribution t) {
        if (t.getId() > 0) {
            try {
                dao.delete(new YvsStatGrhTauxContribution(t.getId()));
                paramStat.getTauxContributions().remove(t);
                succes();
            } catch (Exception ex) {
                Logger.getLogger(ManagedParamStat.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            paramStat.getTauxContributions().remove(t);
        }
    }

    @Override
    public boolean controleFiche(ParametrageStat bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParametrageStat recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(ParametrageStat bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * ********************************************************
     */
    /**
     * IMPRIMER LES ETATS FISCAUX*
     */
    private long periode;
    private int indexTax = 0;
//    private OrdreCalculSalaire periode2 = new OrdreCalculSalaire();
    private PaginatorResult<YvsGrhOrdreCalculSalaire> p_entete = new PaginatorResult<>();
    private List<YvsGrhOrdreCalculSalaire> periodes;
    private long emp1, emp2;
    private long dep1, dep2;
    private Long idExercice;
    private List<YvsGrhEmployes> employes;
    private List<YvsGrhDepartement> departements;

    public int getIndexTax() {
        return indexTax;
    }

    public void setIndexTax(int indexTax) {
        this.indexTax = indexTax;
    }

    public long getEmp1() {
        return emp1;
    }

    public void setEmp1(long emp1) {
        this.emp1 = emp1;
    }

    public long getEmp2() {
        return emp2;
    }

    public void setEmp2(long emp2) {
        this.emp2 = emp2;
    }

    public long getId() {
        return id;
    }

    public String getEtatGen() {
        return etatGen;
    }

    public void setEtatGen(String etatGen) {
        this.etatGen = etatGen;
    }

    public String getEtatLivreP() {
        return etatLivreP;
    }

    public void setEtatLivreP(String etatLivreP) {
        this.etatLivreP = etatLivreP;
    }

    public String getEtatCnps() {
        return etatCnps;
    }

    public void setEtatCnps(String etatCnps) {
        this.etatCnps = etatCnps;
    }

    public String getEtatFne() {
        return etatFne;
    }

    public void setEtatFne(String etatFne) {
        this.etatFne = etatFne;
    }

    public String getEtatTc() {
        return etatTc;
    }

    public void setEtatTc(String etatTc) {
        this.etatTc = etatTc;
    }

    public String getEtatRav() {
        return etatRav;
    }

    public void setEtatRav(String etatRav) {
        this.etatRav = etatRav;
    }

    public YvsStatGRhElementDipe getCurrentElt() {
        return currentElt;
    }

    public void setCurrentElt(YvsStatGRhElementDipe currentElt) {
        this.currentElt = currentElt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDep1() {
        return dep1;
    }

    public void setDep1(long dep1) {
        this.dep1 = dep1;
    }

    public long getDep2() {
        return dep2;
    }

    public void setDep2(long dep2) {
        this.dep2 = dep2;
    }

    public String getEtatTresor() {
        return etatTresor;
    }

    public void setEtatTresor(String etatTresor) {
        this.etatTresor = etatTresor;
    }

    public long getPeriode() {
        return periode;
    }

    public void setPeriode(long periode) {
        this.periode = periode;
    }

    public Long getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(Long idExercice) {
        this.idExercice = idExercice;
    }

    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    public void toogleAddToLivrePaie(YvsGrhElementSalaire el) {
        el.setVisibleOnLivrePaie(!el.getVisibleOnLivrePaie());
        el.setAuthor(currentUser);
        dao.update(el);
    }

    /**
     * *
     */
    //Paramétrage des groupes
    public void loadAllGroupe() {
        groupes = dao.loadNameQueries("YvsStatGrhGroupeElement.findAll", new String[]{}, new Object[]{});
    }

    public void createGroupe() {
        if (groupe.getCodeGroupe() != null && groupe.getLibelle() != null) {
            groupe.setAuthor(currentUser);
            groupe = (YvsStatGrhGroupeElement) dao.update(groupe);
            if (groupes.contains(groupe)) {
                groupes.set(groupes.indexOf(groupe), groupe);
            } else {
                groupes.add(0, groupe);
            }
            groupe = new YvsStatGrhGroupeElement(0L);
            update("table_groupe_elt_dipe");
        }
    }

    public void deleteGroupe(YvsStatGrhGroupeElement grp) {
        try {
            dao.delete(grp);
            int idx = groupes.indexOf(grp);
            if (idx >= 0) {
                groupes.remove(grp);
            }
            update("table_groupe_elt_dipe");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
        }
    }

    public void selectGroupeToUpDate(YvsStatGrhGroupeElement grp) {
        groupe = grp;
        update("form_groupe_elt_dipe");
        openDialog("dlgGroupe");
    }

    public void selectGroupe(SelectEvent ev) {
        YvsStatGrhGroupeElement grp = (YvsStatGrhGroupeElement) ev.getObject();
        if (currentElt != null) {
            currentElt.setGroupeElement(grp);
            dao.update(currentElt);
        }
    }
    private YvsStatGRhElementDipe currentElt;

    public void addLineToGroupe(YvsStatGRhElementDipe el) {
        this.currentElt = el;
        openDialog("dlgGroupe");
    }

    public void definedByFormule(YvsStatGRhElementDipe el) {
        el.setByFormulaire(!el.getByFormulaire());
        if (el.getId() > 0) {
            el.setEtat(paramStat);
            dao.update(el);
        }
    }

    /**
     * ***
     */
    //charge les ordres
    public void loadPlanifOrdreCalcul() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        departements = dao.loadNameQueries("YvsGrhDepartement.findAll", champ, val);
        if (!departements.isEmpty()) {
            dep1 = departements.get(0).getId();
            dep2 = departements.get(departements.size() - 1).getId();
        }
        //créee l'entête des bulletins de paie du mois en cours
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        employes = dao.loadNameQueries("YvsGrhEmployes.findAllMat", champ, val);
        if (!employes.isEmpty()) {
            emp1 = employes.get(0).getId();
            emp2 = employes.get(employes.size() - 1).getId();
        }
    }

    public void loadAllPeriodes() {
        YvsBaseExercice exo = giveExerciceActif(new Date());
        if (exo != null) {
            addParamDates(exo.getDateDebut(), exo.getDateFin());
        } else {
            addParamDates(null, null);
        }
    }

    public void loadHeadBulletin(boolean avance, boolean init) {
        p_entete.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        periodes = p_entete.executeDynamicQuery("YvsGrhOrdreCalculSalaire", "y.debutMois ASC", avance, init, ifin, dao);
    }

    public void addParamDates(Date debut, Date fin) {
        ParametreRequete p = new ParametreRequete("y.dateJour", "dateJour", null, "=", "AND");
        if (debut != null && fin != null) {
            p = new ParametreRequete("y.dateJour", "dateJour", debut, fin, "BETWEEN", "AND");
        }
        p_entete.addParam(p);
        loadHeadBulletin(true, true);
    }

    public void choixExercice(ValueChangeEvent ev) {
        Date debut = null, fin = null;
        if (ev.getNewValue() != null) {
            Long id = (Long) ev.getNewValue();
            if (id >= 0) {
                ManagedExercice service = (ManagedExercice) giveManagedBean(ManagedExercice.class);
                if (service != null) {
                    int idx = service.getExercices().indexOf(new YvsBaseExercice(id));
                    if (idx >= 0) {
                        YvsBaseExercice exo = service.getExercices().get(idx);
                        debut = exo.getDateDebut();
                        fin = exo.getDateFin();
                    }
                }
            }
        }
        addParamDates(debut, fin);
    }

    public void selectLineElt(SelectEvent ev) {
        YvsStatGRhElementDipe st = (YvsStatGRhElementDipe) ev.getObject();
        selectedLine = st;
        if (selectedLine.getGroupeElement() == null) {
            selectedLine.setGroupeElement(new YvsStatGrhGroupeElement(-1L));
        }
        openDialog("dlgEltDipe");
        update("form_up_elt_dipe");
    }

    public void updateElementDipe() {
        if (selectedLine != null) {
            if (selectedLine.getId() > 0) {
                selectedLine.setAuthor(currentUser);
                if (selectedLine.getGroupeElement().getId() <= 0) {
                    selectedLine.setGroupeElement(null);
                }
                dao.update(selectedLine);
                int idx = paramStat.getElements().indexOf(selectedLine);
                if (idx >= 0) {
                    paramStat.getElements().set(idx, selectedLine);
                }
                update("dropArea");
                succes();
            }
        }
    }

    private String givePeriodes(long periode) {
        String header = null;
        String req = "SELECT DISTINCT p.id FROM yvs_grh_ordre_calcul_salaire p INNER JOIN yvs_base_exercice x ON p.debut_mois BETWEEN x.date_debut AND x.date_fin INNER JOIN yvs_grh_ordre_calcul_salaire o ON p.societe = o.societe AND o.debut_mois BETWEEN x.date_debut AND x.date_fin WHERE o.id = ? AND p.societe = ?";
        List<Long> ids = dao.loadListBySqlQuery(req, new yvs.dao.Options[]{new yvs.dao.Options(periode, 1), new yvs.dao.Options(currentAgence.getSociete().getId(), 2)});
        if (ids != null ? !ids.isEmpty() : false) {
            header = "0";
            for (Long value : ids) {
                header += "," + value;
            }
        }
        return header;
    }

    public void exportEtat(String etat) {
        exportEtat(etat, "pdf");
    }

    public void exportEtat(String etat, String extension) {
        if (idAgence > 0 || !agenceSelect.isEmpty()) {
            doawnloadDocPaye(etat, extension);
        } else {
            getErrorMessage("Veuillez choisir l'agence !");
        }
    }

    public void doawnloadDocPaye(String etat, String extension) {
        if (etat != null) {
            switch (etat) {
                case "JRN_PAIE_CONV":
                    exporteJournaPaie("journal_de_paie_by_convention", extension);
                    break;
                case "DIPE_IRPP":
                    //importe deux états
                    exporteDocePaye("dipe_tresor", extension);
                    break;
                case "DIPE_IRPP_2":
                    exporteDocePayeH("recapitulatif_dipe_irpp2", extension, null);
                    break;
                case "DIPE_RAV":
                    exporteDocePaye("dipe_rav", extension);
                    break;
                case "DIPE_RAV2":
                    exporteDocePayeH("recapitulatif_dipe_rav", extension, null);
                    break;
                case "DIPE_TC":
                    exporteDocePaye("dipe_rav_taxe_c", extension);
                    break;
                case "DIPE_TC2":
                    exporteDocePayeH("recapitulatif_dipe_tc", extension, null);
                    break;
                case "DIPE_CF":
                    exporteDocePaye("dipe_credit_foncier", extension);
                    break;
                case "DIPE_CF2":
                    exporteDocePayeH("recapitulatif_dipe_cf", extension, null);
                    break;
                case "DIPE_FNE":
                    exporteDocePaye("dipe_fne", extension);
                    break;
                case "DIPE_FNE2":
                    exporteDocePayeH("recapitulatif_dipe_fne", extension, null);
                    break;
                case "DIPE_CNPS":
                    exporteDocePaye("dipe_cnps", extension);
                    break;
                case "DIPE_CNPS2":
                    exporteDocePayeH("recapitulatif_dipe_cnps", extension, null);
                    break;
                case "CSTC":
                    exporteDocePaye("cstc", extension);
                    break;
                case "CSTC2":
                    exporteDocePayeH("recapitulatif_dipe_cstc", extension, null);
                    break;
                case "GEN_IP":  //information personnel
                    exporteDoceEmployes("information_personnel", extension);
                    break;
                case "GEN_JS":  //journal salaire (Gain et retenue)
                case "GEN_JSR":
                    if (etatAnnuel) {
                        String header = givePeriodes(periode);
                        if (header != null) {
                            exporteDocePaye((extension.equals("pdf") ? "journal_paie_gain" : "journal_paie_gain_excel"), header, "Annuel", extension, etat);
                        }
                    } else {
                        exporteDocePayeH((extension.equals("pdf") ? "journal_paie_gain" : "journal_paie_gain_excel"), extension, etat);
                    }
                    break;
                case "GEN_SMO":  //situation de la main d'oeuvre
                    exporteDocPayeMO("situation_main_oeuvre", extension);
                    break;
                case "GEN_FI":  //fiches individuel$
                    if (oneEmploye) {
                        exporteFicheInd((extension.equals("pdf") ? "fiche_individuelle" : "fiche_individuelle_excel"), extension);
                    } else {
                        exporteFicheInd((extension.equals("pdf") ? "all_fiche_ind" : "all_fiche_ind_excel"), extension);
                    }
                    break;
                case "GEN_LS":  //liste des salariés
                    exporteDocePayeH("liste_salarie", extension, null);
                    break;
                case "LP":  //livre de paye
                    exporteLivrePaye(extension);
                    break;
                case "LPS":  //livre de paye par service
                    exporteLivrePayeService(extension);
                    break;
                case "RECAP"://récapitulatif des salaires (Net à payer)
                    exporteRecapPaye(extension);
                    break;
                case "RES_COT": //résumé de cotisation
                    exporteEtatCotisation(extension);
                    break;
                case "RECAP_PAYE": //Dipe télédéclartion
                    exporteEtatRecap2(extension);
                    break;
                default:
                    System.err.println("Aucun état ne correspond à : " + etat);
                    break;
            }
        } else {
            getErrorMessage("Veuillez choisir l'état à imprimer !");
        }
    }

    public void selectAgence() {
        if (agenceSelect.size() == 1) {
            idAgence = agenceSelect.get(0).getId();
        } else {
            idAgence = -1;
        }
        update("txt_select_agence_rapp");
    }

    public void selectedPeriode() {
        if (!selectPeriode.isEmpty()) {
            periode = selectPeriode.get(0).getId();
        } else {
            periode = -1;
        }
        update("txt_select_periode_rapp");
    }

    public void exporteDocePaye(String rapport, String extension) {
        ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        String nameVille = "", bp = "", tel = "";
        String ids = null;
        for (YvsAgences a : agenceSelect) {
            if (ids != null ? ids.trim().length() > 0 : false) {
                ids += "," + a.getId();
            } else {
                ids = "" + a.getId();
            }
        }
        if (agenceSelect.size() == 1) {
            if (service != null) {
                int idx = service.getListAgence().indexOf(new YvsAgences(idAgence));
                if (idx >= 0) {
                    YvsAgences ag = service.getListAgence().get(idx);
                    nameVille = (ag.getVille() != null) ? ag.getVille().getLibele() : "";
                    bp = ag.getCodePostal();
                    tel = ag.getTelephone();
                }
            }
        } else {
            nameVille = (currentAgence.getSociete().getVille() != null) ? currentAgence.getSociete().getVille().getLibele() : "";
            bp = currentAgence.getSociete().getCodePostal();
            tel = currentAgence.getSociete().getTel();
        }
        Map<String, Object> param_ = new HashMap<>();
        param_.put("SOCIETE", currentAgence.getSociete().getName());
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId());
        param_.put("AGENCES", ids);
        param_.put("VILLE", nameVille);
        param_.put("BP", bp);
        param_.put("TEL", tel);
        param_.put("NUM_EMPLOYEUR", currentAgence.getSociete().getNumeroRegistreComerce());
        param_.put("REGIME_CNPS", currentAgence.getSociete().getRegimeCnps());
        param_.put("HEADER", periode);
        param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
        executeReport(rapport, param_, "", extension);
    }

    public void openViewAgence() {
        agenceSelect.clear();
        if (idAgence == -1) {
            update("data_agence_param_stat");
            openDialog("dlgChooseAgence");
        } else {
            if (idAgence > 0) {
                agenceSelect.add(new YvsAgences(idAgence));
            }
        }
    }

    public void openViewPeriode() {
        selectPeriode.clear();
        if (periode == -1) {
            update("data_periodes_param_stat");
            openDialog("dlgChoosePeriode");
        } else {
            if (periode > 0) {
                selectPeriode.add(new YvsGrhOrdreCalculSalaire(periode));
            }
        }
    }

    public void exporteDocePayeH(String rapport, String extension, String etat) {
        String titre = periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference();
        exporteDocePaye(rapport, periode + "", titre, extension, etat);
    }

    public void exporteDocePaye(String rapport, String header, String periode, String extension, String titre) {
        Map<String, Object> param_ = new HashMap<>();
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
            }
            param_.put("ID_AGENCE", ids);
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            param_.put("MULTIPLE", false);
        }
        param_.put("HEADER", header);
        param_.put("PERIODE", periode);
        if (Util.asString(titre)) {
            param_.put("TITRE", titre);
        }
        executeReport(rapport, param_, "", extension);
    }

    public void exporteDocePayeS(String rapport, String extension) {
        Map<String, Object> param_ = new HashMap<>();
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
        param_.put("NAME_SOCIETE", currentAgence.getSociete().getName());
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            String agences = "";
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
                agences += "," + a.getCodeagence();
            }
            param_.put("ID_AGENCE", ids);
            param_.put("NAME_AGENCE", agences);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            param_.put("NAME_AGENCE", (agences_.get(agences_.indexOf(new YvsAgences(idAgence))).getDesignation()));
        }
        param_.put("HEADER", (int) periode);
        param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        executeReport(rapport, param_, "", extension);
    }

    public void exporteDocPayeMO(String rapport, String extension) {
        Map<String, Object> param_ = new HashMap<>();
        ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
        param_.put("NAME_SOCIETE", currentAgence.getSociete().getName());
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            String agences = "";
            if (agenceSelect.size() > 1) {
                for (YvsAgences a : agenceSelect) {
                    if (ids != null ? ids.trim().length() > 0 : false) {
                        ids += "," + a.getId();
                    } else {
                        ids = "" + a.getId();
                    }
                    if (w != null) {
                        int idx = w.getListAgence().indexOf(a);
                        if (idx > -1) {
                            String code = (w.getListAgence().get(idx).getCodeagence().trim().length() > 0 ? w.getListAgence().get(idx).getCodeagence() : w.getListAgence().get(idx).getAbbreviation());
                            agences += "," + code;
                        }
                    }
                }
                param_.put("ID_AGENCE", ids);
                param_.put("NAME_AGENCE", agences);
            } else {
                param_.put("ID_AGENCE", agenceSelect.get(0).getId());
                if (w != null) {
                    int idx = w.getListAgence().indexOf(agenceSelect.get(0));
                    if (idx > -1) {
                        param_.put("NAME_AGENCE", (w.getListAgence().get(idx).getDesignation()));
                    }
                }
            }
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            if (w != null) {
                int idx = w.getListAgence().indexOf(new YvsAgences(idAgence));
                if (idx > -1) {
                    param_.put("NAME_AGENCE", (w.getListAgence().get(idx).getDesignation()));
                }
            }
            param_.put("MULTIPLE", false);
        }
        param_.put("HEADER", (int) periode);
        param_.put("ANNUEL", etatAnnuel);
        if (etatAnnuel) {
            param_.put("PERIODE", "Annuel");
        } else {
            param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        }
        executeReport(rapport, param_, "", extension);
    }

    public void exporteFicheInd(String rapport, String extension) {
        YvsGrhEmployes e1 = new YvsGrhEmployes(emp1);
        YvsGrhEmployes e2 = new YvsGrhEmployes(emp2);
        int idx = periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode));
        if (idx > -1) {
            YvsGrhOrdreCalculSalaire p = periodes.get(idx);
            YvsBaseExercice y = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), p.getDebutMois()});
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param_ = new HashMap<>();
                param_.put("DATE_DEBUT", y.getDateDebut());
                param_.put("DATE_FIN", p.getFinMois());
                param_.put("PERIODE", "M");
                if (oneEmploye) {
                    param_.put("ID_EMPLOYE", (int) emp1);
                } else {
                    param_.put("ID_AGENCE", (int) idAgence);
                    param_.put("matricule1", ((employes.contains(e1)) ? employes.get(employes.indexOf(e1)).getMatricule() : ""));
                    param_.put("matricule2", ((employes.contains(e2)) ? employes.get(employes.indexOf(e2)).getMatricule() : ""));
                    param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
                }
                executeReport(rapport, param_, "", extension);
            }
        }
    }

    public void exporteDoceEmployes(String rapport, String extension) {
        ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        String nameAgence = "", bp = "", tel = "";
        if (idAgence > 0) {
            if (service != null) {
                int idx = service.getListAgence().indexOf(new YvsAgences(idAgence));
                if (idx >= 0) {
                    YvsAgences ag = service.getListAgence().get(idx);
                    nameAgence = (ag.getVille() != null) ? ag.getVille().getLibele() : "";
                    bp = ag.getAdresse();
                    tel = ag.getTelephone();
                }
            }
        }
        Map<String, Object> param_ = new HashMap<>();
        param_.put("NAME_SOCIETE", currentAgence.getSociete().getName());
        param_.put("NAME_AGENCE", nameAgence);
        param_.put("ID_AGENCE", (int) idAgence);
        param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        param_.put("CURRENT_DATE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getFinMois());
        executeReport(rapport, param_, "", extension);
    }

    public void exporteDoceEmployesSituationMO(String rapport, String extension) {
        ManagedAgence service = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        String nameAgence = "", bp = "", tel = "";
        if (idAgence > 0) {
            if (service != null) {
                int idx = service.getListAgence().indexOf(new YvsAgences(idAgence));
                if (idx >= 0) {
                    YvsAgences ag = service.getListAgence().get(idx);
                    nameAgence = (ag.getVille() != null) ? ag.getVille().getLibele() : "";
                    bp = ag.getAdresse();
                    tel = ag.getTelephone();
                }
            }
        }
        Map<String, Object> param_ = new HashMap<>();
        param_.put("NAME_SOCIETE", currentAgence.getSociete().getName());
        param_.put("NAME_AGENCE", nameAgence);
        param_.put("ID_AGENCE", (int) idAgence);
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId());
        param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        param_.put("HEADER", (int) periode);
        param_.put("CURRENT_DATE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getFinMois());
        executeReport(rapport, param_, "", extension);
    }

    public void exporteLivrePaye(String extension) {
        int idx = periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode));
        if (idx > -1) {
            YvsGrhOrdreCalculSalaire p = periodes.get(idx);
            YvsBaseExercice exercice = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), p.getDebutMois()});
            if (exercice != null ? exercice.getId() > 0 : false) {
                Map<String, Object> param_ = new HashMap<>();
                if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
                    String ids = null;
                    for (YvsAgences a : agenceSelect) {
                        if (ids != null ? ids.trim().length() > 0 : false) {
                            ids += "," + a.getId();
                        } else {
                            ids = "" + a.getId();
                        }
                    }
                    param_.put("ID_AGENCE", ids);
                    param_.put("MULTIPLE", agenceSelect.size() > 1);
                } else {
                    param_.put("ID_AGENCE", idAgence + "");
                    param_.put("MULTIPLE", false);
                }
                if (selectPeriode != null ? selectPeriode.size() > 1 : false) {
                    String ids = null;
                    for (YvsGrhOrdreCalculSalaire y : selectPeriode) {
                        if (ids != null ? ids.trim().length() > 0 : false) {
                            ids += "," + y.getId();
                        } else {
                            ids = "" + y.getId();
                        }
                    }
                    param_.put("HEADER", ids);
                    param_.put("DATE_FIN", selectPeriode.get(selectPeriode.size() - 1).getFinMois());
                } else {
                    param_.put("HEADER", periode + "");
                    param_.put("DATE_FIN", p.getFinMois());
                }
                YvsGrhEmployes e1 = new YvsGrhEmployes(emp1);
                YvsGrhEmployes e2 = new YvsGrhEmployes(emp2);
                param_.put("matricule1", (employes.contains(e1)) ? employes.get(employes.indexOf(e1)).getMatricule() : "");
                param_.put("matricule2", (employes.contains(e2)) ? employes.get(employes.indexOf(e2)).getMatricule() : "");
                param_.put("auteur", currentUser.getUsers().getNomUsers());
                param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
                if (etatAnnuel) {
                    param_.put("DATE_DEBUT", exercice.getDateDebut());
                    executeReport((extension.equals("pdf") ? "livre_de_paie_annuel" : "livre_de_paie_annuel_excel"), param_, "", extension);
                } else {
                    param_.put("DATE_DEBUT", p.getDebutMois());
                    executeReport((extension.equals("pdf") ? "livre_de_paie" : "livre_de_paie_excel"), param_, "", extension);
                }
            }
        }
    }

    public void exporteLivrePayeService(String extension) {
        int idx = periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode));
        if (idx > -1) {
            YvsGrhOrdreCalculSalaire p = periodes.get(idx);
            YvsBaseExercice y = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), p.getDebutMois()});
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param_ = new HashMap<>();
                YvsGrhEmployes e1 = new YvsGrhEmployes(emp1);
                YvsGrhEmployes e2 = new YvsGrhEmployes(emp2);
                param_.put("matricule1", (employes.contains(e1)) ? employes.get(employes.indexOf(e1)).getMatricule() : "");
                param_.put("matricule2", (employes.contains(e2)) ? employes.get(employes.indexOf(e2)).getMatricule() : "");
                param_.put("auteur", currentUser.getUsers().getNomUsers());
                param_.put("HEADER", periode + "");
                if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
                    String ids = null;
                    for (YvsAgences a : agenceSelect) {
                        if (ids != null ? ids.trim().length() > 0 : false) {
                            ids += "," + a.getId();
                        } else {
                            ids = "" + a.getId();
                        }
                    }
                    param_.put("ID_AGENCE", ids);
                    param_.put("MULTIPLE", agenceSelect.size() > 1);
                } else {
                    param_.put("ID_AGENCE", idAgence + "");
                    param_.put("MULTIPLE", false);
                }
                param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
                param_.put("DATE_FIN", p.getFinMois());
                if (etatAnnuel) {
                    param_.put("DATE_DEBUT", y.getDateDebut());
                    executeReport((extension.equals("pdf") ? "livre_de_paie_service_annuel" : "livre_de_paie_service_annuel_excel"), param_, "", extension);
                } else {
                    param_.put("DATE_DEBUT", p.getDebutMois());
                    executeReport((extension.equals("pdf") ? "livre_de_paie_service" : "livre_de_paie_service_excel"), param_, "", extension);
                }
            }
        }
    }

    public void exporteRecapPaye(String extension) {
        Map<String, Object> param_ = new HashMap<>();
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            String agences = "";
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
                agences += "," + a.getCodeagence();
            }
            param_.put("ID_AGENCE", ids);
            param_.put("AGENCE", agences);
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            ManagedAgence m = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (m != null) {
                param_.put("AGENCE", (m.getListAgence().get(m.getListAgence().indexOf(new YvsAgences(idAgence))).getDesignation()));
            }
            param_.put("MULTIPLE", false);
        }
        param_.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
        param_.put("SOCIETE", currentAgence.getSociete().getName());
        int idx = periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode));
        if (idx > -1) {
            param_.put("PERIODE", periodes.get(idx).getReference());
            param_.put("HEADER", periode);
            executeReport("recap_paye", param_, "", extension);
        }
    }

    public void exporteEtatCotisation(String extension) {
        YvsGrhEmployes e1 = new YvsGrhEmployes(emp1);
        YvsGrhEmployes e2 = new YvsGrhEmployes(emp2);
        Map<String, Object> param_ = new HashMap<>();
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
            }
            param_.put("ID_AGENCE", ids);
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            param_.put("MULTIPLE", false);
        }
        param_.put("HEADER", periode + "");
        param_.put("DATE_DEBUT", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getDebutMois());
        param_.put("DATE_FIN", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getFinMois());
        param_.put("MAT_INIT", (employes.contains(e1)) ? employes.get(employes.indexOf(e1)).getMatricule() : "");
        param_.put("MAT_FIN", (employes.contains(e2)) ? employes.get(employes.indexOf(e2)).getMatricule() : "");
        executeReport("resume_cotisation", param_, "", extension);
    }

    public void exporteEtatRecap2(String extension) {
        Map<String, Object> param_ = new HashMap<>();
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
            }
            param_.put("ID_AGENCE", ids);
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            param_.put("MULTIPLE", false);
        }
        param_.put("ID_HEADER", (int) periode);
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
        param_.put("PERIODE", periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode))).getReference());
        executeReport("recapitulatif_paie", param_, "", extension);
    }

    public void exporteJournaPaie(String etat, String extension) {
        Map<String, Object> param_ = new HashMap<>();
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            String ids = null;
            for (YvsAgences a : agenceSelect) {
                if (ids != null ? ids.trim().length() > 0 : false) {
                    ids += "," + a.getId();
                } else {
                    ids = "" + a.getId();
                }
            }
            param_.put("ID_AGENCE", ids);
            param_.put("MULTIPLE", agenceSelect.size() > 1);
        } else {
            param_.put("ID_AGENCE", idAgence + "");
            param_.put("MULTIPLE", false);
        }
        param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param_.put("ID_HEADER", (int) periode);
        param_.put("ID_SOCIETE", currentAgence.getSociete().getId().intValue());
        YvsGrhOrdreCalculSalaire header = periodes.get(periodes.indexOf(new YvsGrhOrdreCalculSalaire(periode)));
        param_.put("DATE_DEBUT", header.getDateDebutTraitement());
        param_.put("DATE_FIN", header.getDateFinTraitement());
        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
        executeReport(etat, param_, "", extension);
    }

    public void downloadDataMasseSalarial() {

    }

    public void loadDataMasseSalarial() {
        String agences = null;
        if (agenceSelect != null ? !agenceSelect.isEmpty() : false) {
            for (YvsAgences a : agenceSelect) {
                if (agences != null ? agences.trim().length() > 0 : false) {
                    agences += "," + a.getId();
                } else {
                    agences = "" + a.getId();
                }
            }
        } else {
            if (idAgence > 0) {
                agences = idAgence + "";
            }
        }

        String ordres = null;
        if (selectPeriode != null ? !selectPeriode.isEmpty() : false) {
            for (YvsGrhOrdreCalculSalaire a : selectPeriode) {
                if (ordres != null ? ordres.trim().length() > 0 : false) {
                    ordres += "," + a.getId();
                } else {
                    ordres = "" + a.getId();
                }
            }
        } else {
            if (periode > 0) {
                ordres = periode + "";
            }
        }

        masseSalariale.returnMasseSalarial(currentAgence.getSociete().getId(), agences, ordres, dao);
    }

    @Override
    public void resetFiche() {
    }

    public void searchElement() {
        if (codeEltSearch != null ? !codeEltSearch.trim().isEmpty() : false) {
            List<YvsGrhElementSalaire> l=new ArrayList<>();
            for(YvsGrhElementSalaire e:allElements){
                if(e.getCode().toUpperCase().contains(codeEltSearch.trim().toUpperCase()) || e.getNom().toUpperCase().contains(codeEltSearch.toUpperCase())){
                    l.add(e);
                }
            }
            sourceElts.clear();
            sourceElts.addAll(l);
        }else{
            sourceElts.clear();
            sourceElts.addAll(allElements);
        }
    }
}

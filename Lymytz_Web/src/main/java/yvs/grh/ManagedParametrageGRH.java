/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.taches.YvsGrhIntervalMajoration;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsGrhMajorationConge;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.grh.majoration.IntervalMajoration;
import yvs.grh.majoration.MajorationConge;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Utilitaire;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedParametrageGRH extends Managed<ParametrageGRH, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{parametrageGRH}")
    ParametrageGRH parametrageGRH;
    ParametrageGRH svgParam = new ParametrageGRH();
    JoursOuvres jourOuvres = new JoursOuvres();
    private JoursOuvres lundi = new JoursOuvres(), mardi = new JoursOuvres(), mercredi = new JoursOuvres(),
            jeudi = new JoursOuvres(), vendredi = new JoursOuvres(), samedi = new JoursOuvres(), dimanche = new JoursOuvres();
    private boolean activBtnSave = false, updateJoursOuvree, updateMajoration,
            vueListeIntervaMajoration, updateIntervaMajoration, displayJour, updateCalendrier;
    private String position, nameBtnAddJour = "Ajouter", dureePause = "00:00";
    private Integer idTemp = 0;
    private Calendrier calendrier = new Calendrier();
    private List<YvsJoursOuvres> listJoursOuvree;
    private List<YvsGrhMajorationConge> listMajorationConge, listSelectMajorationConge;
    private List<IntervalMajoration> listIntervalMajoration, listSelectIntervalMajoration;
    private MajorationConge majorationConge = new MajorationConge();
    private IntervalMajoration intervalMajoration = new IntervalMajoration();

    YvsGrhMajorationConge entityMajorationConge;
    private TabView tw;

    public ManagedParametrageGRH() {
//        listParam = new ArrayList<>();
        listJoursOuvree = new ArrayList<>();
        listMajorationConge = new ArrayList<>();
        listIntervalMajoration = new ArrayList<>();
        listSelectMajorationConge = new ArrayList<>();
        listSelectIntervalMajoration = new ArrayList<>();
        tw = new TabView();
        for (String st : tw.getEventNames()) {

        }
    }

    public boolean isUpdateCalendrier() {
        return updateCalendrier;
    }

    public void setUpdateCalendrier(boolean updateCalendrier) {
        this.updateCalendrier = updateCalendrier;
    }

    public boolean isDisplayJour() {
        return displayJour;
    }

    public void setDisplayJour(boolean displayJour) {
        this.displayJour = displayJour;
    }

    public boolean isUpdateIntervaMajoration() {
        return updateIntervaMajoration;
    }

    public void setUpdateIntervaMajoration(boolean updateIntervaMajoration) {
        this.updateIntervaMajoration = updateIntervaMajoration;
    }

    public boolean isUpdateMajoration() {
        return updateMajoration;
    }

    public List<IntervalMajoration> getListSelectIntervalMajoration() {
        return listSelectIntervalMajoration;
    }

    public void setListSelectIntervalMajoration(List<IntervalMajoration> listSelectIntervalMajoration) {
        this.listSelectIntervalMajoration = listSelectIntervalMajoration;
    }

    public boolean isVueListeIntervaMajoration() {
        return vueListeIntervaMajoration;
    }

    public void setVueListeIntervaMajoration(boolean vueListeIntervaMajoration) {
        this.vueListeIntervaMajoration = vueListeIntervaMajoration;
    }

    public List<YvsGrhMajorationConge> getListSelectMajorationConge() {
        return listSelectMajorationConge;
    }

    public void setListSelectMajorationConge(List<YvsGrhMajorationConge> listSelectMajorationConge) {
        this.listSelectMajorationConge = listSelectMajorationConge;
    }

    public void setUpdateMajoration(boolean updateMajoration) {
        this.updateMajoration = updateMajoration;
    }

    public List<IntervalMajoration> getListIntervalMajoration() {
        return listIntervalMajoration;
    }

    public void setListIntervalMajoration(List<IntervalMajoration> listIntervalMajoration) {
        this.listIntervalMajoration = listIntervalMajoration;
    }

    public List<YvsGrhMajorationConge> getListMajorationConge() {
        return listMajorationConge;
    }

    public void setListMajorationConge(List<YvsGrhMajorationConge> listMajorationConge) {
        this.listMajorationConge = listMajorationConge;
    }

    public MajorationConge getMajorationConge() {
        return majorationConge;
    }

    public void setMajorationConge(MajorationConge majorationConge) {
        this.majorationConge = majorationConge;
    }

    public IntervalMajoration getIntervalMajoration() {
        return intervalMajoration;
    }

    public void setIntervalMajoration(IntervalMajoration intervalMajoration) {
        this.intervalMajoration = intervalMajoration;
    }

    public Integer getIdTemp() {
        return idTemp;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public String getDureePause() {
        return dureePause;
    }

    public void setDureePause(String dureePause) {
        this.dureePause = dureePause;
    }

    public JoursOuvres getLundi() {
        return lundi;
    }

    public void setLundi(JoursOuvres lundi) {
        this.lundi = lundi;
    }

    public JoursOuvres getMardi() {
        return mardi;
    }

    public void setMardi(JoursOuvres mardi) {
        this.mardi = mardi;
    }

    public JoursOuvres getMercredi() {
        return mercredi;
    }

    public void setMercredi(JoursOuvres mercredi) {
        this.mercredi = mercredi;
    }

    public JoursOuvres getJeudi() {
        return jeudi;
    }

    public void setJeudi(JoursOuvres jeudi) {
        this.jeudi = jeudi;
    }

    public JoursOuvres getVendredi() {
        return vendredi;
    }

    public void setVendredi(JoursOuvres vendredi) {
        this.vendredi = vendredi;
    }

    public JoursOuvres getSamedi() {
        return samedi;
    }

    public void setSamedi(JoursOuvres samedi) {
        this.samedi = samedi;
    }

    public JoursOuvres getDimanche() {
        return dimanche;
    }

    public void setDimanche(JoursOuvres dimanche) {
        this.dimanche = dimanche;
    }

    public void setIdTemp(Integer idTemp) {
        this.idTemp = idTemp;
    }

    public ParametrageGRH getSvgParam() {
        return svgParam;
    }

    public boolean isUpdateJoursOuvree() {
        return updateJoursOuvree;
    }

    public void setUpdateJoursOuvree(boolean updateJoursOuvree) {
        this.updateJoursOuvree = updateJoursOuvree;
    }

    public String getNameBtnAddJour() {
        return nameBtnAddJour;
    }

    public void setNameBtnAddJour(String nameBtnAddJour) {
        this.nameBtnAddJour = nameBtnAddJour;
    }

    public void setSvgParam(ParametrageGRH svgParam) {
        this.svgParam = svgParam;
    }

    public JoursOuvres getJourOuvres() {
        return jourOuvres;
    }

    public void setJourOuvres(JoursOuvres jourOuvres) {
        this.jourOuvres = jourOuvres;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<YvsJoursOuvres> getListJoursOuvree() {
        return listJoursOuvree;
    }

    public void setListJoursOuvree(List<YvsJoursOuvres> listJoursOuvree) {
        this.listJoursOuvree = listJoursOuvree;
    }

    public ParametrageGRH getParametrageGRH() {
        return parametrageGRH;
    }

    public void setParametrageGRH(ParametrageGRH parametrageGRH) {
        this.parametrageGRH = parametrageGRH;
    }

    public boolean isActivBtnSave() {
        return activBtnSave;
    }

    public void setActivBtnSave(boolean activBtnSave) {
        this.activBtnSave = activBtnSave;
    }
//    public List<ParametrageGRH> getListParam() {
//        return listParam;
//    }
//
//    public void setListParam(List<ParametrageGRH> listParam) {
//        this.listParam = listParam;
//    }

    private YvsJoursOuvres buildJoursOuvree(JoursOuvres j) {
        YvsJoursOuvres jour = new YvsJoursOuvres();
        if (j != null) {
            jour.setId(j.getId());
            jour.setActif(true);
            jour.setDureePause(j.getDureePause());
            jour.setHeureDebutPause((j.getHeureDebutPause() != null) ? j.getHeureDebutPause() : new Date());
            jour.setHeureDebutTravail((j.getHeureDebutTravail() != null) ? j.getHeureDebutTravail() : new Date());
            jour.setHeureFinPause((j.getHeureFinPause() != null) ? j.getHeureFinPause() : new Date());
            jour.setHeureFinTravail((j.getHeureFinTravail() != null) ? j.getHeureFinTravail() : new Date());
            jour.setJour(j.getJour());
            jour.setOuvrable(j.isOuvrable());
            jour.setCalendrier(entityCalender);
            jour.setAuthor(currentUser);
            jour.setDateSave(new Date());
            jour.setDateUpdate(new Date());
        }
        return jour;
    }

    YvsCalendrier entityCalender;

    private YvsParametreGrh buildParametrage(ParametrageGRH p) {
        YvsParametreGrh param = new YvsParametreGrh();
        param.setId(p.getId());
        param.setActif(p.isActif());
//        param.setDateDebutExercice(p.getDateDebutExercice());
//        param.setDateFinExercice(p.getDateFinExercice());
        param.setDatePaiementSalaire(p.getDatePaiementSalaire());
        param.setDureeCumuleConge(p.getDureeCumulConge());
        param.setLimitHeureSup(p.getLimitHeureSup());
        param.setEchellonageAutomatique(p.isEchellonage());
        param.setPeriodeDavancement(p.getPeriodeDavancement());
        param.setPeriodePremierAvancement(p.getPeriodePremierAvancement());
        param.setNombreMoisAvanceMaxRetenue(p.getNombreMoisAvanceMaxRetenue());
        param.setSociete(currentAgence.getSociete());
        param.setDateDebutTraitementSalaire((p.getDateDebutTraitementSalaire() != null) ? p.getDateDebutTraitementSalaire() : new Date());
        param.setSupp(p.isSupp());
        param.setTotalCongePermis(p.getTotaCongePermis());
        param.setTotalHeureTravailHebd(p.getTotalHeureTravailHebdo());
        param.setPositionBaseSalaire(p.getPositionBaseSalaire());
        param.setCalculPlaningDynamique(p.isCalculPlaningDynamique());
        param.setDureeRetardAutorise(p.getDureeRetardAutorise());
        param.setHeureMinimaleRequise(p.getHeureMinimaleRequise());
        param.setTimeMargeAvance(p.getTimeMargeAvance());
        param.setDelaisSasiePointage(p.getEcartSaisiPointage());
        param.setDelaisValidationPointage(p.getEcartValideFiche());
        param.setHeureDebutPause(p.getHeureDebutPause());
        param.setHeureDebutTravail(p.getHeureDebutTravail());
        param.setHeureFinPause(p.getHeureFinPause());
        param.setHeureFinTravail(p.getHeureFinTravail());
        param.setDateSave(p.getDateSave());
        param.setDateUpdate(new Date());

        if ((entityCalender != null) ? entityCalender.getId() != 0 : false) {
            param.setCalendrier(entityCalender);
        }
        return param;
    }

    public YvsCalendrier buildCalendrier(Calendrier c) {
        YvsCalendrier cal = new YvsCalendrier();
        if (c != null) {
            cal.setId(c.getId());
            cal.setReference(c.getReference());
            cal.setDefaut(true);
            cal.setSociete(currentAgence.getSociete());
            cal.setAuthor(currentUser);
            cal.setDateSave(c.getDateSave());
        }
        return cal;
    }

    public YvsGrhMajorationConge buildMajorationConge(MajorationConge m) {
        YvsGrhMajorationConge c = new YvsGrhMajorationConge();
        if (m != null) {
            c.setId(m.getId());
            c.setActif(true);
            c.setNature(m.getNature());
            c.setSociete(currentAgence.getSociete());
            c.setAuthor(currentUser);
        }
        return c;
    }

    public YvsGrhIntervalMajoration buildIntervalMajoration(IntervalMajoration i) {
        YvsGrhIntervalMajoration r = new YvsGrhIntervalMajoration();
        if (i != null) {
            r.setId(i.getId());
            r.setActif(true);
            r.setNbJour(i.getNbreJour());
            r.setValeurMaximal(i.getValeurMaximal());
            r.setValeurMinimal(i.getValeurMinimal());
            r.setMajorationConge(listMajorationConge.get(listMajorationConge.indexOf(new YvsGrhMajorationConge(majorationConge.getId()))));
            r.setAuthor(currentUser);
        }
        return r;
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        YvsParametreGrh entityParam = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", champ, val);
        if (entityParam != null) {
            cloneObject(parametrageGRH, UtilGrh.buildBeanParametrageGRH(entityParam));
            svgParam = recopieView();
            initialiseJoursOuvres(entityParam.getCalendrier());
        } else {
            YvsCalendrier calendrier = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (calendrier != null) {
                initialiseJoursOuvres(calendrier);
            }
        }
        initTypeMajorationConge();
    }

    public void initialiseJoursOuvres(YvsCalendrier cal) {
        listJoursOuvree.clear();
        if (cal != null) {
            setUpdateCalendrier(true);
            cloneObject(calendrier, parametrageGRH.getCalendrier());
            entityCalender = cal;
            Calendrier c = new Calendrier();
            cloneObject(c, calendrier);
            listJoursOuvree.addAll(cal.getJoursOuvres());
            parametrageGRH.setTotalHeureTravailHebdo(0.0);
            if (listJoursOuvree != null) {
                for (YvsJoursOuvres j : listJoursOuvree) {
                    switch (j.getJour()) {
                        case "Lundi":
                            cloneObject(lundi, UtilGrh.buildBeanJoursOuvree(j));
                            lundi.setVu(true);
                            j.setOrdre(1);
                            break;
                        case "Mardi":
                            cloneObject(mardi, UtilGrh.buildBeanJoursOuvree(j));
                            mardi.setVu(true);
                            j.setOrdre(2);
                            break;
                        case "Mercredi":
                            cloneObject(mercredi, UtilGrh.buildBeanJoursOuvree(j));
                            mercredi.setVu(true);
                            j.setOrdre(3);
                            break;
                        case "Jeudi":
                            cloneObject(jeudi, UtilGrh.buildBeanJoursOuvree(j));
                            jeudi.setVu(true);
                            j.setOrdre(4);
                            break;
                        case "Vendredi":
                            cloneObject(vendredi, UtilGrh.buildBeanJoursOuvree(j));
                            vendredi.setVu(true);
                            j.setOrdre(5);
                            break;
                        case "Samedi":
                            cloneObject(samedi, UtilGrh.buildBeanJoursOuvree(j));
                            samedi.setVu(true);
                            j.setOrdre(6);
                            break;
                        case "Dimanche":
                            cloneObject(dimanche, UtilGrh.buildBeanJoursOuvree(j));
                            dimanche.setVu(true);
                            j.setOrdre(7);
                            break;
                    }
                    calculDureeTravailAdd(j);
//                    getListJoursOuvree().get(getListJoursOuvree().indexOf(j)).setVu(vu);
                }
            }
            Collections.sort(listJoursOuvree, new YvsJoursOuvres());
            update("bloc-param-01:bloc_jour_travail");
            update("bloc-param-01:tab-recapilatif-param");
        }
    }

//    private void reodonneListJour() {
//        List<JoursOuvres> l = new ArrayList<>();
//        JoursOuvres j = findDay("Lundi");
//        if (j != null) {
//            l.add(0, j);
//        } else {
//            l.add(0, null);
//        }
//        j = findDay("Mardi");
//        if (j != null) {
//            l.add(1, j);
//        } else {
//            l.add(1, null);
//        }
//        j = findDay("Mercredi");
//        if (j != null) {
//            l.add(2, j);
//        } else {
//            l.add(2, null);
//        }
//        j = findDay("Jeudi");
//        if (j != null) {
//            l.add(3, j);
//        } else {
//            l.add(3, null);
//        }
//        j = findDay("Vendredi");
//        if (j != null) {
//            l.add(4, j);
//        } else {
//            l.add(4, null);
//        }
//        j = findDay("Samedi");
//        if (j != null) {
//            l.add(5, j);
//        } else {
//            l.add(5, null);
//        }
//        j = findDay("Dimanche");
//        if (j != null) {
//            l.add(6, j);
//        } else {
//            l.add(6, null);
//        }
//        listJoursOuvree.clear();
//        for (JoursOuvres jj : l) {
//            if (jj != null) {
//                listJoursOuvree.add(jj);
//            }
//        }
//    }
//    private JoursOuvres findDay(String day) {
//        for (JoursOuvres j : listJoursOuvree) {
//            if (j.getJour().equals(day)) {
//                return j;
//            }
//        }
//        return null;
//    }
    public void loadAllMajoration() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listMajorationConge = dao.loadNameQueries("YvsMajorationConge.findAll", champ, val);
    }

    public void saveNewCalendrier() {
        if (controleFicheCalendrier(parametrageGRH.getCalendrier())) {
            champ = new String[]{"reference", "societe"};
            val = new Object[]{parametrageGRH.getCalendrier().getReference(), currentAgence.getSociete()};
            YvsCalendrier Ecal = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByReference", champ, val);
            Calendrier cal = UtilGrh.buildBeanCalendrier(Ecal);
            if ((cal != null) ? cal.getId() != 0 : false) {
                cloneObject(parametrageGRH.getCalendrier(), cal);
                initialiseJoursOuvres(Ecal);
            } else {
                entityCalender = buildCalendrier(parametrageGRH.getCalendrier()); //fabrique un objet calendrier
                if (!isUpdateCalendrier()) {
                    entityCalender.setId(null);
                    entityCalender = (YvsCalendrier) dao.save1(entityCalender);
                    parametrageGRH.getCalendrier().setId(entityCalender.getId());
                    parametrageGRH.getCalendrier().setListJoursOuvres(new ArrayList<YvsJoursOuvres>());
                } else {
                    dao.update(entityCalender);
                }
            }
            setUpdateCalendrier(true);
            setDisplayJour(true);
            update("bloc-param-01:bloc_jour_travail");
        }
    }

    public void saveNewJoursOuvree() {
        if (controleFicheJoursOUvree(jourOuvres)) {
            YvsJoursOuvres entityJourOuvree = buildJoursOuvree(jourOuvres);
            if (!updateJoursOuvree) {
                entityJourOuvree.setDateSave(new Date());
                entityJourOuvree.setId(null);
                entityJourOuvree = (YvsJoursOuvres) dao.save1(entityJourOuvree);
                jourOuvres.setId(entityJourOuvree.getId().intValue());
                parametrageGRH.getCalendrier().getListJoursOuvres().add(entityJourOuvree);
                listJoursOuvree.add(entityJourOuvree);
            } else {
                dao.update(entityJourOuvree);
                parametrageGRH.getCalendrier().getListJoursOuvres().set(parametrageGRH.getCalendrier().getListJoursOuvres().indexOf(entityJourOuvree), entityJourOuvree);
                listJoursOuvree.set(listJoursOuvree.indexOf(entityJourOuvree), entityJourOuvree);
            }
            update("bloc-param-01:bloc_jour_travail");
            addJoursOuvres();
        }
    }

    public void addJoursOuvres() {
        switch (jourOuvres.getJour()) {
            case "Lundi":
                cloneObject(lundi, jourOuvres);
                lundi.setVu(true);
                break;
            case "Mardi":
                cloneObject(mardi, jourOuvres);
                mardi.setVu(true);
                break;
            case "Mercredi":
                cloneObject(mercredi, jourOuvres);
                mercredi.setVu(true);
                break;
            case "Jeudi":
                cloneObject(jeudi, jourOuvres);
                jeudi.setVu(true);
                break;
            case "Vendredi":
                cloneObject(vendredi, jourOuvres);
                vendredi.setVu(true);
                break;
            case "Samedi":
                cloneObject(samedi, jourOuvres);
                samedi.setVu(true);
                break;
            case "Dimanche":
                cloneObject(dimanche, jourOuvres);
                dimanche.setVu(true);
                break;
        }
//        resetFiche();
        update("bloc-param-01:tab_travail");
        update("bloc-param-01:tab_jours_ouvree");
        update("bloc-param-01:tab-recapilatif-param");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(parametrageGRH)) {
            paramGrh = buildParametrage(parametrageGRH);
            paramGrh.setDefaultHoraire(true);
            if (parametrageGRH.getId() <= 0) {
                //SAVE PARAMETRES GRH
                paramGrh.setId(null);

                paramGrh = (YvsParametreGrh) dao.save1(paramGrh);
                parametrageGRH.setId(paramGrh.getId());
            } else {
                //UPDATE PARAMETRES GRH
                dao.update(paramGrh);
            }
            succes();
            update("bloc-param-01");
        }
        return true;
    }

    public void deleteParam() {
        try {
            dao.delete(new YvsParametreGrh(parametrageGRH.getId()));
            activBtnSave = false;
            update("bloc-en-tete-param");
            update("bloc-param");
        } catch (Exception ex) {
            getMessage("Impossible de supprimer ces paramètres!!!!", FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(ManagedParametrageGRH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void annuler() {
        if (parametrageGRH.getId() <= 0) {
            resetFiche(parametrageGRH);
            update("bloc-en-tete-param");
            update("bloc-param");
        } else {
            cloneObject(parametrageGRH, svgParam);
            if (svgParam.getCalendrier().getListJoursOuvres() != null) {
                for (YvsJoursOuvres j : svgParam.getCalendrier().getListJoursOuvres()) {
                    switch (j.getJour()) {
                        case "Lundi":
                            cloneObject(lundi, j);
                            lundi.setVu(true);
                            break;
                        case "Mardi":
                            cloneObject(mardi, j);
                            mardi.setVu(true);
                            break;
                        case "Mercredi":
                            cloneObject(mercredi, j);
                            mercredi.setVu(true);
                            break;
                        case "Jeudi":
                            cloneObject(jeudi, j);
                            jeudi.setVu(true);
                            break;
                        case "Vendredi":
                            cloneObject(vendredi, j);
                            vendredi.setVu(true);
                            break;
                        case "Samedi":
                            cloneObject(samedi, j);
                            samedi.setVu(true);
                            break;
                        case "Dimanche":
                            cloneObject(dimanche, j);
                            dimanche.setVu(true);
                            break;
                    }
                }
            }
            listJoursOuvree.clear();
            listJoursOuvree.addAll(svgParam.getCalendrier().getListJoursOuvres());
            update("bloc-en-tete-param");
            update("bloc-param-01");
            update("bloc-param-01:tab-recapilatif-param");
            update("bloc-param-01:detail_jour_travail");
        }
    }

    public void confirm() {
        openDialog("dlgDeleteParam");
    }

    public void onTabChange(TabChangeEvent event) {
//        System.err.println("--- "+event);
//        if ((event != null) ? ((event.getTab() != null) ? event.getTab().getId() != null : false) : false) {
//            setActivBtnSave(("confirm".equals(event.getTab().getId())));
//            setPosition(event.getTab().getId());
//            update("bloc-en-tete-param");
//            update("bloc-param-01:tab-recapilatif-param");
//        }
    }

    public void calculDureeTravailAdd(YvsJoursOuvres bean) {
        System.err.println("Here calcul total hebdo");
        if (bean != null) {
//        parametrageGRH.setTotalHeureTravailHebdo((parametrageGRH.getTotalHeureTravailHebdo() != 0.0) ? parametrageGRH.getTotalHeureTravailHebdo() : 0.0);
            if (bean.getHeureFinTravail() != null && bean.getHeureDebutTravail() != null) {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() + ((double) (bean.getHeureFinTravail().getTime() - bean.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
            } else {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() + 0.0);
            }
        }
    }

    public void calculDureeTravailRemove(JoursOuvres bean) {
        if (bean != null) {
//        parametrageGRH.setTotalHeureTravailHebdo((parametrageGRH.getTotalHeureTravailHebdo() != 0.0) ? parametrageGRH.getTotalHeureTravailHebdo() : 0.0);
            if (bean.getHeureFinTravail() != null && bean.getHeureDebutTravail() != null) {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() - ((double) (bean.getHeureFinTravail().getTime() - bean.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
            } else {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() - 0.0);
            }
        }
    }

    public void eventCalculDureeTravail() {
        if (jourOuvres != null) {
            if (jourOuvres.getHeureFinTravail() != null && jourOuvres.getHeureDebutTravail() != null) {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() + ((double) (jourOuvres.getHeureFinTravail().getTime() - jourOuvres.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
            } else {
                parametrageGRH.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo() + 0.0);
            }
        }
    }

    public void calculDureePause() {
        if (jourOuvres.getHeureFinPause() != null && jourOuvres.getHeureDebutPause() != null) {
            jourOuvres.setDureePause(Utilitaire.calculDureeBetweenDate(jourOuvres.getHeureDebutPause(), jourOuvres.getHeureFinPause()));
        } else {
            jourOuvres.setDureePause(new Date(0));
        }
        update("bloc-param-01:detail_jour_travail_fp");
    }

    @Override
    public boolean controleFiche(ParametrageGRH bean) {
        return true;
    }

    public boolean controleFicheJoursOUvree(JoursOuvres bean) {
        if (bean.getDureePause() == null) {
            getMessage("Vous devez entrer la duree de la pause", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureDebutPause() == null) {
            getMessage("Vous devez entrer l'heure de debut de la pause", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureDebutTravail() == null) {
            getMessage("Vous devez entrer le nombre total de jours de congés permis", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureFinPause() == null) {
            getMessage("Vous devez entrer l'heure de fin de la pause", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getHeureFinTravail() == null) {
            getMessage("Vous devez entrer l'heure de fin du travail", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

//    public boolean controleFicheMajoration(MajorationConge bean) {
//        if (bean.getNature() == null || "".equals(bean.getNature())) {
//            getMessage("Vous devez entrer la nature", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
//        return true;
//    }
    public boolean controleFicheCalendrier(Calendrier bean) {
        if (bean.getReference() == null || "".equals(bean.getReference())) {
            getErrorMessage("Vous devez entrer la reference du calendrier");
            listJoursOuvree.clear();
            update("bloc-param-01:tab_travail");
            update("bloc-param-01:tab-recapilatif-param");
            return false;
        }
        return true;
    }

    public boolean controleFicheInterval(IntervalMajoration bean) {
        if (majorationConge.getId() <= 0) {
            getMessage("Vous devez specifier la majoration", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getNbreJour() <= 0) {
            getMessage("Vous devez entrer le nombre de jour", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (bean.getValeurMaximal() <= 0) {
            getMessage("Vous devez entrer la valeur maximale", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ParametrageGRH recopieView() {
        ParametrageGRH param = new ParametrageGRH();
        param.setId(parametrageGRH.getId());
        param.setActif(parametrageGRH.isActif());
        param.setDatePaiementSalaire(parametrageGRH.getDatePaiementSalaire());
        param.setDureeCumulConge(parametrageGRH.getDureeCumulConge());
        param.setLimitHeureSup(parametrageGRH.getLimitHeureSup());
        param.setEchellonage(parametrageGRH.isEchellonage());
        param.setPeriodeDavancement(parametrageGRH.getPeriodeDavancement());
        param.setPeriodePremierAvancement(parametrageGRH.getPeriodePremierAvancement());
        param.setSociete(parametrageGRH.getSociete());
        param.setSupp(parametrageGRH.isSupp());
        param.setTotaCongePermis(parametrageGRH.getTotaCongePermis());
        param.setTotalHeureTravailHebdo(parametrageGRH.getTotalHeureTravailHebdo());
        param.setHeureDebutPause(parametrageGRH.getHeureDebutPause());
        param.setHeureDebutTravail(parametrageGRH.getHeureDebutTravail());
        param.setHeureFinPause(parametrageGRH.getHeureFinPause());
        param.setHeureFinTravail(parametrageGRH.getHeureFinTravail());
        param.setDateSave(parametrageGRH.getDateSave());
        Calendrier cal = new Calendrier();
        cloneObject(cal, parametrageGRH.getCalendrier());
        param.setCalendrier(cal);
        return param;
    }

    @Override
    public void populateView(ParametrageGRH bean) {

    }

    public void populateViewJours(JoursOuvres bean) {
        cloneObject(jourOuvres, bean);
    }

    public void populateIntervalMajoartion(IntervalMajoration bean) {
        cloneObject(intervalMajoration, bean);
    }

    public void selectMajoration(ValueChangeEvent ev) {
        if (ev != null) {
            //charge les intervalle de cette majoration
            long id = (Long) ev.getNewValue();
            if (id > 0) {
                majorationConge = UtilGrh.buildBeanMajorationConge(listMajorationConge.get(listMajorationConge.indexOf(new YvsGrhMajorationConge(id))));
            } else if (id == -1) {
                //ouvre la boite d'ajout d'un critère

            } else {
                majorationConge.getListIntervalle().clear();
            }
        }
        setUpdateMajoration(!listSelectMajorationConge.isEmpty());
        update("bloc-param-01:bloc-param-02:panel_param_majoration");
        update("bloc-param-01:bloc-param-02:tab_interval_majoration");
    }

    public void selectInterval(IntervalMajoration bean) {
        if (listSelectIntervalMajoration.contains(bean)) {
            listSelectIntervalMajoration.remove(bean);
        } else {
            listSelectIntervalMajoration.add(bean);
        }
        if (listSelectIntervalMajoration.isEmpty()) {
            resetFicheIntervalMajoration();
        } else {
            populateIntervalMajoartion(listSelectIntervalMajoration.get(listSelectIntervalMajoration.size() - 1));
        }
        setUpdateIntervaMajoration(!listSelectIntervalMajoration.isEmpty());
        update("bloc-param-01:bloc-param-02:tab_interval_majoration");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
//        if (ev != null) {
//            MajorationConge bean = (MajorationConge) ev.getObject();
//            if (bean.getNature().equals(Constantes.GRH_MERE)) {
//                labelMajoration = "Age";
//            } else if (bean.getNature().equals(Constantes.GRH_ANCIENNETE)) {
//                labelMajoration = "Ancienneté";
//
//            }
//            selectMajoration(bean);
//        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
//        if (ev != null) {
//            MajorationConge bean = (MajorationConge) ev.getObject();
//            selectMajoration(bean);
//        }
    }
    String labelMajoration;

    public String getLabelMajoration() {
        return labelMajoration;
    }

    public void setLabelMajoration(String labelMajoration) {
        this.labelMajoration = labelMajoration;
    }

    public void loadOnViewInterval(SelectEvent ev) {
        if (ev != null) {
            YvsGrhIntervalMajoration bean = (YvsGrhIntervalMajoration) ev.getObject();
            intervalMajoration = UtilGrh.buildBeanIntervalMajoration(bean);
            updateIntervaMajoration = true;
            openDialog("dlgIntervalMajoration");
            update("dlg_interval_majoration");
        }
    }

    public void unLoadOnViewInterval(UnselectEvent ev) {
        intervalMajoration = new IntervalMajoration();
        updateIntervaMajoration = false;
    }

    public void loadOnViewJours(SelectEvent ev) {
        JoursOuvres bean = (JoursOuvres) ev.getObject();
        populateViewJours(bean);
        setNameBtnAddJour("Modifier");
        setUpdateJoursOuvree(true);
        update("bloc-param-01:detail_jour_travail");
        update("bloc-param-01:tab_jours_ouvree");
    }

    public void unLoadOnViewJours(UnselectEvent ev) {
        resetFiche();
        setUpdateJoursOuvree(false);
        setNameBtnAddJour("Ajouter");
        update("bloc-param-01:detail_jour_travail");
        update("bloc-param-01:tab_jours_ouvree");
    }

    public void deleteBeanJoursOuvreee() {
        if (jourOuvres != null) {
            dao.delete(new YvsJoursOuvres(jourOuvres.getId()));
            listJoursOuvree.remove(jourOuvres);
            switch (jourOuvres.getJour()) {
                case "Lundi":
                    resetFicheJoursOuvree(getLundi());
                    break;
                case "Mardi":
                    resetFicheJoursOuvree(getMardi());
                    break;
                case "Mercredi":
                    resetFicheJoursOuvree(getMercredi());
                    break;
                case "Jeudi":
                    resetFicheJoursOuvree(getJeudi());
                    break;
                case "Vendredi":
                    resetFicheJoursOuvree(getVendredi());
                    break;
                case "Samedi":
                    resetFicheJoursOuvree(getSamedi());
                    break;
                case "Dimanche":
                    resetFicheJoursOuvree(getDimanche());
                    break;
            }
            calculDureeTravailRemove(jourOuvres);
            resetFiche();
            setUpdateJoursOuvree(false);
            setNameBtnAddJour("Ajouter");
            update("bloc-param-01:tab-recapilatif-param");
            update("bloc-param-01:detail_jour_travail");
            update("bloc-param-01:tab_jours_ouvree");
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(jourOuvres);
        jourOuvres.setDureePause(new Date(0));
        setUpdateJoursOuvree(false);
    }

    public void resetFicheJoursOuvree(JoursOuvres bean) {
        resetFiche(bean);
        bean.setVu(false);
    }

    public void searchJoursOuvreeByJour() {
        champ = new String[]{"jour", "calendrier"};
        val = new Object[]{jourOuvres.getJour(), entityCalender};
        YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", champ, val);
        if ((jour != null) ? jour.getId() != 0 : false) {
            resetFiche();
            getMessage("Le jour " + jour.getJour() + " a déja été attribué", FacesMessage.SEVERITY_ERROR);
            update("bloc-param-01:detail_jour_travail");
        }
    }

    public String hourInString(Double hour) {
        return Utilitaire.doubleToHour(hour);
    }

    public boolean vueIsNull(String champ) {
        return ((champ != null) ? champ.equals("") : true);
    }

    public void chooseNature(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            String valeur = (String) ev.getNewValue();
            if (!valeur.equals("")) {
                champ = new String[]{"nature", "societe"};
                val = new Object[]{valeur, currentAgence.getSociete()};
                YvsGrhMajorationConge m = (YvsGrhMajorationConge) dao.loadOneByNameQueries("YvsMajorationConge.findByNatureSociete", champ, val);
                if ((m != null) ? m.getId() != 0 : false) {
                    resetFicheMajoration();
                    getMessage("Cette majoration existe deja", FacesMessage.SEVERITY_ERROR);
                }
                update("bloc-param-01:bloc-param-02:panel_param_majoration");
            }
        }
    }

    public void resetFicheMajoration() {
        resetFiche(majorationConge);
        majorationConge.setListIntervalle(new ArrayList<YvsGrhIntervalMajoration>());
        setUpdateMajoration(false);
        resetPageMajoration();
    }

    public void resetFicheIntervalMajoration() {
        resetFiche(intervalMajoration);
        intervalMajoration.setMajorationConge(new MajorationConge());
        setUpdateIntervaMajoration(false);
        resetPageIntervalMajoration();
    }

    public void resetPageMajoration() {
        listSelectMajorationConge.clear();
    }

    public void resetPageIntervalMajoration() {
        listSelectIntervalMajoration.clear();
    }
    /*Cette fonction vise à initialiser les types de majorations admis  (Mère, Ancienneté)*/

    public void initTypeMajorationConge() {
        loadAllMajoration();
        if (listMajorationConge.isEmpty()) {
            saveNewMajoration(new MajorationConge(null, Constantes.CRITERE_MAJORATION_CONGE_ANC, true, "Ans"));
            saveNewMajoration(new MajorationConge(null, Constantes.CRITERE_MAJORATION_CONGE_DAME, true, "Ans"));
        } else {

        }
    }

    public void saveNewMajoration(MajorationConge m) {
        try {
            entityMajorationConge = buildMajorationConge(m);
            entityMajorationConge.setId(null);
            entityMajorationConge = (YvsGrhMajorationConge) dao.save1(entityMajorationConge);
            listMajorationConge.add(entityMajorationConge);
            entityMajorationConge = buildMajorationConge(m);
            entityMajorationConge.setId(null);
            entityMajorationConge = (YvsGrhMajorationConge) dao.save1(entityMajorationConge);
            listMajorationConge.add(entityMajorationConge);
        } catch (Exception ex) {
            getMessage("Vous avez tenté d'ajouter une majoration qui existe deja", FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(ManagedParametrageGRH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveNewIntervalMajoration() {
        if (controleFicheInterval(intervalMajoration)) {
            YvsGrhIntervalMajoration inter = buildIntervalMajoration(intervalMajoration);
            if (!updateIntervaMajoration) {
                inter.setId(null);
                inter = (YvsGrhIntervalMajoration) dao.save1(inter);
                majorationConge.getListIntervalle().add(0, inter);
            } else {
                dao.update(inter);
                majorationConge.getListIntervalle().set(majorationConge.getListIntervalle().indexOf(inter), inter);
            }
            update("bloc-param-01:bloc-param-02:tab_interval_majoration");
        }
    }

//    public void deleteBeanMajoration() {
//        if (listSelectMajorationConge != null) {
//            if (!listSelectMajorationConge.isEmpty()) {
//                for (MajorationConge m : listSelectMajorationConge) {
//                    dao.delete(new YvsGrhMajorationConge(m.getId()));
//                    listMajorationConge.remove(m);
//                }
//                listSelectMajorationConge.clear();
//                succes();
//                resetFicheMajoration();
//                update("bloc-param-01:bloc-param-02:panel_param_majoration");
//                update("bloc-param-01:bloc-param-02:data_param_majoration");
//            }
//        }
//    }
    public void deleteBeanIntervalMajoration(YvsGrhIntervalMajoration i) {
        try {
            i.setAuthor(currentUser);
            dao.delete(i);
        } catch (Exception ex) {

            getErrorMessage("Impossible de supprimer cet élément !");
        }
        if (listSelectIntervalMajoration != null) {
            if (!listSelectIntervalMajoration.isEmpty()) {
                for (IntervalMajoration m : listSelectIntervalMajoration) {
                    dao.delete(new YvsGrhIntervalMajoration(m.getId()));
                    listIntervalMajoration.remove(m);
                }
                listSelectIntervalMajoration.clear();
                succes();
                resetFicheIntervalMajoration();
                update("dlg_interval_majoration");
                update("bloc-param-01:bloc-param-02:tab_interval_majoration");
            }
        }
    }

//    public void openDlgIntervalMajoartion() {
//        openDialog("dlgIntervalMajoration");
//        update("dlg_interval_majoration");
//    }
    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.ManagedUniteMesure;
import yvs.production.UtilProd;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.entity.compta.analytique.YvsComptaRepartitionAnalytique;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@SessionScoped
@ManagedBean
public class ManagedCentreAnalytique extends Managed<CentreAnalytique, YvsComptaCentreAnalytique> implements Serializable {

    private CentreAnalytique centre = new CentreAnalytique();
    private List<YvsComptaCentreAnalytique> centres, secondaires, centres_all;
    private YvsComptaCentreAnalytique selectCentre;

    private LiaisonCentres liaison = new LiaisonCentres();
    private YvsComptaRepartitionAnalytique selectLiaison;
    private String[] types = new String[]{Constantes.UNITE_TEMPS, Constantes.UNITE_OEUVRE};

    private String numeroSearch, niveauSearch, typeSearch;
    private Boolean actifSearch;
    private long planSearch, uniteSearch;

    public ManagedCentreAnalytique() {
        centres = new ArrayList<>();
        secondaires = new ArrayList<>();
        centres_all = new ArrayList<>();
    }

    public List<YvsComptaCentreAnalytique> getCentres_all() {
        return centres_all;
    }

    public void setCentres_all(List<YvsComptaCentreAnalytique> centres_all) {
        this.centres_all = centres_all;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public LiaisonCentres getLiaison() {
        return liaison;
    }

    public void setLiaison(LiaisonCentres liaison) {
        this.liaison = liaison;
    }

    public YvsComptaRepartitionAnalytique getSelectLiaison() {
        return selectLiaison;
    }

    public void setSelectLiaison(YvsComptaRepartitionAnalytique selectLiaison) {
        this.selectLiaison = selectLiaison;
    }

    public String getNiveauSearch() {
        return niveauSearch;
    }

    public void setNiveauSearch(String niveauSearch) {
        this.niveauSearch = niveauSearch;
    }

    public long getUniteSearch() {
        return uniteSearch;
    }

    public void setUniteSearch(long uniteSearch) {
        this.uniteSearch = uniteSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public List<YvsComptaCentreAnalytique> getSecondaires() {
        return secondaires;
    }

    public void setSecondaires(List<YvsComptaCentreAnalytique> secondaires) {
        this.secondaires = secondaires;
    }

    public long getPlanSearch() {
        return planSearch;
    }

    public void setPlanSearch(long planSearch) {
        this.planSearch = planSearch;
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public CentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(CentreAnalytique centre) {
        this.centre = centre;
    }

    public List<YvsComptaCentreAnalytique> getCentres() {
        return centres;
    }

    public void setCentres(List<YvsComptaCentreAnalytique> centres) {
        this.centres = centres;
    }

    public YvsComptaCentreAnalytique getSelectCentre() {
        return selectCentre;
    }

    public void setSelectCentre(YvsComptaCentreAnalytique selectCentre) {
        this.selectCentre = selectCentre;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        loadActif(null);
        if (centre != null ? centre.getPlan() != null ? centre.getPlan().getId() < 1 : false : false) {
            centre.getPlan().setId(0);
        }
        if (centre != null ? centre.getUniteOeuvre() != null ? centre.getUniteOeuvre().getId() < 1 : false : false) {
            centre.getUniteOeuvre().setId(0);
        }
        if (liaison != null ? liaison.getUnite() != null ? liaison.getUnite().getId() < 1 : false : false) {
            liaison.getUnite().setId(0);
        }
    }

    public void loadAll(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.plan.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        centres = paginator.executeDynamicQuery("YvsComptaCentreAnalytique", "y.codeRef, y.designation", avancer, init, dao);
        update("data_centre_analytique");
    }

    public void loadAllByNamedQuery() {
        centres = dao.loadNameQueries("YvsComptaCentreAnalytique.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllCentre() {
        centres_all = dao.loadNameQueries("YvsComptaCentreAnalytique.findAllActif", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadActif(YvsComptaCentreAnalytique y) {
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        nameQueri = "YvsComptaCentreAnalytique.findByActif";
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"societe", "actif", "id"};
            val = new Object[]{currentAgence.getSociete(), true, y.getId()};
            nameQueri = "YvsComptaCentreAnalytique.findByActifNoId";
        }
        secondaires = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsComptaCentreAnalytique> re = paginator.parcoursDynamicData("YvsComptaCentreAnalytique", "y", "y.codeRef", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
        update("blog_form_centre_analytique");
    }

    public void paginer(boolean avancer) {
        loadAll(avancer, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void resetFiche() {
        PlanAnalytique p = new PlanAnalytique();
        cloneObject(p, centre.getPlan());
        centre = new CentreAnalytique();
        cloneObject(centre.getPlan(), p);
        selectCentre = new YvsComptaCentreAnalytique();
        loadActif(null);
        resetFicheLiaison();
    }

    public void resetFicheLiaison() {
        liaison = new LiaisonCentres();
        selectLiaison = new YvsComptaRepartitionAnalytique();
    }

    @Override
    public boolean controleFiche(CentreAnalytique bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getPlan() != null ? bean.getPlan().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le centre analytique");
            return false;
        }
        if (bean.getType() != null ? bean.getType().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le type");
            return false;
        }
        if (bean.getCodeRef() != null ? bean.getCodeRef().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la reference");
            return false;
        }
        if (bean.getIntitule() != null ? bean.getIntitule().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le libellé");
            return false;
        }
//        if (bean.getUniteOeuvre() != null ? bean.getUniteOeuvre().getId() < 1 : true) {
//            getErrorMessage("Vous devez precisez l'unite d'oeuvre");
//            return false;
//        }
        YvsComptaCentreAnalytique y = (YvsComptaCentreAnalytique) dao.loadOneByNameQueries("YvsComptaCentreAnalytique.findByCodeRef", new String[]{"plan", "codeRef"}, new Object[]{new YvsComptaPlanAnalytique(bean.getPlan().getId()), bean.getCodeRef()});
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez deja crée ce centre analytique");
            return false;
        }
        return true;
    }

    public boolean controleFiche(LiaisonCentres bean) {
        if (selectCentre != null ? selectCentre.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer ou selectionner le centre principal");
            return false;
        }
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getSecondaire() != null ? bean.getSecondaire().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le centre secondaire");
            return false;
        }
        if (bean.getUnite() != null ? bean.getUnite().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez l'unite");
            return false;
        }
        if (bean.getCoefficient() <= 0) {
            getErrorMessage("Vous devez precisez le taux");
            return false;
        }
        YvsComptaRepartitionAnalytique y = (YvsComptaRepartitionAnalytique) dao.loadOneByNameQueries("YvsComptaRepartitionAnalytique.findOne", new String[]{"principal", "secondaire"}, new Object[]{selectCentre, new YvsComptaCentreAnalytique(bean.getSecondaire().getId())});
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Vous avez deja crée cette repartition analytique");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(centre)) {
                selectCentre = UtilCompta.buildCentreAnalytique(centre, currentUser);
                if (centre.getId() < 1) {
                    selectCentre.setId(null);
                    selectCentre = (YvsComptaCentreAnalytique) dao.save1(selectCentre);
                    centre.setId(selectCentre.getId());
                } else {
                    dao.update(selectCentre);
                }
                int idx = centres.indexOf(selectCentre);
                if (idx > -1) {
                    centres.set(idx, selectCentre);
                } else {
                    centres.add(0, selectCentre);
                }
                actionOpenOrResetAfter(this);
                update("data_centre_analytique");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedCentreAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void saveAndReset() {
        saveNew();
        resetFiche();
    }

    public boolean saveNewLiaison() {
        try {
            if (controleFiche(liaison)) {
                YvsComptaRepartitionAnalytique y = UtilCompta.buildRepartition(liaison, currentUser);
                y.setPrincipal(selectCentre);
                if (liaison.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaRepartitionAnalytique) dao.save1(y);
                    liaison.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = centre.getRepartitions().indexOf(y);
                if (idx > -1) {
                    centre.getRepartitions().set(idx, y);
                } else {
                    centre.getRepartitions().add(0, y);
                }
                idx = selectCentre.getRepartitions().indexOf(y);
                if (idx > -1) {
                    selectCentre.getRepartitions().set(idx, y);
                } else {
                    selectCentre.getRepartitions().add(0, y);
                }
                idx = centres.indexOf(selectCentre);
                if (idx > -1) {
                    centres.set(idx, selectCentre);
                }
                update("data_repartition_analytique");
                resetFicheLiaison();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedCentreAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (selectCentre != null ? selectCentre.getId() > 0 : false) {
                dao.delete(selectCentre);
                centres.remove(selectCentre);
                if (selectCentre.getId().equals(centre.getId())) {
                    resetFiche();
                    update("form_centre_analytique");
                }
                update("data_centre_analytique");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedCentreAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanLiaison() {
        try {
            if (selectLiaison != null ? selectLiaison.getId() > 0 : false) {
                dao.delete(selectLiaison);
                centre.getRepartitions().remove(selectLiaison);
                selectCentre.getRepartitions().remove(selectLiaison);
                int idx = centres.indexOf(selectCentre);
                if (idx > -1) {
                    centres.set(idx, selectCentre);
                }
                if (selectLiaison.getId().equals(liaison.getId())) {
                    resetFicheLiaison();
                    update("form_repartition_analytique");
                }
                update("data_repartition_analytique");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
            Logger.getLogger(ManagedCentreAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSelectObject(YvsComptaCentreAnalytique y) {
        selectCentre = y;
        centre = UtilCompta.buildBeanCentreAnalytique(y);
        loadActif(y);
        resetFicheLiaison();
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComptaCentreAnalytique) ev.getObject());
            update("blog_form_centre_analytique");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_centre_analytique");
    }

    public void loadOnViewLiaison(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectLiaison = (YvsComptaRepartitionAnalytique) ev.getObject();
            liaison = UtilCompta.buildBeanRepartition(selectLiaison);
            update("form_repartititon_analytique");
        }
    }

    public void unLoadOnViewLiaison(UnselectEvent ev) {
        resetFicheLiaison();
        update("form_repartititon_analytique");
    }

    public void choosePlan() {
        if (centre.getPlan() != null ? centre.getPlan().getId() > 0 : false) {
            ManagedAnalytique w = (ManagedAnalytique) giveManagedBean(ManagedAnalytique.class);
            if (w != null) {
                int idx = w.getPlans().indexOf(new YvsComptaPlanAnalytique(centre.getPlan().getId()));
                if (idx > -1) {
                    YvsComptaPlanAnalytique y = w.getPlans().get(idx);
                    centre.setPlan(UtilCompta.buildBeanPlanAnalytique(y));
                }
            }
        } else if (centre.getPlan() != null ? centre.getPlan().getId() == -1 : false) {
            openDialog("dlgAddPlan");
        }
    }

    public void chooseSecondaire() {
        if (liaison.getSecondaire() != null ? liaison.getSecondaire().getId() > 0 : false) {
            int idx = secondaires.indexOf(new YvsComptaCentreAnalytique(liaison.getSecondaire().getId()));
            if (idx > -1) {
                YvsComptaCentreAnalytique y = secondaires.get(idx);
                liaison.setSecondaire(UtilCompta.buildBeanCentreAnalytique(y));
            }
        }
    }

    public void chooseUniteOeuvre() {
        if (centre.getUniteOeuvre() != null ? centre.getUniteOeuvre().getId() > 0 : false) {
            ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (w != null) {
                int idx = w.getUnites().indexOf(new YvsBaseUniteMesure(centre.getUniteOeuvre().getId()));
                if (idx > -1) {
                    YvsBaseUniteMesure y = w.getUnites().get(idx);
                    centre.setUniteOeuvre(UtilProd.buildBeanUniteMesure(y));
                }
            }
        } else if (centre.getUniteOeuvre() != null ? centre.getUniteOeuvre().getId() == -1 : false) {
            ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (w != null) {
                resetFiche(w.getUniteMesure());
                w.getUniteMesure().setType(Constantes.UNITE_OEUVRE);
                update("form_unite_centre_analytique");
                openDialog("dlgAddUnite");
            }
        }
    }

    public void chooseUnite() {
        if (liaison.getUnite() != null ? liaison.getUnite().getId() > 0 : false) {
            ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (w != null) {
                int idx = w.getUnites().indexOf(new YvsBaseUniteMesure(liaison.getUnite().getId()));
                if (idx > -1) {
                    YvsBaseUniteMesure y = w.getUnites().get(idx);
                    liaison.setUnite(UtilProd.buildBeanUniteMesure(y));
                }
            }
        } else if (liaison.getUnite() != null ? liaison.getUnite().getId() == -1 : false) {
            ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (w != null) {
                resetFiche(w.getUniteMesure());
                w.getUniteMesure().setType(Constantes.UNITE_OEUVRE);
                update("form_unite_centre_analytique");
                openDialog("dlgAddUnite");
            }
        }
    }

    public void changeActif(YvsComptaCentreAnalytique y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setActif(!y.getActif());
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            dao.update(y);
            int idx = centres.indexOf(y);
            if (idx > -1) {
                centres.set(idx, y);
            }
            succes();
        }
    }

    public void updateTypeCentre(YvsComptaCentreAnalytique y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            y.setTypeCentre((Constantes.PRINCIPAL.equals(y.getTypeCentre()) ? Constantes.AUXILLIAIRE : Constantes.PRINCIPAL));
            dao.update(y);
            int idx = centres.indexOf(y);
            if (idx > -1) {
                centres.set(idx, y);
            }
            succes();
        }
    }

    public CentreAnalytique findCentre(String num, boolean open) {
        addParamReference(num);
        CentreAnalytique a = findCentreResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setCodeRef(num);
            a.setError(true);
        }
        return a;
    }

    private CentreAnalytique findCentreResult(boolean open) {
        CentreAnalytique a = new CentreAnalytique();
        if (centres != null ? !centres.isEmpty() : false) {
            if (centres.size() > 1) {
                if (open) {
                    openDialog("dlgListCentre");
                }
            } else {
                YvsComptaCentreAnalytique y = centres.get(0);
                a = UtilCompta.buildBeanCentreAnalytique(y);
            }
            a.setError(false);
        }
        return a;
    }

    public void addParamReference(String numeroSearch) {
        this.numeroSearch = numeroSearch;
        addParamReference();
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.codeRef", "codeRef", null, "LIKE", "AND");
        if ((numeroSearch != null) ? !numeroSearch.trim().isEmpty() : false) {
            p = new ParametreRequete(null, "codeRef", numeroSearch.trim(), "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.codeRef)", "numero", numeroSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "numero", numeroSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamNiveau() {
        ParametreRequete p = new ParametreRequete("y.niveau", "niveau", null, "=", "AND");
        if ((niveauSearch != null) ? !niveauSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("UPPER(y.niveau)", "niveau", niveauSearch.trim().toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamPlan() {
        ParametreRequete p = new ParametreRequete("y.plan", "plan", null, "=", "AND");
        if (planSearch > 0) {
            p = new ParametreRequete("y.plan", "plan", new YvsComptaPlanAnalytique(planSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamUnite() {
        ParametreRequete p = new ParametreRequete("y.uniteOeuvre", "unite", null, "=", "AND");
        if (uniteSearch > 0) {
            p = new ParametreRequete("y.uniteOeuvre", "unite", new YvsBaseUniteMesure(uniteSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.typeCentre", "type", null, "=", "AND");
        if ((typeSearch != null) ? !typeSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("UPPER(y.typeCentre)", "type", typeSearch.trim().toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actifSearch, "=", "AND"));
        loadAll(true, true);
    }

}

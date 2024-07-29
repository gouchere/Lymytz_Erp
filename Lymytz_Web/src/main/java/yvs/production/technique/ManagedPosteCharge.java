/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedUniteMesure;
import yvs.base.produits.UniteMesure;
import yvs.production.UtilProd;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.entity.param.YvsBaseTypeEtat;
import yvs.entity.production.base.YvsProdCapacitePosteCharge;
import yvs.entity.production.base.YvsProdCentreCharge;
import yvs.entity.production.base.YvsProdCentrePosteCharge;
import yvs.entity.production.base.YvsProdEtatRessource;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.production.base.YvsProdPosteChargeMateriel;
import yvs.entity.production.base.YvsProdPosteChargeEmploye;
import yvs.entity.production.base.YvsProdPosteChargeTiers;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.grh.Calendrier;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.presence.ManagedCalendrier;
import yvs.parametrage.SectionDeValorisation;
import yvs.parametrage.ManagedSectionValorisation;
import yvs.parametrage.ManagedTypeEtat;
import yvs.parametrage.UtilParam;
import yvs.production.base.CentreCharge;
import yvs.production.base.EtatRessource;
import yvs.production.base.ManagedCentreCharge;
import yvs.production.base.ManagedSiteProduction;
import yvs.production.base.SiteProduction;
import yvs.production.base.TypeRessource;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedPosteCharge extends Managed<PosteCharge, YvsProdPosteCharge> implements Serializable {

    @ManagedProperty(value = "#{posteCharge}")
    private PosteCharge posteCharge;
    private List<YvsProdPosteCharge> postes;
    private YvsProdPosteCharge selectPoste;

    private CapacitePosteCharge capacite = new CapacitePosteCharge();
    private YvsProdCapacitePosteCharge selectCapacite;

    private CentrePosteCharge centre = new CentrePosteCharge();
    private YvsProdCentrePosteCharge selectCentre;

    private EtatRessource etat = new EtatRessource();
    private YvsProdEtatRessource selectEtat;

    private String tabIds, tabIds_capacite, tabIds_centre, tabIds_etat;

    private String typeSearch, numSearch;
    private long siteSearch, centreSearch, calSearch;

    public ManagedPosteCharge() {
        postes = new ArrayList<>();
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public long getSiteSearch() {
        return siteSearch;
    }

    public void setSiteSearch(long siteSearch) {
        this.siteSearch = siteSearch;
    }

    public long getCentreSearch() {
        return centreSearch;
    }

    public void setCentreSearch(long centreSearch) {
        this.centreSearch = centreSearch;
    }

    public long getCalSearch() {
        return calSearch;
    }

    public void setCalSearch(long celSearch) {
        this.calSearch = celSearch;
    }

    public PosteCharge getPosteCharge() {
        return posteCharge;
    }

    public void setPosteCharge(PosteCharge posteCharge) {
        this.posteCharge = posteCharge;
    }

    public List<YvsProdPosteCharge> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsProdPosteCharge> postes) {
        this.postes = postes;
    }

    public YvsProdPosteCharge getSelectPoste() {
        return selectPoste;
    }

    public void setSelectPoste(YvsProdPosteCharge selectPoste) {
        this.selectPoste = selectPoste;
    }

    public CapacitePosteCharge getCapacite() {
        return capacite;
    }

    public void setCapacite(CapacitePosteCharge capacite) {
        this.capacite = capacite;
    }

    public YvsProdCapacitePosteCharge getSelectCapacite() {
        return selectCapacite;
    }

    public void setSelectCapacite(YvsProdCapacitePosteCharge selectCapacite) {
        this.selectCapacite = selectCapacite;
    }

    public CentrePosteCharge getCentre() {
        return centre;
    }

    public void setCentre(CentrePosteCharge centre) {
        this.centre = centre;
    }

    public YvsProdCentrePosteCharge getSelectCentre() {
        return selectCentre;
    }

    public void setSelectCentre(YvsProdCentrePosteCharge selectCentre) {
        this.selectCentre = selectCentre;
    }

    public EtatRessource getEtat() {
        return etat;
    }

    public void setEtat(EtatRessource etat) {
        this.etat = etat;
    }

    public YvsProdEtatRessource getSelectEtat() {
        return selectEtat;
    }

    public void setSelectEtat(YvsProdEtatRessource selectEtat) {
        this.selectEtat = selectEtat;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_capacite() {
        return tabIds_capacite;
    }

    public void setTabIds_capacite(String tabIds_capacite) {
        this.tabIds_capacite = tabIds_capacite;
    }

    public String getTabIds_centre() {
        return tabIds_centre;
    }

    public void setTabIds_centre(String tabIds_centre) {
        this.tabIds_centre = tabIds_centre;
    }

    public String getTabIds_etat() {
        return tabIds_etat;
    }

    public void setTabIds_etat(String tabIds_etat) {
        this.tabIds_etat = tabIds_etat;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.siteProduction.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        postes = paginator.executeDynamicQuery("YvsProdPosteCharge", "y.reference", avance, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsProdPosteCharge> re = paginator.parcoursDynamicData("YvsProdPosteCharge", "y", "y.reference", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(PosteCharge bean) {
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            getErrorMessage("Vous devez preciser la reference");
            return false;
        }
        if (bean.getType() == null || bean.getType().trim().length() < 1) {
            getErrorMessage("Vous devez preciser le type");
            return false;
        }
        if (bean.getSite() != null ? bean.getSite().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le site de production");
            return false;
        }
        if (bean.getCalendrier() != null ? bean.getCalendrier().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le calendrier");
            return false;
        }
        champ = new String[]{"reference", "societe"};
        val = new Object[]{bean.getReference(), currentAgence.getSociete()};
        YvsProdPosteCharge y = (YvsProdPosteCharge) dao.loadOneByNameQueries("YvsProdPosteCharge.findByReference", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce poste de charge");
            return false;
        }
        return true;
    }

    public boolean controleFiche(CentrePosteCharge bean) {
        if (bean.getValeur() <= 0) {
            getErrorMessage("Vous devez preciser la valeur");
            return false;
        }
        if (bean.getPeriode() != null ? bean.getPeriode().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser la periode");
            return false;
        }
        if (bean.getPoste() != null ? bean.getPoste().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le poste de charge");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le centre de charge");
            return false;
        }
        champ = new String[]{"centre", "poste"};
        val = new Object[]{new YvsBaseCentreValorisation(bean.getCentre().getId()), new YvsProdPosteCharge(bean.getPoste().getId())};
        YvsProdCentrePosteCharge y = (YvsProdCentrePosteCharge) dao.loadOneByNameQueries("YvsProdCentrePosteCharge.findByPosteCentre", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré ce centre de poste de charge");
            return false;
        }
        return true;
    }

    public boolean controleFiche(CapacitePosteCharge bean) {
        if (bean.getCapaciteQ() <= 0) {
            getErrorMessage("Vous devez preciser la capacitée");
            return false;
        }
        if (bean.getPoste() != null ? bean.getPoste().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le poste de charge");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser l'article");
            return false;
        }
        champ = new String[]{"article", "poste"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsProdPosteCharge(bean.getPoste().getId())};
        YvsProdCapacitePosteCharge y = (YvsProdCapacitePosteCharge) dao.loadOneByNameQueries("YvsProdCapacitePosteCharge.findByPosteArticle", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré cet article");
            return false;
        }
        return true;
    }

    public boolean controleFiche(EtatRessource bean) {
        if (selectPoste != null ? selectPoste.getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le poste de charge");
            return false;
        }
        if (bean.getTypeEtat() != null ? bean.getTypeEtat().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le type d'état");
            return false;
        }
        if (bean.isActif()) {
            champ = new String[]{"actif", "ressource"};
            val = new Object[]{true, new YvsProdPosteChargeMateriel(bean.getRessource().getId())};
            YvsProdEtatRessource y = (YvsProdEtatRessource) dao.loadOneByNameQueries("YvsProdEtatRessource.findByActif", champ, val);
            if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
                getErrorMessage("Cette ressource est déjà à un etat actif");
                return false;
            }
        }
        champ = new String[]{"ressource", "type"};
        val = new Object[]{new YvsProdPosteChargeMateriel(bean.getRessource().getId()), new YvsBaseTypeEtat(bean.getTypeEtat().getId())};
        YvsProdEtatRessource y = (YvsProdEtatRessource) dao.loadOneByNameQueries("YvsProdEtatRessource.findByRessourceType", champ, val);
        if (y != null ? (y.getId() != null ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré cet état de ressource");
            return false;
        }
        return true;
    }

    @Override
    public PosteCharge recopieView() {
        PosteCharge p = new PosteCharge();
        p.setId(posteCharge.getId());
        p.setReference(posteCharge.getReference());
        p.setDesignation(posteCharge.getDesignation());
        p.setDescription(posteCharge.getDescription());
        p.setType(posteCharge.getType());
        p.setPosteEquivalent(posteCharge.getPosteEquivalent());
        p.setTempsAttente(posteCharge.getTempsAttente());
        p.setTempsExecution(posteCharge.getTempsExecution());
        p.setTempsTransfert(posteCharge.getTempsTransfert());
        p.setTempsReglage(posteCharge.getTempsReglage());
        p.setTempsRebus(posteCharge.getTempsRebus());
        p.setTauxRendement(posteCharge.getTauxRendement());
        p.setCapaciteQ(posteCharge.getCapaciteQ());
        p.setCapaciteH(posteCharge.getCapaciteH());
        p.setCentreVal(posteCharge.getCentreVal());
        ManagedCalendrier a = (ManagedCalendrier) giveManagedBean(ManagedCalendrier.class);
        if (a != null ? a.getCalendriers().contains(new YvsCalendrier(posteCharge.getCalendrier().getId())) : false) {
            posteCharge.setCalendrier(UtilGrh.buildBeanCalendrier(a.getCalendriers().get(a.getCalendriers().indexOf(new YvsCalendrier(posteCharge.getCalendrier().getId())))));
        }
        p.setCalendrier(posteCharge.getCalendrier());
        ManagedSiteProduction s = (ManagedSiteProduction) giveManagedBean(ManagedSiteProduction.class);
        if (s != null ? s.getSites().contains(new YvsProdSiteProduction(posteCharge.getSite().getId())) : false) {
            posteCharge.setSite(UtilProd.buildBeanSiteProduction(s.getSites().get(s.getSites().indexOf(new YvsProdSiteProduction(posteCharge.getSite().getId())))));
        }
        p.setSite(posteCharge.getSite());
        ManagedCentreCharge c = (ManagedCentreCharge) giveManagedBean(ManagedCentreCharge.class);
        if (c != null ? c.getCentres().contains(new YvsProdCentreCharge(posteCharge.getCentre().getId())) : false) {
            posteCharge.setCentre(UtilProd.buildBeanCentreCharge(c.getCentres().get(c.getCentres().indexOf(new YvsProdCentreCharge(posteCharge.getCentre().getId())))));
        }
        p.setTiers(posteCharge.getTiers());
        p.setEmploye(posteCharge.getEmploye());
        p.setMateriel(posteCharge.getMateriel());
        p.setCentre(posteCharge.getCentre());
        p.setEtats(posteCharge.getEtats());
        p.setCentres(posteCharge.getCentres());
        p.setCapacites(posteCharge.getCapacites());
        return p;
    }

    public CentrePosteCharge recopieViewCentre(PosteCharge y) {
        CentrePosteCharge c = new CentrePosteCharge();
        c.setId(centre.getId());
        c.setValeur(centre.getValeur());
        c.setDirect(centre.isDirect());
        ManagedUniteMesure su = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (su != null ? su.getUnites().contains(new YvsBaseUniteMesure(centre.getPeriode().getId())) : false) {
            centre.setPeriode(UtilProd.buildBeanUniteMesure(su.getUnites().get(su.getUnites().indexOf(new YvsBaseUniteMesure(centre.getPeriode().getId())))));
        }
        c.setPeriode(centre.getPeriode());
        ManagedSectionValorisation sc = (ManagedSectionValorisation) giveManagedBean(ManagedSectionValorisation.class);
        if (sc != null ? sc.getCentres().contains(new YvsBaseCentreValorisation(centre.getCentre().getId())) : false) {
            centre.setCentre(UtilParam.buildBeanCentreValorisation(sc.getCentres().get(sc.getCentres().indexOf(new YvsBaseCentreValorisation(centre.getCentre().getId())))));
        }
        c.setCentre(centre.getCentre());
        c.setPoste(y);
        return c;
    }

    @Override
    public void populateView(PosteCharge bean) {
        cloneObject(posteCharge, bean);
        chooseType();
        resetFicheCentre();
        resetFicheCapacite();
        resetFicheEtat();
    }

    public void populateView(CentrePosteCharge bean) {
        cloneObject(centre, bean);
    }

    public void populateView(CapacitePosteCharge bean) {
        cloneObject(capacite, bean);
    }

    public void populateView(EtatRessource bean) {
        cloneObject(etat, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(posteCharge);
        posteCharge.setTiers(new Tiers());
        posteCharge.setEmploye(new Employe());
        posteCharge.setMateriel(new Articles());
        posteCharge.setCalendrier(new Calendrier());
        posteCharge.setSite(new SiteProduction());
        posteCharge.setCentre(new CentreCharge());
        posteCharge.setCentreVal(new SectionDeValorisation());
        posteCharge.setEtats(new ArrayList<YvsProdEtatRessource>());
        posteCharge.setCentres(new ArrayList<YvsProdCentrePosteCharge>());
        posteCharge.setCapacites(new ArrayList<YvsProdCapacitePosteCharge>());
        selectPoste = null;
        tabIds = "";
        resetFicheCentre();
        resetFicheCapacite();
        resetFicheEtat();
        update("blog_form_poste_charge");
    }

    public void resetFicheCentre() {
        centre = new CentrePosteCharge();
        selectCentre = null;
        tabIds_centre = "";
    }

    public void resetFicheCapacite() {
        capacite = new CapacitePosteCharge();
        selectCapacite = null;
        tabIds_capacite = "";
    }

    public void resetFicheEtat() {
        etat = new EtatRessource();
        tabIds_etat = "";
        selectEtat = null;
    }

    @Override
    public boolean saveNew() {
        String action = posteCharge.getId() > 0 ? "Modification" : "Insertion";
        try {
            PosteCharge bean = recopieView();
            if (controleFiche(bean)) {
                YvsProdPosteCharge y = UtilProd.buildPosteCharge(bean, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdPosteCharge) dao.save1(y);
                    posteCharge.setId(y.getId());
                }
                switch (posteCharge.getType()) {
                    case "H": {
                        YvsProdPosteChargeEmploye r = (YvsProdPosteChargeEmploye) dao.loadOneByNameQueries("YvsProdPosteChargeEmploye.findOne", new String[]{"ressource"}, new Object[]{y});
                        YvsGrhEmployes e = new YvsGrhEmployes(posteCharge.getEmploye().getId(), posteCharge.getEmploye().getMatricule(), posteCharge.getEmploye().getCivilite(), posteCharge.getEmploye().getNom(), posteCharge.getEmploye().getPrenom());
                        if (r != null ? r.getId() > 0 : false) {
                            r.setEmploye(e);
                            r.setDateUpdate(new Date());
                            r.setAuthor(currentUser);
//                            dao.update(r);
                        } else {
                            r = new YvsProdPosteChargeEmploye(y, e);
                            r.setDateSave(new Date());
                            r.setDateUpdate(new Date());
                            r.setAuthor(currentUser);
//                            r = (YvsProdPosteChargeEmploye) dao.save1(r);
                        }
//                        y.setEmploye(r);
                        break;
                    }
                    case "S": {
                        YvsProdPosteChargeTiers r = (YvsProdPosteChargeTiers) dao.loadOneByNameQueries("YvsProdRessourceProductionTiers.findOne", new String[]{"ressource"}, new Object[]{y});
                        YvsBaseTiers t = new YvsBaseTiers(posteCharge.getTiers().getId(), posteCharge.getTiers().getCodeTiers(), posteCharge.getTiers().getNom(), posteCharge.getTiers().getPrenom());
                        if (r != null ? r.getId() > 0 : false) {
                            r.setTiers(t);
                            r.setDateUpdate(new Date());
                            r.setAuthor(currentUser);
                            dao.update(r);
                        } else {
                            r = new YvsProdPosteChargeTiers(y, t);
                            r.setDateSave(new Date());
                            r.setDateUpdate(new Date());
                            r.setAuthor(currentUser);
                            r.setId(null);
                            r = (YvsProdPosteChargeTiers) dao.save1(t);
                        }
                        y.setTiers(r);
                        break;
                    }
                    default: {
                        YvsProdPosteChargeEmploye e = (YvsProdPosteChargeEmploye) dao.loadOneByNameQueries("YvsProdPosteChargeEmploye.findOne", new String[]{"ressource"}, new Object[]{y});
                        if (e != null ? e.getId() > 0 : false) {
                            dao.delete(e);
                        }
                        y.setEmploye(null);
                        YvsProdPosteChargeTiers t = (YvsProdPosteChargeTiers) dao.loadOneByNameQueries("YvsProdPosteChargeTiers.findOne", new String[]{"ressource"}, new Object[]{y});
                        if (t != null ? t.getId() > 0 : false) {
                            dao.delete(t);
                        }
                        y.setTiers(null);
                        YvsProdPosteChargeMateriel m = (YvsProdPosteChargeMateriel) dao.loadOneByNameQueries("YvsProdPosteChargeMateriel.findOne", new String[]{"ressource"}, new Object[]{y});
                        if (m != null ? m.getId() > 0 : false) {
                            dao.delete(m);
                        }
                        y.setMateriel(null);
                        break;
                    }
                }
                int idx = postes.indexOf(y);
                if (idx > -1) {
                    postes.set(idx, y);
                } else {
                    postes.add(0, y);
                    if (postes.size() > imax) {
                        postes.remove(postes.size() - 1);
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Lymytz Production Error>>>> " + action, ex);
            return false;
        }
    }

    public boolean saveNewCentre() {
        String action = centre.getId() > 0 ? "Modification" : "Insertion";
        try {
            CentrePosteCharge bean = recopieViewCentre(posteCharge);
            if (controleFiche(bean)) {
                YvsProdCentrePosteCharge y = UtilProd.buildCentrePosteCharge(bean, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdCentrePosteCharge) dao.save1(y);
                    centre.setId(y.getId());
                }
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx = postes.get(idx).getCentres().indexOf(y);
                    if (_idx > -1) {
                        postes.get(idx).getCentres().set(_idx, y);
                    } else {
                        postes.get(idx).getCentres().add(0, y);
                    }
                }
                idx = posteCharge.getCentres().indexOf(y);
                if (idx > -1) {
                    posteCharge.getCentres().set(idx, y);
                } else {
                    posteCharge.getCentres().add(0, y);
                }
                resetFicheCentre();
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public boolean saveNewCapacite() {
        String action = capacite.getId() > 0 ? "Modification" : "Insertion";
        try {
            capacite.setPoste(posteCharge);
            if (controleFiche(capacite)) {
                YvsProdCapacitePosteCharge y = UtilProd.buildCapacitePosteCharge(capacite, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdCapacitePosteCharge) dao.save1(y);
                    capacite.setId(y.getId());
                }
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx = postes.get(idx).getCapacites().indexOf(y);
                    if (_idx > -1) {
                        postes.get(idx).getCapacites().set(_idx, y);
                    } else {
                        postes.get(idx).getCapacites().add(0, y);
                    }
                }
                idx = posteCharge.getCapacites().indexOf(y);
                if (idx > -1) {
                    posteCharge.getCapacites().set(idx, y);
                } else {
                    posteCharge.getCapacites().add(0, y);
                }
                resetFicheCapacite();
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public boolean saveNewEtat() {
        String action = etat.getId() > 0 ? "Modification" : "Insertion";
        try {
            ManagedTypeEtat st = (ManagedTypeEtat) giveManagedBean(ManagedTypeEtat.class);
            if (st != null ? st.getTypes().contains(new YvsBaseTypeEtat(etat.getTypeEtat().getId())) : false) {
                etat.setTypeEtat(UtilParam.buildBeanTypeEtat(st.getTypes().get(st.getTypes().indexOf(new YvsBaseTypeEtat(etat.getTypeEtat().getId())))));
            }
            if (controleFiche(etat)) {
                YvsProdEtatRessource y = UtilProd.buildEtatRessource(etat, currentUser);
                y.setRessourceProduction(selectPoste);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsProdEtatRessource) dao.save1(y);
                    etat.setId(y.getId());
                }
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx_ = postes.get(idx).getEtats().indexOf(y);
                    if (_idx_ > -1) {
                        postes.get(idx).getEtats().set(_idx_, y);
                    } else {
                        postes.get(idx).getEtats().add(0, y);
                    }
                }
                idx = selectPoste.getEtats().indexOf(y);
                if (idx > -1) {
                    selectPoste.getEtats().set(idx, y);
                } else {
                    selectPoste.getEtats().add(0, y);
                }
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsProdPosteCharge> list = new ArrayList<>();
                YvsProdPosteCharge bean;
                for (Long ids : l) {
                    bean = postes.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    if (bean.getId() == posteCharge.getId()) {
                        resetFiche();
                    }
                }
                succes();
                postes.removeAll(list);
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBean_(YvsProdPosteCharge y) {
        selectPoste = y;
    }

    public void deleteBean_() {
        try {
            if (selectPoste != null ? selectPoste.getId() > 0 : false) {
                dao.delete(selectPoste);
                int idx = postes.indexOf(selectPoste);
                if (idx > -1) {
                    postes.remove(idx);
                }
                if (selectPoste.getId() == posteCharge.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanCentre() {
        try {
            if (tabIds_centre != null ? tabIds_centre.trim().length() > 0 : false) {
                String[] ids = tabIds_centre.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsProdCentrePosteCharge((int) id));
                    int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                    if (idx > -1) {
                        int _idx = postes.get(idx).getCentres().indexOf(new YvsProdCentrePosteCharge((int) id));
                        if (_idx > -1) {
                            postes.get(idx).getCentres().remove(_idx);
                        }
                    }
                    idx = posteCharge.getCentres().indexOf(new YvsProdCentrePosteCharge((int) id));
                    if (idx > -1) {
                        posteCharge.getCentres().remove(idx);
                    }
                    if (id == centre.getId()) {
                        resetFicheCentre();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanCentre_(YvsProdCentrePosteCharge y) {
        selectCentre = y;
    }

    public void deleteBeanCentre_() {
        try {
            if (selectCentre != null ? selectCentre.getId() > 0 : false) {
                dao.delete(selectCentre);
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx = postes.get(idx).getCentres().indexOf(selectCentre);
                    if (_idx > -1) {
                        postes.get(idx).getCentres().remove(_idx);
                    }
                }
                idx = posteCharge.getCentres().indexOf(selectCentre);
                if (idx > -1) {
                    posteCharge.getCentres().remove(idx);
                }
                if (selectCentre.getId() == centre.getId()) {
                    resetFicheCentre();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanCapacite() {
        try {
            if (tabIds_capacite != null ? tabIds_capacite.trim().length() > 0 : false) {
                String[] ids = tabIds_capacite.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsProdCapacitePosteCharge((int) id));
                    int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                    if (idx > -1) {
                        int _idx = postes.get(idx).getCapacites().indexOf(new YvsProdCapacitePosteCharge((int) id));
                        if (_idx > -1) {
                            postes.get(idx).getCapacites().remove(_idx);
                        }
                    }
                    idx = posteCharge.getCapacites().indexOf(new YvsProdCapacitePosteCharge((int) id));
                    if (idx > -1) {
                        posteCharge.getCapacites().remove(idx);
                    }
                    if (id == capacite.getId()) {
                        resetFicheCapacite();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanCapacite_(YvsProdCapacitePosteCharge y) {
        selectCapacite = y;
    }

    public void deleteBeanCapacite_() {
        try {
            if (selectCapacite != null ? selectCapacite.getId() > 0 : false) {
                dao.delete(selectCapacite);
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx = postes.get(idx).getCapacites().indexOf(selectCapacite);
                    if (_idx > -1) {
                        postes.get(idx).getCapacites().remove(_idx);
                    }
                }
                idx = posteCharge.getCapacites().indexOf(selectCapacite);
                if (idx > -1) {
                    posteCharge.getCapacites().remove(idx);
                }
                if (selectCapacite.getId() == capacite.getId()) {
                    resetFicheCapacite();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanEtat() {
        try {
            if (tabIds_etat != null ? tabIds_etat.trim().length() > 0 : false) {
                String[] ids = tabIds_etat.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsProdEtatRessource((int) id));
                    int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                    if (idx > -1) {
                        int _idx_ = postes.get(idx).getEtats().indexOf(new YvsProdEtatRessource((int) id));
                        if (_idx_ > -1) {
                            postes.get(idx).getEtats().remove(_idx_);
                        }
                    }
                    idx = selectPoste.getEtats().indexOf(new YvsProdEtatRessource((int) id));
                    if (idx > -1) {
                        selectPoste.getEtats().remove(idx);
                    }
                    if (id == etat.getId()) {
                        resetFicheEtat();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanEtat_(YvsProdEtatRessource y) {
        selectEtat = y;
    }

    public void deleteBeanEtat_() {
        try {
            if (selectEtat != null ? selectEtat.getId() > 0 : false) {
                dao.delete(selectEtat);
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx_ = postes.get(idx).getEtats().indexOf(selectEtat);
                    if (_idx_ > -1) {
                        postes.get(idx).getEtats().remove(_idx_);
                    }
                }
                idx = selectPoste.getEtats().indexOf(selectEtat);
                if (idx > -1) {
                    selectPoste.getEtats().remove(idx);
                }
                if (selectEtat.getId() == etat.getId()) {
                    resetFicheEtat();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsProdPosteCharge y) {
        populateView(UtilProd.buildBeanPosteCharge(y));
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsProdPosteCharge bean = (YvsProdPosteCharge) ev.getObject();
            onSelectObject(bean);
            tabIds = postes.indexOf(bean) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewCentre(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsProdCentrePosteCharge bean = (YvsProdCentrePosteCharge) ev.getObject();
            populateView(UtilProd.buildBeanCentrePosteCharge(bean));
        }
    }

    public void unLoadOnViewCentre(UnselectEvent ev) {
        resetFicheCentre();
    }

    public void loadOnViewCapacite(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsProdCapacitePosteCharge bean = (YvsProdCapacitePosteCharge) ev.getObject();
            populateView(UtilProd.buildBeanCapacitePosteCharge(bean));
        }
    }

    public void unLoadOnViewCapacite(UnselectEvent ev) {
        resetFicheCapacite();
    }

    public void loadOnViewEtat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsProdEtatRessource bean = (YvsProdEtatRessource) ev.getObject();
            populateView(UtilProd.buildBeanEtatRessource(bean));
        }
    }

    public void unLoadOnViewEtat(UnselectEvent ev) {
        resetFicheEtat();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            capacite.setArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void loadOnViewUnite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseUniteMesure bean = (YvsBaseUniteMesure) ev.getObject();
            centre.setPeriode(UtilProd.buildBeanUniteMesure(bean));
        }
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers bean = (YvsBaseTiers) ev.getObject();
            posteCharge.setTiers(UtilTiers.buildBeanTiers(bean));
        }
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
            posteCharge.setEmploye(UtilGrh.buildBeanEmploye(bean));
        }
    }

    public void chooseCentreValorisation() {
        if (posteCharge.getCentreVal() != null ? posteCharge.getCentreVal().getId() > 0 : false) {
            ManagedSectionValorisation w = (ManagedSectionValorisation) giveManagedBean(ManagedSectionValorisation.class);
            if (w != null) {
                int idx = w.getCentres().indexOf(new YvsBaseCentreValorisation(posteCharge.getCentreVal().getId()));
                if (idx > -1) {
                    YvsBaseCentreValorisation y = w.getCentres().get(idx);
                    posteCharge.setCentreVal(UtilParam.buildBeanCentreValorisation(y));
                }
            }
        }
    }

    public void chooseType() {
        posteCharge.setEmploye(new Employe());
        posteCharge.setTiers(new Tiers());
        posteCharge.setMateriel(new Articles());
    }

    public void blurCapaciteEtatRessource(boolean horaire) {
        if (horaire) {
            etat.setChargeH((etat.getCapaciteH() != 0 && posteCharge.getCapaciteH() != 0) ? (posteCharge.getCapaciteH() / etat.getCapaciteH()) : 0.0);
        } else {
            etat.setChargeQ((etat.getCapaciteQ() != 0 && posteCharge.getCapaciteQ() != 0) ? (posteCharge.getCapaciteQ() / etat.getCapaciteQ()) : 0.0);
        }
    }

    public void setActiveEtat(YvsProdEtatRessource b) {
        b.setActif(!b.getActif());
        if (b.getActif()) {
            b.setDateEtat(new Date());
        }
        b.setAuthor(currentUser);
        dao.update(b);
        int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
        if (idx > -1) {
            int _idx_ = postes.get(idx).getEtats().indexOf(b);
            if (_idx_ > -1) {
                postes.get(idx).getEtats().set(_idx_, b);
            }
        }
        idx = posteCharge.getEtats().indexOf(b);
        if (idx > -1) {
            posteCharge.getEtats().set(idx, b);
        }
        idx = selectPoste.getEtats().indexOf(b);
        if (idx > -1) {
            selectPoste.getEtats().set(idx, b);
        }
    }

    public void forceActiveEtat() {
        champ = new String[]{"actif", "ressource"};
        val = new Object[]{true, selectEtat.getRessourceProduction()};
        List<YvsProdEtatRessource> l = dao.loadNameQueries("YvsProdEtatRessource.findByActif", champ, val);
        for (YvsProdEtatRessource y : l) {
            if (y != null ? (y.getId() != null ? !y.getId().equals(selectEtat.getId()) : false) : false) {
                y.setActif(false);
                y.setAuthor(currentUser);
                dao.update(y);
                int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
                if (idx > -1) {
                    int _idx_ = postes.get(idx).getEtats().indexOf(y);
                    if (_idx_ > -1) {
                        postes.get(idx).getEtats().set(_idx_, y);
                    }
                }
                idx = posteCharge.getEtats().indexOf(y);
                if (idx > -1) {
                    posteCharge.getEtats().set(idx, y);
                }
                idx = selectPoste.getEtats().indexOf(y);
                if (idx > -1) {
                    selectPoste.getEtats().set(idx, y);
                }
            }
        }
        activeEtat();
    }

    public void checkActiveEtat(YvsProdEtatRessource b) {
        selectEtat = b;
        if (!b.getActif()) {
            champ = new String[]{"actif", "ressource"};
            val = new Object[]{true, b.getRessourceProduction()};
            YvsProdEtatRessource y = (YvsProdEtatRessource) dao.loadOneByNameQueries("YvsProdEtatRessource.findByActif", champ, val);
            if (y != null ? (y.getId() != null ? !y.getId().equals(b.getId()) : false) : false) {
                openDialog("dlgConfirmActiveEtat");
                return;
            }
        }
        activeEtat();
    }

    public void activeEtat() {
        selectEtat.setActif(!selectEtat.getActif());
        if (selectEtat.getActif()) {
            selectEtat.setDateEtat(new Date());
        }
        selectEtat.setAuthor(currentUser);
        dao.update(selectEtat);
        int idx = postes.indexOf(new YvsProdPosteCharge(posteCharge.getId()));
        if (idx > -1) {
            int _idx_ = postes.get(idx).getEtats().indexOf(selectEtat);
            if (_idx_ > -1) {
                postes.get(idx).getEtats().set(_idx_, selectEtat);
            }
        }
        idx = posteCharge.getEtats().indexOf(selectEtat);
        if (idx > -1) {
            posteCharge.getEtats().set(idx, selectEtat);
        }
        idx = selectPoste.getEtats().indexOf(selectEtat);
        if (idx > -1) {
            selectPoste.getEtats().set(idx, selectEtat);
        }
    }

    public void managedEtatRessource(YvsProdPosteCharge y) {
        selectPoste = y;
        resetFicheEtat();
    }

    public void searchArticle() {
        String num = capacite.getArticle().getRefArt();
        capacite.getArticle().setDesignation("");
        capacite.getArticle().setError(true);
        capacite.getArticle().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif(null, num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_article_capacite_poste");
                    } else {
                        capacite.setArticle(y);
                        update("txt_article_capacite_poste_charge");
                    }
                    capacite.getArticle().setError(false);
                }
            }
        }
    }

    public void initArticles() {
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        if (a != null) {
            a.initArticles(null, capacite.getArticle());
        }
        update("data_article_capacite_poste");
    }

    public void searchUniteMesure() {
        String num = centre.getPeriode().getReference();
        centre.getPeriode().setLibelle("");
        centre.getPeriode().setError(true);
        centre.getPeriode().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUniteMesure m = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (m != null) {
                UniteMesure y = m.findUnite(num, true);
                if (m.getUnites() != null ? !m.getUnites().isEmpty() : false) {
                    if (m.getUnites().size() > 1) {
                        update("data_unite_poste_charge");
                    } else {
                        centre.setPeriode(y);
                    }
                    centre.getPeriode().setError(false);
                }
            }
        }
    }

    public void initUnites() {
        ManagedUniteMesure a = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (a != null) {
            a.initUnites(centre.getPeriode());
        }
        update("data_unite_poste_charge");
    }

    public void searchTiers() {
        String num = posteCharge.getTiers().getCodeTiers();
        posteCharge.getTiers().setNom("");
        posteCharge.getTiers().setPrenom("");
        posteCharge.getTiers().setError(true);
        posteCharge.getTiers().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (m != null) {
                Tiers y = m.findTiers(num, true);
                if (m.getListTiers() != null ? !m.getListTiers().isEmpty() : false) {
                    if (m.getListTiers().size() > 1) {
                        update("data_tiers_ressource_poste");
                    } else {
                        posteCharge.setTiers(y);
                    }
                    posteCharge.getTiers().setError(false);
                }
            }
        }
    }

    public void initTiers() {
        ManagedTiers a = (ManagedTiers) giveManagedBean(ManagedTiers.class);
        if (a != null) {
            a.initTiers(posteCharge.getTiers());
        }
        update("data_tiers_ressource_poste");
    }

    public void searchEmploye() {
        String num = posteCharge.getEmploye().getMatricule();
        posteCharge.getEmploye().setNom("");
        posteCharge.getEmploye().setPrenom("");
        posteCharge.getEmploye().setError(true);
        posteCharge.getEmploye().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
            if (m != null) {
                Employe y = m.searchEmployeActif(num, true);
                if (m.getListEmployes() != null ? !m.getListEmployes().isEmpty() : false) {
                    if (m.getListEmployes().size() > 1) {
                        update("data_temployes_ressource_poste");
                    } else {
                        posteCharge.setEmploye(y);
                    }
                    posteCharge.getEmploye().setError(false);
                }
            }
        }
    }

    public void initEmployes() {
        ManagedEmployes a = (ManagedEmployes) giveManagedBean("MEmps");
        if (a != null) {
            a.initEmployes(posteCharge.getEmploye());
        }
        update("data_temployes_ressource_poste");
    }

    public void clearParams() {
        numSearch = null;
        typeSearch = null;
        calSearch = 0;
        centreSearch = 0;
        siteSearch = 0;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null);
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null);
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.type", "type", typeSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCalendier() {
        ParametreRequete p = new ParametreRequete("y.calendrier", "calendrier", null);
        if (calSearch > 0) {
            p = new ParametreRequete("y.calendrier", "calendrier", new YvsCalendrier((int) calSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamCentre() {
        ParametreRequete p = new ParametreRequete("y.centreCharge", "centre", null);
        if (centreSearch > 0) {
            p = new ParametreRequete("y.centreCharge", "centre", new YvsProdCentreCharge((int) centreSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSite() {
        ParametreRequete p = new ParametreRequete("y.siteProduction", "site", null);
        if (siteSearch > 0) {
            p = new ParametreRequete("y.siteProduction", "site", new YvsProdSiteProduction((int) siteSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }
}

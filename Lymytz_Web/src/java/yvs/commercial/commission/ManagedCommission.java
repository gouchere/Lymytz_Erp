/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.objectifs.ManagedPeriodeObjectif;
import yvs.commercial.rrr.GrilleRabais;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComCibleFacteurTaux;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.commercial.commission.YvsComCommissionVente;
import yvs.entity.commercial.commission.YvsComFacteurTaux;
import yvs.entity.commercial.commission.YvsComPeriodeValiditeCommision;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.commission.YvsComTrancheTaux;
import yvs.entity.commercial.commission.YvsComTypeGrille;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.production.UtilProd;
import yvs.service.com.param.IYvsComCommissionCommerciaux;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCommission extends ManagedCommercial<FacteurTaux, YvsComFacteurTaux> implements Serializable {

    private PlanCommission plan = new PlanCommission();
    private FacteurTaux facteur = new FacteurTaux();
    private List<YvsComPlanCommission> plans;
    private YvsComPlanCommission selectPlan;
    private List<YvsComFacteurTaux> facteurs;
    private YvsComFacteurTaux selectFacteur;
    private boolean autoCalcul = true;

    private GrilleRabais tranche = new GrilleRabais();
    private Periodicite periode = new Periodicite();
    private TypeGrille type = new TypeGrille();
    private CibleFacteur cible = new CibleFacteur();

    private List<YvsComTypeGrille> types;
    private YvsComDocVentes facture = new YvsComDocVentes();
    public PaginatorResult<YvsComCommissionCommerciaux> paginators = new PaginatorResult<>();
    private YvsComCommissionCommerciaux commercial = new YvsComCommissionCommerciaux();
    private List<YvsComCommissionCommerciaux> commissions;
    private YvsComCommissionCommerciaux selectCommission;
    private PieceTresorerie reglement = new PieceTresorerie();
    private YvsComptaCaissePieceCommission selectReglement;
    private List<YvsUsers> caissiers;

    private String tabIds, tabIds_reglement, tabIds_article, numeroSearch, factureSearch, articleSearch;
    private boolean onClick = false, needConfirmation;
    private long id = -1, idPeriode, idCommercial, idPoint, periodeSearch, commercialSearch, progress;

    public ManagedCommission() {
        plans = new ArrayList<>();
        types = new ArrayList<>();
        facteurs = new ArrayList<>();
        commissions = new ArrayList<>();
        caissiers = new ArrayList<>();
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public String getFactureSearch() {
        return factureSearch;
    }

    public void setFactureSearch(String factureSearch) {
        this.factureSearch = factureSearch;
    }

    public YvsComCommissionCommerciaux getSelectCommission() {
        return selectCommission;
    }

    public void setSelectCommission(YvsComCommissionCommerciaux selectCommission) {
        this.selectCommission = selectCommission;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public long getPeriodeSearch() {
        return periodeSearch;
    }

    public void setPeriodeSearch(long periodeSearch) {
        this.periodeSearch = periodeSearch;
    }

    public long getCommercialSearch() {
        return commercialSearch;
    }

    public void setCommercialSearch(long commercialSearch) {
        this.commercialSearch = commercialSearch;
    }

    public boolean isAutoCalcul() {
        return autoCalcul;
    }

    public void setAutoCalcul(boolean autoCalcul) {
        this.autoCalcul = autoCalcul;
    }

    public long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public long getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(long idPoint) {
        this.idPoint = idPoint;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public PieceTresorerie getReglement() {
        return reglement;
    }

    public void setReglement(PieceTresorerie reglement) {
        this.reglement = reglement;
    }

    public YvsComptaCaissePieceCommission getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaCaissePieceCommission selectReglement) {
        this.selectReglement = selectReglement;
    }

    public String getTabIds_reglement() {
        return tabIds_reglement;
    }

    public void setTabIds_reglement(String tabIds_reglement) {
        this.tabIds_reglement = tabIds_reglement;
    }

    public List<YvsComCommissionCommerciaux> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<YvsComCommissionCommerciaux> commissions) {
        this.commissions = commissions;
    }

    public YvsComDocVentes getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocVentes facture) {
        this.facture = facture;
    }

    public YvsComCommissionCommerciaux getCommercial() {
        return commercial;
    }

    public void setCommercial(YvsComCommissionCommerciaux commercial) {
        this.commercial = commercial;
    }

    public long getIdPeriode() {
        return idPeriode;
    }

    public void setIdPeriode(long idPeriode) {
        this.idPeriode = idPeriode;
    }

    public List<YvsComFacteurTaux> getFacteurs() {
        return facteurs;
    }

    public void setFacteurs(List<YvsComFacteurTaux> facteurs) {
        this.facteurs = facteurs;
    }

    public YvsComFacteurTaux getSelectFacteur() {
        return selectFacteur;
    }

    public void setSelectFacteur(YvsComFacteurTaux selectFacteur) {
        this.selectFacteur = selectFacteur;
    }

    public boolean isOnClick() {
        return onClick;
    }

    public void setOnClick(boolean onClick) {
        this.onClick = onClick;
    }

    public CibleFacteur getCible() {
        return cible;
    }

    public void setCible(CibleFacteur cible) {
        this.cible = cible;
    }

    public Periodicite getPeriode() {
        return periode;
    }

    public void setPeriode(Periodicite periode) {
        this.periode = periode;
    }

    public TypeGrille getType() {
        return type;
    }

    public void setType(TypeGrille type) {
        this.type = type;
    }

    public FacteurTaux getFacteur() {
        return facteur;
    }

    public void setFacteur(FacteurTaux facteur) {
        this.facteur = facteur;
    }

    public List<YvsComTypeGrille> getTypes() {
        return types;
    }

    public void setTypes(List<YvsComTypeGrille> types) {
        this.types = types;
    }

    public GrilleRabais getTranche() {
        return tranche;
    }

    public void setTranche(GrilleRabais tranche) {
        this.tranche = tranche;
    }

    public YvsComPlanCommission getSelectPlan() {
        return selectPlan;
    }

    public void setSelectPlan(YvsComPlanCommission selectPlan) {
        this.selectPlan = selectPlan;
    }

    public PlanCommission getPlan() {
        return plan;
    }

    public void setPlan(PlanCommission plan) {
        this.plan = plan;
    }

    public List<YvsComPlanCommission> getPlans() {
        return plans;
    }

    public void setPlans(List<YvsComPlanCommission> plans) {
        this.plans = plans;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        loadTypes();
        loadPlans();
        load(true, true);
        if (facteur.getPlan().getId() < 0) {
            facteur.setPlan(new PlanCommission());
        }
        if (facteur.getTypeGrille().getId() < 0) {
            facteur.setTypeGrille(new TypeGrille());
        }
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.plan.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        facteurs = paginator.executeDynamicQuery("YvsComFacteurTaux", "y.plan.reference", avance, init, (int) imax, dao);

    }

    public void load(boolean avance, boolean init) {
        paginators.addParam(new ParametreRequete("y.periode.societe ", "societe", currentAgence.getSociete(), "=", "AND"));
        commissions = paginators.executeDynamicQuery("YvsComCommissionCommerciaux", "y.periode.dateDebut DESC, y.numero DESC", avance, init, (int) imax, dao);
    }

    public void addParamNumero() {
        ParametreRequete p = new ParametreRequete("UPPER(y.numero)", "numero", null);
        if (Util.asString(numeroSearch)) {
            p.setObjet(numeroSearch.toUpperCase() + "%");
            p.setOperation("LIKE");
            p.setPredicat("AND");
        }
        paginators.addParam(p);
        load(false, true);
    }

    public void addParamPeriode() {
        ParametreRequete p = new ParametreRequete("y.periode", "periode", null);
        if (periodeSearch > 0) {
            p.setObjet(new YvsComPeriodeObjectif(periodeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginators.addParam(p);
        load(false, true);
    }

    public void addParamCommercial() {
        ParametreRequete p = new ParametreRequete("y.commerciaux", "commerciaux", null);
        if (commercialSearch > 0) {
            p.setObjet(new YvsComComerciale(commercialSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginators.addParam(p);
        load(false, true);
    }

    public void loadPlans() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComPlanCommission.findAll";
        plans = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadTypes() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComTypeGrille.findAll";
        types = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void paginer(boolean next) {
        loadAll(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    public void gotoPagePaginators() {
        paginator.gotoPage((int) imax);
        load(true, true);
    }

    public PaginatorResult<YvsComCommissionCommerciaux> getPaginators() {
        return paginators;
    }

    public void setPaginators(PaginatorResult<YvsComCommissionCommerciaux> paginators) {
        this.paginators = paginators;
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }
    public void choosePaginators(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        load(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComFacteurTaux> re = paginator.parcoursDynamicData("YvsComFacteurTaux", "y", "y.plan.reference", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public PlanCommission recopieViewPlan() {
        PlanCommission r = new PlanCommission();
        r.setId(plan.getId());
        r.setReference(plan.getReference());
        r.setIntitule(plan.getIntitule());
        r.setActif(plan.isActif());
        r.setDateSave(plan.getDateSave());
        r.setDateUpdate(plan.getDateUpdate());
        r.setFacteurs(facteurs);
        return r;
    }

    public Periodicite recopieView(PlanCommission plan, FacteurTaux bean) {
        if (periode != null) {
            periode.setFacteur(bean);
        }
        return periode;
    }

    public PieceTresorerie recopieViewPiece() {
        if (reglement != null) {
            if (reglement.getMode() != null ? reglement.getMode().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) reglement.getMode().getId()));
                    if (idx > -1) {
                        YvsBaseModeReglement o = m.getModes().get(idx);
                        reglement.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                    }
                }
            }
            if (reglement.getCaisse() != null ? reglement.getCaisse().getId() > 0 : false) {
                ManagedCaisses m = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (m != null) {
                    int idx = m.getCaisses().indexOf(new YvsBaseCaisse(reglement.getCaisse().getId()));
                    if (idx > -1) {
                        YvsBaseCaisse o = m.getCaisses().get(idx);
                        reglement.setCaisse(new Caisses(o.getId(), o.getCode(), o.getIntitule()));
                    }
                }
            }
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() > 0 : false) {
                int idx = caissiers.indexOf(new YvsUsers(reglement.getCaissier().getId()));
                if (idx > -1) {
                    YvsUsers o = caissiers.get(idx);
                    reglement.setCaissier(new Users(o.getId(), o.getCodeUsers(), o.getNomUsers()));
                }
            }
            reglement.setMouvement(Constantes.MOUV_DEBIT);
            reglement.setUpdate(true);
            reglement.setDateUpdate(new Date());
            reglement.setDatePaiement(reglement.getDatePaiementPrevu());
            if (reglement.getId() < 1) {
                reglement.setDatePiece(reglement.getDatePaiementPrevu());
            }
        }
        return reglement;
    }

    @Override
    public FacteurTaux recopieView() {
        return facteur;
    }

    public GrilleRabais recopieView(TypeGrille type) {
        if (tranche.isUnique()) {
            tranche.setMontantMinimal(0);
            tranche.setMontantMaximal(Double.MAX_VALUE);
        }
        if (type != null) {
            tranche.setParent(type.getId());
        }
        return tranche;
    }

    public CibleFacteur recopieView(FacteurTaux y) {
        if (cible != null) {
            cible.setFacteur(y);
        }
        return cible;
    }

    public boolean controleFiche(PlanCommission bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        if (bean.getIntitule() == null || bean.getIntitule().trim().equals("")) {
            getErrorMessage("Vous devez entrer la désignation");
            return false;
        }
        return true;
    }

    public boolean controleFiche(GrilleRabais bean) {
        if (bean.getParent() < 1) {
            if (!saveNewType(false)) {
                return false;
            }
            bean.setParent((type.getId()));
        }
        if (bean.getMontantMaximal() < bean.getMontantMinimal()) {
            getErrorMessage("le montant minimal ne peut pas etre superieur au montant maximal");
            return false;
        }
        return true;
    }

    public boolean controleFiche(Periodicite bean) {
        if (bean.getFacteur() != null ? bean.getFacteur().getId() < 1 : true) {
            getErrorMessage("Vus devez precisez le plan");
            return false;
        }
        return true;
    }

    public boolean controleFiche(TypeGrille bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        return true;
    }

    public boolean controleFiche(CibleFacteur bean, boolean msg) {
        if (bean.getFacteur() != null ? bean.getFacteur().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez specifier le facteur");
            }
            return false;
        }
        if (bean.getIdExterne() < 1) {
            if (msg) {
                getErrorMessage("Vous devez specifier la cible");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean controleFiche(FacteurTaux bean) {
        if (bean.getPlan() != null ? bean.getPlan().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le plan");
            return false;
        }
        if ((bean.isByGrille()) && (bean.getTypeGrille() != null ? bean.getTypeGrille().getId() < 1 : true)) {
            getErrorMessage("Vous devez precisez la grille des tranches");
            return false;
        }
        return true;
    }

    public double giveTotalPT(List<YvsComptaCaissePieceCommission> l) {
        double sum = 0;
        if (l != null) {
            for (YvsComptaCaissePieceCommission p : l) {
                if (p.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && p.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    sum += p.getMontant();
                }
            }
        }
        return sum;
    }

    private boolean controleAddPiece(YvsComCommissionCommerciaux d, YvsComptaCaissePieceCommission pt) {
        if (pt.getMontant() <= 0) {
            getErrorMessage("Le montant est incorrecte !");
            return false;
        }
        if (pt.getId() > 0 && pt.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
            getErrorMessage("Vous ne pouvez modifier cette pièce de règlement,", "son statut n'est pas éditable");
            return false;
        }
        if (d != null) {
            double mtn;
            if (d.getReglements().contains(pt)) {//en cas de modification d'une pièce(isoler le montantF à comparer)
                mtn = (-d.getReglements().get(d.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
            } else {
                mtn = pt.getMontant();
            }
            if (d.getMontant() < (giveTotalPT(d.getReglements()) + mtn)) {
                getErrorMessage("Le montant de la facture ne doit pas être inférieure aux règlements planifiés");
                return false;
            }
        }
        if (pt.getDatePaimentPrevu() == null) {
            getErrorMessage("Vous devez préciser la date de paiement !");
            return false;
        }
        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (pt.getId() != null) ? pt.getId() > 0 : false) {
            getErrorMessage("La pièce en cours est déjà payé !");
            return false;
        }
        if ((pt.getModel() != null) ? (pt.getModel().getId() != null ? pt.getModel().getId() < 1 : true) : true) {
            getErrorMessage("Vous devez préciser indiquer le moyen de paiement !");
            return false;
        }
        if (pt.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            if (pt.getReferenceExterne() != null ? pt.getReferenceExterne().trim().length() < 1 : true) {
                getErrorMessage("Vous devez préciser la reference externe !");
                return false;
            }
        }
        if (!verifyDateExercice(pt.getDatePaiement())) {
            return false;
        }
        return true;
    }

    public void populateView(PlanCommission bean) {
        cloneObject(plan, bean);
    }

    public void populateView(GrilleRabais bean) {
        cloneObject(tranche, bean);
    }

    public void populateView(Periodicite bean) {
        cloneObject(periode, bean);
    }

    public void populateView(TypeGrille bean) {
        cloneObject(type, bean);
    }

    @Override
    public void populateView(FacteurTaux bean) {
        cloneObject(facteur, bean);
        resetSubFiche();
    }

    public void populateView(CibleFacteur bean) {
        cloneObject(cible, bean);
    }

    @Override
    public void resetFiche() {
        plan = new PlanCommission();
        selectPlan = new YvsComPlanCommission();
        update("form_plan_commission");
    }

    public void resetSubFiche() {
        resetFiche();
        resetFichePeriode();
        resetFicheType();
    }

    public void resetFichePeriode() {
        periode = new Periodicite();
        update("form_periodicite_commission");
    }

    public void resetFicheFacteur() {
        facteur = new FacteurTaux();
        selectFacteur = new YvsComFacteurTaux();
        tabIds = "";
        resetSubFiche();
        update("blog_plan_commission");
    }

    public void resetFicheType() {
        type = new TypeGrille();
        resetFicheTranche();
        update("form_type_grille_com");
    }

    public void resetFicheTranche() {
        tranche = new GrilleRabais();
        update("form_type_grille_com");
    }

    public void resetFicheCible() {
        String table = cible.getTableExterne();
        cible = new CibleFacteur();
        cible.setTableExterne(table);
        onClick = false;
        update("form_cible_facteur_taux");
    }

    @Override
    public boolean saveNew() {
        String action = plan.getId() > 0 ? "Modification" : "Insertion";
        try {
            PlanCommission bean = recopieViewPlan();
            if (controleFiche(bean)) {
                YvsComPlanCommission y = UtilCom.buildPlanCommission(bean, currentUser, currentAgence.getSociete());
                if (bean.getId() < 1) {
                    y.setId(null);
                    y = (YvsComPlanCommission) dao.save1(y);
                    plan.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = plans.indexOf(y);
                if (idx > -1) {
                    plans.set(idx, y);
                } else {
                    plans.add(0, y);
                }
                succes();
                resetFiche();
                update("data_plan_commission");
                update("select_plan_commission");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewFacteur() {
        String action = facteur.getId() > 0 ? "Modification" : "Insertion";
        try {
            FacteurTaux bean = recopieView();
            if (controleFiche(bean)) {
                YvsComFacteurTaux y = UtilCom.buildFacteurTaux(bean, currentUser);
                if (facteur.getId() < 1) {
                    y.setId(null);
                    y = (YvsComFacteurTaux) dao.save1(y);
                    facteur.setId(y.getId());
                    facteurs.add(0, y);
                } else {
                    dao.update(y);
                    int idx = facteurs.indexOf(y);
                    if (idx > -1) {
                        facteurs.set(idx, y);
                    } else {
                        facteurs.add(0, y);
                    }
                }
                if (!bean.isPermanent()) {
                    Periodicite p = recopieView(plan, facteur);
                    if (controleFiche(p)) {
                        YvsComPeriodeValiditeCommision yp = UtilCom.buildPeriodeValideCommission(p, currentUser);
                        if (yp.getId() < 1) {
                            yp.setId(null);
                            yp = (YvsComPeriodeValiditeCommision) dao.save1(yp);
                            periode.setId(yp.getId());
                        } else {
                            dao.update(yp);
                        }
                        int idx = y.getPeriodicites().indexOf(yp);
                        if (idx > -1) {
                            y.getPeriodicites().set(idx, yp);
                        } else {
                            y.getPeriodicites().add(0, yp);
                        }
                    }
                }
                succes();
                update("data_facteurs_commission");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewType(boolean msg) {
        String action = type.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(type)) {
                YvsComTypeGrille y = UtilCom.buildTypeGrille(type, currentUser, currentAgence.getSociete());
                if (type.getId() < 1) {
                    y.setId(null);
                    y = (YvsComTypeGrille) dao.save1(y);
                    type.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = types.indexOf(y);
                if (idx > -1) {
                    types.set(idx, y);
                } else {
                    types.add(0, y);
                }
                if (msg) {
                    succes();
                }
                update("data_type_grille_com");
                update("value_type_grille_com");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewTranche() {
        String action = tranche.getId() > 0 ? "Modification" : "Insertion";
        try {
            GrilleRabais bean = recopieView(type);
            if (controleFiche(bean)) {
                YvsComTrancheTaux y = UtilCom.buildTrancheTaux(bean, currentUser);
                if (tranche.getId() < 1) {
                    y.setId(null);
                    y = (YvsComTrancheTaux) dao.save1(y);
                    tranche.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = type.getTranches().indexOf(y);
                if (idx > -1) {
                    type.getTranches().set(idx, y);
                } else {
                    type.getTranches().add(0, y);
                }
                succes();
                resetFicheTranche();
                update("data_tranche_taux");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewCible() {
        return saveNewCible(true);
    }

    public boolean saveNewCible(boolean msg) {
        String action = cible.getId() > 0 ? "Modification" : "Insertion";
        try {
            CibleFacteur bean = recopieView(facteur);
            if (controleFiche(bean, msg)) {
                YvsComCibleFacteurTaux y = UtilCom.buildCibleFacteur(bean, currentUser);
                if (bean.getId() < 1) {
                    y.setId(null);
                    y = (YvsComCibleFacteurTaux) dao.save1(y);
                    addCible(y);
                } else {
                    dao.update(y);
                }
                int idx = facteur.getCibles().indexOf(y);
                if (idx > -1) {
                    facteur.getCibles().set(idx, y);
                } else {
                    facteur.getCibles().add(0, y);
                }
                idx = facteurs.indexOf(new YvsComFacteurTaux(facteur.getId()));
                if (idx > -1) {
                    YvsComFacteurTaux p = facteurs.get(idx);
                    idx = p.getCibles().indexOf(y);
                    if (idx > -1) {
                        p.getCibles().set(idx, y);
                    } else {
                        p.getCibles().add(0, y);
                    }
                }
                if (msg) {
                    succes();
                }
                resetFicheCible();
                update("data_cible_facteur_taux");
                update("blog_cible_facture_taux");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void addCiblesArticle() {
        try {
            if (facteur != null ? facteur.getId() > 0 : false) {
                if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                    ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
                    if (w != null) {
                        List<Integer> ids = decomposeSelection(tabIds_article);
                        YvsBaseArticles article;
                        for (Integer index : ids) {
                            article = w.getListArticle().get(index);
                            chooseArticle(UtilProd.buildBeanArticles(article));
                            saveNewCible(false);
                        }
                        succes();
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner un facteur");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Action Impossible !");
            getException("addCiblesArticle : " + ex.getMessage(), ex);
        }
    }

    private void addCible(YvsComCibleFacteurTaux c) {
        switch (c.getTableExterne()) {
            case Constantes.SCR_ARTICLES: {
                YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{c.getIdExterne()});
                a.setId(c.getId());
                facteur.getArticles().add(0, a);
                facteur.getArticlesSave().add(0, a);
                break;
            }
            case Constantes.SCR_CLIENTS: {
                YvsComClient a = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{c.getIdExterne()});
                a.setId(c.getId());
                facteur.getClients().add(0, a);
                break;
            }
            case Constantes.SCR_DICTIONNAIRE: {
                YvsDictionnaire a = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{c.getIdExterne()});
                a.setId(c.getId());
                facteur.getZones().add(0, a);
                break;
            }
            case Constantes.SCR_CATEGORIE: {
                YvsBaseCategorieClient a = (YvsBaseCategorieClient) dao.loadOneByNameQueries("YvsBaseCategorieClient.findById", new String[]{"id"}, new Object[]{c.getIdExterne()});
                a.setId(c.getId());
                facteur.getCategories().add(0, a);
                break;
            }
            default:
                break;
        }
    }

    public YvsComptaCaissePieceCommission createNewPieceCaisse(YvsComCommissionCommerciaux d, YvsComptaCaissePieceCommission pt) {
        if (controleAddPiece(d, pt)) {
            YvsComptaCaissePieceCommission piece = new YvsComptaCaissePieceCommission(pt.getId());
            piece.setStatutPiece(pt.getStatutPiece());
            piece.setMontant(pt.getMontant());
            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
            piece.setDatePiece(pt.getDatePiece());
            piece.setDatePaiement(pt.getDatePaiement());
            piece.setCommission(d);
            piece.setReferenceExterne(pt.getReferenceExterne());
            piece.setDateUpdate(new Date());
            piece.setDateSave(pt.getDateSave());
            piece.setAuthor(currentUser);
            piece.setNumeroPiece(pt.getNumeroPiece());
            if (pt.getCaisse() != null) {
                piece.setCaisse((pt.getCaisse().getId() > 0) ? pt.getCaisse() : null);
            }
            if (pt.getCaissier() != null) {
                piece.setCaissier((pt.getCaissier().getId() > 0) ? pt.getCaissier() : null);
            }
            if (piece.getId() <= 0 || (piece.getNumeroPiece() != null ? piece.getNumeroPiece().trim().length() < 1 : true)) {
                String numero = genererReference(Constantes.TYPE_PC_COMMISSION_NAME, pt.getDatePiece(), pt.getCaisse().getId());
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return null;
                }
                piece.setNumeroPiece(numero);
            }
            if (pt.getModel() != null) {
                piece.setModel((pt.getModel().getId() > 0) ? pt.getModel() : null);
            }
            if ((pt.getId() == null) ? true : pt.getId() <= 0) {
                piece.setDateSave(new Date());
                piece.setId(null);
                piece = (YvsComptaCaissePieceCommission) dao.save1(piece);
            } else {
                piece.setId(pt.getId());
                dao.update(piece);
            }
            piece.setNew_(true);
            return piece;
        }
        return null;
    }

    private int giveAction(YvsComptaCaissePieceCommission pc) {
        if (pc != null) {
            if (pc.getStatutPiece() == Constantes.STATUT_DOC_ATTENTE) {
                return 1;//Encaisser
            } else {
                return 2; //En attente
            }
        }
        return 0;
    }

    public boolean reglerPieceTresorerie(boolean msg) {
        return reglerPieceTresorerie(commercial, selectReglement, msg);
    }

    public boolean reglerPieceTresorerie(YvsComptaCaissePieceCommission pc, boolean msg) {
        return reglerPieceTresorerie(commercial, pc, msg);
    }

    public boolean reglerPieceTresorerie(YvsComCommissionCommerciaux vente, YvsComptaCaissePieceCommission pc, boolean msg) {
        if (pc != null ? pc.getId() > 0 : false) {//si c'est une suspension, on controle juste le droit
            pc.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pc.getCaisse().getId()}));
            if (controleAccesCaisse(pc.getCaisse(), true)) {
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) { //la pièce ne doit pas être déjà payé
                    //la pièce de caisse doit avoir une caisse
                    int action = giveAction(pc);
                    pc.setAuthor(currentUser);
                    pc.setNew_(true);
                    boolean update = false;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    switch (action) {
                        case 2://Mettre la pièce en attente    
                            if (!verifyCancelPieceCaisse(pc.getDatePaiement())) {
                                return false;
                            }
                            if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_COMMISSION)) {
                                if (!autoriser("compta_od_annul_comptabilite")) {
                                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                    return false;
                                }
                                if (w != null) {
//                                    if (!w.unComptabiliserCaisseVente(pc, false)) {
//                                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
//                                        return false;
//                                    }
                                }
                            }
                            pc.setDatePaiement(null);
                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            update = true;
                            break;
                        case 1://Encaisser la pièce
                            if ((pc.getCaisse() != null) ? (pc.getCaisse().getId() > 0 && pc.getCommission() != null) : false) {
                                if (controleAddPiece(vente, pc)) {
                                    if (pc.getCaissier() != null ? pc.getCaissier().getId() < 1 : true) {
                                        pc.setCaissier(currentUser.getUsers());
                                    }
                                    pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                    pc.setDatePaiement(pc.getDatePaimentPrevu());
                                    update = true;
                                } else {
                                    update = false;
                                }
                            } else if (msg) {
                                if (pc.getCommission() == null) {
                                    getErrorMessage("Cette pièce n'est rattachée à aucune commission");
                                } else {
                                    getErrorMessage("Aucune caisse n'a été trouvé pour ce règlement !");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    if (update) {
                        dao.update(pc);
                        if (vente != null ? vente.getReglements() != null : false) {
                            int idx = vente.getReglements().indexOf(pc);
                            if (idx >= 0) {
                                vente.getReglements().set(idx, pc);
                                update("table_reg_facture");
                            }
                        }
                        if (commercial != null ? commercial.getReglements() != null : false) {
                            int idx = commercial.getReglements().indexOf(pc);
                            if (idx >= 0) {
                                commercial.getReglements().set(idx, pc);
                                update("table_reg_facture");
                            }
                        }
                        if (msg) {
                            succes();
                        }
                        return true;
                    }

                }
            }
        } else if (msg) {
            getErrorMessage("La pièce n'a pas été sauvegardé !");
        }
        return false;
    }

    public void saveNewReglement() {
        boolean update = reglement.getId() > 0;
        try {
            PieceTresorerie bean = recopieViewPiece();
            YvsComptaCaissePieceCommission piec = UtilCom.buildPieceCommission(bean, currentUser);
            piec.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{bean.getCaisse().getId()}));
            if (bean.getId() < 1 ? (selectReglement != null ? !selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : true) : true) {
                piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            }
            if (piec.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                getWarningMessage("Seuls les règlements en espèces peuvent être validé avec ce schéma !");
            }
            if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                if (controleAccesCaisse(piec.getCaisse(), true)) {
                    if (piec.getCaissier() != null ? piec.getCaissier().getId() < 1 : true) {
                        piec.setCaissier(currentUser.getUsers());
                    }
                    piec.setDatePaiement(piec.getDatePaimentPrevu());
                } else {
                    piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    getWarningMessage("Vous n'avez pas d'autorisation pour efectuer des règlements dans cette caisse");
                }
            }
            piec.setCommission(commercial);
            piec = createNewPieceCaisse(commercial, piec);
            if (piec != null ? piec.getId() > 0 : false) {
                if (!update) {
                    commercial.getReglements().add(0, piec);
                } else {
                    int idx = commercial.getReglements().indexOf(piec);
                    if (idx >= 0) {
                        commercial.getReglements().set(idx, piec);
                    } else {
                        commercial.getReglements().add(piec);
                    }
                }
                int idx = commissions.indexOf(commercial);
                if (idx >= 0) {
                    int idx1 = commissions.get(idx).getReglements().indexOf(piec);
                    if (idx1 >= 0) {
                        commissions.get(idx).getReglements().set(idx1, piec);
                    } else {
                        commissions.get(idx).getReglements().add(0, piec);
                    }
                }
                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && reglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    reglerPieceTresorerie(commercial, piec, true);
                } else {
                    succes();
                }
            }
            resetFicheReglement();
        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComFacteurTaux> list = new ArrayList<>();
                YvsComFacteurTaux bean;
                for (Long ids : l) {
                    bean = facteurs.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);

                    if (bean.getId() == facteur.getId()) {
                        resetFicheFacteur();
                    }
                }
                facteurs.removeAll(list);
                succes();
                update("data_facteurs_commission");
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsComPlanCommission y) {
        selectPlan = y;
    }

    public void deleteBean_() {
        try {
            if (selectPlan != null) {
                dao.delete(selectPlan);
                plans.remove(selectPlan);
                if (selectPlan.getId() == plan.getId()) {
                    resetFiche();
                }
                succes();
                update("data_plan_commission");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanType(YvsComTypeGrille y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                types.remove(y);
                if (y.getId() == type.getId()) {
                    resetFicheType();
                }
                succes();
                update("data_type_grille_com");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanFacteur(YvsComFacteurTaux y) {
        selectFacteur = y;
    }

    public void deleteBeanFacteur_() {
        try {
            if (selectFacteur != null ? selectFacteur.getId() > 0 : false) {
                dao.delete(selectFacteur);
                facteurs.remove(selectFacteur);
                if (selectFacteur.getId() == facteur.getId()) {
                    resetFicheFacteur();
                }
                succes();
                update("data_facteurs_commission");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanTranche(YvsComTrancheTaux y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                type.getTranches().remove(y);
                if (y.getId() == tranche.getId()) {
                    resetFicheTranche();
                }
                succes();
                update("data_tranche_taux");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanCibles(YvsComCibleFacteurTaux y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                facteur.getCibles().remove(y);
                int idx = facteurs.indexOf(new YvsComFacteurTaux(facteur.getId()));
                if (idx > -1) {
                    YvsComFacteurTaux p = facteurs.get(idx);
                    p.getCibles().remove(y);
                }
                switch (y.getTableExterne()) {
                    case Constantes.SCR_ARTICLES: {
                        idx = facteur.getArticles().indexOf(new YvsBaseArticles(y.getId()));
                        if (idx > -1) {
                            facteur.getArticles().remove(idx);
                        }
                        idx = facteur.getArticlesSave().indexOf(new YvsBaseArticles(y.getId()));
                        if (idx > -1) {
                            facteur.getArticlesSave().remove(idx);
                        }
                        break;
                    }
                    case Constantes.SCR_CLIENTS: {
                        idx = facteur.getClients().indexOf(new YvsComClient(y.getId()));
                        if (idx > -1) {
                            facteur.getClients().remove(idx);
                        }
                        break;
                    }
                    case Constantes.SCR_DICTIONNAIRE: {
                        idx = facteur.getZones().indexOf(new YvsDictionnaire(y.getId()));
                        if (idx > -1) {
                            facteur.getZones().remove(idx);
                        }
                        break;
                    }
                    case Constantes.SCR_CATEGORIE: {
                        idx = facteur.getCategories().indexOf(new YvsBaseCategorieClient(y.getId()));
                        if (idx > -1) {
                            facteur.getCategories().remove(idx);
                        }
                        break;
                    }
                    default:
                        break;
                }
                if (y.getId() == cible.getId()) {
                    resetFicheCible();
                }
                succes();
                update("data_cible_facteur_taux");
                update("blog_cible_facture_taux");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanCible(long id) {
        try {
            if (id > 0) {
                int idx = facteur.getCibles().indexOf(new YvsComCibleFacteurTaux(id));
                if (idx > -1) {
                    YvsComCibleFacteurTaux y = facteur.getCibles().get(idx);
                    deleteBeanCibles(y);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBeanCommission() {
        try {
            if (selectCommission != null) {
                if (!selectCommission.getStatut().equals(Constantes.ETAT_ATTENTE)) {
                    getErrorMessage("La commission n'est pas dans un état éditable !");
                    return;
                }
                boolean comptabilise = dao.isComptabilise(selectCommission.getId(), Constantes.SCR_COMMISSION);
                if (comptabilise) {
                    getErrorMessage("Cette commission est déja comptabilisée !");
                    return;
                }
                if (selectCommission.getId() > 0) {
                    dao.delete(selectCommission);
                }
                commissions.remove(selectCommission);
                succes();
                update("data_calcul_commercial");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement_(YvsComptaCaissePieceCommission y) {
        selectReglement = y;
    }

    public void deleteBeanReglement_() {
        try {
            if (selectReglement != null) {
                if (!selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE)) {
                    getErrorMessage("La pièce de règlement n'est pas dans un état éditable !");
                    return;
                }
                boolean comptabilise = dao.isComptabilise(selectReglement.getId(), Constantes.SCR_CAISSE_COMMISSION);
                if (comptabilise) {
                    getErrorMessage("Cette piece est déja comptabilisé !");
                    return;
                }
                if (selectReglement.getId() > 0) {
                    dao.delete(selectReglement);
                }
                commercial.getReglements().remove(selectReglement);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComFacteurTaux y) {
        onClick = false;
        if (y != null) {
            selectFacteur = y;
            populateView(UtilCom.buildBeanFacteurTaux(y));
            if (y.getPeriodicites() != null ? !y.getPeriodicites().isEmpty() : false) {
                populateView(UtilCom.buildBeanPeriodeValideCommission(y.getPeriodicites().get(0)));
            }
            for (YvsComCibleFacteurTaux c : y.getCibles()) {
                addCible(c);
            }
        }
        update("blog_plan_commission");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComPlanCommission bean = (YvsComPlanCommission) ev.getObject();
            populateView(UtilCom.buildBeanPlanCommission(bean));
            update("form_plan_commission");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComTrancheTaux bean = (YvsComTrancheTaux) ev.getObject();
            populateView(UtilCom.buildBeanTrancheTaux(bean));
            update("form_grille_commission");
        }
    }

    public void unLoadOnViewTranche(UnselectEvent ev) {
        resetFicheTranche();
    }

    public void loadOnViewFacteur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComFacteurTaux y = (YvsComFacteurTaux) ev.getObject();
            onSelectObject(y);
            tabIds = facteurs.indexOf(y) + "";
        }
    }

    public void unLoadOnViewFacteur(UnselectEvent ev) {
        resetFicheFacteur();
    }

    public void loadOnViewType(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComTypeGrille bean = (YvsComTypeGrille) ev.getObject();
            populateView(UtilCom.buildBeanTypeGrille(bean));
            update("form_type_grille_com");
        }
    }

    public void unLoadOnViewType(UnselectEvent ev) {
        resetFicheType();
    }

    public void loadOnViewCible(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCibleFacteurTaux bean = (YvsComCibleFacteurTaux) ev.getObject();
            populateView(UtilCom.buildBeanCibleFacteur(bean));
            update("form_cible_facteur_taux");
        }
    }

    public void unLoadOnViewCible(UnselectEvent ev) {
        resetFicheCible();
    }

    public void loadOnViewFacture(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            facture = (YvsComDocVentes) ev.getObject();
            List<YvsComContenuDocVente> list = new ArrayList<>();
            list.addAll(facture.getContenus());
            facture.getContenus().clear();
            for (YvsComContenuDocVente c : list) {
                if (c.getComission() > 0) {
                    facture.getContenus().add(c);
                }
            }
        }
    }

    public void unLoadOnViewFacture(UnselectEvent ev) {
        facture = new YvsComDocVentes();
    }

    public void onSelectDistantCommercial(YvsComCommissionCommerciaux y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObjectCommercial(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Calcul des commissions", "modGescom", "smenCalculCommission", true);
            }
        }
    }

    public void onSelectObjectCommercial(YvsComCommissionCommerciaux y) {
        commercial = y;
        if (commercial.getReglements() == null) {
            commercial.setReglements(new ArrayList<YvsComptaCaissePieceCommission>());
        }
        List<Long> ids = dao.loadListByNameQueries("YvsComCommissionVente.findIdFactureByPeriode", new String[]{"periode"}, new Object[]{commercial.getPeriode()});
        if (ids.isEmpty()) {
            ids.add(-1L);
        }
        commercial.setFactures(dao.loadNameQueries("YvsComCommercialVente.findFactureByIdsCommercial", new String[]{"commercial", "ids"}, new Object[]{commercial.getCommerciaux(), ids}));
        commercial.setFacturesSave(new ArrayList<YvsComCommercialVente>(commercial.getFactures()));
    }

    public void loadOnViewCommercial(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectObjectCommercial((YvsComCommissionCommerciaux) ev.getObject());
        }
    }

    public void unLoadOnViewCommercial(UnselectEvent ev) {
        commercial = new YvsComCommissionCommerciaux();
    }

    public void loadOnViewCategorie(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseCategorieClient bean = (YvsBaseCategorieClient) ev.getObject();
            cible.setCategorie(UtilCom.buildBeanCategorieClient(bean));
            cible.setIdExterne(bean.getId());
            update("valeur_cible_facteur");
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient bean = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(bean));
        }
    }

    public void loadOnViewZone(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsDictionnaire bean = (YvsDictionnaire) ev.getObject();
            chooseZone(UtilGrh.buildBeanDictionnaire(bean));
        }
    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaCaissePieceCommission) ev.getObject();
            reglement = UtilCom.buildBeanPieceCommission(selectReglement);
            if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
                reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
            }
            loadCaissiers(selectReglement.getCaisse());
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFicheReglement();
    }

    public void changeStatutCommission(String statut) {
        changeStatutCommission(commercial, statut);
    }

    public void changeStatutCommission(YvsComCommissionCommerciaux commercial, String statut) {
        if (commercial != null ? commercial.getId() > 0 : false) {
            commercial.setStatut(statut);
            dao.update(commercial);
            int index = commissions.indexOf(commercial);
            if (index > -1) {
                commissions.set(index, commercial);
                update("data_calcul_commercial");
            }
        }
    }

    public void resetFicheReglement() {
        reglement = new PieceTresorerie();
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        tabIds_reglement = "";
        if (commercial != null) {
            double montant = commercial.getMontant();
            if (montant > 0) {
                double m = montant;
                for (YvsComptaCaissePieceCommission r : commercial.getReglements()) {
                    m -= r.getMontant();
                }
                reglement.setMontant(m > 0 ? m : 0);
            }
        }
        selectReglement = new YvsComptaCaissePieceCommission();
    }

    public void choosePermanent() {
        periode.setPeriodicite(Constantes.PERIODICITE_PERMANENT);
    }

    public void chooseType(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            int id = (int) ev.getNewValue();
            facteur.setTypeGrille(new TypeGrille(id));
            chooseType();
        }
    }

    public void chooseType() {
        if (facteur.getTypeGrille() != null) {
            if (facteur.getTypeGrille().getId() > 0) {
                int idx = types.indexOf(new YvsComTypeGrille(facteur.getTypeGrille().getId()));
                if (idx > -1) {
                    YvsComTypeGrille y = types.get(idx);
                    facteur.setTypeGrille(UtilCom.buildBeanTypeGrille(y));
                }
            } else if (facteur.getTypeGrille().getId() == -1) {
                openDialog("dlgGrille");
            }
        }
    }

    public void choosePlan(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            int id = (int) ev.getNewValue();
            facteur.setPlan(new PlanCommission(id));
            choosePlan();
        }
    }

    public void choosePlan() {
        if (facteur.getPlan() != null) {
            if (facteur.getPlan().getId() > 0) {
                int idx = plans.indexOf(new YvsComPlanCommission(facteur.getPlan().getId()));
                if (idx > -1) {
                    YvsComPlanCommission y = plans.get(idx);
                    facteur.setPlan(UtilCom.buildBeanPlanCommission(y));
                }
            } else if (facteur.getPlan().getId() == -1) {
                openDialog("dlgPlan");
            }
        }
    }

    public void chooseArticle(Articles bean) {
        cible.setArticle(bean);
        cible.setIdExterne(bean.getId());
        update("valeur_cible_facteur");
    }

    public void chooseClient(Client bean) {
        cible.setClient(bean);
        cible.setIdExterne(bean.getId());
        update("valeur_cible_facteur");
    }

    public void chooseZone(Dictionnaire bean) {
        cible.setZone(bean);
        cible.setIdExterne(bean.getId());
        update("valeur_cible_facteur");
    }

    public void searchArticle() {
        String num = cible.getArticle().getRefArt();
        cible.getArticle().setDesignation("");
        cible.getArticle().setError(true);
        cible.getArticle().setId(0);
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif("V", num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_commission");
                } else {
                    chooseArticle(y);
                }
                cible.getArticle().setError(false);
            }
        }
    }

    public void initArticles() {
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.initArticles("V", cible.getArticle());
        }
        update("data_articles_commission");
    }

    public void searchClient() {
        String num = cible.getClient().getCodeClient();
        cible.getClient().setId(0);
        cible.getClient().setError(true);
        cible.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_commission");
                } else {
                    chooseClient(y);
                }
                cible.getClient().setError(false);
            }
        }
    }

    public void initClients() {
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.initClients(cible.getClient());
        }
        update("data_client_commission");
    }

    public void searchZone() {
        String num = cible.getZone().getAbreviation();
        String titre = cible.getZone().getTitre();
        cible.getZone().setLibelle("");
        cible.getZone().setError(true);
        cible.getZone().setId(0);
        ManagedDico service = (ManagedDico) giveManagedBean("Mdico");
        if (service != null) {
            System.err.println("titre " + titre);
            System.err.println("titre " + titre);
            Dictionnaire y = service.findZoneActif(titre, num, true);
            if (service.getDictionnaires() != null ? !service.getDictionnaires().isEmpty() : false) {
                if (service.getDictionnaires().size() > 1) {
                    update("data_zone_commission");
                } else {
                    chooseZone(y);
                }
                cible.getZone().setError(false);
            }
        }
    }

    public void initZones() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.initZones(cible.getZone(), cible.getZone().getTitre());
        }
        update("data_zone_commission");
    }

    public void activeFacteur(YvsComFacteurTaux bean) {
        if (bean != null) {
            if (!bean.getActif() && !plan.isActif()) {
                getErrorMessage("Vous ne pouvez pas activer cette commission car le plan est inactif");
                return;
            }
            bean.setActif(!bean.getActif());
            facteurs.get(facteurs.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_com_facteur_taux SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void activePlanCommission(YvsComPlanCommission bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            plans.get(plans.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_com_plan_commission SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
    }

    public void activeUnique() {
        if (tranche.isUnique()) {
            tranche.setMontantMinimal(0);
            tranche.setMontantMaximal(Double.MAX_VALUE);
        }
    }

    public void onOpenManagedCible(String titre) {
        if (facteur != null ? facteur.getId() > 0 : false) {
            onClick = true;
            cible = new CibleFacteur();
            cible.setTableExterne(titre);
            openDialog("dlgCibleFacteur");
            update("form_cible_facteur_taux");
        } else {
            getErrorMessage("Vous devez selctionner un facteur");
        }
    }

    public String giveNameCible(char cible) {
        switch (cible) {
            case Constantes.CIBLE_TRANCHE_CA:
                return Constantes.CIBLE_TRANCHE_CA_NAME;
            case Constantes.CIBLE_TRANCHE_MARGE:
                return Constantes.CIBLE_TRANCHE_MARGE_NAME;
            case Constantes.CIBLE_TRANCHE_REMISE:
                return Constantes.CIBLE_TRANCHE_REMISE_NAME;
        }
        return "";
    }

    public String giveNameBase(char base) {
        switch (base) {
            case Constantes.BASE_COMMISSION_CA_HT:
                return Constantes.BASE_COMMISSION_CA_HT_NAME;
            case Constantes.BASE_COMMISSION_CA_TTC:
                return Constantes.BASE_COMMISSION_CA_TTC_NAME;
            case Constantes.BASE_COMMISSION_MARGE:
                return Constantes.BASE_COMMISSION_MARGE_NAME;
        }
        return "";
    }

    public String giveNamePorte(char champ) {
        switch (champ) {
            case Constantes.APPICATION_COMMISSION_FACTURE_REGLE:
                return Constantes.APPICATION_COMMISSION_FACTURE_REGLE_NAME;
            case Constantes.APPICATION_COMMISSION_FACTURE_VALIDE:
                return Constantes.APPICATION_COMMISSION_FACTURE_VALIDE_NAME;
            case Constantes.APPICATION_COMMISSION_FACTURE_ENCOURS:
                return Constantes.APPICATION_COMMISSION_FACTURE_ENCOURS_NAME;
        }
        return "";
    }

    public double valueTaux(YvsComPlanCommission plan, YvsComEnteteDocVente e, YvsComDocVentes d, Date dateDebut, Date dateFin) {
        double taux = 0;
        if (plan != null ? plan.getId() > 0 : false) {

        }
        return taux;
    }

    public String giveLibelleCible(String table, long id) {
        String re;
        switch (table) {
            case Constantes.SCR_ARTICLES: {
                int idx = facteur.getArticles().indexOf(new YvsBaseArticles(id));
                if (idx > -1) {
                    YvsBaseArticles y = facteur.getArticles().get(idx);
                    re = (y != null) ? y.getDesignation() + " [" + y.getRefArt() + "] " : "";
                } else {
                    YvsBaseArticles y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{id});
                    re = (y != null) ? y.getDesignation() + " [" + y.getRefArt() + "] " : "";
                }
                break;
            }
            case Constantes.SCR_CLIENTS: {
                int idx = facteur.getClients().indexOf(new YvsComClient(id));
                if (idx > -1) {
                    YvsComClient y = facteur.getClients().get(idx);
                    re = (y != null) ? y.getNom_prenom() : "";
                } else {
                    YvsComClient y = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{id});
                    re = (y != null) ? y.getNom_prenom() : "";
                }
                break;
            }
            case Constantes.SCR_DICTIONNAIRE: {
                int idx = facteur.getZones().indexOf(new YvsDictionnaire(id));
                if (idx > -1) {
                    YvsDictionnaire y = facteur.getZones().get(idx);
                    re = (y != null) ? y.getLibele() : "";
                } else {
                    YvsDictionnaire y = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{id});
                    re = (y != null) ? y.getLibele() : "";
                }
                break;
            }
            case Constantes.SCR_CATEGORIE: {
                int idx = facteur.getCategories().indexOf(new YvsBaseCategorieClient(id));
                if (idx > -1) {
                    YvsBaseCategorieClient y = facteur.getCategories().get(idx);
                    re = (y != null) ? y.getDesignation() : "";
                } else {
                    YvsBaseCategorieClient y = (YvsBaseCategorieClient) dao.loadOneByNameQueries("YvsBaseCategorieClient.findById", new String[]{"id"}, new Object[]{id});
                    re = (y != null) ? y.getDesignation() : "";
                }
                break;
            }
            default:
                re = "";
                break;
        }
        return re;
    }

    @Override
    public void updateBean() {
        progress = 0;
        for (int i = 0; i < 10000; i++) {
            progress = (long) ((double) i * (double) 100) / 10000;
            System.err.println("progress : " + progress);
            update("progressBar");
        }
    }

    public void calculCommission_NEW(boolean force, boolean load) {
        YvsComPeriodeObjectif periode = null;
        ManagedPeriodeObjectif w = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
        if (w != null) {
            int index = w.getPeriodes().indexOf(new YvsComPeriodeObjectif(idPeriode));
            if (index > -1) {
                periode = w.getPeriodes().get(index);
            }
        }
        if (periode != null ? periode.getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la période");
            return;
        }
        PaginatorResult<YvsComCommissionVente> p = new PaginatorResult<>();
        p.addParam(new ParametreRequete("y.periode", "periode", new YvsComPeriodeObjectif(idPeriode), "=", "AND"));
        if (idCommercial > 0) {
//                    p.addParam(new ParametreRequete("y.facture.commercial", "periode", new YvsComPeriodeObjectif(idPeriode), "=", "AND"));
        }
        if (idPoint > 0) {
            p.addParam(new ParametreRequete("y.facture.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(idPoint), "=", "AND"));
        }
        boolean continu = force;
        if (!force) {
            if (!load) {
                List<YvsComCommissionVente> list = p.executeDynamicQuery("y", "y", "YvsComCommissionVente y", null, true, true, 0, dao);
                if (list != null ? !list.isEmpty() : false) {
                    openDialog("dlgConfirmCalcul");
                    return;
                }
                continu = true;
            }
        }

        List<YvsComComerciale> commerciaux = new ArrayList<>();
        YvsComComerciale com;
        openDialog("dlgPBar");
        progress = 0;
        if (continu) {
            String commercial = "";
            if (idCommercial > 0) {
                commercial = idCommercial + "";
            }
            String point = "";
            if (idPoint > 0) {
                point = idPoint + "";
            }
            String query = "SELECT DISTINCT y.id FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id"
                    + " LEFT JOIN yvs_compta_caisse_piece_vente c ON c.vente = y.id INNER JOIN yvs_com_commercial_vente o ON o.facture = y.id INNER JOIN yvs_com_comerciale co ON co.id = o.commercial INNER JOIN yvs_agences a ON e.agence = a.id"
                    + " WHERE o.commercial IS NOT NULL AND co.commission IS NOT NULL AND y.type_doc = 'FV' AND ((y.statut NOT IN ('E', 'A') AND a.societe = ? AND e.date_entete BETWEEN ? AND ?) OR (c.statut_piece = 'P' AND c.date_paiement BETWEEN ? AND ?))";
            List<Options> params = new ArrayList<>();
            params.add(new Options(currentAgence.getSociete().getId(), 1));
            params.add(new Options(periode.getDateDebut(), 2));
            params.add(new Options(periode.getDateFin(), 3));
            params.add(new Options(periode.getDateDebut(), 4));
            params.add(new Options(periode.getDateFin(), 5));
            if (idCommercial > 0) {
                query += " AND o.commercial = ?";
                params.add(new Options(idCommercial, 6));
            }
            if (idPoint > 0) {
                query += " AND cp.point = ?";
                params.add(new Options(idPoint, 7));
            }
            Long max = (Long) dao.loadObjectBySqlQuery(query.replace("DISTINCT y.id", "COUNT(DISTINCT y.id)"), params.toArray(new Options[params.size()]));
            List<Object> ids = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
            for (progress = 0; progress < ids.size(); progress++) {
                Object facture = ids.get((int) progress);
                List<Object> result = dao.loadListBySqlQuery("select vente from com_calcul_commission(?,?,?,?) as vente", new Options[]{new Options(facture + "", 1), new Options(commercial, 2), new Options(point, 3), new Options(idPeriode, 4)});
                if (result != null ? !result.isEmpty() : false) {
                    System.err.println("facture : " + facture + " - result : " + result);
                    for (Object id : result) {
                        YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{Long.valueOf(id.toString())});
                        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                            Double commission = (Double) dao.loadObjectByNameQueries("YvsComCommissionVente.findSumOne", new String[]{"facture", "periode"}, new Object[]{y, periode});
                            y.setCommision(commission);
                            for (YvsComCommercialVente c : y.getCommerciaux()) {
                                com = new YvsComComerciale(c.getCommercial());
                                int idx = commerciaux.indexOf(com);
                                if (idx > -1) {
                                    com = commerciaux.get(idx);
                                    com.getFactures().add(c);
                                    commerciaux.set(idx, com);
                                } else {
                                    com.getFactures().clear();
                                    com.getFactures().add(c);
                                    commerciaux.add(com);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            List<YvsComCommissionVente> list = p.executeDynamicQuery("y", "y", "YvsComCommissionVente y", null, true, true, 0, dao);
            for (YvsComCommissionVente o : list) {
                YvsComDocVentes y = new YvsComDocVentes(o.getFacture());
                y.setCommision(o.getMontant());
                for (YvsComCommercialVente c : y.getCommerciaux()) {
                    com = new YvsComComerciale(c.getCommercial());
                    int idx = commerciaux.indexOf(com);
                    if (idx > -1) {
                        com = commerciaux.get(idx);
                        com.getFactures().add(c);
                        commerciaux.set(idx, com);
                    } else {
                        com.getFactures().clear();
                        com.getFactures().add(c);
                        commerciaux.add(com);
                    }
                }
            }
        }
        YvsComCommissionCommerciaux y;
        for (YvsComComerciale commercial : commerciaux) {
            y = (YvsComCommissionCommerciaux) dao.loadOneByNameQueries("YvsComCommissionCommerciaux.findOne", new String[]{"commerciaux", "periode"}, new Object[]{commercial, periode});
            if (y != null ? y.getId() < 1 : true) {
                y = new YvsComCommissionCommerciaux(id--);
                y.setCommerciaux(commercial);
                y.setPeriode(periode);
                y.setAuthor(currentUser);
            }
            double montant = commercial.getSommeCommission();
            y.setDateUpdate(new Date());
            y.setNew_(y.getMontant() != montant);
            y.setMontant(montant);
            int index = commissions.indexOf(y);
            if (index > -1) {
                commissions.set(index, y);
            } else {
                commissions.add(y);
            }
            if (autoCalcul) {
                saveCommission(y, false);
            }
        }
    }

    public void calculCommission(boolean force, boolean load) {
        YvsComPeriodeObjectif periode = null;
        ManagedPeriodeObjectif w = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
        if (w != null) {
            int index = w.getPeriodes().indexOf(new YvsComPeriodeObjectif(idPeriode));
            if (index > -1) {
                periode = w.getPeriodes().get(index);
            }
        }
        if (periode != null ? periode.getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la période");
            return;
        }
        String query = "SELECT COUNT(y.id) FROM yvs_com_commission_vente y INNER JOIN yvs_com_commercial_vente co ON y.facture = co.facture INNER JOIN yvs_com_doc_ventes d ON d.id = y.facture"
                + " INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id"
                + " WHERE y.periode = ?";
        List<Options> params = new ArrayList<>();
        params.add(new Options(idPeriode, 1));
        if (idPoint > 0) {
            params.add(new Options(idPoint, 2));
        }
        if (idCommercial > 0) {
            query += " AND co.commercial = ?";
            params.add(new Options(idCommercial, 3));
        }

        boolean continu = force;
        if (!force) {
            if (!load) {
                Long count = (Long) dao.loadObjectBySqlQuery(query, params.toArray(new Options[params.size()]));
                if (count != null ? count > 0 : false) {
                    openDialog("dlgConfirmCalcul");
                    return;
                }
                continu = true;
            }
        }

        List<YvsComComerciale> commerciaux = new ArrayList<>();
        progress = 0;
        if (continu) {
            String commercial = "";
            if (idCommercial > 0) {
                commercial = idCommercial + "";
            }
            String point = "";
            if (idPoint > 0) {
                point = idPoint + "";
            }
            dao.loadListBySqlQuery("select vente from com_calcul_commission(?,?,?,?) as vente", new Options[]{new Options("", 1), new Options(commercial, 2), new Options(point, 3), new Options(periode.getId(), 4)});
        }

        query = query.replace("COUNT(y.id)", "co.commercial, (SUM(y.montant) * (co.taux / 100))") + " GROUP BY co.commercial, co.taux";
        List<Object[]> ids = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
        YvsComComerciale commercial;
        for (Object[] line : ids) {
            commercial = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findById", new String[]{"id"}, new Object[]{line[0]});
            if (commercial != null ? commercial.getId() > 0 : false) {
                int index = commerciaux.indexOf(commercial);
                if (index < 0) {
                    commercial.setSommeCommission((double) line[1]);
                    commerciaux.add(commercial);
                } else {
                    commercial = commerciaux.get(index);
                    commercial.setSommeCommission(commercial.getSommeCommission() + (double) line[1]);
                    commerciaux.set(index, commercial);
                }
            }
        }

        YvsComCommissionCommerciaux y;
        for (YvsComComerciale line : commerciaux) {
            y = (YvsComCommissionCommerciaux) dao.loadOneByNameQueries("YvsComCommissionCommerciaux.findOne", new String[]{"commerciaux", "periode"}, new Object[]{line, periode});
            if (y != null ? y.getId() < 1 : true) {
                y = new YvsComCommissionCommerciaux(id--);
                y.setCommerciaux(line);
                y.setPeriode(periode);
                y.setAuthor(currentUser);
            }
            double montant = line.getSommeCommission();
            y.setDateUpdate(new Date());
            y.setNew_(y.getMontant() != montant);
            y.setMontant(montant);
            int index = commissions.indexOf(y);
            if (index > -1) {
                commissions.set(index, y);
            } else {
                commissions.add(y);
            }
            if (autoCalcul && y.isNew_()) {
                saveCommission(y, false);
            }
        }
    }

    public void downloadCommission(boolean total) {
        try {
            if (!total && idCommercial < 1) {
                getErrorMessage("Veuillez choisir un commercial");
                return;
            }
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("COMMERCIAL", (int) idCommercial);
            param.put("PERIODE", (int) idPeriode);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR + (currentAgence.getSociete().getPrintWithEntete() ? "" : "empty" + FILE_SEPARATOR));
            String report = total ? "commission_vente_total" : "commission_vente";
            executeReport(report, param, true);
        } catch (Exception ex) {
            getException("ManagedCommission (downloadCommission)", ex);
        }
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            caissiers = w.loadCaissiers(y);
        }
        if (y != null ? y.getId() > 0 : false) {
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) && caissiers.contains(currentUser.getUsers())) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
    }

    public void chooseCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();
            caissiers.clear();
            reglement.setCaissier(new Users());
            if (service != null && id > 0) {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx > -1) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
                    loadCaissiers(y);
                }
            }
        }
    }

    public boolean majCommission(YvsComCommissionCommerciaux y, boolean msg) {
        try {
            IYvsComCommissionCommerciaux impl = (IYvsComCommissionCommerciaux) IEntitiSax.createInstance("IYvsComCommissionCommerciaux", dao);
            if (impl == null) {
                return false;
            }
            impl.update(y);
            y.setNew_(false);
            int index = commissions.indexOf(y);
            if (index > -1) {
                commissions.set(index, y);
            }
            if (msg) {
                succes();
            }
            return true;
        } catch (Exception ex) {
            getException("majCommission", ex);
        }
        return false;
    }

    public boolean saveCommission(YvsComCommissionCommerciaux y, boolean msg) {
        try {
            if (y == null) {
                return false;
            }
            if (y.getId() > 0) {
                return majCommission(y, msg);
            }
            IYvsComCommissionCommerciaux impl = (IYvsComCommissionCommerciaux) IEntitiSax.createInstance("IYvsComCommissionCommerciaux", dao);
            if (impl == null) {
                return false;
            }
            YvsComPeriodeObjectif periode = null;
            ManagedPeriodeObjectif w = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
            if (w != null) {
                int index = w.getPeriodes().indexOf(new YvsComPeriodeObjectif(idPeriode));
                if (index > -1) {
                    periode = w.getPeriodes().get(index);
                }
            }
            if (periode != null ? periode.getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous devez precisez la période");
                }
                return false;
            }
            long id = y.getId();
            String reference = genererReference(yvs.dao.salaire.service.Constantes.TYPE_COM_NAME, periode.getDateDebut());
            if (!Util.asString(reference)) {
                return false;
            }
            y.setStatut(Constantes.ETAT_EDITABLE);
            y.setStatutRegle(Constantes.ETAT_ATTENTE);
            y.setNumero(reference);
            y.setAuthor(currentUser);
            ResultatAction<YvsComCommissionCommerciaux> result = impl.save(y);
            if (result != null ? result.isResult() : false) {
                y = (YvsComCommissionCommerciaux) result.getData();
            }
            y.setNew_(false);
            int index = commissions.indexOf(new YvsComCommissionCommerciaux(id));
            if (index > -1) {
                commissions.set(index, y);
            }
            if (msg) {
                succes();
            }
            return true;
        } catch (Exception ex) {
            getException("saveCommission", ex);
        }
        return false;
    }

    public void onRowToggleFacture(ToggleEvent evt) {
        YvsComCommercialVente y = (YvsComCommercialVente) evt.getData();
        y.getFacture().setContenus(dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{y.getFacture()}));
        int index = commercial.getFactures().indexOf(y);
        if (index > -1) {
            commercial.getFactures().set(index, y);
        }
    }

    public double loadCaVente(YvsComDocVentes y) {
        double valeur = 0;
        if (y != null) {
            if (y.getId() > 0) {
                valeur = dao.loadCaVente(y.getId());
            }
            y.setMontantTTC(valeur);
        }
        return valeur;
    }

    public double loadCaCommercial(YvsComCommissionCommerciaux y) {
        double valeur = 0;
        if (y != null) {
            if ((y.getCommerciaux() != null ? y.getCommerciaux().getId() > 0 : false) && y.getPeriode() != null) {
                valeur = dao.loadCaCommercial(y.getCommerciaux().getId(), y.getPeriode().getDateDebut(), y.getPeriode().getDateFin());
            }
            y.setChiffreAffaire(valeur);
        }
        return valeur;
    }

    public void onSearchFacture() {
        if (Util.asString(factureSearch)) {
            String numero = factureSearch.replace("%", "");
            if (Util.asString(numero)) {
                commercial.getFactures().clear();
                for (YvsComCommercialVente y : commercial.getFacturesSave()) {
                    boolean add = false;
                    if (factureSearch.startsWith("%") && factureSearch.endsWith("%")) {
                        add = y.getFacture().getNumDoc().toUpperCase().contains(numero.toUpperCase());
                    } else if (factureSearch.startsWith("%")) {
                        add = y.getFacture().getNumDoc().toUpperCase().endsWith(numero.toUpperCase());
                    } else if (factureSearch.endsWith("%")) {
                        add = y.getFacture().getNumDoc().toUpperCase().startsWith(numero.toUpperCase());
                    } else {
                        add = y.getFacture().getNumDoc().toUpperCase().equals(numero.toUpperCase());
                    }
                    if (add) {
                        commercial.getFactures().add(y);
                    }
                }
                return;
            }
        }
        commercial.setFactures(new ArrayList<YvsComCommercialVente>(commercial.getFacturesSave()));
    }

    public void printPiece(YvsComptaCaissePieceCommission y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                String file = "";
                double montant = y.getMontant();
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(montant));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("pc_commission", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedCommission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findFacteurByArticle() {
        System.out.println("articleSearch : " + articleSearch);
        facteur.getArticles().clear();
        if (Util.asString(articleSearch)) {
            for (YvsBaseArticles c : facteur.getArticlesSave()) {
                if (c.getRefArt().toUpperCase().contains(articleSearch.toUpperCase()) || c.getDesignation().toUpperCase().contains(articleSearch.toUpperCase())) {
                    facteur.getArticles().add(c);
                }
            }
        } else {
            facteur.getArticles().addAll(facteur.getArticlesSave());
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.commission.PlanCommission;
import yvs.commercial.UtilCom;
import yvs.commercial.rrr.PlanRistourne;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.tiers.YvsBaseTiersTemplate;

import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedTiersTemplate extends Managed<TiersTemplate, YvsBaseTiersTemplate> implements Serializable {

    private TiersTemplate template = new TiersTemplate();
    private List<YvsBaseTiersTemplate> templates;
    private YvsBaseTiersTemplate selectTemplate;

    private List<YvsComPlanRistourne> ristournes;
    private List<YvsComPlanCommission> commissions;
    private List<YvsBasePlanTarifaire> tarifaires;

    private String tabIds;

    private long paysSearch, vilSearch, sectSearch;
    private String typeSearch, nameSearch;
    private boolean _first = true;

    public ManagedTiersTemplate() {
        templates = new ArrayList<>();
        ristournes = new ArrayList<>();
        commissions = new ArrayList<>();
        tarifaires = new ArrayList<>();
    }

    public long getPaysSearch() {
        return paysSearch;
    }

    public void setPaysSearch(long paysSearch) {
        this.paysSearch = paysSearch;
    }

    public long getVilSearch() {
        return vilSearch;
    }

    public void setVilSearch(long vilSearch) {
        this.vilSearch = vilSearch;
    }

    public long getSectSearch() {
        return sectSearch;
    }

    public void setSectSearch(long SectSearch) {
        this.sectSearch = SectSearch;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public TiersTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TiersTemplate template) {
        this.template = template;
    }

    public List<YvsBaseTiersTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<YvsBaseTiersTemplate> templates) {
        this.templates = templates;
    }

    public YvsBaseTiersTemplate getSelectTemplate() {
        return selectTemplate;
    }

    public void setSelectTemplate(YvsBaseTiersTemplate selectTemplate) {
        this.selectTemplate = selectTemplate;
    }

    public List<YvsComPlanRistourne> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<YvsComPlanRistourne> ristournes) {
        this.ristournes = ristournes;
    }

    public List<YvsComPlanCommission> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<YvsComPlanCommission> commissions) {
        this.commissions = commissions;
    }

    public List<YvsBasePlanTarifaire> getTarifaires() {
        return tarifaires;
    }

    public void setTarifaires(List<YvsBasePlanTarifaire> tarifaires) {
        this.tarifaires = tarifaires;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadTemplate(true, true);
//        loadTarifaire();
        loadRistourne();
        loadCommission();
        _first = true;
    }

    public void load(Boolean client) {

    }

    public void loadTemplate(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        String query = "YvsBaseTiersTemplate y LEFT JOIN FETCH y.compteCollectif LEFT JOIN FETCH y.ristourne LEFT JOIN FETCH y.comission LEFT JOIN FETCH y.categorieComptable "
                + " LEFT JOIN FETCH y.pays LEFT JOIN FETCH y.ville LEFT JOIN FETCH y.secteur LEFT JOIN FETCH y.mdr LEFT JOIN FETCH y.author LEFT JOIN FETCH y.author.users ";
        templates = paginator.executeDynamicQuery("y", "y", query, "y.id", avance, init, (int) imax, dao);
    }

    public void loadTemplate(String type) {
        if (_first) {
            clearParams();
        }
        typeSearch = null;
        if (type != null ? type.trim().length() > 0 : false) {
            switch (type) {
                case "C":
                    typeSearch = "MC";
                    break;
                case "F":
                    typeSearch = "MF";
                    break;
                default:
                    typeSearch = type;
                    break;
            }
        }
        _first = false;
        addParamType(typeSearch);
    }

    private void loadRistourne() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComPlanRistourne.findAll";
        ristournes = dao.loadNameQueries(nameQueri, champ, val);
    }

    private void loadCommission() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComPlanCommission.findAll";
        commissions = dao.loadNameQueries(nameQueri, champ, val);
    }

    private void loadTarifaire() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsBasePlanTarifaire.findAll";
        tarifaires = dao.loadNameQueries(nameQueri, champ, val);
    }

    public YvsBaseTiersTemplate buildTiersTemplate(TiersTemplate y) {
        YvsBaseTiersTemplate t = new YvsBaseTiersTemplate();
        if (y != null) {
            t.setId(y.getId());
            t.setAddNom(y.isAddNom());
            t.setAddPrenom(y.isAddPrenom());
            t.setAddSecteur(y.isAddSecteur());
            t.setAddSeparateur(y.isAddSeparateur());
            t.setApercu(y.getApercu());
            t.setSeparateur(y.getSeparateur());
            t.setTailleNom(y.getTailleNom());
            t.setTaillePrenom(y.getTaillePrenom());
            t.setTailleSecteur(y.getTailleSecteur());
            t.setLibelle(y.getLibelle());
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
            t.setType(String.valueOf(y.getType()) != null ? (String.valueOf(y.getType()).trim().length() > 0 ? y.getType() : 'M') : 'M');
            if (y.getCategorieComptable() != null ? y.getCategorieComptable().getId() > 0 : false) {
                t.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()));
            }
            if (y.getCategorieTarifaire() != null ? y.getCategorieTarifaire().getId() > 0 : false) {
//                t.setCategorieTarifaire(new YvsComPlanRemise(y.getCategorieTarifaire().getId()));
            }
            if (y.getCompteCollectif() != null ? y.getCompteCollectif().getId() > 0 : false) {
                t.setCompteCollectif(new YvsBasePlanComptable(y.getCompteCollectif().getId(), y.getCompteCollectif().getNumCompte()));
            }
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                t.setMdr(new YvsBaseModeReglement((long)y.getModel().getId()));
            }
            if (y.getCommission() != null ? y.getCommission().getId() > 0 : false) {
                t.setComission(new YvsComPlanCommission(y.getCommission().getId()));
            }
            if (y.getRistourne() != null ? y.getRistourne().getId() > 0 : false) {
                t.setRistourne(new YvsComPlanRistourne(y.getRistourne().getId()));
            }
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (y.getSecteur() != null ? y.getSecteur().getId() > 0 : false) {
                if (m != null) {
                    t.setSecteur(m.getSecteurs().get(m.getSecteurs().indexOf(new YvsDictionnaire(y.getSecteur().getId()))));
                } else {
                    t.setSecteur(new YvsDictionnaire(y.getSecteur().getId()));
                }
            }
            if (y.getVille() != null ? y.getVille().getId() > 0 : false) {
                if (m != null) {
                    t.setVille(m.getVilles().get(m.getVilles().indexOf(new YvsDictionnaire(y.getVille().getId()))));
                } else {
                    t.setVille(new YvsDictionnaire(y.getVille().getId()));
                }
            }
            if (y.getPays() != null ? y.getPays().getId() > 0 : false) {
                if (m != null) {
                    t.setPays(m.getPays().get(m.getPays().indexOf(new YvsDictionnaire(y.getPays().getId()))));
                } else {
                    t.setPays(new YvsDictionnaire(y.getPays().getId()));
                }
            }
            t.setAgence(currentAgence);
            t.setAuthor(currentUser);
            t.setNew_(true);
        }
        return t;
    }

    public YvsDictionnaire buildDictionnaire(Dictionnaire d) {
        YvsDictionnaire r = new YvsDictionnaire();
        if (d != null) {
            r.setId(d.getId());
            r.setLibele(d.getLibelle());
            r.setTitre(d.getTitre());
            r.setActif(true);
            r.setSupp(false);
            if (d.getParent() != null ? d.getParent().getId() > 0 : false) {
                r.setParent(new YvsDictionnaire(d.getParent().getId()));
            }
            r.setAbreviation(d.getAbreviation());
            r.setSociete(currentAgence.getSociete());
            r.setAuthor(currentUser);
        }
        return r;
    }

    public Dictionnaire recopie(Dictionnaire y, Dictionnaire p, String Titre) {
        Dictionnaire d = new Dictionnaire();
        d.setId(y.getId());
        d.setLibelle(y.getLibelle());
        d.setParent(p);
        d.setAbreviation(y.getAbreviation());
        d.setTitre(Titre);
        return d;
    }

    @Override
    public boolean controleFiche(TiersTemplate bean) {
        if (bean.getApercu() == null || bean.getApercu().trim().equals("")) {
            getErrorMessage("Vous devez créer un modèle de code (Apercu)");
            return false;
        }
        if (bean.getSecteur_() != null ? bean.getSecteur_().getId() < 1 : true) {
            getErrorMessage("Vous devez choisir une zone");
            return false;
        }
        if (bean.getPays() != null ? bean.getPays().getId() > 0 : false) {
            if (bean.getVille() != null ? bean.getVille().getId() > 0 : false) {
                if (bean.getSecteur() != null ? bean.getSecteur().getId() > 0 : false) {
                    champ = new String[]{"societe", "pays", "ville", "secteur", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getPays().getId()), new YvsDictionnaire(bean.getVille().getId()), new YvsDictionnaire(bean.getSecteur().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByPaysVilleSecteur";
                } else {
                    champ = new String[]{"societe", "pays", "ville", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getPays().getId()), new YvsDictionnaire(bean.getVille().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByPaysVilleOthersNull";
                }
            } else {
                if (bean.getSecteur() != null ? bean.getSecteur().getId() > 0 : false) {
                    champ = new String[]{"societe", "pays", "secteur", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getPays().getId()), new YvsDictionnaire(bean.getSecteur().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByPaysSecteur";
                } else {
                    champ = new String[]{"societe", "pays", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getPays().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByPaysOthersNull";
                }
            }
        } else {
            if (bean.getVille() != null ? bean.getVille().getId() > 0 : false) {
                if (bean.getSecteur() != null ? bean.getSecteur().getId() > 0 : false) {
                    champ = new String[]{"societe", "ville", "secteur", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getVille().getId()), new YvsDictionnaire(bean.getSecteur().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByVilleSecteur";
                } else {
                    champ = new String[]{"societe", "ville", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getVille().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByVille";
                }
            } else {
                if (bean.getSecteur() != null ? bean.getSecteur().getId() > 0 : false) {
                    champ = new String[]{"societe", "secteur", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(bean.getSecteur().getId()), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findBySecteur";
                } else {
                    champ = new String[]{"societe", "pays", "type"};
                    val = new Object[]{currentAgence.getSociete(), new YvsDictionnaire(0L), bean.getType()};
                    nameQueri = "YvsBaseTiersTemplate.findByPaysOthersNull";
                }
            }
        }
        List<YvsBaseTiersTemplate> l = dao.loadNameQueries(nameQueri, champ, val);
        if (l != null ? !l.isEmpty() : false) {
            if (!l.get(0).getId().equals(template.getId())) {
                getErrorMessage("Ce template existe déja");
                return false;
            }
        }

        return true;
    }

    public boolean controleFiche(Dictionnaire bean) {
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer la désignation");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(TiersTemplate bean) {
        cloneObject(template, bean);
    }

    @Override
    public void resetFiche() {
        template = new TiersTemplate();
        selectTemplate = new YvsBaseTiersTemplate();
        tabIds = "";
        update("txt_apercu_template");
        update("blog_form_template");
    }

    public void reset(Dictionnaire d) {
        resetFiche(d);
        d.setParent(new Dictionnaire());
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(template)) {
                selectTemplate = buildTiersTemplate(template);
                if (!template.isUpdate()) {
                    selectTemplate.setId(null);
                    selectTemplate = (YvsBaseTiersTemplate) dao.save1(selectTemplate);
                    templates.add(0, selectTemplate);
                } else {
                    dao.update(selectTemplate);
                    if (templates.contains(selectTemplate)) {
                        templates.set(templates.indexOf(selectTemplate), selectTemplate);
                    }
                }
                template.setUpdate(true);
                template.setId(selectTemplate.getId());
                succes();
                actionOpenOrResetAfter(this);
                update("data_template");
                update("data_template_hist");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Error " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseTiersTemplate> list = new ArrayList<>();
                YvsBaseTiersTemplate bean;
                for (Long ids : l) {
                    bean = templates.get(ids.intValue());
                    list.add(bean);
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    if (bean.getId() == template.getId()) {
                        resetFiche();
                    }
                }
                templates.removeAll(list);
                succes();
                update("data_template");
                update("data_template_hist");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    public void deleteBean_(YvsBaseTiersTemplate y) {
        selectTemplate = y;
    }

    public void deleteBean_() {
        try {
            if (selectTemplate != null) {
                dao.delete(selectTemplate);
                templates.remove(selectTemplate);

                succes();
                update("data_template");
                update("data_template_hist");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            System.err.println("Error Suppresion : " + ex.getMessage());
        }
    }

    @Override
    public void onSelectObject(YvsBaseTiersTemplate bean) {
        selectTemplate = bean;
        populateView(UtilCom.buildBeanTiersTemplate(bean));
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(bean.getPays());
            m.loadSecteurs(bean.getVille());
        }
        update("blog_form_template");
        update("txt_apercu_template");
        update("txt_pays_ville");
        update("txt_pays_secteur");
        update("txt_ville_secteur");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTiersTemplate bean = (YvsBaseTiersTemplate) ev.getObject();
            onSelectObject(bean);
            execute("collapseForm('template')");
            tabIds = templates.indexOf(bean) + "";
            execute("onselectLine(" + tabIds + ",'template')");
            execute("oncollapsesForm('template')");

        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_template");
        update("txt_apercu_template");
        update("txt_pays_ville");
        update("txt_pays_secteur");
        update("txt_ville_secteur");
    }

    public void chooseRistourne() {
        if (template.getRistourne() != null ? template.getRistourne().getId() > 0 : false) {
            YvsComPlanRistourne y = ristournes.get(ristournes.indexOf(new YvsComPlanRistourne(template.getRistourne().getId())));
            PlanRistourne d = UtilCom.buildBeanPlanRistourne(y);
            cloneObject(template.getRistourne(), d);
        }
    }

    public void chooseCommission() {
        if (template.getCommission() != null ? template.getCommission().getId() > 0 : false) {
            YvsComPlanCommission y = commissions.get(commissions.indexOf(new YvsComPlanCommission(template.getCommission().getId())));
            PlanCommission d = UtilCom.buildBeanPlanCommission(y);
            cloneObject(template.getCommission(), d);
        }
    }

    public void chooseTarifaire() {
        if (template.getCategorieTarifaire() != null ? template.getCategorieTarifaire().getId() > 0 : false) {
            YvsBasePlanTarifaire y = tarifaires.get(tarifaires.indexOf(new YvsBasePlanTarifaire(template.getCategorieTarifaire().getId())));
//            PlanTarifaireClient d = UtilCom.buildBeanPlanTarifaireClient(y);
//            cloneObject(template.getCategorieTarifaire(), d);
        }
    }

    public void choosePays() {
        update("txt_pays_ville");
        update("txt_pays_secteur");
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.choosePays(template.getPays().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(template.getPays(), d);
            }
        }
    }

    public void chooseVille() {
        update("txt_ville_secteur");
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseVille(template.getPays(), template.getVille().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(template.getVille(), d);
            }
        }
    }

    public void chooseSecteur() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(template.getVille(), template.getSecteur().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(template.getSecteur(), d);
            }
        }
    }

    public void chooseType() {
        template.setCategorieComptable(new CategorieComptable());
        ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
        if (m != null) {
            if (template.getType() == 'C') {
                m.loadComptable(Constantes.VENTE);
            } else if (template.getType() == 'F') {
                m.loadComptable(Constantes.ACHAT);
            } else {
                m.loadComptable(null);
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void ecouteSaisieCG() {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
        if (service != null) {
            service.findCompteByNum(template.getCompteCollectif().getNumCompte());
            template.getCompteCollectif().setError(true);
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    template.getCompteCollectif().setError(false);
                    if (service.getListComptes().size() == 1) {
                        cloneObject(template.getCompteCollectif(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        openDialog("dlgCmpteG");
                        update("table_cpt_G_template_T");
                    }
                }
            }
        }

    }

    public void choisirCompteG(SelectEvent ev) {
        if (ev != null) {
            cloneObject(template.getCompteCollectif(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
        }
    }

    public void init(boolean next) {
        loadTemplate(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadTemplate(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadTemplate(true, true);
    }

    public void clearParams() {
        typeSearch = null;
        paysSearch = 0;
        vilSearch = 0;
        sectSearch = 0;
        nameSearch = null;
        paginator.getParams().clear();
        _first = true;
        loadTemplate(true, true);
    }

    public void addParamName() {
        ParametreRequete p = new ParametreRequete("y.libelle", "libelle", null);
        if (nameSearch != null ? nameSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "libelle", nameSearch.trim().toUpperCase() + "%", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "libelle", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.pays.libele)", "pays", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.pays.abreviation)", "pay1", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ville.libele)", "ville", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.ville.abreviation)", "ville1", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.secteur.libele)", "secteur", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.secteur.abreviation)", "secteur1", nameSearch.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadTemplate(true, true);
    }

    public void addParamType() {
        addParamType(typeSearch);
    }

    public void addParamType(String type) {
        ParametreRequete p = new ParametreRequete("y.type", "type", null);
        if (type != null ? type.trim().length() > 0 : false) {
            switch (type) {
                case "M":
                    p = new ParametreRequete("y.type", "type", type.charAt(0), "=", "AND");
                    break;
                case "F":
                    p = new ParametreRequete("y.type", "type", type.charAt(0), "=", "AND");
                    break;
                case "C":
                    p = new ParametreRequete("y.type", "type", type.charAt(0), "=", "AND");
                    break;
                case "MC":
                    p = new ParametreRequete(null, "type", type.charAt(0), "=", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.type", "type", 'M', "=", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.type", "type", 'C', "=", "OR"));
                    break;
                case "MF":
                    p = new ParametreRequete(null, "type", type.charAt(0), "=", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.type", "type", 'M', "=", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.type", "type", 'F', "=", "OR"));
                    break;
            }
        }
        paginator.addParam(p);
        loadTemplate(true, true);
    }

    public void addParamPays() {
        ParametreRequete p = new ParametreRequete("y.pays", "pays", null);
        if (paysSearch > 0) {
            p = new ParametreRequete("y.pays", "pays", new YvsDictionnaire(paysSearch), "=", "AND");
        } else {
            vilSearch = 0;
            addParamVille();
            sectSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(new YvsDictionnaire(paysSearch));
        }
        paginator.addParam(p);
        loadTemplate(true, true);
    }

    public void addParamVille() {
        ParametreRequete p = new ParametreRequete("y.ville", "ville", null);
        if (vilSearch > 0) {
            p = new ParametreRequete("y.ville", "ville", new YvsDictionnaire(vilSearch), "=", "AND");
        } else {
            sectSearch = 0;
            addParamSecteur();
        }
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadSecteurs(new YvsDictionnaire(vilSearch));
        }
        paginator.addParam(p);
        loadTemplate(true, true);
    }

    public void addParamSecteur() {
        ParametreRequete p = new ParametreRequete("y.secteur", "secteur", null);
        if (sectSearch > 0) {
            p = new ParametreRequete("y.secteur", "secteur", new YvsDictionnaire(sectSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadTemplate(true, true);
    }

}

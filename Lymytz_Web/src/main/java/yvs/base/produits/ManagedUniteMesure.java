/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.production.UtilProd;
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
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedUniteMesure extends Managed<UniteMesure, YvsBaseUniteMesure> implements Serializable {

    @ManagedProperty(value = "#{uniteMesure}")
    private UniteMesure uniteMesure;
    private List<YvsBaseUniteMesure> unites, equivalents, colonnes;
    private YvsBaseUniteMesure selectUnite;

    private TableConversion equivalent = new TableConversion();
    private List<Object[]> grilles;
    private YvsBaseTableConversion selectEquivalent;

    private String tabIds, tabIds_equivalent;
    private String fusionneTo;
    private List<String> fusionnesBy;

    private String typeEquivalent, typeSearch, numSearch, typeGrill;
    private double tauxEquivalence;
    private int idUnite = -1, idEquivalence = -1;

    public ManagedUniteMesure() {
        unites = new ArrayList<>();
        colonnes = new ArrayList<>();
        grilles = new ArrayList<>();
        equivalents = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public int getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(int idUnite) {
        this.idUnite = idUnite;
    }

    public int getIdEquivalence() {
        return idEquivalence;
    }

    public void setIdEquivalence(int idEquivalence) {
        this.idEquivalence = idEquivalence;
    }

    public double getTauxEquivalence() {
        return tauxEquivalence;
    }

    public void setTauxEquivalence(double tauxEquivalence) {
        this.tauxEquivalence = tauxEquivalence;
    }

    public List<YvsBaseUniteMesure> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<YvsBaseUniteMesure> colonnes) {
        this.colonnes = colonnes;
    }

    public List<Object[]> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<Object[]> grilles) {
        this.grilles = grilles;
    }

    public String getTypeGrill() {
        return typeGrill != null ? typeGrill.trim().length() > 0 ? typeGrill : Constantes.UNITE_QUANTITE : Constantes.UNITE_QUANTITE;
    }

    public void setTypeGrill(String typeGrill) {
        this.typeGrill = typeGrill;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getTypeEquivalent() {
        return typeEquivalent;
    }

    public void setTypeEquivalent(String typeEquivalent) {
        this.typeEquivalent = typeEquivalent;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public List<YvsBaseUniteMesure> getEquivalents() {
        return equivalents;
    }

    public void setEquivalents(List<YvsBaseUniteMesure> equivalents) {
        this.equivalents = equivalents;
    }

    public YvsBaseUniteMesure getSelectUnite() {
        return selectUnite;
    }

    public void setSelectUnite(YvsBaseUniteMesure selectUnite) {
        this.selectUnite = selectUnite;
    }

    public YvsBaseTableConversion getSelectEquivalent() {
        return selectEquivalent;
    }

    public void setSelectEquivalent(YvsBaseTableConversion selectEquivalent) {
        this.selectEquivalent = selectEquivalent;
    }

    public UniteMesure getUniteMesure() {
        return uniteMesure;
    }

    public void setUniteMesure(UniteMesure uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public List<YvsBaseUniteMesure> getUnites() {
        return unites;
    }

    public void setUnites(List<YvsBaseUniteMesure> unites) {
        this.unites = unites;
    }

    public TableConversion getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(TableConversion equivalent) {
        this.equivalent = equivalent;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getTabIds_equivalent() {
        return tabIds_equivalent;
    }

    public void setTabIds_equivalent(String tabIds_equivalent) {
        this.tabIds_equivalent = tabIds_equivalent;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        loadEquivalence(null);
    }

    public void load(Boolean load) {

    }

    public void loadEquivalence(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            nameQueri = "YvsBaseUniteMesure.findByType";
            champ = new String[]{"societe", "type"};
            val = new Object[]{currentAgence.getSociete(), type};
        } else {
            nameQueri = "YvsBaseUniteMesure.findAll";
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
        }
        equivalents = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAll(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            nameQueri = "YvsBaseUniteMesure.findByType";
            champ = new String[]{"societe", "type"};
            val = new Object[]{currentAgence.getSociete(), type};
        } else {
            nameQueri = "YvsBaseUniteMesure.findAll";
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
        }
        unites = dao.loadNameQueries(nameQueri, champ, val);
        update("blog_unite_centre_search");
    }

    public void loadByArticle(String article, String type) {
        if (article != null ? article.trim().length() > 0 : false) {
            nameQueri = "YvsBaseConditionnement.findUniteByArticle";
            champ = new String[]{"societe", "article"};
            val = new Object[]{currentAgence.getSociete(), article.toUpperCase() + "%"};
        } else {
            if (type != null ? type.trim().length() > 0 : false) {
                nameQueri = "YvsBaseUniteMesure.findByType";
                champ = new String[]{"societe", "type"};
                val = new Object[]{currentAgence.getSociete(), type};
            } else {
                nameQueri = "YvsBaseUniteMesure.findAll";
                champ = new String[]{"societe"};
                val = new Object[]{currentAgence.getSociete()};
            }
        }
        unites = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        unites = paginator.executeDynamicQuery("YvsBaseUniteMesure", "y.type, y.reference", avance, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsBaseUniteMesure> re = paginator.parcoursDynamicData("YvsBaseUniteMesure", "y", "y.type, y.reference", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadGrille(String type) {
        colonnes.clear();
        grilles.clear();
        if (type != null) {
            nameQueri = "YvsBaseUniteMesure.findByType";
            champ = new String[]{"societe", "type"};
            val = new Object[]{currentAgence.getSociete(), type};
            colonnes = dao.loadNameQueries(nameQueri, champ, val);

            Object[] v;
            for (YvsBaseUniteMesure r : colonnes) {
                v = new Object[colonnes.size()];
                for (int i = 0; i < colonnes.size(); i++) {
                    YvsBaseUniteMesure c = colonnes.get(i);
                    if (r.equals(c)) {
                        v[i] = 1;
                    } else {
                        nameQueri = "YvsBaseTableConversion.findTauxByUniteCorrespondance";
                        champ = new String[]{"unite", "uniteE"};
                        val = new Object[]{r, c};
                        v[i] = dao.loadObjectByNameQueries(nameQueri, champ, val);
                    }
                }
                grilles.add(v);
            }
        }
    }

    public void loadByType(String type) {
        typeSearch = type;
        addParamType();
    }

    public void loadByTypes(String[] types) {
        ParametreRequete p = new ParametreRequete("y.type", "type", null);
        if (types != null ? types.length > 0 : false) {
            typeSearch = types[0];
            p = new ParametreRequete(null, "type", typeSearch, "=", "AND");
            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.type)", "type" + i, type.toUpperCase(), "=", "OR"));
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
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
    public boolean controleFiche(UniteMesure bean) {
        if (bean.getReference() == null || bean.getReference().trim().length() < 1) {
            getErrorMessage("Vous devez precisez la reference");
            return false;
        }
        champ = new String[]{"reference", "type", "societe"};
        val = new Object[]{bean.getReference(), bean.getType(), currentAgence.getSociete()};
        YvsBaseUniteMesure y = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByNumType", champ, val);
        if (y != null ? (y.getId() > 0 ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré cette unité");
            return false;
        }
        if (bean.isDefaut()) {
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            y = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByDefaut", champ, val);
            if (y != null ? (y.getId() > 0 ? !y.getId().equals(bean.getId()) : false) : false) {
                getErrorMessage("Vous avez deja inseré une unité par défaut");
                return false;
            }
        }
        return true;
    }

    public boolean controleFiche(TableConversion bean) {
        if (bean.getUnite() != null ? bean.getUnite().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez l'unité de reference");
            return false;
        }
        if (bean.getUniteEquivalent() != null ? bean.getUniteEquivalent().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez l'unité d'équivalence");
            return false;
        }
        if (bean.getTauxChange() <= 0) {
            getErrorMessage("Vous devez entrer le taux de change");
            return false;
        }
        champ = new String[]{"uniteE", "unite"};
        val = new Object[]{new YvsBaseUniteMesure(bean.getUniteEquivalent().getId()), new YvsBaseUniteMesure(bean.getUnite().getId())};
        YvsBaseTableConversion t = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", champ, val);
        if (t != null ? (t.getId() > 0 ? !t.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré cet équivalence");
            return false;
        }
        return true;
    }

    @Override
    public UniteMesure recopieView() {
        return uniteMesure;
    }

    public TableConversion recopieViewEquivalent(UniteMesure y) {
        TableConversion r = new TableConversion();
        r.setId(equivalent.getId());
        r.setTauxChange(equivalent.getTauxChange());
        if (equivalents.contains(new YvsBaseUniteMesure(equivalent.getUniteEquivalent().getId()))) {
            YvsBaseUniteMesure m = equivalents.get(equivalents.indexOf(new YvsBaseUniteMesure(equivalent.getUniteEquivalent().getId())));
            equivalent.setUniteEquivalent(UtilProd.buildBeanUniteMesure(m));
        }
        r.setUniteEquivalent(equivalent.getUniteEquivalent());
        UniteMesure u = new UniteMesure(y.getId());
        if (equivalents.contains(new YvsBaseUniteMesure(u.getId()))) {
            YvsBaseUniteMesure m = equivalents.get(equivalents.indexOf(new YvsBaseUniteMesure(u.getId())));
            u = UtilProd.buildBeanUniteMesure(m);
        }
        r.setUnite(u);
        return r;
    }

    @Override
    public void populateView(UniteMesure bean) {
        cloneObject(uniteMesure, bean);
        resetFicheEquivalent();
    }

    public void populateView(TableConversion bean) {
        cloneObject(equivalent, bean);
    }

    @Override
    public void resetFiche() {
        String type = uniteMesure.getReference();
        resetFiche(uniteMesure);
        uniteMesure.setDefaut(false);
        uniteMesure.setType(type);
        uniteMesure.setEquivalences(new ArrayList<YvsBaseTableConversion>());
        tabIds = "";
        selectUnite = null;
        idUnite = -1;
        idEquivalence = -1;
        resetFicheEquivalent();
        update("blog_form_unite_mesure");
    }

    public void resetFicheEquivalent() {
        equivalent = new TableConversion();
        selectEquivalent = null;
        tabIds_equivalent = "";
    }

    @Override
    public boolean saveNew() {
        String action = uniteMesure.getId() > 0 ? "Modification" : "Insertion";
        try {
            UniteMesure bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseUniteMesure y = UtilProd.buildUniteMasse(bean, currentUser, currentAgence.getSociete());
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseUniteMesure) dao.save1(y);
                    uniteMesure.setId(y.getId());
                }
                if (equivalents.contains(y)) {
                    equivalents.set(equivalents.indexOf(y), y);
                } else {
                    equivalents.add(0, y);
                }
                int idx = unites.indexOf(y);
                if (idx > -1) {
                    unites.set(idx, y);
                } else {
                    unites.add(0, y);
                    if (unites.size() > imax) {
                        unites.remove(unites.size() - 1);
                    }
                }
                update("blog_unite_centre_search");
                succes();
                actionOpenOrResetAfter(this);
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public boolean saveNewEquivalent() {
        String action = equivalent.getId() > 0 ? "Modification" : "Insertion";
        try {
            TableConversion bean = recopieViewEquivalent(uniteMesure);
            if (controleFiche(bean)) {
                YvsBaseTableConversion y = UtilProd.buildTableConversion(bean, currentUser);
                if (y.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseTableConversion) dao.save1(y);
                    equivalent.setId(y.getId());
                }
                int idx = uniteMesure.getEquivalences().indexOf(y);
                if (idx > -1) {
                    uniteMesure.getEquivalences().set(idx, y);
                } else {
                    uniteMesure.getEquivalences().add(0, y);
                }
                idx = unites.indexOf(new YvsBaseUniteMesure(uniteMesure.getId()));
                if (idx > -1) {
                    int id = unites.get(idx).getEquivalences().indexOf(y);
                    if (id > -1) {
                        unites.get(idx).getEquivalences().set(id, y);
                    } else {
                        unites.get(idx).getEquivalences().add(0, y);
                    }
                }
                resetFicheEquivalent();
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
            return false;
        }
    }

    public boolean saveNewEquivalence(YvsBaseUniteMesure unite, YvsBaseUniteMesure equivalent, double taux) {
        try {
            champ = new String[]{"unite", "uniteE"};
            val = new Object[]{unite, equivalent};
            nameQueri = "YvsBaseTableConversion.findUniteCorrespondance";
            YvsBaseTableConversion y = (YvsBaseTableConversion) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                y.setTauxChange(taux);
                y.setAuthor(currentUser);
                dao.update(y);
            } else {
                y = new YvsBaseTableConversion(unite, equivalent, taux);
                y.setId(null);
                y.setAuthor(currentUser);
                dao.save1(y);
            }
            if (grilles != null ? !(grilles.isEmpty() ? (idUnite > -1 && idEquivalence > -1) : false) : false) {
                grilles.get(idUnite)[idEquivalence] = taux;
            }
            if (!unite.equals(equivalent)) {
                val = new Object[]{equivalent, unite};
                y = (YvsBaseTableConversion) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setTauxChange(1 / taux);
                    y.setAuthor(currentUser);
                    dao.update(y);
                } else {
                    y = new YvsBaseTableConversion(equivalent, unite, (1 / taux));
                    y.setAuthor(currentUser);
                    y.setId(null);
                    dao.save1(y);
                }
                if (grilles != null ? !(grilles.isEmpty() ? (idUnite > -1 && idEquivalence > -1) : false) : false) {
                    grilles.get(idEquivalence)[idUnite] = (1 / taux);
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
             System.err.println("tabIds = " + tabIds);
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseUniteMesure> list = new ArrayList<>();
                YvsBaseUniteMesure bean;
                for (Long ids : l) {
                    if (ids >= 0) {
                        bean = unites.get(ids.intValue());
                        list.add(bean);
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        dao.delete(bean);
                        if (bean.getId() == uniteMesure.getId()) {
                            resetFiche();
                        }
                    }

                }
                unites.removeAll(list);
                succes();
                
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible ! Cet élement est déjà utiliser ");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBean_(YvsBaseUniteMesure y) {
        selectUnite = y;
    }

    public void deleteBean_(YvsBaseUniteMesure y, boolean open) {
        selectUnite = y;
        if (!open) {
            deleteBean_();
        }
    }

    public void deleteBean_() {
        try {
            if (selectUnite != null ? selectUnite.getId() > 0 : false) {
                dao.delete(selectUnite);
                int idx = unites.indexOf(selectUnite);
                if (idx > -1) {
                    unites.remove(idx);
                }
                if (selectUnite.getId() == uniteMesure.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanEquivalent() {
        try {
            if (tabIds_equivalent != null ? tabIds_equivalent.trim().length() > 0 : false) {
                String[] ids = tabIds_equivalent.trim().split("-");
                for (String o : ids) {
                    long id = Long.valueOf(o);
                    dao.delete(new YvsBaseTableConversion(id));
                    int idx = uniteMesure.getEquivalences().indexOf(new YvsBaseTableConversion(id));
                    if (idx > -1) {
                        uniteMesure.getEquivalences().remove(idx);
                    }
                    if (id == equivalent.getId()) {
                        resetFicheEquivalent();
                    }
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression impossible");
            getException("Error Suppression ", ex);
        }
    }

    public void deleteBeanEquivalent_(YvsBaseTableConversion y) {
        selectEquivalent = y;
    }

    public void deleteBeanEquivalent_() {
        try {
            if (selectEquivalent != null ? selectEquivalent.getId() > 0 : false) {
                dao.delete(selectEquivalent);
                int idx = uniteMesure.getEquivalences().indexOf(selectEquivalent);
                if (idx > -1) {
                    uniteMesure.getEquivalences().remove(idx);
                }
                if (selectEquivalent.getId() == equivalent.getId()) {
                    resetFicheEquivalent();
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
    public void onSelectObject(YvsBaseUniteMesure y) {
        selectUnite = y;
        populateView(UtilProd.buildBeanUniteMesure(selectUnite));
        update("blog_form_unite_mesure");

    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsBaseUniteMesure) ev.getObject());
            execute("collapseForm('unite_mesure')");
//            execute("oncollapsesForm('unite_mesure')");
            tabIds = unites.indexOf((YvsBaseUniteMesure) ev.getObject()) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewEquivalent(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseTableConversion y = (YvsBaseTableConversion) ev.getObject();
            populateView(UtilProd.buildBeanTableConversion(y));
        }
    }

    public void unLoadOnViewEquivalent(UnselectEvent ev) {
        resetFicheEquivalent();
    }

    public void editEquivalence(int idUnite, int idEquivalence) {
        this.idUnite = idUnite;
        this.idEquivalence = idEquivalence;
        double v = returnValeur(idUnite, idEquivalence);
        if (v != 0) {
            tauxEquivalence = v;
        }
        update("txt_taux_equivalence");
    }

    public void onEquivalenceEdit() {
        try {
            YvsBaseUniteMesure row = colonnes.get(idUnite);
            YvsBaseUniteMesure col = colonnes.get(idEquivalence);
            if (saveNewEquivalence(row, col, tauxEquivalence)) {
                succes();
            }
        } catch (Exception ex) {

        }
    }

    public double returnValeur(int irow, int icol) {
        Object v = grilles.get(irow)[icol];
        return v != null ? Double.valueOf(v.toString()) : 0;
    }

    public void chooseTypeEquivalent() {
        loadEquivalence(typeEquivalent);
    }

    public void chooseTypeGrille() {
        loadGrille(typeGrill);
    }

    public void definedDefaut(YvsBaseUniteMesure bean) {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        YvsBaseUniteMesure y = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByDefaut", champ, val);
        if (y != null ? (y.getId() > 0 ? !y.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja inseré une unité par défaut");
            return;
        }
        bean.setDefaut(!bean.getDefaut());
        bean.setAuthor(currentUser);
        dao.update(bean);
        succes();
    }

    public void insertEquivalence(YvsBaseTableConversion y, boolean msg) {
        if (y != null ? (y.getId() > 0 ? ((y.getUnite() != null ? y.getUnite().getId() > 0 : false) && (y.getUniteEquivalent() != null ? y.getUniteEquivalent().getId() > 0 : false)) : false) : false) {
            champ = new String[]{"unite", "uniteE"};
            val = new Object[]{y.getUniteEquivalent(), y.getUnite()};
            YvsBaseTableConversion t = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", champ, val);
            if (t != null ? t.getId() < 1 : true) {
                t = new YvsBaseTableConversion();
                t.setUnite(y.getUniteEquivalent());
                t.setUniteEquivalent(y.getUnite());

                t.setTauxChange(1 / y.getTauxChange());
                t.setAuthor(currentUser);
                t = (YvsBaseTableConversion) dao.save1(t);

                y.getUniteEquivalent().getEquivalences().add(t);
                int idx = unites.indexOf(y.getUniteEquivalent());
                if (idx > -1) {
                    unites.set(idx, y.getUniteEquivalent());
                }
            }
            if (msg) {
                succes();
            }
        }
    }

    public void insertAllEquivalence(YvsBaseUniteMesure y) {
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"unite"};
            val = new Object[]{y};
            List<YvsBaseTableConversion> l = dao.loadNameQueries("YvsBaseTableConversion.findByEquivalent", champ, val);
            for (YvsBaseTableConversion t : l) {
                insertEquivalence(t, false);
            }
            succes();
        }
    }

    public void genererTableForAll() {
        genererTableForAll(typeSearch);
    }

    public void genererTableForAll(String unite) {
        if (unite != null ? unite.trim().length() > 0 : false) {
            nameQueri = "YvsBaseUniteMesure.findByType";
            champ = new String[]{"societe", "type"};
            val = new Object[]{currentAgence.getSociete(), unite};
        } else {
            nameQueri = "YvsBaseUniteMesure.findAll";
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
        }
        List<YvsBaseUniteMesure> list = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsBaseUniteMesure y : list) {
            genererTable(y, false);
        }
        succes();
    }

    public void genererTable(YvsBaseUniteMesure y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"unite"};
            val = new Object[]{y};
            y.setUnitesLiees(dao.loadNameQueries("YvsBaseTableConversion.findEquivalentByUnite", champ, val));
            y.getUnitesLiees().addAll(dao.loadNameQueries("YvsBaseTableConversion.findUniteByEquivalent", champ, val));

            champ = new String[]{"unite"};
            val = new Object[]{y};
            List<YvsBaseTableConversion> l = dao.loadNameQueries("YvsBaseTableConversion.findByUnite", champ, val);
            for (YvsBaseTableConversion u : l) {
                loadSousTable(y, u);
            }
            champ = new String[]{"unite"};
            val = new Object[]{y};
            l = dao.loadNameQueries("YvsBaseTableConversion.findByEquivalent", champ, val);
            for (YvsBaseTableConversion u : l) {
                u.setChange(true);
                u.setUniteEquivalent(u.getUnite());
                loadSousTable(y, u);
            }
            if (msg) {
                succes();
            }
        }
    }

    private void loadSousTable(YvsBaseUniteMesure y, YvsBaseTableConversion u) {
        champ = new String[]{"unite"};
        val = new Object[]{u.getUniteEquivalent()};
        List<YvsBaseTableConversion> l = dao.loadNameQueries("YvsBaseTableConversion.findByUnite", champ, val);
        for (YvsBaseTableConversion n : l) {
            insertSousTable(y, u, n);
        }
        champ = new String[]{"unite"};
        val = new Object[]{u.getUniteEquivalent()};
        l = dao.loadNameQueries("YvsBaseTableConversion.findByEquivalent", champ, val);
        for (YvsBaseTableConversion n : l) {
            n.setChange(true);
            n.setUniteEquivalent(n.getUnite());
            insertSousTable(y, u, n);
        }
    }

    private void insertSousTable(YvsBaseUniteMesure y, YvsBaseTableConversion u, YvsBaseTableConversion n) {
        if (!y.equals(n.getUniteEquivalent())) {
            champ = new String[]{"unite", "uniteE"};
            val = new Object[]{y, n.getUniteEquivalent()};
            YvsBaseTableConversion t = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", champ, val);
            if (t != null ? t.getId() < 1 : true) {
                t = new YvsBaseTableConversion();
                t.setUnite(y);
                t.setUniteEquivalent(n.getUniteEquivalent());
                t.setTauxChange((u.isChange() ? (1 / u.getTauxChange()) : u.getTauxChange()) * (n.isChange() ? (1 / n.getTauxChange()) : n.getTauxChange()));
                t.setAuthor(currentUser);
                t = (YvsBaseTableConversion) dao.save1(t);

                y.getEquivalences().add(t);
                int idx = unites.indexOf(y);
                if (idx > -1) {
                    unites.set(idx, y);
                }
            }
            if (!y.getUnitesLiees().contains(t.getUniteEquivalent())) {
                y.getUnitesLiees().add(t.getUniteEquivalent());
                loadSousTable(y, t);
            }
        }
    }

    public void clearParams() {
        typeSearch = null;
        numSearch = null;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamType() {
        ParametreRequete p = new ParametreRequete("y.type", "type", null);
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.type)", "type", typeSearch.toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete(null, "reference", null, "=", "AND");
        ParametreRequete p0;
        ParametreRequete p1;
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p0 = new ParametreRequete("UPPER(y.reference)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR");
            p1 = new ParametreRequete("UPPER(y.libelle)", "libelle", numSearch.toUpperCase() + "%", "LIKE", "OR");
            p.setObjet("XXX");
            p.getOtherExpression().add(p0);
            p.getOtherExpression().add(p1);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public UniteMesure findUnite(String num, boolean open) {
        UniteMesure a = new UniteMesure();
        a.setReference(num);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.reference", "reference", null, "LIKE", "OR");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.reference)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "reference", num.toUpperCase() + "%", "LIKE", "OR"));

        }
        paginator.addParam(p);
        loadAll(true, true);
        a = findUniteResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setReference(num);
            a.setError(true);
        }
        return a;
    }

    private UniteMesure findUniteResult(boolean open) {
        UniteMesure a = new UniteMesure();
        if (unites != null ? !unites.isEmpty() : false) {
            if (unites.size() > 1) {
                if (open) {
                    openDialog("dlgListUnite");
                }
                a.setList(true);
            } else {
                YvsBaseUniteMesure c = unites.get(0);
                a = UtilProd.buildBeanUniteMesure(c);
                a.setSelect(true);
            }
            a.setError(false);
        }
        return a;
    }

    public void initUnites(UniteMesure a) {
        if (a == null) {
            a = new UniteMesure();
        }
        paginator.addParam(new ParametreRequete("y.reference", "reference", null));
        loadAll(true, true);
        a.setSelect(false);
        a.setList(true);
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }
            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty() ? ids.size() > 1 : false) {
                long newValue = unites.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (unites.get(i).getId() != newValue) {
                            oldValue += "," + unites.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_unite_mesure", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                unites.remove(new YvsBaseUniteMesure(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = unites.get(idx).getLibelle();
                    } else {
                        YvsBaseUniteMesure c = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getLibelle();
                        }
                    }
                    YvsBaseUniteMesure c;
                    for (int i : ids) {
                        long oldValue = unites.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(unites.get(i).getLibelle());
                            }
                        } else {
                            c = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getLibelle());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 unites");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Action impossible");
            getException("fusionner ", ex);
        }
    }
}

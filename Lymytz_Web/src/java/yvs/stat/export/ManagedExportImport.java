/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.stat.export.YvsStatExportColonne;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.init.Initialisation;
import yvs.parametrage.ManagedImportExport;
import yvs.stat.UtilStat;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedExportImport extends Managed<ExportEtat, YvsStatExportEtat> implements Serializable {

    @ManagedProperty(value = "#{exportEtat}")
    private ExportEtat exportEtat;
    private YvsStatExportEtat selectExportEtat, currentExportEtat;
    private List<YvsStatExportEtat> etats;
    private ExportColonne colonne = new ExportColonne();
    private PaginatorResult<YvsStatExportColonne> p_colonnes = new PaginatorResult<>(5000);

    private List<String> references;
    private List<String> tableLiee, tables, colonnes, tablesContraint, colonnesContraint;
    private String numSearch, msg_erreur, msg_query;
    private ReturnImport resultImport;

    private Boolean visibleSearch, integrerSearch, contrainteSearch;
    private String tableSearch;

    List<String> tablesConditionsForce = new ArrayList<String>() {
        {
            add("yvs_societes");
            add("yvs_agences");
            add("yvs_users");
        }
    };

    public ManagedExportImport() {
        etats = new ArrayList<>();
        tableLiee = new ArrayList<>();
        references = new ArrayList<>();
        tables = new ArrayList<>();
        colonnes = new ArrayList<>();
        tablesContraint = new ArrayList<>();
        colonnesContraint = new ArrayList<>();
    }

    public ReturnImport getResultImport() {
        return resultImport;
    }

    public void setResultImport(ReturnImport resultImport) {
        this.resultImport = resultImport;
    }

    public String getMsg_query() {
        return msg_query;
    }

    public void setMsg_query(String msg_query) {
        this.msg_query = msg_query;
    }

    public YvsStatExportEtat getCurrentExportEtat() {
        return currentExportEtat;
    }

    public void setCurrentExportEtat(YvsStatExportEtat currentExportEtat) {
        this.currentExportEtat = currentExportEtat;
    }

    public YvsStatExportEtat getSelectExportEtat() {
        return selectExportEtat;
    }

    public void setSelectExportEtat(YvsStatExportEtat selectExportEtat) {
        this.selectExportEtat = selectExportEtat;
    }

    public PaginatorResult<YvsStatExportColonne> getP_colonnes() {
        return p_colonnes;
    }

    public void setP_colonnes(PaginatorResult<YvsStatExportColonne> p_colonnes) {
        this.p_colonnes = p_colonnes;
    }

    public Boolean getVisibleSearch() {
        return visibleSearch;
    }

    public void setVisibleSearch(Boolean visibleSearch) {
        this.visibleSearch = visibleSearch;
    }

    public Boolean getIntegrerSearch() {
        return integrerSearch;
    }

    public void setIntegrerSearch(Boolean integrerSearch) {
        this.integrerSearch = integrerSearch;
    }

    public Boolean getContrainteSearch() {
        return contrainteSearch;
    }

    public void setContrainteSearch(Boolean contrainteSearch) {
        this.contrainteSearch = contrainteSearch;
    }

    public String getTableSearch() {
        return tableSearch;
    }

    public void setTableSearch(String tableSearch) {
        this.tableSearch = tableSearch;
    }

    public String getMsg_erreur() {
        return msg_erreur;
    }

    public void setMsg_erreur(String msg_erreur) {
        this.msg_erreur = msg_erreur;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public List<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<String> colonnes) {
        this.colonnes = colonnes;
    }

    public List<String> getTablesContraint() {
        return tablesContraint;
    }

    public void setTablesContraint(List<String> tablesContraint) {
        this.tablesContraint = tablesContraint;
    }

    public List<String> getColonnesContraint() {
        return colonnesContraint;
    }

    public void setColonnesContraint(List<String> colonnesContraint) {
        this.colonnesContraint = colonnesContraint;
    }

    public List<String> getTableLiee() {
        return tableLiee;
    }

    public void setTableLiee(List<String> tableLiee) {
        this.tableLiee = tableLiee;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public ExportEtat getExportEtat() {
        return exportEtat;
    }

    public void setExportEtat(ExportEtat exportEtat) {
        this.exportEtat = exportEtat;
    }

    public List<YvsStatExportEtat> getEtats() {
        return etats;
    }

    public void setEtats(List<YvsStatExportEtat> etats) {
        this.etats = etats;
    }

    public ExportColonne getColonne() {
        return colonne;
    }

    public void setColonne(ExportColonne colonne) {
        this.colonne = colonne;
    }

    @Override
    public void loadAll() {
        loadAll(true, true);
        tables = dao.field(null, null, null);
    }

    public void loadAll(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        etats = paginator.executeDynamicQuery("YvsStatExportEtat", "y.code", avance, init, (int) imax, dao);
        update("data_etat_export");
    }

    public void loadColonnes(boolean avance, boolean init) {
        if (exportEtat != null ? exportEtat.getId() > 0 : false) {
            p_colonnes.clear();
            p_colonnes.addParam(new ParametreRequete("y.etat", "etat", new YvsStatExportEtat(exportEtat.getId()), "=", "AND"));
            exportEtat.setColonnes(p_colonnes.executeDynamicQuery("YvsStatExportColonne", "y.ordre", avance, init, dao));
        }
    }

    public void loadTableLier(YvsStatExportEtat etat) {
        tableLiee.clear();
        if (etat != null ? etat.getId() > 0 : false) {
            nameQueri = "YvsStatExportColonne.findTableLiee";
            champ = new String[]{"etat"};
            val = new Object[]{etat};
            tableLiee = dao.loadListByNameQueries(nameQueri, champ, val);

            nameQueri = "YvsStatExportColonne.findTableNotLiee";
            List<String> list = dao.loadListByNameQueries(nameQueri, champ, val);

            if (list != null ? !list.isEmpty() : false) {
                tableLiee.addAll(list);
            }
        }
    }

    public void paginer(boolean next) {
        loadAll(next, false);
    }

    public void paginerColonne(boolean next) {
        loadColonnes(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void choosePaginatorColonne(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            p_colonnes.setRows((int) v);
            loadColonnes(true, true);
        }
    }

    @Override
    public boolean controleFiche(ExportEtat bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getCode() != null ? bean.getCode().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le type");
            return false;
        }
        if (bean.getReference() != null ? bean.getReference().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la reference");
            return false;
        }
        if (bean.getFileName() != null ? bean.getFileName().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le nom du fichier");
            return false;
        }
        if (bean.getTypeFormule() == 'F') {
            if (bean.getFormule() != null ? bean.getFormule().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la requete");
                return false;
            }
        }

        return true;
    }

    public boolean controleFiche(ExportColonne bean) {
        if (bean == null) {
            getErrorMessage("Action impossible");
            return false;
        }
        if (bean.getEtat() != null ? bean.getEtat().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez l'etat");
            return false;
        }
        if (bean.getTableName() != null ? bean.getTableName().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la table");
            return false;
        }
        if (bean.getColonne() != null ? bean.getColonne().trim().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la colonne");
            return false;
        }
        if (bean.isCondition()) {
            if (bean.getConditionExpression() != null ? bean.getConditionExpression().trim().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la valeur de la condition");
                return false;
            }
            if (bean.getConditionOperator().equals("BETWEEN")) {
                if (bean.getConditionExpressionOther() != null ? bean.getConditionExpressionOther().trim().length() < 1 : true) {
                    getErrorMessage("Vous devez precisez la 2ieme valeur de la condition");
                    return false;
                }
            }
        }
        if (bean.isContrainte()) {
            if (bean.getTableNameLiee() != null ? bean.getTableNameLiee().trim().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la table liée");
                return false;
            }
            if (bean.getColonneLiee() != null ? bean.getColonneLiee().trim().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la colonne liéeS");
                return false;
            }
        } else {
//            if (tableLiee != null ? !tableLiee.isEmpty() : false) {
//                if (!tableLiee.contains(bean.getTableName())) {
//                    getErrorMessage("Vous ne pouvez pas ajouter cette table sans avoir crée sa contrainte");
//                    return false;
//                }
//            }
        }
        nameQueri = "YvsStatExportColonne.findByLibelle";
        champ = new String[]{"libelle", "etat"};
        val = new Object[]{bean.getLibelle(), new YvsStatExportEtat(bean.getEtat().getId())};
        YvsStatExportColonne y = (YvsStatExportColonne) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (y != null ? (y.getId() != null ? y.getId() > 0 ? y.getId() != bean.getId() : false : false) : false) {
            getErrorMessage("Vous ne pouvez pas ajouter ce libelle. il est deja attribué");
            return false;
        }
        nameQueri = "YvsStatExportColonne.findOneByEtat";
        champ = new String[]{"tableName", "colonne", "etat"};
        val = new Object[]{bean.getTableName(), bean.getColonne(), new YvsStatExportEtat(bean.getEtat().getId())};
        y = (YvsStatExportColonne) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
            bean.setId(y.getId());
        }
        return true;
    }

    @Override
    public ExportEtat recopieView() {
        return exportEtat;
    }

    public ExportColonne recopieView(ExportEtat etat) {
        colonne.setEtat(etat);
        colonne.setVisible(colonne.isIntegrer() ? colonne.isVisible() : false);
        return colonne;
    }

    @Override
    public void populateView(ExportEtat bean) {
        cloneObject(exportEtat, bean);
        loadTableLier(new YvsStatExportEtat(bean.getId()));
        loadColonnes(true, true);
        chooseType();
        resetFicheColonne();
    }

    public void populateView(ExportColonne bean) {
        cloneObject(colonne, bean);
        chooseTable();
        chooseColonne();
        chooseTableContraint();
    }

    @Override
    public void resetFiche() {
        resetFiche(exportEtat);
        exportEtat.setColonnes(new ArrayList<YvsStatExportColonne>());
        resetFicheColonne();
        update("blog_etat_export");
    }

    public void resetFicheColonne() {
        colonne = new ExportColonne();
        colonne.setTableName(getTablePrincipalExport(new YvsStatExportEtat(exportEtat.getId())));
        chooseTable();
    }

    @Override
    public boolean saveNew() {
        String action = exportEtat.getId() > 0 ? "Modification" : "Insertion";
        try {
            ExportEtat bean = recopieView();
            if (controleFiche(bean)) {
                YvsStatExportEtat y = UtilStat.buildExportEtat(bean, currentUser, currentAgence.getSociete());
                if (y != null ? y.getId() > 0 : false) {
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y.setId(null);
                    y.setDateSave(new Date());
                    y = (YvsStatExportEtat) dao.save1(y);
                    exportEtat.setId(y.getId());
                }
                currentExportEtat = y;
                int idx = etats.indexOf(y);
                if (idx > -1) {
                    etats.set(idx, y);
                } else {
                    etats.add(0, y);
                }
                succes();
                actionOpenOrResetAfter(this);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error : ", ex);
        }
        return false;
    }

    public boolean saveNewColonne() {
        String action = colonne.getId() > 0 ? "Modification" : "Insertion";
        try {
            ExportColonne bean = recopieView(exportEtat);
            if (controleFiche(bean)) {
                YvsStatExportColonne y = UtilStat.buildExportColonne(bean, currentUser);
                if (!Util.asString(y.getType())) {
                    y.setType(getTypeColonne(y.getTableName(), y.getColonne()));
                }
                if (y != null ? y.getId() > 0 : false) {
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y.setId(null);
                    y.setDateSave(new Date());
                    y = (YvsStatExportColonne) dao.save1(y);
                }
                if (tableLiee != null ? tableLiee.isEmpty() : false) {
                    if (!tableLiee.contains(bean.getTableName())) {
                        tableLiee.add(bean.getTableName());
                    }
                }
                if (bean.getTableNameLiee() != null ? bean.getTableNameLiee().length() > 0 : false) {
                    if (!tableLiee.contains(bean.getTableNameLiee())) {
                        tableLiee.add(bean.getTableNameLiee());
                    }
                }
                int idx = exportEtat.getColonnes().indexOf(y);
                if (idx > -1) {
                    exportEtat.getColonnes().set(idx, y);
                } else {
                    exportEtat.getColonnes().add(0, y);
                }
                idx = etats.indexOf(new YvsStatExportEtat(exportEtat.getId()));
                if (idx > -1) {
                    int idx_ = etats.get(idx).getColonnes().indexOf(y);
                    if (idx_ > -1) {
                        etats.get(idx).getColonnes().set(idx_, y);
                    } else {
                        etats.get(idx).getColonnes().add(0, y);
                    }
                }
                resetFicheColonne();
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error : ", ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean(YvsStatExportEtat y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                etats.remove(y);
                if (exportEtat.getId() == y.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error : ", ex);
        }
    }

    public void deleteBeanColonne(YvsStatExportColonne y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                dao.delete(y);
                exportEtat.getColonnes().remove(y);
                if (colonne.getId() == y.getId()) {
                    resetFicheColonne();
                    update("form_colonne_etat_export");
                }
                succes();
            }
        } catch (Exception ex) {
            getException("Error : ", ex);
        }
    }

    public void changeVisibleColonne(YvsStatExportColonne y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setVisible(!y.getVisible());
                dao.update(y);
                int index = exportEtat.getColonnes().indexOf(y);
                if (index > -1) {
                    exportEtat.getColonnes().set(index, y);
                }
            }
        } catch (Exception ex) {
            getException("Error : ", ex);
        }
    }

    public void changeIntegreColonne(YvsStatExportColonne y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setIntegrer(!y.getIntegrer());
                dao.update(y);
                int index = exportEtat.getColonnes().indexOf(y);
                if (index > -1) {
                    exportEtat.getColonnes().set(index, y);
                }
            }
        } catch (Exception ex) {
            getException("Error : ", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsStatExportEtat y = (YvsStatExportEtat) ev.getObject();
        currentExportEtat = y;
        populateView(UtilStat.buildBeanExportEtat(y));
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewColonne(SelectEvent ev) {
        YvsStatExportColonne y = (YvsStatExportColonne) ev.getObject();
        populateView(UtilStat.buildBeanExportColonne(y));
    }

    public void unLoadOnViewColonne(UnselectEvent ev) {
        resetFicheColonne();
    }

    public void onRowEdit(YvsStatExportColonne y) {
        populateView(UtilStat.buildBeanExportColonne(y));
    }

    public void chooseType() {
        references.clear();
        String code = exportEtat.getCode();
        if (code != null ? code.trim().length() > 0 : false) {
            champ = new String[]{"code", "societe"};
            val = new Object[]{code, currentAgence.getSociete()};
            references = dao.loadListByNameQueries("YvsStatExportEtat.findReference", champ, val);
        }
    }

    public void chooseReference(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            String code = (String) ev.getNewValue();
            if (code != null ? code.trim().length() > 0 : false) {
                champ = new String[]{"reference", "societe"};
                val = new Object[]{code, currentAgence.getSociete()};
                YvsStatExportEtat y = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByReference", champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    resetFiche();
                    populateView(UtilStat.buildBeanExportEtat(y));
                }
            }
        }
    }

    public void loadOnReference(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            String code = (String) ev.getObject();
            if (code != null ? code.trim().length() > 0 : false) {
                champ = new String[]{"reference", "societe"};
                val = new Object[]{code, currentAgence.getSociete()};
                YvsStatExportEtat y = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByReference", champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    resetFiche();
                    populateView(UtilStat.buildBeanExportEtat(y));
                }
            }
        }
    }

    public void changeSensContraint() {
        colonne.setSensContrainte(colonne.getSensContrainte() == 'N' ? 'I' : 'N');
    }

    public void chooseTable() {
        colonnes.clear();
        if (colonne.getTableName() != null ? colonne.getTableName().trim().length() > 0 : false) {
            colonnes = dao.field(colonne.getTableName(), null, null);
        }
    }

    public void chooseContrainte() {
        if (colonne.isRealContrainte()) {
            colonne.setContrainte(colonne.isRealContrainte());
        } else {
            if (colonne.isContrainte()) {
                tablesContraint = dao.field(null, null, null);
            } else {
                tablesContraint.clear();
            }
        }
    }

    private String getTypeColonne(String table, String column) {
        return (String) dao.loadObjectBySqlQuery("SELECT data_type FROM information_schema.columns WHERE table_name = ? AND column_name = ?", new Options[]{new Options(table, 1), new Options(column, 2)});
    }

    public void chooseColonne() {
        tablesContraint.clear();
        if (colonne.getColonne() != null ? colonne.getColonne().trim().length() > 0 : false) {
            tablesContraint = dao.field(colonne.getTableName(), colonne.getColonne(), null);
            colonne.setRealContrainte(!tablesContraint.isEmpty());
            colonne.setContrainte(colonne.isRealContrainte());
        }
        if (colonne.getLibelle() != null ? colonne.getLibelle().trim().length() < 1 : true) {
            colonne.setLibelle(colonne.getColonne());
        }
        colonne.setType(getTypeColonne(colonne.getTableName(), colonne.getColonne()));
    }

    public void chooseTableContraint() {
        colonnesContraint.clear();
        if (colonne.getTableNameLiee() != null ? colonne.getTableNameLiee().trim().length() > 0 : false) {
            if (colonne.isRealContrainte()) {
                colonnesContraint = dao.field(colonne.getTableName(), colonne.getColonne(), colonne.getTableNameLiee());
            } else {
                colonnesContraint = dao.field(colonne.getTableNameLiee(), null, null);
            }
        }
    }

    public String getTablePrincipalExport(YvsStatExportEtat etat) {
        String tablePrincipale = null;
        try {
            List<Object[]> tableNames = (List<Object[]>) dao.loadListByNameQueries("YvsStatExportColonne.findTableWithCount", new String[]{"etat"}, new Object[]{etat});
            if (tableNames != null && tableNames.size() > 0) {
                long number = 0;
                for (Object[] tableName : tableNames) {
                    if (tableName.length == 2 && (long) tableName[1] > number) {
                        number = (long) tableName[1];
                        tablePrincipale = (String) tableName[0];
                    }
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tablePrincipale;
    }

    public void addColumns(String table, List<String> colonnes) {
        if (table == null || table.isEmpty()) {
            return;
        }
        if (colonnes == null || colonnes.isEmpty()) {
            return;
        }
        for (String colonne : colonnes) {
            addColumn(table, colonne);
        }
    }

    public void addColumn(String table, String colonne) {
        try {
            if (exportEtat == null || exportEtat.getId() < 1) {
                return;
            }
            if (isExist(table, colonne)) {
                return;
            }
            Integer max = (Integer) dao.loadObjectByNameQueries("YvsStatExportColonne.maxOrdre", new String[]{"etat"}, new Object[]{new YvsStatExportEtat(exportEtat.getId())});
            int ordre = max != null ? max + 1 : 1;
            YvsStatExportColonne entity = new YvsStatExportColonne();
            entity.setTableName(table);
            entity.setColonne(colonne);
            entity.setLibelle(table + "." + colonne);
            entity.setOrdre(ordre);
            entity.setIntegrer(true);
            entity.setVisible(true);
            entity.setType(getTypeColonne(table, colonne));
            entity.setEtat(new YvsStatExportEtat(exportEtat.getId()));
            entity.setDateSave(new Date());
            entity.setAuthor(currentUser);

            entity = (YvsStatExportColonne) dao.save1(entity);
            exportEtat.getColonnes().add(entity);

            String query = "SELECT tc.constraint_name, tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name "
                    + "FROM information_schema.table_constraints AS tc "
                    + "JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.constraint_schema = kcu.constraint_schema "
                    + "JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name AND ccu.constraint_schema = tc.constraint_schema "
                    + "WHERE tc.constraint_type = 'FOREIGN KEY' AND ccu.table_name IN ('yvs_societes','yvs_agences') AND tc.table_name = ?";

            Query qr = dao.getEntityManager().createNativeQuery(query).setParameter(1, table);
            List<Object[]> foreigns = qr.getResultList();
            if (foreigns != null && !foreigns.isEmpty()) {
                for (Object[] foreign : foreigns) {
                    String columnTable = foreign[2].toString();
                    if (isExist(table, columnTable)) {
                        continue;
                    }
                    ordre++;
                    String tableForeign = foreign[3].toString();
                    String columnForeign = foreign[4].toString();
                    entity = new YvsStatExportColonne();
                    entity.setTableName(table);
                    entity.setColonne(columnTable);
                    entity.setLibelle(table + "." + columnTable);
                    entity.setOrdre(ordre);
                    entity.setIntegrer(false);
                    entity.setVisible(false);
                    entity.setContrainte(true);
                    entity.setTableNameLiee(tableForeign);
                    entity.setColonneLiee(columnForeign);
                    entity.setType(getTypeColonne(table, columnTable));
                    entity.setEtat(new YvsStatExportEtat(exportEtat.getId()));
                    entity.setDateSave(new Date());
                    entity.setAuthor(currentUser);

                    entity = (YvsStatExportColonne) dao.save1(entity);
                    exportEtat.getColonnes().add(entity);
                }
            }

            succes();
            update("blog_colonne_etat_export");
        } catch (Exception ex) {
            getException("Error : ", ex);
        }
    }

    public boolean isExist(String table, String colonne) {
        if (exportEtat != null && exportEtat.getColonnes() != null && !exportEtat.getColonnes().isEmpty()) {
            for (int i = 0; i < exportEtat.getColonnes().size(); i++) {
                YvsStatExportColonne current = exportEtat.getColonnes().get(i);
                if (current.getTableName().equals(table) && current.getColonne().equals(colonne)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isUpOrderColonne(YvsStatExportColonne y) {
        int index = -1;
        if (exportEtat != null && exportEtat.getColonnes() != null && !exportEtat.getColonnes().isEmpty()) {
            index = exportEtat.getColonnes().indexOf(y);
        }
        return index > 0;
    }

    public void onUpOrderColonne(YvsStatExportColonne y) {
        if (exportEtat != null && exportEtat.getColonnes() != null && !exportEtat.getColonnes().isEmpty()) {
            int index = exportEtat.getColonnes().indexOf(y);
            if (index <= 0) {
                return;
            }
            YvsStatExportColonne first = exportEtat.getColonnes().get(index - 1);
            int ordre = first.getOrdre() < y.getOrdre() ? first.getOrdre() : (y.getOrdre() > 0 ? y.getOrdre() - 1 : 0);
            first.setOrdre(y.getOrdre());
            dao.update(first);
            exportEtat.getColonnes().set(index, first);
            y.setOrdre(ordre);
            dao.update(y);
            exportEtat.getColonnes().set(index - 1, y);
        }
    }

    public void onDownOrderColonne(YvsStatExportColonne y) {
        if (exportEtat != null && exportEtat.getColonnes() != null && !exportEtat.getColonnes().isEmpty()) {
            int index = exportEtat.getColonnes().indexOf(y);
            if (index >= exportEtat.getColonnes().size() - 1) {
                return;
            }
            YvsStatExportColonne first = exportEtat.getColonnes().get(index + 1);
            int ordre = first.getOrdre() > y.getOrdre() ? first.getOrdre() : (y.getOrdre() + 1);
            first.setOrdre(y.getOrdre());
            dao.update(first);
            exportEtat.getColonnes().set(index, first);
            y.setOrdre(ordre);
            dao.update(y);
            exportEtat.getColonnes().set(index + 1, y);
        }
    }

    public boolean isDownOrderColonne(YvsStatExportColonne y) {
        int index = -1;
        if (exportEtat != null && exportEtat.getColonnes() != null && !exportEtat.getColonnes().isEmpty()) {
            index = exportEtat.getColonnes().indexOf(y);
        }
        return index > -1 && index < exportEtat.getColonnes().size() - 1;
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.code", "code", null, "LIKE", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "code", numSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.code)", "code", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.libelle)", "code", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fileName)", "code", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamTable() {
        ParametreRequete p = new ParametreRequete("y.tableName", "table", null, "LIKE", "AND");
        if (tableSearch != null ? tableSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "table", tableSearch, "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tableName)", "table", tableSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tableNameLiee)", "table", tableSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_colonnes.addParam(p);
        loadColonnes(true, true);
    }

    public void addParamVisible() {
        p_colonnes.addParam(new ParametreRequete("y.visible", "visible", visibleSearch, "=", "AND"));
        loadColonnes(true, true);
    }

    public void addParamIntegrer() {
        p_colonnes.addParam(new ParametreRequete("y.integrer", "integrer", integrerSearch, "=", "AND"));
        loadColonnes(true, true);
    }

    public void addParamContrainte() {
        p_colonnes.addParam(new ParametreRequete("y.contrainte", "contrainte", contrainteSearch, "=", "AND"));
        loadColonnes(true, true);
    }

    public long nombreIntegrer(YvsStatExportEtat y) {
        if (y != null ? y.getId() > 0 : false) {
            Long v = (Long) dao.loadObjectByNameQueries("YvsStatExportColonne.countByIntegrer", new String[]{"etat", "integrer"}, new Object[]{y, true});
            return v != null ? v : 0;
        }
        return 0;
    }

    public long nombreVisible(YvsStatExportEtat y) {
        if (y != null ? y.getId() > 0 : false) {
            Long v = (Long) dao.loadObjectByNameQueries("YvsStatExportColonne.countByVisible", new String[]{"etat", "visible"}, new Object[]{y, true});
            return v != null ? v : 0;
        }
        return 0;
    }

    public long nombreContrainte(YvsStatExportEtat y) {
        if (y != null ? y.getId() > 0 : false) {
            Long v = (Long) dao.loadObjectByNameQueries("YvsStatExportColonne.countByContrainte", new String[]{"etat", "contrainte"}, new Object[]{y, true});
            return v != null ? v : 0;
        }
        return 0;
    }

    public void duplicateModel(YvsStatExportEtat y) {
        try {
            YvsStatExportEtat bean = new YvsStatExportEtat(y);
            bean.setId(null);
            bean.setDateSave(new Date());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);

            bean.setId(null);
            bean = (YvsStatExportEtat) dao.save1(bean);
            for (YvsStatExportColonne c : bean.getColonnes()) {
                c.setId(null);
                c.setDateSave(new Date());
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                c.setEtat(bean);
                c = (YvsStatExportColonne) dao.save1(c);
            }
            etats.add(bean);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Duplication impossible");
        }
    }

    public void onViewModel() {
        ExportData query = onBuildQuery(currentExportEtat);
        msg_query = query.getQuery();
        update("txt_msg_query_compilation_export");
    }

    public void compilerModel() {
        try {
            ExportData query = onBuildQuery(currentExportEtat);
            Query qr = dao.getEntityManager().createNativeQuery(query.getQuery());
            if (query.getConditions() != null && !query.getConditions().isEmpty()) {
                List<String> integers = new ArrayList<String>() {
                    {
                        add("int");
                        add("integer");
                        add("bigint");
                    }
                };
                List<String> booleans = new ArrayList<String>() {
                    {
                        add("boolean");
                    }
                };
                List<String> times = new ArrayList<String>() {
                    {
                        add("date");
                        add("time");
                        add("timestamp");
                    }
                };
                List<String> arrays = new ArrayList<String>() {
                    {
                        add("IN");
                        add("NOT IN");
                    }
                };
                for (int index = 1; index <= query.getConditions().size(); index++) {
                    String type = query.getConditions().get(index - 1).getType();
                    boolean is_arrays = arrays.contains(query.getConditions().get(index - 1).getOperateur());
                    Object valeur = is_arrays ? "0" : integers.contains(type) ? 0 : booleans.contains(type) ? false : times.contains(type) ? new Date() : null;
                    qr = qr.setParameter(index, valeur);
                }
            }
            qr.getResultList();
            getInfoMessage("Correct !");
        } catch (Exception ex) {
            msg_erreur = ex.getMessage();
            update("txt_msg_erreur_compilation_export");
            openDialog("dlgErreurExport");
        }
    }

    public String sanitize(String head) {
        return head != null ? head.replaceAll("\"", "") : "";
    }

    public void onOpenToExport(YvsStatExportEtat y) {
        selectExportEtat = y;
        selectExportEtat.setColonnes(dao.loadNameQueries("YvsStatExportColonne.findCondition", new String[]{"etat"}, new Object[]{y}));
        update("blog_export_data");
    }

    private ExportData onBuildQuery(String report) {
        champ = new String[]{"reference", "societe"};
        val = new Object[]{report, currentAgence.getSociete()};
        YvsStatExportEtat etat = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByReference", champ, val);
        return onBuildQuery(etat);
    }

    private ExportData onBuildQuery(YvsStatExportEtat etat) {
        ExportData result = new ExportData();
        if (etat != null ? etat.getId() > 0 : false) {
            if (etat.getTypeFormule().equals('S')) {
                champ = new String[]{"etat", "integrer"};
                val = new Object[]{etat, true};
                List<YvsStatExportColonne> colonnes = dao.loadNameQueries("YvsStatExportColonne.findByIntegrer", champ, val);
                if (colonnes != null ? !colonnes.isEmpty() : false) {
                    String tablePrincipale = getTablePrincipalExport(etat);
                    List<String> columns = new ArrayList<>();
                    List<String> contraintes = new ArrayList<>();
                    List<String> conditions = new ArrayList<>();
                    List<String> orders = new ArrayList<>();

                    for (YvsStatExportColonne colonne : colonnes) {
                        String column = colonne.getTableName() + "." + colonne.getColonne();
                        if (colonne.getVisible()) {
                            result.getHeaders().add(colonne.getLibelle());
                            switch (colonne.getType()) {
                                case "date":
                                    column = "to_char(" + column + ", 'dd-MM-yyyy')";
                                    break;
                                case "time":
                                    column = "to_char(" + column + ", 'HH:mm:ss')";
                                    break;
                                case "timestamp":
                                    column = "to_char(" + column + ", 'dd-MM-yyyy HH:mm:ss')";
                                    break;
                            }
                            if (Util.asString(colonne.getDefautValeur())) {
                                column = "COALESCE(" + column + "," + Util.getValues(colonne.getType(), colonne.getDefautValeur()) + ")";
                            }
                            columns.add(column);
                        }
                        if (colonne.getContrainte()) {
                            String contrainte = colonne.getTableNameLiee() + "." + colonne.getColonneLiee();
                            String jointure = colonne.getSensContrainte().equals('N') ? colonne.getTableNameLiee() + " ON " + column + " = " + contrainte : colonne.getTableName() + " ON " + contrainte + " = " + column;
                            if (!contraintes.contains(jointure)) {
                                contraintes.add(colonne.getJointure() + " " + jointure);
                            }
                        }
                        if (colonne.getOrderBy() != null && !String.valueOf(colonne.getOrderBy()).trim().isEmpty()) {
                            orders.add(column + " " + (colonne.getOrderBy().equals('D') ? "DESC" : "ASC"));
                        }
                        if (colonne.getCondition()) {
                            List<String> no_string = new ArrayList<String>() {
                                {
                                    add("int");
                                    add("integer");
                                    add("bigint");
                                    add("boolean");
                                    add("double precision");
                                }
                            };
                            List<String> is_arrays = new ArrayList<String>() {
                                {
                                    add("IN");
                                    add("NOT IN");
                                }
                            };
                            boolean addQuote = !no_string.contains(colonne.getType());
                            boolean addArray = is_arrays.contains(colonne.getConditionOperator());

                            String conditionExpression = addArray ? "(select unnest(string_to_array(" + colonne.getConditionExpression() + ",';')::" + colonne.getType() + "[]))" : colonne.getConditionExpression();
                            if (colonne.getConditionExpression().startsWith(":") || colonne.getConditionExpression().startsWith("?")) {
                                conditionExpression = addArray ? "(select unnest(string_to_array(?,';')::" + colonne.getType() + "[]))" : " ? ";
                                result.getConditions().add(new ExportData.ExportDataCondition(colonne.getType(), colonne.getConditionExpression(), colonne.getConditionOperator()));
                            } else if (addQuote) {
                                conditionExpression = "'" + colonne.getConditionExpression() + "'";
                            }
                            String condition = column + " " + colonne.getConditionOperator() + " " + conditionExpression;
                            if (colonne.getConditionOperator().equals("BETWEEN")) {
                                String conditionExpressionOther = conditionExpression;
                                if (colonne.getConditionExpressionOther() != null && !colonne.getConditionExpressionOther().trim().isEmpty()) {
                                    conditionExpressionOther = addArray ? "(select unnest(string_to_array(" + colonne.getConditionExpressionOther() + ",';')::" + colonne.getType() + "[]))" : colonne.getConditionExpressionOther();
                                    if (colonne.getConditionExpressionOther().startsWith(":") || colonne.getConditionExpressionOther().startsWith("?")) {
                                        conditionExpressionOther = addArray ? "(select unnest(string_to_array(?,';')::" + colonne.getType() + "[]))" : " ? ";
                                        result.getConditions().add(new ExportData.ExportDataCondition(colonne.getType(), colonne.getConditionExpressionOther(), colonne.getConditionOperator()));
                                    } else if (addQuote) {
                                        conditionExpressionOther = "'" + colonne.getConditionExpressionOther() + "'";
                                    }
                                } else if (colonne.getConditionExpression().startsWith(":") || colonne.getConditionExpression().startsWith("?")) {
                                    result.getConditions().add(new ExportData.ExportDataCondition(colonne.getType(), colonne.getConditionExpression(), colonne.getConditionOperator()));
                                }
                                condition += " AND " + conditionExpressionOther;
                            }
                            conditions.add(condition);
                        }
                    }

                    String select = Util.join(", ", columns);
                    String jointure = Util.join(" ", contraintes);
                    String condition = conditions.isEmpty() ? "" : "WHERE " + Util.join(" AND ", conditions);
                    String order = orders.isEmpty() ? "" : "ORDER BY " + Util.join(", ", orders);

                    result.setQuery("SELECT " + select + " FROM " + tablePrincipale + " " + jointure + " " + condition + " " + order);
                }
            } else {
                result.setQuery(etat.getFormule());
            }
        }
        return result;
    }

    /*/BEGIN EXPORTATION/*/
    public void onExporter() {
        try {
            if (selectExportEtat == null || selectExportEtat.getId() < 1) {
                return;
            }
            Map<String, Object> parametres = new HashMap<>();
            for (YvsStatExportColonne c : selectExportEtat.getColonnes()) {
                if (c.getConditionExpression() != null && !c.getConditionExpression().trim().isEmpty() && c.getConditionExpression().startsWith(":")) {
                    if (c.getConditionValue() == null || c.getConditionValue().toString().trim().isEmpty()) {
                        getErrorMessage("Vous devez preciser la valeur de l'expression : " + c.getConditionExpression());
                        return;
                    }
                    Object value = c.getConditionValue().toString().contains(";") ? c.getConditionValue() : Util.getValues(c.getType(), c.getConditionValue().toString());
                    parametres.put(c.getConditionExpression().replace(":", ""), value);
                }
                if (c.getConditionExpressionOther() != null && !c.getConditionExpressionOther().trim().isEmpty() && c.getConditionExpressionOther().startsWith(":")) {
                    if (c.getConditionValueOther() == null || c.getConditionValueOther().toString().trim().isEmpty()) {
                        getErrorMessage("Vous devez preciser la valeur de l'expression : " + c.getConditionExpressionOther());
                        return;
                    }
                    Object value = c.getConditionValueOther().toString().contains(";") ? c.getConditionValueOther() : Util.getValues(c.getType(), c.getConditionValueOther().toString());
                    parametres.put(c.getConditionExpressionOther().replace(":", ""), value);
                }
            }
            onExporter(selectExportEtat, parametres);
        } catch (Exception ex) {
            getErrorMessage("Exportation impossible");
            getException("Exportation impossible (exporter)", ex);
        }
    }

    public String onExporter(String report, Map<String, Object> parametres) {
        champ = new String[]{"reference", "societe"};
        val = new Object[]{report, currentAgence.getSociete()};
        YvsStatExportEtat etat = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByReference", champ, val);
        return onExporter(etat, parametres);
    }

    public String onExporter(YvsStatExportEtat etat, Map<String, Object> parametres) {
        String result = null;
        try {
            if (etat != null ? etat.getId() > 0 : false) {
                ExportData data = onBuildQuery(etat);
                if (data != null ? data.getQuery().trim().length() > 0 : false) {
                    List<yvs.dao.Options> options = new ArrayList<>();
                    if (parametres != null && !parametres.keySet().isEmpty()) {
                        int size = parametres.keySet().size();
                        String[] keys = parametres.keySet().toArray(new String[size]);
                        for (int index = 1; index <= size; index++) {
                            String key = keys[index - 1];
                            options.add(new yvs.dao.Options(parametres.get(key), key, index));
                        }
                    }
                    data.setValues(dao.loadListBySqlQuery(data.getQuery(), options.toArray(new Options[options.size()])));
                    if (data.getValues() == null) {
                        return "Erreur sur la requete";
                    }
                    if (data.getValues().isEmpty()) {
                        return "Aucune données trouvées";
                    }
                    String file = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + etat.getFileName();
                    switch (etat.getFormat()) {
                        case Constantes.FILE_XML:
                            file += ".xml";
                            onExportXML(file, etat, data.getValues());
                            succes();
                            break;
                        case Constantes.FILE_JSON:
                            file += ".json";
                            onExportJSON(file, data.getColonnes(), data.getValues());
                            succes();
                            break;
                        case Constantes.FILE_CSV:
                            file += ".csv";
                            onExportCSV(file, data.getHeaders(), data.getValues());
                            succes();
                            break;
                        default:
                            file += ".txt";
                            onExportTXT(file, data.getHeaders(), data.getValues(), etat.getSeparateur());
                            succes();
                            break;
                    }
                    Util.getDownloadFile(file, etat.getFileName());
                }
            } else {
                getErrorMessage("Aucun modèle d'exportation pour ce document n'a été trouvé");
            }
        } catch (IOException | JAXBException ex) {
            java.util.logging.Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            result = ex.getMessage(); 
        }
        return result;
    }

    private <T> void onExportXML(String fileDestination, YvsStatExportEtat etat, List<T> mappedData) throws IOException, JAXBException {
        Map<String, Object> value = new HashMap<>();
        JAXBContext context = JAXBContext.newInstance(ExportEtat.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Sortie console
        marshaller.marshal(etat, System.out);

        // Sauvegarde dans un fichier
        marshaller.marshal(etat, new File(fileDestination));
    }

    private <T> void onExportJSON(String fileDestination, List<String> titles, List<T> mappedData) throws IOException {
        Map<String, Object> value = new HashMap<>();
        for (int i = 0; i < titles.size(); i++) {

        }
    }

    private <T> void onExportCSV(String fileDestination, List<String> titles, List<T> mappedData) throws IOException {
        Util.createFileCSV(fileDestination, titles, mappedData);
    }

    private <T> void onExportTXT(String fileDestination, List<String> titles, List<T> mappedData, String sep) throws IOException {
        Util.createFileTXT(fileDestination, titles, mappedData, sep);
    }
    /*/END EXPORTATION/*/

    /*/BEGIN IMPORTATION/*/
    public void handleFile(FileUploadEvent ev) throws IOException {
        resultImport = new ReturnImport();
        if (ev == null) {
            getErrorMessage("Vous devez selectionner un fichier");
            return;
        }
        if (selectExportEtat == null || selectExportEtat.getId() < 1) {
            getErrorMessage("Vous devez selectionner un model");
            return;
        }
        if (!selectExportEtat.getTypeFormule().equals('S')) {
            getErrorMessage("Ce model ne peut pas etre importer car il est de type formule");
            return;
        }
        try {
            String destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + "temp." + selectExportEtat.getFormat().toLowerCase();
            File file = null;
            try {
                file = Util.convertInputStreamToFile(destination, ev.getFile().getInputstream());
            } catch (IOException ex) {
                Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (file != null) {
                List<YvsStatExportColonne> colonnes = dao.loadNameQueries("YvsStatExportColonne.findAll", new String[]{"etat"}, new Object[]{selectExportEtat});
                List<String> titles = new ArrayList<>();
                if (!colonnes.isEmpty()) {
                    for (YvsStatExportColonne y : colonnes) {
                        if (y.getIntegrer() && y.getVisible()) {
                            titles.add(y.getLibelle());
                        }
                    }
                }
                switch (selectExportEtat.getFormat()) {
                    case Constantes.FILE_CSV:
                        resultImport = onImporteCSV(file, colonnes, titles);
                        break;
                    case Constantes.FILE_TEXT:
                        resultImport = onImporteTXT(file, colonnes, titles, selectExportEtat.getSeparateur());
                        break;
                }
                openDialog("dlgImportData");
                update("blog_import_data");
            }
        } catch (IOException ex) {
            Logger.getLogger(ManagedImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<ImportData.TableOrder> onOrdonnerTable(List<YvsStatExportColonne> colonnes) {
        List<ImportData.TableOrder> tables = new ArrayList<>();
        for (YvsStatExportColonne y : colonnes) {
            ImportData.TableOrder table = new ImportData.TableOrder(y.getTableName());
            if (y.getKey()) {
                table.setKey(y.getColonne());
            }
            int index = tables.indexOf(table);
            if (index < 0) {
                tables.add(table);
            } else if (y.getKey()) {
                tables.set(index, table);
            }
        }

        List<ImportData.TableOrder> result = new ArrayList<>(tables);
        String query = "SELECT tc.constraint_name, tc.table_name, kcu.column_name, ccu.table_name AS foreign_table_name, ccu.column_name AS foreign_column_name "
                + "FROM information_schema.table_constraints AS tc "
                + "JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name AND tc.constraint_schema = kcu.constraint_schema "
                + "JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name AND ccu.constraint_schema = tc.constraint_schema "
                + "WHERE tc.constraint_type = 'FOREIGN KEY' AND tc.table_name = ?";
        for (int i = 0; i < tables.size(); i++) {
            ImportData.TableOrder table = tables.get(i);
            Query qr = dao.getEntityManager().createNativeQuery(query).setParameter(1, table.getTable());
            List<Object[]> foreigns = qr.getResultList();
            if (foreigns != null && !foreigns.isEmpty()) {
                for (Object[] foreign : foreigns) {
                    String tableForeign = foreign[3].toString();
                    String columnForeign = foreign[2].toString();
                    int i1 = result.indexOf(new ImportData.TableOrder(tableForeign));
                    if (i1 > -1) {
                        int i0 = result.indexOf(table);
                        if (i1 > i0) {
                            ImportData.TableOrder tab = result.get(i1);
                            result.set(i0, tab);
                            result.set(i1, table);
                        } else {
                            result.set(i0, table);
                        }
                    }
                    if (tablesConditionsForce.contains(tableForeign)) {
                        int i0 = result.indexOf(table);
                        if (i0 > -1) {
                            Object value = null;
                            switch (tableForeign) {
                                case "yvs_societes":
                                    value = currentAgence.getSociete().getId();
                                    break;
                                case "yvs_agences":
                                    value = currentAgence.getId();
                                    break;
                                case "yvs_users":
                                    value = currentUser.getUsers().getId();
                                    break;
                            }
                            result.get(i0).getConditionsForce().add(new ImportData.TableContrainte(tableForeign, columnForeign, value));
                        }
                    }
                }
            }
        }
        return result;
    }

    private ReturnImport onImporteCSV(File file, List<YvsStatExportColonne> colonnes, List<String> titles) throws FileNotFoundException, IOException {
        return onImporteTXT(file, colonnes, titles, ";");
    }

    private ReturnImport onImporteTXT(File file, List<YvsStatExportColonne> colonnes, List<String> titles, String separator) throws FileNotFoundException, IOException {
        ReturnImport result = new ReturnImport(titles);
        if (colonnes == null || colonnes.isEmpty()) {
            return result;
        }
        List<ImportData.TableOrder> ordres = onOrdonnerTable(colonnes);
        separator = (separator == null || separator.trim().isEmpty()) ? ";" : separator;
        String[] headers = new String[0];
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String ligne;
            boolean takeHeader = titles.isEmpty();
            while ((ligne = br.readLine()) != null) {
                String[] lignes = ligne.split(separator);
                if (!takeHeader) {
                    headers = lignes;
                    if (headers.length != titles.size()) {
                        getErrorMessage("Les colonnes ne sont pas identiques");
                        return result;
                    }
                    for (int i = 0; i < headers.length - 1; i++) {
                        if (headers[i].equals(titles.get(i))) {
                            getErrorMessage("Les colonnes ne sont pas identiques");
                            return result;
                        }
                    }
                    takeHeader = true;
                } else {
                    result.getLignes().add(lignes);
                    ImportData data = new ImportData();
                    for (int i = 0; i < lignes.length; i++) {
                        String header = headers.length < 1 ? "" : sanitize(headers[i]).trim();
                        YvsStatExportColonne colonne = null;
                        if (header == null || header.trim().isEmpty()) {
                            colonne = colonnes.size() > i ? new YvsStatExportColonne(colonnes.get(i)) : null;
                        } else {
                            for (YvsStatExportColonne y : colonnes) {
                                String libelle = y.getLibelle();
                                if (libelle.equals(header)) {
                                    colonne = new YvsStatExportColonne(y);
                                    break;
                                }
                            }
                        }
                        if (colonne == null) {
                            continue;
                        }
                        String current = sanitize(lignes[i]).trim();
                        current = (current != null && !current.isEmpty()) ? current : "";
                        colonne.setValue(ImportData.getValues(colonne.getType(), current));
                        if (colonne.getKey()) {
                            int index = data.getKeys().indexOf(colonne);
                            if (index < 0) {
                                data.getKeys().add(colonne);
                            }
                        }
                        data.getColonnes().add(colonne);
                    }
                    for (YvsStatExportColonne colonne : colonnes) {
                        int index = data.getColonnes().indexOf(colonne);
                        if (index > -1) {
                            continue;
                        }
                        if (colonne.getCondition()) {
                            if ((Util.asString(colonne.getConditionExpression()) && (colonne.getConditionExpression().startsWith(":") || colonne.getConditionExpression().startsWith("?")))
                                    || (Util.asString(colonne.getConditionExpressionOther()) && (colonne.getConditionExpressionOther().startsWith(":") || colonne.getConditionExpressionOther().startsWith("?")))) {
                                switch (colonne.getColonne()) {
                                    case "societe":
                                        if (!colonne.getVisible()) {
                                            colonne.setValue(currentAgence.getSociete().getId());
                                        }
                                        colonne.setConditionValue(currentAgence.getSociete().getId());
                                        colonne.setConditionValueOther(currentAgence.getSociete().getId());
                                        break;
                                    case "agence":
                                        if (!colonne.getVisible()) {
                                            colonne.setValue(currentAgence.getId());
                                        }
                                        colonne.setConditionValue(currentAgence.getId());
                                        colonne.setConditionValueOther(currentAgence.getId());
                                        break;
                                    case "user":
                                    case "users":
                                        if (!colonne.getVisible()) {
                                            colonne.setValue(currentUser.getUsers().getId());
                                        }
                                        colonne.setConditionValue(currentUser.getUsers().getId());
                                        colonne.setConditionValueOther(currentUser.getUsers().getId());
                                        break;
                                }
                                if (colonne.getValue() != null || colonne.getConditionValue() != null) {
                                    data.getColonnes().add(colonne);
                                    continue;
                                }
                            } else {
                                colonne.setCondition(false);
                                colonne.setValue(ImportData.getValues(colonne.getType(), colonne.getConditionExpression()));
                                data.getColonnes().add(colonne);
                                continue;
                            }
                        }
                        if (colonne.getContrainte()) {
                            switch (colonne.getTableNameLiee()) {
                                case "yvs_societes":
                                    colonne.setValue(currentAgence.getSociete().getId());
                                    break;
                                case "yvs_agences":
                                    colonne.setValue(currentAgence.getId());
                                    break;
                                case "yvs_users":
                                    colonne.setValue(currentUser.getUsers().getId());
                                    break;
                            }
                            data.getColonnes().add(colonne);
                        }
                    }
                    result.getDatas().add(data);
                }
            }
        }
        result.setTitles(Arrays.asList(headers));
        result.setTables(ordres);

        return result;
    }

    public void onImporte() {
        if (resultImport == null) {
            return;
        }
        List<String> colonnes;
        List<Options> values;
        List<String> conditions;
        List<Options> parametres;
        for (ImportData data : resultImport.getDatas()) {
            for (ImportData.TableOrder table : resultImport.getTables()) {
                colonnes = new ArrayList<>();
                values = new ArrayList<>();
                conditions = new ArrayList<>();
                parametres = new ArrayList<>();
                ImportData.ImportValue value = new ImportData.ImportValue();
                for (YvsStatExportColonne colonne : data.getColonnes()) {
                    if (!table.getTable().equals(colonne.getTableName())) {
                        continue;
                    }
                    if (colonne.getContrainte()) {
                        if (colonne.getValue() != null) {
                            colonnes.add(colonne.getColonne());
                            values.add(new Options(colonne.getValue(), values.size() + 1));

                            conditions.add(colonne.getColonne() + " = ?");
                            parametres.add(new Options(colonne.getValue(), parametres.size() + 1));
                        } else {
                            YvsStatExportColonne key = data.getKey(colonne.getTableNameLiee(), null);
                            int index = resultImport.getTables().indexOf(new ImportData.TableOrder(colonne.getTableNameLiee()));
                            if (index > -1 && key != null) {
                                ImportData.TableOrder contrainte = resultImport.getTables().get(index);
                                for (ImportData.ImportValue current : contrainte.getValues()) {
                                    if (current.getId() == null) {
                                        continue;
                                    }
                                    if (current.getKey() != null && Objects.equals(current.getKey().getValue(), key.getValue())) {
                                        colonnes.add(colonne.getColonne());
                                        values.add(new Options(current.getId(), values.size() + 1));

                                        conditions.add(colonne.getColonne() + " = ?");
                                        parametres.add(new Options(current.getId(), parametres.size() + 1));

                                        colonne.setValue(current.getId());
                                        value.getColonnes().add(colonne);
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if (colonne.getKey()) {
                            value.setKey(colonne);
                        }
                        if (colonne.getValue() != null) {
                            colonnes.add(colonne.getColonne());
                            values.add(new Options(colonne.getValue(), values.size() + 1));
                        }
                        if (colonne.getKey() || colonne.getCondition()) {
                            if (colonne.getConditionValue() != null) {
                                String condition = colonne.getColonne() + " " + colonne.getConditionOperator() + " ?";
                                parametres.add(new Options(colonne.getConditionValue(), parametres.size() + 1));
                                if (colonne.getConditionValueOther() != null && colonne.getConditionOperator().equals("BETWEEN")) {
                                    condition += " AND ?";
                                    parametres.add(new Options(colonne.getConditionValueOther(), parametres.size() + 1));
                                }
                                conditions.add(condition);
                            } else if (colonne.getValue() != null) {
                                conditions.add(colonne.getColonne() + " = ?");
                                parametres.add(new Options(colonne.getValue(), parametres.size() + 1));
                            }
                        }
                        value.getColonnes().add(colonne);
                    }
                }
                for (ImportData.TableContrainte force : table.getConditionsForce()) {
                    if (!conditions.contains(force.getColonne() + " = ?")) {
                        conditions.add(force.getColonne() + " = ?");
                        parametres.add(new Options(force.getValue(), parametres.size() + 1));
                    }
                    if (!colonnes.contains(force.getColonne())) {
                        colonnes.add(force.getColonne());
                        values.add(new Options(force.getValue(), values.size() + 1));
                    }
                }
                Long id = null;
                if (!conditions.isEmpty()) {
                    String _conditions = " WHERE " + Util.join(" AND ", conditions);
                    String query = "SELECT id FROM " + table.getTable() + " " + _conditions + " LIMIT 1";
                    id = (Long) dao.loadObjectBySqlQuery(query, parametres.toArray(new Options[parametres.size()]));
                }
                if (id == null) {
                    String _colonnes = Util.join(", ", colonnes);
                    String _values = Util.join(", ", values, "?");
                    String query = "INSERT INTO " + table.getTable() + "(" + _colonnes + ") VALUES (" + _values + ") RETURNING id";
                    id = (Long) dao.loadObjectBySqlQuery(query, values.toArray(new Options[values.size()]));
                }
                if (id != null) {
                    value.setId(id);
                    table.getValues().add(value);
                }
            }
        }
        succes();
    }
    /*/END IMPORTATION/*/
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Query;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.stat.export.YvsStatExportColonne;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.stat.UtilStat;
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
public class ManagedEtatExport extends Managed<ExportEtat, YvsStatExportEtat> implements Serializable {

    @ManagedProperty(value = "#{exportEtat}")
    private ExportEtat exportEtat;
    private YvsStatExportEtat selectExportEtat;
    private List<YvsStatExportEtat> etats;
    private ExportColonne colonne = new ExportColonne();
    private PaginatorResult<YvsStatExportColonne> p_colonnes = new PaginatorResult<>();

    private List<String> references;
    private List<String> tableLiee, tables, colonnes, tablesContraint, colonnesContraint;
    private String numSearch, msg_erreur;

    private Boolean visibleSearch, integrerSearch, contrainteSearch;
    private String tableSearch;
    private String idsExport;

    public ManagedEtatExport() {
        etats = new ArrayList<>();
        tableLiee = new ArrayList<>();
        references = new ArrayList<>();
        tables = new ArrayList<>();
        colonnes = new ArrayList<>();
        tablesContraint = new ArrayList<>();
        colonnesContraint = new ArrayList<>();
    }

    public YvsStatExportEtat getSelectExportEtat() {
        return selectExportEtat;
    }

    public void setSelectExportEtat(YvsStatExportEtat selectExportEtat) {
        this.selectExportEtat = selectExportEtat;
    }

    public String getIdsExport() {
        return idsExport;
    }

    public void setIdsExport(String idsExport) {
        this.idsExport = idsExport;
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
        if (bean.getLibelle() != null ? bean.getLibelle().length() < 1 : true) {
            getErrorMessage("Vous devez precisez le libelle");
            return false;
        }
        if (bean.getTablePrincipal() != null ? bean.getTablePrincipal().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la table principale");
            return false;
        }
        if (bean.isForExportation()) {
            if (bean.getColonnePrincipal() != null ? bean.getColonnePrincipal().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la colonne principale");
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
        if (bean.getTableName() != null ? bean.getTableName().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la table");
            return false;
        }
        if (bean.getColonne() != null ? bean.getColonne().length() < 1 : true) {
            getErrorMessage("Vous devez precisez la colonne");
            return false;
        }
        if (bean.getEtat().isForExportation() ? bean.isContrainte() : false) {
            if (bean.getTableNameLiee() != null ? bean.getTableNameLiee().length() < 1 : true) {
                getErrorMessage("Vous devez precisez la table liée");
                return false;
            }
            if (bean.getColonneLiee() != null ? bean.getColonneLiee().length() < 1 : true) {
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
        nameQueri = "YvsStatExportColonne.findOneByEtat";
        champ = new String[]{"tableName", "colonne", "etat"};
        val = new Object[]{bean.getTableName(), bean.getColonne(), new YvsStatExportEtat(bean.getEtat().getId())};
        YvsStatExportColonne y = (YvsStatExportColonne) dao.loadOneByNameQueries(nameQueri, champ, val);
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
        colonne.setTableName(exportEtat.getTablePrincipal());
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
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsStatExportEtat) dao.save1(y);
                    exportEtat.setId(y.getId());
                }
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
                if (y != null ? y.getId() > 0 : false) {
                    dao.update(y);
                } else {
                    y.setId(null);
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
                }
                succes();
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

    public void compilerModel() {
        try {
            String query = buildRequeteExport(exportEtat.getReference(), new Long[]{(long) 0});
            Query qr = dao.getEntityManager().createNativeQuery(query);
            qr.getResultList();
            getInfoMessage("Correct !");
        } catch (Exception ex) {
            msg_erreur = ex.getMessage();
            update("txt_msg_erreur_compilation_export");
            openDialog("dlgErreurExport");
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

        String type = (String) dao.loadObjectBySqlQuery("SELECT data_type FROM information_schema.columns WHERE table_name = ? AND column_name = ?", new Options[]{new Options(colonne.getTableName(), 1), new Options(colonne.getColonne(), 2)});
        if (type != null ? type.trim().length() > 0 : false) {
            if (type.equals("date")) {
                colonne.setColonneDate(true);
            }
        }
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

    public void chooseIntegrer() {
        if (!colonne.isIntegrer()) {
            colonne.setVisible(false);
        }
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
                c.setId(null);
                c = (YvsStatExportColonne) dao.save1(c);
            }
            etats.add(bean);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Duplication impossible");
        }
    }

    public void exporter() {
        try {
            List<Long> ids = new ArrayList<>();
            if (Util.asString(idsExport)) {
                ids = decomposeIdSelection(idsExport);
            }
            executeExport(selectExportEtat.getReference(), ids);
        } catch (Exception ex) {
            getErrorMessage("Exportation impossible");
            getException("Exportation impossible (exporter)", ex);
        }
    }
}

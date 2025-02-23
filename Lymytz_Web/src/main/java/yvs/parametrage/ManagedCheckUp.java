/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Table;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.MyClassLoader;
import yvs.annotation.Descriptor;
import yvs.entity.synchro.YvsSynchroListenTable;
import yvs.entity.users.YvsUsers;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedCheckUp extends Managed<Serializable, YvsSynchroListenTable> implements Serializable {

    private List<YvsSynchroListenTable> result;
    static List<YvsUsers> users;
    static List<Class<?>> entities;

    static List<Tables> tables;

    private String userSearch;
    private Long sourceSearch;
    private String tableSearch;
    private String actionSearch;
    private boolean dateSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();

    private MyClassLoader CLASSLOADER = new MyClassLoader();

    public ManagedCheckUp() {
        result = new ArrayList<>();
        users = new ArrayList<>();
        entities = new ArrayList<>();
        tables = new ArrayList<>();
    }

    public List<YvsSynchroListenTable> getResult() {
        return result;
    }

    public void setResult(List<YvsSynchroListenTable> result) {
        this.result = result;
    }

    public String getUserSearch() {
        return userSearch;
    }

    public void setUserSearch(String userSearch) {
        this.userSearch = userSearch;
    }

    public Long getSourceSearch() {
        return sourceSearch;
    }

    public void setSourceSearch(Long sourceSearch) {
        this.sourceSearch = sourceSearch;
    }

    public String getTableSearch() {
        return tableSearch;
    }

    public void setTableSearch(String tableSearch) {
        this.tableSearch = tableSearch;
    }

    public String getActionSearch() {
        return actionSearch;
    }

    public void setActionSearch(String actionSearch) {
        this.actionSearch = actionSearch;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    @Override
    public void loadAll() {
        if (users.isEmpty()) {
            users = dao.loadNameQueries("YvsUsers.findAllUser", new String[]{}, new Object[]{});
        }
        if (entities.isEmpty()) {
            entities = CLASSLOADER.getAllClass();
        }
    }

    public void loadAll(boolean avance, boolean init) {
//        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        result = paginator.executeDynamicQuery("YvsSynchroListenTable", "y.dateSave DESC", avance, init, (int) imax, dao);
    }

    public void paginer(boolean avancer) {
        loadAll(false, avancer);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public void clearParams() {
        paginator.clear();
        loadAll(true, true);
    }

    public void addParamUsers() {
        ParametreRequete p = new ParametreRequete("y.author", "author", null);
        if ((userSearch != null) ? !userSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("y.author", "author", getAuteursId(userSearch), "IN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamAction() {
        ParametreRequete p = new ParametreRequete("y.actionName", "actionName", null);
        if (actionSearch != null ? !actionSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("y.actionName", "actionName", actionSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamtable() {
        ParametreRequete p = new ParametreRequete("y.nameTable", "nameTable", null);
        if (tableSearch != null ? !tableSearch.trim().isEmpty() : false) {
            p = new ParametreRequete("y.nameTable", "nameTable", tableSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamSource() {
        ParametreRequete p = new ParametreRequete("y.idSource", "idSource", null);
        if (sourceSearch != null ? sourceSearch > 0 : false) {
            p = new ParametreRequete("y.idSource", "idSource", sourceSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateSave", "dateSave", null);
        if (dateSearch) {
            p = new ParametreRequete("y.dateSave", "dateSave", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public String getAuteurName(Long id) {
        if (id != null ? id > 0 : false) {
            int index = users.indexOf(new YvsUsers(id));
            if (index > -1) {
                return users.get(index).getNomUsers();
            }
        }
        return "--INCONNU--";
    }

    public String getTableName(String table) {
        if (asString(table)) {
            int index = tables.indexOf(new Tables(table));
            if (index > -1) {
                return tables.get(index).getLibelle();
            }
            Annotation annotation;
            String value;
            for (Class eClass : entities) {
                annotation = eClass.getAnnotation(Table.class);
                if (annotation == null) {
                    continue;
                }
                value = ((Table) annotation).name();
                if (value.equals(table)) {
                    annotation = eClass.getAnnotation(Descriptor.class);
                    value = annotation != null ? ((Descriptor) annotation).name() : table;
                    tables.add(new Tables(table, value));
                    return value;
                }
                tables.add(new Tables(table, table));
            }
        }
        return table;
    }

    private List<Long> getAuteursId(String auteur) {
        List<Long> list = new ArrayList<Long>() {
            {
                add(-1L);
            }
        };
        String name = auteur.replace("%", "").trim();
        for (YvsUsers item : users) {
            if (item.getCodeUsers() == null || item.getNomUsers() == null) {
                continue;
            }
            if (auteur.startsWith("%") && auteur.endsWith("%")) {
                if (item.getCodeUsers().contains(name) || item.getNomUsers().contains(name)) {
                    list.add(item.getId());
                }
            } else if (auteur.startsWith("%")) {
                if (item.getCodeUsers().endsWith(name) || item.getNomUsers().endsWith(name)) {
                    list.add(item.getId());
                }
            } else if (auteur.endsWith("%")) {
                if (item.getCodeUsers().startsWith(name) || item.getNomUsers().startsWith(name)) {
                    list.add(item.getId());
                }
            } else {
                if (item.getCodeUsers().equals(name) || item.getNomUsers().equals(name)) {
                    list.add(item.getId());
                }
            }
        }
        return list;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
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

    class Tables {

        private String name;
        private String libelle;

        public Tables(String name) {
            this.name = name;
        }

        public Tables(String name, String libelle) {
            this.name = name;
            this.libelle = libelle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLibelle() {
            return libelle;
        }

        public void setLibelle(String libelle) {
            this.libelle = libelle;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tables other = (Tables) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

    }

}

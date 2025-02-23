/*
 * To change this license contrat, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.contrat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.print.YvsPrintContratEmployeArticle;
import yvs.entity.print.YvsPrintContratEmployeHeader;
import yvs.util.Managed;
import static yvs.util.Managed.formatDate;
import static yvs.util.Managed.formatHeure;
import yvs.util.ParametreRequete;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintContrat extends Managed<PrintContratHeader, YvsPrintContratEmployeHeader> implements Serializable {

    private PrintContratHeader bean = new PrintContratHeader();
    private PrintContratArticle article = new PrintContratArticle();
    private YvsPrintContratEmployeHeader entity = new YvsPrintContratEmployeHeader();
    private List<YvsPrintContratEmployeHeader> list;

    private List<VariablePrint> variables;

    private String nomSearch;

    public ManagedPrintContrat() {
        this.list = new ArrayList<>();
        this.variables = new ArrayList<VariablePrint>() {
            {
                add(new VariablePrint("AE", "Adresse de l'employé", ""));
                add(new VariablePrint("DE", "Date d'embauche de l'employé", ""));
                add(new VariablePrint("HHE", "Horaire hebdomadaire de l'employé", ""));
                add(new VariablePrint("NE", "Nom de l'employé", ""));
                add(new VariablePrint("NUIE", "Numero d'immatrulation de l'employé", ""));
                add(new VariablePrint("PE", "Poste de l'employé", ""));
                add(new VariablePrint("SME", "Salaire mensuel de l'employé", ""));
            }
        };
    }

    private String getValueTest(String variable) {
        switch (variable) {
            case "AE":
                return "Essos - Yaounde";
            case "DE":
                return formatDate.format(new Date());
            case "HHE":
                return "48";
            case "NUIE":
                return "0123456789";
            case "PE":
                return "Adjoint comptable";
            case "SME":
                return "100 000";
            case "NE":
                return currentUser.getUsers().getNomUsers();
        }
        return "";
    }

    public List<VariablePrint> getVariables() {
        return variables;
    }

    public void setVariables(List<VariablePrint> variables) {
        this.variables = variables;
    }

    public String getNomSearch() {
        return nomSearch;
    }

    public void setNomSearch(String nomSearch) {
        this.nomSearch = nomSearch;
    }

    public PrintContratHeader getBean() {
        return bean;
    }

    public void setBean(PrintContratHeader bean) {
        this.bean = bean;
    }

    public PrintContratArticle getArticle() {
        return article;
    }

    public void setArticle(PrintContratArticle article) {
        this.article = article;
    }

    public YvsPrintContratEmployeHeader getEntity() {
        return entity;
    }

    public void setEntity(YvsPrintContratEmployeHeader entity) {
        this.entity = entity;
    }

    public List<YvsPrintContratEmployeHeader> getList() {
        return list;
    }

    public void setList(List<YvsPrintContratEmployeHeader> list) {
        this.list = list;
    }

    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintContratEmployeHeader.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_contrat");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    public void loadAll(boolean avancer, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
            list = paginator.executeDynamicQuery("YvsPrintContratEmployeHeader", "y.nom", avancer, init, (int) imax, dao);
            update("blog-print_contrat");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public boolean controleFiche(PrintContratHeader bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (bean.getNom() != null ? bean.getNom().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser le nom du model");
                return false;
            }
            if (bean.getModel() != null ? bean.getModel().trim().length() < 1 : true) {
                getErrorMessage("Vous devez selectionner un modèle");
                return false;
            }
            YvsPrintContratEmployeHeader y = (YvsPrintContratEmployeHeader) dao.loadOneByNameQueries("YvsPrintContratEmployeHeader.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le nom " + bean.getNom());
                return false;
            }
            if (bean.isDefaut()) {
                y = (YvsPrintContratEmployeHeader) dao.loadOneByNameQueries("YvsPrintContratEmployeHeader.findByDefaut", new String[]{"societe", "defaut"}, new Object[]{currentAgence.getSociete(), true});
                if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                    getErrorMessage("Vous avez deja creer un model par défaut ");
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("controleFiche", ex);
        }
        return false;
    }

    public boolean controleFiche(PrintContratArticle bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (bean.getHeader() != null ? bean.getHeader().getId() < 1 : true) {
                saveNew();
                bean.setHeader(this.bean);
            }
            if (bean.getHeader() != null ? bean.getHeader().getId() < 1 : true) {
                return false;
            }
            if (bean.getTitre() != null ? bean.getTitre().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser le titre");
                return false;
            }
            if (bean.getContenu() != null ? bean.getContenu().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser un contenu");
                return false;
            }
            YvsPrintContratEmployeArticle y = (YvsPrintContratEmployeArticle) dao.loadOneByNameQueries("YvsPrintContratEmployeArticle.findByIndice", new String[]{"indice", "header"}, new Object[]{bean.getIndice(), entity});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le niveau " + bean.getIndice());
                return false;
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("controleFiche", ex);
        }
        return false;
    }

    @Override
    public void resetFiche() {
        try {
            bean = new PrintContratHeader();
            entity = new YvsPrintContratEmployeHeader();
            resetFicheArticle();
            update("form-print_contrat");
            update("data-print_contrat");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    public void resetFicheArticle() {
        try {
            article = new PrintContratArticle();
            update("tab-print_contrat:blog-print_conrat_article");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            bean.setModel("contrat_employe");
            if (controleFiche(bean)) {
                entity = PrintContratHeader.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintContratEmployeHeader) dao.save1(entity);
                    bean.setId(entity.getId());
                } else {
                    entity = (YvsPrintContratEmployeHeader) dao.update(entity);
                }
                int index = list.indexOf(entity);
                if (index > -1) {
                    list.set(index, entity);
                } else {
                    list.add(0, entity);
                }
                update("data-print_contrat");
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("saveNew", ex);
        }
        return false;
    }

    public boolean saveNewArticle() {
        try {
            article.setHeader(bean);
            if (controleFiche(article)) {
                YvsPrintContratEmployeArticle entity = PrintContratArticle.buildEntity(article, currentUser);
                if (article.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintContratEmployeArticle) dao.save1(entity);
                } else {
                    entity = (YvsPrintContratEmployeArticle) dao.update(entity);
                }
                int index = bean.getArticles().indexOf(entity);
                if (index > -1) {
                    bean.getArticles().set(index, entity);
                } else {
                    bean.getArticles().add(entity);
                }
                resetFicheArticle();
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("saveNew", ex);
        }
        return false;
    }

    @Override
    public void deleteBean(YvsPrintContratEmployeHeader y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_contrat");
                }
            } else {
                entity = y;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("deleteBean", ex);
        }
    }

    public void deleteBeanArticle(YvsPrintContratEmployeArticle y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(article.getId())) {
                        resetFicheArticle();
                    }
                    bean.getArticles().remove(y);
                    update("tab-print_contrat:data-print_conrat_article");
                }
            } else {

            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("deleteBean", ex);
        }
    }

    @Override
    public void onSelectObject(YvsPrintContratEmployeHeader y) {
        try {
            bean = PrintContratHeader.buildBean(y);
            bean.setArticles(dao.loadNameQueries("YvsPrintContratEmployeArticle.findAll", new String[]{"header"}, new Object[]{y}));
            entity = y;
            resetFicheArticle();
            update("form-print_contrat");
            update("data-print_contrat");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    public void onSelectObjectArticle(YvsPrintContratEmployeArticle y) {
        try {
            article = PrintContratArticle.buildBean(y);
            update("tab-print_contrat:blog-print_conrat_article");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintContratEmployeHeader) ev.getObject());
            }
        } catch (Exception ex) {
            getException("loadOnView", ex);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        try {
            resetFiche();
        } catch (Exception ex) {
            getException("unLoadOnView", ex);
        }
    }

    public void print(YvsPrintContratEmployeHeader y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                String query = "SELECT y.id FROM yvs_grh_contrat_emps y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE a.societe = ? ORDER BY y.id DESC LIMIT 1";
                Object id = dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
                Map<String, Object> param = new HashMap<>();
                param.put("MODEL", y.getId().intValue());
                param.put("CONTRAT", (id != null ? ((Long) id).intValue() : 0));
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                executeReport("contrat_employe", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedPrintContrat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dropToPartSociete(DragDropEvent ddEvent) {
        if (bean != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = bean.getPartieSociete();
            bean.setPartieSociete(contenu + " @" + var.getCode()+"@");
        }
    }

    public void dropToPartPrestataire(DragDropEvent ddEvent) {
        if (bean != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = bean.getPartiePrestataire();
            bean.setPartiePrestataire(contenu + " @" + var.getCode()+"@");
        }
    }

    public void dropToContenu(DragDropEvent ddEvent) {
        if (article != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = article.getContenu();
            article.setContenu(contenu + " @" + var.getCode()+"@");
        }
    }

    public void clearParams() {
        nomSearch = null;
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamNom() {
        ParametreRequete p = new ParametreRequete("y.nom", "nom", null);
        if (nomSearch != null ? nomSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", nomSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nom)", "nom", nomSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.titre)", "titre", nomSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.model)", "model", nomSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public String getContenu(String contenu) {
        String result = contenu;
        for (VariablePrint variable : variables) {
            result = result.replace("@" + variable.getCode()+"@", getValueTest(variable.getCode()));
        }
        return result;
    }

}

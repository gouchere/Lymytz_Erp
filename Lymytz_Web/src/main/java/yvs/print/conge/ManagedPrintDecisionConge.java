/*
 * To change this license decision_conge, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print.conge;

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
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.print.YvsPrintDecisionCongeArticle;
import yvs.entity.print.YvsPrintDecisionCongeHeader;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.print.contrat.VariablePrint;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintDecisionConge extends Managed<PrintDecisionCongeHeader, YvsPrintDecisionCongeHeader> implements Serializable {

    private PrintDecisionCongeHeader bean = new PrintDecisionCongeHeader();
    private PrintDecisionCongeArticle article = new PrintDecisionCongeArticle();
    private YvsPrintDecisionCongeHeader entity = new YvsPrintDecisionCongeHeader();
    private List<YvsPrintDecisionCongeHeader> list;

    private List<VariablePrint> variables;

    private String nomSearch;

    public ManagedPrintDecisionConge() {
        this.list = new ArrayList<>();
        this.variables = new ArrayList<VariablePrint>() {
            {
                add(new VariablePrint("DDE", "Date de début de l'exercice", ""));
                add(new VariablePrint("DFE", "Date de fin de l'exercice", ""));
                add(new VariablePrint("DDC", "Date début du congé", ""));
                add(new VariablePrint("DFC", "Date fin du congé", ""));
                add(new VariablePrint("DRC", "Date de retour du congé", ""));
                add(new VariablePrint("TC", "Durée du congé", ""));
                add(new VariablePrint("TCS", "Durée du congé en lettre", ""));
                add(new VariablePrint("TPC", "Durée principal du congé", ""));
                add(new VariablePrint("TPCS", "Durée principal du congé en lettre", ""));
                add(new VariablePrint("TSC", "Durée supplémentaire du congé", ""));
                add(new VariablePrint("TSCS", "Durée supplémentaire du congé en lettre", ""));
                add(new VariablePrint("TPPC", "Durée permission prise du congé", ""));
                add(new VariablePrint("TPPCS", "Durée permission prise du congé en lettre", ""));
                add(new VariablePrint("HRC", "Heure de retour effective du congé", ""));
                add(new VariablePrint("NE", "Nom de l'employé", ""));
            }
        };
    }

    private String getValueTest(String variable) {
        switch (variable) {
            case "DDE":
                return formatDate.format(currentExo.getDateDebut());
            case "DFE":
                return formatDate.format(currentExo.getDateFin());
            case "DDC":
                return formatDate.format(new Date());
            case "DFC":
                return formatDate.format(new Date());
            case "DRC":
                return formatDate.format(new Date());
            case "TC":
                return "1";
            case "TCS":
                return "un";
            case "TPC":
                return "1";
            case "TPCS":
                return "un";
            case "TSC":
                return "1";
            case "TSCS":
                return "un";
            case "TPPC":
                return "1";
            case "TPPCS":
                return "un";
            case "HRC":
                return formatHeure.format(new Date());
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

    public PrintDecisionCongeHeader getBean() {
        return bean;
    }

    public void setBean(PrintDecisionCongeHeader bean) {
        this.bean = bean;
    }

    public PrintDecisionCongeArticle getArticle() {
        return article;
    }

    public void setArticle(PrintDecisionCongeArticle article) {
        this.article = article;
    }

    public YvsPrintDecisionCongeHeader getEntity() {
        return entity;
    }

    public void setEntity(YvsPrintDecisionCongeHeader entity) {
        this.entity = entity;
    }

    public List<YvsPrintDecisionCongeHeader> getList() {
        return list;
    }

    public void setList(List<YvsPrintDecisionCongeHeader> list) {
        this.list = list;
    }

    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintDecisionCongeHeader.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_decision_conge");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    public void loadAll(boolean avancer, boolean init) {
        try {
            paginator.addParam(new ParametreRequete("y.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
            list = paginator.executeDynamicQuery("YvsPrintDecisionCongeHeader", "y.nom", avancer, init, (int) imax, dao);
            update("blog-print_decision_conge");
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
    public boolean controleFiche(PrintDecisionCongeHeader bean) {
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
            YvsPrintDecisionCongeHeader y = (YvsPrintDecisionCongeHeader) dao.loadOneByNameQueries("YvsPrintDecisionCongeHeader.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le nom " + bean.getNom());
                return false;
            }
            if (bean.isDefaut()) {
                y = (YvsPrintDecisionCongeHeader) dao.loadOneByNameQueries("YvsPrintDecisionCongeHeader.findByDefaut", new String[]{"societe", "defaut"}, new Object[]{currentAgence.getSociete(), true});
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

    public boolean controleFiche(PrintDecisionCongeArticle bean) {
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
            if (bean.getContenu() != null ? bean.getContenu().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser un contenu");
                return false;
            }
            YvsPrintDecisionCongeArticle y = (YvsPrintDecisionCongeArticle) dao.loadOneByNameQueries("YvsPrintDecisionCongeArticle.findByIndice", new String[]{"indice", "header"}, new Object[]{bean.getIndice(), entity});
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
            bean = new PrintDecisionCongeHeader();
            entity = new YvsPrintDecisionCongeHeader();
            resetFicheArticle();
            update("form-print_decision_conge");
            update("data-print_decision_conge");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    public void resetFicheArticle() {
        try {
            article = new PrintDecisionCongeArticle();
            update("tab-print_decision_conge:blog-print_decision_conge_article");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            bean.setModel("decision_conge");
            if (controleFiche(bean)) {
                entity = PrintDecisionCongeHeader.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintDecisionCongeHeader) dao.save1(entity);
                    bean.setId(entity.getId());
                } else {
                    entity = (YvsPrintDecisionCongeHeader) dao.update(entity);
                }
                int index = list.indexOf(entity);
                if (index > -1) {
                    list.set(index, entity);
                } else {
                    list.add(0, entity);
                }
                update("data-print_decision_conge");
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
                YvsPrintDecisionCongeArticle entity = PrintDecisionCongeArticle.buildEntity(article, currentUser);
                if (article.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintDecisionCongeArticle) dao.save1(entity);
                } else {
                    entity = (YvsPrintDecisionCongeArticle) dao.update(entity);
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
    public void deleteBean(YvsPrintDecisionCongeHeader y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_decision_conge");
                }
            } else {
                entity = y;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("deleteBean", ex);
        }
    }

    public void deleteBeanArticle(YvsPrintDecisionCongeArticle y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(article.getId())) {
                        resetFicheArticle();
                    }
                    bean.getArticles().remove(y);
                    update("tab-print_decision_conge:data-print_decision_conge_article");
                }
            } else {

            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("deleteBean", ex);
        }
    }

    @Override
    public void onSelectObject(YvsPrintDecisionCongeHeader y) {
        try {
            bean = PrintDecisionCongeHeader.buildBean(y);
            bean.setArticles(dao.loadNameQueries("YvsPrintDecisionCongeArticle.findAll", new String[]{"header"}, new Object[]{y}));
            entity = y;
            resetFicheArticle();
            update("form-print_decision_conge");
            update("data-print_decision_conge");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    public void onSelectObjectArticle(YvsPrintDecisionCongeArticle y) {
        try {
            article = PrintDecisionCongeArticle.buildBean(y);
            update("tab-print_decision_conge:blog-print_decision_conge_article");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintDecisionCongeHeader) ev.getObject());
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

    public void print(YvsPrintDecisionCongeHeader y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                String query = "SELECT y.id FROM yvs_grh_conge_emps y INNER JOIN yvs_grh_employes e ON y.employe = e.id INNER JOIN yvs_agences a ON e.agence = a.id WHERE a.societe = ? ORDER BY y.id DESC LIMIT 1";
                Object id = dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
                Map<String, Object> param = new HashMap<>();
                param.put("MODEL", y.getId().intValue());
                param.put("ID", (id != null ? ((Long) id).intValue() : 0));
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("IMG_LOGO", returnLogo());
                param.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
                param.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
                param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
                executeReport("decision_conge", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedPrintDecisionConge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dropToIntroduction(DragDropEvent ddEvent) {
        if (bean != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = bean.getIntroduction();
            bean.setIntroduction(contenu + " @" + var.getCode() + "@");
        }
    }

    public void dropToDefinitionConventive(DragDropEvent ddEvent) {
        if (bean != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = bean.getDefinitionConventive();
            bean.setDefinitionConventive(contenu + " @" + var.getCode() + "@");
        }
    }

    public void dropToContenu(DragDropEvent ddEvent) {
        if (article != null) {
            VariablePrint var = ((VariablePrint) ddEvent.getData());
            String contenu = article.getContenu();
            article.setContenu(contenu + " @" + var.getCode() + "@");
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
            result = result.replace("@" + variable.getCode() + "@", getValueTest(variable.getCode()));
        }
        return result;
    }
}

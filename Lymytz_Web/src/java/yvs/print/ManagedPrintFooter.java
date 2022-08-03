/*
 * To change this license footer, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsSocietesInfosSuppl;
import yvs.entity.print.YvsPrintFooter;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintFooter extends Managed<PrintFooter, YvsPrintFooter> implements Serializable {

    private PrintFooter bean = new PrintFooter();
    private YvsPrintFooter entity = new YvsPrintFooter();
    private List<YvsPrintFooter> list;

    private String model;

    public ManagedPrintFooter() {
        this.list = new ArrayList<>();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        update("blog-view_model_footer");
    }

    public PrintFooter getBean() {
        return bean;
    }

    public void setBean(PrintFooter bean) {
        this.bean = bean;
    }

    public YvsPrintFooter getEntity() {
        return entity;
    }

    public void setEntity(YvsPrintFooter entity) {
        this.entity = entity;
    }

    public List<YvsPrintFooter> getList() {
        return list;
    }

    public void setList(List<YvsPrintFooter> list) {
        this.list = list;
    }

    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintFooter.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_footer");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    public void loadAll(String model) {
        try {
            this.model = model;
            list = dao.loadNameQueries("YvsPrintFooter.findByModel", new String[]{"societe", "model"}, new Object[]{currentAgence.getSociete(), model});
            resetFiche();
            update("blog-print_footer");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    @Override
    public boolean controleFiche(PrintFooter bean) {
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
            YvsPrintFooter y = (YvsPrintFooter) dao.loadOneByNameQueries("YvsPrintFooter.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le nom " + bean.getNom());
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
            bean = new PrintFooter();
            entity = new YvsPrintFooter();
            update("form-print_footer");
            update("data-print_footer");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            bean.setModel(model);
            if (controleFiche(bean)) {
                entity = PrintFooter.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintFooter) dao.save1(entity);
                } else {
                    entity = (YvsPrintFooter) dao.update(entity);
                }
                int index = list.indexOf(entity);
                if (index > -1) {
                    list.set(index, entity);
                } else {
                    list.add(0, entity);
                }
                resetFiche();
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
    public void deleteBean(YvsPrintFooter y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_footer");
                }
            } else {
                entity = y;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("deleteBean", ex);
        }
    }

    @Override
    public void onSelectObject(YvsPrintFooter y) {
        try {
            bean = PrintFooter.buildBean(y);
            entity = y;
            update("form-print_footer");
            update("data-print_footer");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintFooter) ev.getObject());
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

    public String getValueSupplementaire(String type) {
        try {
            if (currentAgence != null ? currentAgence.getSociete() != null ? currentAgence.getSociete().getSupplementaires() != null : false : false) {
                for (YvsSocietesInfosSuppl s : currentAgence.getSociete().getSupplementaires()) {
                    if (s.getType().equals(type)) {
                        return s.getValeur();
                    }
                }
            }
        } catch (Exception ex) {
            getException("getValueSupplementaire", ex);
        }
        return "---";
    }

}

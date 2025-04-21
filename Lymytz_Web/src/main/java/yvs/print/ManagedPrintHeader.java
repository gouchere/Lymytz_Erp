/*
 * To change this license header, choose License Headers in Project Properties.
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
import yvs.entity.print.YvsPrintHeader;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintHeader extends Managed<PrintHeader, YvsPrintHeader> implements Serializable {

    private PrintHeader bean = new PrintHeader();
    private YvsPrintHeader entity = new YvsPrintHeader();
    private List<YvsPrintHeader> list;

    private String model;

    public ManagedPrintHeader() {
        this.list = new ArrayList<>();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
        update("blog-view_model_header");
    }

    public PrintHeader getBean() {
        return bean;
    }

    public void setBean(PrintHeader bean) {
        this.bean = bean;
    }

    public YvsPrintHeader getEntity() {
        return entity;
    }

    public void setEntity(YvsPrintHeader entity) {
        this.entity = entity;
    }

    public List<YvsPrintHeader> getList() {
        return list;
    }

    public void setList(List<YvsPrintHeader> list) {
        this.list = list;
    }

    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintHeader.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_header");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    public void loadAll(String model) {
        try {
            this.model = model;
            list = dao.loadNameQueries("YvsPrintHeader.findByModel", new String[]{"societe", "model"}, new Object[]{currentAgence.getSociete(), model});
            resetFiche();
            update("blog-print_header");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    @Override
    public boolean controleFiche(PrintHeader bean) {
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
            YvsPrintHeader y = (YvsPrintHeader) dao.loadOneByNameQueries("YvsPrintHeader.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
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
            bean = new PrintHeader();
            entity = new YvsPrintHeader();
            update("form-print_header");
            update("data-print_header");
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
                entity = PrintHeader.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintHeader) dao.save1(entity);
                } else {
                    entity = (YvsPrintHeader) dao.update(entity);
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
    public void deleteBean(YvsPrintHeader y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_header");
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
    public void onSelectObject(YvsPrintHeader y) {
        try {
            bean = PrintHeader.buildBean(y);
            entity = y;
            update("form-print_header");
            update("data-print_header");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintHeader) ev.getObject());
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

}

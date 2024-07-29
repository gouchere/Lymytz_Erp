/*
 * To change this license ticket_vente, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.print;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.print.YvsPrintTicketVente;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintTicketVente extends Managed<PrintTicketVente, YvsPrintTicketVente> implements Serializable {
    
    private PrintTicketVente bean = new PrintTicketVente();
    private YvsPrintTicketVente entity = new YvsPrintTicketVente();
    private List<YvsPrintTicketVente> list;
    
    private String model;
    
    public ManagedPrintTicketVente() {
        this.list = new ArrayList<>();
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
        update("blog-view_model_ticket_vente");
    }
    
    public PrintTicketVente getBean() {
        return bean;
    }
    
    public void setBean(PrintTicketVente bean) {
        this.bean = bean;
    }
    
    public YvsPrintTicketVente getEntity() {
        return entity;
    }
    
    public void setEntity(YvsPrintTicketVente entity) {
        this.entity = entity;
    }
    
    public List<YvsPrintTicketVente> getList() {
        return list;
    }
    
    public void setList(List<YvsPrintTicketVente> list) {
        this.list = list;
    }
    
    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintTicketVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_ticket_vente");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }
    
    public void loadAll(String model) {
        try {
            this.model = model;
            list = dao.loadNameQueries("YvsPrintTicketVente.findByModel", new String[]{"societe", "model"}, new Object[]{currentAgence.getSociete(), model});
            resetFiche();
            update("blog-print_ticket_vente");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }
    
    @Override
    public boolean controleFiche(PrintTicketVente bean) {
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
            YvsPrintTicketVente y = (YvsPrintTicketVente) dao.loadOneByNameQueries("YvsPrintTicketVente.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le nom " + bean.getNom());
                return false;
            }
            if (bean.isDefaut()) {
                y = (YvsPrintTicketVente) dao.loadOneByNameQueries("YvsPrintTicketVente.findByDefaut", new String[]{"defaut", "societe"}, new Object[]{true, currentAgence.getSociete()});
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
    
    @Override
    public void resetFiche() {
        try {
            bean = new PrintTicketVente();
            entity = new YvsPrintTicketVente();
            update("form-print_ticket_vente");
            update("data-print_ticket_vente");
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
                entity = PrintTicketVente.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintTicketVente) dao.save1(entity);
                } else {
                    entity = (YvsPrintTicketVente) dao.update(entity);
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
    public void deleteBean(YvsPrintTicketVente y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_ticket_vente");
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
    public void onSelectObject(YvsPrintTicketVente y) {
        try {
            bean = PrintTicketVente.buildBean(y);
            entity = y;
            update("form-print_ticket_vente");
            update("data-print_ticket_vente");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintTicketVente) ev.getObject());
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
    
    public void changeDefaut(YvsPrintTicketVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!y.getDefaut()) {
                    YvsPrintTicketVente r = (YvsPrintTicketVente) dao.loadOneByNameQueries("YvsPrintTicketVente.findByDefaut", new String[]{"defaut", "societe"}, new Object[]{true, currentAgence.getSociete()});
                    if (r != null ? r.getId() > 0 ? !r.getId().equals(bean.getId()) : false : false) {
                        getErrorMessage("Vous avez deja creer un model par défaut ");
                        return;
                    }
                }
                y.setDefaut(!y.getDefaut());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
                int index = list.indexOf(y);
                if (index > -1) {
                    list.set(index, y);
                }
                update("data-print_ticket_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeDefaut", ex);
        }
    }
    
}

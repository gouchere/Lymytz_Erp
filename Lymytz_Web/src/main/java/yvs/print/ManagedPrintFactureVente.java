/*
 * To change this license facture_vente, choose License Headers in Project Properties.
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
import yvs.entity.print.YvsPrintFactureVente;
import yvs.entity.print.YvsPrintFooter;
import yvs.entity.print.YvsPrintHeader;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedPrintFactureVente extends Managed<PrintFactureVente, YvsPrintFactureVente> implements Serializable {
    
    private PrintFactureVente bean = new PrintFactureVente();
    private YvsPrintFactureVente entity = new YvsPrintFactureVente();
    private List<YvsPrintFactureVente> list;
    
    private String model;
    
    public ManagedPrintFactureVente() {
        this.list = new ArrayList<>();
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
        update("blog-view_model_facture_vente");
    }
    
    public PrintFactureVente getBean() {
        return bean;
    }
    
    public void setBean(PrintFactureVente bean) {
        this.bean = bean;
    }
    
    public YvsPrintFactureVente getEntity() {
        return entity;
    }
    
    public void setEntity(YvsPrintFactureVente entity) {
        this.entity = entity;
    }
    
    public List<YvsPrintFactureVente> getList() {
        return list;
    }
    
    public void setList(List<YvsPrintFactureVente> list) {
        this.list = list;
    }
    
    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsPrintFactureVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-print_facture_vente");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }
    
    public void loadAll(String model) {
        try {
            this.model = model;
            list = dao.loadNameQueries("YvsPrintFactureVente.findByModel", new String[]{"societe", "model"}, new Object[]{currentAgence.getSociete(), model});
            resetFiche();
            update("blog-print_facture_vente");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }
    
    @Override
    public boolean controleFiche(PrintFactureVente bean) {
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
            if (bean.getHeader() != null ? bean.getHeader().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un entête");
                return false;
            }
            YvsPrintFactureVente y = (YvsPrintFactureVente) dao.loadOneByNameQueries("YvsPrintFactureVente.findByNom", new String[]{"nom", "societe"}, new Object[]{bean.getNom(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le nom " + bean.getNom());
                return false;
            }
            if (bean.isDefaut()) {
                y = (YvsPrintFactureVente) dao.loadOneByNameQueries("YvsPrintFactureVente.findByDefaut", new String[]{"defaut", "societe"}, new Object[]{true, currentAgence.getSociete()});
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
            bean = new PrintFactureVente();
            entity = new YvsPrintFactureVente();
            update("form-print_facture_vente");
            update("data-print_facture_vente");
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
                entity = PrintFactureVente.buildEntity(bean, currentAgence.getSociete(), currentUser);
                if (bean.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsPrintFactureVente) dao.save1(entity);
                } else {
                    entity = (YvsPrintFactureVente) dao.update(entity);
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
    public void deleteBean(YvsPrintFactureVente y, boolean delete) {
        try {
            if (delete) {
                if (y != null ? y.getId() > 0 : false) {
                    dao.delete(y);
                    if (y.getId().equals(bean.getId())) {
                        resetFiche();
                    }
                    list.remove(y);
                    update("data-print_facture_vente");
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
    public void onSelectObject(YvsPrintFactureVente y) {
        try {
            bean = PrintFactureVente.buildBean(y);
            entity = y;
            update("form-print_facture_vente");
            update("data-print_facture_vente");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsPrintFactureVente) ev.getObject());
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
    
    public void changeDefaut(YvsPrintFactureVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!y.getDefaut()) {
                    YvsPrintFactureVente r = (YvsPrintFactureVente) dao.loadOneByNameQueries("YvsPrintFactureVente.findByDefaut", new String[]{"defaut", "societe"}, new Object[]{true, currentAgence.getSociete()});
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
                update("data-print_facture_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("changeDefaut", ex);
        }
    }
    
    public void chooseHeader() {
        try {
            if (bean.getHeader() != null ? bean.getHeader().getId() > 0 : false) {
                ManagedPrintHeader w = (ManagedPrintHeader) giveManagedBean(ManagedPrintHeader.class);
                if (w != null) {
                    int index = w.getList().indexOf(new YvsPrintHeader(bean.getHeader().getId()));
                    if (index > -1) {
                        YvsPrintHeader y = w.getList().get(index);
                        w.onSelectObject(y);
                        bean.setHeader(PrintHeader.buildBean(y));
                    }
                }
            }
        } catch (Exception ex) {
            getException("chooseHeader", ex);
        }
    }
    
    public void chooseFooter() {
        try {
            if (bean.getFooter()!= null ? bean.getFooter().getId() > 0 : false) {
                ManagedPrintFooter w = (ManagedPrintFooter) giveManagedBean(ManagedPrintFooter.class);
                if (w != null) {
                    int index = w.getList().indexOf(new YvsPrintFooter(bean.getFooter().getId()));
                    if (index > -1) {
                        YvsPrintFooter y = w.getList().get(index);
                        w.onSelectObject(y);
                        bean.setFooter(PrintFooter.buildBean(y));
                    }
                }
            }
        } catch (Exception ex) {
            getException("chooseFooter", ex);
        }
    }
    
}

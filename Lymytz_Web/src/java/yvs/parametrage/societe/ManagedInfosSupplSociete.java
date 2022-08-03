/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.param.YvsSocietesInfosSuppl;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedInfosSupplSociete extends Managed<InfosSupplSociete, YvsSocietesInfosSuppl> implements Serializable {

    private InfosSupplSociete bean = new InfosSupplSociete();
    private YvsSocietesInfosSuppl entity = new YvsSocietesInfosSuppl();
    private List<YvsSocietesInfosSuppl> list;

    private List<String> types = new ArrayList<String>() {
        {
            add("Autre");
            add(Constantes.SUPPL_SOCIETE_AGREEMENT_NAME);
        }
    };

    public ManagedInfosSupplSociete() {
        this.list = new ArrayList<>();
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public InfosSupplSociete getBean() {
        return bean;
    }

    public void setBean(InfosSupplSociete bean) {
        this.bean = bean;
    }

    public YvsSocietesInfosSuppl getEntity() {
        return entity;
    }

    public void setEntity(YvsSocietesInfosSuppl entity) {
        this.entity = entity;
    }

    public List<YvsSocietesInfosSuppl> getList() {
        return list;
    }

    public void setList(List<YvsSocietesInfosSuppl> list) {
        this.list = list;
    }

    @Override
    public void loadAll() {
        try {
            list = dao.loadNameQueries("YvsSocietesInfosSuppl.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            update("data-infos_suppl");
        } catch (Exception ex) {
            getException("loadAll", ex);
        }
    }

    @Override
    public boolean controleFiche(InfosSupplSociete bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (bean.getTitre() != null ? bean.getTitre().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser le titre");
                return false;
            }
            if (bean.getValeur() != null ? bean.getValeur().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser la valeur");
                return false;
            }
            YvsSocietesInfosSuppl y = (YvsSocietesInfosSuppl) dao.loadOneByNameQueries("YvsSocietesInfosSuppl.findByType", new String[]{"type", "societe"}, new Object[]{bean.getType(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le type " + bean.getType());
                return false;
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("controleFiche", ex);
        }
        return false;
    }

    public boolean controleFiche(YvsSocietesInfosSuppl bean) {
        try {
            if (bean == null) {
                getErrorMessage("Action impossible!!!");
                return false;
            }
            if (bean.getTitre() != null ? bean.getTitre().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser le titre");
                return false;
            }
            bean.setType(getTypeByTitre(bean.getTitre()));
            if (bean.getValeur() != null ? bean.getValeur().trim().length() < 1 : true) {
                getErrorMessage("Vous devez preciser la valeur");
                return false;
            }
            YvsSocietesInfosSuppl y = (YvsSocietesInfosSuppl) dao.loadOneByNameQueries("YvsSocietesInfosSuppl.findByType", new String[]{"type", "societe"}, new Object[]{bean.getType(), currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
                getErrorMessage("Vous avez deja creer un model avec le type " + bean.getType());
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
            bean = new InfosSupplSociete();
            entity = new YvsSocietesInfosSuppl();
            update("form-infos_suppl");
            update("data-infos_suppl");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("resetFiche", ex);
        }
    }

    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(bean)) {
                entity = InfosSupplSociete.buildEntity(bean, currentAgence.getSociete(), currentUser);
                boolean correct = saveNew(entity);
                if (correct) {
                    resetFiche();
                }
                return correct;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("saveNew", ex);
        }
        return false;
    }

    public boolean saveNew(YvsSocietesInfosSuppl entity) {
        try {
            if (controleFiche(entity)) {
                entity.setAuthor(currentUser);
                entity.setSociete(currentAgence.getSociete());
                entity.setDateUpdate(new Date());
                if (entity.getId() < 1) {
                    entity.setId(null);
                    entity.setDateSave(new Date());
                    entity = (YvsSocietesInfosSuppl) dao.save1(entity);
                } else {
                    entity = (YvsSocietesInfosSuppl) dao.update(entity);
                }
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
    public void deleteBean(YvsSocietesInfosSuppl y, boolean delete) {
        try {
            if (delete) {
                if (y != null) {
                    if (y.getId() > 0) {
                        dao.delete(y);
                        if (y.getId().equals(bean.getId())) {
                            resetFiche();
                        }
                    }
                    list.remove(y);
                    update("data-infos_suppl");
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
    public void onSelectObject(YvsSocietesInfosSuppl y) {
        try {
            bean = InfosSupplSociete.buildBean(y);
            entity = y;
            update("form-infos_suppl");
            update("data-infos_suppl");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onSelectObject", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                onSelectObject((YvsSocietesInfosSuppl) ev.getObject());
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

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for (String s : types) {
            if (s.toLowerCase().startsWith(query.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public void onTitreSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String titre = (String) ev.getObject();
            bean.setType(getTypeByTitre(titre));
        }
    }

    public String getTypeByTitre(String titre) {
        switch (titre) {
            case Constantes.SUPPL_SOCIETE_AGREEMENT_NAME:
                return Constantes.SUPPL_SOCIETE_AGREEMENT;
        }
        return Constantes.AUTRE;
    }

    public void add() {
        if (!list.isEmpty()) {
            YvsSocietesInfosSuppl last = list.get(list.size() - 1);
            if (last.getId() < 1) {
                return;
            }
        }
        list.add(new YvsSocietesInfosSuppl(-1L));
        System.err.println("list " + list);
    }

}

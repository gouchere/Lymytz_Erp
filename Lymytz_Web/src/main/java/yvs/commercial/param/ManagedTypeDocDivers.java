/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Options;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedTypeDocDivers extends Managed<TypeDocDivers, YvsBaseTypeDocDivers> implements Serializable {

    private TypeDocDivers typeDoc = new TypeDocDivers();
    private List<YvsBaseTypeDocDivers> typesDocDivers, typesOd_all;
    private YvsBaseTypeDocDivers selectType = new YvsBaseTypeDocDivers();

    public ManagedTypeDocDivers() {
        typesDocDivers = new ArrayList<>();
        typesOd_all = new ArrayList<>();
    }

    public YvsBaseTypeDocDivers getSelectType() {
        return selectType;
    }

    public void setSelectType(YvsBaseTypeDocDivers selectType) {
        this.selectType = selectType;
    }

    public TypeDocDivers getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(TypeDocDivers typeDoc) {
        this.typeDoc = typeDoc;
    }

    public List<YvsBaseTypeDocDivers> getTypesDocDivers() {
        return typesDocDivers;
    }

    public void setTypesDocDivers(List<YvsBaseTypeDocDivers> typesDocDivers) {
        this.typesDocDivers = typesDocDivers;
    }

    public List<YvsBaseTypeDocDivers> getTypesOd_all() {
        return typesOd_all;
    }

    public void setTypesOd_all(List<YvsBaseTypeDocDivers> typesOd_all) {
        this.typesOd_all = typesOd_all;
    }

    @Override
    public boolean controleFiche(TypeDocDivers bean) {
        //le code doit être unique
        champ = new String[]{"societe", "libelle"};
        val = new Object[]{currentAgence.getSociete(), bean.getLibelle()};
        Long id = (Long) dao.loadObjectByNameQueries("YvsBaseTypeDocDivers.findByIdByCode", champ, val);
        if (id != null ? id > 0 : false) {
            if (bean.getId() > 0) { // si on est en modification
                if (bean.getId() != id) {
                    getErrorMessage("Un élément existe déjà avec ce code !");
                    return false;
                }
            } else {
                getErrorMessage("Un élément existe déjà avec ce code !");
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetFiche() {
        typeDoc = new TypeDocDivers();
        selectType = new YvsBaseTypeDocDivers();
        update("tabview_param_compta:form_edit_type_doc");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsBaseTypeDocDivers y = (YvsBaseTypeDocDivers) ev.getObject();
        onSelectOneObject(y);
    }

    private void onSelectOneObject(YvsBaseTypeDocDivers y) {
        if (y != null) {
            typeDoc = UtilCompta.buildBeanTypeDoc(y);
            update("tabview_param_compta:form_edit_type_doc");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        typeDoc = new TypeDocDivers();
    }

    @Override
    public void loadAll() {
        if (autoriser("compta_od_save_all_type")) {
            typesDocDivers = dao.loadNameQueries("YvsBaseTypeDocDivers.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            typesOd_all = typesDocDivers;
        } else {
            boolean autoriser = autoriser("compta_od_view_all_type");
            if (autoriser) {
                typesOd_all = dao.loadNameQueries("YvsBaseTypeDocDivers.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            loadAllByCodeAcces();
            if (!autoriser) {
                typesOd_all = typesDocDivers;
            }
        }

    }

    public void loadAll(String type) {
        if (autoriser("compta_od_save_all_type")) {
            typesDocDivers = dao.loadNameQueries("YvsBaseTypeDocDivers.findByModule", new String[]{"societe", "module"}, new Object[]{currentAgence.getSociete(), type});
            typesOd_all = typesDocDivers;
        } else {
            boolean autoriser = autoriser("compta_od_view_all_type");
            if (autoriser) {
                typesOd_all = dao.loadNameQueries("YvsBaseTypeDocDivers.findByModule", new String[]{"societe", "module"}, new Object[]{currentAgence.getSociete(), type});
            }
            List<Long> ids = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
            if (ids.isEmpty()) {
                ids.add(0L);
            }
            typesDocDivers = dao.loadNameQueries("YvsBaseTypeDocDivers.findByAccesModule", new String[]{"societe", "module", "ids"}, new Object[]{currentAgence.getSociete(), type, ids});
            if (!autoriser) {
                typesOd_all = typesDocDivers;
            }
        }

    }

    public void loadAllByCodeAcces() {
        //récupère les codes accès du users connecté
        List<Long> ids = dao.loadNameQueries("YvsBaseUsersAcces.findIdAccesByUsers", new String[]{"users", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
        if (ids.isEmpty()) {
            ids.add(0L);
        }
        typesDocDivers = dao.loadNameQueries("YvsBaseTypeDocDivers.findByAcces", new String[]{"societe", "ids"}, new Object[]{currentAgence.getSociete(), ids});
    }

    @Override
    public boolean saveNew() {
        if (typeDoc.getLibelle() != null ? !typeDoc.getLibelle().trim().isEmpty() : false) {
            if (controleFiche(typeDoc)) {
                YvsBaseTypeDocDivers y = UtilCompta.buildBeanTypeDoc(typeDoc, currentUser);
                if (typeDoc.getCodeAcces() != null ? typeDoc.getCodeAcces().getCode() != null ? typeDoc.getCodeAcces().getCode().trim().length() > 0 : false : false) {
                    y.setCodeAcces(returnCodeAcces(typeDoc.getCodeAcces().getCode()));
                } else {
                    y.setCodeAcces(null);
                }
                y.setSociete(currentAgence.getSociete());
                if (typeDoc.getId() <= 0) {
                    y.setId(null);
                    y = (YvsBaseTypeDocDivers) dao.save1(y);
                    typesDocDivers.add(0, y);
                } else {
                    dao.update(y);
                    int idx = typesDocDivers.indexOf(y);
                    if (idx >= 0) {
                        typesDocDivers.set(idx, y);
                    } else {
                        typesDocDivers.add(0, y);
                    }
                }
                resetFiche();
                update("tabview_param_compta:tables_type_doc");
                update("tabview_param_compta:form_edit_type_doc");
            }
        } else {
            getErrorMessage("Vous devez entrer le code identifiant du type");
            return false;
        }
        return true;
    }

    public void toogleActiveCaisse(YvsBaseTypeDocDivers bc) {
        if (bc != null) {
            bc.setActif(!bc.getActif());
            bc.setAuthor(currentUser);
            bc.setDateUpdate(new Date());
            dao.update(bc);
        }
    }

    public void openToDelete(YvsBaseTypeDocDivers y) {
        selectType = y;
        openDialog("dlgConfirmDelTypeDoc");
    }

    @Override
    public void deleteBean() {
        if (selectType != null ? selectType.getId() > 0 : false) {
            try {
                dao.delete(selectType);
                if (typesDocDivers.contains(selectType)) {
                    typesDocDivers.remove(selectType);
                }
                if (typeDoc.getId() == selectType.getId()) {
                    resetFiche();
                }
                update("tabview_param_compta:tables_type_doc");
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette caisse !");
            }
        }
    }

    private long id = -1000;

    public void buildCategorie(YvsBaseTypeDocDivers y) {
        selectType = y;
        selectType.getCategories().clear();
        YvsBaseTypeDocCategorie c;
        for (Options o : Constantes.CATEGORIES) {
            c = (YvsBaseTypeDocCategorie) dao.loadOneByNameQueries("YvsBaseTypeDocCategorie.findOne", new String[]{"typeDoc", "categorie"}, new Object[]{y, o.getValeur()});
            if (c != null ? c.getId() < 1 : true) {
                c = new YvsBaseTypeDocCategorie(id--);
                c.setTypeDoc(y);
                c.setCategorie(o.getValeur());
                c.setAuthor(currentUser);
            }
            selectType.getCategories().add(c);
        }
        update("data-categories_type_doc");
    }

    public void addCategorie(YvsBaseTypeDocCategorie y) {
        if (y != null) {
            long id = y.getId();
            if (id > 0) {
                dao.delete(y);
                y.setId(this.id--);
            } else {
                y.setId(null);
                y = (YvsBaseTypeDocCategorie) dao.save1(y);
            }
            int index = selectType.getCategories().indexOf(new YvsBaseTypeDocCategorie(id));
            if (index > -1) {
                selectType.getCategories().set(index, y);
            }
            update("data-categories_type_doc");
        }
    }
}

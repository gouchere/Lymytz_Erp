/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.production.UtilProd;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedClasseStat extends Managed<ClassesStat, YvsBaseClassesStat> implements Serializable {

    @ManagedProperty(value = "#{classesStat}")
    private ClassesStat classe;
    private List<YvsBaseClassesStat> classesStat;

    boolean init = true;

    public ManagedClasseStat() {
        classesStat = new ArrayList<>();
    }

    public ClassesStat getClasse() {
        return classe;
    }

    public void setClasse(ClassesStat classe) {
        this.classe = classe;
    }

    public List<YvsBaseClassesStat> getClassesStat() {
        return classesStat;
    }

    public void setClassesStat(List<YvsBaseClassesStat> classesStat) {
        this.classesStat = classesStat;
    }

    @Override
    public void loadAll() {
        loadAllClasStat(null);
    }

    @Override
    public boolean controleFiche(ClassesStat bean) {
        if (bean.getCodeRef() != null ? bean.getCodeRef().trim().isEmpty() : true) {
            getErrorMessage("Vous devez indiquer un code de référence");
            return false;
        }
        if (bean.getParent() > 0 ? bean.getId() == bean.getParent() : false) {
            getErrorMessage("Cette classe ne peut pas etre son propre parent");
            return false;
        }
        YvsBaseClassesStat y = (YvsBaseClassesStat) dao.loadOneByNameQueries("YvsBaseClassesStat.findByCodeRef", new String[]{"societe", "codeRef"}, new Object[]{currentAgence.getSociete(), bean.getCodeRef()});
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            getErrorMessage("Ce code est déjà associé à une autre classe");
            return false;
        }
        return true;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        onselectObject((YvsBaseClassesStat) ev.getObject());
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    @Override
    public ClassesStat recopieView() {
        return classe;
    }

    public void loadAllClasStat(Boolean actif) {
        loadAllClasStat(actif, true);
    }
    public void loadAllClasStat(Boolean actif, boolean count) {
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif, "=", "AND"));
        classesStat = paginator.executeDynamicQuery(count, "y", "y", "YvsBaseClassesStat y ", "y.codeRef", true, init, (int) 0, "id", dao);
    }

    @Override
    public void resetFiche() {
        resetFiche(classe);
        update("form_param_classe_stat");
        update("tabview_param_base:form_param_classe_stat");
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(classe)) {
            YvsBaseClassesStat entity = UtilProd.buildClasseStat(classe);
            if (classe.getParent() > 0) {
                int index = classesStat.indexOf(new YvsBaseClassesStat(classe.getParent()));
                if (index > -1) {
                    entity.setParent(classesStat.get(index));
                }
            }
            entity.setSociete(currentAgence.getSociete());
            entity.setAuthor(currentUser);
            if (classe.getId() <= 0) {
                entity.setId(null);
                entity = (YvsBaseClassesStat) dao.save1(entity);
            } else {
                dao.update(entity);
            }
            int idx = classesStat.indexOf(entity);
            if (idx >= 0) {
                classesStat.set(idx, entity);
            } else {
                classesStat.add(0, entity);
            }
            succes();
            actionOpenOrResetAfter(this);
        }
        update("tabview_param_base:table_param_classes");
        update("tabview_param_base:select_parent_classe_stat");
        return true;
    }

    public void onselectObject(YvsBaseClassesStat bean) {
        if (bean != null) {
            cloneObject(classe, UtilProd.buildBeanClasseStat(bean));
            update("tabview_param_base:form_param_classe_stat");
        }
    }

    public void deleteLine(YvsBaseClassesStat bean, boolean all) {
        try {
            if (bean != null) {
                dao.delete(bean);
                classesStat.remove(bean);
                if (bean.getId().equals(classe.getId())) {
                    resetFiche();
                    update("tabview_param_base:form_param_classe_stat");
                }
                if (!all) {
                    update("tabview_param_base:table_param_classes");
                    update("tabview_param_base:select_parent_classe_stat");
                    succes();
                }
            }
        } catch (Exception ex) {
            getException(getClass().getSimpleName() + " (deleteLine)", ex);
            if (!all) {
                getErrorMessage("Action Impossible");
            }
        }
    }

    public void deleteLineAll() {
        for (YvsBaseClassesStat c : classesStat) {
            deleteLine(c, true);
        }
        update("tabview_param_base:table_param_classes");
        update("tabview_param_base:select_parent_classe_stat");
        succes();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.users.UtilUsers;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedCodeAcces extends Managed<CodeAcces, YvsBaseCodeAcces> implements Serializable {
    
    private CodeAcces codeAcces = new CodeAcces();
    private YvsBaseCodeAcces entity;
    private List<YvsBaseCodeAcces> codesAcces;
    
    private String tabIds;
    
    public ManagedCodeAcces() {
        codesAcces = new ArrayList<>();
    }
    
    public CodeAcces getCodeAcces() {
        return codeAcces;
    }
    
    public void setCodeAcces(CodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }
    
    public YvsBaseCodeAcces getEntity() {
        return entity;
    }
    
    public void setEntity(YvsBaseCodeAcces entity) {
        this.entity = entity;
    }
    
    public List<YvsBaseCodeAcces> getCodesAcces() {
        return codesAcces;
    }
    
    public void setCodesAcces(List<YvsBaseCodeAcces> codesAcces) {
        this.codesAcces = codesAcces;
    }
    
    public String getTabIds() {
        return tabIds;
    }
    
    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }
    
    @Override
    public void loadAll() {
        codesAcces = dao.loadNameQueries("YvsBaseCodeAcces.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }
    
    @Override
    public boolean controleFiche(CodeAcces bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getCode() != null ? bean.getCode().trim().length() < 1 : true) {
            getErrorMessage("Vous devez entrer le code");
            return false;
        }
        return true;
    }
    
    @Override
    public void resetFiche() {
        codeAcces = new CodeAcces();
        update("form_code_acces");
    }
    
    @Override
    public boolean saveNew() {
        try {
            if (controleFiche(codeAcces)) {
                entity = UtilUsers.buildCodeAcces(codeAcces, currentAgence.getSociete(), currentUser);
                if (codeAcces.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsBaseCodeAcces) dao.save1(entity);
                } else {
                    dao.update(entity);
                }
                int idx = codesAcces.indexOf(entity);
                if (idx > -1) {
                    codesAcces.set(idx, entity);
                } else {
                    codesAcces.add(0, entity);
                }
                update("data_code_acces");
                resetFiche();
                succes();
            }
        } catch (Exception ex) {
        }
        return false;
    }
    
    @Override
    public void deleteBean() {
        try {
            
        } catch (Exception ex) {
        }
    }

    public void deleteBean(YvsBaseCodeAcces y) {
        try {
            if (y != null) {
                dao.delete(y);
                codesAcces.remove(y);
                update("date_code_acces");
                succes();
            }
        } catch (Exception ex) {
        }
    }
    
    @Override
    public void onSelectObject(YvsBaseCodeAcces y) {
        codeAcces = UtilUsers.buildBeanCodeAcces(y);
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsBaseCodeAcces) ev.getObject());
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }
    
}

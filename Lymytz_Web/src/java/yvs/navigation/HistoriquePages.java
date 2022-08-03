/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.navigation;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz
 */
@ManagedBean
@SessionScoped
public class HistoriquePages implements Serializable, Comparator<HistoriquePages> {

    private int id;
    private String content;
    private boolean actif, actif2;
    private String titlePage, module, page;
    private String codeModule, codePage;

    public HistoriquePages() {

    }

    public HistoriquePages(int id, String content, boolean actif, boolean actif2) {
        this.id = id;
        this.content = content;
        this.actif = actif;
        this.actif2 = actif2;
    }

    public HistoriquePages(boolean actif, boolean actif2) {
        this.actif = actif;
        this.actif2 = actif2;
    }

    public boolean isActif2() {
        return actif2;
    }

    public void setActif2(boolean actif2) {
        this.actif2 = actif2;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public HistoriquePages(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getCodeModule() {
        return codeModule;
    }

    public void setCodeModule(String codeModule) {
        this.codeModule = codeModule;
    }

    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HistoriquePages other = (HistoriquePages) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(HistoriquePages o1, HistoriquePages o2) {
        return ((o1.getId() - o2.getId()) >= 0) ? 1 : (-1);
    }

}

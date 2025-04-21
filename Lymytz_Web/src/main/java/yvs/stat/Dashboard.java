/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.util.Objects;
import yvs.dao.Util;

/**
 *
 * @author Lymytz Dowes
 */
public class Dashboard {

    private String code;
    private String page;
    private String groupe;
    private String options;
    private boolean acces ;

    public Dashboard(String code) {
        this.code = code;
    }

    public Dashboard(String code, String page, String options, boolean acces) {
        this(code);
        this.page = page;
        this.options = options;
        this.acces = acces;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getGroupe() {
        return Util.asString(groupe) ? groupe : "";
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isAcces() {
        return acces;
    }

    public void setAcces(boolean acces) {
        this.acces = acces;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.code);
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
        final Dashboard other = (Dashboard) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }
}

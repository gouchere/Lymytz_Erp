/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "themeService", eager = true)
@SessionScoped
public class ThemeService implements Serializable{

    private List<Theme> themes;

    public ThemeService() {
        themes = new ArrayList<>();
    }

    public ThemeService(List<Theme> themes) {
        this.themes = themes;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }
}

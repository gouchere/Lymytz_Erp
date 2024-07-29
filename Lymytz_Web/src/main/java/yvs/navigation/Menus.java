/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.navigation;

import java.io.Serializable;
/**
 *
 * @author GOUCHERE YVES
 */
public class Menus implements Serializable{

    private String module;
    private String menu;
    private String page;

    public Menus() {
    }

    public Menus(String module, String menu, String page) {
        this.module = module;
        this.menu = menu;
        this.page = page;
    }

    public String getMenu() {
        return menu;
    }

    public String getModule() {
        return module;
    }

    public String getPage() {
        return page;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author dowes
 */
@ManagedBean
@SessionScoped
public class ReturnImport implements Serializable {

    private List<String> titles;
    private List<String[]> lignes;
    private List<ImportData.TableOrder> tables;
    private List<ImportData> datas;

    public ReturnImport() {
        titles = new ArrayList<>();
        tables = new ArrayList<>();
        lignes = new ArrayList<>();
        datas = new ArrayList<>();
    }

    public ReturnImport(List<String> titles) {
        this();
        this.titles = titles != null ? new ArrayList<>(titles) : new ArrayList<String>();
    }

    public List<ImportData.TableOrder> getTables() {
        return tables;
    }

    public void setTables(List<ImportData.TableOrder> tables) {
        this.tables = tables;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String[]> getLignes() {
        return lignes;
    }

    public void setLignes(List<String[]> lignes) {
        this.lignes = lignes;
    }

    public List<ImportData> getDatas() {
        return datas;
    }

    public void setDatas(List<ImportData> datas) {
        this.datas = datas;
    }
}

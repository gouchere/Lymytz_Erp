/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats.excell;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DOWES
 */
public class Row {

    private int position;
    private List<Column> columns;

    public Row(int position) {
        this.position = position;
        this.columns = new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public int size() {
        return this.columns != null ? this.columns.size() : 0;
    }

    public Object getValue(String... names) throws Exception{
        for (String name : names) {
            int index = columns.indexOf(new Column(name));
            if (index > -1) {
                return columns.get(index).getValue();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.position;
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
        final Row other = (Row) obj;
        if (this.position != other.position) {
            return false;
        }
        return true;
    }

}

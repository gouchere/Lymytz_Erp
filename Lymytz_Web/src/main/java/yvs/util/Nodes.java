/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author LYMYTZ-PC
 */
public class Nodes extends DefaultTreeNode {

    private long id;
    private long idParent;

    public Nodes() {
    }

    public Nodes(Object data) {
        super(data, null);
    }

    public Nodes(long id, Object data) {
        super(data, null);
        this.id = id;
    }

    public Nodes(Object data, TreeNode parent) {
        super(data, parent);
    }

    public Nodes(long id, Object data, TreeNode parent) {
        super(data, parent);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdParent() {
        return idParent;
    }

    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Nodes other = (Nodes) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

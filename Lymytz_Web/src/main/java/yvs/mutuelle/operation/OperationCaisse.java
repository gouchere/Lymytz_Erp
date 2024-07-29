/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.base.Tiers;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class OperationCaisse extends Operation implements Serializable {

    private CaisseMutuelle caisse = new CaisseMutuelle();
    private Tiers source = new Tiers();
    private long idExterne;
    private String tableExterne;
    private boolean abonner = true;
    private boolean inSoldeMutuelle = true; //Indique si l'objet participe au solde de la caisse à redistribuer

    public OperationCaisse() {
    }

    public OperationCaisse(long id) {
        super(id);
    }

    @Override
    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    @Override
    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    public Tiers getSource() {
        return source;
    }

    public void setSource(Tiers source) {
        this.source = source;
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public boolean isAbonner() {
        return abonner;
    }

    public void setAbonner(boolean abonner) {
        this.abonner = abonner;
    }

    public boolean isInSoldeMutuelle() {
        return inSoldeMutuelle;
    }

    public void setInSoldeMutuelle(boolean inSoldeMutuelle) {
        this.inSoldeMutuelle = inSoldeMutuelle;
    }

}

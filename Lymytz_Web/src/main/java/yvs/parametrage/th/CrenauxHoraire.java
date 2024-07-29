/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.BeanDeBase;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.param.YvsCrenauxHoraire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean(name = "th")
@SessionScoped
public class CrenauxHoraire extends BeanDeBase implements Serializable {

    private long id;
    private String libelle;
    private String codeTranche;
    private String typeDeJournee;
    private int ordre;
    private Date heureDeb;
    private Date heureFin;

    public CrenauxHoraire() {
    }

    public CrenauxHoraire(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CrenauxHoraire other = (CrenauxHoraire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public Date getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(Date heureDeb) {
        this.heureDeb = heureDeb;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public String getTypeDeJournee() {
        return typeDeJournee;
    }

    public void setTypeDeJournee(String typeDeJournee) {
        this.typeDeJournee = typeDeJournee;
    }

    public String getCodeTranche() {
        return codeTranche;
    }

    public void setCodeTranche(String codeTranche) {
        this.codeTranche = codeTranche;
    }

    public List<CrenauxHoraire> loadCrenaux(YvsBaseDepots dep) {
        List<CrenauxHoraire> l = new ArrayList<>();
//        for (YvsDepotCrenaux dc : dep.getYvsCrenauxHoraireList()) {
//            l.add(buidCrenaux(dc.getIdCrenaux()));
//        }
        return l;
    }

    public CrenauxHoraire buidCrenaux(YvsCrenauxHoraire c) {
        CrenauxHoraire ch = null;
        if (c != null) {
            ch = new CrenauxHoraire();
            ch.setId(c.getId());
            ch.setCodeTranche(c.getCodeTranche());
            ch.setHeureDeb(c.getHeureDeb());
            ch.setHeureFin(c.getHeureFin());
        }
        return ch;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.personnel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_diplome_employe")
@NamedQueries({
    @NamedQuery(name = "YvsDiplomeEmploye.findAll", query = "SELECT y FROM YvsDiplomeEmploye y"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByDiplome", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.yvsDiplomeEmployePK.diplome = :diplome"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByEmploye", query = "SELECT y FROM YvsDiplomeEmploye y JOIN FETCH y.diplome WHERE y.yvsDiplomeEmployePK.employe = :employe"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByDateObtention", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.dateObtention = :dateObtention"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByEcole", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.ecole = :ecole"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByClassement", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.classement = :classement"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByMention", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.mention = :mention"),
    @NamedQuery(name = "YvsDiplomeEmploye.findBySupp", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByActif", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByDateUpdate", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsDiplomeEmploye.findByDateSave", query = "SELECT y FROM YvsDiplomeEmploye y WHERE y.dateSave = :dateSave")})
public class YvsDiplomeEmploye implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsDiplomeEmployePK yvsDiplomeEmployePK;
    @Column(name = "date_obtention")
    @Temporal(TemporalType.DATE)
    private Date dateObtention;
    @Size(max = 2147483647)
    @Column(name = "ecole")
    private String ecole;
    @Column(name = "classement")
    private Short classement;
    @Size(max = 2147483647)
    @Column(name = "mention")
    private String mention;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "employe", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "diplome", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDiplomes diplome;

    public YvsDiplomeEmploye() {
    }

    public YvsDiplomeEmploye(YvsDiplomeEmployePK yvsDiplomeEmployePK) {
        this.yvsDiplomeEmployePK = yvsDiplomeEmployePK;
    }

    public YvsDiplomeEmploye(int diplome, int employe) {
        this.yvsDiplomeEmployePK = new YvsDiplomeEmployePK(diplome, employe);
    }

    public YvsDiplomeEmployePK getYvsDiplomeEmployePK() {
        return yvsDiplomeEmployePK;
    }

    public void setYvsDiplomeEmployePK(YvsDiplomeEmployePK yvsDiplomeEmployePK) {
        this.yvsDiplomeEmployePK = yvsDiplomeEmployePK;
    }

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
    }

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public Short getClassement() {
        return classement;
    }

    public void setClassement(Short classement) {
        this.classement = classement;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsDiplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(YvsDiplomes diplome) {
        this.diplome = diplome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsDiplomeEmployePK != null ? yvsDiplomeEmployePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsDiplomeEmploye)) {
            return false;
        }
        YvsDiplomeEmploye other = (YvsDiplomeEmploye) object;
        if ((this.yvsDiplomeEmployePK == null && other.yvsDiplomeEmployePK != null) || (this.yvsDiplomeEmployePK != null && !this.yvsDiplomeEmployePK.equals(other.yvsDiplomeEmployePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsDiplomeEmploye[ yvsDiplomeEmployePK=" + yvsDiplomeEmployePK + " ]";
    }
    
}

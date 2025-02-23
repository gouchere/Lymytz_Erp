/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_compte_bancaire")
@NamedQueries({
    @NamedQuery(name = "YvsCompteBancaire.findAll", query = "SELECT y FROM YvsCompteBancaire y"),
    @NamedQuery(name = "YvsCompteBancaire.findById", query = "SELECT y FROM YvsCompteBancaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsCompteBancaire.findByNumeroCompte", query = "SELECT y FROM YvsCompteBancaire y WHERE y.numeroCompte = :numeroCompte"),
    @NamedQuery(name = "YvsCompteBancaire.findByEmploye", query = "SELECT y FROM YvsCompteBancaire y JOIN FETCH y.banque WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsCompteBancaire.findByBanque", query = "SELECT y FROM YvsCompteBancaire y WHERE y.banque = :banque")})
public class YvsCompteBancaire implements Serializable {

    @Id
    @SequenceGenerator(sequenceName = "yvs_compte_bancaire_id_seq", name = "yvs_compte_bancaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compte_bancaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "code_banque")
    private String codeBanque;
    @Size(max = 2147483647)
    @Column(name = "cle_compte")
    private String cleCompte;
    private static final long serialVersionUID = 1L;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "numero_compte")
    private String numeroCompte;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "banque", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBanques banque;

    public YvsCompteBancaire() {
    }

    public YvsCompteBancaire(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsCompteBancaire)) {
            return false;
        }
        YvsCompteBancaire other = (YvsCompteBancaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsCompteBancaire[ id=" + id + " ]";
    }

    public String getCodeBanque() {
        return codeBanque;
    }

    public void setCodeBanque(String codeBanque) {
        this.codeBanque = codeBanque;
    }

    public String getCleCompte() {
        return cleCompte;
    }

    public void setCleCompte(String cleCompte) {
        this.cleCompte = cleCompte;
    }

    public YvsBanques getBanque() {
        return banque;
    }

    public void setBanque(YvsBanques banque) {
        this.banque = banque;
    }

}

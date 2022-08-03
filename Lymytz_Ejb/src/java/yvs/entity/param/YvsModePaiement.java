/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_mode_paiement")
@NamedQueries({
    @NamedQuery(name = "YvsModePaiement.findAll", query = "SELECT y FROM YvsModePaiement y"),
    @NamedQuery(name = "YvsModePaiement.findById", query = "SELECT y FROM YvsModePaiement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsModePaiement.findByTypePaiement", query = "SELECT y FROM YvsModePaiement y WHERE y.typePaiement = :typePaiement"),
    @NamedQuery(name = "YvsModePaiement.findBySupp", query = "SELECT y FROM YvsModePaiement y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsModePaiement.findByActif", query = "SELECT y FROM YvsModePaiement y WHERE y.actif = :actif")})
public class YvsModePaiement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mode_paiement_id_seq", name = "yvs_mode_paiement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mode_paiement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "type_paiement")
    private String typePaiement;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsModePaiement() {
    }

    public YvsModePaiement(Long id) {
        this.id = id;
    }

    public YvsModePaiement(Long id, String typeP) {
        this.id = id;
        this.typePaiement = typeP;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsModePaiement)) {
            return false;
        }
        YvsModePaiement other = (YvsModePaiement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsModePaiement[ id=" + id + " ]";
    }

    }

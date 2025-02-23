/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_mut_financement_activite", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsMutFinancementActivite.findAll", query = "SELECT y FROM YvsMutFinancementActivite y"),
    @NamedQuery(name = "YvsMutFinancementActivite.findById", query = "SELECT y FROM YvsMutFinancementActivite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutFinancementActivite.findByMontantRequis", query = "SELECT y FROM YvsMutFinancementActivite y WHERE y.montantRecu = :montantRecu"),
    @NamedQuery(name = "YvsMutFinancementActivite.findByDateFinancement", query = "SELECT y FROM YvsMutFinancementActivite y WHERE y.dateFinancement = :dateFinancement"),

    @NamedQuery(name = "YvsMutFinancementActivite.findByActivite", query = "SELECT y FROM YvsMutFinancementActivite y WHERE y.activite = :activite"),
    @NamedQuery(name = "YvsMutFinancementActivite.findFinancement", query = "SELECT SUM(y.montantRecu) FROM YvsMutFinancementActivite y WHERE y.activite.evenement = :evenement"),
    @NamedQuery(name = "YvsMutFinancementActivite.findByEvenement", query = "SELECT y FROM YvsMutFinancementActivite y WHERE y.activite.evenement = :evenement")})
public class YvsMutFinancementActivite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_financement_activite_id_seq", name = "yvs_mut_financement_activite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_financement_activite_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_recu")
    private Double montantRecu;
    @Column(name = "date_financement")
    @Temporal(TemporalType.DATE)
    private Date dateFinancement;
    @JoinColumn(name = "activite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutActivite activite;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;

    public YvsMutFinancementActivite() {
    }

    public YvsMutFinancementActivite(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Double getMontantRecu() {
        return montantRecu != null ? montantRecu : 0;
    }

    public void setMontantRecu(Double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public Date getDateFinancement() {
        return dateFinancement != null ? dateFinancement : new Date();
    }

    public void setDateFinancement(Date dateFinancement) {
        this.dateFinancement = dateFinancement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutActivite getActivite() {
        return activite;
    }

    public void setActivite(YvsMutActivite activite) {
        this.activite = activite;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
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
        if (!(object instanceof YvsMutFinancementActivite)) {
            return false;
        }
        YvsMutFinancementActivite other = (YvsMutFinancementActivite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutFinancementActivite[ id=" + id + " ]";
    }

}

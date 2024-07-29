/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.operation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_mut_abonnement_depense")
@NamedQueries({
    @NamedQuery(name = "YvsMutAbonnementDepense.findAll", query = "SELECT y FROM YvsMutAbonnementDepense y"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findById", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findByMouvement", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.mouvementCaisse = :mouvement"),
    @NamedQuery(name = "YvsMutAbonnementDepense.countByMouvement", query = "SELECT COUNT(y) FROM YvsMutAbonnementDepense y WHERE y.mouvementCaisse = :mouvement"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findByAuthor", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.author = :author"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findByDateSave", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findByDateUpdate", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsMutAbonnementDepense.findByTypeVal", query = "SELECT y FROM YvsMutAbonnementDepense y WHERE y.typeVal = :typeVal")})
public class YvsMutAbonnementDepense implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_abonnement_depense_id_seq", name = "yvs_mut_abonnement_depense_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_abonnement_depense_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @Column(name = "date_retrait")
    @Temporal(TemporalType.DATE)
    private Date dateRetrait;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "type_val")
    private String typeVal;
    @JoinColumn(name = "mouvement_caisse", referencedColumnName = "id")
    @ManyToOne
    private YvsMutMouvementCaisse mouvementCaisse;

    public YvsMutAbonnementDepense() {
    }

    public YvsMutAbonnementDepense(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public YvsMutMouvementCaisse getMouvementCaisse() {
        return mouvementCaisse;
    }

    public void setMouvementCaisse(YvsMutMouvementCaisse mouvementCaisse) {
        this.mouvementCaisse = mouvementCaisse;
    }

    public Date getDateRetrait() {
        return dateRetrait;
    }

    public void setDateRetrait(Date dateRetrait) {
        this.dateRetrait = dateRetrait;
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
        if (!(object instanceof YvsMutAbonnementDepense)) {
            return false;
        }
        YvsMutAbonnementDepense other = (YvsMutAbonnementDepense) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.operation.YvsMutAbonnementDepense[ id=" + id + " ]";
    }

}

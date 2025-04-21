/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.compta.analytique.YvsComptaCentreCoutVirement;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_cout_sup_piece_virement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCoutSupPieceVirement.findAll", query = "SELECT y FROM YvsComptaCoutSupPieceVirement y"),
    @NamedQuery(name = "YvsComptaCoutSupPieceVirement.findById", query = "SELECT y FROM YvsComptaCoutSupPieceVirement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCoutSupPieceVirement.findByMontant", query = "SELECT y FROM YvsComptaCoutSupPieceVirement y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCoutSupPieceVirement.findByDateUpdate", query = "SELECT y FROM YvsComptaCoutSupPieceVirement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCoutSupPieceVirement.findByDateSave", query = "SELECT y FROM YvsComptaCoutSupPieceVirement y WHERE y.dateSave = :dateSave")})
public class YvsComptaCoutSupPieceVirement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_cout_sup_piece_virement_id_seq", name = "yvs_compta_cout_sup_piece_virement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_cout_sup_piece_virement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhTypeCout typeCout;
    @JoinColumn(name = "virement", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceVirement virement;

    @OneToMany(mappedBy = "cout")
    private List<YvsComptaCentreCoutVirement> analytiques;

    @Transient
    private boolean new_;

    public YvsComptaCoutSupPieceVirement() {
        analytiques = new ArrayList<>();
    }

    public YvsComptaCoutSupPieceVirement(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public YvsComptaCaissePieceVirement getVirement() {
        return virement;
    }

    public void setVirement(YvsComptaCaissePieceVirement virement) {
        this.virement = virement;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public List<YvsComptaCentreCoutVirement> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaCentreCoutVirement> analytiques) {
        this.analytiques = analytiques;
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
        if (!(object instanceof YvsComptaCoutSupPieceVirement)) {
            return false;
        }
        YvsComptaCoutSupPieceVirement other = (YvsComptaCoutSupPieceVirement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCoutSupPieceVirement[ id=" + id + " ]";
    }

}

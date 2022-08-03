/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.operation;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_mut_mouvement_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsMutMouvementCaisse.findAll", query = "SELECT y FROM YvsMutMouvementCaisse y"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findById", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByNumero", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.numero = :numero"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByIdExterne", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.idExterne = :idExterne"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByNote", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.note = :note"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByTableExterne", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.tableExterne = :tableExterne"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByMontant", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByReferenceExterne", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.referenceExterne = :referenceExterne"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByDateMvt", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.dateMvt = :dateMvt"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByStatutPiece", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.statutPiece = :statutPiece"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByMouvement", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsMutMouvementCaisse.findByNameTiers", query = "SELECT y FROM YvsMutMouvementCaisse y WHERE y.nameTiers = :nameTiers")})
public class YvsMutMouvementCaisse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_mouvement_caisse_id_seq", name = "yvs_mut_mouvement_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_mouvement_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "numero")
    private String numero;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @Size(min = 1, max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "date_mvt")
    @Temporal(TemporalType.DATE)
    private Date dateMvt;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Size(max = 2147483647)
    @Column(name = "mouvement")
    private String mouvement;
    @Size(max = 2147483647)
    @Column(name = "name_tiers")
    private String nameTiers;
    @Column(name = "in_solde_caisse")
    private Boolean inSoldeCaisse;  //Indique si l'objet participe au solde de la caisse à redistribuer
    @Column(name = "id_externe")
    private Long idExterne;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "tiers_externe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTiers tiersExterne;

    /**
     * Le tiers interne est en générale un mutualiste
     */
    @JoinColumn(name = "tiers_interne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste tiersInterne;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;

    @Transient
    private boolean new_;

    public YvsMutMouvementCaisse() {
    }

    public YvsMutMouvementCaisse(Long id) {
        this.id = id;
    }

    public YvsMutMouvementCaisse(Long id, long idExterne, String tableExterne) {
        this.id = id;
        this.idExterne = idExterne;
        this.tableExterne = tableExterne;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getIdExterne() {
        return idExterne != null ? idExterne : -1L;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Date getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(Date dateMvt) {
        this.dateMvt = dateMvt;
    }

    public Character getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public String getMouvement() {
        return mouvement;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public String getNameTiers() {
        return nameTiers;
    }

    public void setNameTiers(String nameTiers) {
        this.nameTiers = nameTiers;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsMutTiers getTiersExterne() {
        return tiersExterne;
    }

    public void setTiersExterne(YvsMutTiers tiersExterne) {
        this.tiersExterne = tiersExterne;
    }

    public YvsMutMutualiste getTiersInterne() {
        return tiersInterne;
    }

    public void setTiersInterne(YvsMutMutualiste tiersInterne) {
        this.tiersInterne = tiersInterne;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Boolean getInSoldeCaisse() {
        return inSoldeCaisse != null ? inSoldeCaisse : false;
    }

    public void setInSoldeCaisse(Boolean inSoldeCaisse) {
        this.inSoldeCaisse = inSoldeCaisse;
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
        if (!(object instanceof YvsMutMouvementCaisse)) {
            return false;
        }
        YvsMutMouvementCaisse other = (YvsMutMouvementCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.operation.YvsMutMouvementCaisse[ id=" + id + " ]";
    }

}

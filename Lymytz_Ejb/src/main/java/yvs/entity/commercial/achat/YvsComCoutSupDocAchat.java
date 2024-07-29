/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_cout_sup_doc_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComCoutSupDocAchat.findAll", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.docAchat.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findById", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findByMontant", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findBySupp", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findByActif", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findByDocAchat", query = "SELECT y FROM YvsComCoutSupDocAchat y WHERE y.docAchat = :docAchat"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findSumByDocAchat", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocAchat y WHERE y.docAchat = :docAchat AND y.typeCout.augmentation = :sens"),
    
    @NamedQuery(name = "YvsComCoutSupDocAchat.findSumByFournisseurDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocAchat y WHERE y.docAchat.agence = :agence AND y.docAchat.fournisseur = :fournisseur AND y.typeCout.augmentation = :sens AND y.docAchat.dateDoc BETWEEN :dateDebut AND :dateFin AND y.docAchat.statut = 'V'"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findSumByFournisseurDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocAchat y WHERE y.docAchat.fournisseur = :fournisseur AND y.typeCout.augmentation = :sens AND y.docAchat.dateDoc BETWEEN :dateDebut AND :dateFin AND y.docAchat.statut = 'V'"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findSumByFournisseurDate", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocAchat y WHERE y.docAchat.fournisseur = :fournisseur AND y.typeCout.augmentation = :sens AND y.docAchat.dateDoc <= :dateFin AND y.docAchat.statut = 'V'"),
    @NamedQuery(name = "YvsComCoutSupDocAchat.findSumByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocAchat y WHERE y.docAchat.fournisseur = :docAchat AND y.typeCout.augmentation = :sens")})
public class YvsComCoutSupDocAchat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_cout_sup_doc_achat_id_seq", name = "yvs_com_cout_sup_doc_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_cout_sup_doc_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "montant")
    private double montant;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JsonManagedReference
    @JoinColumn(name = "doc_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats docAchat;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    
    @Transient
    private long idDistant;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComCoutSupDocAchat() {
    }

    public YvsComCoutSupDocAchat(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
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

    public YvsComDocAchats getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(YvsComDocAchats docAchat) {
        this.docAchat = docAchat;
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
        if (!(object instanceof YvsComCoutSupDocAchat)) {
            return false;
        }
        YvsComCoutSupDocAchat other = (YvsComCoutSupDocAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComCoutSupDocAchat[ id=" + id + " ]";
    }

}

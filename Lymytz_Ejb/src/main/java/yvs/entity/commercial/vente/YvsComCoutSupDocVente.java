/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

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
@Table(name = "yvs_com_cout_sup_doc_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComCoutSupDocVente.findAll", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.docVente.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findById", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findByMontant", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findBySupp", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findByActif", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findByDocVente", query = "SELECT y FROM YvsComCoutSupDocVente y WHERE y.docVente = :docVente"),
    @NamedQuery(name = "YvsComCoutSupDocVente.countByDocVente", query = "SELECT COUNT(y) FROM YvsComCoutSupDocVente y WHERE y.docVente = :docVente"),
    
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByDocVente", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente = :docVente AND y.typeCout.augmentation = :sens"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByClient", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.docVente.statut = 'V'"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByClientDate", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete <= :dateFin"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByClientDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByClient", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.service = :service AND y.docVente.statut = 'V'"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByClientDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.service = :service AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByClientDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.enteteDoc.agence = :agence AND y.docVente.client = :client AND y.typeCout.augmentation = :sens AND y.service = :service AND y.docVente.statut = 'V' AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByFactures", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.service = :service AND y.typeCout.augmentation = :sens AND y.docVente.id IN :docVente"),    
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByDocVente", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.service = :service AND y.typeCout.augmentation = :sens AND y.docVente = :docVente"),    
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.enteteDoc.creneau.users.agence.societe = :societe AND y.docVente.typeDoc = :type AND y.service = :service AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeCout.augmentation = :sens"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumServiceByAgenceDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.enteteDoc.creneau.users.agence = :agence AND y.docVente.typeDoc = :type AND y.service = :service AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeCout.augmentation = :sens"),
    
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.enteteDoc.creneau.users.agence.societe = :societe AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeCout.augmentation = :sens"),
    @NamedQuery(name = "YvsComCoutSupDocVente.findSumByAgenceDates", query = "SELECT SUM(y.montant) FROM YvsComCoutSupDocVente y WHERE y.docVente.enteteDoc.creneau.users.agence = :agence AND y.docVente.typeDoc = :type AND y.docVente.statut = :statut AND y.docVente.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeCout.augmentation = :sens")})
public class YvsComCoutSupDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_cout_sup_doc_vente_id_seq", name = "yvs_com_cout_sup_doc_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_cout_sup_doc_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "service")
    private Boolean service;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JsonManagedReference
    @JoinColumn(name = "doc_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes docVente;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    @Transient
    private long idDistant;

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }
    public YvsComCoutSupDocVente() {
        this.dateSave = new Date();
        this.dateUpdate= new Date();
    }

    public YvsComCoutSupDocVente(Long id) {
        this.id = id;
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

    public Boolean getService() {
        return service != null ? service : false;
    }

    public void setService(Boolean service) {
        this.service = service;
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

    public YvsComDocVentes getDocVente() {
        return docVente;
    }

    public void setDocVente(YvsComDocVentes docVente) {
        this.docVente = docVente;
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
        if (!(object instanceof YvsComCoutSupDocVente)) {
            return false;
        }
        YvsComCoutSupDocVente other = (YvsComCoutSupDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComCoutSupDocVente[ id=" + id + " ]";
    }

}

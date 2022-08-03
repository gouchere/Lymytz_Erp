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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.rrr.YvsComRistourne;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_ristourne_doc_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComRistourneDocVente.findAll", query = "SELECT y FROM YvsComRistourneDocVente y WHERE y.author.agence.societe = :societe"),
    @NamedQuery(name = "YvsComRistourneDocVente.findByDocVente", query = "SELECT y FROM YvsComRistourneDocVente y WHERE y.docVente = :docVente"),
    @NamedQuery(name = "YvsComRistourneDocVente.findById", query = "SELECT y FROM YvsComRistourneDocVente y WHERE y.id = :id")})
public class YvsComRistourneDocVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_ristourne_doc_vente_id_seq", name = "yvs_com_ristourne_doc_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_ristourne_doc_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "ristourne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComRistourne ristourne;
    @JsonManagedReference
    @JoinColumn(name = "doc_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes docVente;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private long idDistant;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private double montant;

    public YvsComRistourneDocVente() {
    }

    public YvsComRistourneDocVente(Long id) {
        this.id = id;
    }

    public YvsComRistourneDocVente(Long id, YvsComRistourne ristourne) {
        this.id = id;
        this.ristourne = ristourne;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsComRistourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(YvsComRistourne ristourne) {
        this.ristourne = ristourne;
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
        if (!(object instanceof YvsComRistourneDocVente)) {
            return false;
        }
        YvsComRistourneDocVente other = (YvsComRistourneDocVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComRistourneDocVente[ id=" + id + " ]";
    }

}

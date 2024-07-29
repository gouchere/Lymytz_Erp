/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueDocDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_caisse_doc_divers_tiers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findAll", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findById", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findByMontant", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findByDateUpdate", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findByDateSave", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.findByDocDivers", query = "SELECT y FROM YvsComptaCaisseDocDiversTiers y WHERE y.docDivers = :docDivers"),
    @NamedQuery(name = "YvsComptaCaisseDocDiversTiers.sumByDocDiversNotId", query = "SELECT SUM(y.montant) FROM YvsComptaCaisseDocDiversTiers y WHERE y.docDivers = :docDivers AND y.id != :id")

})
public class YvsComptaCaisseDocDiversTiers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_doc_divers_tiers_id_seq", name = "yvs_compta_caisse_doc_divers_tiers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_doc_divers_tiers_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "id_tiers")
    private Long idTiers;
    @Column(name = "table_tiers")
    private String tableTiers;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;

    @OneToOne(mappedBy = "tiersDivers")
    private YvsGrhRetenueDocDivers retenue;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaCaisseDocDiversTiers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaCaisseDocDiversTiers(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
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

    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    public Long getIdTiers() {
        return idTiers != null ? idTiers : 0;
    }

    public void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRetenueDocDivers getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhRetenueDocDivers retenue) {
        this.retenue = retenue;
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
        if (!(object instanceof YvsComptaCaisseDocDiversTiers)) {
            return false;
        }
        YvsComptaCaisseDocDiversTiers other = (YvsComptaCaisseDocDiversTiers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaCaisseDocDiversTiers[ id=" + id + " ]";
    }

}

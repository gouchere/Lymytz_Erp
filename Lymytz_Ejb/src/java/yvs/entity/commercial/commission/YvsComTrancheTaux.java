/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_tranche_taux")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComTrancheTaux.findAll", query = "SELECT y FROM YvsComTrancheTaux y"),
    @NamedQuery(name = "YvsComTrancheTaux.findById", query = "SELECT y FROM YvsComTrancheTaux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTrancheTaux.findByTrancheMaximal", query = "SELECT y FROM YvsComTrancheTaux y WHERE y.trancheMaximal = :trancheMaximal"),
    @NamedQuery(name = "YvsComTrancheTaux.findByTrancheMinimal", query = "SELECT y FROM YvsComTrancheTaux y WHERE y.trancheMinimal = :trancheMinimal"),
    @NamedQuery(name = "YvsComTrancheTaux.findByTaux", query = "SELECT y FROM YvsComTrancheTaux y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsComTrancheTaux.findByNature", query = "SELECT y FROM YvsComTrancheTaux y WHERE y.nature = :nature")})
public class YvsComTrancheTaux implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_tranche_taux_id_seq", name = "yvs_com_tranche_taux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_tranche_taux_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tranche_maximal")
    private Double trancheMaximal;
    @Column(name = "tranche_minimal")
    private Double trancheMinimal;
    @Column(name = "taux")
    private Double taux;
    @Column(name = "nature")
    private Character nature;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_grille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComTypeGrille typeGrille;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean unique;

    public YvsComTrancheTaux() {
    }

    public YvsComTrancheTaux(Integer id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isUnique() {
        unique = getTrancheMaximal().equals(Double.MAX_VALUE);
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Double getTrancheMaximal() {
        return trancheMaximal != null ? trancheMaximal : 0;
    }

    public void setTrancheMaximal(Double trancheMaximal) {
        this.trancheMaximal = trancheMaximal;
    }

    public Double getTrancheMinimal() {
        return trancheMinimal != null ? trancheMinimal : 0;
    }

    public void setTrancheMinimal(Double trancheMinimal) {
        this.trancheMinimal = trancheMinimal;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Character getNature() {
        return nature != null ? String.valueOf(nature).trim().length() > 0 ? nature : 'P' : 'P';
    }

    public void setNature(Character nature) {
        this.nature = nature;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComTypeGrille getTypeGrille() {
        return typeGrille;
    }

    public void setTypeGrille(YvsComTypeGrille typeGrille) {
        this.typeGrille = typeGrille;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComTrancheTaux)) {
            return false;
        }
        YvsComTrancheTaux other = (YvsComTrancheTaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComTrancheTaux[ id=" + id + " ]";
    }

}

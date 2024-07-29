/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.objectifs;

import java.io.Serializable;
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
import yvs.entity.commercial.commission.YvsComCommissionVente;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_periode_objectif")
@NamedQueries({
    @NamedQuery(name = "YvsComPeriodeObjectif.findAll", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.societe = :societe ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findById", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.id = :id ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByCodeRef", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.codeRef = :codeRef ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByDates", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE :date BETWEEN y.dateDebut AND y.dateFin AND y.societe = :societe ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByDateDebut", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.dateDebut = :dateDebut ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByDateFin", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.dateFin = :dateFin ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByDateSave", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.dateSave = :dateSave ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsComPeriodeObjectif.findByDateUpdate", query = "SELECT y FROM YvsComPeriodeObjectif y WHERE y.dateUpdate = :dateUpdate ORDER BY y.dateDebut DESC")})
public class YvsComPeriodeObjectif implements Serializable {
    @Size(max = 2147483647)
    @Column(name = "abbreviation")
    private String abbreviation;
    @OneToMany(mappedBy = "periode")
    private List<YvsComCommissionVente> yvsComCommissionVenteList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_periode_objectif_id_seq", name = "yvs_com_periode_objectif_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_periode_objectif_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;

    public YvsComPeriodeObjectif() {
    }

    public YvsComPeriodeObjectif(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComPeriodeObjectif)) {
            return false;
        }
        YvsComPeriodeObjectif other = (YvsComPeriodeObjectif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.objectifs.YvsComPeriodeObjectif[ id=" + id + " ]";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<YvsComCommissionVente> getYvsComCommissionVenteList() {
        return yvsComCommissionVenteList;
    }

    public void setYvsComCommissionVenteList(List<YvsComCommissionVente> yvsComCommissionVenteList) {
        this.yvsComCommissionVenteList = yvsComCommissionVenteList;
    }

}

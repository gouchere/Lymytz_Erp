/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_type_grille")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComTypeGrille.findAll", query = "SELECT y FROM YvsComTypeGrille y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsComTypeGrille.findById", query = "SELECT y FROM YvsComTypeGrille y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTypeGrille.findByReference", query = "SELECT y FROM YvsComTypeGrille y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsComTypeGrille.findByCible", query = "SELECT y FROM YvsComTypeGrille y WHERE y.cible = :cible")})
public class YvsComTypeGrille implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_type_grille_id_seq", name = "yvs_com_type_grille_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_type_grille_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Column(name = "cible")
    private Character cible;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @OneToMany(mappedBy = "typeGrille")
    private List<YvsComTrancheTaux> tranches;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComTypeGrille() {
        tranches = new ArrayList<>();
    }

    public YvsComTypeGrille(Integer id) {
        this();
        this.id = id;
    }

    public YvsComTypeGrille(Integer id, String reference) {
        this(id);
        this.reference = reference;
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
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Character getCible() {
        return cible != null ? String.valueOf(cible).trim().length() > 0 ? cible : 'C' : 'C';
    }

    public void setCible(Character cible) {
        this.cible = cible;
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

    public List<YvsComTrancheTaux> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComTrancheTaux> tranches) {
        this.tranches = tranches;
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
        if (!(object instanceof YvsComTypeGrille)) {
            return false;
        }
        YvsComTypeGrille other = (YvsComTypeGrille) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComTypeGrille[ id=" + id + " ]";
    }

}

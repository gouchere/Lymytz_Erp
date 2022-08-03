/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_unite_mesure")
@NamedQueries({
    @NamedQuery(name = "YvsBaseUniteMesure.findCountAll", query = "SELECT COUNT(y) FROM YvsBaseUniteMesure y WHERE y.societe =:societe"),
    @NamedQuery(name = "YvsBaseUniteMesure.findAll", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.societe =:societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findById", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.id = :id ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByReference", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.reference = :reference ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByCode", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.reference = :reference AND y.societe = :societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByNumType", query = "SELECT y FROM YvsBaseUniteMesure y WHERE UPPER(y.reference) = UPPER(:reference) AND y.type = :type AND y.societe =:societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByType", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.type = :type AND y.societe =:societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByDefaut", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.defaut = true AND y.societe =:societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByVenteOnline", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.type = :type AND y.societe.venteOnline =:venteOnline ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseUniteMesure.findByLibelle", query = "SELECT y FROM YvsBaseUniteMesure y WHERE y.libelle = :libelle ORDER BY y.reference")})
public class YvsBaseUniteMesure extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_unite_masse_id_seq", name = "yvs_prod_unite_masse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_unite_masse_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @OneToMany(mappedBy = "unite")
    private List<YvsBaseTableConversion> equivalences;
    
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private List<YvsBaseUniteMesure> unitesLiees;
    @Transient
    private String message;

    public YvsBaseUniteMesure() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        equivalences = new ArrayList<>();
        unitesLiees = new ArrayList<>();
    }

    public YvsBaseUniteMesure(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseUniteMesure(Long id, String reference, String libelle) {
        this(id);
        this.reference = reference;
        this.libelle = libelle;
    }

    public YvsBaseUniteMesure(Long id, String reference, String libelle, String type) {
        this(id, reference, libelle);
        this.type = type;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "M" : "M";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseUniteMesure> getUnitesLiees() {
        return unitesLiees;
    }

    public void setUnitesLiees(List<YvsBaseUniteMesure> unitesLiees) {
        this.unitesLiees = unitesLiees;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseTableConversion> getEquivalences() {
        return equivalences;
    }

    public void setEquivalences(List<YvsBaseTableConversion> equivalences) {
        this.equivalences = equivalences;
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
        if (!(object instanceof YvsBaseUniteMesure)) {
            return false;
        }
        YvsBaseUniteMesure other = (YvsBaseUniteMesure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseUniteConversion[ id=" + id + " ]";
    }

}

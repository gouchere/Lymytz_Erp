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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_model_reglement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModelReglement.findAll", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findById", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModelReglement.findByType", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND y.type = :type ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByTypeActif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND y.type = :type AND y.actif = :actif ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByType2", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND (y.type = :type1 OR y.type = :type2) ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByType2Actif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND (y.type = :type1 OR y.type = :type2) AND y.actif = :actif ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByReference", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.reference = :reference AND y.societe = :societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsBaseModelReglement.findByActif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND y.actif = :actif ORDER BY y.reference"),
    
    @NamedQuery(name = "YvsBaseModelReglement.findByType2ActifAcces", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND (y.type = :type1 OR y.type = :type2) AND (y.codeAcces IS NULL OR y.codeAcces.id IN :acces) AND y.actif = :actif ORDER BY y.reference"),

    @NamedQuery(name = "YvsBaseModelReglement.findLikeCodeType2Actif", query = "SELECT y FROM YvsBaseModelReglement y WHERE y.societe = :societe AND UPPER(y.reference) LIKE UPPER(:code) AND (y.type = :type1 OR y.type = :type2) AND y.actif = :actif ORDER BY y.reference"),})
public class YvsBaseModelReglement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_model_reglement_id_seq", name = "yvs_base_model_reglement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_model_reglement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "description")
    private String description;
    @Column(name = "reference")
    private String reference;
    @Column(name = "type")
    private char type;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "paye_before_valide")
    private Boolean payeBeforeValide;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private List<YvsBaseTrancheReglement> tranches;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;
    @Transient
    private String nameType;

    public YvsBaseModelReglement() {
        tranches = new ArrayList<>();
    }

    public YvsBaseModelReglement(Long id) {
        this.id = id;
        tranches = new ArrayList<>();
    }

    public YvsBaseModelReglement(Long id, String designation) {
        this.id = id;
        this.reference = designation;
        tranches = new ArrayList<>();
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public String getNameType() {
        nameType = (type == 'C' ? "CLIENT" : (type == 'F' ? "FOURNISSEUR" : (type == 'A' ? "AUTRE" : "MIXTE")));
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Boolean getPayeBeforeValide() {
        return payeBeforeValide != null ? payeBeforeValide : false;
    }

    public void setPayeBeforeValide(Boolean payeBeforeValide) {
        this.payeBeforeValide = payeBeforeValide;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
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
    public List<YvsBaseTrancheReglement> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsBaseTrancheReglement> tranches) {
        this.tranches = tranches;
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
        if (!(object instanceof YvsBaseModelReglement)) {
            return false;
        }
        YvsBaseModelReglement other = (YvsBaseModelReglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getReference();
    }

}

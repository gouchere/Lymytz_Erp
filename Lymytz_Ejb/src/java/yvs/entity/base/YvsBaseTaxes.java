/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_taxes")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTaxes.findAll", query = "SELECT y FROM YvsBaseTaxes y LEFT JOIN FETCH y.compte WHERE y.societe = :societe ORDER BY y.taux"),
    @NamedQuery(name = "YvsBaseTaxes.findByModule", query = "SELECT y FROM YvsBaseTaxes y WHERE y.societe = :societe AND y.module = :module ORDER BY y.taux"),
    @NamedQuery(name = "YvsBaseTaxes.findByModules", query = "SELECT y FROM YvsBaseTaxes y WHERE y.societe = :societe AND y.module IN :modules ORDER BY y.taux"),
    @NamedQuery(name = "YvsBaseTaxes.findByCodeTaxe", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeTaxe = :code AND y.societe=:societe"),
    @NamedQuery(name = "YvsBaseTaxes.findByTaux", query = "SELECT y FROM YvsBaseTaxes y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsBaseTaxes.findByCodeAppel", query = "SELECT y FROM YvsBaseTaxes y WHERE y.codeAppel = :codeAppel"),
    @NamedQuery(name = "YvsBaseTaxes.findById", query = "SELECT y FROM YvsBaseTaxes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTaxes.findBySupp", query = "SELECT y FROM YvsBaseTaxes y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseTaxes.findByActif", query = "SELECT y FROM YvsBaseTaxes y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseTaxes.findByDesignation", query = "SELECT y FROM YvsBaseTaxes y WHERE y.designation = :designation")})
public class YvsBaseTaxes extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_taxes_id_seq", name = "yvs_taxes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_taxes_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "code_taxe")
    private String codeTaxe;
    @Size(max = 2147483647)
    @Column(name = "code_appel")
    private String codeAppel;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Size(max = 2147483647)
    @Column(name = "libelle_print")
    private String libellePrint;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "module")
    private String module = "M";
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseTaxes() {
    }

    public YvsBaseTaxes(Long id) {
        this.id = id;
    }

    public YvsBaseTaxes(Long id, double taux) {
        this.id = id;
        this.taux = taux;
    }

    public YvsBaseTaxes(Long id, String codeTaxe, Double taux) {
        this.taux = taux;
        this.codeTaxe = codeTaxe;
        this.id = id;
    }

    public String getLibellePrint() {
        return libellePrint != null ? libellePrint.trim().length() > 0 ? libellePrint : getCodeTaxe() : getCodeTaxe();
    }

    public void setLibellePrint(String libellePrint) {
        this.libellePrint = libellePrint;
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

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public String getCodeTaxe() {
        return codeTaxe != null ? codeTaxe : "";
    }

    public void setCodeTaxe(String codeTaxe) {
        this.codeTaxe = codeTaxe;
    }

    public String getCodeAppel() {
        return codeAppel;
    }

    public void setCodeAppel(String codeAppel) {
        this.codeAppel = codeAppel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getModule() {
        return module != null ? (module.trim().length() > 0 ? module : "M") : "M";
    }

    public void setModule(String module) {
        this.module = module;
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

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
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
        if (!(object instanceof YvsBaseTaxes)) {
            return false;
        }
        YvsBaseTaxes other = (YvsBaseTaxes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseTaxes[ id=" + id + " ]";
    }

}

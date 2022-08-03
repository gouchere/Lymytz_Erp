/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_mutuelle")
@NamedQueries({
    @NamedQuery(name = "YvsMutMutuelle.findAll", query = "SELECT y FROM YvsMutMutuelle y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsMutMutuelle.findById", query = "SELECT y FROM YvsMutMutuelle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutMutuelle.findByDesignation", query = "SELECT y FROM YvsMutMutuelle y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsMutMutuelle.findByDateCreation", query = "SELECT y FROM YvsMutMutuelle y WHERE y.dateCreation = :dateCreation"),
    @NamedQuery(name = "YvsMutMutuelle.findByMontantEpargne", query = "SELECT y FROM YvsMutMutuelle y WHERE y.montantEpargne = :montantEpargne")})
public class YvsMutMutuelle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_mutuelle_id_seq", name = "yvs_mut_mutuelle_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_mutuelle_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "logo")
    private String logo;
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_epargne")
    private Double montantEpargne;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_inscription")
    private Double montantInscription;
    @Column(name = "montant_assurance")
    private Double montantAssurance;
    @Column(name = "retenue_fixe")
    private Double retenueFixe;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToOne(mappedBy = "mutuelle")
    private YvsMutParametre paramsMutuelle;
    @OneToMany(mappedBy = "mutuelle")
    private List<YvsAgences> agences;
    @OneToMany(mappedBy = "mutuelle")
    private List<YvsMutCaisse> caisses;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutMutuelle() {
    }

    public YvsMutMutuelle(Long id) {
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

    public Double getMontantInscription() {
        return montantInscription;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMontantInscription(Double montantInscription) {
        this.montantInscription = montantInscription;
    }

    public Long getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateCreation() {
        return dateCreation;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getMontantEpargne() {
        return montantEpargne != null ? montantEpargne : 0;
    }

    public void setMontantEpargne(Double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsAgences> getAgences() {
        return agences;
    }

    public void setAgences(List<YvsAgences> agences) {
        this.agences = agences;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsMutParametre getParamsMutuelle() {
        return paramsMutuelle;
    }

    public void setParamsMutuelle(YvsMutParametre paramsMutuelle) {
        this.paramsMutuelle = paramsMutuelle;
    }

    public Double getMontantAssurance() {
        return montantAssurance != null ? montantAssurance : 0;
    }

    public void setMontantAssurance(Double montantAssurance) {
        this.montantAssurance = montantAssurance;
    }

    public Double getRetenueFixe() {
        return retenueFixe!=null?retenueFixe:0;
    }

    public void setRetenueFixe(Double retenueFixe) {
        this.retenueFixe = retenueFixe;
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
        if (!(object instanceof YvsMutMutuelle)) {
            return false;
        }
        YvsMutMutuelle other = (YvsMutMutuelle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.YvsMutMutuelle[ id=" + id + " ]";
    }

}

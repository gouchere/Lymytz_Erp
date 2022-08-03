/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_departement", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsBaseDepartement.findAll", query = "SELECT y FROM YvsBaseDepartement y WHERE y.societe = :societe ORDER BY y.intitule"),
    @NamedQuery(name = "YvsBaseDepartement.findById", query = "SELECT y FROM YvsBaseDepartement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseDepartement.findByCheminParent", query = "SELECT y FROM YvsBaseDepartement y WHERE y.cheminParent = :cheminParent"),
    @NamedQuery(name = "YvsBaseDepartement.findByCodeDepartement", query = "SELECT y FROM YvsBaseDepartement y WHERE y.codeDepartement = :codeDepartement AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseDepartement.findByDescription", query = "SELECT y FROM YvsBaseDepartement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseDepartement.findByCheminFichier", query = "SELECT y FROM YvsBaseDepartement y WHERE y.cheminFichier = :cheminFichier"),
    @NamedQuery(name = "YvsBaseDepartement.findByIntitule", query = "SELECT y FROM YvsBaseDepartement y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsBaseDepartement.findByNivau", query = "SELECT y FROM YvsBaseDepartement y WHERE y.nivau = :nivau"),
    @NamedQuery(name = "YvsBaseDepartement.findBySupp", query = "SELECT y FROM YvsBaseDepartement y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseDepartement.findByActif", query = "SELECT y FROM YvsBaseDepartement y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseDepartement.findByAbreviation", query = "SELECT y FROM YvsBaseDepartement y WHERE y.abreviation = :abreviation"),
    @NamedQuery(name = "YvsBaseDepartement.findByDateUpdate", query = "SELECT y FROM YvsBaseDepartement y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseDepartement.findByDateSave", query = "SELECT y FROM YvsBaseDepartement y WHERE y.dateSave = :dateSave")})
public class YvsBaseDepartement extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_departement_id_seq", name = "yvs_base_departement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_departement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "chemin_parent")
    private String cheminParent;
    @Size(max = 255)
    @Column(name = "code_departement")
    private String codeDepartement;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "chemin_fichier")
    private String cheminFichier;
    @Size(max = 255)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "nivau")
    private Integer nivau;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "abreviation")
    private String abreviation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "departement_parent", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepartement departementParent;

    @OneToMany(mappedBy = "departementParent")
    private List<YvsBaseDepartement> sous;

    public YvsBaseDepartement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        sous = new ArrayList<>();
    }

    public YvsBaseDepartement(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseDepartement(Long id, String codeDepartement, String intitule) {
        this(id);
        this.codeDepartement = codeDepartement;
        this.intitule = intitule;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheminParent() {
        return cheminParent;
    }

    public void setCheminParent(String cheminParent) {
        this.cheminParent = cheminParent;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getNivau() {
        return nivau != null ? nivau : 0;
    }

    public void setNivau(Integer nivau) {
        this.nivau = nivau;
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

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
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

    @Override
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

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseDepartement> getSous() {
        return sous;
    }

    public void setSous(List<YvsBaseDepartement> sous) {
        this.sous = sous;
    }

    public YvsBaseDepartement getDepartementParent() {
        return departementParent;
    }

    public void setDepartementParent(YvsBaseDepartement departementParent) {
        this.departementParent = departementParent;
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
        if (!(object instanceof YvsBaseDepartement)) {
            return false;
        }
        YvsBaseDepartement other = (YvsBaseDepartement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseDepartement[ id=" + id + " ]";
    }

}

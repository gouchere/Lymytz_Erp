/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseDepartement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_departement")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDepartement.findAll", query = "SELECT y FROM YvsGrhDepartement y LEFT JOIN FETCH y.departementParent WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsGrhDepartement.findById", query = "SELECT y FROM YvsGrhDepartement y LEFT JOIN FETCH y.departementParent WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDepartement.findByIdIn", query = "SELECT y FROM YvsGrhDepartement y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsGrhDepartement.findBySociete", query = "SELECT y FROM YvsGrhDepartement y LEFT JOIN FETCH y.chefDepartement LEFT JOIN FETCH y.departementParent WHERE y.societe = :societe AND y.actif=true ORDER BY y.intitule ASC"),
    @NamedQuery(name = "YvsGrhDepartement.findByIntitule", query = "SELECT y FROM YvsGrhDepartement y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsGrhDepartement.findByParent", query = "SELECT y FROM YvsGrhDepartement y WHERE y.departementParent = :parent"),
    @NamedQuery(name = "YvsGrhDepartement.findByAgence", query = "SELECT y FROM YvsGrhDepartement y WHERE y.chefDepartement.agence = :agence"),
    @NamedQuery(name = "YvsGrhDepartement.findByCode", query = "SELECT y FROM YvsGrhDepartement y WHERE y.codeDepartement like :codeDepartement AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhDepartement.findByReference", query = "SELECT y FROM YvsGrhDepartement y WHERE y.codeDepartement = :code AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhDepartement.findByDescription", query = "SELECT y FROM YvsGrhDepartement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsGrhDepartement.countD", query = "SELECT COUNT(y) FROM YvsGrhDepartement y WHERE y.codeDepartement = :code AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhDepartement.findByService", query = "SELECT y FROM YvsGrhDepartement y WHERE y.service = :service"),
    @NamedQuery(name = "YvsGrhDepartement.findByFile", query = "SELECT y FROM YvsGrhDepartement y WHERE y.file = :file")})
public class YvsGrhDepartement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_grh_departement_id_seq")
    @SequenceGenerator(sequenceName = "yvs_grh_departement_id_seq", allocationSize = 1, name = "yvs_grh_departement_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "chemin_parent")
    private String cheminParent;
    @Column(name = "nivau")
    private Integer nivau;
    @Column(name = "visible_on_livre_paie")
    private Boolean visibleOnLivrePaie;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "chemin_fichier")
    private String file;
    @Column(name = "code_departement")
    private String codeDepartement;
    @Column(name = "abreviation")
    private String abreviation;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "chef_departement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes chefDepartement;
    @JoinColumn(name = "departement_parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDepartement departementParent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "service", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepartement service;

    @OneToMany(mappedBy = "departement")
    private List<YvsGrhPosteDeTravail> yvsPosteDeTravailList;
    @OneToMany(mappedBy = "departementParent")
    private List<YvsGrhDepartement> subDepartements;

    public YvsGrhDepartement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhDepartement(Integer id) {
        this();
        this.id = id;
    }

    public YvsGrhDepartement(String codeDepartement) {
        this();
        this.codeDepartement = codeDepartement;
    }

    public YvsGrhDepartement(Integer id, String intitule) {
        this(id);
        this.intitule = intitule;
    }

    public YvsGrhDepartement(Integer id, String codeDepartement, String intitule) {
        this(id, intitule);
        this.codeDepartement = codeDepartement;
    }

    public YvsGrhDepartement(Integer id, YvsUsersAgence author) {
        this(id);
        this.author = author;
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

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<YvsGrhDepartement> getSubDepartements() {
        return subDepartements;
    }

    public void setSubDepartements(List<YvsGrhDepartement> subDepartements) {
        this.subDepartements = subDepartements;
    }

    public YvsGrhDepartement getDepartementParent() {
        return departementParent;
    }

    public void setDepartementParent(YvsGrhDepartement departementParent) {
        this.departementParent = departementParent;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public YvsBaseDepartement getService() {
        return service;
    }

    public void setService(YvsBaseDepartement service) {
        this.service = service;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhDepartement)) {
            return false;
        }
        YvsGrhDepartement other = (YvsGrhDepartement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhDepartement[ id=" + id + " ]";
    }

    public String getCheminParent() {
        return cheminParent;
    }

    public void setCheminParent(String cheminParent) {
        this.cheminParent = cheminParent;
    }

    public Integer getNivau() {
        return nivau;
    }

    public void setNivau(Integer nivau) {
        this.nivau = nivau;
    }

    public Boolean getVisibleOnLivrePaie() {
        return visibleOnLivrePaie != null ? visibleOnLivrePaie : false;
    }

    public void setVisibleOnLivrePaie(Boolean visibleOnLivrePaie) {
        this.visibleOnLivrePaie = visibleOnLivrePaie;
    }

    public List<YvsGrhPosteDeTravail> getYvsPosteDeTravailList() {
        return yvsPosteDeTravailList;
    }

    public void setYvsPosteDeTravailList(List<YvsGrhPosteDeTravail> yvsPosteDeTravailList) {
        this.yvsPosteDeTravailList = yvsPosteDeTravailList;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhEmployes getChefDepartement() {
        return chefDepartement;
    }

    public void setChefDepartement(YvsGrhEmployes chefDepartement) {
        this.chefDepartement = chefDepartement;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

}

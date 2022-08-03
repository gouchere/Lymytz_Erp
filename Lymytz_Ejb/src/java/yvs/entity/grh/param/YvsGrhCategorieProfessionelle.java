/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_categorie_professionelle")
@NamedQueries({
    @NamedQuery(name = "YvsCategorieProfessionelle.findAll", query = "SELECT DISTINCT y FROM YvsGrhCategorieProfessionelle y LEFT OUTER JOIN y.listEchelons E WHERE y.societe=:societe ORDER BY y.degre ASC"),
    @NamedQuery(name = "YvsCategorieProfessionelle.findAll1", query = "SELECT y.categorie FROM YvsGrhCategorieProfessionelle y WHERE y.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsCategorieProfessionelle.findAllActif", query = "SELECT y FROM YvsGrhCategorieProfessionelle y WHERE y.societe=:societe AND y.actif=true ORDER BY y.degre"),
    @NamedQuery(name = "YvsCategorieProfessionelle.findByCategorieSociete", query = "SELECT y FROM YvsGrhCategorieProfessionelle y WHERE y.categorie = :categorie AND y.societe = :societe"),
    @NamedQuery(name = "YvsCategorieProfessionelle.findById", query = "SELECT y FROM YvsGrhCategorieProfessionelle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsCategorieProfessionelle.count", query = "SELECT count(y) FROM YvsGrhCategorieProfessionelle y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsCategorieProfessionelle.findByCategorie", query = "SELECT y FROM YvsGrhCategorieProfessionelle y WHERE y.categorie = :categorie")})
public class YvsGrhCategorieProfessionelle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_categorie_professionelle_id_seq", name = "yvs_categorie_professionelle_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_categorie_professionelle_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "degre")
    private Integer degre;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    private List<YvsGrhCategoriePreavis> listDureePreavis;
    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    private List<YvsGrhConventionCollective> listEchelons;

    @Transient
    private String chainePreavis;
    @Transient
    private boolean new_;

    public YvsGrhCategorieProfessionelle() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhCategorieProfessionelle(Integer id) {
        this();
        this.id = id;
    }

    public YvsGrhCategorieProfessionelle(Integer id, String categorie) {
        this(id);
        this.categorie = categorie;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getChainePreavis() {
        return chainePreavis;
    }

    public void setChainePreavis(String chainePreavis) {
        this.chainePreavis = chainePreavis;
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
        if (!(object instanceof YvsGrhCategorieProfessionelle)) {
            return false;
        }
        YvsGrhCategorieProfessionelle other = (YvsGrhCategorieProfessionelle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsCategorieProfessionelle[ id=" + id + " ]";
    }

    public List<YvsGrhConventionCollective> getListEchelons() {
        return listEchelons;
    }

    public void setListEchelons(List<YvsGrhConventionCollective> listEchelons) {
        this.listEchelons = listEchelons;
    }

    public Integer getDegre() {
        return degre != null ? degre : 0;
    }

    public void setDegre(Integer degre) {
        this.degre = degre;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsGrhCategoriePreavis> getListDureePreavis() {
        return listDureePreavis;
    }

    public void setListDureePreavis(List<YvsGrhCategoriePreavis> listDureePreavis) {
        this.listDureePreavis = listDureePreavis;
    }

}

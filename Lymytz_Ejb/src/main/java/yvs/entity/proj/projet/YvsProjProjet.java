/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.proj.projet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_proj_projet", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsProjProjet.findAll", query = "SELECT y FROM YvsProjProjet y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsProjProjet.findById", query = "SELECT y FROM YvsProjProjet y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProjProjet.findByCode", query = "SELECT y FROM YvsProjProjet y WHERE y.code = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsProjProjet.findByLibelle", query = "SELECT y FROM YvsProjProjet y WHERE y.libelle = :libelle AND y.societe = :societe"),
    @NamedQuery(name = "YvsProjProjet.findByDescription", query = "SELECT y FROM YvsProjProjet y WHERE y.description = :description AND y.societe = :societe"),
    @NamedQuery(name = "YvsProjProjet.findBySupp", query = "SELECT y FROM YvsProjProjet y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsProjProjet.findByActif", query = "SELECT y FROM YvsProjProjet y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsProjProjet.findByDateUpdate", query = "SELECT y FROM YvsProjProjet y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProjProjet.findByDateSave", query = "SELECT y FROM YvsProjProjet y WHERE y.dateSave = :dateSave")})
public class YvsProjProjet extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_proj_projet_id_seq", name = "yvs_proj_projet_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_proj_projet_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "direct")
    private Boolean direct = false;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private YvsProjProjet parent;

    @OneToMany(mappedBy = "projet")
    private List<YvsProjProjetService> services;

    public YvsProjProjet() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.services = new ArrayList<>();
    }

    public YvsProjProjet(Long id) {
        this();
        this.id = id;
    }

    public YvsProjProjet(Long id, String code, String libelle) {
        this(id);
        this.code = code;
        this.libelle = libelle;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getDirect() {
        return direct != null ? direct : false;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
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

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsProjProjetService> getServices() {
        return services;
    }

    public void setServices(List<YvsProjProjetService> services) {
        this.services = services;
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

    public YvsProjProjet getParent() {
        return parent;
    }

    public void setParent(YvsProjProjet parent) {
        this.parent = parent;
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
        if (!(object instanceof YvsProjProjet)) {
            return false;
        }
        YvsProjProjet other = (YvsProjProjet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.proj.YvsProjProjet[ id=" + id + " ]";
    }

}

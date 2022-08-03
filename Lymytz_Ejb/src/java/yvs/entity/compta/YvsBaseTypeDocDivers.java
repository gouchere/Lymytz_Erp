/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.Util;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_type_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTypeDocDivers.findAll", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.societe=:societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByModule", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.societe=:societe AND y.module = :module ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findAllActif", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.societe=:societe AND y.actif=true ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByAcces", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.societe=:societe AND y.actif=true AND (y.codeAcces.id IN :ids OR y.codeAcces IS NULL) ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByAccesModule", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.societe=:societe AND y.module = :module AND y.actif=true AND (y.codeAcces.id IN :ids OR y.codeAcces IS NULL) ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByIdByCode", query = "SELECT y.id FROM YvsBaseTypeDocDivers y WHERE y.libelle=:libelle AND y.societe=:societe ORDER BY y.libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findById", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByLibelle", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByDescription", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByDateSave", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseTypeDocDivers.findByDateUpdate", query = "SELECT y FROM YvsBaseTypeDocDivers y WHERE y.dateUpdate = :dateUpdate")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseTypeDocDivers extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_type_doc_divers_id_seq", name = "yvs_base_type_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_type_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "module")
    private String module;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "can_reception")
    private Boolean canReception;
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
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @OneToMany(mappedBy = "typeDoc", fetch = FetchType.LAZY)
    private List<YvsBaseTypeDocCategorie> categories;

    public YvsBaseTypeDocDivers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.categories = new ArrayList<>();
    }

    public YvsBaseTypeDocDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseTypeDocDivers(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getCanReception() {
        return canReception != null ? canReception : false;
    }

    public void setCanReception(Boolean canReception) {
        this.canReception = canReception;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getModule() {
        return Util.asString(module) ? module : Constantes.TYPE_OD;
    }

    public void setModule(String module) {
        this.module = module;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
    }

    @XmlTransient
    public List<YvsBaseTypeDocCategorie> getCategories() {
        return categories;
    }

    public void setCategories(List<YvsBaseTypeDocCategorie> categories) {
        this.categories = categories;
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
        if (!(object instanceof YvsBaseTypeDocDivers)) {
            return false;
        }
        YvsBaseTypeDocDivers other = (YvsBaseTypeDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsBaseTypeDocDivers[ id=" + id + " ]";
    }

}

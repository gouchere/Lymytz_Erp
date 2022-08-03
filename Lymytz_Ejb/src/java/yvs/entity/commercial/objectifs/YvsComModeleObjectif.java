/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.objectifs;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_modele_objectif")
@NamedQueries({
    @NamedQuery(name = "YvsComModeleObjectif.findAll", query = "SELECT y FROM YvsComModeleObjectif y"),
    @NamedQuery(name = "YvsComModeleObjectif.findById", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComModeleObjectif.findByCodeRef", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.codeRef = :codeRef"),
    @NamedQuery(name = "YvsComModeleObjectif.findByDescription", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.description = :description"),
    @NamedQuery(name = "YvsComModeleObjectif.findByIndicateur", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.indicateur = :indicateur"),
    @NamedQuery(name = "YvsComModeleObjectif.findByDateSave", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComModeleObjectif.findByDateUpdate", query = "SELECT y FROM YvsComModeleObjectif y WHERE y.dateUpdate = :dateUpdate")})
public class YvsComModeleObjectif implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_modele_objectif_id_seq", name = "yvs_com_modele_objectif_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_modele_objectif_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "indicateur")
    private String indicateur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @OneToMany(mappedBy = "objectif")
    private List<YvsComCibleObjectif> cibles;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComModeleObjectif() {
    }

    public YvsComModeleObjectif(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<YvsComCibleObjectif> getCibles() {
        return cibles;
    }

    public void setCibles(List<YvsComCibleObjectif> cibles) {
        this.cibles = cibles;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComModeleObjectif)) {
            return false;
        }
        YvsComModeleObjectif other = (YvsComModeleObjectif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.objectifs.YvsComModeleObjectif[ id=" + id + " ]";
    }

}

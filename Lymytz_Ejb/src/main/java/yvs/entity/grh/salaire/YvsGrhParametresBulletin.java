/*
 * To change this license headerBulletin, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_grh_parametres_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsGrhParametresBulletin.findAll", query = "SELECT y FROM YvsGrhParametresBulletin y"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findById", query = "SELECT y FROM YvsGrhParametresBulletin y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findOneParam", query = "SELECT y FROM YvsGrhParametresBulletin y WHERE y.codeElement=:code AND y.contrat=:contrat AND y.headerBulletin=:header"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findVOneParam", query = "SELECT y.valeur FROM YvsGrhParametresBulletin y WHERE y.codeElement=:code AND y.contrat=:contrat AND y.headerBulletin=:header"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findDistinctCode", query = "SELECT DISTINCT y.codeElement, y.description FROM YvsGrhParametresBulletin y WHERE y.headerBulletin.societe=:societe"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findDistinct", query = "SELECT DISTINCT y FROM YvsGrhParametresBulletin y WHERE y.headerBulletin.societe=:societe"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findByCodeElement", query = "SELECT y FROM YvsGrhParametresBulletin y WHERE y.codeElement = :codeElement"),
    @NamedQuery(name = "YvsGrhParametresBulletin.findByDateSave", query = "SELECT y FROM YvsGrhParametresBulletin y WHERE y.dateSave = :dateSave")})
public class YvsGrhParametresBulletin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_parametres_bulletin_id_seq", name = "yvs_grh_parametres_bulletin_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_parametres_bulletin_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "code_element")
    private String codeElement;
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "header_bulletin", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhOrdreCalculSalaire headerBulletin;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;
    @Column(name = "actif")
    private Boolean actif;

    public YvsGrhParametresBulletin() {
    }

    public YvsGrhParametresBulletin(Long id) {
        this.id = id;
    }

    public YvsGrhParametresBulletin(Long id, YvsGrhParametresBulletin y) {
        this.id = id;
        this.dateUpdate = y.dateUpdate;
        this.codeElement = y.codeElement;
        this.description = y.description;
        this.valeur = y.valeur;
        this.dateSave = y.dateSave;
        this.author = y.author;
        this.headerBulletin = y.headerBulletin;
        this.contrat = y.contrat;
        this.actif = y.actif;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeElement() {
        return codeElement;
    }

    public void setCodeElement(String codeElement) {
        this.codeElement = codeElement;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhOrdreCalculSalaire getHeaderBulletin() {
        return headerBulletin;
    }

    public void setHeaderBulletin(YvsGrhOrdreCalculSalaire headerBulletin) {
        this.headerBulletin = headerBulletin;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getActif() {
        return actif;
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
        if (!(object instanceof YvsGrhParametresBulletin)) {
            return false;
        }
        YvsGrhParametresBulletin other = (YvsGrhParametresBulletin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhParametresBulletin[ id=" + id + " ]";
    }

}

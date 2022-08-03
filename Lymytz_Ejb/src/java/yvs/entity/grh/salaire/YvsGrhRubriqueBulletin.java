/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_rubrique_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRubriqueBulletin.findAll", query = "SELECT y FROM YvsGrhRubriqueBulletin y WHERE y.societe=:societe ORDER BY y.code ASC"),
    @NamedQuery(name = "YvsGrhRubriqueBulletin.findById", query = "SELECT y FROM YvsGrhRubriqueBulletin y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhRubriqueBulletin.findByDesignation", query = "SELECT y FROM YvsGrhRubriqueBulletin y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsGrhRubriqueBulletin.findByCode", query = "SELECT y FROM YvsGrhRubriqueBulletin y WHERE y.code = :code")})
public class YvsGrhRubriqueBulletin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_rubrique_bulletin_id_seq", name = "yvs_grh_rubrique_bulletin_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_rubrique_bulletin_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @OneToMany(mappedBy = "rubrique")
    private List<YvsGrhElementSalaire> elements;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private List<YvsGrhDetailBulletin> elementsBulletin;

    public YvsGrhRubriqueBulletin() {

    }

    public YvsGrhRubriqueBulletin(Long id) {
        this.id = id;
    }

    public YvsGrhRubriqueBulletin(Long id, String code, String designation) {
        this.id = id;
        this.designation = designation;
        this.code = code;
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

    public Long getId() {
        return id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<YvsGrhElementSalaire> getElements() {
        return elements;
    }

    public void setElements(List<YvsGrhElementSalaire> elements) {
        this.elements = elements;
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

    public List<YvsGrhDetailBulletin> getElementsBulletin() {
        return elementsBulletin;
    }

    public void setElementsBulletin(List<YvsGrhDetailBulletin> elementsBulletin) {
        this.elementsBulletin = elementsBulletin;
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
        if (!(object instanceof YvsGrhRubriqueBulletin)) {
            return false;
        }
        YvsGrhRubriqueBulletin other = (YvsGrhRubriqueBulletin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhRubriqueBulletin[ id=" + id + " ]";
    }

}

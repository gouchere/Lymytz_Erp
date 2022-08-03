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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_structure_salaire")
@NamedQueries({
    @NamedQuery(name = "YvsStructureSalaire.findAll", query = "SELECT y FROM YvsGrhStructureSalaire y WHERE y.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsStructureSalaire.findById", query = "SELECT y FROM YvsGrhStructureSalaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStructureSalaire.findByNom", query = "SELECT y FROM YvsGrhStructureSalaire y WHERE y.nom LIKE :nom AND y.societe=:societe AND y.actif=true")})
public class YvsGrhStructureSalaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_structure_salaire_id_seq", name = "yvs_structure_salaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_structure_salaire_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "nom")
    private String nom;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @OneToMany(mappedBy = "structure", fetch = FetchType.LAZY)
    private List<YvsGrhElementStructure> elementSalaire;

    public YvsGrhStructureSalaire() {
    }

    public YvsGrhStructureSalaire(Long id) {
        this.id = id;
    }

    public YvsGrhStructureSalaire(YvsGrhStructureSalaire s) {
        this.author = s.author;
        this.supp = s.supp;
        this.actif = s.actif;
        this.description = s.description;
        this.code = s.code;
        this.elementSalaire = s.elementSalaire;
        this.societe = s.societe;
        this.id = s.id;
        this.nom = s.nom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public List<YvsGrhElementStructure> getElementSalaire() {
        return elementSalaire;
    }

    public void setElementSalaire(List<YvsGrhElementStructure> elementSalaire) {
        this.elementSalaire = elementSalaire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof YvsGrhStructureSalaire)) {
            return false;
        }
        YvsGrhStructureSalaire other = (YvsGrhStructureSalaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsStructureSalaire[ id=" + id + " ]";
    }

    }

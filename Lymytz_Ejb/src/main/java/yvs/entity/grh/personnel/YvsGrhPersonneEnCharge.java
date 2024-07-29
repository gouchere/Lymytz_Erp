/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_personne_en_charge")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findAll", query = "SELECT y FROM YvsGrhPersonneEnCharge y"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findById", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByNom", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByDateNaissance", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.dateNaissance = :dateNaissance"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByFile", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.file = :file"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findBySupp", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByActif", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByEmploye", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByInfirme", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.infirme = :infirme"),
    @NamedQuery(name = "YvsGrhPersonneEnCharge.findByScolarise", query = "SELECT y FROM YvsGrhPersonneEnCharge y WHERE y.scolarise = :scolarise")})
public class YvsGrhPersonneEnCharge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_employe_personnes_id_seq", name = "yvs_employe_personnes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_employe_personnes_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Size(max = 2147483647)
    @Column(name = "file")
    private String file;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "infirme")
    private Boolean infirme;
    @Column(name = "scolarise")
    private Boolean scolarise;
    @Column(name = "epouse")
    private Boolean epouse;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsGrhPersonneEnCharge() {
    }

    public YvsGrhPersonneEnCharge(Long id) {
        this.id = id;
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

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getFile() {
        return file;
    }

    public void setEpouse(Boolean epouse) {
        this.epouse = epouse;
    }

    public Boolean getEpouse() {
        return epouse;
    }

    public void setFile(String file) {
        this.file = file;
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

    public Boolean getInfirme() {
        return infirme;
    }

    public void setInfirme(Boolean infirme) {
        this.infirme = infirme;
    }

    public Boolean getScolarise() {
        return scolarise;
    }

    public void setScolarise(Boolean scolarise) {
        this.scolarise = scolarise;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsGrhPersonneEnCharge)) {
            return false;
        }
        YvsGrhPersonneEnCharge other = (YvsGrhPersonneEnCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhPersonneEnCharge[ id=" + id + " ]";
    }

}

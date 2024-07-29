/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_contacts_emps")
@NamedQueries({
    @NamedQuery(name = "YvsContactsEmps.findAll", query = "SELECT y FROM YvsContactsEmps y"),
    @NamedQuery(name = "YvsContactsEmps.findById", query = "SELECT y FROM YvsContactsEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsContactsEmps.findByNom", query = "SELECT y FROM YvsContactsEmps y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsContactsEmps.findByEmploye", query = "SELECT y FROM YvsContactsEmps y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsContactsEmps.findByTelephone", query = "SELECT y FROM YvsContactsEmps y WHERE y.telephone = :telephone"),
    @NamedQuery(name = "YvsContactsEmps.findByAdress", query = "SELECT y FROM YvsContactsEmps y WHERE y.adress = :adress")})
public class YvsContactsEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    @SequenceGenerator(sequenceName = "yvs_contacts_emps_id_seq", name = "yvs_contacts_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contacts_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(min = 1, max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Size(max = 2147483647)
    @Column(name = "adress")
    private String adress;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsContactsEmps() {
    }

    public YvsContactsEmps(String nom) {
        this.nom = nom;
    }

    public YvsContactsEmps(String nom, int id) {
        this.nom = nom;
        this.id = id;
    }

    public YvsContactsEmps(int id) {
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

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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
        hash += (nom != null ? nom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsContactsEmps)) {
            return false;
        }
        YvsContactsEmps other = (YvsContactsEmps) object;
        if ((this.nom == null && other.nom != null) || (this.nom != null && !this.nom.equals(other.nom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsContactsEmps[ nom=" + nom + " ]";
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

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

}

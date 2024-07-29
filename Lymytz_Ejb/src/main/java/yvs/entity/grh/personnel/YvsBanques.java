/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_banques")
@NamedQueries({
    @NamedQuery(name = "YvsBanques.findAll", query = "SELECT y FROM YvsBanques y LEFT JOIN FETCH y.pays LEFT JOIN FETCH y.ville WHERE y.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsBanques.findById", query = "SELECT y FROM YvsBanques y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBanques.findByNom", query = "SELECT y FROM YvsBanques y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsBanques.findByVille", query = "SELECT y FROM YvsBanques y WHERE y.ville = :ville"),
    @NamedQuery(name = "YvsBanques.findByPays", query = "SELECT y FROM YvsBanques y WHERE y.pays = :pays"),
    @NamedQuery(name = "YvsBanques.findByBoitePostal", query = "SELECT y FROM YvsBanques y WHERE y.boitePostal = :boitePostal"),
    @NamedQuery(name = "YvsBanques.findBySupp", query = "SELECT y FROM YvsBanques y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBanques.findByActif", query = "SELECT y FROM YvsBanques y WHERE y.actif = :actif")})
public class YvsBanques implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_banques_id_seq", name = "yvs_banques_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_banques_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "boite_postal")
    private String boitePostal;
    @Column(name = "supp")
    private Boolean supp;
    @Size(max = 2147483647)
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire ville;
    @JoinColumn(name = "pays", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire pays;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsBanques() {
    }

    public YvsBanques(Integer id) {
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

    public Integer getId() {
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

    public String getBoitePostal() {
        return boitePostal;
    }

    public void setBoitePostal(String boitePostal) {
        this.boitePostal = boitePostal;
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
        if (!(object instanceof YvsBanques)) {
            return false;
        }
        YvsBanques other = (YvsBanques) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsBanques[ id=" + id + " ]";
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public YvsDictionnaire getVille() {
        return ville;
    }

    public void setVille(YvsDictionnaire ville) {
        this.ville = ville;
    }

    public YvsDictionnaire getPays() {
        return pays;
    }

    public void setPays(YvsDictionnaire pays) {
        this.pays = pays;
    }

}

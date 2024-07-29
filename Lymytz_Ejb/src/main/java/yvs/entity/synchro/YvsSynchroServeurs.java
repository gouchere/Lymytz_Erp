/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.synchro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_synchro_serveurs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsSynchroServeurs.findAll", query = "SELECT y FROM YvsSynchroServeurs y"),
    @NamedQuery(name = "YvsSynchroServeurs.findById", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroServeurs.findByNomServeur", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.nomServeur = :nomServeur"),
    @NamedQuery(name = "YvsSynchroServeurs.findIdByNomServeur", query = "SELECT y.id FROM YvsSynchroServeurs y WHERE (y.nomServeur = :nomServeur OR y.adresseIp=:nomServeur)"),
    @NamedQuery(name = "YvsSynchroServeurs.findByAdresseIp", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.adresseIp = :adresseIp"),
    @NamedQuery(name = "YvsSynchroServeurs.findByActif", query = "SELECT y FROM YvsSynchroServeurs y WHERE y.actif = :actif")})
public class YvsSynchroServeurs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_serveurs_id_seq", name = "yvs_synchro_serveurs_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_serveurs_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nom_serveur")
    private String nomServeur;
    @Size(max = 2147483647)
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Column(name = "actif")
    private Boolean actif;
    @OneToMany(mappedBy = "serveur")
    private List<YvsSynchroDataSynchro> yvsSynchroDataSynchroList;

    public YvsSynchroServeurs() {
    }

    public YvsSynchroServeurs(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomServeur() {
        return nomServeur;
    }

    public void setNomServeur(String nomServeur) {
        this.nomServeur = nomServeur;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsSynchroDataSynchro> getYvsSynchroDataSynchroList() {
        return yvsSynchroDataSynchroList;
    }

    public void setYvsSynchroDataSynchroList(List<YvsSynchroDataSynchro> yvsSynchroDataSynchroList) {
        this.yvsSynchroDataSynchroList = yvsSynchroDataSynchroList;
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
        if (!(object instanceof YvsSynchroServeurs)) {
            return false;
        }
        YvsSynchroServeurs other = (YvsSynchroServeurs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.synchro.YvsSynchroServeurs[ id=" + id + " ]";
    }

}

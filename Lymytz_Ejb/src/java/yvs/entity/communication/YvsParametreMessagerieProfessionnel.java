/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.communication;

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
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_parametre_messagerie_professionnel")
@NamedQueries({
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findAll", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y"),
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findById", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findByNomDomaine", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y WHERE y.nomDomaine = :nomDomaine"),
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findByPort", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y WHERE y.port = :port"),
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findByAdresse", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y WHERE y.adresse = :adresse"),
    @NamedQuery(name = "YvsParametreMessagerieProfessionnel.findByPassword", query = "SELECT y FROM YvsParametreMessagerieProfessionnel y WHERE y.password = :password")})
public class YvsParametreMessagerieProfessionnel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_parametre_messagerie_professionnel_id_seq", name = "yvs_parametre_messagerie_professionnel_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_parametre_messagerie_professionnel_id_seq_name", strategy = GenerationType.SEQUENCE) 
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
    @Column(name = "nom_domaine")
    private String nomDomaine;
    @Column(name = "port")
    private Integer port;
    @Size(max = 2147483647)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 2147483647)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsParametreMessagerieProfessionnel() {
    }

    public YvsParametreMessagerieProfessionnel(Integer id) {
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

    public String getNomDomaine() {
        return nomDomaine;
    }

    public void setNomDomaine(String nomDomaine) {
        this.nomDomaine = nomDomaine;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(object instanceof YvsParametreMessagerieProfessionnel)) {
            return false;
        }
        YvsParametreMessagerieProfessionnel other = (YvsParametreMessagerieProfessionnel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsParametreMessagerieProfessionnel[ id=" + id + " ]";
    }

}

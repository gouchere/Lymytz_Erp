/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.tiers;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_visiteur_client")
@NamedQueries({
    @NamedQuery(name = "YvsBaseVisiteurClient.findAll", query = "SELECT y FROM YvsBaseVisiteurClient y"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findById", query = "SELECT y FROM YvsBaseVisiteurClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findByVisiteurSociete", query = "SELECT y FROM YvsBaseVisiteurClient y WHERE y.visiteur = :visiteur AND y.client.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findClientByVisiteurSociete", query = "SELECT DISTINCT y.client FROM YvsBaseVisiteurClient y WHERE y.visiteur = :visiteur AND y.client.tiers.societe = :societe"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findIdClientByVisiteur", query = "SELECT DISTINCT y.client.id FROM YvsBaseVisiteurClient y WHERE y.visiteur = :visiteur"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findByVisiteur", query = "SELECT y FROM YvsBaseVisiteurClient y WHERE y.visiteur = :visiteur"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findByDateSave", query = "SELECT y FROM YvsBaseVisiteurClient y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseVisiteurClient.findByDateUpdate", query = "SELECT y FROM YvsBaseVisiteurClient y WHERE y.dateUpdate = :dateUpdate")})
public class YvsBaseVisiteurClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_visiteur_client_id_seq", name = "yvs_base_visiteur_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_visiteur_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private YvsComClient client;
    @JoinColumn(name = "visiteur", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseVisiteur visiteur;

    public YvsBaseVisiteurClient() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseVisiteurClient(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsBaseVisiteur getVisiteur() {
        return visiteur;
    }

    public void setVisiteur(YvsBaseVisiteur visiteur) {
        this.visiteur = visiteur;
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
        if (!(object instanceof YvsBaseVisiteurClient)) {
            return false;
        }
        YvsBaseVisiteurClient other = (YvsBaseVisiteurClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.tiers.YvsBaseVisiteurClient[ id=" + id + " ]";
    }

}

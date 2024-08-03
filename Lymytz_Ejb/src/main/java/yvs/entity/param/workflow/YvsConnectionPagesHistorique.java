/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_connection_pages_historique")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsConnectionPagesHistorique.findAll", query = "SELECT y FROM YvsConnectionPagesHistorique y"),
    @NamedQuery(name = "YvsConnectionPagesHistorique.findById", query = "SELECT y FROM YvsConnectionPagesHistorique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsConnectionPagesHistorique.findByTitrePage", query = "SELECT y FROM YvsConnectionPagesHistorique y WHERE y.titrePage = :titrePage"),
    @NamedQuery(name = "YvsConnectionPagesHistorique.findByDateOuverture", query = "SELECT y FROM YvsConnectionPagesHistorique y WHERE y.dateOuverture = :dateOuverture"),
    @NamedQuery(name = "YvsConnectionPagesHistorique.findByAuthor", query = "SELECT y FROM YvsConnectionPagesHistorique y WHERE y.author = :author")})
public class YvsConnectionPagesHistorique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "titre_page")
    private String titrePage;
    @Column(name = "date_ouverture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOuverture;
    @Column(name = "author")
    private BigInteger author;
    @JoinColumn(name = "auteur_page", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsConnectionHistorique auteurPage;

    public YvsConnectionPagesHistorique() {
    }

    public YvsConnectionPagesHistorique(Long id) {
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

    public String getTitrePage() {
        return titrePage;
    }

    public void setTitrePage(String titrePage) {
        this.titrePage = titrePage;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public BigInteger getAuthor() {
        return author;
    }

    public void setAuthor(BigInteger author) {
        this.author = author;
    }

    public YvsConnectionHistorique getAuteurPage() {
        return auteurPage;
    }

    public void setAuteurPage(YvsConnectionHistorique auteurPage) {
        this.auteurPage = auteurPage;
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
        if (!(object instanceof YvsConnectionPagesHistorique)) {
            return false;
        }
        YvsConnectionPagesHistorique other = (YvsConnectionPagesHistorique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsConnectionPagesHistorique[ id=" + id + " ]";
    }

}

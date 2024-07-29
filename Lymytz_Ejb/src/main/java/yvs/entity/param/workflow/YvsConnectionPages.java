/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_connection_pages")
@NamedQueries({
    @NamedQuery(name = "YvsConnectionPages.findAll", query = "SELECT y FROM YvsConnectionPages y"),
    @NamedQuery(name = "YvsConnectionPages.findById", query = "SELECT y FROM YvsConnectionPages y WHERE y.id = :id"),
    @NamedQuery(name = "YvsConnectionPages.findByTitrePage", query = "SELECT y FROM YvsConnectionPages y WHERE y.titrePage = :titrePage"),
    @NamedQuery(name = "YvsConnectionPages.findByDateOuverture", query = "SELECT y FROM YvsConnectionPages y WHERE y.dateOuverture = :dateOuverture"),
    @NamedQuery(name = "YvsConnectionPages.findByAuthor", query = "SELECT y FROM YvsConnectionPages y WHERE y.author = :author")})
public class YvsConnectionPages implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_connection_pages_id_seq", name = "yvs_connection_pages_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_connection_pages_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre_page")
    private String titrePage;
    @Column(name = "date_ouverture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOuverture;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "auteur_page", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsConnection auteurPage;

    public YvsConnectionPages() {
    }

    public YvsConnectionPages(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsConnection getAuteurPage() {
        return auteurPage;
    }

    public void setAuteurPage(YvsConnection auteurPage) {
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
        if (!(object instanceof YvsConnectionPages)) {
            return false;
        }
        YvsConnectionPages other = (YvsConnectionPages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsConnectionPages[ id=" + id + " ]";
    }

}

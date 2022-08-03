/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_autorisation_ressources_page")
@NamedQueries({
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findAll", query = "SELECT y FROM YvsAutorisationRessourcesPage y"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findByNiveauAcces", query = "SELECT y FROM YvsAutorisationRessourcesPage y JOIN FETCH y.ressourcePage WHERE y.niveauAcces = :niveauAcces"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findByRessourcePage", query = "SELECT y FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage = :ressourcePage"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.countRessource", query = "SELECT COUNT(y) FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage.pageModule = :page AND y.niveauAcces = :niveau AND y.acces = true"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findByRessourceNiveau", query = "SELECT y FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage = :ressource AND y.niveauAcces = :niveau"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findByrefRessourcePage", query = "SELECT y.niveauAcces.id FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage.referenceRessource=:reference"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findAccesRessource", query = "SELECT y.acces FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage.referenceRessource=:reference AND y.niveauAcces=:niveau"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findAccesRessource_", query = "SELECT y FROM YvsAutorisationRessourcesPage y WHERE y.ressourcePage.referenceRessource=:reference AND y.niveauAcces=:niveau"),
    @NamedQuery(name = "YvsAutorisationRessourcesPage.findByAcces", query = "SELECT y FROM YvsAutorisationRessourcesPage y WHERE y.acces = :acces")})
public class YvsAutorisationRessourcesPage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_autorisation_ressources_page_id_seq", name = "yvs_autorisation_ressources_page_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_autorisation_ressources_page_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "acces")
    private Boolean acces;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "ressource_page", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsRessourcesPage ressourcePage;
    @JoinColumn(name = "niveau_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveauAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsAutorisationRessourcesPage() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsAutorisationRessourcesPage(Long id) {
        this();
        this.id = id;
    }

    public YvsAutorisationRessourcesPage(YvsNiveauAcces niveauAcces, YvsRessourcesPage ressourcePage) {
        this();
        this.niveauAcces = niveauAcces;
        this.ressourcePage = ressourcePage;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAcces() {
        return acces != null ? acces : false;
    }

    public void setAcces(Boolean acces) {
        this.acces = acces;
    }

    public YvsRessourcesPage getRessourcePage() {
        return ressourcePage;
    }

    public void setRessourcePage(YvsRessourcesPage ressourcePage) {
        this.ressourcePage = ressourcePage;
    }

    public YvsNiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(YvsNiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsAutorisationRessourcesPage other = (YvsAutorisationRessourcesPage) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationRessourcesPage[ id=" + id + " ]";
    }

}

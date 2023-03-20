/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_session_of")
@NamedQueries({
    @NamedQuery(name = "YvsProdSessionOf.findAll", query = "SELECT y FROM YvsProdSessionOf y"),
    @NamedQuery(name = "YvsProdSessionOf.findIdSessByOrdre", query = "SELECT y.sessionProd.id  FROM YvsProdSessionOf y WHERE y.ordre = :ordre "),
    @NamedQuery(name = "YvsProdSessionOf.findDateByOrdre_", query = "SELECT DISTINCT y.sessionProd.dateSession, y.sessionProd.tranche FROM YvsProdSessionOf y WHERE y.ordre = :ordre ORDER BY y.sessionProd.dateSession"),
    @NamedQuery(name = "YvsProdSessionOf.findDateByOrdre", query = "SELECT DISTINCT y.sessionProd.dateSession, y.sessionProd.tranche FROM YvsProdSessionOf y WHERE y.ordre = :ordre AND y.sessionProd.dateSession BETWEEN :date1 AND :date2 ORDER BY y.sessionProd.dateSession"),
    @NamedQuery(name = "YvsProdSessionOf.findDateByProducteurOrdre_", query = "SELECT DISTINCT y.sessionProd.dateSession, y.sessionProd.tranche FROM YvsProdSessionOf y WHERE y.ordre = :ordre AND y.sessionProd.producteur=:producteur ORDER BY y.sessionProd.dateSession"),
    @NamedQuery(name = "YvsProdSessionOf.findDateByProducteurOrdre", query = "SELECT DISTINCT y.sessionProd.dateSession, y.sessionProd.tranche FROM YvsProdSessionOf y WHERE y.ordre = :ordre AND y.sessionProd.producteur=:producteur AND y.sessionProd.dateSession BETWEEN :date1 AND :date2 ORDER BY y.sessionProd.dateSession"),
    @NamedQuery(name = "YvsProdSessionOf.findEquipeByOrdre", query = "SELECT DISTINCT y.sessionProd.equipe FROM YvsProdSessionOf y WHERE y.ordre = :ordre  ORDER BY y.sessionProd.equipe.reference"),
    @NamedQuery(name = "YvsProdSessionOf.findEquipeProducteurOrdre", query = "SELECT DISTINCT y.sessionProd.equipe FROM YvsProdSessionOf y WHERE y.ordre = :ordre AND y.sessionProd.producteur=:producteur  ORDER BY y.sessionProd.equipe.reference"),
    @NamedQuery(name = "YvsProdSessionOf.findById", query = "SELECT y FROM YvsProdSessionOf y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdSessionOf.findOne", query = "SELECT y FROM YvsProdSessionOf y WHERE y.ordre=:ordre AND y.sessionProd=:session"),
    
    @NamedQuery(name = "YvsProdSessionOf.findIdProdByUsersDates", query = "SELECT DISTINCT(y.ordre.id) FROM YvsProdSessionOf y WHERE y.sessionProd.producteur = :producteur AND y.sessionProd.dateSession BETWEEN :dateDebut AND :dateFin"),
    
    @NamedQuery(name = "YvsProdSessionOf.findByDateSave", query = "SELECT y FROM YvsProdSessionOf y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdSessionOf.findByDateUpdate", query = "SELECT y FROM YvsProdSessionOf y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdSessionOf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_session_of_id_seq", name = "yvs_prod_session_of_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_session_of_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_save")
    @Temporal(TemporalType.DATE)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordre", referencedColumnName = "id")
    private YvsProdOrdreFabrication ordre;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_prod", referencedColumnName = "id")
    private YvsProdSessionProd sessionProd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "sessionOf")
    private List<YvsProdSuiviOperations> operations;

    public YvsProdSessionOf() {
    }

    public YvsProdSessionOf(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
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

    public YvsProdOrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(YvsProdOrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdSessionProd getSessionProd() {
        return sessionProd;
    }

    public void setSessionProd(YvsProdSessionProd sessionProd) {
        this.sessionProd = sessionProd;
    }

    public List<YvsProdSuiviOperations> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsProdSuiviOperations> operations) {
        this.operations = operations;
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
        if (!(object instanceof YvsProdSessionOf)) {
            return false;
        }
        YvsProdSessionOf other = (YvsProdSessionOf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdSessionOf[ id=" + id + " ]";
    }

}

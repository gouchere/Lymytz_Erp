/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_conditionnement_declaration")
@NamedQueries({
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findAll", query = "SELECT y FROM YvsProdConditionnementDeclaration y"),
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findById", query = "SELECT y FROM YvsProdConditionnementDeclaration y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findByDateSave", query = "SELECT y FROM YvsProdConditionnementDeclaration y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findByDateUpdate", query = "SELECT y FROM YvsProdConditionnementDeclaration y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsProdConditionnementDeclaration.findDeclarationByFiche", query = "SELECT y.declaration FROM YvsProdConditionnementDeclaration y WHERE y.conditionnement = :fiche"),
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findyDeclaration", query = "SELECT y FROM YvsProdConditionnementDeclaration y WHERE y.declaration = :declaration"),
    @NamedQuery(name = "YvsProdConditionnementDeclaration.findFicheByDeclaration", query = "SELECT DISTINCT y.conditionnement.id FROM YvsProdConditionnementDeclaration y WHERE y.declaration = :declaration")})
public class YvsProdConditionnementDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_conditionnement_declaration_id_seq", name = "yvs_prod_conditionnement_declaration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_conditionnement_declaration_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne
    private YvsProdFicheConditionnement conditionnement;
    @JoinColumn(name = "declaration", referencedColumnName = "id")
    @ManyToOne
    private YvsProdDeclarationProduction declaration;
    
    @Transient
    private boolean new_;

    public YvsProdConditionnementDeclaration() {

    }

    public YvsProdConditionnementDeclaration(Long id) {
        this();
        this.id = id;
    }

    public YvsProdConditionnementDeclaration(YvsProdFicheConditionnement conditionnement, YvsProdDeclarationProduction declaration) {
        this();
        this.conditionnement = conditionnement;
        this.declaration = declaration;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
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

    public YvsProdFicheConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsProdFicheConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsProdDeclarationProduction getDeclaration() {
        return declaration;
    }

    public void setDeclaration(YvsProdDeclarationProduction declaration) {
        this.declaration = declaration;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsProdConditionnementDeclaration)) {
            return false;
        }
        YvsProdConditionnementDeclaration other = (YvsProdConditionnementDeclaration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdConditionnementDeclaration[ id=" + id + " ]";
    }

}

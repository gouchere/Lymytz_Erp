/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_indicateur_op")
@NamedQueries({
    @NamedQuery(name = "YvsProdIndicateurOp.findAll", query = "SELECT y FROM YvsProdIndicateurOp y"),
    @NamedQuery(name = "YvsProdIndicateurOp.findById", query = "SELECT y FROM YvsProdIndicateurOp y WHERE y.id = :id")
})
public class YvsProdIndicateurOp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_indicateur_phase_id_seq", name = "yvs_prod_indicateur_phase_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_indicateur_phase_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "type_indicateur")
    private String typeIndicateur;  //QUALITATIF OU QUANTITATIF
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "composant_op", referencedColumnName = "id")
    @ManyToOne
    private YvsProdComposantOp composantOp;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "indicateur")
    private List<YvsProdValeursQualitative> valeurs;

    @Transient
    private boolean selectActif;

    public YvsProdIndicateurOp() {
    }

    public YvsProdIndicateurOp(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
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

    public String getTypeIndicateur() {
        return typeIndicateur;
    }

    public void setTypeIndicateur(String typeIndicateur) {
        this.typeIndicateur = typeIndicateur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsProdComposantOp getComposantOp() {
        return composantOp;
    }

    public void setComposantOp(YvsProdComposantOp composantOp) {
        this.composantOp = composantOp;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsProdValeursQualitative> getValeurs() {
        return valeurs;
    }

    public void setValeurs(List<YvsProdValeursQualitative> valeurs) {
        this.valeurs = valeurs;
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
        if (!(object instanceof YvsProdIndicateurOp)) {
            return false;
        }
        YvsProdIndicateurOp other = (YvsProdIndicateurOp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdIndicateurPhase[ id=" + id + " ]";
    }

}

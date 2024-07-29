/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.rrr;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_grille_rabais")
@NamedQueries({
    @NamedQuery(name = "YvsComGrilleRabais.findAll", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.rabais.article.article.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsComGrilleRabais.findById", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComGrilleRabais.findByRabais", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.rabais = :rabais"),
    @NamedQuery(name = "YvsComGrilleRabais.findByRabaisMontant", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.rabais = :rabais AND :montant BETWEEN y.montantMinimal AND y.montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRabais.findByRabaisMontantBase", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.rabais = :rabais AND ((y.base = :base1 AND :valeur1 BETWEEN y.montantMinimal AND y.montantMaximal) OR (y.base = :base2 AND :valeur2 BETWEEN y.montantMinimal AND y.montantMaximal))"),
    @NamedQuery(name = "YvsComGrilleRabais.findByMontantMinimal", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsComGrilleRabais.findByMontantMaximal", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsComGrilleRabais.findByMontantRabais", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.montantRabais = :montantRabais"),
    @NamedQuery(name = "YvsComGrilleRabais.findByNatureMontant", query = "SELECT y FROM YvsComGrilleRabais y WHERE y.natureMontant = :natureMontant"),
    @NamedQuery(name = "YvsComGrilleRabais.findByBaseRabais", query = "SELECT DISTINCT(y.rabais) FROM YvsComGrilleRabais y WHERE y.base = :baseRabais AND y.rabais.article.article.point.agence.societe = :societe")})

public class YvsComGrilleRabais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_grille_rabais_id_seq", name = "yvs_com_grille_rabais_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_grille_rabais_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "montant_rabais")
    private Double montantRabais;
    @Size(max = 2147483647)
    @Column(name = "nature_montant")
    private String natureMontant;
    @Size(max = 2147483647)
    @Column(name = "base")
    private String base;
    @JoinColumn(name = "rabais", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComRabais rabais;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;
    @Transient
    private boolean unique;

    public YvsComGrilleRabais() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComGrilleRabais(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public String getBase() {
        return base != null ? (base.trim().length() > 0 ? base : Constantes.BASE_QTE) : Constantes.BASE_QTE;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public boolean isUnique() {
        unique = ((getMontantMinimal() <= 0) && (getMontantMaximal() >= Double.MAX_VALUE || getMontantMaximal() <= 0));
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getMontantMinimal() {
        return montantMinimal != null ? montantMinimal : 0;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Double getMontantRabais() {
        return montantRabais != null ? montantRabais : 0;
    }

    public void setMontantRabais(Double montantRabais) {
        this.montantRabais = montantRabais;
    }

    public String getNatureMontant() {
        return natureMontant != null ? (natureMontant.trim().length() > 0 ? natureMontant : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
    }

    public YvsComRabais getRabais() {
        return rabais;
    }

    public void setRabais(YvsComRabais rabais) {
        this.rabais = rabais;
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
        if (!(object instanceof YvsComGrilleRabais)) {
            return false;
        }
        YvsComGrilleRabais other = (YvsComGrilleRabais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.rrr.YvsComGrilleRabais[ id=" + id + " ]";
    }

}

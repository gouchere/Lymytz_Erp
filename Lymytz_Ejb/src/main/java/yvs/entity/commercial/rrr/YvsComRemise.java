/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.rrr;

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
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_remise")
@NamedQueries({
    @NamedQuery(name = "YvsComRemise.findAll", query = "SELECT y FROM YvsComRemise y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsComRemise.findById", query = "SELECT y FROM YvsComRemise y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRemise.findByReference", query = "SELECT y FROM YvsComRemise y WHERE y.refRemise = :reference"),
    @NamedQuery(name = "YvsComRemise.findByReference_", query = "SELECT y FROM YvsComRemise y WHERE y.refRemise = :reference AND y.societe = :societe"),
    @NamedQuery(name = "YvsComRemise.findByPermanent", query = "SELECT y FROM YvsComRemise y WHERE y.permanent = :permanent"),
    @NamedQuery(name = "YvsComRemise.findByActif", query = "SELECT y FROM YvsComRemise y WHERE y.actif = :actif AND y.societe=:societe"),
    @NamedQuery(name = "YvsComRemise.findByDateDebut", query = "SELECT y FROM YvsComRemise y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComRemise.findByDateFin", query = "SELECT y FROM YvsComRemise y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsComRemise.findAllReference", query = "SELECT y.refRemise FROM YvsComRemise y WHERE y.societe = :societe")})
public class YvsComRemise implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_remise_id_seq", name = "yvs_com_remise_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_remise_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "ref_remise")
    private String refRemise;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "remise", fetch =FetchType.LAZY)
    private List<YvsComGrilleRemise> grilles;
    
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;

    public YvsComRemise() {
    }

    public YvsComRemise(Long id) {
        this.id = id;
    }

    public YvsComRemise(Long id, String reference) {
        this.id = id;
        this.refRemise = reference;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefRemise() {
        return refRemise;
    }

    public void setRefRemise(String refRemise) {
        this.refRemise = refRemise;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public List<YvsComGrilleRemise> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsComGrilleRemise> grilles) {
        this.grilles = grilles;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
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
        if (!(object instanceof YvsComRemise)) {
            return false;
        }
        YvsComRemise other = (YvsComRemise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.rrr.YvsComRemise[ id=" + id + " ]";
    }

}

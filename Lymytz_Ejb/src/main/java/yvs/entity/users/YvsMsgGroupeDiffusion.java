/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_msg_groupe_diffusion")
@NamedQueries({
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findAll", query = "SELECT y FROM YvsMsgGroupeDiffusion y"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findById", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findByUsers", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.users = :users AND y.publics = false"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findBySociete", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.societe = :societe AND y.publics = true"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findByReference", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findByLibelleUsers", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.users = :users AND y.libelle = :libelle AND y.publics = false"),
    @NamedQuery(name = "YvsMsgGroupeDiffusion.findByLibelle", query = "SELECT y FROM YvsMsgGroupeDiffusion y WHERE y.libelle = :libelle")})
public class YvsMsgGroupeDiffusion implements Serializable {

    @Column(name = "publics")
    private Boolean publics;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_groupes_diffusion_id_seq", name = "yvs_groupes_diffusion_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_groupes_diffusion_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @OneToMany(mappedBy = "groupeDiffusion")
    private List<YvsMsgDiffusionContact> yvsDiffusionContactList;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;

    public YvsMsgGroupeDiffusion() {
    }

    public YvsMsgGroupeDiffusion(Integer id) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<YvsMsgDiffusionContact> getYvsDiffusionContactList() {
        return yvsDiffusionContactList;
    }

    public void setYvsDiffusionContactList(List<YvsMsgDiffusionContact> yvsDiffusionContactList) {
        this.yvsDiffusionContactList = yvsDiffusionContactList;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public Boolean getPublics() {
        return publics;
    }

    public void setPublics(Boolean publics) {
        this.publics = publics;
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
        if (!(object instanceof YvsMsgGroupeDiffusion)) {
            return false;
        }
        YvsMsgGroupeDiffusion other = (YvsMsgGroupeDiffusion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsGroupeDiffusion[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

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

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_msg_diffusion_contact")
@NamedQueries({
    @NamedQuery(name = "YvsDiffusionContact.findAll", query = "SELECT y FROM YvsMsgDiffusionContact y"),
    @NamedQuery(name = "YvsDiffusionContact.findById", query = "SELECT y FROM YvsMsgDiffusionContact y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDiffusionContact.findByActif", query = "SELECT y FROM YvsMsgDiffusionContact y WHERE y.actif = :actif")})
public class YvsMsgDiffusionContact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_diffusion_contact_id_seq", name = "yvs_diffusion_contact_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_diffusion_contact_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "groupe_diffusion", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgGroupeDiffusion groupeDiffusion;

    public YvsMsgDiffusionContact() {
    }

    public YvsMsgDiffusionContact(Long id) {
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsMsgGroupeDiffusion getGroupeDiffusion() {
        return groupeDiffusion;
    }

    public void setGroupeDiffusion(YvsMsgGroupeDiffusion groupeDiffusion) {
        this.groupeDiffusion = groupeDiffusion;
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
        if (!(object instanceof YvsMsgDiffusionContact)) {
            return false;
        }
        YvsMsgDiffusionContact other = (YvsMsgDiffusionContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsDiffusionContact[ id=" + id + " ]";
    }

}

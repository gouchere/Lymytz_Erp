/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.communication;

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
import yvs.entity.users.YvsUsers;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_msg_groupe_message")
@NamedQueries({
    @NamedQuery(name = "YvsMsgGroupeMessage.findAll", query = "SELECT y FROM YvsMsgGroupeMessage y"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findById", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByIdAndParent", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.parent.id = :id OR y.id =:id"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByUsers", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.users = :users"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByLibelleUsers", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.users = :users AND  y.libelle = :libelle"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByParent", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.parent = :parent"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByReference", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsMsgGroupeMessage.findByLibelle", query = "SELECT y FROM YvsMsgGroupeMessage y WHERE y.libelle = :libelle")})
public class YvsMsgGroupeMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_groupe_message_id_seq", name = "yvs_groupe_message_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_groupe_message_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgGroupeMessage parent;

    public YvsMsgGroupeMessage() {
    }

    public YvsMsgGroupeMessage(Integer id) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsMsgGroupeMessage getParent() {
        return parent;
    }

    public void setParent(YvsMsgGroupeMessage parent) {
        this.parent = parent;
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
        if (!(object instanceof YvsMsgGroupeMessage)) {
            return false;
        }
        YvsMsgGroupeMessage other = (YvsMsgGroupeMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsMsgGroupeMessage[ id=" + id + " ]";
    }

}

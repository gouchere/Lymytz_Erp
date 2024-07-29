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
@Table(name = "yvs_msg_filtre_message")
@NamedQueries({
    @NamedQuery(name = "YvsMsgFiltreMessage.findAll", query = "SELECT y FROM YvsMsgFiltreMessage y"),
    @NamedQuery(name = "YvsMsgFiltreMessage.findById", query = "SELECT y FROM YvsMsgFiltreMessage y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMsgFiltreMessage.findByBase", query = "SELECT y FROM YvsMsgFiltreMessage y WHERE y.base = :base"),
    @NamedQuery(name = "YvsMsgFiltreMessage.findByUser", query = "SELECT y FROM YvsMsgFiltreMessage y WHERE y.users = :users"),
    @NamedQuery(name = "YvsMsgFiltreMessage.findByValeur", query = "SELECT y FROM YvsMsgFiltreMessage y WHERE y.valeur = :valeur")})
public class YvsMsgFiltreMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_msg_filtre_message_id_seq", name = "yvs_msg_filtre_message_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_msg_filtre_message_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "base")
    private String base;
    @Size(max = 2147483647)
    @Column(name = "valeur")
    private String valeur;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "groupe_message", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgGroupeMessage groupeMessage;

    public YvsMsgFiltreMessage() {
    }

    public YvsMsgFiltreMessage(Integer id) {
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

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public YvsMsgGroupeMessage getGroupeMessage() {
        return groupeMessage;
    }

    public void setGroupeMessage(YvsMsgGroupeMessage groupeMessage) {
        this.groupeMessage = groupeMessage;
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
        if (!(object instanceof YvsMsgFiltreMessage)) {
            return false;
        }
        YvsMsgFiltreMessage other = (YvsMsgFiltreMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsMsgFiltreMessage[ id=" + id + " ]";
    }

}

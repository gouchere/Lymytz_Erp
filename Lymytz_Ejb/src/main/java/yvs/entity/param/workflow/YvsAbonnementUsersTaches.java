/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import yvs.entity.grh.taches.YvsGrhTaches;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_abonnement_users_taches")
@NamedQueries({
    @NamedQuery(name = "YvsAbonnementUsersTaches.findAll", query = "SELECT y FROM YvsAbonnementUsersTaches y"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByUser", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.yvsAbonnementUsersTachesPK.users = :user"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByTaches", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.yvsAbonnementUsersTachesPK.taches = :taches"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByAjout", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.ajout = :ajout"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByModif", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.modif = :modif"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByMove", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.move = :move"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByMessageAjout", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.messageAjout = :messageAjout"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByMessageModif", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.messageModif = :messageModif"),
    @NamedQuery(name = "YvsAbonnementUsersTaches.findByMessageRemove", query = "SELECT y FROM YvsAbonnementUsersTaches y WHERE y.messageRemove = :messageRemove")})
public class YvsAbonnementUsersTaches implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsAbonnementUsersTachesPK yvsAbonnementUsersTachesPK;
    @Column(name = "ajout")
    private boolean ajout;
    @Column(name = "modif")
    private boolean modif;
    @Column(name = "move")
    private boolean move;
    @Size(max = 2147483647)
    @Column(name = "message_ajout")
    private String messageAjout;
    @Size(max = 2147483647)
    @Column(name = "message_modif")
    private String messageModif;
    @Size(max = 2147483647)
    @Column(name = "message_remove")
    private String messageRemove;
    @JoinColumn(name = "taches", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTaches yvsTaches;
    @JoinColumn(name = "users", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers yvsUsers;

    public YvsAbonnementUsersTaches() {
    }

    public YvsAbonnementUsersTaches(YvsAbonnementUsersTachesPK yvsAbonnementUsersTachesPK) {
        this.yvsAbonnementUsersTachesPK = yvsAbonnementUsersTachesPK;
    }

    public YvsAbonnementUsersTaches(long user, long taches) {
        this.yvsAbonnementUsersTachesPK = new YvsAbonnementUsersTachesPK(user, taches);
    }

    public YvsAbonnementUsersTachesPK getYvsAbonnementUsersTachesPK() {
        return yvsAbonnementUsersTachesPK;
    }

    public void setYvsAbonnementUsersTachesPK(YvsAbonnementUsersTachesPK yvsAbonnementUsersTachesPK) {
        this.yvsAbonnementUsersTachesPK = yvsAbonnementUsersTachesPK;
    }

    public boolean isAjout() {
        return ajout;
    }

    public void setAjout(Boolean ajout) {
        this.ajout = ajout;
    }

    public boolean isModif() {
        return modif;
    }

    public void setModif(Boolean modif) {
        this.modif = modif;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(Boolean move) {
        this.move = move;
    }

    public String getMessageAjout() {
        return messageAjout;
    }

    public void setMessageAjout(String messageAjout) {
        this.messageAjout = messageAjout;
    }

    public String getMessageModif() {
        return messageModif;
    }

    public void setMessageModif(String messageModif) {
        this.messageModif = messageModif;
    }

    public String getMessageRemove() {
        return messageRemove;
    }

    public void setMessageRemove(String messageRemove) {
        this.messageRemove = messageRemove;
    }

    public YvsGrhTaches getYvsTaches() {
        return yvsTaches;
    }

    public void setYvsTaches(YvsGrhTaches yvsTaches) {
        this.yvsTaches = yvsTaches;
    }

    public YvsUsers getYvsUsers() {
        return yvsUsers;
    }

    public void setYvsUsers(YvsUsers yvsUsers) {
        this.yvsUsers = yvsUsers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsAbonnementUsersTachesPK != null ? yvsAbonnementUsersTachesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsAbonnementUsersTaches)) {
            return false;
        }
        YvsAbonnementUsersTaches other = (YvsAbonnementUsersTaches) object;
        if ((this.yvsAbonnementUsersTachesPK == null && other.yvsAbonnementUsersTachesPK != null) || (this.yvsAbonnementUsersTachesPK != null && !this.yvsAbonnementUsersTachesPK.equals(other.yvsAbonnementUsersTachesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsAbonnementUsersTaches[ yvsAbonnementUsersTachesPK=" + yvsAbonnementUsersTachesPK + " ]";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author GOUCHERE YVES
 */
@Embeddable
public class YvsAbonnementUsersTachesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "users")
    private long users;
    @Basic(optional = false)
    @Column(name = "taches")
    private long taches;

    public YvsAbonnementUsersTachesPK() {
    }

    public YvsAbonnementUsersTachesPK(long user, long taches) {
        this.users = user;
        this.taches = taches;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long user) {
        this.users = user;
    }

    public long getTaches() {
        return taches;
    }

    public void setTaches(long taches) {
        this.taches = taches;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) users;
        hash += (int) taches;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsAbonnementUsersTachesPK)) {
            return false;
        }
        YvsAbonnementUsersTachesPK other = (YvsAbonnementUsersTachesPK) object;
        if (this.users != other.users) {
            return false;
        }
        if (this.taches != other.taches) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsAbonnementUsersTachesPK[ user=" + users + ", taches=" + taches + " ]";
    }
    
}

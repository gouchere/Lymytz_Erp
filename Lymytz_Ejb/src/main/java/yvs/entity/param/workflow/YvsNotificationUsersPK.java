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
public class YvsNotificationUsersPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "users")
    private long users;
    @Basic(optional = false)
    @Column(name = "notification")
    private long notification;

    public YvsNotificationUsersPK() {
    }

    public YvsNotificationUsersPK(long users, long notification) {
        this.users = users;
        this.notification = notification;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getNotification() {
        return notification;
    }

    public void setNotification(long notification) {
        this.notification = notification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) users;
        hash += (int) notification;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsNotificationUsersPK)) {
            return false;
        }
        YvsNotificationUsersPK other = (YvsNotificationUsersPK) object;
        if (this.users != other.users) {
            return false;
        }
        if (this.notification != other.notification) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsNotificationUsersPK[ users=" + users + ", notification=" + notification + " ]";
    }
    
}

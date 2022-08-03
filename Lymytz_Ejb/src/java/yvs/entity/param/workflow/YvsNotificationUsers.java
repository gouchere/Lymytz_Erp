/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_notification_users")
@NamedQueries({
    @NamedQuery(name = "YvsNotificationUsers.findAll", query = "SELECT y FROM YvsNotificationUsers y"),
    @NamedQuery(name = "YvsNotificationUsers.findByUsers", query = "SELECT y FROM YvsNotificationUsers y JOIN y.yvsNotifications n WHERE y.yvsNotificationUsersPK.users = :users ORDER BY n.dateNotif DESC, n.heureNotif DESC, y.vue DESC"),
    @NamedQuery(name = "YvsNotificationUsers.findByNotification", query = "SELECT y FROM YvsNotificationUsers y WHERE y.yvsNotificationUsersPK.notification = :notification"),
    @NamedQuery(name = "YvsNotificationUsers.findByVue", query = "SELECT y FROM YvsNotificationUsers y WHERE y.vue = :vue")})
public class YvsNotificationUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected YvsNotificationUsersPK yvsNotificationUsersPK;
    @Column(name = "vue")
    private boolean vue;
    @Size(max = 2147483647)
    @Column(name = "message")
    private String message;
    @JoinColumn(name = "notification", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNotifications yvsNotifications;
    @JoinColumn(name = "users", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers yvsUsers;

    public YvsNotificationUsers() {
    }

    public YvsNotificationUsers(YvsNotificationUsersPK yvsNotificationUsersPK) {
        this.yvsNotificationUsersPK = yvsNotificationUsersPK;
    }

    public YvsNotificationUsers(long users, long notification) {
        this.yvsNotificationUsersPK = new YvsNotificationUsersPK(users, notification);
    }

    public YvsNotificationUsersPK getYvsNotificationUsersPK() {
        return yvsNotificationUsersPK;
    }

    public void setYvsNotificationUsersPK(YvsNotificationUsersPK yvsNotificationUsersPK) {
        this.yvsNotificationUsersPK = yvsNotificationUsersPK;
    }

    public boolean isVue() {
        return vue;
    }

    public void setVue(boolean vue) {
        this.vue = vue;
    }

    public YvsNotifications getYvsNotifications() {
        return yvsNotifications;
    }

    public void setYvsNotifications(YvsNotifications yvsNotifications) {
        this.yvsNotifications = yvsNotifications;
    }

    public YvsUsers getYvsUsers() {
        return yvsUsers;
    }

    public void setYvsUsers(YvsUsers yvsUsers) {
        this.yvsUsers = yvsUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yvsNotificationUsersPK != null ? yvsNotificationUsersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsNotificationUsers)) {
            return false;
        }
        YvsNotificationUsers other = (YvsNotificationUsers) object;
        if ((this.yvsNotificationUsersPK == null && other.yvsNotificationUsersPK != null) || (this.yvsNotificationUsersPK != null && !this.yvsNotificationUsersPK.equals(other.yvsNotificationUsersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsNotificationUsers[ yvsNotificationUsersPK=" + yvsNotificationUsersPK + " ]";
    }
}

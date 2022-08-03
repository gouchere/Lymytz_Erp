/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_notifications")
@NamedQueries({
    @NamedQuery(name = "YvsNotifications.findAll", query = "SELECT y FROM YvsNotifications y"),
    @NamedQuery(name = "YvsNotifications.findById", query = "SELECT y FROM YvsNotifications y WHERE y.id = :id"),
    @NamedQuery(name = "YvsNotifications.findByTitre", query = "SELECT y FROM YvsNotifications y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsNotifications.findByPorte", query = "SELECT y FROM YvsNotifications y WHERE y.porte = :porte")})
public class YvsNotifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_notifications_id_seq")
    @SequenceGenerator(sequenceName = "yvs_notifications_id_seq", allocationSize = 1, name = "yvs_notifications_id_seq")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Column(name = "porte")
    private boolean porte;
    @Column(name = "date_notif")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNotif;
    @Column(name = "heure_notif")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date heureNotif;
    @Column(name = "notif")
    private boolean notif;

    public YvsNotifications() {
    }

    public YvsNotifications(Long id) {
        this.id = id;
    }

    public YvsNotifications(Long id, String titre) {
        this.id = id;
        this.titre = titre;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean isPorte() {
        return porte;
    }

    public void setPorte(boolean porte) {
        this.porte = porte;
    }

    public Date getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(Date dateNotif) {
        this.dateNotif = dateNotif;
    }

    public Date getHeureNotif() {
        return heureNotif;
    }

    public void setHeureNotif(Date heureNotif) {
        this.heureNotif = heureNotif;
    }

    public boolean isNotif() {
        return notif;
    }

    public void setNotif(boolean notif) {
        this.notif = notif;
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
        if (!(object instanceof YvsNotifications)) {
            return false;
        }
        YvsNotifications other = (YvsNotifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsNotifications[ id=" + id + " ]";
    }
}

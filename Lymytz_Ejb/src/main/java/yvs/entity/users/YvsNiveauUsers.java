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
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_niveau_users")
@NamedQueries({
    @NamedQuery(name = "YvsNiveauUsers.findAll", query = "SELECT y FROM YvsNiveauUsers y"),
    @NamedQuery(name = "YvsNiveauUsers.findNivoUser", query = "SELECT y FROM YvsNiveauUsers y WHERE y.idUser=:user AND y.idNiveau=:niveau"),
    @NamedQuery(name = "YvsNiveauUsers.findNivoUserInScte", query = "SELECT y FROM YvsNiveauUsers y WHERE y.idUser=:user AND y.idNiveau.societe=:societe"),
    @NamedQuery(name = "YvsNiveauUsers.findUserByNiveau", query = "SELECT DISTINCT y.idUser FROM YvsNiveauUsers y WHERE y.idNiveau = :niveau AND y.idUser.actif=true"),
    @NamedQuery(name = "YvsNiveauUsers.findUserByNiveaux", query = "SELECT y.idUser FROM YvsNiveauUsers y WHERE y.idNiveau.id IN :niveau AND y.idUser.actif=true"),
    @NamedQuery(name = "YvsNiveauUsers.findNiveauByUser", query = "SELECT y.idNiveau FROM YvsNiveauUsers y WHERE y.idUser=:user AND y.idNiveau.societe=:societe"),
    @NamedQuery(name = "YvsNiveauUsers.findNiveauByUserS", query = "SELECT y FROM YvsNiveauUsers y WHERE y.idUser=:user AND y.idNiveau.societe=:societe"),
    @NamedQuery(name = "YvsNiveauUsers.findNiveauByCodeUsers", query = "SELECT DISTINCT y.idNiveau FROM YvsNiveauUsers y WHERE (UPPER(y.idUser.codeUsers) LIKE UPPER(:codeUsers) OR UPPER(y.idUser.nomUsers) LIKE UPPER(:codeUsers)) AND y.idNiveau.societe = :societe"),
    @NamedQuery(name = "YvsNiveauUsers.findNiveauByCodeUsersNotSuper", query = "SELECT DISTINCT y.idNiveau FROM YvsNiveauUsers y WHERE (UPPER(y.idUser.codeUsers) LIKE UPPER(:codeUsers) OR UPPER(y.idUser.nomUsers) LIKE UPPER(:codeUsers)) AND y.idNiveau.superAdmin = false AND y.idNiveau.societe = :societe"),
    @NamedQuery(name = "YvsNiveauUsers.findById", query = "SELECT y FROM YvsNiveauUsers y WHERE y.id = :id")
})
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsNiveauUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_niveau_users_id_seq", name = "yvs_niveau_users_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_niveau_users_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave = new Date();
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers idUser;
    @JoinColumn(name = "id_niveau", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces idNiveau;

    public YvsNiveauUsers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsNiveauUsers(Long id) {
        this();
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsUsers getIdUser() {
        return idUser;
    }

    public void setIdUser(YvsUsers idUser) {
        this.idUser = idUser;
    }

    public YvsNiveauAcces getIdNiveau() {
        return idNiveau;
    }

    public void setIdNiveau(YvsNiveauAcces idNiveau) {
        this.idNiveau = idNiveau;
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
        if (!(object instanceof YvsNiveauUsers)) {
            return false;
        }
        YvsNiveauUsers other = (YvsNiveauUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsNiveauUsers[ id=" + id + " ]";
    }

}

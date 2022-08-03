/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

/**
 *
 * @author Lymytz Dowes
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_societes_connexion")
@NamedQueries({
    @NamedQuery(name = "YvsSocietesConnexion.findAll", query = "SELECT y FROM YvsSocietesConnexion y WHERE y.societe=:societe")}
)
public class YvsSocietesConnexion extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "SERIAL")
    @SequenceGenerator(sequenceName = "yvs_societes_connexion_id_seq", name = "yvs_societes_connexion_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_societes_connexion_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(max = 255)
    @Column(name = "users")
    private String users;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "domain")
    private String domain;
    @Size(max = 255)
    @Column(name = "type_connexion")
    private String typeConnexion;
    @Column(name = "port")
    private Integer port;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsSocietesConnexion() {
        super();
    }

    public YvsSocietesConnexion(Long id) {
        this();
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTypeConnexion() {
        return typeConnexion;
    }

    public void setTypeConnexion(String typeConnexion) {
        this.typeConnexion = typeConnexion;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @Override
    public YvsUsersAgence getAuthor() {
        return null;
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
        if (!(object instanceof YvsSocietesConnexion)) {
            return false;
        }
        YvsSocietesConnexion other = (YvsSocietesConnexion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsSocietesConnexion{"
                + "id=" + id
                + '}';
    }
}

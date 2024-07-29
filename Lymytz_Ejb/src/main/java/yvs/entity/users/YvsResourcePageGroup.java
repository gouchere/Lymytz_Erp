/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.users;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_resource_page_group")
@NamedQueries({
    @NamedQuery(name = "YvsResourcePageGroup.findAll", query = "SELECT y FROM YvsResourcePageGroup y"),
    @NamedQuery(name = "YvsResourcePageGroup.findById", query = "SELECT y FROM YvsResourcePageGroup y WHERE y.id = :id"),
    @NamedQuery(name = "YvsResourcePageGroup.findByDesignation", query = "SELECT y FROM YvsResourcePageGroup y WHERE y.designation = :designation")})
public class YvsResourcePageGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;

    public YvsResourcePageGroup() {
    }

    public YvsResourcePageGroup(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        if (!(object instanceof YvsResourcePageGroup)) {
            return false;
        }
        YvsResourcePageGroup other = (YvsResourcePageGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsResourcePageGroup[ id=" + id + " ]";
    }
    
}

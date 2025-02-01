/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.salaire;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_element_formule_non_interprete")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementFormuleNonInterprete.findAll", query = "SELECT y FROM YvsGrhElementFormuleNonInterprete y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementFormuleNonInterprete.findById", query = "SELECT y FROM YvsGrhElementFormuleNonInterprete y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhElementFormuleNonInterprete.findByCodeFormule", query = "SELECT y FROM YvsGrhElementFormuleNonInterprete y WHERE y.codeFormule = :codeFormule AND y.societe=:societe")})
public class YvsGrhElementFormuleNonInterprete implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "code_formule")
    private String codeFormule;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsGrhElementFormuleNonInterprete() {
    }

    public YvsGrhElementFormuleNonInterprete(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeFormule() {
        return codeFormule;
    }

    public void setCodeFormule(String codeFormule) {
        this.codeFormule = codeFormule;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsGrhElementFormuleNonInterprete)) {
            return false;
        }
        YvsGrhElementFormuleNonInterprete other = (YvsGrhElementFormuleNonInterprete) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhElementFormuleNonInterprete[ id=" + id + " ]";
    }
    
}

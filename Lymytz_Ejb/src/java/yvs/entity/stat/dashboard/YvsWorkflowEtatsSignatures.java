/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.stat.dashboard;

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
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowModelDoc;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_etats_signatures")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowEtatsSignatures.findAll", query = "SELECT y FROM YvsWorkflowEtatsSignatures y"),
    @NamedQuery(name = "YvsWorkflowEtatsSignatures.findById", query = "SELECT y FROM YvsWorkflowEtatsSignatures y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowEtatsSignatures.findByModelDoc", query = "SELECT y FROM YvsWorkflowEtatsSignatures y WHERE y.modelDoc = :modelDoc AND y.societe=:societe")})
public class YvsWorkflowEtatsSignatures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "titre1")
    private String titre1;
    @Size(max = 2147483647)
    @Column(name = "titre2")
    private String titre2;
    @Size(max = 2147483647)
    @Column(name = "titre3")
    private String titre3;
    @Size(max = 2147483647)
    @Column(name = "titre4")
    private String titre4;
    @Size(max = 2147483647)
    @Column(name = "titre5")
    private String titre5;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;
    @JoinColumn(name = "model_doc", referencedColumnName = "id")
    @ManyToOne
    private YvsWorkflowModelDoc modelDoc;

    public YvsWorkflowEtatsSignatures() {
    }

    public YvsWorkflowEtatsSignatures(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre1() {
        return titre1;
    }

    public void setTitre1(String titre1) {
        this.titre1 = titre1;
    }

    public String getTitre2() {
        return titre2;
    }

    public void setTitre2(String titre2) {
        this.titre2 = titre2;
    }

    public String getTitre3() {
        return titre3;
    }

    public void setTitre3(String titre3) {
        this.titre3 = titre3;
    }

    public String getTitre4() {
        return titre4;
    }

    public void setTitre4(String titre4) {
        this.titre4 = titre4;
    }

    public String getTitre5() {
        return titre5;
    }

    public void setTitre5(String titre5) {
        this.titre5 = titre5;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsWorkflowModelDoc getModelDoc() {
        return modelDoc;
    }

    public void setModelDoc(YvsWorkflowModelDoc modelDoc) {
        this.modelDoc = modelDoc;
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
        if (!(object instanceof YvsWorkflowEtatsSignatures)) {
            return false;
        }
        YvsWorkflowEtatsSignatures other = (YvsWorkflowEtatsSignatures) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.stat.dashboard.YvsWorkflowEtatsSignatures[ id=" + id + " ]";
    }

}





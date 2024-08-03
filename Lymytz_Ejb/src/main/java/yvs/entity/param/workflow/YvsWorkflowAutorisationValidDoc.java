/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_autorisation_valid_doc")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findAll", query = "SELECT y FROM YvsWorkflowAutorisationValidDoc y"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findById", query = "SELECT y FROM YvsWorkflowAutorisationValidDoc y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findEtapes", query = "SELECT y FROM YvsWorkflowAutorisationValidDoc y JOIN FETCH y.niveauAcces WHERE y.etapeValide = :etape"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findOrdreStepe", query = "SELECT COALESCE(y.etapeValide.ordreEtape, -2) FROM YvsWorkflowAutorisationValidDoc y WHERE y.etapeValide.documentModel.titreDoc=:document AND y.canNotify=true AND y.niveauAcces=:niveau ORDER BY y.canNotify ASC"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findOrdreStepeByNature", query = "SELECT COALESCE(y.etapeValide.ordreEtape, -2) FROM YvsWorkflowAutorisationValidDoc y WHERE y.etapeValide.documentModel.titreDoc=:document AND y.canNotify=true AND y.niveauAcces=:niveau AND y.etapeValide.nature = :nature ORDER BY y.canNotify ASC"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithType", query = "SELECT COALESCE(y.etapeValide.ordreEtape, -2) FROM YvsWorkflowAutorisationValidDoc y WHERE y.etapeValide.documentModel.titreDoc=:document AND y.canNotify=true AND y.niveauAcces=:niveau AND y.etapeValide.nature = :nature AND y.etapeValide.typeDocDivers IS NOT NULL ORDER BY y.canNotify ASC"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findOrdreStepeByNatureWithOutType", query = "SELECT COALESCE(y.etapeValide.ordreEtape, -2) FROM YvsWorkflowAutorisationValidDoc y WHERE y.etapeValide.documentModel.titreDoc=:document AND y.canNotify=true AND y.niveauAcces=:niveau AND y.etapeValide.nature = :nature AND y.etapeValide.typeDocDivers IS NULL ORDER BY y.canNotify ASC"),
    @NamedQuery(name = "YvsWorkflowAutorisationValidDoc.findByCanValide", query = "SELECT y FROM YvsWorkflowAutorisationValidDoc y WHERE y.canValide = :canValide")})
public class YvsWorkflowAutorisationValidDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_autorisation_valid_doc_id_seq", name = "yvs_workflow_autorisation_valid_doc_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_autorisation_valid_doc_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "can_valide")
    private Boolean canValide;
    @Column(name = "can_notify")
    private Boolean canNotify;
    @JoinColumn(name = "etape_valide", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowEtapeValidation etapeValide;
    @JoinColumn(name = "niveau_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveauAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsWorkflowAutorisationValidDoc() {
    }

    public YvsWorkflowAutorisationValidDoc(Long id) {
        this.id = id;
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

    public Boolean getCanValide() {
        return canValide != null ? canValide : false;
    }

    public void setCanValide(Boolean canValide) {
        this.canValide = canValide;
    }

    public YvsWorkflowEtapeValidation getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(YvsWorkflowEtapeValidation etapeValide) {
        this.etapeValide = etapeValide;
    }

    public YvsNiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(YvsNiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getCanNotify() {
        return canNotify != null ? canNotify : false;
    }

    public void setCanNotify(Boolean canNotify) {
        this.canNotify = canNotify;
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
        if (!(object instanceof YvsWorkflowAutorisationValidDoc)) {
            return false;
        }
        YvsWorkflowAutorisationValidDoc other = (YvsWorkflowAutorisationValidDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc[ id=" + id + " ]";
    }

}

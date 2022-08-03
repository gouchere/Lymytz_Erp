/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat.retenue;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaisseDocDiversTiers;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_retenue_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRetenueDocDivers.findAll", query = "SELECT y FROM YvsGrhRetenueDocDivers y WHERE y.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhRetenueDocDivers.findById", query = "SELECT y FROM YvsGrhRetenueDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhRetenueDocDivers.findByDocDivers", query = "SELECT y FROM YvsGrhRetenueDocDivers y WHERE y.docDivers = :docDivers"),
    @NamedQuery(name = "YvsGrhRetenueDocDivers.findByTiersDivers", query = "SELECT y FROM YvsGrhRetenueDocDivers y WHERE y.tiersDivers = :tiersDivers")
})
public class YvsGrhRetenueDocDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_retenue_doc_divers_id_seq", name = "yvs_grh_retenue_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_retenue_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;
    @JoinColumn(name = "tiers_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDiversTiers tiersDivers;
    @JoinColumn(name = "retenue", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementAdditionel retenue;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhRetenueDocDivers() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhRetenueDocDivers(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public YvsGrhElementAdditionel getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhElementAdditionel retenue) {
        this.retenue = retenue;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    public YvsComptaCaisseDocDiversTiers getTiersDivers() {
        return tiersDivers;
    }

    public void setTiersDivers(YvsComptaCaisseDocDiversTiers tiersDivers) {
        this.tiersDivers = tiersDivers;
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
        if (!(object instanceof YvsGrhRetenueDocDivers)) {
            return false;
        }
        YvsGrhRetenueDocDivers other = (YvsGrhRetenueDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhRetenueDocDivers[ id=" + id + " ]";
    }

}

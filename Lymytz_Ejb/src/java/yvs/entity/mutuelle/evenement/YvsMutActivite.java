/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_mut_activite", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsMutActivite.findAll", query = "SELECT y FROM YvsMutActivite y"),
    @NamedQuery(name = "YvsMutActivite.findById", query = "SELECT y FROM YvsMutActivite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutActivite.findByMontantRequis", query = "SELECT y FROM YvsMutActivite y WHERE y.montantRequis = :montantRequis"),

    @NamedQuery(name = "YvsMutActivite.findOne", query = "SELECT y FROM YvsMutActivite y WHERE y.evenement = :evenement AND y.typeActivite = :type")})
public class YvsMutActivite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_activite_id_seq", name = "yvs_mut_activite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_activite_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_requis")
    private Double montantRequis;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "evenement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutEvenement evenement;
    @JoinColumn(name = "type_activite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutActiviteType typeActivite;
    @OneToMany(mappedBy = "activite")
    private List<YvsMutParticipantEvenement> participants;
    @OneToMany(mappedBy = "activite")
    private List<YvsMutFinancementActivite> financements;
    @Transient
    private boolean select;
    @Transient
    private boolean new_;
    @Transient
    private double montantRecu;

    public YvsMutActivite() {
        participants = new ArrayList();
        financements = new ArrayList();
    }

    public YvsMutActivite(Long id) {
        this();
        this.id = id;
    }

    public YvsMutActivite(Long id, YvsMutActiviteType typeActivite) {
        this(id);
        this.typeActivite = typeActivite;
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

    public Double getMontantRequis() {
        return montantRequis != null ? montantRequis : 0;
    }

    public void setMontantRequis(Double montantRequis) {
        this.montantRequis = montantRequis;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutEvenement getEvenement() {
        return evenement;
    }

    public void setEvenement(YvsMutEvenement evenement) {
        this.evenement = evenement;
    }

    public YvsMutActiviteType getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(YvsMutActiviteType typeActivite) {
        this.typeActivite = typeActivite;
    }

    public List<YvsMutParticipantEvenement> getParticipants() {
        return participants;
    }

    public void setParticipants(List<YvsMutParticipantEvenement> participants) {
        this.participants = participants;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public double getMontantRecu() {
        montantRecu = 0;
        for (YvsMutFinancementActivite f : financements) {
            montantRecu += f.getMontantRecu();
        }
        return montantRecu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public List<YvsMutFinancementActivite> getFinancements() {
        return financements;
    }

    public void setFinancements(List<YvsMutFinancementActivite> financements) {
        this.financements = financements;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsMutActivite)) {
            return false;
        }
        YvsMutActivite other = (YvsMutActivite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutActivite[ id=" + id + " ]";
    }

}

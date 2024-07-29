/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.Transient;
import yvs.dao.YvsEntity;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_compta_journaux_periode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComptaJournauxPeriode.findAll", query = "SELECT y FROM YvsComptaJournauxPeriode y WHERE y.journal.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaJournauxPeriode.findById", query = "SELECT y FROM YvsComptaJournauxPeriode y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaJournauxPeriode.findByOne", query = "SELECT y FROM YvsComptaJournauxPeriode y WHERE y.journal = :journal AND y.periode = :periode"),})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComptaJournauxPeriode extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_journaux_periode_id_seq", name = "yvs_compta_journaux_periode_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_journaux_periode_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "cloture")
    private Boolean cloture;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPeriodeExercice periode;
    @Transient
    public static long ids = -1;

    public YvsComptaJournauxPeriode() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaJournauxPeriode(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaJournauxPeriode(Long id, Boolean cloture, YvsComptaJournaux journal, YvsMutPeriodeExercice periode) {
        this(id);
        this.cloture = cloture;
        this.journal = journal;
        this.periode = periode;
    }

    public Long getId() {
        return id != null ? id : 0;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutPeriodeExercice getPeriode() {
        return periode;
    }

    public void setPeriode(YvsMutPeriodeExercice periode) {
        this.periode = periode;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public Boolean getCloture() {
        return cloture != null ? cloture : false;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
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
        if (!(object instanceof YvsComptaJournauxPeriode)) {
            return false;
        }
        YvsComptaJournauxPeriode other = (YvsComptaJournauxPeriode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaJournauxPeriode[ id=" + id + " ]";
    }
}

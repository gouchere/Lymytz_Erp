/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_cible_facteur_taux")
@NamedQueries({
    @NamedQuery(name = "YvsComCibleFacteurTaux.findAll", query = "SELECT y FROM YvsComCibleFacteurTaux y"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findById", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findByTableExterne", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.tableExterne = :tableExterne"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findByLibelle", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findByDateSave", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findByDateUpdate", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComCibleFacteurTaux.findByIdExterne", query = "SELECT y FROM YvsComCibleFacteurTaux y WHERE y.idExterne = :idExterne")})
public class YvsComCibleFacteurTaux implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_cible_facteur_taux_id_seq", name = "yvs_com_cible_facteur_taux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_cible_facteur_taux_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "id_externe")
    private Long idExterne;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "facteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComFacteurTaux facteur;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComCibleFacteurTaux() {
    }

    public YvsComCibleFacteurTaux(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Long getIdExterne() {
        return idExterne != null ? idExterne : 0;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComFacteurTaux getFacteur() {
        return facteur;
    }

    public void setFacteur(YvsComFacteurTaux facteur) {
        this.facteur = facteur;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsComCibleFacteurTaux)) {
            return false;
        }
        YvsComCibleFacteurTaux other = (YvsComCibleFacteurTaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComCibleFacteurTaux[ id=" + id + " ]";
    }

}

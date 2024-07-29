/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_societes_infos_suppl")
@NamedQueries({
    @NamedQuery(name = "YvsSocietesInfosSuppl.findAll", query = "SELECT y FROM YvsSocietesInfosSuppl y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsSocietesInfosSuppl.findByTitre", query = "SELECT y FROM YvsSocietesInfosSuppl y WHERE y.titre = :titre AND y.societe = :societe"),
    @NamedQuery(name = "YvsSocietesInfosSuppl.findByType", query = "SELECT y FROM YvsSocietesInfosSuppl y WHERE y.type = :type AND y.societe = :societe"),
    @NamedQuery(name = "YvsSocietesInfosSuppl.findById", query = "SELECT y FROM YvsSocietesInfosSuppl y WHERE y.id = :id")})
public class YvsSocietesInfosSuppl extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_societes_infos_suppl_id_seq", name = "yvs_societes_infos_suppl_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_societes_infos_suppl_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(min = 1, max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Size(min = 1, max = 2147483647)
    @Column(name = "valeur")
    private String valeur;
    @Size(min = 1, max = 2147483647)
    @Column(name = "type")
    private String type;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsSocietesInfosSuppl() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsSocietesInfosSuppl(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre != null ? titre.trim().length() > 0 ? titre : Constantes.SUPPL_SOCIETE_AGREEMENT_NAME : Constantes.SUPPL_SOCIETE_AGREEMENT_NAME;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : Constantes.SUPPL_SOCIETE_AGREEMENT : Constantes.SUPPL_SOCIETE_AGREEMENT;
    }

    public void setType(String type) {
        this.type = type;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsSocietesInfosSuppl)) {
            return false;
        }
        YvsSocietesInfosSuppl other = (YvsSocietesInfosSuppl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsSocietesInfosSuppl[ id=" + id + " ]";
    }

}

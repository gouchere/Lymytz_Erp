/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.equipe;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_prod_membres_equipe")
@NamedQueries({
    @NamedQuery(name = "YvsProdMembresEquipe.findAll", query = "SELECT y FROM YvsProdMembresEquipe y"),
    @NamedQuery(name = "YvsProdMembresEquipe.findById", query = "SELECT y FROM YvsProdMembresEquipe y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdMembresEquipe.findMemebreEquipes", query = "SELECT y.producteur FROM YvsProdMembresEquipe y WHERE y.equipeProduction = :equipe"),
    @NamedQuery(name = "YvsProdMembresEquipe.findByActif", query = "SELECT y FROM YvsProdMembresEquipe y WHERE y.actif = :actif")})
public class YvsProdMembresEquipe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_membres_equipe_id_seq", name = "yvs_prod_membres_equipe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_membres_equipe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "equipe_production", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdEquipeProduction equipeProduction;
    @JoinColumn(name = "producteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers producteur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean selectActif;

    public YvsProdMembresEquipe() {
    }

    public YvsProdMembresEquipe(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsProdEquipeProduction getEquipeProduction() {
        return equipeProduction;
    }

    public void setEquipeProduction(YvsProdEquipeProduction equipeProduction) {
        this.equipeProduction = equipeProduction;
    }

    public YvsUsers getProducteur() {
        return producteur;
    }

    public void setProducteur(YvsUsers producteur) {
        this.producteur = producteur;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsProdMembresEquipe)) {
            return false;
        }
        YvsProdMembresEquipe other = (YvsProdMembresEquipe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.equipe.YvsProdEmployeEquipe[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.proj.projet;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_proj_projet_contenu_doc_achat", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findAll", query = "SELECT y FROM YvsProjProjetContenuDocAchat y"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findById", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findByDateUpdate", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findByDateSave", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findByProjet", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.projet = :projet"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findOne", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.projet = :projet AND y.contenu = :contenu"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findByContenu", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.contenu = :contenu"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.findByProjetContenu", query = "SELECT y FROM YvsProjProjetContenuDocAchat y WHERE y.projet.projet = :projet AND y.contenu = :contenu"),
    
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.sumByContenu", query = "SELECT SUM(y.quantite) FROM YvsProjProjetContenuDocAchat y WHERE y.contenu = :contenu"),
    @NamedQuery(name = "YvsProjProjetContenuDocAchat.sumByContenuNotId", query = "SELECT SUM(y.quantite) FROM YvsProjProjetContenuDocAchat y WHERE y.contenu = :contenu AND y.id != :id")})
public class YvsProjProjetContenuDocAchat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_proj_projet_contenu_doc_achat_id_seq", name = "yvs_proj_projet_contenu_doc_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_proj_projet_contenu_doc_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Double quantite;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "projet", referencedColumnName = "id")
    @ManyToOne
    private YvsProjProjetService projet;
    @JoinColumn(name = "contenu", referencedColumnName = "id")
    @ManyToOne
    private YvsComContenuDocAchat contenu;
    @Transient
    public static long ids = -1;

    public YvsProjProjetContenuDocAchat() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsProjProjetContenuDocAchat(Long id) {
        this();
        this.id = id;
    }

    @Override
    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
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

    @Override
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProjProjetService getProjet() {
        return projet;
    }

    public void setProjet(YvsProjProjetService projet) {
        this.projet = projet;
    }

    public YvsComContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(YvsComContenuDocAchat contenu) {
        this.contenu = contenu;
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
        if (!(object instanceof YvsProjProjetContenuDocAchat)) {
            return false;
        }
        YvsProjProjetContenuDocAchat other = (YvsProjProjetContenuDocAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.proj.YvsProjProjetContenuDocAchat[ id=" + id + " ]";
    }

}

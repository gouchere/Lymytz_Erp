/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.print;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_print_contrat_employe_article", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findAll", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.header = :header ORDER BY y.niveau, y.id"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findById", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByTitre", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByContenu", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.contenu = :contenu"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByIndice", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.indice = :indice AND y.header = :header"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByNiveau", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.niveau = :niveau"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByDateUpdate", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByDateSave", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintContratEmployeArticle.findByExecuteTrigger", query = "SELECT y FROM YvsPrintContratEmployeArticle y WHERE y.executeTrigger = :executeTrigger")})
public class YvsPrintContratEmployeArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_print_contrat_employe_article_id_seq", name = "yvs_print_contrat_employe_article_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_print_contrat_employe_article_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "contenu")
    private String contenu;
    @Size(max = 2147483647)
    @Column(name = "indice")
    private String indice;
    @Column(name = "niveau")
    private Integer niveau;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "header", referencedColumnName = "id")
    @ManyToOne
    private YvsPrintContratEmployeHeader header;

    public YvsPrintContratEmployeArticle() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintContratEmployeArticle(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id!=null?id:0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public Integer getNiveau() {
        return niveau != null ? niveau : 0;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsPrintContratEmployeHeader getHeader() {
        return header;
    }

    public void setHeader(YvsPrintContratEmployeHeader header) {
        this.header = header;
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
        if (!(object instanceof YvsPrintContratEmployeArticle)) {
            return false;
        }
        YvsPrintContratEmployeArticle other = (YvsPrintContratEmployeArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintContratEmployeArticle[ id=" + id + " ]";
    }

}

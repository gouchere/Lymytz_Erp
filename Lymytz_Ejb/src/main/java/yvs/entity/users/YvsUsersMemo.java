/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.users;

import java.io.Serializable;
import java.util.Calendar;
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

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_users_memo")
@NamedQueries({
    @NamedQuery(name = "YvsUsersMemo.findAll", query = "SELECT y FROM YvsUsersMemo y"),
    @NamedQuery(name = "YvsUsersMemo.findById", query = "SELECT y FROM YvsUsersMemo y WHERE y.id = :id"),
    @NamedQuery(name = "YvsUsersMemo.findByTitre", query = "SELECT y FROM YvsUsersMemo y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsUsersMemo.findByDescription", query = "SELECT y FROM YvsUsersMemo y WHERE y.description = :description"),
    @NamedQuery(name = "YvsUsersMemo.findByDateMemo", query = "SELECT y FROM YvsUsersMemo y WHERE y.dateMemo = :dateMemo"),
    @NamedQuery(name = "YvsUsersMemo.findByDateRappel", query = "SELECT y FROM YvsUsersMemo y WHERE y.dateDebutRappel = :dateDebutRappel"),
    @NamedQuery(name = "YvsUsersMemo.findByDureeRappel", query = "SELECT y FROM YvsUsersMemo y WHERE y.dateFinRappel = :dateFinRappel"),

    @NamedQuery(name = "YvsUsersMemo.findByRappel", query = "SELECT y FROM YvsUsersMemo y WHERE y.users = :users AND :date BETWEEN y.dateDebutRappel AND y.dateFinRappel ORDER BY y.dateDebutRappel DESC, y.id")})
public class YvsUsersMemo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_users_memo_id_seq", name = "yvs_users_memo_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_users_memo_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "date_memo")
    @Temporal(TemporalType.DATE)
    private Date dateMemo;
    @Column(name = "date_debut_rappel")
    @Temporal(TemporalType.DATE)
    private Date dateDebutRappel;
    @Column(name = "date_fin_rappel")
    @Temporal(TemporalType.DATE)
    private Date dateFinRappel;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    
    @Transient
    private int dureeRappel;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsUsersMemo() {
    }

    public YvsUsersMemo(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre != null ? titre : "";
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateMemo() {
        return dateMemo != null ? dateMemo : new Date();
    }

    public void setDateMemo(Date dateMemo) {
        this.dateMemo = dateMemo;
    }

    public Date getDateDebutRappel() {
        return dateDebutRappel != null ? dateDebutRappel : new Date();
    }

    public void setDateDebutRappel(Date dateDebutRappel) {
        this.dateDebutRappel = dateDebutRappel;
    }

    public Date getDateFinRappel() {
        return dateFinRappel != null ? dateFinRappel : new Date();
    }

    public void setDateFinRappel(Date dateFinRappel) {
        this.dateFinRappel = dateFinRappel;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public int getDureeRappel() {
        Calendar debut = Calendar.getInstance();
        debut.setTime(dateDebutRappel);
        Calendar fin = Calendar.getInstance();
        fin.setTime(dateFinRappel);
        dureeRappel = fin.get(Calendar.DAY_OF_YEAR) - debut.get(Calendar.DAY_OF_YEAR);
        return dureeRappel;
    }

    public void setDureeRappel(int dureeRappel) {
        this.dureeRappel = dureeRappel;
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
        if (!(object instanceof YvsUsersMemo)) {
            return false;
        }
        YvsUsersMemo other = (YvsUsersMemo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsMemoUsers[ id=" + id + " ]";
    }

}

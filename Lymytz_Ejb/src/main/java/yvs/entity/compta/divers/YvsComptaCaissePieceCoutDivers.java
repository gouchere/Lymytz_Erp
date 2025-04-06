/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_caisse_piece_cout_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaissePieceCoutDivers.findAll", query = "SELECT y FROM YvsComptaCaissePieceCoutDivers y"),
    @NamedQuery(name = "YvsComptaCaissePieceCoutDivers.findById", query = "SELECT y FROM YvsComptaCaissePieceCoutDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaissePieceCoutDivers.findByDateUpdate", query = "SELECT y FROM YvsComptaCaissePieceCoutDivers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCaissePieceCoutDivers.findByDateSave", query = "SELECT y FROM YvsComptaCaissePieceCoutDivers y WHERE y.dateSave = :dateSave")})
public class YvsComptaCaissePieceCoutDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_piece_cout_divers_id_seq", name = "yvs_compta_caisse_piece_cout_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_piece_cout_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "cout", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCoutSupDocDivers cout;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne
    private YvsComptaCaissePieceDivers piece;

    public YvsComptaCaissePieceCoutDivers() {
        dateSave = new Date();
        dateUpdate = new Date();
    }

    public YvsComptaCaissePieceCoutDivers(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCaissePieceCoutDivers(YvsComptaCoutSupDocDivers cout, YvsComptaCaissePieceDivers piece) {
        this();
        this.cout = cout;
        this.piece = piece;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaCoutSupDocDivers getCout() {
        return cout;
    }

    public void setCout(YvsComptaCoutSupDocDivers cout) {
        this.cout = cout;
    }

    public YvsComptaCaissePieceDivers getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaCaissePieceDivers piece) {
        this.piece = piece;
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
        if (!(object instanceof YvsComptaCaissePieceCoutDivers)) {
            return false;
        }
        YvsComptaCaissePieceCoutDivers other = (YvsComptaCaissePieceCoutDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaCaissePieceCoutDivers[ id=" + id + " ]";
    }

}

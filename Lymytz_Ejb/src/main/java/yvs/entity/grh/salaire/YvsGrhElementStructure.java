/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_element_structure")
@NamedQueries({
    @NamedQuery(name = "YvsElementStructure.findAll", query = "SELECT y FROM YvsGrhElementStructure y"),
    @NamedQuery(name = "YvsElementStructure.findByStructure", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.structure=:structure"),
    @NamedQuery(name = "YvsElementStructure.findElementStructure", query = "SELECT y.element FROM YvsGrhElementStructure y WHERE y.structure=:structure AND y.element.actif=true"),
    @NamedQuery(name = "YvsElementStructure.findElementStructureByCat", query = "SELECT y.element FROM YvsGrhElementStructure y WHERE y.structure=:structure AND y.element.actif=true AND y.element.categorie.codeCategorie=:categorie AND y.element.categorie.actif=true "),
    @NamedQuery(name = "YvsElementStructure.findElementStructure_", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.structure=:structure AND y.element=:element"),
    @NamedQuery(name = "YvsGrhElementStructure.findElementStructureLike", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.structure=:structure AND y.element.actif=true AND (y.element.code LIKE :code OR y.element.nom LIKE :code)"),
    @NamedQuery(name = "YvsGrhElementStructure.findElementInStructure", query = "SELECT y.element FROM YvsGrhElementStructure y WHERE y.structure=:structure AND y.element.actif=true AND (y.element.code=:code)"),
    @NamedQuery(name = "YvsElementStructure.findByStructureSalaire", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.structure = :structureSalaire"),
    @NamedQuery(name = "YvsElementStructure.findByElementSalaire", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.element = :elementSalaire"),
    @NamedQuery(name = "YvsElementStructure.findByActif", query = "SELECT y FROM YvsGrhElementStructure y WHERE y.actif = :actif")})
public class YvsGrhElementStructure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_element_structure_id_seq", name = "yvs_element_structure_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_element_structure_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "structure_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhStructureSalaire structure;
    @JoinColumn(name = "element_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire element;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhElementStructure() {
    }

    public YvsGrhElementStructure(YvsGrhElementStructure e) {
        this.id = e.id;
        this.actif = e.actif;
        this.structure = e.structure;
        this.element = e.element;
        this.author = e.author;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhStructureSalaire getStructure() {
        return structure;
    }

    public void setStructure(YvsGrhStructureSalaire structure) {
        this.structure = structure;
    }

    public YvsGrhElementSalaire getElement() {
        return element;
    }

    public void setElement(YvsGrhElementSalaire element) {
        this.element = element;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhElementStructure other = (YvsGrhElementStructure) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}

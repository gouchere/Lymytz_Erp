/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.synchro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_synchro_tables")
@NamedQueries({
    @NamedQuery(name = "YvsSynchroTables.findAll", query = "SELECT y FROM YvsSynchroTables y ORDER BY y.tableName"),
    @NamedQuery(name = "YvsSynchroTables.findById", query = "SELECT y FROM YvsSynchroTables y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroTables.findNotTables", query = "SELECT y FROM YvsSynchroTables y WHERE y.tableName NOT IN :tables ORDER BY y.tableName")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsSynchroTables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_tables_id_seq", name = "yvs_synchro_tables_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_tables_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "use_societe")
    private Boolean useSociete;
    @Column(name = "interval_load")
    private Integer intervalLoad;

    public YvsSynchroTables() {
    }

    public YvsSynchroTables(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getUseSociete() {
        return useSociete != null ? useSociete : false;
    }

    public void setUseSociete(Boolean useSociete) {
        this.useSociete = useSociete;
    }

    public Integer getIntervalLoad() {
        return intervalLoad != null ? intervalLoad : 0;
    }

    public void setIntervalLoad(Integer intervalLoad) {
        this.intervalLoad = intervalLoad;
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
        if (!(object instanceof YvsSynchroTables)) {
            return false;
        }
        YvsSynchroTables other = (YvsSynchroTables) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.synchro.YvsSynchroTables[ id=" + id + " ]";
    }

}

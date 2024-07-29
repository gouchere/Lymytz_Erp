/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author GOUCHERE YVES
 */
@Embeddable
public class YvsDepotCrenauxPk implements Serializable {

    @Column(name = "id_crenaux")
    private long idCrenaux;
    @Column(name = "id_depot")
    private long idDepot;

    public YvsDepotCrenauxPk() {
    }

    public YvsDepotCrenauxPk(long idCrenaux, long idDepot) {
        this.idCrenaux = idCrenaux;
        this.idDepot = idDepot;
    }

    public long getIdCrenaux() {
        return idCrenaux;
    }

    public void setIdCrenaux(long idCrenaux) {
        this.idCrenaux = idCrenaux;
    }

    public long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(long idDepot) {
        this.idDepot = idDepot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsDepotCrenauxPk other = (YvsDepotCrenauxPk) obj;
        if (this.idCrenaux != other.idCrenaux) {
            return false;
        }
        if (this.idDepot != other.idDepot) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.idDepot ^ (this.idDepot >>> 32));
        hash = 83 * hash + (int) (this.idCrenaux ^ (this.idCrenaux >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "YvsLiaisonDepotPk{" + "idDepot=" + idDepot + ", idCrenaux=" + idCrenaux+ '}';
    }
}

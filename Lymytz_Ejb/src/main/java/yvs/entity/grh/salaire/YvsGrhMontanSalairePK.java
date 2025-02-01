///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package yvs.entity.grh.salaire;
//
//import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//import javax.validation.constraints.NotNull;
//
///**
// *
// * @author user1
// */
//@Embeddable
//public class YvsGrhMontanSalairePK implements Serializable {
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "element_salaire")
//    private long elementSalaire;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "bulletin")
//    private long bulletin;
//
//    public YvsGrhMontanSalairePK() {
//    }
//
//    public YvsGrhMontanSalairePK(long elementSalaire, long bulletin) {
//        this.elementSalaire = elementSalaire;
//        this.bulletin = bulletin;
//    }
//
//    public long getElementSalaire() {
//        return elementSalaire;
//    }
//
//    public void setElementSalaire(long elementSalaire) {
//        this.elementSalaire = elementSalaire;
//    }
//
//    public long getBulletin() {
//        return bulletin;
//    }
//
//    public void setBulletin(long bulletin) {
//        this.bulletin = bulletin;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (int) elementSalaire;
//        hash += (int) bulletin;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsGrhMontanSalairePK)) {
//            return false;
//        }
//        YvsGrhMontanSalairePK other = (YvsGrhMontanSalairePK) object;
//        if (this.elementSalaire != other.elementSalaire) {
//            return false;
//        }
//        if (this.bulletin != other.bulletin) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.grh.salaire.YvsGrhMontanSalairePK[ elementSalaire=" + elementSalaire + ", bulletin=" + bulletin + " ]";
//    }
//    
//}

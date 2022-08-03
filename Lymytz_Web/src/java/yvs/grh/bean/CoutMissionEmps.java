///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.grh.bean;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//
///**
// *
// * @author user1
// */
//public class CoutMissionEmps implements Serializable {
//
//    private long id;
//    private TypeCout typeCout = new TypeCout();
//    private double montant;
//    private MissionEmps employe = new MissionEmps();
//
//    public CoutMissionEmps() {
//    }
//
//    public CoutMissionEmps(CoutMissionEmps c) {
//        this.id = c.getId();
//        this.montant = c.getMontant();
//        this.typeCout = c.getTypeCout();
//    }
//
//    public CoutMissionEmps(long id) {
//        this.id = id;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public TypeCout getTypeCout() {
//        return typeCout;
//    }
//
//    public void setTypeCout(TypeCout typeCout) {
//        this.typeCout = typeCout;
//    }
//
//    public double getMontant() {
//        return montant;
//    }
//
//    public void setMontant(double montant) {
//        this.montant = montant;
//    }
//
//    public MissionEmps getEmploye() {
//        return employe;
//    }
//
//    public void setEmploye(MissionEmps employe) {
//        this.employe = employe;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final CoutMissionEmps other = (CoutMissionEmps) obj;
//        if (this.id != other.id) {
//            return false;
//        }
//        return true;
//    }
//
//}

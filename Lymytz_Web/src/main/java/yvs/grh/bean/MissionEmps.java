///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.grh.bean;
//
//import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//
///**
// *
// * @author LYMYTZ
// */
//@ManagedBean
//@SessionScoped
//public class MissionEmps implements Serializable {
//
//    private long id;
//    private Employe employe = new Employe();
//    private String role, detailRole;
//    private List<CoutMissionEmps> coutsMission;
//    private boolean selectActif;
//
//    public MissionEmps() {
//        coutsMission = new ArrayList<>();
//    }
//
//    public List<CoutMissionEmps> getCoutsMission() {
//        return coutsMission;
//    }
//
//    public void setCoutsMission(List<CoutMissionEmps> coutsMission) {
//        this.coutsMission = coutsMission;
//    }
//
//    public MissionEmps(long id) {
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
//    public Employe getEmploye() {
//        return employe;
//    }
//
//    public void setSelectActif(boolean selectActif) {
//        this.selectActif = selectActif;
//    }
//
//    public boolean isSelectActif() {
//        return selectActif;
//    }
//
//    public void setEmploye(Employe employe) {
//        this.employe = employe;
//    }
//
////    public Mission getMission() {
////        return mission;
////    }
////
////    public void setMission(Mission mission) {
////        this.mission = mission;
////    }
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public String getDetailRole() {
//        return detailRole;
//    }
//
//    public void setDetailRole(String detailRole) {
//        this.detailRole = detailRole;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
//        final MissionEmps other = (MissionEmps) obj;
//        if (this.id != other.id) {
//            return false;
//        }
//        return true;
//    }
//}

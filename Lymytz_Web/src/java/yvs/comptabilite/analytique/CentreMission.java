/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.analytique;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.achat.ContenuDocAchat;
import yvs.grh.bean.mission.Mission;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CentreMission extends SectionAnalytique implements Serializable {

    private Mission mission = new Mission();

    public CentreMission() {
    }

    public CentreMission(long id) {
        super(id);
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

}

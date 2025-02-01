/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.analytique.SectionAnalytique;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ObjetMissionAnalytique extends SectionAnalytique {

    private ObjetMission objetMission = new ObjetMission();

    public ObjetMissionAnalytique() {
        super();
    }

    public ObjetMissionAnalytique(long id) {
        super(id);
    }

    public ObjetMission getObjetMission() {
        return objetMission;
    }

    public void setObjetMission(ObjetMission objetMission) {
        this.objetMission = objetMission;
    }

    @Override
    public String toString() {
        return "ArticleAnalytique{" + "id=" + id + ", article=" + objetMission + ", centre=" + centre + '}';
    }

}

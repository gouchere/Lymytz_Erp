/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.Personnel;

@ManagedBean(name = "personnelService", eager = true)
@SessionScoped
public class PersonnelService implements Serializable {

    private List<Personnel> personnels;

    public PersonnelService() {
        personnels = new ArrayList<>();
    }

    public PersonnelService(List<Personnel> personnels) {
        this.personnels = personnels;
    }

    public List<Personnel> getPersonnels() {
        return personnels;
    }

    public void setPersonnels(List<Personnel> personnels) {
        this.personnels = personnels;
    }
}

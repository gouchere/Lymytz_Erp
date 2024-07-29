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
import yvs.entity.users.YvsUsers;

@ManagedBean(name = "employeComService", eager = true)
@SessionScoped
public class EmployeComService implements Serializable{

    private List<YvsUsers> employes;

    public EmployeComService() {
        employes = new ArrayList<>();
    }

    public EmployeComService(List<YvsUsers> employes) {
        this.employes = employes;
    }

    public List<YvsUsers> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsUsers> employes) {
        this.employes = employes;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author hp Elite 8300
 */
@ApplicationScoped
@ManagedBean
public class LymytzApplication implements Serializable {

    public static List<UserConnect> listUser;
    private static int count = 0;

    public LymytzApplication() {
        listUser = new ArrayList<>();
    }

    public List<UserConnect> getListUser() {
        return listUser;
    }

    public void setListUser(List<UserConnect> listUser) {
        LymytzApplication.listUser = listUser;
    }

    @PostConstruct
    public void count() {
        Logger.getLogger(LymytzApplication.class.getName()).log(Level.INFO, "SESSION : " + count);
    }

    public void doNothing() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@ApplicationScoped
public class ManagedVersion implements Serializable {

    private Version version = new Version();

    public ManagedVersion() {
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void load() {
        version = new VersionReader().create();
    }
}

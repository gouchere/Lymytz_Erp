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
import yvs.entity.tiers.YvsBaseTiers;

@ManagedBean(name = "tiersService", eager = true)
@SessionScoped
public class TiersService implements Serializable{

    private List<YvsBaseTiers> tiers;

    public TiersService() {
        tiers = new ArrayList<>();
    }

    public TiersService(List<YvsBaseTiers> articles) {
        this.tiers = articles;
    }

    public List<YvsBaseTiers> getTiers() {
        return tiers;
    }

    public void setTiers(List<YvsBaseTiers> tiers) {
        this.tiers = tiers;
    }
}

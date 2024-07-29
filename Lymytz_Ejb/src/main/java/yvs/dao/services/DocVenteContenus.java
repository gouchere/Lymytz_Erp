/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services;

import yvs.entity.commercial.vente.YvsComDocVentes;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Lymytz Dowes
 */
public class DocVenteContenus extends XmlAdapter<String, YvsComDocVentes> {

    @Override
    public String marshal(YvsComDocVentes v) throws Exception {
        System.out.println("v : " + v);
        if (v != null ? v instanceof YvsComDocVentes : false) {
            ((YvsComDocVentes) v).setContenus(null);
        }
        return null;
    }

    @Override
    public YvsComDocVentes unmarshal(String v) throws Exception {
        System.err.println("v : " + v);
        return null;
    }
}

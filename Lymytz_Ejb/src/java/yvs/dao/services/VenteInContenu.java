/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Lymytz Dowes
 */
public class VenteInContenu extends XmlAdapter<String, Object> {

    @Override
    public String marshal(Object v) throws Exception {
        System.out.println("Object : " + v);
        return null;
    }

    @Override
    public Object unmarshal(String v) throws Exception {
        System.out.println("String : " + v);
        return null;
    }

}

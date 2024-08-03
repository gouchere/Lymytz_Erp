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
public class LongAdapter extends XmlAdapter<String, Long> {

    @Override
    public String marshal(Long v) throws Exception {
        System.out.println("Long : " + v);
        return v.toString();
    }

    @Override
    public Long unmarshal(String v) throws Exception {
        System.out.println("String : " + v);
        return Long.valueOf(v);
    }
}

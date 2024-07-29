/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = DocVenteContenus.class, type = YvsComDocVentes.class)
})
package yvs.entity.commercial.vente;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import yvs.dao.services.DocVenteContenus;

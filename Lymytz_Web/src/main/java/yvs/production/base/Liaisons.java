/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.production.base;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import yvs.base.produits.Articles;

/**
 *
 * @author yves
 */
public class Liaisons implements Serializable{
    
    private Articles composant;
    private short niveau;
    private Date debut=new Date(), fin=new Date();
    private String typeDoc, modeArondi;
    private double pourcentageRebu;
}

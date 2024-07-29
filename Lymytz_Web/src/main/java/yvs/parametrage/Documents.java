/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Documents implements Serializable{
    
    private String docAchat;
    private String docVente;
    private String docCaisse;
    private String pieceComptable;
}

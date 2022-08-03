/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.doc;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "configBRV")
@XStreamAlias("configBRV")
public class ConfigBRV extends ConfigDoc implements Serializable {

    public ConfigBRV() {
        super();
        this.setPrefixe("BRV");
        this.setMonth(true);
        this.setFournisseur(true);
        this.setNbDigit(4);
        this.setYear(true);
        
    }

    
}

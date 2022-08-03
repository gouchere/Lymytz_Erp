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
@ManagedBean(name = "configFAV")
@XStreamAlias("configFAV")
public class ConfigFAV extends ConfigDoc implements Serializable {

    public ConfigFAV() {
        super();
        this.setPrefixe("FAV");
        this.setMonth(true);
        this.setFournisseur(true);
        this.setNbDigit(4);
        this.setYear(true);
    }

   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.doc;

/**
 *
 * @author GOUCHERE YVES
 */
public class FactoryDoc {

    public FactoryDoc() {
    }

    public ConfigDoc creerDoc(String type) {
        ConfigDoc doc = null;
        switch (type) {
            case "FA":
                doc = new ConfigFactureA();
                break;
            case "BCA":
                doc = new ConfigBCA();
                break;
            case "BRA":
                doc = new ConfigBRA();
                break;
            case "BAA":
                doc = new ConfigBAA();
                break;
            case "FAA":
                doc = new ConfigFactureAA();
                break;
            case "FRA":
                doc = new ConfigFactureRA();
                break;
            case "BCV":
                doc = new ConfigBCV();
                break;
            case "BLV":
                doc = new ConfigBLV();
                break;
            case "BAV":
                doc = new ConfigBAV();
                break;
            case "BRV":
                doc = new ConfigBRV();
                break;
            case "FV":
                doc = new ConfigFV();
                break;
            case "FLV":
                doc = new ConfigFLV();
                break;
            case "FAV":
                doc = new ConfigFAV();
                break;
            case "FRV":
                doc = new ConfigFRV();
                break;
            case "TR":
                doc = new ConfigTR();
                break;
            case "EN":
                doc = new ConfigEN();
                break;
            case "SO":
                doc = new ConfigSO();
                break;
            case "INV":
                doc = new ConfigINV();
                break;
            default:
                break;
        }
        return doc;
    }
}

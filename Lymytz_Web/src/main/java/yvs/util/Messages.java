/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author GOUCHERE YVES
 */
public class Messages {

    private ResourceBundle bundle;

    public Messages() {
        bundle = ResourceBundle.getBundle("yvs.properties.Messages", Locale.FRANCE);        
    } 

    public String getMessage(String key) {
        String msg = "";
        try {
            msg = bundle.getString(new StringBuilder("msg.").append(key).toString());
        } catch (MissingResourceException e) {
            return "unkown message " + key;
        }
        return msg;
    }
    
    public String getMessages(String key, Object... params) {
        String message = null;
        try {
            message = bundle.getString(new StringBuilder("msg.").append(key).toString());
            message = MessageFormat.format(message, params);
        } catch (MissingResourceException e) {
            return "unkown message "+key;
        } catch (IllegalArgumentException e) {
            return "bad parameters for "+message;
        }
        return message;        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer;

import javax.ejb.Local;

/**
 *
 * @author LYMYTZ-PC
 */
@Local
public interface InterfaceTimerLocal {
    
    public void myTimer();

    void avancement();
}

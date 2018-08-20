/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smb_smo_popr;

/**
 *
 * @author Ja
 */
public class Random {

    public static double randomWithRange(double min, double max) {
        Random randomGenerator = new Random();
        double range = (max - min);
        return (Math.random() * range) + min;
    }
    
    
}

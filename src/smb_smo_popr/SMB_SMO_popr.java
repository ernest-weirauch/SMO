/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smb_smo_popr;

import java.io.IOException;

/**
 *
 * @author Ja
 */
public class SMB_SMO_popr {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        SMO smo = new SMO();
        smo.wczytajDane();
        smo.wygenerujZgloszenia(); //todo: niedeterministyczne lambda i mi
        smo.run();
    }
    
}

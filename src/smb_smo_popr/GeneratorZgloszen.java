/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smb_smo_popr;

import java.util.LinkedList;
import java.util.Random;
import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.PoissonGenerator;

/**
 *
 * @author Ja
 */
public class GeneratorZgloszen {

    private LinkedList<Zgloszenie> listaZgloszen;
    private double lambda;
    private double mi;
    private boolean isLambdaDeterministyczna;
    private boolean isMiDeterministyczne;
    private int T;
    private int t, tt;
    private double aktualnyCzas;

    private Random randomGenerator;
    private PoissonGenerator poissonGenerator;
    private ExponentialGenerator exponentialGenerator;

    public GeneratorZgloszen(double lambda, double mi, boolean isLambdaDeterministyczna, boolean isMiDeterministyczne, int T) {
        this.lambda = lambda;
        this.mi = mi;
        this.isLambdaDeterministyczna = isLambdaDeterministyczna;
        this.isMiDeterministyczne = isMiDeterministyczne;
        this.T = T;
        this.listaZgloszen = new LinkedList<Zgloszenie>();

        randomGenerator = new Random(); //inicjalizacja generatorow losowych
        poissonGenerator = new PoissonGenerator(lambda, randomGenerator);
        if(isMiDeterministyczne==false) exponentialGenerator = new ExponentialGenerator(mi, randomGenerator);
        else exponentialGenerator = new ExponentialGenerator(1/lambda, randomGenerator);
    }

    public LinkedList<Zgloszenie> getWygenerowaneZgloszenia() throws Exception {
        //utworz zgloszenia, dodaj do listyZgloszen oraz ustal ich czas poczatkowy
        if (isLambdaDeterministyczna) {
            int lambdaI = (int) this.lambda;
            for (int t = 0; t < T; t++) {
                while (tt < lambdaI) {
                    aktualnyCzas = (t + ((1.0 / lambdaI) * tt));
                    listaZgloszen.add(new Zgloszenie(aktualnyCzas, 0.0));
                    tt++;
                }
                tt = 0;
            }
            listaZgloszen.add(new Zgloszenie(T, 0));
        } else { //lambda niedeterministyczna
            //srednio jak wiele razy cos dzieje sie w jednej jednostce czasu
            int lambdaI = (int) this.lambda;
            for (int aktualnyCzas = 0; aktualnyCzas < T; aktualnyCzas++) { //aktualnyCzas - czas calkowity
                int liczbaZgloszenWTejJednostce = poissonGenerator.nextValue().intValue();

                //podziel jednostke czasu na czesci zaleznie od liczbyZgloszenWTejJednostce
                int j = 0;
                while (j < liczbaZgloszenWTejJednostce) {
                    double tmp = (aktualnyCzas + (1 / liczbaZgloszenWTejJednostce) * j);

//                    if(liczbaZgloszenWTejJednostce!=1) 
                    listaZgloszen.add(new Zgloszenie(tmp, 0));
                    //else listaZgloszen.add(new Zgloszenie(aktualnyCzas,0));
                    j++;
                }

                //listaZgloszen.add(new Zgloszenie(poissonGenerator.nextValue().doubleValue(), 0.0));
                //System.out.println(listaZgloszen.getLast().toString(""));
            }
            listaZgloszen.add(new Zgloszenie(T, 0));
        }

        //ustal czas koncowy zgloszen i je posortuj
        if (isMiDeterministyczne == true) {
            int mi = (int) this.mi;
            for (Zgloszenie z : listaZgloszen) {
                z.setCzasObslugi(mi);
            } //mi niedeterministyczne
        } else { //mi niedeterministyczne
            
           // int mi = (int) this.mi;
            for (Zgloszenie z : listaZgloszen) {
                z.setCzasObslugi(exponentialGenerator.nextValue().doubleValue());
            }

            //posortuj wszyst. wylos. zglosz. 
        }

        //Przypisz każdemu zgłoszeniu id (w kolejności ich pojawiania się w systemie)
        for (Zgloszenie z : listaZgloszen) {
            z.updateId();
            //System.out.println(z.toString(""));
        }
        return listaZgloszen;
    }

}

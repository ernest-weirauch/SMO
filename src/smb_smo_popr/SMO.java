/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smb_smo_popr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author Ja
 */
class SMO {

    private double lambda;
    private double mi;
    private int L; //max dl. kolejki
    private int T; //liczba jednostek czasu funkcjonowania systemu
    private int iloscKanalowObslugi;
    private boolean isLambdaDeterministyczna;
    private boolean isMiDeterministyczne;
    private LinkedList<Zgloszenie> listaZgloszen;
    private double aktualnyCzas;

    private Kolejka kolejka;
    private ArrayList<KanalObslugi> listaKanalowObslugi;

    private Zgloszenie z;

    private int liczbaWykresow = 6; //liczba wykresow
    private PrintWriter[] wykres = new PrintWriter[liczbaWykresow];
    private int liczbaOdrzuconychZgloszen =0;

    public SMO() throws IOException {
        listaZgloszen = new LinkedList<Zgloszenie>();
        aktualnyCzas = 0.0;
        //inicjalizacja kolejki rozmiarem po wczytajDane() we wczytajDane()
        //kanalObslugi j.w.
        z = null;

    }

    public void wczytajDane() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String odpowiedz;
        
        System.out.println("Podaj max dlugosc kolejki (L): ");
        L = Integer.parseInt(in.readLine());
        System.out.println("Podaj liczbe jednostek czasu funkcjonowania systemu (T): ");
        T = Integer.parseInt(in.readLine());
        System.out.println("Podaj liczbe kanalow obslugi: ");
        iloscKanalowObslugi = Integer.parseInt(in.readLine());

        System.out.println("Lambda deterministyczna? [T/N]");
        odpowiedz = in.readLine();
        if (odpowiedz.equals("t") || odpowiedz.equals("T")) {
            isLambdaDeterministyczna = true;
            System.out.println("Podaj lambda: ");
            lambda = Double.parseDouble(in.readLine());
        } else {
            isLambdaDeterministyczna = false;
            lambda = Random.randomWithRange(1.0, 10.0);
            System.out.println("lambda=" + lambda);
        }

        System.out.println("Mi deterministyczne? [T/N]");
        odpowiedz = in.readLine();
        if (odpowiedz.equals("t") || odpowiedz.equals("T")) {
            isMiDeterministyczne = true;
            System.out.println("Podaj mi (liczba calkowita): ");
            mi = Double.parseDouble(in.readLine());
        } else {
            isMiDeterministyczne = false;
            mi = Random.randomWithRange(1.0, 10.0);
            System.out.println("mi=" + mi);
        }

        
        
        
//        L = 10;
//        T = 10;
//        iloscKanalowObslugi = 1;    //zrobic dla wiecej niz 1 kanalu
//        isLambdaDeterministyczna = false;
//        //lambda = 2.0; //dla determinist lambda dziala
//        isMiDeterministyczne = false;
//        //mi = 1.0;     //dla determinist mi dziala
//        lambda = (Math.random() * (10.0 - 1.0)) + 1.0;
//        mi = (Math.random() * (10.0 - 1.0)) + 1.0;
//        System.out.println("lambda=" + lambda);
//        System.out.println("mi1=" + mi);        
//        //lambda = Random.randomWithRange(1.0, 10.0);
//        //mi = Random.randomWithRange(1.0, 10.0);
        
     
        
        
        //stworz kolejke
        kolejka = new Kolejka(L);

        //utworz kanaly obslugi
        listaKanalowObslugi = new ArrayList<KanalObslugi>();
        for (int i = 0; i < iloscKanalowObslugi; i++) {
            listaKanalowObslugi.add(new KanalObslugi());
        }

        zainicjalizujWykresy();
    }

    public void run() throws Exception {
        ListIterator<Zgloszenie> iListaZgloszen = listaZgloszen.listIterator();
        int lambdaI = (int) lambda;
        Zgloszenie z = null;
        while (!listaZgloszen.isEmpty()) {
//            for (int i = 0; i < iloscKanalowObslugi; i++) { //powtarzaj kroki tyle ile kanalow obslugi (aby w przypadku gdy kilka jest wolnych to wszystkie zgloszenia do nich poszly
//                if (z == null) { //ozn ze poprzednie zgloszenie z listaZgloszen zostalo juz przeniesione do kolejki i mozna zajac sie nastepnym
//                    z = iListaZgloszen.next();
//           }
//
//                aktualizujKanalyObslugi();
//
//            }
            double aktualnyCzas = 0.0;
            double poprzCzas = 0.0;
            boolean breakMe = false;
            while (breakMe == false && !listaZgloszen.isEmpty()) { //dopoki w tej samej jedn czasu sa jeszcze zgloszenia
                z = iListaZgloszen.next();
                //ZAKLADAM ZE LISTA WYGEN ZGLOSZ. JEST POSORT ROSNACO
                //UWAGA!!! UPLYW CZASU SYMULACJI!!!
                aktualnyCzas = z.getCzasPrzybycia();

                System.out.print("\n[" + aktualnyCzas + "]\t");
                System.out.print("[Przyszlo " + z.toString("") + "]\t");

                fun2(aktualnyCzas);

                //wrzuc zgloszenie do kolejki
                if (kolejka.wstaw(z) == true) {
                    System.out.print("[" + z.toString() + " -> kolejka]\t");
                } else { //nie udalo sie wstawic do kolejki - odrzuc
                    System.out.print("[Odrzucono " + z.toString() + " - kolejka pelna!]\t");
                    liczbaOdrzuconychZgloszen++;
                    wykres[5].println(aktualnyCzas+"\t"+liczbaOdrzuconychZgloszen);
                }
                iListaZgloszen.remove(); //usuwa pobrany uprzednio element z listyZgloszen 
                //z = null;

                fun2(aktualnyCzas);

                //  Jezeli ktorys kanal obslugi pusty to wstaw do niego nast zgloszenie z kolejki
                fun(aktualnyCzas);
                aktualizujKanalyObslugi(aktualnyCzas);

                //  Jezeli ktorys kanal obslugi pusty to wstaw do niego nast zgloszenie z kolejki
                fun(aktualnyCzas);

                wykres[4].println(aktualnyCzas+"\t"+listaKanalowObslugi.get(0).getIloscObsluzonychZgloszen());
                //System.out.println(listaKanalowObslugi.get(0).getIloscObsluzonychZgloszen());
                
                
                poprzCzas = aktualnyCzas;
            }

        }
        //zamknij pliki
        for (int w = 0; w < liczbaWykresow; w++) {
            wykres[w].close();
        }

    }

    void wygenerujZgloszenia() throws Exception {
        GeneratorZgloszen generatorZgloszen = new GeneratorZgloszen(lambda, mi, isLambdaDeterministyczna, isMiDeterministyczne, T);
        listaZgloszen = generatorZgloszen.getWygenerowaneZgloszenia();

        //wykres1
        for (Zgloszenie z : listaZgloszen) {
            wykres[0].println(z.getCzasPrzybycia() + "\t0.0");
        }

    }

    void aktualizujKanalyObslugi(double aktualnyCzas) {
        for (KanalObslugi k : listaKanalowObslugi) {
            k.update(aktualnyCzas, wykres[2]);
        }

    }

    public static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void fun(double aktualnyCzas) throws Exception {
        if (listaKanalowObslugi.get(0).isZajety()) {
            wykres[2].println(aktualnyCzas + "\t1");
        } else {
            wykres[2].println(aktualnyCzas + "\t0");
        }

        for (KanalObslugi k : listaKanalowObslugi) {
            if (k.isZajety() == false && !kolejka.isEmpty()) {
                Zgloszenie z_tmp = kolejka.podejrzyj();
                k.wstawZgloszenie(kolejka.pobierz(), aktualnyCzas);
                System.out.print("[" + z_tmp.toString() + " -> " + k.toString() + "]\t");
            }
        }

        if (listaKanalowObslugi.get(0).isZajety()) {
            wykres[2].println(aktualnyCzas + "\t1");
        } else {
            wykres[2].println(aktualnyCzas + "\t0");
        }
    }

    private void zainicjalizujWykresy() throws IOException {
        for (int i = 0; i < liczbaWykresow; i++) {
            wykres[i] = new PrintWriter(new BufferedWriter(new FileWriter("wykres" + Integer.toString(i) + ".txt")));
        }

        wykres[0].println("#pojawianie sie zgloszen w systemie\n#czas\t#y=0");//wykres 0 - pojawiajace sie zgloszenia y = 0 na osi x punkty
        wykres[1].println("#dlugosc kolejki w czasie\n#czas\t#dlugoscKolejki");
        //wykres[2].println("#liczba obsluzonych zgloszen (lacznie wszystkie kanaly) w czasie\n#czas\t#obsluzonych zgloszen");
        wykres[2].println("#zajetosc kanalu\n#czas\t#zajetosc [0,1]");
        //wykres[3].println("#liczba odrzuconych zgloszen w jednostce czasu\n#czas\t#nieobsluzonych zgloszen"); //wykres 3 - liczba odrzuconych zgloszen w jednostce czasu
        wykres[3].println("#srednia liczba zgloszen przybylych na jednostke czasu\n#czas\t#liczbaZgloszen");
        for (int t = 0; t < T; t++) {
            wykres[3].println((double) t + "\t" + Double.toString(lambda));
        }
        wykres[4].println("#liczba obsluzonych zgloszen przez kanal 1\n#czas\t#liczbaZgloszen obsluzonych");
        wykres[5].println("#liczba odrzuconych zgloszen przez kanal 1\n#czas\t#liczbaZgloszen odrzuconych");
    }

    public void fun2(double aktualnyCzas) {
        wykres[1].println(aktualnyCzas + "\t" + kolejka.getCurrentSize());
    }
}

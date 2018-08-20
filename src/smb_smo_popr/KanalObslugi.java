package smb_smo_popr;

import java.io.PrintWriter;


public class KanalObslugi {
    private int id;
    private static int lastId=0;
    private boolean zajety;
    private Zgloszenie zgloszenie;
    private int iloscObsluzonychZgloszen;
    private double momentOtrzymaniaAktualnegoZgloszenia;
    private double czasObslugiAktualnegoZgloszenia;
    private double czasByciaZajetym;
    
    public int getIloscObsluzonychZgloszen() {
        return iloscObsluzonychZgloszen;
    }

    public KanalObslugi() {
        id = lastId++;
        zajety = false;
        zgloszenie=null;
        iloscObsluzonychZgloszen=0;
        
        momentOtrzymaniaAktualnegoZgloszenia=0.0;
        czasObslugiAktualnegoZgloszenia=0.0;
    }
    
    public void wstawZgloszenie(Zgloszenie zgloszenie, double czas){
        zajety=true;
        this.zgloszenie = zgloszenie;
        
        momentOtrzymaniaAktualnegoZgloszenia=czas;
        czasObslugiAktualnegoZgloszenia=0.0;
    }
    
    public void update(double czas, PrintWriter wykres){    //otrzymuje aktualny czas symulacji
        if(zgloszenie==null || zajety==false) return;   //Jezeli brak zgloszenia w kanale LUB kanal pusty
        czasObslugiAktualnegoZgloszenia=(czas-momentOtrzymaniaAktualnegoZgloszenia);
        if(this.zajety) wykres.println(czas+"\t1");
        else wykres.println(czas+"\t0");
        //jezeli kanal obsluguje zgloszenie tyle czasu ile zajmuje jego obsluzenie
        if(czasObslugiAktualnegoZgloszenia>=zgloszenie.getCzasObslugi()){
            zgloszenie.setObsluzone(true);
            iloscObsluzonychZgloszen++;
            System.out.print("["+this.toString()+": obsluzyl zgloszenie: "+zgloszenie.toString()+"w t="+czas+"]\t");
            zgloszenie=null;
            zajety=false;
            //czasByciaZajetym=poprzCzas=0.0;   //wykonywane przy wstawianiu nowegio zgloszenia
            //czasByciaZajetym+=czasObslugiAktualnegoZgloszenia;
        } 
        czasByciaZajetym+=czasObslugiAktualnegoZgloszenia;
        
        //if(zajety==true) poprzCzas=czas;
    }

    public boolean isZajety() {
        return zajety;
    }
 
    @Override
    public String toString(){
        return "KO("+(id)+")";
    }
}

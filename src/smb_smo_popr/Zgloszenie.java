package smb_smo_popr;

import java.util.Comparator;


public class Zgloszenie implements Comparable<Zgloszenie>, Cloneable{ //
    private int id;
    private double czasPrzybycia;
    private double czasObslugi;
    private static int lastId=0;
    private boolean obsluzone=false;
    
    public boolean isObsluzone() {
        return obsluzone;
    }

    public void setObsluzone(boolean obsluzone) {
        this.obsluzone = obsluzone;
    }
    
    public int getId() {
        return id;
    }
    
    public Zgloszenie(){
        
    }

    public void setCzasPrzybycia(double czasPrzybycia) {
        this.czasPrzybycia = czasPrzybycia;
    }

    public void setCzasObslugi(double czasObslugi) {
        this.czasObslugi = czasObslugi;
    }
    

    public Zgloszenie(double czasPrzybycia, double czasObslugi){
        //id = ++lastId;
        this.czasPrzybycia = czasPrzybycia;
        this.czasObslugi = czasObslugi;
    }

    public double getCzasPrzybycia() {
        return czasPrzybycia;
    }

    public double getCzasObslugi() {
        return czasObslugi;
    }
    
    @Override
    public String toString(){
        return "Z("+id+")";
    }
    
    public String toString(String s){
        return this.toString()+": t1="+czasPrzybycia+", t2="+czasObslugi;
    }

    public void updateId(){
        id = lastId++;
    }
    
    
    @Override
    public int compareTo(Zgloszenie o) {
        return Comparators.CZAS_UTWORZENIA.compare(this, o);
    }
    
    public static class Comparators{
        public static Comparator<Zgloszenie> CZAS_UTWORZENIA = new Comparator<Zgloszenie>(){
           @Override
           public int compare(Zgloszenie z1, Zgloszenie z2){
               Double d1 = new Double(z1.czasPrzybycia);
               Double d2 = new Double(z2.czasPrzybycia);
               
               return d1.compareTo(d2);
           }
        };
    }
    
}

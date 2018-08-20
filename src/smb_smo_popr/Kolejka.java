package smb_smo_popr;

import java.util.LinkedList;
import java.util.Queue;


public class Kolejka { //Kolejka o ustalonym rozmiarze
    Queue<Zgloszenie> kolejka = new LinkedList<Zgloszenie>();
    private int maxSize;
    private int currentSize;
    private int liczbaOdrzuconychZgloszen;
    
    public Kolejka(int maxSize) {
        this.maxSize = maxSize;
        this.currentSize = 0;
    }
    
    public boolean wstaw(Zgloszenie zgloszenie){ //zwraca false jesli kolejka zapelniona
        if(currentSize<maxSize){
            kolejka.offer(zgloszenie);
            currentSize++;
            return true;
        }
        else { //kolejka pelna
            liczbaOdrzuconychZgloszen++;
            return false;
        } 
    }
    
    public Zgloszenie pobierz() throws Exception{ 
        if(currentSize==0) return null;//throw new Exception("Nie mozna pobrac elementu z kolejki: Kolejka jest pusta!");
        else{
            Zgloszenie zgloszenie = kolejka.poll();
            currentSize--;
            return zgloszenie;
        }
        
    }
    
    public Zgloszenie podejrzyj() throws Exception{
        if(kolejka.isEmpty()==true || currentSize==0) throw new Exception("Nie mozna podejrzec elementu z kolejki: Kolejka jest pusta!");
        return kolejka.peek();
    }
    
    public boolean isEmpty(){
        if(currentSize==0) return true;
        else return false;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    int getLiczbaOdrzuconychZgloszen() {
        return liczbaOdrzuconychZgloszen;
    }
    
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package smb_smo_popr;
//
//import java.util.ArrayList;
//import org.apache.commons.math3.random.RandomGenerator;
//import org.uncommons.maths.random.PoissonGenerator;
//
///**
// *
// * @author Ja
// */
//public class test {
//
//    public static void main(String[] args) {
//        double lambda=4.0;
//         Random randomGenerator = new Random();
//        PoissonGenerator poissonGenerator = new PoissonGenerator(lambda, randomGenerator);
//        
//        ArrayList<Zgloszenie> lZ = new ArrayList<Zgloszenie>();
//        System.out.println("Lista wygen zgloszen:");
//        for(int i=0;i<3;i++){
//            lZ.add(new Zgloszenie((double)poissonGenerator.nextValue().intValue(),0.5));
//            System.out.println(lZ.get(i).toString(""));
//        }
//        
//        KanalObslugi k = new KanalObslugi();
//        int iterator=0;
//        
//        for(double i=0.0;i<10.0;i++){
//            if(lZ.get(iterator).isObsluzone()==false && lZ.get(iterator).getCzasPrzybycia()==i){
//                if(k.isZajety()==false){
//                    k.wstawZgloszenie(lZ.get(iterator));
//                    System.out.println("Wstawiono zloszenie do kanalu");
//                }
//                else System.out.println("Kanal zajety, odrzucono zgloszenie");
//                iterator++;
//                if(iterator>=3) break;
//            }
//            k.update(i);
//        }
//        
//        
//    }
//    
//    public static void a(){
//                int T = 5;
//        int tt = 0;
//        int lambda = 4;
//        double aktualnyCzas = 0.0;
//
//        for (int t = 0; t < T; t++) {
//            while (tt < lambda) {
//
//                aktualnyCzas = (t + ((1.0/lambda) * tt));
//                System.out.println(aktualnyCzas);
//                tt++;
//            }
//            tt = 0;
//        }
//        System.out.println(T);
//    }
//}

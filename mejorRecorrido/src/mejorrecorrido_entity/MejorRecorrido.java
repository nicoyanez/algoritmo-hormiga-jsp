/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nnn
 */
public class MejorRecorrido {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        double FEROMONAINICIAL=15.0;
        double ALPHAINICIAL=3.0;
        double BETAINICIAL=4.7;
        double INDICEDEEVAPORACION=0.78;
        int numeroDeHormigas=17;
        int [][] distancias=new int[17][17];
        double [][] feromona=new double[17][17];
        try {
            leeDistancias(distancias,"C:\\Users\\nnn\\Desktop\\gr17.tsp");
            llenaFeromonas(feromona, FEROMONAINICIAL);
        } catch (Exception ex) {
           System.out.println("exept "+ex);
        }
        ///Comienza el proceso 
        ArrayList Hormigas = new ArrayList();
        Hormiga[] hormigas = new Hormiga[17];
        for (int i=0;i<hormigas.length;i++){
            //Hormigas.add(new Hormiga(distancias,feromona,i,ALPHAINICIAL,BETAINICIAL));
            hormigas[i]= new Hormiga(distancias,feromona,i,ALPHAINICIAL,BETAINICIAL);
        }
        boolean sigue=true;
        int MejorRecorrido=0;
        while (sigue){
            for (int i=0;i<17;i++){
                if (hormigas[i].avanza()){
                    System.out.println("Recorrido "+hormigas[i].recorrido+"\tpeso "+hormigas[i].peso());
                    if (hormigas[MejorRecorrido].peso()>hormigas[i].peso()){
                        MejorRecorrido=i;
                    }
                }else{
                    sigue=false;
                }
            }
        }
        //System.out.println(MejorRecorrido);
        System.out.println("el mejor recorrido es\n\t"+hormigas[MejorRecorrido].recorrido+"\n\tCon peso \t"+hormigas[MejorRecorrido].peso());
    }
    static void evaporaFeromonas(){
        //trabajando 
    }
    static void llenaFeromonas (double[][] feromonas,double fi){
        for (int i =0;i<feromonas[0].length;i++){
            for (int j =0;j<feromonas.length;j++){
                if(i!=j){
                    feromonas[i][j]=fi;
                }
            }
        }
    }
    static void leeDistancias(int[][] distancias, String arch) throws FileNotFoundException, IOException {
        File archivo = new File (arch);
        FileReader fr = new FileReader (archivo);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        int contador=0;
        int fila =0;
        int col = 0;
        while( (linea = br.readLine())!=null){
            contador++;
            if (linea.compareTo("EOF")!=0 &&  contador>=8){
                //linea =linea.substring(1, linea.length()-1);
                if (linea.substring(linea.length()-2, linea.length()-1).compareTo(" ")!=0){
                    linea +=" ";
                }
                //System.out.println(linea);
                String [] arr = linea.split(" ");
                //System.out.println(arr.length);
                for(int i =1;i<arr.length;i++){
                    distancias[fila][col]= Integer.parseInt(arr[i]);
                    distancias[col][fila]= Integer.parseInt(arr[i]);
                    //System.out.println("("+fila+", "+col+") "+distancias[fila][col]);
                    if (distancias[fila][col]==0){
                        col+=1;
                        fila=0;
                    }else{
                        fila+=1;
                    }
                }
            }
        }
    }
}

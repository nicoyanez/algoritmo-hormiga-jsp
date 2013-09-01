/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mejorrecorrido_entity;

/**
 *
 * @author nnn
 */
import java.util.ArrayList;

public class Hormiga {
    int[][] d;
    double[][] f;
    double alpha;
    double beta;
    ArrayList<Object> recorrido;

    public Hormiga(int[][] d, double[][] f, int ci, double alpha, double beta) {
        this.recorrido= new ArrayList<>();
        this.d = d;
        this.f = f;
        this.alpha = alpha;
        this.beta = beta;
        for (int i = 1; i < 17; i++) {
            this.recorrido.add(-1);
        }
        this.recorrido.set(0,ci);
    }
    public int peso() {
        int distancia = 0;
        for (int i = 1; i < this.recorrido.size(); i++) {
            if ( this.recorrido.get(i) != -1) {
                distancia += this.d[i - 1][i];
            }
        }
        if (this.recorrido.indexOf(-1)!=-1) {
            distancia += this.d[0][this.recorrido.size() - 1];
        }
        return distancia;
    }

    boolean avanza() {
        //el calculo de la pribabilidad de eleccion de ciudad
        //True si puede ingresar la ciudad
        //False si no se pueden ingresar mas ciudades
        if (this.recorrido.indexOf(-1)!=-1) {
            int ciudadActial = (int) this.recorrido.get(this.recorrido.indexOf(-1)-1);
            double probabilidad=-1.0;
            double temp=0.0;
            double numerador=0.0;
            double sumatoria=0.0;
            int ciudadElegida=-1;
            for (int i =0;i<this.recorrido.size();i++){
                if (this.recorrido.indexOf(i)==-1){
                    //((self.F[ciudadActual-1][i]*1.0)**self.alpha)*((self.D[ciudadActual-1][i]*1.0)**self.beta)
                    sumatoria+=Math.pow(this.f[ciudadActial][i], this.alpha)* Math.pow(this.d[ciudadActial][i], this.beta);
                }
            }
            for (int i =0;i<this.recorrido.size();i++){
                if (this.recorrido.indexOf(i)==-1){
                    //numerador=((self.F[ciudadActual-1][i]*1.0)**self.alpha)*((self.D[ciudadActual-1][i]*1.0)**self.beta)
                    //temp=numerador*1.0/sumatoria*1.0
                    numerador=Math.pow(this.f[ciudadActial][i], this.alpha)* Math.pow(this.d[ciudadActial][i], this.beta);
                    temp=numerador/sumatoria;
                    if (temp>probabilidad){
                        probabilidad=temp;
                        ciudadElegida=i;
                    }
                }
            }
            this.recorrido.set(this.recorrido.indexOf(-1), ciudadElegida);
            return true;
        }
        System.out.println("no puede ingresar mas ciudades al recorrido de la H "+this.recorrido.get(0));
        return false;
    }
}

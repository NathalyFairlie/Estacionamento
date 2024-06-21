/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estacionamento1;

import java.util.ArrayList;
import java.util.List;

public class Valores {

    private double hora1;
    private double hora2;
    private double hora3;
    private double hora6;
    private double hora12;
    private double diaria;
    private double mensal;
    private int id;
    

    public Valores(double hora1, double hora2, double hora3, double hora6, double hora12, double diaria, double mensal) {
        this.hora1 = hora1;
        this.hora2 = hora2;
        this.hora3 = hora3;
        this.hora6 = hora6;
        this.hora12 = hora12;
        this.diaria = diaria;
        this.mensal = mensal;
    }
    public static List<Valores> CadastroValores() {
        return new ArrayList<>();
    }

    public double getHora1() {
        return hora1;
    }

    public void setHora1(double hora1) {
        this.hora1 = hora1;
    }

    public double getHora2() {
        return hora2;
    }

    public void setHora2(double hora2) {
        this.hora2 = hora2;
    }

    public double getHora3() {
        return hora3;
    }

    public void setHora3(double hora3) {
        this.hora3 = hora3;
    }

    public double getHora6() {
        return hora6;
    }

    public void setHora6(double hora6) {
        this.hora6 = hora6;
    }

    public double getHora12() {
        return hora12;
    }

    public void setHora12(double hora12) {
        this.hora12 = hora12;
    }

    public double getDiaria() {
        return diaria;
    }

    public void setDiaria(double diaria) {
        this.diaria = diaria;
    }

    public double getMensal() {
        return mensal;
    }

    public void setMensal(double mensal) {
        this.mensal = mensal;
    }

}

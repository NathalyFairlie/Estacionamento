/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estacionamento1;

import java.util.ArrayList;
import java.util.List;

public class Desconto {

    private String nomeDesconto;
    private double desconto;

    public Desconto(String nomeDesconto, double desconto) {
        this.nomeDesconto = nomeDesconto;
        this.desconto = desconto;
    }
    
    public static List<Desconto> CadastroDesconto() {
        return new ArrayList<>();
    }

    public String getNomeDesconto() {
        return nomeDesconto;
    }

    public void setNomeDesconto(String nomeDesconto) {
        this.nomeDesconto = nomeDesconto;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

}

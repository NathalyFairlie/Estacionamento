/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estacionamento1;

import java.time.LocalDate;
import java.time.LocalTime;



public class Avulso {

    private String placa;
    private LocalDate data;
    private LocalTime entrada;
    private LocalTime saida;
    private double valorTotal;
    private double percentualDesconto;
    private String nomeDesconto;
    private double valorComDesconto;
    private int horasEstacionadas;

    // Construtor que recebe a placa como parâmetro
    public Avulso(String placa) {
        this.placa = placa;
        this.data = data;
        this.entrada = entrada;

    }

    public Avulso(String placa, LocalTime entrada, LocalDate data) {
         this.placa = placa;
        this.data = data;
        this.entrada = entrada;
        
    }

    public Avulso(String placa, LocalTime entrada, int horasEstacionadas) {
        this.placa = placa;
        this.data = data;
        this.entrada = entrada;
        this.saida = null;
        this.valorTotal = 0.0;
        this.percentualDesconto = 0.0;
        this.nomeDesconto = "";
        this.valorComDesconto = 0.0;
        this.horasEstacionadas = 0;
    }

    /*public static List<Avulso> estacionamentoAvulso() {
        return new ArrayList<>();
    }*/

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public String getNomeDesconto() {
        return nomeDesconto;
    }

    public void setNomeDesconto(String nomeDesconto) {
        this.nomeDesconto = nomeDesconto;
    }

    public double getValorComDesconto() {
        return valorComDesconto;
    }

    public void setValorComDesconto(double valorComDesconto) {
        this.valorComDesconto = valorComDesconto;
    }

    public int getHorasEstacionadas() {
        return horasEstacionadas;
    }

    public void setHorasEstacionadas(int horasEstacionadas) {
        this.horasEstacionadas = horasEstacionadas;
    }

    
    
}
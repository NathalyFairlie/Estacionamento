/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estacionamento1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mensalista {

    private String placa;
    private int mesesContratados;
    private LocalDate inicioContrato;
    private LocalDate fimContrato;
    private double valorTotal;
    private double percentualDesconto;
    private String nomeDesconto;
    private double valorComDesconto;
    private double desconto;

    public Mensalista(String placa) {
        this.placa = placa;

    }

    public Mensalista(String placa, LocalDate inicioContrato) {
        this.placa = placa;
        this.inicioContrato = inicioContrato;
    }

    public Mensalista(String placa, LocalDate inicioContrato, int mesesContratados) {
        this.placa = placa;
        this.mesesContratados = mesesContratados;
        this.inicioContrato = inicioContrato;
        this.fimContrato = calcularDataFinalVigenciaMensalista();
        this.valorTotal = 0.0; // Valor inicial
        this.nomeDesconto = "";
        this.percentualDesconto = 0.0; // Sem desconto por padrão
        this.valorComDesconto = 0.0; // Valor inicial
    }

    public LocalDate calcularDataFinalVigenciaMensalista() {
        if (this.inicioContrato != null) {
            return this.inicioContrato.plusMonths(mesesContratados);
        } else {
            // Handle the case where inicioContrato is null
            return null;
        }
    }

    public boolean verificarVigencia(LocalDate inicioContrato) {
        LocalDate dataFinalVigencia = calcularDataFinalVigenciaMensalista();
        return inicioContrato.isBefore(dataFinalVigencia);
    }

    public static List<Mensalista> CadastroMensalista() {
        return new ArrayList<>();
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    // Método para definir o desconto
    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    // Método para obter o percentual de desconto
    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getMesesContratados() {
        return mesesContratados;
    }

    public void setMesesContratados(int mesesContratados) {
        this.mesesContratados = mesesContratados;
    }

    public LocalDate getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(LocalDate inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public LocalDate getFimContrato() {
        return fimContrato;
    }

    public void setFimContrato(LocalDate fimContrato) {
        this.fimContrato = fimContrato;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorComDesconto() {
        return valorComDesconto;
    }

    public void setValorComDesconto(double valorComDesconto) {
        this.valorComDesconto = valorComDesconto;
    }

    public String getNomeDesconto() {
        return nomeDesconto;
    }

    public void setNomeDesconto(String nomeDesconto) {
        this.nomeDesconto = nomeDesconto;
    }

}

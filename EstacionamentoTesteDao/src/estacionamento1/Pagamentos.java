/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estacionamento1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pagamentos {
     private String placa;
     private LocalDate data;
     private double valorPago;

    public Pagamentos(String placa, LocalDate data, double valorPago) {
        this.placa = placa;
        this.data = data;
        this.valorPago = valorPago;
    }

    

    

     public static List<Pagamentos> pagamento() {
        return new ArrayList<>();
    }

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

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

   
    
}

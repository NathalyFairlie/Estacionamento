/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Pagamentos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoPagamentos {

    private Conexao conexao;
    private static final String TABELA_PAGAMENTO = "Pagamento";

    public DaoPagamentos() {
        this.conexao = new Conexao();
    }

    public void addPagamento(Pagamentos objetoPagamento) {
    String sql = "INSERT INTO Pagamentos (Placa, Data, ValorPago) VALUES (?, ?, ?)";
    try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, objetoPagamento.getPlaca());
        pstmt.setDate(2, java.sql.Date.valueOf(objetoPagamento.getData()));
        pstmt.setDouble(3, objetoPagamento.getValorPago());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public List<Pagamentos> listaPagamento() {
        List<Pagamentos> listaPagamento = new ArrayList<>();
        String sql = "SELECT Placa, Data, ValorPago FROM Pagamentos";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String placa = rs.getString("Placa");
                LocalDate data = rs.getDate("Data").toLocalDate();
                double valorPago = rs.getDouble("ValorPago");
                Pagamentos objetoPagamento = new Pagamentos(placa, data, valorPago);
                listaPagamento.add(objetoPagamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPagamento;
    }
   

    public void updatePagamento(List<Pagamentos> listaPagamento) {
         String sql = "UPDATE Pagamentos SET Data = ?, ValorPago = ? WHERE Placa = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Pagamentos objetoPagamento : listaPagamento) {
                pstmt.setDate(1, java.sql.Date.valueOf(objetoPagamento.getData()));
                pstmt.setDouble(2, objetoPagamento.getValorPago());
                pstmt.setString(3, objetoPagamento.getPlaca());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pagamentos getPagamentos(String placa) {
        Pagamentos pagamento = null;
        String sql = "SELECT * FROM " + TABELA_PAGAMENTO + " WHERE Placa=?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate data = rs.getDate("Data").toLocalDate();
                    double valorPago = rs.getDouble("ValorPago");
                    pagamento = new Pagamentos(placa, data, valorPago);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pagamento;
    }
}
    


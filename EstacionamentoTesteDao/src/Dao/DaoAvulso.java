/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Avulso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DaoAvulso {

    private final Conexao conexao;

    public DaoAvulso() {
        this.conexao = new Conexao();
    }

    public void addAvulso(Avulso objetoAvulso) {
        String query = "INSERT INTO avulso (Placa, Data, Entrada, Saida, HorasEstacionadas, ValorTotal, NomeDesconto, PercentualDesconto, ValorComDesconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, objetoAvulso.getPlaca());
            pstmt.setString(2, objetoAvulso.getData().toString());
            pstmt.setString(3, objetoAvulso.getEntrada().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            pstmt.setString(4, objetoAvulso.getSaida() != null ? objetoAvulso.getSaida().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null);
            pstmt.setInt(5, objetoAvulso.getHorasEstacionadas());
            pstmt.setDouble(6, objetoAvulso.getValorTotal());
            pstmt.setString(7, objetoAvulso.getNomeDesconto());
            pstmt.setDouble(8, objetoAvulso.getPercentualDesconto());
            pstmt.setDouble(9, objetoAvulso.getValorComDesconto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
      public Avulso getAvulso(String placa, LocalDate data) {
        String query = "SELECT * FROM avulso WHERE Placa = ? AND Data = ?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, placa);
            pstmt.setString(2, data.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LocalTime entrada = LocalTime.parse(rs.getString("Entrada"));
                    LocalTime saida = rs.getString("Saida") != null ? LocalTime.parse(rs.getString("Saida")) : null;
                    int horasEstacionadas = rs.getInt("HorasEstacionadas");
                    double valorTotal = rs.getDouble("ValorTotal");
                    String nomeDesconto = rs.getString("NomeDesconto");
                    double percentualDesconto = rs.getDouble("PercentualDesconto");
                    double valorComDesconto = rs.getDouble("ValorComDesconto");

                    Avulso avulso = new Avulso(placa);
                    avulso.setEntrada(entrada);
                    avulso.setSaida(saida);
                    avulso.setHorasEstacionadas(horasEstacionadas);
                    avulso.setValorTotal(valorTotal);
                    avulso.setNomeDesconto(nomeDesconto);
                    avulso.setPercentualDesconto(percentualDesconto);
                    avulso.setValorComDesconto(valorComDesconto);

                    return avulso;
                }
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Avulso> listaAvulso() {
        List<Avulso> carrosAvulsos = new ArrayList<>();

        String query = "SELECT * FROM avulso";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String placa = rs.getString("Placa");
                LocalDate data = LocalDate.parse(rs.getString("Data"));
                LocalTime entrada = LocalTime.parse(rs.getString("Entrada"));
                LocalTime saida = rs.getString("Saida") != null ? LocalTime.parse(rs.getString("Saida")) : null;
                int horasEstacionadas = rs.getInt("HorasEstacionadas");
                double valorTotal = rs.getDouble("ValorTotal");
                String nomeDesconto = rs.getString("NomeDesconto");
                double percentualDesconto = rs.getDouble("PercentualDesconto");
                double valorComDesconto = rs.getDouble("ValorComDesconto");

                Avulso objetoAvulso = new Avulso(placa);
                objetoAvulso.setData(data);
                objetoAvulso.setEntrada(entrada);
                objetoAvulso.setSaida(saida);
                objetoAvulso.setHorasEstacionadas(horasEstacionadas);
                objetoAvulso.setValorTotal(valorTotal);
                objetoAvulso.setNomeDesconto(nomeDesconto);
                objetoAvulso.setPercentualDesconto(percentualDesconto);
                objetoAvulso.setValorComDesconto(valorComDesconto);

                carrosAvulsos.add(objetoAvulso);
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
        }

        return carrosAvulsos;
    }

    public void updateAvulso(Avulso avulso) {
        String query = "UPDATE avulso SET Saida=?, HorasEstacionadas=?, ValorTotal=?, NomeDesconto=?, PercentualDesconto=?, ValorComDesconto=? WHERE Placa=? AND Data=? AND Entrada=?";
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, avulso.getSaida() != null ? avulso.getSaida().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null);
            pstmt.setInt(2, avulso.getHorasEstacionadas());
            pstmt.setDouble(3, avulso.getValorTotal());
            pstmt.setString(4, avulso.getNomeDesconto());
            pstmt.setDouble(5, avulso.getPercentualDesconto());
            pstmt.setDouble(6, avulso.getValorComDesconto());
            pstmt.setString(7, avulso.getPlaca());
            pstmt.setString(8, avulso.getData().toString());
            pstmt.setString(9, avulso.getEntrada().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeAvulso(String placa, LocalDate data, LocalTime entrada) {
    String query = "DELETE FROM avulso WHERE Placa=? AND Data=? AND Entrada=?";
    try (Connection conn = conexao.conectar();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, placa);
        pstmt.setString(2, data.toString());
        pstmt.setString(3, entrada.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
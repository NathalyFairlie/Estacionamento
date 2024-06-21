/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Desconto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoDesconto {

    // Utilizando a mesma classe de conexão para simplificar o exemplo
    private final Conexao conexao;

    public DaoDesconto() {
        this.conexao = new Conexao();
    }

    public void addDesconto(Desconto objetoDesconto) {
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Desconto (nomeDesconto, desconto) VALUES (?, ?)")) {
            pstmt.setString(1, objetoDesconto.getNomeDesconto());
            pstmt.setDouble(2, objetoDesconto.getDesconto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Desconto> listaDesconto() {
        List<Desconto> listaDesconto = new ArrayList<>();

        try (Connection conn = conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Desconto")) {

            while (rs.next()) {
                String nome = rs.getString("nomeDesconto");
                double desconto = rs.getDouble("desconto");
                Desconto objetoDesconto = new Desconto(nome, desconto);
                listaDesconto.add(objetoDesconto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaDesconto;
    }

    public void updateDesconto(List<Desconto> listaDesconto) {
        try (Connection conn = conexao.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM Desconto");

            for (Desconto objetoDesconto : listaDesconto) {
                addDesconto(objetoDesconto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerDesconto(String nomeDesconto) {
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Desconto WHERE nomeDesconto = ?")) {
            pstmt.setString(1, nomeDesconto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getDesconto(String nomeDesconto) {
        try (Connection conn = conexao.conectar();
             PreparedStatement pstmt = conn.prepareStatement("SELECT desconto FROM Desconto WHERE nomeDesconto = ?")) {
            pstmt.setString(1, nomeDesconto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("desconto");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}

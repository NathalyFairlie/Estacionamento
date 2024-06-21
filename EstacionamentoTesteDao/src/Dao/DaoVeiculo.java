/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoVeiculo {

    private final Conexao conexao;

    public DaoVeiculo() {
        this.conexao = new Conexao();
    }

    public void addVeiculo(Veiculo veiculo) throws SQLException {
        String query = "INSERT INTO veiculo (Placa, marca, modelo) VALUES (?, ?, ?)";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, veiculo.getPlaca());
            pstmt.setString(2, veiculo.getMarca());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.executeUpdate();
        }
    }

    public List<Veiculo> listaVeiculo() throws SQLException {
        String query = "SELECT * FROM veiculo";
        List<Veiculo> veiculos = new ArrayList<>();

        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String placa = rs.getString("Placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                Veiculo veiculo = new Veiculo(placa, marca, modelo);
                veiculos.add(veiculo);
            }
        }

        return veiculos;
    }

    public void updateVeiculo(Veiculo veiculo) throws SQLException {
        String query = "UPDATE veiculo SET marca=?, modelo=? WHERE Placa=?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, veiculo.getMarca());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setString(3, veiculo.getPlaca());
            pstmt.executeUpdate();
        }
    }

    public Veiculo getVeiculo(String placa) throws SQLException {
        Veiculo veiculo = null;

        String query = "SELECT * FROM veiculo WHERE Placa=?";
        try (Connection conn = conexao.conectar(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, placa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String marca = rs.getString("marca");
                    String modelo = rs.getString("modelo");
                    veiculo = new Veiculo(placa, marca, modelo);
                }
            }
        }

        return veiculo;
    }

}

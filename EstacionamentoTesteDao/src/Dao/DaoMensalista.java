/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Mensalista;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoMensalista {

    private final Conexao conexao;

    public DaoMensalista() {
        this.conexao = new Conexao();
    }

    public void addMensalista(Mensalista objetoMensalista) {
        String sql = "INSERT INTO mensalista (Placa, MesesContratados, InicioContrato, FimContrato, ValorTotal, NomeDesconto, PercentualDesconto, ValorComDesconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, objetoMensalista.getPlaca());
            stmt.setInt(2, objetoMensalista.getMesesContratados());

            // Verifica se objetoMensalista.getInicioContrato() não é nulo antes de converter para java.sql.Date
            if (objetoMensalista.getInicioContrato() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(objetoMensalista.getInicioContrato()));
            } else {
                // Trate o caso em que o inicioContrato é nulo (atribua um valor padrão ou lance uma exceção, dependendo da lógica do seu aplicativo)
                // Aqui, estamos simplesmente definindo como NULL no banco de dados
                stmt.setNull(3, java.sql.Types.DATE);
            }

            // Verifica se objetoMensalista.getFimContrato() não é nulo antes de converter para java.sql.Date
            if (objetoMensalista.getFimContrato() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(objetoMensalista.getFimContrato()));
            } else {
                // Trate o caso em que o fimContrato é nulo (atribua um valor padrão ou lance uma exceção, dependendo da lógica do seu aplicativo)
                // Aqui, estamos simplesmente definindo como NULL no banco de dados
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setDouble(5, objetoMensalista.getValorTotal());
            stmt.setString(6, objetoMensalista.getNomeDesconto());
            stmt.setDouble(7, objetoMensalista.getPercentualDesconto());
            stmt.setDouble(8, objetoMensalista.getValorComDesconto());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Mensalista> listaMensalista() {
        List<Mensalista> nomeListaMensalista = new ArrayList<>();
        String sql = "SELECT * FROM mensalista";
        try (Connection conn = conexao.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String placa = rs.getString("Placa");
                int mesesContratados = rs.getInt("MesesContratados");
                LocalDate inicioContrato = rs.getDate("InicioContrato") != null ? rs.getDate("InicioContrato").toLocalDate() : null;
                LocalDate fimContrato = rs.getDate("FimContrato") != null ? rs.getDate("FimContrato").toLocalDate() : null;
                double valorTotal = rs.getDouble("ValorTotal");
                String nomeDesconto = rs.getString("NomeDesconto");
                double percentualDesconto = rs.getDouble("PercentualDesconto");
                double valorComDesconto = rs.getDouble("ValorComDesconto");
                Mensalista objetoMensalista = new Mensalista(placa, inicioContrato, mesesContratados);
                objetoMensalista.setFimContrato(fimContrato);
                objetoMensalista.setValorTotal(valorTotal);
                objetoMensalista.setNomeDesconto(nomeDesconto);
                objetoMensalista.setPercentualDesconto(percentualDesconto);
                objetoMensalista.setValorComDesconto(valorComDesconto);
                nomeListaMensalista.add(objetoMensalista);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomeListaMensalista;
    }

    public void updateMensalista(Mensalista mensalista) {
    String sql = "UPDATE mensalista SET MesesContratados=?, InicioContrato=?, FimContrato=?, ValorTotal=?, NomeDesconto=?, PercentualDesconto=?, ValorComDesconto=? WHERE Placa=?";
    try (Connection conn = conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, mensalista.getMesesContratados());
        stmt.setDate(2, java.sql.Date.valueOf(mensalista.getInicioContrato()));
        stmt.setDate(3, java.sql.Date.valueOf(mensalista.getFimContrato()));
        stmt.setDouble(4, mensalista.getValorTotal());
        stmt.setString(5, mensalista.getNomeDesconto());
        stmt.setDouble(6, mensalista.getPercentualDesconto());
        stmt.setDouble(7, mensalista.getValorComDesconto());
        stmt.setString(8, mensalista.getPlaca());
        
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    }
    public void removeMensalista(String placa) {
    String sql = "DELETE FROM mensalista WHERE Placa=?";
    try (Connection conn = conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, placa);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public Mensalista getMensalista(String placa) {
    Mensalista mensalista = null;

    try {
        String sql = "SELECT * FROM mensalista WHERE Placa = ?";
        try (Connection conn = conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int mesesContratados = rs.getInt("MesesContratados");
                    LocalDate inicioContrato = rs.getDate("InicioContrato") != null ? rs.getDate("InicioContrato").toLocalDate() : null;
                    LocalDate fimContrato = rs.getDate("FimContrato") != null ? rs.getDate("FimContrato").toLocalDate() : null;
                    double valorTotal = rs.getDouble("ValorTotal");
                    String nomeDesconto = rs.getString("NomeDesconto");
                    double percentualDesconto = rs.getDouble("PercentualDesconto");
                    double valorComDesconto = rs.getDouble("ValorComDesconto");

                    mensalista = new Mensalista(placa, inicioContrato, mesesContratados);
                    mensalista.setFimContrato(fimContrato);
                    mensalista.setValorTotal(valorTotal);
                    mensalista.setNomeDesconto(nomeDesconto);
                    mensalista.setPercentualDesconto(percentualDesconto);
                    mensalista.setValorComDesconto(valorComDesconto);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return mensalista;
}}
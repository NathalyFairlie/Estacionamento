/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import estacionamento1.Valores;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoValores {

    private Conexao conexao;

    public DaoValores() {
         this.conexao = new Conexao();
    }

    // Método para adicionar valores ao banco de dados
    public void addValores(Valores objetoValores) {
        try (Connection conn = conexao.conectar();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO valores (hora1, hora2, hora3, hora6, hora12, diaria, mensal) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            statement.setDouble(1, objetoValores.getHora1());
            statement.setDouble(2, objetoValores.getHora2());
            statement.setDouble(3, objetoValores.getHora3());
            statement.setDouble(4, objetoValores.getHora6());
            statement.setDouble(5, objetoValores.getHora12());
            statement.setDouble(6, objetoValores.getDiaria());
            statement.setDouble(7, objetoValores.getMensal());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obter valores do banco de dados
    public List<Valores> listaValores() {
        List<Valores> valoresList = new ArrayList<>();

        try (Connection conn = conexao.conectar();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM valores")) {

            while (resultSet.next()) {
                double hora1 = resultSet.getDouble("hora1");
                double hora2 = resultSet.getDouble("hora2");
                double hora3 = resultSet.getDouble("hora3");
                double hora6 = resultSet.getDouble("hora6");
                double hora12 = resultSet.getDouble("hora12");
                double diaria = resultSet.getDouble("diaria");
                double mensal = resultSet.getDouble("mensal");

                Valores objetoValores = new Valores(hora1, hora2, hora3, hora6, hora12, diaria, mensal);
                valoresList.add(objetoValores);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valoresList;
    }

    // Método para atualizar valores no banco de dados
    public void updateValores(Valores objetoValores) {
        try (Connection conn = conexao.conectar();
             PreparedStatement statement = conn.prepareStatement("UPDATE valores SET hora1=?, hora2=?, hora3=?, hora6=?, hora12=?, diaria=?, mensal=? WHERE id=1")) {

            statement.setDouble(1, objetoValores.getHora1());
            statement.setDouble(2, objetoValores.getHora2());
            statement.setDouble(3, objetoValores.getHora3());
            statement.setDouble(4, objetoValores.getHora6());
            statement.setDouble(5, objetoValores.getHora12());
            statement.setDouble(6, objetoValores.getDiaria());
            statement.setDouble(7, objetoValores.getMensal());
           

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Valores getValores(int id) {
    Valores objetoValores = null;

    try (Connection conn = conexao.conectar();
         PreparedStatement statement = conn.prepareStatement("SELECT * FROM valores WHERE id = ?")) {

        statement.setInt(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                double hora1 = resultSet.getDouble("hora1");
                double hora2 = resultSet.getDouble("hora2");
                double hora3 = resultSet.getDouble("hora3");
                double hora6 = resultSet.getDouble("hora6");
                double hora12 = resultSet.getDouble("hora12");
                double diaria = resultSet.getDouble("diaria");
                double mensal = resultSet.getDouble("mensal");

                objetoValores = new Valores(hora1, hora2, hora3, hora6, hora12, diaria, mensal);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return objetoValores;
}
}
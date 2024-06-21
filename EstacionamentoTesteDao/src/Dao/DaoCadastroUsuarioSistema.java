/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Conexao.Conexao;
import Conexao.SenhaCripto;
import estacionamento1.CadastroUsuarioSistema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCadastroUsuarioSistema {

    private Connection conn;

    public DaoCadastroUsuarioSistema() {
        Conexao conexao = new Conexao();
        try {
            conn = conexao.conectar();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }

    }

    public void addCadastroUsuarioSistema(CadastroUsuarioSistema objetoCadastro) {
        try {
            String sql = "INSERT INTO cadastro_usuariosistema (Nome, Cargo, Login, Senha) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, objetoCadastro.getNome());
            stmt.setString(2, objetoCadastro.getCargo());
            stmt.setString(3, objetoCadastro.getLogin());
            stmt.setString(4, SenhaCripto.senhaCripto(objetoCadastro.getSenha())); // Criptografando a senha antes de inserir
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar cadastro de usuário", e);

        }
    }

    public List<CadastroUsuarioSistema> listaCadastroUsuarioSistema() {
        List<CadastroUsuarioSistema> nomeListaCadastro = new ArrayList<>();

        try {
            String sql = "SELECT * FROM cadastro_usuariosistema";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("Nome");
                String cargo = rs.getString("Cargo");
                String login = rs.getString("Login");
                String senha = rs.getString("Senha");
                CadastroUsuarioSistema objetoCadastro = new CadastroUsuarioSistema(nome, cargo, login, senha);
                nomeListaCadastro.add(objetoCadastro);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nomeListaCadastro;
    }

    public void updateCadastroUsuarioSistema(List<CadastroUsuarioSistema> nomeListaCadastro) {
        try {
            String sql = "TRUNCATE TABLE cadastro_usuariosistema";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

            for (CadastroUsuarioSistema objetoCadastro : nomeListaCadastro) {
                sql = "INSERT INTO cadastro_usuariosistema (Nome, Cargo, Login, Senha) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, objetoCadastro.getNome());
                stmt.setString(2, objetoCadastro.getCargo());
                stmt.setString(3, objetoCadastro.getLogin());
                stmt.setString(4, objetoCadastro.getSenha());
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CadastroUsuarioSistema getCadastro(String login) {
        CadastroUsuarioSistema usuarioSistema = null;

        try {
            String sql = "SELECT * FROM cadastro_usuariosistema WHERE Login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("Nome");
                String cargo = rs.getString("Cargo");
                String senha = rs.getString("Senha");

                usuarioSistema = new CadastroUsuarioSistema(nome, cargo, login, senha);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarioSistema;
    }

}

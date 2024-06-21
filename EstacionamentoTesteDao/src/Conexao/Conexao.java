/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexao;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexao {
    
    private Connection conn;
    
    private static final String URL = "jdbc:mysql://localhost:3306/estacionamento";
    private static final String USER = "root";
    private static final String PASSWORD = "F@1rl130803";
    

   public Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
             throw new SQLException("Driver não encontrado", ex);
        }
    }
    
    public void desconectar() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao desconectar: " + ex.getMessage());
        }
    }

   
}

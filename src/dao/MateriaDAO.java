package dao;

import modelo.Materia;
import db.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {
    public static List<Materia> listarTodos() throws SQLException {
        List<Materia> lista = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM materias");
        while (rs.next()) {
            lista.add(new Materia(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("creditos"),
                    rs.getString("profesor"),
                    rs.getString("alumno")));
        }
        rs.close();
        stmt.close();
        conn.close();
        return lista;
    }

    public static void agregar(Materia m) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO materias (nombre, creditos, profesor, alumno) VALUES (?, ?, ?, ?)");
        ps.setString(1, m.getNombre());
        ps.setInt(2, m.getCreditos());
        ps.setString(3, m.getProfesor());
        ps.setString(4, m.getAlumno());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public static void actualizar(Materia m) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE materias SET nombre=?, creditos=?, profesor=?, alumno=? WHERE id=?");
        ps.setString(1, m.getNombre());
        ps.setInt(2, m.getCreditos());
        ps.setString(3, m.getProfesor());
        ps.setString(4, m.getAlumno());
        ps.setInt(5, m.getId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public static void borrar(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM materias WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }
}
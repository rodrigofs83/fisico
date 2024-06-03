/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package academia.interfaces;

/**
 *
 * @author POSITIVO
 */
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    Boolean insert(T entity) throws SQLException;
    Boolean put(T entity) throws SQLException;
    Boolean delete(Integer id) throws SQLException;
    public List getAll() throws SQLException;
    public Optional getById( Integer id) throws SQLException;
}


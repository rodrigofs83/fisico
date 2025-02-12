/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package main.java.interfaces;

/**
 *
 * @author POSITIVO
 */
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T> {
    Boolean create(T entity) throws SQLException;
    Boolean update(T entity) throws SQLException;
    Boolean delete(T entity) throws SQLException;
    public List getAll() throws SQLException;
    public Optional find( Integer id) throws SQLException;
}


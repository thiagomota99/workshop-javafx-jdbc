package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDAO;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDAO{
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public DepartamentoDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Departamento obj) {
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO Department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, obj.getNome());
			
			int linhasAfetadas = preparedStatement.executeUpdate();
			if(linhasAfetadas > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				while(resultSet.next())
					obj.setId(resultSet.getInt(1));
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public void update(Departamento obj) {
		try {
			preparedStatement = connection.prepareStatement("UPDATE Department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			preparedStatement.setString(1, obj.getNome());
			preparedStatement.setInt(2, obj.getId());
			
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public void deleteById(Integer id) {
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM Department WHERE Id = ?");
			preparedStatement.setInt(1, id);
			
			int linhasAfetadas = preparedStatement.executeUpdate();
			if(linhasAfetadas == 0)
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public Departamento findById(Integer id) {
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM Department WHERE Id = ?");
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				Departamento dep = new Departamento(resultSet.getInt("Id"), resultSet.getString("Name"));
				return dep;
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
		return null;
	}

	@Override
	public List<Departamento> findAll() {
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM Department");
			
			resultSet = preparedStatement.executeQuery();
			
			List<Departamento> lista = new ArrayList<Departamento>();
			while(resultSet.next()) {
				lista.add(new Departamento(resultSet.getInt("Id"), resultSet.getString("Name")));				
			}
			return lista;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

}

package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.VendedorDAO;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoJDBC implements VendedorDAO{
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public VendedorDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void insert(Vendedor obj) {
		try {
			preparedStatement = connection.prepareStatement(""
					+ "INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, obj.getNome());
			preparedStatement.setString(2, obj.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(obj.getDataAniversario().getTime()));
			preparedStatement.setDouble(4, obj.getSalarioBase());
			preparedStatement.setInt(5, obj.getDepartamento().getId());
			
			int linhasAfetadas = preparedStatement.executeUpdate();
			if(linhasAfetadas > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					int id = resultSet.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
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
	public void update(Vendedor obj) {
		try {
			preparedStatement = connection.prepareStatement(""
					+ "UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, obj.getNome());
			preparedStatement.setString(2, obj.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(obj.getDataAniversario().getTime()));
			preparedStatement.setDouble(4, obj.getSalarioBase());
			preparedStatement.setInt(5, obj.getDepartamento().getId());
			preparedStatement.setInt(6, obj.getId());
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public void deleteById(Integer id) {
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			preparedStatement.setInt(1, id);
			if(preparedStatement.executeUpdate() == 0)
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
	}

	@Override
	public Vendedor findById(Integer id) {
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Departamento departamento = instanciarDepartamento(resultSet);
				Vendedor vendedor = instanciarVendedor(resultSet, departamento);
				return vendedor;
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

	private Vendedor instanciarVendedor(ResultSet resultSet, Departamento departamento) throws SQLException {
		Vendedor vendedor = new Vendedor();
		vendedor.setId(resultSet.getInt("Id"));
		vendedor.setNome(resultSet.getString("Name"));
		vendedor.setEmail(resultSet.getString("Email"));
		vendedor.setSalarioBase(resultSet.getDouble("BaseSalary"));
		vendedor.setDataAniversario(resultSet.getDate("BirthDate"));
		vendedor.setDepartamento(departamento);
		return vendedor;
	}

	private Departamento instanciarDepartamento(ResultSet resultSet) throws SQLException {
		Departamento departamento = new Departamento();
		departamento.setId(resultSet.getInt("DepartmentId"));
		departamento.setNome(resultSet.getString("DepName"));
		return departamento;
	}

	@Override
	public List<Vendedor> findAll() {
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "");
			
			resultSet = preparedStatement.executeQuery();
			
			List<Vendedor> listaVendedor = new ArrayList<Vendedor>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(resultSet.next()) {
				
				Departamento dep = map.get(resultSet.getInt("DepartmentId"));
				if(dep == null) {
					dep = instanciarDepartamento(resultSet);
					map.put(dep.getId(), dep);
				}
				
				Vendedor vendedor = instanciarVendedor(resultSet, dep);
				listaVendedor.add(vendedor);				
			}
			return listaVendedor;	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE department.Id = ?");
			
			preparedStatement.setInt(1, departamento.getId());
			resultSet = preparedStatement.executeQuery();
			
			List<Vendedor> listaVendedor = new ArrayList<Vendedor>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while(resultSet.next()) {
				
				Departamento dep = map.get(resultSet.getInt("DepartmentId"));
				if(dep == null) {
					dep = instanciarDepartamento(resultSet);
					map.put(dep.getId(), dep);
				}
				
				Vendedor vendedor = instanciarVendedor(resultSet, dep);
				listaVendedor.add(vendedor);				
			}
			return listaVendedor;	
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
			DB.closeResultSet(resultSet);
		}
	}	
}

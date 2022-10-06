package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDAO {

	public void insert(Vendedor obj);
	public void update(Vendedor obj);
	public void deleteById(Integer id);
	public Vendedor findById(Integer id);
	public List<Vendedor> findByDepartamento(Departamento departamento);
	public List<Vendedor> findAll();
}

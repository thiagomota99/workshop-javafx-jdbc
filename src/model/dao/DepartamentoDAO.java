package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDAO {
	
	public void insert(Departamento obj);
	public void update(Departamento obj);
	public void deleteById(Integer id);
	Departamento findById(Integer id);
	List<Departamento> findAll();
}

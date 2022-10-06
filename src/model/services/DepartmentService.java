package model.services;

import java.util.List;

import model.dao.DepartamentoDAO;
import model.dao.FabricaDAO;
import model.entities.Departamento;

public class DepartmentService {
	
	private DepartamentoDAO dao = FabricaDAO.createDepartamentoDao();
	
	public List<Departamento> findAll() {
		return dao.findAll();
	}
}

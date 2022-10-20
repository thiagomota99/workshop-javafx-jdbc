package model.services;

import java.util.List;

import model.dao.VendedorDAO;
import model.dao.FabricaDAO;
import model.entities.Vendedor;

public class SellerService {
	
	private VendedorDAO dao = FabricaDAO.createVendedorDao();
	
	public List<Vendedor> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Vendedor obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Vendedor obj) {
		dao.deleteById(obj.getId());
	}
}

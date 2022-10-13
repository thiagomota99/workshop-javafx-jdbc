package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Departamento;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Departamento entidade;
	
	private DepartmentService service;

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label lableErroNome;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
	}
	
	@FXML
	public void onBtnSalvarAction(ActionEvent evento) {
		if (entidade == null )
			throw new IllegalStateException("Entity was null");
		if (service == null)
			throw new IllegalStateException("Service was null");
		
		try {
			this.entidade = getFormData();
			service.saveOrUpdate(entidade);
			Utils.currentStage(evento).close();
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private Departamento getFormData() {
		Departamento obj = new Departamento();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		
		return obj;
	}

	@FXML
	public void onBtnCancelarAction(ActionEvent evento) {
		Utils.currentStage(evento).close();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormData() {
		if (entidade == null) //Verifica a dependência da entidade 
			throw new IllegalStateException("Entidade nula");
		
		txtId.setText(String.valueOf(entidade.getId())); //setando valore ao campo txtId
		txtNome.setText(entidade.getNome()); //setando valor ao campo txtNome
	}
}

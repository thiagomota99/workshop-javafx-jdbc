package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.entities.Vendedor;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Vendedor entidade;
	
	private SellerService service;
	
	private List<DataChangeListener> dataChangelisterners = new ArrayList<DataChangeListener>();

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
	
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangelisterners.add(listener);
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
			notifayDataChangeListeners();
			Utils.currentStage(evento).close();
		}
		catch (ValidationException e) {
			setErrorsMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void notifayDataChangeListeners() {
		for (DataChangeListener listener : dataChangelisterners)
			listener.onDataChanged();
	}

	private Vendedor getFormData() {
		Vendedor obj = new Vendedor();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		ValidationException exception = new ValidationException("Erro ao cadastrar o formulário");
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo nome precisa ser preenchido");
			throw exception;
		}	
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
	
	public void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet(); //Pegando todas as chaves do Map e adicionando Set fields
		
		if (fields.contains("nome")) {
			lableErroNome.setText(errors.get("nome"));
		}
	}
}

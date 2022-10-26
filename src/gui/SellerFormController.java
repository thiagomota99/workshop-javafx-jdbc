package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private Vendedor entidade;
	
	private SellerService service;
	
	private DepartmentService depatmentService;
	
	private List<DataChangeListener> dataChangelisterners = new ArrayList<DataChangeListener>();
	
	private ObservableList<Departamento> obsListDepartamento;

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtSalarioBase;
	
	@FXML
	private ComboBox<Departamento> cbDepartamento;
	
	@FXML
	private DatePicker dpDataNascimento;
	
	@FXML
	private Label lableErroNome;
	
	@FXML
	private Label lableErroEmail;
	
	@FXML
	private Label lableErroSalarioBase;
	
	@FXML
	private Label lableErroDataNascimento;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.depatmentService = departmentService;
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
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 100);
		Constraints.setTextFieldDouble(txtSalarioBase);
		Utils.formatDatePicker(dpDataNascimento, "dd/MM/yyyy");
		
		initializeComboBoxDepartment();
	}
	
	//Associar objetos Departamento do banco de dados ao combo box de Departamentos
	public void loadAssociatedObjects() {
		if(this.depatmentService == null)
			throw new IllegalStateException("DepartmentService is null");
		
		List<Departamento> list = this.depatmentService.findAll();
		this.obsListDepartamento = FXCollections.observableArrayList(list);
		this.cbDepartamento.setItems(obsListDepartamento);
	}
	
	public void updateFormData() {
		if (entidade == null) //Verifica a dependência da entidade 
			throw new IllegalStateException("Entidade nula");
		
		txtId.setText(String.valueOf(entidade.getId())); //setando valore ao campo txtId
		txtNome.setText(entidade.getNome()); //setando valor ao campo txtNome
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US); //Define o ponto como separador decimal
		txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));
		
		if(entidade.getDataAniversario() != null) //Verifica primerio se objeto do método getDataAniversario é nulo
			dpDataNascimento.setValue(LocalDate.ofInstant(entidade.getDataAniversario().toInstant(), ZoneId.systemDefault()));
		
		if(entidade.getDepartamento() != null)
			cbDepartamento.setValue(entidade.getDepartamento());
		else
			cbDepartamento.getSelectionModel().selectFirst();
	}
	
	public void setErrorsMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet(); //Pegando todas as chaves do Map e adicionando Set fields
		
		if (fields.contains("nome")) {
			lableErroNome.setText(errors.get("nome"));
		}
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbDepartamento.setCellFactory(factory);
		cbDepartamento.setButtonCell(factory.call(null));
	}	
}

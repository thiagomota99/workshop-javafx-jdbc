package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

public class DepartmentFormController implements Initializable {
	
	private Departamento entidade;

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
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
	}
	
	@FXML
	public void onBtnSalvarAction() {
		System.out.println("onBtnSalvarAction");
	}
	
	@FXML
	public void onBtnCancelarAction() {
		System.out.println("onBtnCancelarAction");
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

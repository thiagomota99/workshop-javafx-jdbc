package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

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
}

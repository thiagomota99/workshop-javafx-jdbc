package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartmentService;

public class DepartamentListController implements Initializable {
	
	private DepartmentService service;
	
	@FXML
	private TableView<Departamento> tableViewDepartaments;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnName;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Departamento obj = new Departamento();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow(); //Pega refer�ncia da janela
		tableViewDepartaments.prefHeightProperty().bind(stage.heightProperty()); //Define a altura da tabela conforme a altura da janela
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartaments.setItems(obsList);
	}
	
	private void createDialogForm(Departamento obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController(); //Pegando refer�ncia do controlador da view
			controller.setDepartamento(obj); //Injetando depend�ncia do departamento no controlador da view
			controller.setDepartmentService(new DepartmentService()); //Injetando depend�ncia do service no controlador da view
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); //Janela n�o pode ser redimensionada
			dialogStage.initOwner(parentStage); //Define o stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL); //Define o comportamento modal para a janela
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}
}

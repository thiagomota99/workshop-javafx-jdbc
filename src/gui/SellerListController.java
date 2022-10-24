package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.SellerService;

public class SellerListController implements Initializable {

	private SellerService service;

	@FXML
	private TableView<Vendedor> tableViewSellers;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnName;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEDIT;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;
	
	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Vendedor, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Vendedor, Double> tableColumnBaseSalary;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Vendedor obj = new Vendedor();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		
		//Formatando valores das colunas Data de Aniversário e Salário Base
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow(); // Pega referência da janela
		tableViewSellers.prefHeightProperty().bind(stage.heightProperty()); // Define a altura da tabela conforme a
																					// altura da janela
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Vendedor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSellers.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Vendedor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController(); // Pegando referência do controlador da view
			controller.setVendedor(obj); // Injetando dependência do departamento no controlador da view
			controller.setSellerService(new SellerService()); // Injetando dependência do service no controlador
																		// da view
			controller.subscribeDataChangeListener(() -> updateTableView());
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Cadastro de Vendedor");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // Janela não pode ser redimensionada
			dialogStage.initOwner(parentStage); // Define o stage pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL); // Define o comportamento modal para a janela
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	protected Object removeEntity(Vendedor obj) {
		if(service == null) {
			throw new IllegalStateException("service was null");
		}
		try {
			service.remove(obj);
			updateTableView();
		} catch (DbIntegrityException e) {
			Alerts.showAlert("Erro ao deletar departamento", null, e.getMessage(), AlertType.ERROR);
		}
		return null;
	}
}

package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("onMenuItemVendedorAction");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		//Implementando interface Consumer com a expressão lambda
		carregarView("/gui/DepartmentList.fxml", (DepartamentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	private synchronized <T> void carregarView(String absoluteName, Consumer<T> inicializar) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //Pega o primeiro elemento da View
			
			Node mainMenu = mainVBox.getChildren().get(0); //Pegar o primeiro filho do VBox principal (Barra de menu)
			mainVBox.getChildren().clear(); //Limpar todos os filhos do VBox principal
			mainVBox.getChildren().add(mainMenu); //Adicionar um filho ao VBox principal
			mainVBox.getChildren().addAll(newVBox.getChildren()); //Adicionar uma coleção de filhos ao VBox Principal
			
			T controller = loader.getController();
			inicializar.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onMenuItemSobreAction() {
		carregarView("/gui/About.fxml", x -> { }); //Passando como argumento uma função vazia
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {				
	}
}

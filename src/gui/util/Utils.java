package gui.util;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	//Respons�vel por capturar o stage de onde o controler (elemento gr�fico) est�
	public static Stage currentStage(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
}

package com.fam;

import com.fam.VO.Member;
import com.fam.VO.Task;
import com.fam.view.MainLoginController;
import com.fam.view.OverviewController;
import com.fam.view.RootLayoutController;
import com.fam.view.TaskEditDialogController;

import java.util.ArrayList;
import java.util.List;

import com.fam.MainApp;
//import com.fam.view.RootLayoutController;
//import com.fam.view.TaskEditDialogController;
import com.fam.BIZ.TaskBIZ;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private ObservableList<Task> taskData = FXCollections.observableArrayList();

	/**
	 * MainApp ������ ������ �� DB�� �ִ� �����͸� �����ͼ� taskData(ObservableList)�� �ְ� getter�޼��带 ����
	 * ObservableList �Ѱ���
	 */
	public MainApp() {

	}

	/**
	 * ���� �޼���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * getter method: ObservableList<Task> taskData
	 * 
	 * @return
	 */
	public ObservableList<Task> getTaskData() {

		List<Task> all = new ArrayList<>(); // DB�� TASK���̺� �ִ� ���� �� �����ͼ� all(List)���־���.
		all = new TaskBIZ().task_selectAll();
		// observableList taskData.add(<Task>)
		// -> for������ all(java.util.List)�� ����ִ�Task��ü ������ add�� argu�� �ֱ�
		for (int i = 0; i < all.size(); i++) {
			taskData.add(all.get(i));
		}

		return taskData;
	}

	/**
	 * ȭ�� ����
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Family To do list App");

		initRootLayout();
		showMainLogin();
		// showOverview();
	}

	/**
	 * rootLayout(BorderPane) ����
	 */
	private void initRootLayout() {
		try {
			// fxml ���Ͽ��� ���� ���̾ƿ��� �����´�.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// ���� ���̾ƿ��� �����ϴ� scene�� �����ش�.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// ��Ʈ�ѷ����� MainApp ���� ������ �ش�.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showMainLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/MainLogin.fxml"));
			AnchorPane mainLogin = (AnchorPane) loader.load();

			rootLayout.setCenter(mainLogin);

			MainLoginController loginController = loader.getController();
			loginController.setMainApp(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * rootLayout�� �ֿ���ȭ�� setting
	 */
	public void showOverview(Member member) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Overview.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			rootLayout.setCenter(overview);

			// ���� ���ø����̼��� ��Ʈ�ѷ��� �̿��� �� �ְ� �Ѵ�.
			OverviewController controller = loader.getController();
			controller.setMainApp(this, member);
			//controller.setMemeberId(memberId);// label�� ǥ��

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Stage getPrimarystage() {
		return primaryStage;
	}

	/**
	 * Task ������Ʈ�� ���� ���̾�α� ȭ�� �����ֱ�
	 * 
	 * @param task
	 * @return
	 */
	public boolean showTaskEditDialog(Task task) {
		try {
			System.out.println("showTaskEditDialog");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TaskEditDialog.fxml"));
			AnchorPane dialog = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Task");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(dialog);
			dialogStage.setScene(scene);

			TaskEditDialogController dialogController = loader.getController();
			dialogController.setDialogStage(dialogStage);
			dialogController.setTask(task);

			dialogStage.showAndWait();

			return dialogController.isOkClicked();

		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public void showSignUpDialog(Member member) {

	}
}

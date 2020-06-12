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
	 * MainApp 생성자 생성할 때 DB에 있는 데이터를 가져와서 taskData(ObservableList)에 넣고 getter메서드를 통해
	 * ObservableList 넘겨줌
	 */
	public MainApp() {

	}

	/**
	 * 메인 메서드
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

		List<Task> all = new ArrayList<>(); // DB의 TASK테이블에 있는 내용 다 가져와서 all(List)에넣었음.
		all = new TaskBIZ().task_selectAll();
		// observableList taskData.add(<Task>)
		// -> for문으로 all(java.util.List)에 들어있는Task객체 꺼내서 add의 argu로 넣기
		for (int i = 0; i < all.size(); i++) {
			taskData.add(all.get(i));
		}

		return taskData;
	}

	/**
	 * 화면 시작
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
	 * rootLayout(BorderPane) 설정
	 */
	private void initRootLayout() {
		try {
			// fxml 파일에서 상위 레이아웃을 가져온다.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// 상위 레이아웃을 포함하는 scene을 보여준다.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// 컨트롤러한테 MainApp 접근 권한을 준다.
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
	 * rootLayout에 주요기능화면 setting
	 */
	public void showOverview(Member member) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Overview.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			rootLayout.setCenter(overview);

			// 메인 애플리케이션이 컨트롤러를 이용할 수 있게 한다.
			OverviewController controller = loader.getController();
			controller.setMainApp(this, member);
			//controller.setMemeberId(memberId);// label에 표시

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Stage getPrimarystage() {
		return primaryStage;
	}

	/**
	 * Task 업데이트를 위한 다이얼로그 화면 보여주기
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

package com.fam.view;

import java.time.LocalDate;
import java.util.Vector;

import com.fam.MainApp;
import com.fam.VO.Data;
import com.fam.VO.Member;
import com.fam.VO.Task;
import com.fam.client.ChatClient;
import com.fam.BIZ.TaskBIZ; //view������ biz�� ����
//import com.fam.client.ChatClient;
import com.fam.util.DateUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class OverviewController {
	/**
	 * [!!!!]���� memberId�� �ش��ϴ� �����͸� �ҷ����µ� task�� familyId...�ְ�..������ �������� �� 
	 * SELECT * FROM task WHERE familyId = v_familyId�� �ؾ�... ���� to do list ������ ǥ��.
	 */

	/* ���̺�� */
	@FXML private TableView<Task> taskTable;
	@FXML private TableColumn<Task, String> categoryColumn;
	@FXML private TableColumn<Task, String> taskColumn;
	@FXML private TableColumn<Task, String> statusColumn;
	@FXML private TableColumn<Task, String> memberColumn;
	@FXML private TableColumn<Task, String> substituteColumn;
	@FXML private TableColumn<Task, LocalDate> startDateColumn;
	@FXML private TableColumn<Task, LocalDate> endDateColumn;
	@FXML private TableColumn<Task, Integer> likeNoColumn;

	@FXML private Label categoryLabel;
	@FXML private Label taskLabel;
	@FXML private Label statusLabel;
	@FXML private Label memberLabel;
	@FXML private Label substituteLabel;
	@FXML private Label startDateLabel;
	@FXML private Label endDateLabel;
	@FXML private Label memoLabel;
	@FXML private Label likeNoLabel;
	
	private ObservableList<Task> taskData = FXCollections.observableArrayList();
	private Stage dialogStage;
	private String mId;

	/* ä�� */
	@FXML private TextArea inputText;
	@FXML private TextArea outputText;
	//@FXML private Label userCountLabel;
	@FXML private TableView<Member> userTable;
	@FXML private Label testLabel;
	@FXML private TableColumn<Member, String> userColumn;

	private ObservableList<Member> userList = FXCollections.observableArrayList();
	
    private String serverIP = "127.0.0.1";
    private int port = 5555;
    private ChatClient client;


	// ���� ���ø����̼� ����
	private MainApp mainApp;

	// ������
	public OverviewController() {

	}

	/**
	 * ���̺�� �÷� �ʱ�ȭ
	 */
	@FXML
	private void initialize() {
				
		// TO DO LIST> TASK LIST ���̺� �ʱ�ȭ
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		taskColumn.setCellValueFactory(cellData -> cellData.getValue().taskProperty());
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
		memberColumn.setCellValueFactory(cellData -> cellData.getValue().memberProperty());
		substituteColumn.setCellValueFactory(cellData -> cellData.getValue().substituteProperty());
		startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
		endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
		likeNoColumn.setCellValueFactory(cellData -> cellData.getValue().likeNoProperty().asObject());

		showTaskDetails(null);

		taskTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showTaskDetails(newValue));
		
		// CHATTING> USER LIST ���̺� �ʱ�ȭ
		userColumn.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
	}



	public void setMainApp(MainApp mainApp, Member member) {
		this.mainApp = mainApp;
		// ���̺� observable ����Ʈ �����͸� �߰��Ѵ�.
		taskTable.setItems(mainApp.getTaskData());
		userTable.setItems(getUserList());
		
		client = new ChatClient(serverIP, port, member.getNickname(), this);
		client.start();

	}
	//CurrentUser ǥ��
	public void setMemeberId(String memberId) {
		testLabel.setText(memberId);
	}

	/**
	 * ���̺��� task�� �ϳ� �������� �� �ش� task�� �������� �����ش�.
	 * 
	 * @param task
	 */
	private void showTaskDetails(Task task) {
		if (task != null) {
			// task ��ü�� label�� ������ ä���.
			categoryLabel.setText(task.getCategory());
			taskLabel.setText(task.getTask());
			statusLabel.setText(task.getStatus());
			memberLabel.setText(task.getMember());
			substituteLabel.setText(task.getSubstitute());
			startDateLabel.setText(DateUtil.format(task.getStartDate()));
			endDateLabel.setText(DateUtil.format(task.getEndDate()));
			memoLabel.setText(task.getMemo());
			likeNoLabel.setText(Integer.toString(task.getLike()));

		} else {
			// task�� null�̸� ��� �ؽ�Ʈ�� �����.
			categoryLabel.setText("");
			taskLabel.setText("");
			statusLabel.setText("");
			memberLabel.setText("");
			substituteLabel.setText("");
			startDateLabel.setText("");
			endDateLabel.setText("");
			memoLabel.setText("");
			likeNoLabel.setText("");
		}
	}

	/**
	 * [NEW]��ư �̺�Ʈ -> ���ο� Task ��ü DB�� �ֱ�
	 */
	@FXML
	private void handleNewTask() {
		System.out.println("handleNewTask()");
		int res = 0;

		Task tempTask = new Task();
		System.out.println(tempTask.toString());
		boolean okClicked = mainApp.showTaskEditDialog(tempTask);
		if (okClicked) {
			System.out.println("���̾�α׿��� �Ѿ�� task ��ü: " + tempTask.toString());
			res = new TaskBIZ().task_insert(tempTask);
			System.out.println("handleNewTask: res=" + res);
			// TO DO: select all�� ���̺�信 �����ֱ�
			mainApp.getTaskData().add(tempTask);			
		}
	}

	/**
	 * [EDIT]��ư �̺�Ʈ -> ������Ʈ�� Task ��ü DB�� �ֱ�
	 */
	@FXML
	private void handleEditTask() {
		Task selectedTask = taskTable.getSelectionModel().getSelectedItem();

		if (selectedTask != null) {
			boolean okClicked = mainApp.showTaskEditDialog(selectedTask);
			if (okClicked) {
				int res = new TaskBIZ().task_update(selectedTask);
				showTaskDetails(selectedTask);
			}
		} else {
			// �ƹ� �͵� �������� �ʾ��� ���
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimarystage());
			alert.setTitle("No selection");
			alert.setHeaderText("No Task Selected");
			alert.setContentText("Please select a task in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * [DELETE]��ư �̺�Ʈ -> ���̺�信�� ���õ� Task ��ü DB���� �����ϱ�
	 */
	@FXML
	private void handleDeleteTask() {
		// ���̺��(����Ʈ)���� ������ ��ü�� �ε��� ��ȣ
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Task delTask = new Task();
			delTask = taskTable.getItems().get(selectedIndex);
			int delTaskNo = delTask.taskNoProperty().getValue().intValue();
			new TaskBIZ().task_delete(delTaskNo);
			mainApp.getTaskData().remove(selectedIndex);
			// TO DO: delTaskNo �� selectOne ��ü���� ������...�ƹ�ư sqlüũ �� ���� Ȯ���ϸ� ���̺�� �ٽ� �ҷ�����.

		} else {
			// ���� �޽����� �����ش�.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimarystage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Task Selected");
			alert.setContentText("Please select a task in the table");

			alert.showAndWait();
		}
	}

	/**
	 * ����ڰ� task�� ���ؼ� [LIKE]��ư�� ������ likeNo�� �����Ѵ�. TO DO: �α��� ������ �����ϰ��� ���̺��ϳ� ����
	 * user id/ ip �����ؾ� ��. �ߺ����� ������ �ʵ���
	 */
	@FXML
	private void handleLikeNo() {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Task selTask = new Task();
			selTask = taskTable.getItems().get(selectedIndex);
			int taskNo = selTask.taskNoProperty().getValue().intValue();
			int likeNo = selTask.likeNoProperty().getValue().intValue() + 1;// 1������Ŵ
			new TaskBIZ().task_updateLike(likeNo, taskNo);
		}
	}

	// -----------------------------------ä��---------------------------------------------
	/**
	 * ���� ������ Ŭ���̾�Ʈ�� ���� �ִ� userVector�� ���Ϲ޾Ƽ� ObservableList<String>�� �ִ´�.
	 */
	public ObservableList<Member> getUserList() {
		System.out.println("������� �Ծ�? vector�� ���ΰž�?");
		//Vector<String> temp = client.getUserVector();
		/*
		for(int i = 0; i < temp.size(); i++) {
			userList.add(new Member(temp.elementAt(i))); //ObservableList<String> userList
		}
		*/	
		return userList;
	}
	
	/**
	 * [SEND] ��ư ������ �� �۵�
	 */
	@FXML
	private void send() {
		String msg = inputText.getText();
		client.copyText(msg, Data.CHAT_MESSAGE);
		inputText.setText("");
		inputText.requestFocus();
	}
	/**
	 * EnterŰ�� ������ �� �̺�Ʈ �߻� -> send()�޼��� ȣ��
	 * @param event
	 */
	@FXML
	private void inputTextKeyEvent(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
        {
			send();
        }
	}

	/**
	 * ä��ȭ�鿡 msg ����
	 * @param clientMsg
	 */
	public void appendText(String clientMsg) {
		System.out.println("msg: " + clientMsg);
		outputText.appendText(clientMsg);
	}

	/**
	 * ���̾�α� ��������
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}

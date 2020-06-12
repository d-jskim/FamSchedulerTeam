package com.fam.view;

import java.time.LocalDate;
import java.util.Vector;

import com.fam.MainApp;
import com.fam.VO.Data;
import com.fam.VO.Member;
import com.fam.VO.Task;
import com.fam.client.ChatClient;
import com.fam.BIZ.TaskBIZ; //view에서는 biz와 연결
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
	 * [!!!!]현재 memberId에 해당하는 데이터를 불러오는데 task에 familyId...넣고..유저가 접속했을 때 
	 * SELECT * FROM task WHERE familyId = v_familyId로 해야... 가족 to do list 데이터 표시.
	 */

	/* 테이블뷰 */
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

	/* 채팅 */
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


	// 메인 애플리케이션 참조
	private MainApp mainApp;

	// 생성자
	public OverviewController() {

	}

	/**
	 * 테이블뷰 컬럼 초기화
	 */
	@FXML
	private void initialize() {
				
		// TO DO LIST> TASK LIST 테이블 초기화
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
		
		// CHATTING> USER LIST 테이블 초기화
		userColumn.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
	}



	public void setMainApp(MainApp mainApp, Member member) {
		this.mainApp = mainApp;
		// 테이블에 observable 리스트 데이터를 추가한다.
		taskTable.setItems(mainApp.getTaskData());
		userTable.setItems(getUserList());
		
		client = new ChatClient(serverIP, port, member.getNickname(), this);
		client.start();

	}
	//CurrentUser 표시
	public void setMemeberId(String memberId) {
		testLabel.setText(memberId);
	}

	/**
	 * 테이블에서 task를 하나 선택했을 때 해당 task의 상세정보를 보여준다.
	 * 
	 * @param task
	 */
	private void showTaskDetails(Task task) {
		if (task != null) {
			// task 객체로 label에 정보를 채운다.
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
			// task가 null이면 모든 텍스트를 지운다.
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
	 * [NEW]버튼 이벤트 -> 새로운 Task 객체 DB에 넣기
	 */
	@FXML
	private void handleNewTask() {
		System.out.println("handleNewTask()");
		int res = 0;

		Task tempTask = new Task();
		System.out.println(tempTask.toString());
		boolean okClicked = mainApp.showTaskEditDialog(tempTask);
		if (okClicked) {
			System.out.println("다이얼로그에서 넘어온 task 객체: " + tempTask.toString());
			res = new TaskBIZ().task_insert(tempTask);
			System.out.println("handleNewTask: res=" + res);
			// TO DO: select all로 테이블뷰에 보여주기
			mainApp.getTaskData().add(tempTask);			
		}
	}

	/**
	 * [EDIT]버튼 이벤트 -> 업데이트된 Task 객체 DB에 넣기
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
			// 아무 것도 선택하지 않았을 경우
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimarystage());
			alert.setTitle("No selection");
			alert.setHeaderText("No Task Selected");
			alert.setContentText("Please select a task in the table.");

			alert.showAndWait();
		}
	}

	/**
	 * [DELETE]버튼 이벤트 -> 테이블뷰에서 선택된 Task 객체 DB에서 삭제하기
	 */
	@FXML
	private void handleDeleteTask() {
		// 테이블뷰(리스트)에서 선택한 객체의 인덱스 번호
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {
			Task delTask = new Task();
			delTask = taskTable.getItems().get(selectedIndex);
			int delTaskNo = delTask.taskNoProperty().getValue().intValue();
			new TaskBIZ().task_delete(delTaskNo);
			mainApp.getTaskData().remove(selectedIndex);
			// TO DO: delTaskNo 가 selectOne 객체에서 없으면...아무튼 sql체크 후 삭제 확인하면 테이블뷰 다시 불러오기.

		} else {
			// 오류 메시지를 보여준다.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimarystage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Task Selected");
			alert.setContentText("Please select a task in the table");

			alert.showAndWait();
		}
	}

	/**
	 * 사용자가 task에 대해서 [LIKE]버튼을 누르면 likeNo가 증가한다. TO DO: 로그인 페이지 연결하고나서 테이블하나 만들어서
	 * user id/ ip 저장해야 함. 중복으로 누르지 않도록
	 */
	@FXML
	private void handleLikeNo() {
		int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Task selTask = new Task();
			selTask = taskTable.getItems().get(selectedIndex);
			int taskNo = selTask.taskNoProperty().getValue().intValue();
			int likeNo = selTask.likeNoProperty().getValue().intValue() + 1;// 1증가시킴
			new TaskBIZ().task_updateLike(likeNo, taskNo);
		}
	}

	// -----------------------------------채팅---------------------------------------------
	/**
	 * 현재 접속한 클라이언트가 갖고 있는 userVector를 리턴받아서 ObservableList<String>에 넣는다.
	 */
	public ObservableList<Member> getUserList() {
		System.out.println("여기까지 왔어? vector가 널인거야?");
		//Vector<String> temp = client.getUserVector();
		/*
		for(int i = 0; i < temp.size(); i++) {
			userList.add(new Member(temp.elementAt(i))); //ObservableList<String> userList
		}
		*/	
		return userList;
	}
	
	/**
	 * [SEND] 버튼 눌렀을 때 작동
	 */
	@FXML
	private void send() {
		String msg = inputText.getText();
		client.copyText(msg, Data.CHAT_MESSAGE);
		inputText.setText("");
		inputText.requestFocus();
	}
	/**
	 * Enter키를 눌렀을 때 이벤트 발생 -> send()메서드 호출
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
	 * 채팅화면에 msg 쓰기
	 * @param clientMsg
	 */
	public void appendText(String clientMsg) {
		System.out.println("msg: " + clientMsg);
		outputText.appendText(clientMsg);
	}

	/**
	 * 다이얼로그 스테이지
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
}

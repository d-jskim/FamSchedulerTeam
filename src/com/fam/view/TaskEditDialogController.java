package com.fam.view;


import com.fam.VO.Task;
import com.fam.util.DateUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskEditDialogController {

	@FXML private TextField categoryField;
	@FXML private TextField taskField;
	@FXML private ComboBox<String> statusBox;
	@FXML private TextField memberField;
	@FXML private TextField substituteField;
	@FXML private DatePicker startDateField;
	@FXML private DatePicker endDateField;
	@FXML private TextField memoField;
	@FXML private Label likeLabel;
	
	private ObservableList<String> statusList = FXCollections.observableArrayList("Not started", "In progress", "Completed");

	private Stage dialogStage;
	private Task task;
	private boolean okClicked = false;
	
	@FXML
	public void initialize() {
		statusBox.setItems(statusList);
		
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setTask(Task task) {
		this.task = task;
		
		categoryField.setText(task.getCategory());
		taskField.setText(task.getTask());
		statusBox.setPromptText(task.getStatus());
		memberField.setText(task.getMember());
		substituteField.setText(task.getSubstitute());
		startDateField.setPromptText(DateUtil.format(task.getStartDate()));
		endDateField.setPromptText(DateUtil.format(task.getEndDate()));
		memoField.setText(task.getMemo());
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	
	@FXML
	private void handleOk() {

		if(isInputValid()) {
			task.setCategory(categoryField.getText());
			task.setTask(taskField.getText());
			task.setStatus(statusBox.getValue().toString());
			task.setMember(memberField.getText());
			task.setSubstitute(substituteField.getText());
			task.setStartDate(DateUtil.parse(startDateField.getPromptText()));
			task.setEndDate(DateUtil.parse(endDateField.getPromptText()));
			task.setMemo(memoField.getText());
			
			okClicked = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * 다이얼로그 창에 입력한 값이 유효한지 확인하는 메서드
	 * @return
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		
		if(categoryField.getText() == null || categoryField.getText().length() == 0) {
			errorMessage += "No valid category name";
		}
		if(taskField.getText() == null || taskField.getText().length() == 0){
			errorMessage += "No valid task name";
		}
		if(statusBox.getValue().toString() == null || statusBox.getValue().toString().length() == 0){
			errorMessage += "No valid status name";
		}
		if(memberField.getText() == null || memberField.getText().length() == 0){
			errorMessage += "No valid member name";
		}
		if(substituteField.getText() == null || substituteField.getText().length() == 0){
			errorMessage += "No valid substitute name";
		}
		if(startDateField.getPromptText() == null || startDateField.getPromptText().length() == 0){
			errorMessage += "No valid start date";
		}else {
			if(!DateUtil.validDate(startDateField.getPromptText())) {
				errorMessage += "No valie start date.\n";
			}
		}
		if(endDateField.getPromptText() == null || endDateField.getPromptText().length() == 0){
			errorMessage += "No valid end date";
		}else {
			if(!DateUtil.validDate(endDateField.getPromptText())) {
				errorMessage += "No valie end date. \n";
			}
		}
		if(memoField.getText() == null || memoField.getText().length() == 0){
			errorMessage += "No valid memo";
		}
		
		if(errorMessage.length() == 0) {
			return true;
		}else {
			//오류 메시지 창 
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
		
	}
}

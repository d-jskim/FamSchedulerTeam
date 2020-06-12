package com.fam.view;


import com.fam.MainApp;
import com.fam.VO.Member;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpDialogController {
	
	 	@FXML private TextField memberIdField;
	    @FXML private TextField passwordField;
	    @FXML private TextField familyIdField;
	    @FXML private TextField nicknameField;
	 
	  	private Stage dialogStage;
	    private Member member;
	    private boolean okClicked = false;
	    
	   
	    /**
	     * ���� ���ø����̼� ����
	     */
	    private MainApp mainApp;


	    /**
	     * ������.initialize() �޼��� ������ ȣ��ȴ�.
	     */
	    public SignUpDialogController() {}

	    //��Ʈ�ѷ� Ŭ������ �ʱ�ȭ�Ѵ�.�� �޼���� fxml ������ �ε�� �� �ڵ����� ȣ��ȴ�.
	    /**
	     * ��Ʈ�ѷ� Ŭ������ �ʱ�ȭ�Ѵ�.�� �޼���� fxml ������ �ε�� �� �ڵ����� ȣ��ȴ�.
	     */
	    @FXML
	    private void initialize() {}

	    //���̾�α��� ���������� �����Ѵ�.
	    public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	    
	   //ȸ������ â
	    public void setMember(Member member) {
	        this.member = member;
	        memberIdField.setText(member.getMemberId());
	        passwordField.setText(member.getPassword());
	        familyIdField.setText(Integer.toString(member.getFamilyId()));
	        nicknameField.setText(member.getNickname());  
	    }
	    
	    public boolean isOkClicked() {
	        return okClicked;
	    }
	    
	    @FXML
	    private void handleOk() {
	        if (isInputValid()) {
	        	member.setMemberId(memberIdField.getText());
	        	member.setPassword(passwordField.getText());
	        	member.setFamilyId(Integer.parseInt(familyIdField.getText()));
	        	member.setNickname(nicknameField.getText());
	        	/*
	        	int res = new MemberBiz().getInsert(member);
	        	if (res >0 ) {
					System.out.println("�Է� ����!");
				}
				*/
	        	okClicked = true;
	            dialogStage.close();
	        }
	    }

	    /**
	     * ����ڰ� cancel�� Ŭ���ϸ� ȣ��ȴ�.
	     */
	    @FXML
	    private void handleCancel() {
	        dialogStage.close();
	        }

	    /**
	     * �ؽ�Ʈ �ʵ�� ����� �Է��� �˻��Ѵ�.
	     *
	     * @return true if the input is valid
	     */
	    private boolean isInputValid() {
	        String errorMessage = "";

	        if (memberIdField.getText() == null || memberIdField.getText().length() == 0) {
	            errorMessage += "No valid id!\n";
	        }
	        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
	            errorMessage += "No valid last password!\n";
	        }
	        if (familyIdField.getText() == null || familyIdField.getText().length() == 0) {
	            errorMessage += "No valid family id!\n";
	        }

	        if (nicknameField.getText() == null || nicknameField.getText().length() == 0) {
	            errorMessage += "No valid nickname!\n";
	        } 

	        if (errorMessage.length() == 0) {
	            return true;
	            
	        } else {
	            // ���� �޽����� �����ش�.
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

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
	     * 메인 애플리케이션 참조
	     */
	    private MainApp mainApp;


	    /**
	     * 생성자.initialize() 메서드 이전에 호출된다.
	     */
	    public SignUpDialogController() {}

	    //컨트롤러 클래스를 초기화한다.이 메서드는 fxml 파일이 로드된 후 자동으로 호출된다.
	    /**
	     * 컨트롤러 클래스를 초기화한다.이 메서드는 fxml 파일이 로드된 후 자동으로 호출된다.
	     */
	    @FXML
	    private void initialize() {}

	    //다이얼로그의 스테이지를 설정한다.
	    public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	    
	   //회원가입 창
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
					System.out.println("입력 성공!");
				}
				*/
	        	okClicked = true;
	            dialogStage.close();
	        }
	    }

	    /**
	     * 사용자가 cancel을 클릭하면 호출된다.
	     */
	    @FXML
	    private void handleCancel() {
	        dialogStage.close();
	        }

	    /**
	     * 텍스트 필드로 사용자 입력을 검사한다.
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
	            // 오류 메시지를 보여준다.
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

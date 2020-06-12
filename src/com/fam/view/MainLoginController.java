package com.fam.view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.fam.MainApp;
import com.fam.BIZ.MemberBIZ;
//import com.biz.MemberBiz;
//import com.famApp.view.ChatClient;
//import com.model.Member;
import com.fam.VO.Member;
import com.fam.client.ChatClient;


public class MainLoginController {
	
	
    private MainApp mainApp;
   
	@FXML private TextField memberIdField;
    @FXML private TextField passwordField;
    @FXML private TextField familyIDField;
       
    private Stage dialogStage;
    private boolean okClicked = false;
    private Member member;

    @FXML
    private void initialize() {
    	
    }
    
    /**
     * 메인 애플리케이션 참조
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMember(Member loginMember) {
        this.member = loginMember;
        memberIdField.setText(loginMember.getMemberId());
        passwordField.setText(loginMember.getPassword());
        familyIDField.setText(Integer.toString(loginMember.getFamilyId()));
        
    }
    
    
    public boolean isOkClicked() {
        return okClicked;}
    
    @FXML
    private void handleJoin() {
    	Member member = null;
    	if (isInputValid()) {
    		System.out.println("join 눌렀을 때 메서드");
    		member = new Member(memberIdField.getText(), passwordField.getText(), Integer.parseInt(familyIDField.getText()));

    		System.out.println(member.toString());
    		int rs = new MemberBIZ().getLogIn(member);
    		System.out.println("biz 갔다 왔니??" + rs);
    		if (rs == 1) {
    			okClicked = true;
    			
    			System.out.println("user가 입력한 이메일 : " + member.getMemberId());
    			member = new MemberBIZ().member_selectVO(member.getMemberId());
    			System.out.println("로그인 후에 memberId 비교해서 받아온 객체 ");
    			System.out.println(member.toString());
        	    mainApp.showOverview(member);	   			
    	}

    		
    		}else {
    			Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText("wrong id or pw");
                alert.showAndWait();

    		}
    		
    	}
     

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
   
    @FXML
    private void handleSignUp() {
    	//Member newMember = new Member();
        //mainApp.showSignUpDialog(newMember);
     } 
   

    private boolean isInputValid() {
        String errorMessage = "";

        if (memberIdField.getText() == null || memberIdField.getText().length() == 0) {
            errorMessage += "No valid id!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "No valid last password!\n";
        }
        
        if (familyIDField.getText() == null || familyIDField.getText().length() == 0) {
            errorMessage += "No valid family id!\n";
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

    
    /**
     * 애플리케이션을 닫는다.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

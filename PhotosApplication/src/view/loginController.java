package view;
/**
 * @author ktt43
 * Controller for Login Functions.
 * Checks for existing users
 * username: admin
 * 
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

import view.adminController;

public class loginController{

	@FXML TextField usernameInput;
	@FXML Button loginButton;
	
	Alert a = new Alert(AlertType.WARNING);
	public static String userToLogin;
	String username;
	public static ArrayList<User> listUsers = new ArrayList<User>();
	File userFile = new File("data/user.dat");
	
public void start(Stage ps) throws IOException{
	//System.out.println("Login Controller Running");
	//String test = new File(".").getAbsolutePath();
	//System.out.println(test);	
	
	if(!userFile.exists()) {
	try {
		stockUser();
		listUsers = User.readApp();
	} catch (ClassNotFoundException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	
	} else {
		try {
			listUsers = User.readApp();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
/**
 * Login Button
 * admin
 * stock
 * Created users	
 */
	loginButton.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			username = usernameInput.getText().trim();
			
			if(username.isEmpty()) {
			//	System.out.println("Empty Input");
				return;
			} else if(username.equals("admin")) {
				
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/adminMain.fxml"));
					Parent adminMain = (Parent)loader.load();			
					Scene scene = new Scene(adminMain);
					ps.setScene(scene);
					ps.setTitle("Photo Library");
					ps.setResizable(false);  
					ps.show();
					
					adminController adminController = loader.getController();
					adminController.start(ps);
					return;
				} catch (IOException e1) {
				//	System.out.println("Error on calling admin Controller From loginController");
				//	e1.printStackTrace();
				}
			} else {
				for(User u : listUsers) {
					if(u.getUsername().equals(usernameInput.getText().trim())){
						userToLogin=u.getUsername();
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userMain.fxml"));
							Parent userMain = (Parent)loader.load();			
							Scene scene = new Scene(userMain);
							ps.setScene(scene);
							ps.setTitle("Photo Library");
							ps.setResizable(false);  
							ps.show();
							
							userController userController = loader.getController();
							userController.start(ps);
							return;
					} catch (IOException e1) {
					//	System.out.println("Error on calling userController From loginController");
						//e1.printStackTrace();
					}
				}
				}
				
				a.setAlertType(AlertType.ERROR);
				a.setContentText("Username does not exist");
				a.show();

			}
			 

			
			
			
		}
	});
	

	
}

/**
* Creates Stock User
*/
public void stockUser() {
File file;
	
User stock = new User("stock");
Album stockAlbum= new Album("stock");
stock.addAlbum(stockAlbum);


//File file = new File("data/A.jpg");
//Image image = new Image(file.toURI().toString());
//System.out.println(file.getName());
Calendar date = Calendar.getInstance();

Photo A = new Photo("A.jpg");
A.addTag(new Tag("Alphabet", "A"));
A.setCaption("This is Letter A");
A.setPhotoDirectory("/data/A.jpg");
file = new File("data/A.jpg");
A.setPhotoDirectory(file.toURI().toString());
date.setTimeInMillis(file.lastModified());
date.set(Calendar.MILLISECOND,0);
A.setDate(date.getTime());

Photo B = new Photo("B.jpg");
B.addTag(new Tag("Alphabet", "B"));
B.setCaption("This is Letter B");
B.setPhotoDirectory("/data/B.jpg");
file = new File("data/B.jpg");
B.setPhotoDirectory(file.toURI().toString());
date.setTimeInMillis(file.lastModified());
date.set(Calendar.MILLISECOND,0);
B.setDate(date.getTime());


Photo C = new Photo("C.jpg");
C.addTag(new Tag("Alphabet", "C"));
C.setCaption("This is Letter C");
C.setPhotoDirectory("/data/C.jpg");
file = new File("data/C.jpg");
C.setPhotoDirectory(file.toURI().toString());
date.setTimeInMillis(file.lastModified());
date.set(Calendar.MILLISECOND,0);
C.setDate(date.getTime());

Photo D = new Photo("D.jpg");
D.addTag(new Tag("Alphabet", "D"));
D.setCaption("This is Letter D");
D.setPhotoDirectory("/data/D.jpg");
file = new File("data/D.jpg");
D.setPhotoDirectory(file.toURI().toString());
date.setTimeInMillis(file.lastModified());
date.set(Calendar.MILLISECOND,0);
D.setDate(date.getTime());


Photo E = new Photo("E.jpg");
E.addTag(new Tag("Alphabet", "E"));
E.setCaption("This is Letter E");
E.setPhotoDirectory("/data/E.jpg");
file = new File("data/E.jpg");
E.setPhotoDirectory(file.toURI().toString());
date.setTimeInMillis(file.lastModified());
date.set(Calendar.MILLISECOND,0);
E.setDate(date.getTime());

stockAlbum.addPhoto(A);
stockAlbum.addPhoto(B);
stockAlbum.addPhoto(C);
stockAlbum.addPhoto(D);
stockAlbum.addPhoto(E);
//stockAlbum.addPhoto(F);

adminController.getUserList().add(stock);



try {
	User.writeApp(adminController.getUserList());
} catch (IOException e) {
	// TODO Auto-generated catch block
	//e.printStackTrace();
}

ArrayList<User> x;
try {
	x = User.readApp();
		
} catch (ClassNotFoundException e) {
	// TODO Auto-generated catch block
	//e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	//e.printStackTrace();
}


}
}
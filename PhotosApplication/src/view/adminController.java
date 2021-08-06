package view;
/**
 * @author ktt43
 * 
 * Controller for the Admin Function.
 * Creates Users
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

public class adminController {
	
	@FXML ListView<String> userList;
	@FXML Button createUserButton;
	@FXML Button deleteUserButton;
	@FXML Button logoutButton;
	@FXML TextField newUser;
	@FXML Button exitButton;
	
	private int currentUserIndex;
	
	private ObservableList<String> obsList = FXCollections.observableArrayList();
	private static ArrayList<User> allCurrentUsers = new ArrayList<User>();
	Alert a= new Alert(AlertType.ERROR);
	
	
	public void start(Stage ps) throws IOException{
		//System.out.println("adminController running");
		try {
			currentUserIndex = userList.getSelectionModel().getSelectedIndex();
		}catch (NullPointerException e) {
			//System.out.println("Empty list");
		}
		//Load All users from Data into an ArrayList
		try {
			allCurrentUsers = User.readApp();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		userList.setItems(obsList);
		showUsers();
		
	
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`Buttons
	exitButton.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			System.exit(0);
		}
	});
		
		
		/**
		 * Logout Button. Back to Login Screen
		 * Creates Stock User if nonexistent
		 */
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
					Parent out = (Parent)loader.load();
					
					Scene scene = new Scene(out);
					ps.setScene(scene);
					ps.setTitle("Photo Library");
					ps.setResizable(false);  
					ps.show();
					
				loginController loginController = loader.getController();
				loginController.start(ps);
				
				} catch (IOException e1) {
				//	System.out.println("Error on calling admin Controller From loginController");
				//	e1.printStackTrace();
				}
			}
		});
		
		/**
		 * Create user Button
		 */
		createUserButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(newUser.getText().isBlank()) {
					;
				} else {
					//Check Dupicate Usernames
					
					for(User user: allCurrentUsers) {
						if((user.getUsername().toLowerCase()).equals(newUser.getText().trim().toLowerCase())){					
							a.setContentText("Duplicate username");
							a.show();
							newUser.clear();
							return;
						}
					}
				
					
					User n = new User(newUser.getText());
					createStockAlbum(n);
					allCurrentUsers.add(n);
					try {
						User.writeApp(allCurrentUsers);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}
					showUsers();
					userList.getSelectionModel().select(newUser.getText());
					
					currentUserIndex= userList.getSelectionModel().getSelectedIndex();
					newUser.clear();
				}
				
				
			}
		});
		
		/**
		 * Delete User Button
		 * Selected User will be Deleted 
		 */
		deleteUserButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(userList.getSelectionModel().isSelected(currentUserIndex)) {
					String userTobeDeleted= userList.getSelectionModel().getSelectedItem();
					a.setAlertType(AlertType.CONFIRMATION);
					a.setContentText("Are you sure you want to delete: "+ userTobeDeleted);
					 Optional <ButtonType> confirm = a.showAndWait();
					 if(confirm.get() == ButtonType.OK) {
					allCurrentUsers.remove(currentUserIndex);
					
					try {
						User.writeApp(allCurrentUsers);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}
					 }
				}
				
								showUsers();
			}
		});
		
		
		/**
		 * Get Selected User index from list
		 */
		
			userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					currentUserIndex = userList.getSelectionModel().getSelectedIndex();
				}
			});
		
		
	}


	/**
	 * Show all Users saved in the Observable List
	 */
	
	
	public void showUsers() {
		userList.getItems().clear();
		
		for(User user : allCurrentUsers) {
			obsList.add(user.getUsername());
		}
	}
	
	/**
	 * Return an ArrayList of Users
	 * @return
	 */
	
	public static ArrayList<User> getUserList(){
		return allCurrentUsers;
	}
	
	/**
	 * Creates the Stock Album for new user
	 * @param user
	 */
	public void createStockAlbum(User user) {
		File file;
			
		//User stock = new User("stock");
		Album stockAlbum= new Album("stock");
		user.addAlbum(stockAlbum);


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

		//adminController.getUserList().add(user);



		}
	
	
	

}

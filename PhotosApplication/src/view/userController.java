package view;
/**
 * @author ktt43
 * 
 * Controller for handling functions of the User
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

public class userController {
	
		@FXML ListView<String> albumList;
		@FXML ListView<String> albumInfo;
		@FXML Button createButton;
		@FXML Button deleteButton;
		@FXML Button openButton;
		@FXML Button renameButton;
		@FXML Button logoutButton;
		@FXML Button exitButton;
		@FXML TextField newAlbumInput;
		@FXML TextField newNameInput;
	
		private int currentAlbumIndex;
		
		private ObservableList<String> obsList = FXCollections.observableArrayList();
		Alert alert = new Alert(AlertType.WARNING);
		
		User currentUser;
		
		public void start(Stage ps) throws IOException {		
			//System.out.println("userController running");
			try {
				currentAlbumIndex = albumList.getSelectionModel().getSelectedIndex();
			}catch (NullPointerException e) {
			//	System.out.println("Empty list");
			}
			
			
			
			//Get Logged in Users information
			for(User u : loginController.listUsers) {
				if(u.getUsername().equals(loginController.userToLogin)) {
					currentUser = u;
					break;
				}
			}

			try {
				currentAlbumIndex = albumList.getSelectionModel().getSelectedIndex();
			}catch (NullPointerException e) {
				//System.out.println("Empty list");
			}
			
			albumList.setItems(obsList);
			showAlbums();
	
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
					//	System.out.println("Error on calling loginController From userController");
					//	e1.printStackTrace();
					}
				}
			});
			exitButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.exit(0);
				}
			});
			createButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(newAlbumInput.getText().isBlank()) {
						;
					} else {
						Album newAlbum = new Album(newAlbumInput.getText().trim());
						if(!checkDupAlbum(newAlbum.getAlbumName())) {
							alert.setAlertType(AlertType.INFORMATION);
							alert.setContentText("New Album Successfully created");
							alert.show();
							currentUser.addAlbum(newAlbum);
							newAlbumInput.clear();
							save();
						} else {
							alert.setAlertType(AlertType.ERROR);
							alert.setContentText("Album name Already Exists");
							alert.show();
							newAlbumInput.clear();
							return;
						}
					}
					newAlbumInput.clear();
					showAlbums();
				}
			});
			/**
			 * Delete Selected Album from list
			 */
			deleteButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(albumList.getSelectionModel().isSelected(currentAlbumIndex)) {
						alert.setAlertType(AlertType.CONFIRMATION);
						alert.setContentText("Confirm Deletion");
						 Optional <ButtonType> confirm = alert.showAndWait();
						 if(confirm.get() == ButtonType.OK) {
							 currentUser.getAllAlbums().remove(currentAlbumIndex);
							 save();
						 }
					}
						showAlbums();
						albumInfo.getItems().clear();
						try {
							albumList.getSelectionModel().clearSelection();
							currentAlbumIndex = albumList.getSelectionModel().getSelectedIndex();
							
						}catch (NullPointerException a) {
							//System.out.println("Empty list");
						}
						
				}
			});
			/**
			 * Rename Selected Album in list.
			 * No Album of same name
			*/ 
			renameButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					for(Album a : currentUser.getAllAlbums()) {
						if((a.getAlbumName().toLowerCase()).equals(newNameInput.getText().trim().toLowerCase())) {
							alert.setAlertType(AlertType.ERROR);
							alert.setContentText("Album of same name already exists");
							alert.show();
							newNameInput.clear();
							return;
						}
					}
					//No duplicate name found
					try {
					currentUser.getAllAlbums().get(currentAlbumIndex).setAlbumName(newNameInput.getText().trim());
					save();
					showAlbums();
					}catch(IndexOutOfBoundsException a) {
						System.out.println("Nothing is Selected");
					}
					newNameInput.clear();
				}			
			});
			
			openButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					Album tobeOpened = new Album("No Album");
if(albumList.getSelectionModel().isSelected(currentAlbumIndex)) {
	tobeOpened=currentUser.getAllAlbums().get(currentAlbumIndex);
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoMain.fxml"));
		Parent toPho = (Parent)loader.load();
		
		Scene scene = new Scene(toPho);
		ps.setScene(scene);
		ps.setTitle("Photo Library");
		ps.setResizable(false);  
		ps.show();
		
	photoController photoController = loader.getController();
	photoController.initData(tobeOpened,currentUser);
	photoController.start(ps);
	
	} catch (IOException e1) {
	//	System.out.println("Error on calling photoController From userController");
	//	e1.printStackTrace();
	}

} else {
	alert.setAlertType(AlertType.ERROR);
	alert.setContentText("No Album Selected to be opened");
	alert.show();
}
				
				}				
			});
			
			
			/**
			 * Get Selected Album index from list
			 */
			albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					try {
						currentAlbumIndex = albumList.getSelectionModel().getSelectedIndex();
						Platform.runLater(() -> showAlbumInfo());
					}catch(IndexOutOfBoundsException e) {
			//			System.out.println("Nothing is Selected");
					}
					}
			});
		
		}
		
		
		
		/**
		 * Show all Albums saved for user
		 */		
		public void showAlbums() {
			albumList.getItems().clear();
			
			for(Album album : currentUser.getAllAlbums()) {
				obsList.add(album.getAlbumName());
			}
			albumList.getSelectionModel().select(currentAlbumIndex);
		}
		
		/**
		 * Show the information, Number of Photos and Date Range, of the Selected Album
		 */
		public void showAlbumInfo() {
			albumInfo.getItems().clear();
			try {
			Album selectedAlbum = currentUser.getAllAlbums().get(currentAlbumIndex);
			albumInfo.getItems().add("# of Photos: "+currentUser.getAllAlbums().get(currentAlbumIndex).getSize());

			try {
			albumInfo.getItems().add(getEarly(selectedAlbum) + " to "+ getLatest(selectedAlbum));
			} catch(NoSuchElementException e) {
				//System.out.println("No Photos");
				albumInfo.getItems().add("Add some Photos!");
			}
			}catch(IndexOutOfBoundsException e) {
				//System.out.println("Nothing Selected");
			}
			
		}
		
		/**
		 * Gets the Latest Date for the param Album
		 * @param a
		 * @return
		 */
		public Date getLatest(Album a) {
			ArrayList<Photo> plist = currentUser.getAlbumfromList(a.getAlbumName()).getPhotoList();
			ArrayList<Date> dlist = new ArrayList<Date>();
			for(Photo p : plist) {
				dlist.add(p.getDate());
			}
			Date eDate = Collections.max(dlist);
			return eDate;
		}
		/**
		 * Gets the Earliest Date for the param Album
		 * @param a
		 * @return
		 */
		public Date getEarly(Album a) {
			ArrayList<Photo> plist = currentUser.getAlbumfromList(a.getAlbumName()).getPhotoList();
			ArrayList<Date> dlist = new ArrayList<Date>();
			for(Photo p : plist) {
				dlist.add(p.getDate());
			}
			Date eDate = Collections.min(dlist);
			return eDate;
			
		}
		
		/**
		 * Save Data
		 */
		public void save() {
			try {
				User.writeApp(loginController.listUsers);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		
		/**
		 * Checks for Duplicate Album Names
		 * True : Duplicates
		 * False : No Duplicates
		 * @param name
		 * @return
		 */
		public boolean checkDupAlbum(String name) {
			for(Album a : currentUser.getAllAlbums()) {
				if((a.getAlbumName().toLowerCase()).equals(name.toLowerCase())) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Get the index of Specified Album
		 * @param albumName
		 * @return
		 */
		
		public int getIndexofAlbum(String albumName) {
			return currentUser.getAllAlbums().indexOf(currentUser.getAlbumfromList(albumName));
			}
		
		
		}


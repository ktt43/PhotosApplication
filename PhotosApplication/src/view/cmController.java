package view;

/**
 * @author ktt43
 * 
 * Controller for Copy and Move Function screen
 */

import java.io.IOException;

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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;

public class cmController {
	@FXML Button copyButton;
	@FXML Button moveButton;
	@FXML Button backButton;
	@FXML Button logoutButton;
	@FXML ListView<String> albumList;
	@FXML Button exitButton;
	Album openedAlbum;
	User currentUser;
	Photo selectedPhoto;
	Alert a = new Alert(AlertType.ERROR);
	private int currentAlbumIndex;
	
	private ObservableList<String> obsList = FXCollections.observableArrayList();

	public void initData(Album a,User u,Photo p) {
		this.openedAlbum=a;
		this.currentUser=u;
		this.selectedPhoto=p;
	}
	
	public void start(Stage ps) throws IOException{
		albumList.setItems(obsList);
		showAlbums();
	
		copyButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(albumList.getSelectionModel().isSelected(currentAlbumIndex)) {
					if(openedAlbum.getAlbumName().equals(currentUser.getAllAlbums().get(currentAlbumIndex).getAlbumName())) {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("This is the same Album");
						a.show();
						return;
					}
					Photo newPhoto = selectedPhoto;
					
					if(!currentUser.getAllAlbums().get(currentAlbumIndex).checkDuplicatepPhotoName(newPhoto)){
						currentUser.getAllAlbums().get(currentAlbumIndex).addPhoto(newPhoto);
						a.setAlertType(AlertType.INFORMATION);
						a.setContentText("Copied");
						a.show();
						save();
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoMain.fxml"));
							Parent back = (Parent)loader.load();
							
							Scene scene = new Scene(back);
							ps.setScene(scene);
							ps.setTitle("Photo Library");
							ps.setResizable(false);  
							ps.show();
							
						photoController photoController = loader.getController();
						photoController.initData(openedAlbum, currentUser);
						photoController.start(ps);
						
						} catch (IOException e1) {
						//	System.out.println("Error on calling login Controller From photoController");
						//	e1.printStackTrace();
						}
						
					}else {
					a.setAlertType(AlertType.ERROR);
					a.setContentText("Either No Album was Selected or Photo name Already exists");
					a.show();
					}
					}
			}
		});
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
		moveButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(albumList.getSelectionModel().isSelected(currentAlbumIndex)) {
					if(openedAlbum.getAlbumName().equals(currentUser.getAllAlbums().get(currentAlbumIndex).getAlbumName())) {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("This is the same Album");
						a.show();
						return;
					}
					Photo newPhoto = selectedPhoto;
					if(!currentUser.getAllAlbums().get(currentAlbumIndex).checkDuplicatepPhotoName(newPhoto)){
						currentUser.getAllAlbums().get(currentAlbumIndex).addPhoto(newPhoto);
						openedAlbum.getPhotoList().remove(selectedPhoto);
						a.setAlertType(AlertType.INFORMATION);
						a.setContentText("Photo Moved");
						a.show();
						save();
						try {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoMain.fxml"));
							Parent back = (Parent)loader.load();
							
							Scene scene = new Scene(back);
							ps.setScene(scene);
							ps.setTitle("Photo Library");
							ps.setResizable(false);  
							ps.show();
							
						photoController photoController = loader.getController();
						photoController.initData(openedAlbum, currentUser);
						photoController.start(ps);
						
						} catch (IOException e1) {
							//System.out.println("Error on calling login Controller From photoController");
							//e1.printStackTrace();
						}
						
					} else {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("Either No Album was Selected or Photo name Already exists");
						a.show();
					}
					
			}
			}
		});
		
		albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					currentAlbumIndex = albumList.getSelectionModel().getSelectedIndex();
				}catch(IndexOutOfBoundsException e) {
					//System.out.println("Nothing is Selected");
				}
				}
		});
		
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
					//System.out.println("Error on calling login Controller From photoController");
					//e1.printStackTrace();
				}
			}
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoMain.fxml"));
					Parent back = (Parent)loader.load();
					
					Scene scene = new Scene(back);
					ps.setScene(scene);
					ps.setTitle("Photo Library");
					ps.setResizable(false);  
					ps.show();
					
				photoController photoController = loader.getController();
				photoController.initData(openedAlbum, currentUser);
				photoController.start(ps);
				
				} catch (IOException e1) {
					//System.out.println("Error on calling login Controller From photoController");
					//e1.printStackTrace();
				}
			}
		});
	}

	public void showAlbums() {
		albumList.getItems().clear();
		
		for(Album album : currentUser.getAllAlbums()) {
			obsList.add(album.getAlbumName());
		}
		albumList.getSelectionModel().select(currentAlbumIndex);
	}

	/**
	 * Save Data
	 */
	
	public void save() {
		try {
			User.writeApp(loginController.listUsers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

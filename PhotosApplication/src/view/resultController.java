package view;
/**
 * @author ktt43
 * 
 * Controller for handling results For Searches
 */
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;


public class resultController {
	@FXML ListView<Photo> photoList;
	@FXML Button createButton;
	@FXML TextField albumNameInput;
	@FXML Button logoutButton;
	@FXML Button backButton;
	@FXML Button prevButton;
	@FXML Button nextButton;
	@FXML ImageView displayArea;
	@FXML Button exitButton;
	
	int currentPhotoIndex;
	User currentUser;
	Album currentAlbum;
	ArrayList<Photo> resultPhoto;
	ObservableList<Photo> obsPhotoList;
	ObservableList<String> obsTagList;
	
	Alert alert = new Alert(AlertType.CONFIRMATION);
	
	
	/**
	 * Passes in User,ArrayList<Photo>, and an Album to be used on the next screen
	 * @param u
	 * @param p
	 * @param currentAlbum
	 */
	public void initData(User u,ArrayList<Photo> p,Album currentAlbum) {
		this.currentUser=u;
		this.resultPhoto=p;
		this.currentAlbum=currentAlbum;
	}
	
	public void start(Stage ps) throws IOException{
		photoList.setCellFactory(param -> new PhotoCell());
		obsPhotoList = FXCollections.observableArrayList(resultPhoto);
		photoList.setItems(obsPhotoList);
		
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(albumNameInput.getText().isBlank()) {
					alert.setAlertType(AlertType.ERROR);
					alert.setContentText("New Album needs name");
					alert.show();
					return;
				}
				
				for(Album a : currentUser.getAllAlbums()) {
					if(a.getAlbumName().toLowerCase().equals(albumNameInput.getText().trim())) {
						alert.setAlertType(AlertType.ERROR);
						alert.setContentText("Album of same name Already Exists");
						alert.show();
						return;
					}
				}
				
				alert.setAlertType(AlertType.CONFIRMATION);
				alert.setContentText("Are you sure you want to create Album from results");
				 Optional <ButtonType> confirm = alert.showAndWait();
				 if(confirm.get() == ButtonType.OK) {
					 Album newAlbum = new Album(albumNameInput.getText().trim());
					 for(Photo p : resultPhoto) {
					 newAlbum.addPhoto(p);
				}
					currentUser.addAlbum(newAlbum);
					save();
					backButton.fire();
				 }
			}
		});
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});
		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(currentPhotoIndex < resultPhoto.size()) {
					photoList.getSelectionModel().selectNext();
					currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
					if(!photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
						photoList.getSelectionModel().selectFirst();
						currentPhotoIndex=photoList.getSelectionModel().getSelectedIndex();
					}
					Image newImage = new Image(resultPhoto.get(currentPhotoIndex).getPhotoDirectory());
					displayArea.setImage(newImage);
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
				photoController.initData(currentAlbum, currentUser);
				photoController.start(ps);
				
				} catch (IOException e1) {
				//	System.out.println("Error on calling login Controller From photoController");
				//	e1.printStackTrace();
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
				//	System.out.println("Error on calling loginController From userController");
				//	e1.printStackTrace();
				}
			}
		});
		
		prevButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(currentPhotoIndex < resultPhoto.size()) {
					photoList.getSelectionModel().selectPrevious();			
					currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
					if(!photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
						photoList.getSelectionModel().selectLast();
						currentPhotoIndex=photoList.getSelectionModel().getSelectedIndex();
					}
					Image newImage = new Image(resultPhoto.get(currentPhotoIndex).getPhotoDirectory());
					displayArea.setImage(newImage);
				} 
				
						
				
			}
		});

		photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
					
					if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
					try {
						displayPhoto();
						
					}catch(NullPointerException npe) {
					}

					}
				}catch(IndexOutOfBoundsException e) {
				//	System.out.println("Nothing is Selected");
				}
				}
		});
	}
	/**
	 * Save Data
	 */

	public void save() {
		try {
			User.writeApp(loginController.listUsers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
		
	}
	
	/**
	 * Display Photo onto seperate Area enlarged
	 */
	public void displayPhoto() {
	
		Image newImage = new Image(resultPhoto.get(currentPhotoIndex).getPhotoDirectory());
		displayArea.setImage(newImage);
		
	}
	/**
	 * Responsible for loading thumbnail onto the listview
	 * 
	 *
	 */
	private class PhotoCell extends ListCell<Photo>{
		private ImageView imageView = new ImageView();
		@Override
		protected void updateItem(Photo item, boolean empty) {
			super.updateItem(item,empty);
			
			if(empty || item == null) {
				imageView.setImage(null);
				setGraphic(null);
				setText(null);
			} else {
				
				
				imageView.setImage(new Image(item.getPhotoDirectory()));
				imageView.setFitHeight(100);
				imageView.setFitWidth(100);
				setText("Caption: " + item.getCaption()
						+ "\nDate Taken: " + item.getDate()
						);
				setGraphic(imageView);
			
			}
		}
		
		
}
}

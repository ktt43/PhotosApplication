package view;
/**
 * @author ktt43
 * 
 * Controller for Photos. 
 */
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;
import model.User;

public class photoController {

	@FXML ListView<Photo> photoList;
	@FXML ListView<String> tagList;
	@FXML Button addButton;
	@FXML Button removeButton;
	@FXML Button captionButton;
	@FXML Button addTagButton;
	@FXML Button deleteTagButton;
	@FXML Button displayButton;
	@FXML Button copymoveButton;
	@FXML Button slideshowButton;
	@FXML Button searchDateButton;
	@FXML Button searchTagButton;
	@FXML Button backButton;
	@FXML Button logoutButton;
	@FXML Button nextButton;
	@FXML Button prevButton;
	@FXML TextField captionInput;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	@FXML ImageView displayArea;
	
	@FXML TextField fromDate;
	@FXML TextField toDate;
	@FXML RadioButton orRadio;
	@FXML RadioButton andRadio;
	@FXML ToggleGroup group1;
	@FXML ChoiceBox<String> tagName1;
	@FXML ChoiceBox<String> tagName2;
	@FXML ChoiceBox<String> tagValue1;
	@FXML ChoiceBox<String> tagValue2;
	@FXML RadioButton disableRadio;
	@FXML Button exitButton;
	
	Album openedAlbum;
	User currentUser;
		
	ObservableList<Photo> obsPhotoList;
	ObservableList<String> obsTagList;
	public int currentPhotoIndex;
	int currentTagIndex;
	ArrayList<Photo> resultList = new ArrayList<Photo>();
	ArrayList<String> uniqueTags= new ArrayList<String>();
	ArrayList<String> uniqueValues= new ArrayList<String>();
	SimpleDateFormat sdf;
	
	Alert a = new Alert(AlertType.ERROR);
	//Array
	
	/**
	 * Get Selected Album from Previous Scene and its User
	 * @param a
	 * @param u
	 */
	public void initData(Album a,User u) {
		this.openedAlbum=a;
		this.currentUser=u;
	}
	
	public void start(Stage ps) throws IOException{
		//System.out.println("PhotoController Running");
		//System.out.println(openedAlbum.getAlbumName()+" in photoController.");
		obsPhotoList = FXCollections.observableArrayList(openedAlbum.getPhotoList());
		orRadio.setToggleGroup(group1);
		andRadio.setToggleGroup(group1);
		disableRadio.setToggleGroup(group1);
		
		disableRadio.setSelected(true);
		tagName2.setDisable(true);
		tagValue2.setDisable(true);
		
		
photoList.setCellFactory(param -> new PhotoCell());
photoList.setItems(obsPhotoList);

updateTags();

exitButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		System.exit(0);
	}
});

searchTagButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		ArrayList<Tag> tag1 = new ArrayList<Tag>();
		ArrayList<Tag> tag2 = new ArrayList<Tag>();
			if(tagName1.getSelectionModel().getSelectedItem()!=null && tagValue1.getSelectionModel().getSelectedItem()!=null) {
				String tag1Name = tagName1.getSelectionModel().getSelectedItem().trim().toLowerCase();
				String tag1Value = tagValue1.getSelectionModel().getSelectedItem().trim().toLowerCase();
				if(disableRadio.isSelected()) {
					for(Photo p : openedAlbum.getPhotoList()) {
						tag1=p.getTags();
						for(Tag t: tag1) {
							if(t.getTagName().toLowerCase().trim().equals(tag1Name)&&t.getValueName().toLowerCase().trim().equals(tag1Value)) {
								resultList.add(p);
								break;
							}
						}
					}
					
				} else if(orRadio.isSelected()) {
					if(tagName2.getSelectionModel().getSelectedItem()!=null && tagValue2.getSelectionModel().getSelectedItem()!=null) {
						String tag2Name = tagName2.getSelectionModel().getSelectedItem().trim().toLowerCase();
						String tag2Value = tagValue2.getSelectionModel().getSelectedItem().trim().toLowerCase();
						for(Photo p : openedAlbum.getPhotoList()) {
							tag1=p.getTags();
							for(Tag t:tag1) {
								if(((t.getTagName().toLowerCase().trim().equals(tag1Name))&&(t.getValueName().toLowerCase().trim().equals(tag1Value)))
									||((t.getTagName().toLowerCase().trim().equals(tag2Name))&&(t.getValueName().toLowerCase().trim().equals(tag2Value)))) {
									resultList.add(p);
									break;
								}
							}
						}
					} else {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("Please Fill in Second Criteria or Select Dis/Conjunctive");
						a.show();
						return;
					}
				} else if(andRadio.isSelected()) {
					if(tagName2.getSelectionModel().getSelectedItem()!=null && tagValue2.getSelectionModel().getSelectedItem()!=null) {
						String tag2Name = tagName2.getSelectionModel().getSelectedItem().trim().toLowerCase();
						String tag2Value = tagValue2.getSelectionModel().getSelectedItem().trim().toLowerCase();
						for(Photo p : openedAlbum.getPhotoList()) {
							tag1=p.getTags();
							boolean c1=false,c2=false;
							for(Tag t:tag1) {
							
								
								if(((t.getTagName().toLowerCase().trim().equals(tag1Name))&&(t.getValueName().toLowerCase().trim().equals(tag1Value)))) {
									c1=true;
								}
								if(((t.getTagName().toLowerCase().trim().equals(tag2Name))&&(t.getValueName().toLowerCase().trim().equals(tag2Value)))) {
									c2=true;
								}
								
							if(c1 && c2) {
								resultList.add(p);
								break;
							}
							}
						}
					} else {
						a.setAlertType(AlertType.ERROR);
						a.setContentText("Please completely fill in both criteria or select disable dis/conjuctive");
						a.show();
						return;
					}
				}
				
				
			} else {
				a.setAlertType(AlertType.ERROR);
				a.setContentText("Please Fill in First Criteria");
				a.show();
				return;
			}
			
			if(resultList.isEmpty()) {
				a.setAlertType(AlertType.INFORMATION);
				a.setContentText("No Results Found Given filters");
				a.show();
			} else {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchResult.fxml"));
					Parent toResult = (Parent)loader.load();
					
					Scene scene = new Scene(toResult);
					ps.setScene(scene);
					ps.setTitle("Photo Library");
					ps.setResizable(false);  
					ps.show();
					
				resultController resultController = loader.getController();
				resultController.initData(currentUser,resultList,openedAlbum);
				resultController.start(ps);
				
				} catch (IOException e1) {
					//System.out.println("Error on calling resultController From photoController");
					//e1.printStackTrace();
				}
			}
			
		 
		
		
	}
});


orRadio.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(orRadio.isSelected()) {
		tagName2.setDisable(false);
		tagValue2.setDisable(false);
		}
	}
});
andRadio.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(andRadio.isSelected()) {
		tagName2.setDisable(false);
		tagValue2.setDisable(false);
		}
	}
});



disableRadio.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(disableRadio.isSelected()) {
		tagName2.setDisable(true);
		tagValue2.setDisable(true);
		}
	}
});
captionButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
			openedAlbum.getPhotoList().get(currentPhotoIndex).setCaption(captionInput.getText().trim());
			save();
			captionInput.clear();
			updateDisplay();
		}
	}
});

searchDateButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(fromDate.getText().isBlank() || toDate.getText().isBlank()) {
			a.setAlertType(AlertType.ERROR);
			a.setContentText("Please Fill in From Date and To Date");
			a.show();
			return;
		}
		String fDate = fromDate.getText().trim();
		String tDate = toDate.getText().trim();
		
		sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date frDate = sdf.parse(fDate);
			Date toDate = sdf.parse(tDate);
			
			if(frDate.after(toDate)) {
				Date tempDate = frDate;
				frDate = toDate;
				toDate = tempDate;
			}
			
			for(Photo p : openedAlbum.getPhotoList()) {
				if((p.getDate().after(frDate) || p.getDate().equals(frDate))&&(p.getDate().before(toDate)||p.getDate().equals(toDate))) {
					resultList.add(p);
				}
			}
			
			if(resultList.isEmpty()) {
				a.setAlertType(AlertType.INFORMATION);
				a.setContentText("No Results Found For the Date Range");
				a.show();
			} else {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchResult.fxml"));
					Parent toResult = (Parent)loader.load();
					
					Scene scene = new Scene(toResult);
					ps.setScene(scene);
					ps.setTitle("Photo Library");
					ps.setResizable(false);  
					ps.show();
					
				resultController resultController = loader.getController();
				resultController.initData(currentUser,resultList,openedAlbum);
				resultController.start(ps);
				
				} catch (IOException e1) {
					//System.out.println("Error on calling resultController From photoController");
					//e1.printStackTrace();
				}
			}
			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			a.setAlertType(AlertType.INFORMATION);
			a.setContentText("Please check date syntax");
			a.show();
		}
	}
});



addButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		Calendar date = Calendar.getInstance();
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		if(selectedFile != null) {
			String filepath = selectedFile.toURI().toString();
			Photo uploadPhoto = new Photo(selectedFile.getName());
			uploadPhoto.setPhotoDirectory(filepath);
			
			date.setTimeInMillis(selectedFile.lastModified());
			date.set(Calendar.MILLISECOND,0);
			uploadPhoto.setDate(date.getTime());
			
			openedAlbum.addPhoto(uploadPhoto);
			
			save();
			updateDisplay();
		}
	}
});

copymoveButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		Photo selectedPhoto = new Photo("new Photo");
		if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
			selectedPhoto=openedAlbum.getPhotoList().get(currentPhotoIndex);
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cmMain.fxml"));
				Parent toCM = (Parent)loader.load();
				
				Scene scene = new Scene(toCM);
				ps.setScene(scene);
				ps.setTitle("Photo Library");
				ps.setResizable(false);  
				ps.show();
				
			cmController cmController = loader.getController();
			cmController.initData(openedAlbum,currentUser,selectedPhoto);
			cmController.start(ps);
			
			} catch (IOException e1) {
				//System.out.println("Error on calling cmController From photoController");
				//e1.printStackTrace();
			}

		} else {
			a.setAlertType(AlertType.ERROR);
			a.setContentText("No Photo Selected from list");
			a.show();
		}
			
							
						
	}
});

removeButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		a.setAlertType(AlertType.CONFIRMATION);
		if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
		a.setContentText("Confirm Deletion?");
		 Optional <ButtonType> confirm = a.showAndWait();
		 if(confirm.get() == ButtonType.OK) {
			openedAlbum.getPhotoList().remove(currentPhotoIndex);
		 
		
		save();
		obsPhotoList.removeAll(obsPhotoList);
		updateDisplay();
		tagList.getItems().clear();
		displayArea.setImage(null);
		 }
		}
		}
});

nextButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(currentPhotoIndex < openedAlbum.getPhotoList().size()) {
			photoList.getSelectionModel().selectNext();
			currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
			if(!photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
				photoList.getSelectionModel().selectFirst();
				currentPhotoIndex=photoList.getSelectionModel().getSelectedIndex();
			}
			Image newImage = new Image(openedAlbum.getPhotoList().get(currentPhotoIndex).getPhotoDirectory());
			displayArea.setImage(newImage);
			displayTag();
		} 
		
		
		
	}
});

prevButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		if(currentPhotoIndex < openedAlbum.getPhotoList().size()) {
			photoList.getSelectionModel().selectPrevious();			
			currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
			if(!photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
				photoList.getSelectionModel().selectLast();
				currentPhotoIndex=photoList.getSelectionModel().getSelectedIndex();
			}
			Image newImage = new Image(openedAlbum.getPhotoList().get(currentPhotoIndex).getPhotoDirectory());
			displayArea.setImage(newImage);
			displayTag();
		} 
		
				
		
	}
});



backButton.setOnAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userMain.fxml"));
			Parent back = (Parent)loader.load();
			
			Scene scene = new Scene(back);
			ps.setScene(scene);
			ps.setTitle("Photo Library");
			ps.setResizable(false);  
			ps.show();
			
		userController userController = loader.getController();
		userController.start(ps);
		
		} catch (IOException e1) {
			//System.out.println("Error on calling login Controller From photoController");
			//e1.printStackTrace();
		}
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
			//System.out.println("Error on calling login Controller From photoController");
			//e1.printStackTrace();
		}
	}
});


tagList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent event) {
		try {
			currentTagIndex = tagList.getSelectionModel().getSelectedIndex();
		} catch(IndexOutOfBoundsException e) {
			//System.out.println("No Tag Selected is Selected");
		}
	}
});



/**
 * Set Selected User index from list
 */
photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	@Override
	public void handle(MouseEvent event) {
		try {
			currentPhotoIndex = photoList.getSelectionModel().getSelectedIndex();
			if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
			try {
				displayPhoto();
				
			}catch(NullPointerException npe) {
				//System.out.println("Nothing selected to Display");
			}
				displayTag();
			
			}
		}catch(IndexOutOfBoundsException e) {
			//System.out.println("Nothing is Selected");
		}
		}
});
	
addTagButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
	@Override
	public void handle(MouseEvent event) {
		if(tagName.getText().isBlank() || tagValue.getText().isBlank()) {
			System.out.println("Please Fill both Tag Name and Value.");
		} else {
			for(Tag t : openedAlbum.getPhotoList().get(currentPhotoIndex).getTags()) {
				if(t.getTagName().toLowerCase().trim().equals(tagName.getText().trim().toLowerCase())&&(t.getValueName().trim().toLowerCase().equals(tagValue.getText().trim().toLowerCase()))) {
					a.setAlertType(AlertType.INFORMATION);
					a.setContentText("Tag Name Value already exists for this photo");
					a.show();
					tagName.clear();
					tagValue.clear();
					return;
				}
			}
			a.setAlertType(AlertType.CONFIRMATION);
			a.setContentText("Confirm: Name: "+makewordpretty(tagName.getText().trim()+" Value: "+makewordpretty(tagValue.getText().trim())));
			 Optional <ButtonType> confirm = a.showAndWait();
			 if(confirm.get() == ButtonType.OK) {
				 openedAlbum.getPhotoList().get(currentPhotoIndex).addTag(new Tag(makewordpretty(tagName.getText().trim()),makewordpretty(tagValue.getText().trim())));
			 }
			save();
		}
		tagName.clear();
		tagValue.clear();
		updateTags();
		displayTag();
	}
});

deleteTagButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
	public void handle(MouseEvent event) {
		if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
			if(tagList.getSelectionModel().isSelected(currentTagIndex)) {
				a.setAlertType(AlertType.CONFIRMATION);
				a.setContentText("Confirm Removal of tag");
				 Optional <ButtonType> confirm = a.showAndWait();
				 if(confirm.get() == ButtonType.OK) {
					 openedAlbum.getPhotoList().get(currentPhotoIndex).getTags().remove(currentTagIndex);
				 }
				save();
			
			}
		}
		
		displayTag();
	}
});

	}
	
	/**
	 * Calls clearTagChoice() and listTagName() for tags into the choiceboxes
	 */
	public void updateTags() {
		clearTagChoice();
		listTagName();
	}
	/**
	 * Clears all choices in Choiceboxes
	 */
	
	public void clearTagChoice() {
		tagName1.getItems().clear();
		tagName2.getItems().clear();
		tagValue1.getItems().clear();
		tagValue2.getItems().clear();
	}
	
	/**
	 * Gets all the Unique Tags from Photos
	 */
	public void loadAllCurrentTagName() {
		for(Photo p : openedAlbum.getPhotoList()) {
			for(Tag t : p.getTags()) {
							
				if(!uniqueTags.contains(t.getTagName())) {
					uniqueTags.add(t.getTagName());		
				}
			}
		}
		for(Photo p : openedAlbum.getPhotoList()) {
			for(Tag t : p.getTags()) {
				if(!uniqueValues.contains(t.getValueName())) {
					uniqueValues.add(t.getValueName());		
				}
			}
		}
		
	}
	/**
	 * Capitalize the first Letter and lowercases the rest
	 * @param name
	 * @return
	 */
	public String makewordpretty(String name) {
		String fletter = name.substring(0,1).toUpperCase();
		String rletter = name.substring(1).toLowerCase();
		return fletter+rletter;
	}
	
	/**
	 * Add all the Tag Names into ChoiceBoxes
	 */
	public void listTagName() {
		loadAllCurrentTagName();
		for(String t: uniqueTags) {
			tagName1.getItems().add(t);
			tagName2.getItems().add(t);
		}
		for(String t: uniqueValues) {
			tagValue1.getItems().add(t);
			tagValue2.getItems().add(t);
		}
	}
	
	/**
	 * Display Photo into a separate Area
	 */
	public void displayPhoto() {
		Image newImage = new Image(openedAlbum.getPhotoList().get(currentPhotoIndex).getPhotoDirectory());
		displayArea.setImage(newImage);
	}
	
	/**
	 * Load Thumbnails
	 */
	public void updateDisplay() {
		obsPhotoList = FXCollections.observableArrayList(openedAlbum.getPhotoList());				
		photoList.setCellFactory(param -> new PhotoCell());
		photoList.setItems(obsPhotoList);
	}
	
	
	/**
	 * Load Tag Name Values into Tag ListView
	 */
	public void displayTag() {
		tagList.getItems().clear();
		if(photoList.getSelectionModel().isSelected(currentPhotoIndex)) {
			for(Tag t : openedAlbum.getPhotoList().get(currentPhotoIndex).getTags()) {
				tagList.getItems().add(t.getTagName() + ": " + t.getValueName());
			}
		}
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
	 * ImageView for Image and Text Using Cells
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
	
	


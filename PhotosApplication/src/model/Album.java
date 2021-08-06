package model;
/**
 * @author ktt43
 * 
 * Album Class
 */
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AlbumName;
	ArrayList<Photo> PhotoList;
	
	/**
	 * Contructor for Album
	 * @param AlbumName
	 */
	public Album(String AlbumName) {
		PhotoList=new ArrayList<Photo>();
		this.AlbumName=AlbumName;
	}

	/**
	 * Adds Photo the this Album
	 * @param photo
	 */
	public void addPhoto(Photo photo) {
		if(checkDuplicatepPhotoName(photo) == false) {
			PhotoList.add(photo);
		}else {
			System.out.println("Photo SAME Name");
		}
	}
	
	
	/**
	 * Checks if the Photo list contains the new Photo Name
	 * TRue : Duplicate detected
	 * False : none
	 * @param name
	 * @return
	 */
	
	public boolean containPhoto(String name) {
		for(Photo p: PhotoList) {
			if(p.getName().trim().equals(name)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Gets Album name
	 * @return
	 */
	public String getAlbumName() {
		return AlbumName;
	}
/**
 * Gets ArrayList of photos
 * @return
 */
	public ArrayList<Photo> getPhotoList() {
		return PhotoList;
	}
/**
 * Sets the Album name
 * @param albumName
 */
	public void setAlbumName(String albumName) {
		AlbumName = albumName;
	}
/**
 * Gets the size of the Photolist
 * @return
 */
	public int getSize() {
		return PhotoList.size();
	}
	
	/** 
	 * Checks for duplicate Photo of same name
	 * True : DUp
	 * False: no Dup
	 * @param p
	 * @return
	 */
	public boolean checkDuplicatepPhotoName(Photo p) {
		String photoName = p.getName().trim();
		for(Photo x: PhotoList) {
			if(x.getName().equals(photoName)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
}

package model;
/**
 * @author ktt43
 * User Class
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	ArrayList<Album> AlbumList;

/**
 * Constructor for User
 * @param username
 */
	
	public User(String username) {
		this.username=username;
		AlbumList = new ArrayList<Album>();		
	}
/**
 * Gets an ArrayList of Albums
 * @return
 */
	public ArrayList<Album> getAllAlbums() {
		return AlbumList;
}
/**
 * Gets specific Album given a Album Name
 * @param name
 * @return
 */
	public Album getAlbumfromList(String name) {
		if(albumExist(name)) {
			for(Album a : this.AlbumList) {
				if(a.getAlbumName().equals(name)) {
					return a;
				}
			}
		}
		return null ;
	}
	/**
	 * Checks if the Album exists
	 * True if exists
	 * @param name
	 * @return
	 */
	public boolean albumExist(String name) {
		for(Album a : this.AlbumList) {
			if(a.getAlbumName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add an Album
	 * @param album
	 */
	public void addAlbum(Album album) {
		String albumName = album.getAlbumName();
		if(checkDuplicateAlbumName(albumName)== false) {
			AlbumList.add(album);
		} else {
			System.out.println("Cannot Create."+ album.getAlbumName() +" already exists!");
		}
	}

/**
 * Checks if the Album already exists
 * True = Duplicate
 * False = No Duplicate
 * @param name
 * @return
 */
	private boolean checkDuplicateAlbumName(String name) {
		for(Album album : AlbumList) {
			System.out.println(album.getAlbumName());
			if((album.getAlbumName()).equals(name)) {
				System.out.println("Duplicate Album Name");
				return true;
			}
		}
		return false;
		
	}

/**
 * Gets username of user
 * @return
 */
	public String getUsername() {
		return username;
	}
	/**
	 * Saves data to user.dat 
	 * @param arrayList
	 * @throws IOException
	 */
	public static void writeApp(ArrayList<User> arrayList) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/user.dat"));
		oos.writeObject(arrayList);
	}
	/**
	 * Reads data from user.dat
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static ArrayList<User> readApp() throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream("data/user.dat"));
		ArrayList<User> users = (ArrayList<User>)ois.readObject();
		return users;
	}

	
	/**
	 * ToString for user
	 */
	@Override
	public String toString() {
		return "User [username=" + username + ", AlbumList=" + AlbumList + "]";
	}

		
	
}

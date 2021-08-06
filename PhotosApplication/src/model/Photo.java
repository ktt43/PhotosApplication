package model;

/**
 * @author ktt43
 * Photo Class
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.Image;

public class Photo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String photoDirectory;
	private String caption;
	private Date date;
	private Calendar cal;
	ArrayList<Tag> tags;
	
	public Photo(String name) {
		tags = new ArrayList<Tag>();
		this.name=name;
		this.photoDirectory = "";
		this.caption = "";
		this.date = Calendar.getInstance().getTime();
	}
	
/**
 * Gets the name of Photo	
 * @return
 */
	public String getName() {
		return name;
	}
/**
 * Sets name for photo
 * @param name
 */
	public void setName(String name) {
		this.name = name;
	}
/**
 * Gets the Directory/Path of the Photo
 * @return
 */
	public String getPhotoDirectory() {
		return photoDirectory;
	}
/**
 * Sets the Directory/path of photo
 * @param photoDirectory
 */
	public void setPhotoDirectory(String photoDirectory) {
		this.photoDirectory = photoDirectory;
	}

/**
 * Gets the caption of photo
 * @return
 */
	public String getCaption() {
		return caption;
	}

/**
 * Sets the caption of the Photo
 * @param caption
 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
/**
 * Gets the Date take of Photo
 * @return
 */
	public Date getDate() {
		return date;
	}
/**
 * Sets the Date of Photo
 * @param date
 */
	public void setDate(Date date) {
		
		this.date = date;
	}
/**
 * Change Date to a formated Date
 * @param date
 */
	public void setFormatedDate(Date date) {
		//cal.setTime(date);
		cal.set(Calendar.MILLISECOND,0);
	}
/**
 * Add a caption to Photo
 * @param caption
 */
	public void addCaption(String caption) {
		this.caption=caption;
	}
	
	/**
	 * Add a new Tag to photo
	 * @param tag
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Gets ArrayList of Tags for Photos
	 * @return
	 */
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	/**
	 * Gets the specific tag given Name Value
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean getthisTag(String name,String value) {
		for(Tag t: tags) {
			if(t.getTagName().toLowerCase().trim().equals(name.toLowerCase().trim())&&(t.getValueName().toLowerCase().trim().equals(value.toLowerCase().trim()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets Image from path
	 * @param path
	 * @return
	 */
	public Image getImage(String path) {
		return new Image(path);
	}

	
	
	
}

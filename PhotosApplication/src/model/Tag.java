package model;
/**
 * @author ktt43
 * Tag Class
 */
import java.io.Serializable;

public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String value;

	/**
	 * Construct for Tag
	 * @param name
	 * @param value
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}
	/**
	 * Gets the Tag name
	 */
	public String getTagName() {
		return this.name;
	}
	/**
	 * Delete Tag
	 */
	public void deleteTag() {
		this.name = null;
		this.value = null;
	}
	/**
	 * Get the value of the tag
	 * @return
	 */
	public String getValueName() {
		return this.value;
	}
/**
 * toString for Tag
 */
	public String toString() {
		return name + ": " + value;
	}
	
}

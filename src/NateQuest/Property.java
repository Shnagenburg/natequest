package NateQuest;

import java.io.IOException;
import java.io.Serializable;

public class Property {

	public Entity parentEntity;
	boolean saveThisProperty;

	public Property(Entity parent) {
		parentEntity = parent;
		saveThisProperty = true;
	}

	public void removeProperty() {

	}

	public void setSaveThisProperty(boolean savethis) {
		saveThisProperty = savethis;
	}

	public boolean getSaveThisProperity() {
		return saveThisProperty;
	}

}

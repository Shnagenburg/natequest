package NateQuest.SavingAndLoading;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.StyledEditorKit.BoldAction;

import GameClasses.CUtil;
import GameClasses.ScreenShifter.ShiftType;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.Editor.InspectorField;

public class SavedEntityParams implements Serializable {

	private static final long serialVersionUID = 5500278326659804696L;
	Entity entity;
	Entity defaultEntity;
	HashMap<String, Integer> intValues;
	HashMap<String, Float> floatValues;
	HashMap<String, String> stringValues;
	HashMap<String, Boolean> boolValues;

	public SavedEntityParams(Entity entity) {
		this.entity = entity;
		craftValueMaps();
	}

	private void craftValueMaps() {
		defaultEntity = getDefaultEntity(entity.getClass());
		if (defaultEntity == null) {
			// Debugger.print("Couldn't get a default entity from blueprint: " +
			// entity.getClass());
			return;
		}

		for (Field field : defaultEntity.getClass().getFields()) {
			compareAndMap(field, Integer.TYPE);
			compareAndMap(field, Float.TYPE);
			compareAndMap(field, Boolean.TYPE);
			compareAndMap(field, String.class);
			compareAndMapEnums(field);
		}

	}

	private void compareAndMap(Field field, Class type) {
		if (field.getType().isAssignableFrom(type)) {
			Object defaultValue = null;
			Object instanceValue = null;
			try {
				defaultValue = field.get(defaultEntity);
				instanceValue = field.get(entity);
				if (!compareViaStrings(defaultValue, instanceValue)) {
					mapValue(field.getName(), instanceValue);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean compareViaStrings(Object source, Object target) {
		// Debugger.print("Comparing " + source + " to " + target);
		if (source.toString().equals(target.toString())) {
			return true;
		} else {
			return false;
		}
	}

	private void mapValue(String variableName, Object value) {
		if (isInt(value)) {
			mapInt(variableName, (Integer) value);
			// Debugger.print("Mapped: " + variableName + " -> " +
			// value.toString() + " as int." );
		} else if (isFloat(value)) {
			mapFloat(variableName, (Float) value);
			// Debugger.print("Mapped: " + variableName + " -> " +
			// value.toString() + " as float." );
		} else if (isBoolean(value)) {
			mapBool(variableName, (Boolean) value);
			// Debugger.print("Mapped: " + variableName + " -> " +
			// value.toString() + " as bool." );
		} else if (isString(value)) {
			mapString(variableName, (String) value);
			// Debugger.print("Mapped: " + variableName + " -> " +
			// value.toString() + " as string." );
		}
	}

	private void compareAndMapEnums(Field field) {
		if (field.getType().isEnum()) {
			Object defaultValue = null;
			Object instanceValue = null;
			try {
				defaultValue = field.get(defaultEntity);
				instanceValue = field.get(entity);
				if (!compareViaStrings(defaultValue, instanceValue)) {
					mapString(field.getName(), instanceValue.toString());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isInt(Object object) {
		return object.getClass() == Integer.class;
	}

	private boolean isFloat(Object object) {
		return object.getClass() == Float.class;
	}

	private boolean isBoolean(Object object) {
		return object.getClass() == Boolean.class;
	}

	private boolean isString(Object object) {
		if (object.getClass() == String.class) {
			return true;
		}
		return false;
	}

	private void mapInt(String variableName, Integer value) {
		if (intValues == null) {
			intValues = new HashMap<String, Integer>();
		}
		intValues.put(variableName, value);
	}

	private void mapFloat(String variableName, Float value) {
		if (floatValues == null) {
			floatValues = new HashMap<String, Float>();
		}
		floatValues.put(variableName, value);
	}

	private void mapBool(String variableName, Boolean value) {
		if (boolValues == null) {
			boolValues = new HashMap<String, Boolean>();
		}
		boolValues.put(variableName, value);
	}

	private void mapString(String variableName, String value) {
		if (stringValues == null) {
			stringValues = new HashMap<String, String>();
		}
		stringValues.put(variableName, value);
	}

	private Entity getDefaultEntity(Class blueprint) {
		Entity defaultEntity = null;
		try {
			defaultEntity = (Entity) blueprint.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return defaultEntity;
	}

	private void writeObject(java.io.ObjectOutputStream out) {

		try {
			out.writeObject(intValues);
			out.writeObject(boolValues);
			out.writeObject(floatValues);
			out.writeObject(stringValues);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			intValues = (HashMap<String, Integer>) in.readObject();
			boolValues = (HashMap<String, Boolean>) in.readObject();
			floatValues = (HashMap<String, Float>) in.readObject();
			stringValues = (HashMap<String, String>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void parameterize(Entity e) {
		entity = e;
		parameterizeMap(intValues);
		parameterizeMap(floatValues);
		parameterizeMap(boolValues);
		parameterizeMap(stringValues);
	}

	private void parameterizeMap(HashMap<String, ? extends Object> map) {
		if (map != null) {
			for (String str : map.keySet()) {
				overwrite(str, map.get(str));
			}
		}
	}

	private Field getFieldViaName(String fieldName) {
		try {
			return entity.getClass().getField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void overwrite(String fieldName, Object value) {
		try {
			Field field = getFieldViaName(fieldName);
			if (field.getType().isEnum()) {
				field.set(
						entity,
						Enum.valueOf((Class<Enum>) field.getType(),
								value.toString()));
			} else {
				field.set(entity, value);
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}

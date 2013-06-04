package NateQuest.Editor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Scanner;

import javax.lang.model.type.PrimitiveType;

import org.newdawn.slick.geom.Vector2f;

import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Drawables.Drawable;
import UserInterface.DanButton;

public class InspectorField extends Entity {

	Class fieldClass;
	Field field;
	Entity target;
	DanButton button;
	boolean isEnteringField = false;

	public InspectorField(GameScreen gs, Entity target, Field field) {
		super(gs);
		this.fieldClass = field.getType();
		this.field = field;
		this.target = target;
		button = new DanButton(gs, new Vector2f(200, 200), 300, 25,
				field.getName() + ": " + currentValue());
		saveThisEntity = false;

	}

	private String currentValue() {
		Field[] list = target.getClass().getFields();
		for (int i = 0; i < list.length; i++) {
			if (field.getName().equals(list[i].getName())) {
				try {
					if (list[i].get(target) != null)
						return list[i].get(target).toString();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "cant find or null";
	}

	private void setValue(String input) {
		try {

			if (fieldClass == Integer.class) {
				field.set(target, Integer.parseInt(input));
				button.setMessage(field.getName() + ": " + currentValue());
			} else if (fieldClass.isAssignableFrom(Integer.TYPE)) {
				field.set(target, Integer.parseInt(input));
				button.setMessage(field.getName() + ": " + currentValue());
			} else if (fieldClass.isAssignableFrom(Float.TYPE)) {
				field.set(target, Float.parseFloat(input));
				button.setMessage(field.getName() + ": " + currentValue());
			} else if (fieldClass.isAssignableFrom(Boolean.TYPE)) {
				field.set(target, Boolean.parseBoolean(input));
				button.setMessage(field.getName() + ": " + currentValue());
			} else if (fieldClass == String.class) {
				field.set(target, input);
				button.setMessage(field.getName() + ": " + currentValue());
			} else {
				Debugger.print("Can't handle the class: "
						+ fieldClass.toString());
			}

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPosition(int x, int y) {
		button.setPosition(x, y);
	}

	public void update() {
		if (button.isHit) {
			// inputField();
			setValue(inputField());

		}
	}

	public String inputField() {
		System.out.println("Current value of " + field.getName() + ": "
				+ currentValue() + ". Input new value: ");
		Scanner user_input = new Scanner(System.in);

		String name;
		name = user_input.next();
		return name;
	}

	@Override
	public void removeEntity() {
		super.removeEntity();
		button.removeEntity();
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		button.markForDeletion();
	}

}

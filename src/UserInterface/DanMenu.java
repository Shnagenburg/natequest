package UserInterface;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Wall;
import NateQuest.ActionItems.Actionable;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.ActionItems.WarpIns.WarpIn;
import NateQuest.Enemies.Enemy;

public class DanMenu extends Entity {

	public enum Filters {
		INSPECTOR, NONE
	}

	public enum Layout {
		HORIZONTAL, VERTICAL
	}

	public enum Size {
		THIN, NORMAL, SQUARE
	}

	final int DEF_X = 400;
	final int DEF_Y = 400;
	static public final int THIN_BUTTON_WIDTH = 200;
	static public final int THIN_BUTTON_HEIGHT = 25;
	static public final int BUTTON_WIDTH = 115;
	static public final int BUTTON_HEIGHT = 50;
	int width, height;
	Point location;
	ArrayList<DanButton> buttons;
	ArrayList<Object> options;
	Object selection = null;
	Object hoverSelection = null;

	public DanMenu(GameScreen gs, ArrayList<? extends Object> items) {
		this(gs, items, Filters.NONE, Layout.VERTICAL, Size.NORMAL, new Point(
				300, 300));
	}

	public DanMenu(GameScreen gs, ArrayList<? extends Object> items,
			Filters filter) {
		this(gs, items, filter, Layout.VERTICAL, Size.NORMAL, new Point(300,
				300));
	}

	public DanMenu(GameScreen gs, ArrayList<? extends Object> items,
			Filters filter, Layout layout, Size size, Point location) {
		super(gs);
		this.location = location;
		buttons = new ArrayList<DanButton>();
		options = new ArrayList<Object>();
		setWidthAndHeight(size);

		for (int i = 0; i < items.size(); i++)
			if (filterEntities(items.get(i), filter))
				options.add(items.get(i));

		for (int i = 0; i < options.size(); i++)
			buttons.add(new DanButton(gs, new Vector2f(location.x, location.y),
					width, height, options.get(i).toString()));

		anchor(layout);
	}

	// Returns true if any option has been hit
	public boolean anyOptionHit() {
		for (DanButton b : buttons) {
			if (b.isHit || b.isPrimed)
				return true;
		}
		return false;

	}

	private void setWidthAndHeight(Size size) {
		switch (size) {
		case NORMAL:
			width = BUTTON_WIDTH;
			height = BUTTON_HEIGHT;
			break;
		case THIN:
			width = THIN_BUTTON_WIDTH;
			height = THIN_BUTTON_HEIGHT;
			break;
		case SQUARE:
			width = BUTTON_HEIGHT;
			height = BUTTON_HEIGHT;
			break;

		}
	}

	private boolean filterEntities(Object o, Filters filter) {
		switch (filter) {
		case INSPECTOR:
			if (o instanceof Enemy)
				return true;
			if (o instanceof Wall)
				return true;
			if (o instanceof LevelTransition)
				return true;
			if (o instanceof Actionable)
				return true;
			if (o instanceof WarpIn)
				return true;
			return false;
		case NONE:
			return true;
		}
		return false;

	}

	private void anchor(Layout layout) {
		for (int i = 0; i < buttons.size(); i++)
			if (layout == Layout.VERTICAL)
				buttons.get(i).setPosition(location.x,
						location.y + (i * height));
			else
				buttons.get(i)
						.setPosition(location.x + (i * width), location.y);
	}

	public void update() {
		selection = null;
		hoverSelection = null;
		for (DanButton b : buttons) {
			if (b.isHit) {
				selection = options.get(buttons.indexOf(b));
			} else if (b.isPrimed) {
				hoverSelection = options.get(buttons.indexOf(b));
			}
		}
	}

	public boolean hasMadeSelection() {
		if (selection == null)
			return false;
		return true;
	}

	public boolean isHoveringOverOption() {
		if (hoverSelection == null)
			return false;
		return true;
	}

	public Object getSelection() {
		return selection;
	}

	public Object getHoverSelection() {
		return hoverSelection;
	}

	public void markForDeletion() {
		super.markForDeletion();
		for (DanButton b : buttons)
			b.markForDeletion();
	}

}

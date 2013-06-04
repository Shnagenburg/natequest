package NateQuest.ActionItems;

import java.awt.Desktop.Action;
import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.DrawableLine;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Editor.Editor;
import NateQuest.Editor.InspectorDialogue;
import UserInterface.DanButton;
import UserInterface.DanMenu;
import UserInterface.DanMenu.Filters;
import UserInterface.DanMenu.Layout;
import UserInterface.DanMenu.Size;

public class ActionCreationMenu extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8762273513990739164L;
	DanMenu actions;
	Actionable source, target;
	DanMenu candidates;
	DanMenu candidateActions;
	String srcAct, trgAct;
	DrawableLine drawaLine;
	InspectorDialogue parentDialogue;

	public ActionCreationMenu(GameScreen gs, Actionable target,
			InspectorDialogue parent) {
		super(gs);
		this.source = target;
		makeActionMenu();
		drawaLine = new DrawableLine(this, LayerConstants.DEBUG);
		drawable = drawaLine;
		parentDialogue = parent;
	}

	private void makeActionMenu() {
		actions = new DanMenu(parentScreen, source.possibleActions,
				Filters.NONE, Layout.VERTICAL, Size.NORMAL, new Point(415, 200));
	}

	public void update() {
		if (actions != null && actions.hasMadeSelection()) {
			srcAct = (String) actions.getSelection();
			createCandidateMenu();
		}
		if (candidates != null && candidates.hasMadeSelection()) {
			target = (Actionable) candidates.getSelection();
			createCandidateActionMenu(target);
		}
		if (candidates != null && candidates.anyOptionHit()) {
			Actionable trg = (Actionable) candidates.getHoverSelection();
			if (source != null && trg != null)
				drawaLine.setTargets(source.position, trg.position);
		}
		if (candidateActions != null && candidateActions.hasMadeSelection()) {
			trgAct = (String) candidateActions.getSelection();
			createActionLink(source, srcAct, target, trgAct);
		}
	}

	private void createCandidateMenu() {
		ArrayList<Entity> entities = parentScreen
				.findEntities(Actionable.class);
		if (entities == null) {
			Debugger.print("No Candidates for Action");
			return;
		}
		ArrayList<Actionable> cands = new ArrayList<Actionable>();

		for (Entity e : entities) {
			cands.add((Actionable) e);
		}
		candidates = new DanMenu(parentScreen, cands, Filters.NONE,
				Layout.VERTICAL, Size.NORMAL, new Point(515, 200));
	}

	private void createCandidateActionMenu(Actionable candidate) {
		candidateActions = new DanMenu(parentScreen, candidate.possibleActions,
				Filters.NONE, Layout.VERTICAL, Size.NORMAL, new Point(630, 200));
	}

	private void createActionLink(Actionable src, String srcAction,
			Actionable trg, String trgAction) {
		int srcid = src.stringToActionID(srcAction);
		int trgid = trg.stringToActionID(trgAction);
		src.addActionLink(new ActionLink(src, srcid, trg, trgid));

		parentDialogue.clearActionInfos();
		parentDialogue.makeActionInfos(src);
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		if (actions != null)
			actions.markForDeletion();
		if (candidates != null)
			candidates.markForDeletion();
		if (candidateActions != null)
			candidateActions.markForDeletion();
	}

}

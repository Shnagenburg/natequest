package NateQuest.SavingAndLoading;

import java.io.IOException;
import java.io.Serializable;

import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ActionItems.ActionLink;
import NateQuest.ActionItems.Actionable;

public class SavedActionLink implements Serializable {

	private static final long serialVersionUID = 1222092570443414663L;
	String source, target;
	int srcActId, trgActId;

	public SavedActionLink(ActionLink link) {
		source = link.parentCaller.getName();
		target = link.target.getName();
		srcActId = link.sourceActID;
		trgActId = link.targetActID;
	}

	public void reattachActionLink(GameScreen screen) {
		Entity srcEnt = screen.findEntity(source);
		Entity trgEnt = screen.findEntity(target);
		// Debugger.print("Source: " + source + " target: " + target );
		// screen.printEntities();
		if (srcEnt != null && trgEnt != null) {
			Actionable trgActionable = (Actionable) trgEnt;
			Actionable srcActionable = (Actionable) srcEnt;
			srcActionable.addActionLink(new ActionLink(srcActionable, srcActId,
					trgActionable, trgActId));
			// Debugger.print("linked " + srcEnt.getName() + " to " +
			// trgEnt.getName());
		} else {
			// Debugger.print("Couldn't find the source/target for an action link while loading.");
		}

	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeObject(source);
			out.writeObject(target);
			out.writeInt(srcActId);
			out.writeInt(trgActId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream in) {
		try {
			source = (String) in.readObject();
			target = (String) in.readObject();
			srcActId = in.readInt();
			trgActId = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

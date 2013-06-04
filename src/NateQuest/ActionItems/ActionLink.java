package NateQuest.ActionItems;

import java.io.Serializable;

import NateQuest.Debugger;
import NateQuest.Entity;

public class ActionLink {

	public Actionable parentCaller;
	public Actionable target;
	public int targetActID;
	public int sourceActID;

	public ActionLink(Actionable parentCaller, int sourceID, Actionable target,
			int targetID) {
		this.parentCaller = parentCaller;
		this.target = target;
		this.sourceActID = sourceID;
		this.targetActID = targetID;
	}

	public void performAction() {
		target.action(targetActID, parentCaller);
		target.playActions(targetActID);
	}

	public boolean equals(ActionLink other) {
		Debugger.print("Comparing... " + this.toString() + " to "
				+ other.toString());
		return (parentCaller == other.parentCaller) && (target == other.target)
				&& (targetActID == other.targetActID)
				&& (sourceActID == other.sourceActID);
	}

	// pcal - flip1 : srcid - 0 -> target - flip2 : targetid - 0
	// pcal - flip2 : srcid - 0 -> target - door : targetid - 0
	// pcal - door : srcid - 0 -> target - flip1 : targetid - 0
	//
	//
	//
	public boolean willNextHop(ActionLink other) {
		if (target == other.parentCaller && targetActID == other.sourceActID) {
			return true;
		}
		return false;
	}

	public String toString() {
		Actionable src = (Actionable) parentCaller;

		return "AL - Src: " + parentCaller + " SrcID: "
				+ src.possibleActions.get(sourceActID) + " Trg: " + target
				+ " TrgID: " + target.possibleActions.get(targetActID);
	}
}

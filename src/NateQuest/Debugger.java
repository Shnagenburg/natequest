package NateQuest;

import java.io.Serializable;

public class Debugger implements Serializable {

	public static void print(String str) {
		// System.out.println(str);
	}

	public static void peek(Object o) {
		if (o != null)
			print(o.toString() + " Type: " + o.getClass().toString());
	}

	public static void peek(Object o, String prefix) {
		if (o != null)
			print(prefix + " " + o.toString() + " Type: "
					+ o.getClass().toString());
	}

}

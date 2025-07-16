package org.mfouad.utilities;

import de.huxhorn.sulky.ulid.ULID;

public class Utils {

	static ULID ulidGen = new ULID();

	public static String getULID() {

		return ulidGen.nextValue().toString();
	}

}

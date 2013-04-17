package com.emrahdayioglu;
/**
* This class handle basic java tasks generates random numbers and string parsing etc.
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.UUID;

public class AppUtil {

	public static String generateRandomAlpaNumeric(boolean isShort) {
		String uuid = UUID.randomUUID().toString();
		if (isShort) {
			uuid = uuid.substring(1, 6);
		}
		return uuid;
	}

	public static int[] parseStringToIntegerArray(String coupons) {
		String[] couponsString = coupons.split(",");
		int[] couponsInt = new int[couponsString.length];
		for (int i = 0; i < couponsString.length; i++) {
			try {
				couponsInt[i] = Integer.parseInt(couponsString[i]);
			} catch (NumberFormatException nfe) {
			}
			;
		}
		return couponsInt;
	}
}

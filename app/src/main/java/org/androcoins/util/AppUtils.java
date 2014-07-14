package org.androcoins.util;

import java.text.NumberFormat;

/**
 * Commonly used application utils
 * @author vitaly gashock
 */

public final class AppUtils {
	public static final String formatToCurrency(double amount) {
		final NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format(amount);
	}
}

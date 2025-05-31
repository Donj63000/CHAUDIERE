package fr.val.chaudiere.util;

/**
 * Validation simple de chaînes.
 */
public final class ValidationUtil {
    private ValidationUtil() { }

    public static boolean isDecimal(String s) {
        if (s == null || s.isBlank()) return false;
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }
}

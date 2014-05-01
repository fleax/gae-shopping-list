package com.github.fleax.shoppinglist.utils;

import java.util.logging.Level;

public final class Logger {

	private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Logger.class.getName());

	private Logger() {
	}

	/**
	 * Info
	 * @param string
	 */
	public static void info(String string) {
		LOG.info(string);
	}

	/**
	 * Info
	 * @param o
	 */
	public static void info(Object... o) {
		LOG.info(StringUtils.getString(o));
	}

	/**
	 * Fine
	 * @param string
	 */
	public static void fine(String string) {
		LOG.fine(string);
	}

	/**
	 * Fine
	 * @param o
	 */
	public static void fine(Object... o) {
		LOG.fine(StringUtils.getString(o));
	}

	/**
	 * Warning
	 * @param string
	 */
	public static void warning(String string) {
		LOG.warning(string);
	}

	/**
	 * Logs an exception
	 * @param e
	 */
	public static void exception(Exception e) {
		LOG.severe(StringUtils.getString(e.getClass().getName(), ": ", e.getMessage()));
		if (e.getStackTrace() != null) {
			for (StackTraceElement ste : e.getStackTrace()) {
				LOG.severe(ste.toString());
			}
		}
	}

	public static boolean isFineLoggable() {
		return LOG.isLoggable(Level.FINE);
	}
}

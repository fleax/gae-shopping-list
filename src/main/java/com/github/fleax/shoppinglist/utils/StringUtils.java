package com.github.fleax.shoppinglist.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilidades para String
 * @author Alejandro Aranda
 *
 */
public final class StringUtils {

	/**
	 * Constructor
	 */
	private StringUtils() {}

	/**
	 * Devuelve un String con la concatenación de la representación de String de los objetos pasados
	 * @param objects
	 * @return
	 */
	public static String getString(Object... objects) {
		final StringBuilder sb = new StringBuilder();
		if (objects != null) {
			for (Object o : objects) {
				if (o != null) {
					sb.append(o.toString());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Devuelve true si la cadena es nula o vacía
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * Remplaza la última occurrencia encontrada de la cadena introducida.
	 * 
	 * @param string
	 * 			cadena inicial			
	 * @param toReplace
	 * 			cadena a remplazar
	 * @param replacement
	 * 			cadena por la que se remplaza
	 * @return cadena inicial con el remplazo.
	 */
	public static String replaceLast(final String string, final String toReplace, final String replacement) {
		final int pos = string.lastIndexOf(toReplace);
		if (pos > -1) {
			return string.substring(0, pos)
					+ replacement
					+ string.substring(pos + toReplace.length(), string.length());
		} else {
			return string;
		}
	}

	/**
	 * Obtiene una lista de String a partir de una cadena separada por un
	 * separador. Si no contiene el separador entonces devuelve 
	 * <code>null</code>.
	 * 
	 * @param string
	 * 			cadena para listar
	 * @param separator
	 * 			separador
	 * @return lista de String
	 */
	public static List<String> getListByStringAndSeparator(final String string, final String separator) {
		return string != null ? Arrays.asList(string.split(separator)) : null;
	}

	/**
	 * Devuelve una lista de long
	 * @param list
	 * @return
	 */
	public static List<Long> getListOfLong(String list) {
		if (list != null) {
			String[] array = list.split(",");
			if (array != null && array.length > 0) {
				List<Long> listOfIds = new ArrayList<Long>();
				for (String id : array) {
					try {
						listOfIds.add(Long.parseLong(id));
					} catch (NumberFormatException e) {
						return null;
					}
				}
				return listOfIds;
			}
		}
		return null;
	}

	/**
	 * Devuelve una lista de string
	 * @param list
	 * @return
	 */
	public static List<String> getListOfString(String list) {
		if (list != null) {
			String[] array = list.split(",");
			if (array != null && array.length > 0) {
				List<String> listOfIds = new ArrayList<String>();
				for (String id : array) {
					listOfIds.add(id);
				}
				return listOfIds;
			}
		}
		return null;
	}
}

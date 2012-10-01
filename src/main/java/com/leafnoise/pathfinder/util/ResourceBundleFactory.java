package com.leafnoise.pathfinder.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Centraliza el instanciado de los Bundles de Mensajes en un solo objeto est&aacute;tico
 * @author Jorge Morando
 *
 */
public class ResourceBundleFactory {

	/**
	 * Instancia un Bundle de mensaje teniendo en cuenta el nombre base del paquete y su locale
	 * 
	 * @param baseName
	 *            nombre del bundle de mensajes
	 * @param locale
	 *            locale del bundle de mensajes
	 * @return ResourceBundle instancia del bundle de mensajes espec&iacute;fico
	 */
	public static final ResourceBundle getBundle(String baseName, Locale locale) {
		return ResourceBundle.getBundle(baseName, locale);
	}

	/**
	 * Instancia un Bundle de mensaje teniendo en cuenta el nombre base del paquete<br>
	 * Por defecto el Locale que se utilizar&aacute; ser&aacute; es_AR
	 * 
	 * @param baseName
	 *            nombre del bundle de mensajes
	 * @return ResourceBundle instancia del bundle de mensajes espec&iacute;fico
	 */
	public static final ResourceBundle getBundle(String baseName) {
		return ResourceBundle.getBundle(baseName, new Locale("es", "AR"));
	}
	
}
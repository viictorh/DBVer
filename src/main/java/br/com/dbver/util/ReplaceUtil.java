package br.com.dbver.util;

import java.util.Map;

/**
 * 
 * @author victor
 *
 */
public class ReplaceUtil {
	private ReplaceUtil() {

	}

	public static String replaceParams(Map<String, String> hashMap, String template) {
		return hashMap.entrySet().stream().reduce(template, (s, e) -> s.replace("%(" + e.getKey() + ")", e.getValue()),
				(s, s2) -> s);
	}

	public static String replaceString(Map<String, String> hashMap, String template) {
		return hashMap.entrySet().stream().reduce(template, (s, e) -> s.replace(e.getKey(), e.getValue()),
				(s, s2) -> s);
	}

}

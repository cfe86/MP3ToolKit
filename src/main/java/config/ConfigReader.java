package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import com.cf.util.Regex;

public class ConfigReader {

	/**
	 * finds a given value in a config file with lines like param[sep]value.
	 * e.g. param=1 with param as param and = as seperator
	 * 
	 * @param str
	 *            given file already parsed in a String. Each line is \n
	 *            seperated
	 * @param param
	 *            parameter to search vor
	 * @param sep
	 *            used seperator
	 * 
	 * @return the value of param, if the param isn't found null will be
	 *         returned
	 */
	public static String findParameter(String str, String param, String sep) {
		// search until = is found
		Regex reg = new Regex("([^=]*)" + Regex.replaceRegexChars(sep) + "(.*)");
		String[] tmp = str.split("\n");
		for (int i = 0; i < tmp.length; i++) {
			// check syntax
			if (reg.matches(tmp[i])) {
				if (reg.getGroup(1).equals(param))
					return reg.getGroup(2).trim();
			}
		}

		return null;
	}

	/**
	 * finds a given value in a config file with lines like param[sep]value.
	 * e.g. param=1 with param as param and = as seperator
	 * 
	 * @param file
	 *            given file
	 * @param param
	 *            parameter to search vor
	 * @param sep
	 *            used seperator
	 * 
	 * @return the value of param
	 */
	public static String findParameter(File file, String param, String sep) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null)
				buf.append(line.trim() + "\n");

			br.close();
			return findParameter(buf.toString(), param, sep);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * finds all given parameters and writes them into the given map.
	 * 
	 * @param txt
	 *            text to search the params in
	 * @param params
	 *            map with the params as param -> found param. If the param
	 *            isn't found the default set found param will not be replaced.
	 * @param sep
	 *            given seperator
	 */
	public static void findParameters(String txt, Map<String, String> params, String sep) {

		String param;
		for (String k : params.keySet()) {
			param = findParameter(txt, k, sep);
			if (param != null)
				params.put(k, param);
		}
	}

	/**
	 * finds all given parameters and writes them into the given map.
	 * 
	 * @param file
	 *            file to search the params in
	 * @param params
	 *            map with the params as param -> found param. If the param
	 *            isn't found the default set found param will not be replaced.
	 * @param sep
	 *            given seperator
	 */
	public static void findParameters(File file, Map<String, String> params, String sep) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null)
				buf.append(line.trim() + "\n");

			br.close();

			findParameters(buf.toString(), params, sep);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
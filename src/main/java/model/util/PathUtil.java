package model.util;

import java.net.URL;

public class PathUtil {

	/**
	 * checks if the given class is in the Jar file
	 * 
	 * @param clazz
	 *            given class
	 * 
	 * @return true if it is in the jar file, else false
	 */
	public static boolean isInJar(Class<?> clazz) {
		String jar = which(clazz);
		return !jar.toLowerCase().startsWith("file:");
	}

	/**
	 * Search the classloader of the given file for the given classname.
	 * 
	 * @param clazz
	 *            the fully qualified name of the class to search for
	 * 
	 * @return the source location of the resource, or null if it wasn't found
	 */
	private static String which(Class<?> clazz) {
		return which(clazz.getName(), clazz.getClassLoader());
	}

	/**
	 * Search the specified classloader for the given classname.
	 * 
	 * @param classname
	 *            the fully qualified name of the class to search for
	 * @param loader
	 *            the classloader to search
	 * @return the source location of the resource, or null if it wasn't found
	 */
	private static String which(String classname, ClassLoader loader) {

		String classnameAsResource = classname.replace('.', '/') + ".class";

		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}

		URL it = loader.getResource(classnameAsResource);
		if (it != null) {
			return it.toString();
		} else {
			return null;
		}
	}

	/**
	 * gets the path to the executed Jar File
	 * 
	 * @param clazz
	 *            an instance of the calling class (just put 'this.getClass()'
	 *            in here)
	 * 
	 * @return path to the executed jar file
	 * 
	 * @throws NoJarFileException
	 *             thrown if the given class is not in a Jar file
	 */
	public static String getJarPath(Class<?> clazz) {
		String jar = which(clazz);

		// form: jar:file://path/to/Jar/jar.jar!/Class.class
		// get first before !
		String tmp = jar.split("!")[0];
		// delete jar:file:/
		jar = tmp.substring(9);

		if (System.getProperty("os.name").toLowerCase().startsWith("win"))
			jar = jar.substring(1);

		return jar;
	}

	/**
	 * gets the path to the workspace
	 * 
	 * @return the path
	 */
	public static String getWorkspacePath() {
		String path = which(PathUtil.class);
		path = path.split("target")[0];
		path = path.replace("file:/", "");

		return path;
	}
}
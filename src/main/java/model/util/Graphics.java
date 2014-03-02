package model.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Graphics {

	/**
	 * scales the given image
	 * 
	 * @param img
	 *            given image
	 * @param pixelX
	 *            width in pixel
	 * @param pixelY
	 *            height in pixel
	 * 
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage img, int pixelX, int pixelY) {
		int width = img.getWidth();
		int height = img.getHeight();

		return scale(img, ((double) pixelX) / ((double) width), ((double) pixelY) / ((double) height));
	}

	/**
	 * scales a given image
	 * 
	 * @param img
	 *            given image
	 * @param factor
	 *            the given scale factor
	 * 
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage img, double factor) {
		return scale(img, factor, factor);

	}

	/**
	 * scales a given image
	 * 
	 * @param img
	 *            given image
	 * @param factorX
	 *            scalefactor for width
	 * @param factorY
	 *            scalefactor for height
	 * 
	 * @return scaled image
	 */
	public static BufferedImage scale(BufferedImage img, double factorX, double factorY) {
		int width = img.getWidth();
		int height = img.getHeight();

		BufferedImage result = new BufferedImage((int) (width * factorX), (int) (height * factorY), BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(img, 0, 0, (int) (width * factorX), (int) (height * factorY), null);

		return result;
	}

	/**
	 * reads the image found at the given path
	 * 
	 * @param path
	 *            path to the image
	 * 
	 * @return the image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static BufferedImage readImage(String path) throws IOException {
		return ImageIO.read(new File(path));
	}

	/**
	 * writes a given image to a file
	 * 
	 * @param image
	 *            given image
	 * @param path
	 *            path where to save the file
	 * @param extension
	 *            extension of the image
	 * 
	 * @return true if written successfully, else false
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static boolean writeImage(BufferedImage image, String path, String extension) throws IOException {
		return ImageIO.write(image, extension, new File(path));
	}

	/**
	 * reads the image found at the given URL
	 * 
	 * @param url
	 *            given URL
	 * 
	 * @return the Image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static BufferedImage getImageFromURL(URL url) throws IOException {
		return ImageIO.read(url);
	}

	/**
	 * reads the image found at the given URL
	 * 
	 * @param url
	 *            given URL
	 * 
	 * @return the Image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static BufferedImage getImageFromURL(String url) throws IOException {
		return getImageFromURL(new URL(url));
	}

	/**
	 * reads the image found at the given URL as a byte[]
	 * 
	 * @param url
	 *            given URL
	 * 
	 * @return the Image in byte
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static byte[] getImageFromURLasByte(URL url) throws IOException {
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		return out.toByteArray();
	}

	/**
	 * reads the image given via byte[]
	 * 
	 * @param bytes
	 *            given bytes
	 * 
	 * @return the image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static BufferedImage getImageFromBytes(byte[] bytes) throws IOException {
		InputStream in = new ByteArrayInputStream(bytes);
		return ImageIO.read(in);
	}

	/**
	 * reads the image found at the given URL as a byte[]
	 * 
	 * @param url
	 *            given URL
	 * 
	 * @return the Image in byte
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static byte[] getImageFromURLasByte(String url) throws MalformedURLException, IOException {
		return getImageFromURLasByte(new URL(url));
	}

	/**
	 * reads an Image out of the jar file
	 * 
	 * @param path
	 *            the path in the jar file. This has the form path/to/file
	 * 
	 * @return the image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public static BufferedImage readImageFromJar(String path) throws IOException {
		Logger.getLogger(Util.class.getName()).log(Level.FINER, "read image out of jar from: " + path);
		InputStream stream;
		stream = Util.class.getResourceAsStream("/" + path);
		// if this fails try again using relativ paths and ClassLoader
		if (stream == null) {
			stream = ClassLoader.getSystemResourceAsStream(path);
		}

		if (stream == null) {
			throw new IOException("Couldn't find Image: " + path);
		}

		return ImageIO.read(stream);
	}

	/**
	 * reads an Image out of the jar file
	 * 
	 * @param path
	 *            the path in the jar file. This has the form path/to/file
	 * 
	 * @return the image
	 */
	public static BufferedImage readImageFromJarWoExc(String path) {
		Logger.getLogger(Util.class.getName()).log(Level.FINER, "read image out of jar from: " + path);
		InputStream stream;
		stream = Util.class.getResourceAsStream("/" + path);
		// if this fails try again using relativ paths and ClassLoader
		if (stream == null) {
			stream = ClassLoader.getSystemResourceAsStream(path);
		}

		try {
			return ImageIO.read(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
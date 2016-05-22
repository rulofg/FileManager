package engine;

/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */

import java.awt.Color;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

/**
 * This class uses a very simple, naive similarity algorithm to compare an image
 * with all others in the same directory.
 */
public class ImageComparator {

	private static final float DEFAULT_THRESHOLD = 100f;
	private static final int DEFAULT_SAMPLE_SIZE = 10;

	public static final int BIGGER = 1;
	public static final int SMALLER = 2;
	public static final int SAME = 0;
	public static final int ERROR = -1;

	public File getReference() {
		return reference;
	}

	private float threshold;

	// The reference image "signature" (25 representative pixels, each in
	// R,G,B).
	// We use instances of Color to make things simpler.
	private Color[][] signature;
	// The base size of the images.
	private int baseSizeX;
	private int baseSizeY;
	private File reference;
	private int signatureX,signatureY, sampleSize;

	public ImageComparator(File reference) throws IOException {
		this(reference, DEFAULT_THRESHOLD, DEFAULT_SAMPLE_SIZE);
	}

	public ImageComparator(File reference, float threshold) throws IOException {
		this(reference, threshold, DEFAULT_SAMPLE_SIZE);
	}

	public ImageComparator(File reference, int sampleSize) throws IOException {
		this(reference, DEFAULT_THRESHOLD, sampleSize);
	}

	public ImageComparator(File reference, float threshold, int sampleSize)
			throws IOException {
		RenderedImage image = ImageIO.read(reference);
		baseSizeX = image.getWidth();
		baseSizeY = image.getHeight();
		this.reference = reference;
		this.threshold = threshold;	
		this.sampleSize=sampleSize;

		int sigX = (int) (image.getWidth()/sampleSize);
		int sigY = (int) (image.getHeight()/sampleSize);
		
		this.signatureX = (image.getWidth()%sampleSize==0)?sigX:sigX+1;
		this.signatureY = (image.getHeight()%sampleSize==0)?sigY:sigY+1;
		
		// Calculate the signature vector for the reference.
		signature = calcSignature(image);
	}

	public int compareSize(File file) throws IOException {
		RenderedImage image = ImageIO.read(file);
		if (image.getWidth() == baseSizeX && image.getHeight() == baseSizeY)
			return SAME;
		if (image.getWidth() > baseSizeX && image.getHeight() > baseSizeY)
			return BIGGER;
		if (image.getWidth() < baseSizeX && image.getHeight() < baseSizeY)
			return SMALLER;
		
		return ERROR;
	}
	
	public static int compareSize(RenderedImage imageA, RenderedImage imageB){
		if (imageA.getWidth() == imageB.getWidth() && imageA.getHeight() == imageB.getHeight())
			return SAME;
		if (imageA.getWidth() > imageB.getWidth() && imageA.getHeight() > imageB.getHeight())
			return BIGGER;
		if (imageA.getWidth() < imageB.getWidth() && imageA.getHeight() < imageB.getHeight())
			return SMALLER;
		return ERROR;
	}

	public boolean isSimilarTo(RenderedImage image) {
		RenderedImage resized = rescaleToReference(image);
		double distance = calcDistance(resized);
		if (distance < threshold)
			return true;

		return false;
	}

	/*
	 * This method rescales an image using the JAI scale operator.
	 */
	private RenderedImage rescaleToReference(RenderedImage i) {
		float scaleW = ((float) baseSizeX) / i.getWidth();
		float scaleH = ((float) baseSizeY) / i.getHeight();
		// Scales the original image
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(i);
		pb.add(scaleW);
		pb.add(scaleH);
		pb.add(0.0F);
		pb.add(0.0F);
		pb.add(new InterpolationNearest());
		// Creates a new, scaled image and uses it on the DisplayJAI component
		return JAI.create("scale", pb);
	}

	/*
	 * This method calculates and returns signature vectors for the input image.
	 */
	private Color[][] calcSignature(RenderedImage i) {
		// Get memory for the signature.
		Color[][] sig = new Color[signatureX][signatureY];
		// For each of the 25 signature values average the pixels around it.
		// Note that the coordinate of the central pixel is in proportions.
		float[] propX = new float[signatureX];
		float xMax = (float) (2 * signatureX);
		int ind = 0;
		for (float x = 1f; x < xMax; x += 2f) {
			propX[ind] = x / xMax;
			ind++;
		}

		float[] propY = new float[signatureY];
		float yMax = (float) (2 * signatureY);
		ind = 0;
		for (float y = 1f; y < yMax; y += 2f) {
			propY[ind] = y / yMax;
			ind++;
		}

		for (int x = 0; x < signatureX; x++)
			for (int y = 0; y < signatureY; y++)
				sig[x][y] = averageAround(i, propX[x], propY[y]);
		return sig;
	}

	/*
	 * This method averages the pixel values around a central point and return
	 * the average as an instance of Color. The point coordinates are
	 * proportional to the image.
	 */
	private Color averageAround(RenderedImage i, double px, double py) {
		// Get an iterator for the image.
		RandomIter iterator = RandomIterFactory.create(i, null);
		// Get memory for a pixel and for the accumulator.
		double[] pixel = new double[3];
		double[] accum = new double[3];
		
		
		int numPixels = 0;
		// Sample the pixels.
		double startX = px * baseSizeX - sampleSize;
		double endX = px * baseSizeX + sampleSize;
		double startY = py * baseSizeY - sampleSize;
		double endY = py * baseSizeY + sampleSize;
		if (startX < 0)
			startX = 0f;
		if (endX > baseSizeX)
			endX = baseSizeX;
		if (startY < 0)
			startY = 0f;
		if (endY > baseSizeY)
			endY = baseSizeY;

		for (double x = startX; x < endX; x++) {
			for (double y = startY; y < endY; y++) {
				iterator.getPixel((int) x, (int) y, pixel);
				accum[0] += pixel[0];
				accum[1] += pixel[1];
				accum[2] += pixel[2];
				numPixels++;
			}
		}
		// Average the accumulated values.
		accum[0] /= numPixels;
		accum[1] /= numPixels;
		accum[2] /= numPixels;
		return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
	}

	/*
	 * This method calculates the distance between the signatures of an image
	 * and the reference one. The signatures for the image passed as the
	 * parameter are calculated inside the method.
	 */
	private double calcDistance(RenderedImage other) {
		// Calculate the signature for that image.
		Color[][] sigOther = calcSignature(other);
		// There are several ways to calculate distances between two vectors,
		// we will calculate the sum of the distances between the RGB values of
		// pixels in the same positions.
		double dist = 0;
		for (int x = 0; x < 5; x++)
			for (int y = 0; y < 5; y++) {
				int r1 = signature[x][y].getRed();
				int g1 = signature[x][y].getGreen();
				int b1 = signature[x][y].getBlue();
				int r2 = sigOther[x][y].getRed();
				int g2 = sigOther[x][y].getGreen();
				int b2 = sigOther[x][y].getBlue();
				double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
						* (g1 - g2) + (b1 - b2) * (b1 - b2));
				dist += tempDist;
			}
		return dist;
	}
}

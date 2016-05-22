/*
  * Part of the Java Image Processing Cookbook, please see
  * http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
  * for information on usage and distribution.
  * Rafael Santos (rafael.santos@lac.inpe.br)
  */

package engine;

import java.io.File;

import javax.swing.filechooser.FileFilter;
 /*
  * This class implements a generic file name filter that allows the listing/selection
  * of JPEG files.
  */
public class ImageFileFilter extends FileFilter implements java.io.FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.getName().toLowerCase().endsWith(".jpeg")) return true;
		if(f.getName().toLowerCase().endsWith(".jpg")) return true;
		if(f.getName().toLowerCase().endsWith(".png")) return true;
		return false;
	}

	@Override
	public String getDescription() {
		return "Image Files (JPEG & PNG)";
	}

}
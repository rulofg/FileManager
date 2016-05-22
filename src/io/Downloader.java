package io;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class Downloader {
	private final static int URL_MARK = 0;
	private final static int START_URL = 1;
	private final static int END_URL = 2;
	private final static int NEXT_URL = 3;
	private final static int START_NEXT = 4;
	private final static int END_NEXT = 5;
	private final static int IMG = 6;
	private final static int START_IMG = 7;
	private final static int END_IMG = 8;

	private static ArrayList<URL> filesUrl = new ArrayList<URL>();
	private static ArrayList<URL> toDownload = new ArrayList<URL>();
	
	 public static void download(URL url,String name)
	    {
	        BufferedImage img;
	        try
	        {

	        
	            img = ImageIO.read(url);
	            File os = new File(System.getProperty("user.home")+File.separator+"testdirThread"+File.separator+name);
	            ImageIO.write(img, "jpg", os);
	            System.out.println(Thread.currentThread().getName()+ " downloaded");
	        }
	        catch (IOException ex)
	        {
	            System.out.println("Some problem with the file "+ name + " -IOEXCEPTION");
	        }
	    }

	public static void download(String urlAddress, String downloadDir,
			DownloadParameters opt) {
		try {
			URL url = new URL(urlAddress);
			filesUrl = new ArrayList<URL>();
			addFilesUrl(url, opt);
			addToDownload(filesUrl, downloadDir, opt);
			BufferedImage image = null;
			for (final URL u : toDownload) {
				System.out.println(u);

				//try {
					String uString = u.toString();
					
					File dir = new File(System.getProperty("user.home")+File.separator+downloadDir);
					if(!dir.exists())
						dir.mkdir();
					String path = dir.getAbsolutePath()+ File.separator
							+ uString.substring(uString.lastIndexOf("/")+1);
					final String imgSrc = uString.substring(uString.lastIndexOf("/") + 1);
		            String imageFormat = null;
		            imageFormat = imgSrc.substring(imgSrc.lastIndexOf(".") + 1);
		            
		            
		       /*     URLConnection connection = u.openConnection();

		            File file = new File(path);
		            FileOutputStream fos = new FileOutputStream(file);
		           FileChannel output = fos.getChannel();
		           long length = connection.getContentLengthLong();
		            long n =output.transferFrom(Channels.newChannel(connection.getInputStream()), 0, length);
		            System.out.println(n+" / "+length);
		            fos.close();
		            output.close();*/
		            //TODO
		            BufferedImage img;
		           ( new Thread()  //line 1 - Anonymous inner Thread Class
		           
		            	            {                                      //line 2
		           
		            	                public void run()         //line 3
		           
		            	                {
		           
		            	              download(u,imgSrc);
		            	                }                               
		           
		            	            }).start();    	            
		            
		            
		            
		            /*image = ImageIO.read(u);
		            if (image != null) {
		                File file = new File(path);
		                ImageIO.write(image, imageFormat, file);
		            }
		 

		            InputStream in = new BufferedInputStream(u.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] buf = new byte[4096];
					int n = 0;
					while (-1 != (n = in.read(buf))) {
						out.write(buf, 0, n);
					}
					out.close();
					in.close();
					byte[] response = out.toByteArray();
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(response);
					fos.close();*/
			//	} catch (IOException e) {
					// TODO Auto-generated catch block e.printStackTrace();
				//	e.printStackTrace();
				//}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void addToDownload(ArrayList<URL> filesURL,
			String downloadDir, DownloadParameters opt) {
		System.out.println("Addo download");
		int indImage;
		String input = "";
		for (URL url : filesURL) {
			input = "";
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					/*
					 * indImage = inputLine.indexOf(args[IMG]); if (indImage !=
					 * -1) { // if string match image block // identificator
					 * indImage = inputLine.indexOf(args[START_IMG]); // int i =
					 * inputLine.indexOf("\"",iThumb); int i =
					 * inputLine.indexOf(args[END_IMG],indImage);
					 * toDownload.add(new URL(url, Jsoup.parse(
					 * inputLine.substring(indImage, i)).text())); }
					 */
					input += inputLine;
				}
				in.close();
				ArrayList<int[]> positions = matchRegex(input,
						opt.getImgRegex());
				String str = "";
				for (int[] pos : positions) {
					str = input.substring(pos[0], pos[1]);
					if (str.contains(opt.getImgDelimitorStart())) {
						int start, end;
						start = str.indexOf(opt.getImgDelimitorStart())
								+ opt.getImgDelimitorStart().length();
						end = str.lastIndexOf(opt.getImgDelimitorEnd());
						if (start != end)
							str = str.substring(start, end);
						else
							str = str.substring(start);
					}
					toDownload.add(new URL(str));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void addFilesUrl(URL url, DownloadParameters opt) {
		System.out.println("Add files");
		try {
			URL nextURL = null;
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			String input = "";
			int iThumb, iNext;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				/*
				 * // iThumb = inputLine.indexOf("class=\"thumb\""); iThumb =
				 * inputLine.indexOf(args[URL_MARK]); if (iThumb != -1) { //
				 * iThumb = inputLine.indexOf("index.php", iThumb); iThumb =
				 * inputLine.indexOf(args[START_URL]); // int i =
				 * inputLine.indexOf("\"",iThumb); int i =
				 * inputLine.indexOf(args[END_URL], iThumb); filesUrl.add(new
				 * URL(url, Jsoup.parse( inputLine.substring(iThumb,
				 * i)).text())); } if (nextURL == null) { // iNext =
				 * inputLine.indexOf("alt=\"next\""); iNext =
				 * inputLine.indexOf(args[NEXT_URL]); if (iNext != -1) {
				 * /*String firstPart = inputLine.substring(0, iNext - 2); //
				 * int i = firstPart.lastIndexOf("?page=post"); int i =
				 * firstPart.indexOf(args[START_NEXT]); //int iEnd =
				 * inputLine.indexOf(args[END_NEXT]); String s =
				 * firstPart.substring(i, iEnd); s = Jsoup.parse(s).text();
				 * nextURL = new URL(url, s);
				 */
				/*
				 * } }
				 */
				input += inputLine;
			}
			in.close();
			ArrayList<int[]> positions = matchRegex(input, opt.getUrlRegex());
			String str = "";
			for (int[] pos : positions) {
				str = input.substring(pos[0], pos[1]);
				if (str.contains(opt.getUrlDelimitorStart())) {
					int start, end;
					start = str.indexOf(opt.getUrlDelimitorStart())
							+ opt.getUrlDelimitorStart().length();
					end = str.lastIndexOf(opt.getUrlDelimitorEnd());
					if (start != end)
						str = str.substring(start, end);
				}
				filesUrl.add(new URL(url, str));
			}
			if (nextURL == null) {
				positions = matchRegex(input, opt.getNextUrlRegex());
				str = "";
				for (int[] pos : positions) {
					str = input.substring(pos[0], pos[1]);
					if (str.contains(opt.getNextUrlDelimitorStart())) {
						int start, end;
						start = str.indexOf(opt.getNextUrlDelimitorStart())
								+ opt.getNextUrlDelimitorStart().length();
						end = str.lastIndexOf(opt.getNextUrlDelimitorEnd());
						if (start != end)
							str = str.substring(start, end);
					}
					nextURL = new URL(url, str);
				}
			}
			
			//TODO Search only the first page (test mode)
			/*if (nextURL != null)
				addFilesUrl(nextURL, opt);
				*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<int[]> matchRegex(String str, String regex) {
		System.out.println("MatchRegex");
		ArrayList<int[]> positions = new ArrayList<int[]>();
		Pattern p = Pattern.compile(regex); // insert your pattern here
		Matcher m = p.matcher(str);

		while (m.find()) {
			int tab[] = { m.start(), m.end() };
			positions.add(tab);
		}
		return positions;
	}

	public static Object[] getImageUrlAndName(URL url, ArrayList<String> tags) {
		Object[] data = new Object[3];
		ArrayList<String> nameTags = new ArrayList<String>();
		URL fileUrl = null;
		String id = "";
		try {
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("id=\"image\"")) {
					int a, b;
					a = inputLine.indexOf("alt=\"") + 5;
					b = inputLine.indexOf("\"", a);
					String[] tokens = inputLine.substring(a, b).split(" ");
					for (String token : tokens)
						if (tags.contains(token))
							nameTags.add(token);
					a = inputLine.indexOf("src=\"") + 5;
					b = inputLine.indexOf("\"", a);
					String s = inputLine.substring(a, b).split("\\?")[0];
					id = inputLine.substring(inputLine.indexOf("?", a) + 1, b);
					fileUrl = new URL(s);
				}

			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data[0] = id;
		data[1] = fileUrl;
		data[2] = nameTags;

		return data;
	}

	public static ArrayList<URL> getFilesUrl(URL url) {
		ArrayList<URL> filesUrl = new ArrayList<URL>();
		try {
			URL nextURL = null;
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			int iThumb, iNext;
			while ((inputLine = in.readLine()) != null) {
				iThumb = inputLine.indexOf("class=\"thumb\"");
				if (iThumb != -1) {
					iThumb = inputLine.indexOf("index.php", iThumb);
					int i = inputLine.indexOf("\"", iThumb);
					filesUrl.add(new URL(url, Jsoup.parse(
							inputLine.substring(iThumb, i)).text()));
				}
				if (nextURL == null) {
					iNext = inputLine.indexOf("alt=\"next\"");
					if (iNext != -1) {
						String firstPart = inputLine.substring(0, iNext - 2);
						int i = firstPart.lastIndexOf("?page=post");
						String s = "index.php" + firstPart.substring(i);
						s = Jsoup.parse(s).text();
						nextURL = new URL(url, s);
					}
				}
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filesUrl;
	}

	public static void openURL() {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			//TODO remove test URL
			URL url = new URL(
					"http://bcy.net/coser/listhotwork/3770");
			System.out.println();
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			String input = "";
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine.replaceAll(
						"&([^;]+(?!(?:\\w|;)))", "&amp;$1"));
				if (!inputLine.contains("meta"))
					input += inputLine + "\n";
			}
			in.close();
			String cleanInput = input.replaceAll("&([^;]+(?!(?:\\w|;)))",
					"&amp;$1");

			doc = builder.build(new StringReader(cleanInput));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			System.out.println("Error : Can't load");

		} else
			System.out.println("Opened");

		// Element root = doc.getRootElement();

	}
}

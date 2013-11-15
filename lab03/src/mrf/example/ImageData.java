package mrf.example;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageData {
	public static void main(String[] args) throws IOException{
		BufferedImage img = ImageIO.read(new File(args[0]));
		
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < pixels.length; i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(pixels[i]);
			data.add(temp);
		}
		
		File file = new File(args[1]);
		file.createNewFile();
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		
		oos.writeObject(data);
		
		oos.close();
	}
}

package com.whatever.service;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.whatever.exceptions.ImageTooSmallException;
import com.whatever.exceptions.InvalidFileException;
import com.whatever.model.FileInfo;

/** A Image Service class*/

@Service
public class FileService {
	
	@Value("${photo.file.extensions}")
	private String imageExtensions;
	
	private Random random = new Random();

	private String getFileExtensions(String filename){
		
		int dotPosition = filename.lastIndexOf(".");
		
		if (dotPosition < 0)
			return null;
		return filename.substring(dotPosition + 1).toLowerCase();
	}
	
	private boolean isImageExtension(String extension){
		
		String testExtension = extension.toLowerCase();
		
		for (String validExtension: imageExtensions.split(",")){
			
			if (testExtension.equals(validExtension))
				return true;
		}	
		return false;
	}
	
	private File makeSubdirectory(String basePath, String prefix){
		int nDirectory = random.nextInt(1000);
		String sDirectory = String.format("%s%03d", prefix, nDirectory);
		
		File directory = new File(basePath, sDirectory);
		
		if (!directory.exists()){
			directory.mkdir();
		}
		return directory;
	}
	
	public FileInfo saveImageFile(MultipartFile file, String baseDirectory, String subDirPrefix, String filePrefix, int width, int height) throws InvalidFileException, IOException, ImageTooSmallException{
		int nFilename = random.nextInt(1000);
		String filename = String.format("%s%03d", filePrefix, nFilename);
		
		String extension = getFileExtensions(file.getOriginalFilename());
		if (extension == null)
			throw new InvalidFileException("The file has no extention");
		
		if (!isImageExtension(extension))
			throw new InvalidFileException("The file extention is invalid for an image");
		
		File subDirectory = makeSubdirectory(baseDirectory, subDirPrefix);
		
		Path filepath = Paths.get(subDirectory.getCanonicalPath(), filename + "." + extension);
		
		BufferedImage resizedImage = resizeImage(file, width, height);
		
		ImageIO.write(resizedImage, extension, filepath.toFile());
		
		return new FileInfo(filename, extension, subDirectory.getName(), baseDirectory);		
	}
	
	/** Resize image to a standard dimension*/
	private BufferedImage resizeImage(MultipartFile inputFile, int width, int height) throws IOException, ImageTooSmallException {
		
		BufferedImage image = ImageIO.read(inputFile.getInputStream());
		
		if (image.getWidth() < width && image.getHeight() < height)
			return image;
		
		double scale = Math.min((double)width / image.getWidth(), (double)height / image.getHeight());
		
		BufferedImage scaledImage = new BufferedImage((int)(scale * image.getWidth()), (int)(scale * image.getHeight()), image.getType());
						
		Graphics2D g = scaledImage.createGraphics();
		AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
		g.drawImage(image, transform, null);
		g.dispose();

		return scaledImage.getSubimage(0, 0, (int)(scale * image.getWidth()), (int)(scale * image.getHeight()));
		
	}
	
}

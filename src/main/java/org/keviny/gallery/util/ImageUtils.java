package org.keviny.gallery.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.commons.io.IOUtils;

public class ImageUtils {

	public static byte[] scale(InputStream in, int width, int height, String format) {
		return scale(in, width, height, format, 1);
	}
	
	public static byte[] scale(byte[] b, int width, int height, String format, float quality) {
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			return scale(bis, width, height, format, quality);
	}
	
	public static byte[] scale(InputStream in, int width, int height, String format, float quality) {
		byte[] buf = null;
		ByteArrayOutputStream out = null;
		try {
			BufferedImage bi = ImageIO.read(in);
			int _width = bi.getWidth();
			int _height = bi.getHeight();
			BigDecimal _w = new BigDecimal(_width);
			BigDecimal _h = new BigDecimal(_height);
			BigDecimal _ratio = _w.divide(_h, 2, BigDecimal.ROUND_HALF_UP);
			//FIXME: when height is not set and width < _w
			// it won't be as expected
			if(width <= 0) 
				width = _width;
			if(height <= 0) 
				height = _height;
			BigDecimal w = new BigDecimal(width);
			BigDecimal h = new BigDecimal(height);
			BigDecimal ratio = w.divide(h, 2, BigDecimal.ROUND_HALF_UP);
			float _r = _ratio.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			if(ratio.compareTo(_ratio) == 1) 
				height = Math.round( width / _r);
			else 
				width = Math.round(height * _r);
			// Calibrate
			if(width == _width && Math.abs(height - _height) <= 1 ) 
				height = _height;
			if(height == _height && Math.abs(width - _width) <= 1)
				width = _width;
			
			BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			Graphics2D g = dst.createGraphics();
			g.drawImage(bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
			g.dispose();
			
			out = new ByteArrayOutputStream();
			// currently compression only supports jpg files
			if (quality > 0 && quality < 1 
					&& !format.equalsIgnoreCase("png")
					&& !format.equalsIgnoreCase("gif")) {
				Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(format);
				ImageWriter imgWriter = it.next();
				ImageWriteParam param = imgWriter.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(quality);
				param.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
				ColorModel cm = ColorModel.getRGBdefault();
				param.setDestinationType(new ImageTypeSpecifier(cm, cm.createCompatibleSampleModel(16, 16)));
				imgWriter.setOutput(ImageIO.createImageOutputStream(out));
				IIOImage im = new IIOImage(dst, null, null);
				imgWriter.write(null, im, param);
			} else {
				ImageIO.write(dst, format, out);
			}
			buf = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		return buf;
	}

	public static byte[] scale(byte[] b, int width, int height, String format) {
		ByteArrayInputStream bis = new ByteArrayInputStream(b);
		return scale(bis, width, height, format);
	}
	
	public static void main(String[] args) {
		int width = 300;
		int height = 400;
		int _width = 400;
		int _height = 300;
		BigDecimal _w = new BigDecimal(_width);
		BigDecimal _h = new BigDecimal(_height);
		BigDecimal _ratio = _w.divide(_h, 2, BigDecimal.ROUND_HALF_UP);
		if(width <= 0) 
			width = _width;
		if(height <= 0) 
			height = _height;
		BigDecimal w = new BigDecimal(width);
		BigDecimal h = new BigDecimal(height);
		BigDecimal ratio = w.divide(h, 2, BigDecimal.ROUND_HALF_UP);
		float _r = _ratio.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		if(ratio.compareTo(_ratio) == 1) 
			height = Math.round( width / _r);
		else 
			width = Math.round(height * _r);

		// Calibrate
		if(width == _width && Math.abs(height - _height) <= 1 ) 
			height = _height;
		if(height == _height && Math.abs(width - _width) <= 1)
			width = _width;
		
		System.out.println("width=" + width + ", height=" + height);
	}
}

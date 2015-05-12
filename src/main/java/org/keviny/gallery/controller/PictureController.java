package org.keviny.gallery.controller;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.keviny.gallery.common.FileMetadata;
import org.keviny.gallery.common.FileWrapper;
import org.keviny.gallery.common.exception.ErrorCode;
import org.keviny.gallery.common.exception.ErrorCodeException;
import org.keviny.gallery.mongo.repository.PictureRepository;
import org.keviny.gallery.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("api/pictures")
public class PictureController {

	private static final String[] supportedImageTypes = { "image/gif",
			"image/jpeg", "image/png", "image/bmp" };

	@Autowired
	private PictureRepository pictureRepository;

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<FileMetadata> handlePictureUpload(
			@RequestParam("file") MultipartFile image) {

		if (image.isEmpty()) {
			throw new ErrorCodeException(ErrorCode.FILE_IS_EMPTY);
		}

		if (!isImageSupported(image.getContentType())) {
			throw new ErrorCodeException(ErrorCode.CONTENT_TYPE_NOT_SUPPORTED);
		}

		FileMetadata meta = new FileMetadata(image.getOriginalFilename(),
				image.getContentType());
		InputStream in = null;
		try {
			in = image.getInputStream();
			pictureRepository.storeFile(image.getBytes(), meta);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		HttpStatus status = HttpStatus.CREATED;

		if (meta.getFid() == null) {
			throw new ErrorCodeException(ErrorCode.FILE_UPLOAD_FAILED);
		}
		return new ResponseEntity<FileMetadata>(meta, status);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Object list(
			@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "start_time", required = false) Long startTime,
			@RequestParam(value = "end_time", required = false) Long endTime) {
		
		return null;
	}

	@RequestMapping(value = "/{id}.{ext:jpg|png|gif}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> get(
			@PathVariable("id") ObjectId id,
			@PathVariable("ext") String ext,
			@RequestParam(value = "width", required = false, defaultValue = "0") Integer width,
			@RequestParam(value = "height", required = false, defaultValue = "0") Integer height,
			@RequestParam(value = "quality", required = false, defaultValue = "1") Float quality) {
		
		FileWrapper fw  = pictureRepository.getFileById(id);
		byte[] b = fw.getData();
		b = ImageUtils.scale(b, width, height, ext, quality);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf("image/" + ext));
		headers.setContentLength(b.length);
		return new ResponseEntity<byte[]>(b, headers, HttpStatus.OK);
	}

	private static boolean isImageSupported(final String contentType) {
		if (contentType == null)
			return false;
		for (String type : supportedImageTypes) {
			if (type.equals(contentType.trim()))
				return true;
		}
		return false;
	}
}

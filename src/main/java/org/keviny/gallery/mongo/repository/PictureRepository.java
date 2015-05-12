package org.keviny.gallery.mongo.repository;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.InputStream;
import java.util.Map;

import org.bson.types.ObjectId;
import org.keviny.gallery.common.FileMetadata;
import org.keviny.gallery.common.FileWrapper;
import org.springframework.web.multipart.MultipartFile;

public interface PictureRepository {
	public Map<String, Object> storeFile(MultipartFile file);
	public FileWrapper getFileById(ObjectId id);
	public void storeFile(InputStream in, FileMetadata meta);
	public void storeFile(byte[] b, FileMetadata meta);
}

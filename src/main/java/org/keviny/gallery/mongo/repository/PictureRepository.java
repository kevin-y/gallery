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
	Map<String, Object> storeFile(MultipartFile file);
	FileWrapper getFileById(ObjectId id);
	void storeFile(InputStream in, FileMetadata meta);
	void storeFile(byte[] b, FileMetadata meta);
}

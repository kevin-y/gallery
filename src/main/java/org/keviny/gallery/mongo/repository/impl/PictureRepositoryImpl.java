package org.keviny.gallery.mongo.repository.impl;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.keviny.gallery.common.FileMetadata;
import org.keviny.gallery.common.FileWrapper;
import org.keviny.gallery.mongo.repository.PictureRepository;
import org.keviny.gallery.util.MessageDigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

@Repository
public class PictureRepositoryImpl implements PictureRepository {

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	public Map<String, Object> storeFile(MultipartFile file) {
		Map<String, Object> m = null;
		try {
			GridFSFile gridFSFile = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());		
			m = new HashMap<String, Object>();
			m.put("filename", gridFSFile.getFilename());
			m.put("fid", gridFSFile.getId());
			m.put("md5sum", gridFSFile.getMD5());
			m.put("uploadTime", gridFSFile.getUploadDate());
			m.put("length", gridFSFile.getLength());
			m.put("contentType", gridFSFile.getContentType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public void storeFile(InputStream in, FileMetadata meta) {
		Assert.notNull(meta);
		GridFSFile gridFSFile = gridFsTemplate.store(in, meta.getFilename(), meta.getContentType());		
		if(gridFSFile != null) {
			meta.setFid(gridFSFile.getId());
			meta.setMd5sum(gridFSFile.getMD5());
			meta.setLength(gridFSFile.getLength());
			meta.setUploadTime(gridFSFile.getUploadDate());
		}
	}
	
	public void storeFile(byte[] b, FileMetadata meta) {
		Assert.notNull(meta);
		MessageDigestUtils md5 = MessageDigestUtils.getInstance("md5");
		String md5String = md5.encode(b);
		Query query = new Query();
		query.addCriteria(Criteria.where("md5").is(md5String));
		GridFSDBFile file = gridFsTemplate.findOne(query);
		
		if(file != null) {
			meta.setFid(file.getId());
			meta.setMd5sum(file.getMD5());
			meta.setLength(file.getLength());
			meta.setUploadTime(file.getUploadDate());
			return;
		}	
		GridFSFile fsFile = gridFsTemplate.store(new ByteArrayInputStream(b), meta.getFilename(), meta.getContentType());	
		if(fsFile != null) {
			meta.setFid(fsFile.getId());
			meta.setMd5sum(fsFile.getMD5());
			meta.setLength(fsFile.getLength());
			meta.setUploadTime(fsFile.getUploadDate());
		}
	}
	
	public FileMetadata getFileList() {
		return null;
	}
	
	public FileWrapper getFileById(ObjectId id) {
		Assert.notNull(id);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		GridFSDBFile file = gridFsTemplate.findOne(query);
		
		ByteArrayOutputStream bos = null;
		FileWrapper fw = null;
		try {
			if(file != null) {
				bos = new ByteArrayOutputStream();
				file.writeTo(bos);
				FileMetadata meta = new FileMetadata();
				meta.setContentType(file.getContentType());
				meta.setFilename(file.getFilename());
				meta.setLength(file.getLength());
				meta.setMd5sum(file.getMD5());
				meta.setUploadTime(file.getUploadDate());				
				fw = new FileWrapper(bos.toByteArray(), meta);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bos);
		}
		return fw;
	}
}

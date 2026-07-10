package com.nascorp.marketpal.service;

import lombok.RequiredArgsConstructor;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {

            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }

            Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "marketpal",
                    "resource_type", "image"
                )
            );

            return (String) uploadResult.get("secure_url");
        } catch (IOException ioe) {
            throw new RuntimeException("Image upload failed : " + ioe.getMessage());
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ioe) {
            throw new RuntimeException("Image deletion failed : " + ioe.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        String afterUpload = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);

        if (afterUpload.startsWith("v")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf("/" + 1));
        }

        return afterUpload.substring(0, afterUpload.lastIndexOf("."));
    }
    
}
package com.unigrab.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static com.unigrab.util.HandleExceptions.throwNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ClientService {

    private final AmazonS3 s3Client;
    @Value("${aws.s3.bucket}")
    private String S3_BUCKET_NAME;
    @Value("${aws.s3.image-base-url}")
    private String IMAGE_BASE_URL;

    public boolean uploadImage(String fileName, MultipartFile file) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            compressImage(file, outputStream);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(outputStream.size());
            metadata.setContentType(file.getContentType());

            s3Client.putObject(S3_BUCKET_NAME, IMAGE_BASE_URL + fileName, new ByteArrayInputStream(outputStream.toByteArray()), metadata);
            log.info("Successfully uploaded to s3 image: {}", fileName);
            return true;
        } catch (Exception e) {
            log.error("Failed to upload image: '{}' Exception: {}", fileName, String.valueOf(e));
            return false;
        }
    }

    public byte[] downloadImage(String url) {
        try {
            S3Object s3Object = s3Client.getObject(S3_BUCKET_NAME, IMAGE_BASE_URL + url);
            return s3Object.getObjectContent().readAllBytes();
        } catch (Exception e) {
            log.error("Failed to download image url: '{}' Exception: {}", url, String.valueOf(e));
            throwNotFoundException("img", String.format("Image '%s' is not found", url));
        }
        return new byte[0];
    }

    private void compressImage(MultipartFile file, ByteArrayOutputStream outputStream) {
        try {
            Thumbnails.of(file.getInputStream())
                    .size(800, 600)
                    .outputFormat("jpg")
                    .outputQuality(0.8)
                    .toOutputStream(outputStream);
        } catch (IOException e) {
            log.info("Error occurred while compressing image: {}, {}", file.getOriginalFilename(), e.getMessage());
        }
    }

}

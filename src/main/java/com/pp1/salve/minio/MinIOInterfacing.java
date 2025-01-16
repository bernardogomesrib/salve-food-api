package com.pp1.salve.minio;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.transaction.Transactional;

@Service
public class MinIOInterfacing {
    @Autowired
    private MinioClient minioClient;

    @Transactional
    public String getSingleUrl(String bucketName, String fileName) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(60 * 60)
                            .build());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public String uploadFile(String bucketName, String nomeUnico, MultipartFile file) throws Exception {
        try {
            if (!bucketExists(bucketName)) {
                createBucket(bucketName);
            }
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nomeUnico)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build());
            return getSingleUrl(bucketName, nomeUnico);

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public boolean createBucket(String bucketName) throws Exception {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public boolean bucketExists(String bucketName) throws Exception {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw e;
        }
    }
    @Transactional
    public void deleteFile(String bucketName, String fileName) throws Exception {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw e;
        }
    }
}

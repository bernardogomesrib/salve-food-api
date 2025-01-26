package com.pp1.salve.minio;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.transaction.Transactional;

@Service
public class MinIOInterfacing {
    @Autowired
    private MinioClient minioClient;

    @Transactional
    public String getSingleUrl(String bucketName, String fileName) throws Exception {
        try {
            if (!(minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build()).size()>0)) {
                return null;
            }
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
            if (!(minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build()).size()>0)) {
                return;
            }

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw e;
        }
    }
    @Transactional
    public void deleteBucket(String bucketName) throws Exception {
        try {

            Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : objects) {
                Item item = result.get();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            throw e;
        }
    }
}

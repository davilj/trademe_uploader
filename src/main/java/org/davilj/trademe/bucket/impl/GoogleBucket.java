package org.davilj.trademe.bucket.impl;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.davilj.trademe.bucket.IBucket;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.logging.Logger;

/**
 * Created by daniev on 24/04/17.
 */
public class GoogleBucket implements IBucket {
        private static final Logger LOGGER = Logger.getLogger(GoogleBucket.class.getName());
        private Storage storage;
        private String bucketName;

        public GoogleBucket(Storage storage, String bucketName) {
            this.storage = storage;
            this.bucketName = bucketName;
        }

        @Override
        public boolean addFile(final File file)  {
            //Doing this in groovy fails?? Silently!!
            BlobId blobId = BlobId.of(this.bucketName, file.getName());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();

            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = Files.newInputStream(file.toPath())) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        writer.write(ByteBuffer.wrap(buffer, 0, limit));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info(String.format("Uploaded file: %s", file));
            return true;

        }
    }

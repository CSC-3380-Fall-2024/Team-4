package org.eru.models.cloudstorage;

import org.eru.errorhandling.exceptions.common.NotFoundException;
import org.eru.managers.CryptoManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CloudStorageFile {
    @JsonProperty("uniqueFilename")
    public String UniqueFilename;

    @JsonProperty("fileName")
    public String FileName;

    @JsonProperty("hash")
    public String Hash;

    @JsonProperty("hash256")
    public String Hash256;

    @JsonProperty("length")
    public long Length;

    @JsonProperty("contentType")
    public String ContentType = "application/octet-stream";

    @JsonProperty("uploaded")
    public String Uploaded;

    @JsonProperty("storageType")
    public String StorageType = "S3";

    @JsonProperty("doNotCache")
    public boolean DoNotCache = true;

    public CloudStorageFile(String filePath) throws NotFoundException {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));

            File file = ResourceUtils.getFile(filePath);
            FileName = file.getName();

            byte[] data;

            try (InputStream in = new FileInputStream(file)) {
                data = in.readAllBytes();
            }
            catch (Exception e) {
                throw new NotFoundException();
            }

            Hash = CryptoManager.HashSha1(data);
            Hash256 = CryptoManager.HashSha256(data);
            Length = file.length();
            Uploaded = dtf.format(Instant.ofEpochMilli(file.lastModified()));

        } catch (Exception e) {
            throw new NotFoundException();
        }
    }
}

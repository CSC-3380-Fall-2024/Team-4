package org.eru.controllers;

import org.eru.errorhandling.exceptions.common.NotFoundException;
import org.eru.models.cloudstorage.CloudStorageFile;
import org.eru.models.cloudstorage.Config;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CloudStorageController {
    @GetMapping("/eru/api/cloudstorage/system")
    public ResponseEntity<CloudStorageFile[]> system() throws NotFoundException, FileNotFoundException {
        String path = "classpath:CloudStorage/";
        File folder = ResourceUtils.getFile(path);
        File[] listOfFiles = folder.listFiles();

        CloudStorageFile[] files = new CloudStorageFile[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            files[i] = new CloudStorageFile(path + listOfFiles[i].getName());
            files[i].UniqueFilename = GetCloudstorageFile(listOfFiles[i].getName());
        }

        return ResponseEntity.ok(files);
    }

    @GetMapping("/eru/api/cloudstorage/system/config")
    public ResponseEntity<Config> config() {
        return ResponseEntity.ok(new Config());
    }

    @GetMapping(value = "/eru/api/cloudstorage/system/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] systemGet(@PathVariable String fileName) throws NotFoundException {
        String filePath = "classpath:CloudStorage/" + GetCloudstorageFile(fileName) + ".ini";

        System.out.println(filePath);
        try {
            File file = ResourceUtils.getFile(filePath);
            try (InputStream in = new FileInputStream(file)) {
                return in.readAllBytes();
            }
            catch (Exception e) {
                throw new NotFoundException();
            }
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    @GetMapping("/eru/api/cloudstorage/user/{accountId}")
    public ResponseEntity<String[]> user(@PathVariable String accountId) {
        return ResponseEntity.ok(new String[0]);
    }

    @GetMapping("/eru/api/cloudstorage/user/{accountId}/{fileName}")
    public ResponseEntity userGet(@PathVariable String accountId, @PathVariable String fileName) {
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/eru/api/cloudstorage/user/{accountId}/{fileName}")
    public ResponseEntity userPut(@PathVariable String accountId, @PathVariable String fileName) {
        return ResponseEntity.status(204).build();
    }

    private static String GetCloudstorageFile(String file) {
        HashMap<String, String> dictionary = new HashMap<String, String>() {
            {
                put("DefaultGame", "a22d837b6a2b46349421259c0a5411bf");
                put("DefaultEngine", "3460cbe1c57d4a838ace32951a4d7171");
                put("DefaultInput", "mhl5jvb7fm85e157u49k1lbf8p9kpj50");
                put("DefaultRuntimeOptions", "894da02711046b48cc1e712756a172bc01b24a32");
            }
        };

        if (!dictionary.containsKey(file.split("\\.")[0])) {
            for (Map.Entry<String, String> entry : dictionary.entrySet()) {
                if (entry.getValue().equals(file.split("\\.")[0])) {
                    return entry.getKey();
                }
            }
        }

        return dictionary.get(file.split("\\.")[0]);
    }
}

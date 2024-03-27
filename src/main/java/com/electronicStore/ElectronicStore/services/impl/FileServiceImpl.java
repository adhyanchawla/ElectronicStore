package com.electronicStore.ElectronicStore.services.impl;

import com.electronicStore.ElectronicStore.exceptions.BadApiRequest;
import com.electronicStore.ElectronicStore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFilename = file.getOriginalFilename();
        logger.info("File name : {}", originalFilename);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithExtension = path + fileNameWithExtension;

        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")) {
            logger.info("File extension is: {}", extension);
            File folder = new File(path);
            if(!folder.exists()) {
                //create a folder
                folder.mkdirs();
            }

            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithExtension));
            return fullPathWithExtension;
        } else {
            throw new BadApiRequest("Extension Not Allowed!");
        }

    }

    @Override
    public InputStream getResource(String name, String path) throws FileNotFoundException {
//        System.out.println(name + " " + path);
        String fullPath = path;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}

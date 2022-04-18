package org.apiaddicts.apitools.apigen.generatorcore.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {

    private static final int BUFFER_SIZE = 2048;

    private ZipUtils() {
        // Intentional blank
    }

    public static Path zip(Path folderPath) {
        zipDirectory(folderPath.toFile(), folderPath.toString() + ".zip");
        return Paths.get(folderPath.toString() + ".zip");
    }

    private static void zipDirectory(File folder, String zipDirName) {
        try (FileOutputStream fos = new FileOutputStream(zipDirName); ZipOutputStream zos = new ZipOutputStream(fos)) {
            List<String> filePaths = getFilePaths(folder);
            for (String filePath : filePaths) {
                ZipEntry ze = new ZipEntry(filePath.substring(folder.getAbsolutePath().length() + 1));
                zos.putNextEntry(ze);
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }
        } catch (IOException e) {
            log.error("Error creating zip file", e);
        }
    }

    private static List<String> getFilePaths(File folder) {
        List<String> filePaths = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) return filePaths;
        for (File file : files) {
            if (file.isFile()) {
                filePaths.add(file.getAbsolutePath());
            } else {
                filePaths.addAll(getFilePaths(file));
            }
        }
        return filePaths;
    }

    public static boolean isZip(MultipartFile file) {
        String type = file.getContentType();
        return "application/zip".equals(type) || "application/x-zip-compressed".equals(type);
    }

    public static Path unzip(byte[] bytes) throws IOException {
        Path directory = Files.createTempDirectory("");
        return unzip(bytes, directory);
    }

    public static Path unzip(byte[] bytes, Path directory) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(bytes));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = directory.toString() + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                File file = new File(filePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, false))) {
                    byte[] bytesIn = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = zipIn.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                }
            } else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return directory;
    }

}

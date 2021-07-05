package org.apiaddicts.apitools.apigen.generatorcore.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipUtilsTest {

    @Test
    void givenAFolderWithFiles_whenConvertToZip_thenSuccess(@TempDir Path path) throws IOException {
        Files.createFile(Paths.get(path.toString(), "file.txt"));
        Files.createDirectory(Paths.get(path.toString(), "folder"));
        Files.createFile(Paths.get(path.toString(), "folder", "file.txt"));
        Path zipPath = ZipUtils.zip(path);
        assertTrue(Files.exists(zipPath), "Zip file not generated");
    }

    @Test
    void givenAFolderWithFiles_whenConvertToZip_thenSuccess(@TempDir Path pathSource, @TempDir Path pathTarget) throws IOException {
        Files.createFile(Paths.get(pathSource.toString(), "file.txt"));
        Files.createDirectory(Paths.get(pathSource.toString(), "folder"));
        Files.createFile(Paths.get(pathSource.toString(), "folder", "file.txt"));
        Path zipPath = ZipUtils.zip(pathSource);
        ZipUtils.unzip(Files.readAllBytes(zipPath), pathTarget);
        assertTrue(Files.exists(Paths.get(pathTarget.toString(), "file.txt")));
        assertTrue(Files.exists(Paths.get(pathTarget.toString(), "folder")));
        assertTrue(Files.exists(Paths.get(pathTarget.toString(), "folder", "file.txt")));
    }
}
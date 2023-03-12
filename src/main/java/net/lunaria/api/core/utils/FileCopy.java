package net.lunaria.api.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileCopy {
    public static void copy(File src, File dest) {
        copy(src.toPath(), dest.toPath());
    }

    public static void copy(Path src, Path dest) {
        try {
            Files.walkFileTree(src, new CopyFileVisitor(src, dest));
            System.out.println("[✓] " + src + " -> " + dest);
        } catch (IOException e) {
            System.err.println("[X] " + e.getMessage());
        }
    }
    public static boolean delete(File file) {
        File[] files = file.listFiles();

        if (files != null) {
            for (File delete : files) {
                delete(delete);
            }
        }

        return file.delete();
    }
}

class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private final Path source;
    private final Path destination;

    public CopyFileVisitor(Path source, Path destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetPath = destination.resolve(source.relativize(dir));
        if (!Files.exists(targetPath)) {
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetPath = destination.resolve(source.relativize(file));
        Files.copy(file, targetPath);
        return FileVisitResult.CONTINUE;
    }
}

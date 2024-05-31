package ru.job4j.finder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FinderFile extends SimpleFileVisitor<Path> {

    private Predicate<Path> condition;
    private List<Path> paths;

    public FinderFile(Predicate<Path> condition) {
        this.condition = condition;
        this.paths = new ArrayList<>();
    }

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (this.condition.test(file)) {
            this.paths.add(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
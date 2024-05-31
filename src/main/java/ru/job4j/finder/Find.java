package ru.job4j.finder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class Find {
    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        FinderFile finderFile = new FinderFile(condition);
        Files.walkFileTree(root, finderFile);
        return finderFile.getPaths();
    }

    private void writeToFile(List<Path> paths, File outFile) throws IOException {
        try (FileWriter writer = new FileWriter(outFile)) {
            for (Path path : paths) {
                writer.write(path.toString());
                writer.write(System.lineSeparator());
            }
        }
    }
    public static void main(String[] args) throws IOException {
        ArgsName argsName = ArgsName.of(args);
        List<Path> paths = search(Paths.get(argsName.get("d")),
                path -> path.toFile().getName().equals(argsName.get("n")));
        new Find().writeToFile(paths, new File(argsName.get("o")));
    }
}

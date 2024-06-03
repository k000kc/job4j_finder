package ru.job4j.finder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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

    private Predicate<Path> checkType(ArgsName args) {
        Predicate<Path> predicate = null;
        String type = args.get("t");
        String name = args.get("n");
        if (type.equals("name")) {
            predicate = path -> path.getFileName().toFile().toString()
                    .equals(name);
        }
        if (type.equals("mask")) {
            predicate = path -> path.getFileName().toFile().toString()
                    .matches(maskToRegex(name));
        }
        if (type.equals("regex")) {
            predicate = path -> Pattern.compile(name)
                    .matcher(path.getFileName().toFile().toString())
                    .matches();
        }
        return predicate;
    }

    private String maskToRegex(String mask) {
        return mask.replace("*", "\\S*")
                .replace("?", "\\S{1}")
                .replace(".", "\\.");
    }

    public static void main(String[] args) throws IOException {
        Find find = new Find();
        ArgsName argsName = ArgsName.of(args);
        List<Path> paths = search(Paths.get(argsName.get("d")), find.checkType(argsName));
        find.writeToFile(paths, new File(argsName.get("o")));
    }
}

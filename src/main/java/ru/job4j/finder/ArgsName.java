package ru.job4j.finder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArgsName {
    private final Map<String, String> values = new HashMap<>();
    private String dirPath;
    private String fileName;
    private String searchType;
    private String outFile;

    public String get(String key) {
        this.chekKey(key);
        return values.get(key);
    }

    private void chekKey(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
    }

    private void chekArgs(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        Arrays.stream(args).forEach(str -> {
            String[] array = str.split("=");
            if (!str.startsWith("-")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not start with a '-' character", str));
            }
            if (str.startsWith("-=")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a key", str));
            }
            if (!str.contains("=")) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain an equal sign", str));
            }
            if (array[1].isEmpty()) {
                throw new IllegalArgumentException(String.format("Error: This argument '%s' does not contain a value", str));
            }
            if (str.startsWith("-d")) {
                this.dirPath = array[1];
            }
            if (str.startsWith("-n")) {
                this.fileName = array[1];
            }
            if (str.startsWith("-t")) {
                this.searchType = array[1];
            }
            if (str.startsWith("-o")) {
                this.outFile = array[1];
            }
        });
        if (dirPath == null || fileName == null || searchType == null || outFile == null) {
            throw new IllegalArgumentException("Usage: -d=directory -n=name/mask/regex -t=type -o=output file");
        }
    }

    private void parse(String[] args) {
        String[] argsArray;
        for (String arg : args) {
            argsArray = arg.split("=");
            this.values.put(argsArray[0].substring(1), argsArray[1]);
        }
    }

    public static ArgsName of(String[] args) {
        ArgsName names = new ArgsName();
        names.chekArgs(args);
        names.parse(args);
        return names;
    }
}


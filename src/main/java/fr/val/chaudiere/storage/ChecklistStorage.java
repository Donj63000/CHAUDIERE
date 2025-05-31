package fr.val.chaudiere.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.val.chaudiere.model.Checklist;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sauvegarde les check-lists en JSON dans un fichier local.
 */
public final class ChecklistStorage {

    private static final Path FILE = Paths.get("inspections.json");
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private ChecklistStorage() { }

    public static void save(Checklist cl) {
        List<Checklist> all = loadAll();
        all.add(cl);
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), all);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<Checklist> loadAll() {
        if (!Files.exists(FILE)) return new ArrayList<>();
        try {
            return Arrays.asList(MAPPER.readValue(FILE.toFile(), Checklist[].class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

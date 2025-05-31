package fr.val.chaudiere.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Une inspection complète de chaudière.
 */
public record Checklist(LocalDateTime date, List<Field> fields) { }

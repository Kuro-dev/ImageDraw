package org.kurodev.graph.calculator.sanitisation;

/**
 * Sanitizers are being called prior to *any* evaluations.
 */
public interface Sanitizer {
    String sanitize(String formula);
}

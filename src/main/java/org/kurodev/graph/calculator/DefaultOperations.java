package org.kurodev.graph.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

/**
 * Conclusive list of all operations that can be calculated by the calculator in order of precedence.
 */
public enum DefaultOperations implements Operation {
    ADD('+') {
        @Override
        public BigDecimal conclude(BigDecimal a, BigDecimal b) {
            return a.add(b);
        }
    },
    SUBTRACT('-') {
        @Override
        public BigDecimal conclude(BigDecimal a, BigDecimal b) {
            return a.subtract(b);
        }
    },
    MULTIPLY('*') {
        @Override
        public BigDecimal conclude(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }
    },
    DIVIDE('/') {
        @Override
        public BigDecimal conclude(BigDecimal a, BigDecimal b) {
            return a.divide(b, RoundingMode.HALF_UP);
        }
    },
    POW('^') {
        @Override
        public BigDecimal conclude(BigDecimal a, BigDecimal b) {
            return a.pow(b.intValueExact());
        }
    };
    private final char chara;
    private final int precedence;

    DefaultOperations(char sign) {
        this.chara = sign;
        this.precedence = ordinal();
    }

    public static DefaultOperations of(String search) {
        return of(search, () -> {
            throw new IllegalArgumentException(search);
        });
    }

    public static DefaultOperations of(String search, DefaultOperations orElse) {
        return of(search, () -> orElse);
    }

    public static DefaultOperations of(String search, Supplier<DefaultOperations> orElse) {
        for (DefaultOperations value : DefaultOperations.values()) {
            if (value.name().equalsIgnoreCase(search)) {
                return value;
            }
            if (search.equals(String.valueOf(value.chara))) {
                return value;
            }
        }
        return orElse.get();
    }

    public static DefaultOperations of(char chara) {
        return of(String.valueOf(chara));
    }


    public DefaultOperations interact(DefaultOperations other) {
        if (other == this) {
            return ADD;
        }
        return SUBTRACT;
    }

    /**
     * @return The sign which is being parsed form the string. "ADD" for example would return "+"
     */
    @Override
    public char getOperator() {
        return chara;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}

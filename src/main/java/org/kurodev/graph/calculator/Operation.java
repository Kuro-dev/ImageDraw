package org.kurodev.graph.calculator;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public interface Operation {
    /**
     * @param name             The name of the new operation, could be a descriptive String like "Addition"
     * @param identifier       Identifier. Example 'P', usage "1p2"
     * @param precedence       Precedence of the operation.
     * @param concludeFunction The calculation this operation is supposed to perform, if invoked.
     *                         In the example from above "a" would be "1" and "b" would be "2"
     * @return An Operation object
     */
    static Operation of(String name, char identifier, int precedence, BiFunction<BigDecimal, BigDecimal, BigDecimal> concludeFunction) {
        return new Operation() {
            @Override
            public BigDecimal conclude(BigDecimal a, BigDecimal b) {
                return concludeFunction.apply(a, b);
            }

            @Override
            public char getOperator() {
                return identifier;
            }

            @Override
            public int getPrecedence() {
                return precedence;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    BigDecimal conclude(BigDecimal a, BigDecimal b);

    char getOperator();

    int getPrecedence();

    String name();
}

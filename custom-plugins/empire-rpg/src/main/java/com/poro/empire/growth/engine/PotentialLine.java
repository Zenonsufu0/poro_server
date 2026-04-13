package com.poro.empire.growth.engine;

public record PotentialLine(
        int lineNo,
        PotentialGrade grade,
        String optionCode,
        double value
) {
}

package mate.academy.dto;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String[] title,
        String[] author,
        BigDecimal[] price
) {
}

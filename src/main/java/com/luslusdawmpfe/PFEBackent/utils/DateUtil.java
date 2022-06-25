package com.luslusdawmpfe.PFEBackent.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

public class DateUtil {

    private static final String TIMEZONE_HEADER_NAME = "Timezone-Val";

    public static OffsetDateTime now() {
        var zone = ZoneId.systemDefault();
        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            zone = Optional.ofNullable(request.getHeader(TIMEZONE_HEADER_NAME))
                    .map(ZoneId::of)
                    .orElse(ZoneId.systemDefault());
        }
        return OffsetDateTime.ofInstant(Instant.now(), zone);
    }
}

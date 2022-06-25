package com.luslusdawmpfe.PFEBackent.configs;

import com.luslusdawmpfe.PFEBackent.utils.DateUtil;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component("auditingDateTimeProvider")
public class DateTimeProviderImpl implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(DateUtil.now());
    }
}

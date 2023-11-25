package com.my;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
//@Component
@ConfigurationProperties(prefix = "servers")
@Validated
public class ServerConfig {
    private String ipAddress;
    @Max(value = 9000, message = "最大值不能超过9000")
    @Min(value = 200, message = "最小值不能小于200")
    private int port;
    private long timeout;

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration serverTimeOut;
    @DataSizeUnit(DataUnit.KILOBYTES)
    private DataSize dataSize;
}

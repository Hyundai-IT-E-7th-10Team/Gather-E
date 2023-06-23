package com.kosa.gather.e.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.kosa.gather.e")
public class DBConfig {
}

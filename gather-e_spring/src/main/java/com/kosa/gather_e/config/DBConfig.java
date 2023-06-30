package com.kosa.gather_e.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.kosa.gather_e.**.dao")
public class DBConfig {
}

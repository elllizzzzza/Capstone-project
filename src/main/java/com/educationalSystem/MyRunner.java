package com.educationalSystem;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

@Component
public class MyRunner implements CommandLineRunner {

    private final ApplicationContext ctx;
    private final Environment env;

    public MyRunner(ApplicationContext ctx, Environment env) {
        this.ctx = ctx;
        this.env = env;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== All Environment Properties ===");
        Properties props = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, env.getProperty(propName)));

        props.forEach((key, value) -> System.out.println(key + " = " + value));

        System.out.println("=== End of Environment Properties ===");

        System.out.println("\n=== All Beans ===");
        Arrays.stream(ctx.getBeanDefinitionNames()).forEach(System.out::println);
    }
}


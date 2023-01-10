package org.apiaddicts.apitools.apigen.archetypecore.core.persistence.executor;

import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.Embeddable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ApigenSearchExecutorTypeConverters {

    private static ConcurrentHashMap<Class, ApigenConverter> converters = new ConcurrentHashMap<>();

    static {
        register(String.class, s -> s);
        register(Integer.class, s -> s == null ? null : Integer.parseInt(s));
        register(Long.class, s -> s == null ? null : Long.parseLong(s));
        register(Float.class, s -> s == null ? null : Float.parseFloat(s));
        register(Double.class, s -> s == null ? null : Double.parseDouble(s));
        register(LocalDate.class, s -> s == null ? null : LocalDate.parse(s));
        register(OffsetDateTime.class, s -> s == null ? null : OffsetDateTime.parse(s));
        register(Instant.class, s -> s == null ? null : OffsetDateTime.parse(s).toInstant());
        register(Boolean.class, s -> s == null ? null : Boolean.parseBoolean(s));
        register(BigInteger.class, s -> s == null? null : new BigInteger(s));
        register(BigDecimal.class, s -> s == null? null : new BigDecimal(s));
    }

    private ApigenSearchExecutorTypeConverters() {
        // Intentional blank
    }


    public static void register(Class clazz, ApigenConverter converter) {
        converters.put(clazz, converter);
    }

    public static Comparable convert(Class clazz, String value) {
        if (clazz.isEnum()) {
            return Enum.valueOf(clazz, value);
        }
        if (clazz.isAnnotationPresent(Embeddable.class)) {
            return convertEmbedded(clazz, value);
        }
        ApigenConverter converter = converters.get(clazz);
        if (converter == null) throw new IllegalArgumentException("No converter found for type: " + clazz);
        return converter.convert(value);
    }

    private static Comparable convertEmbedded(Class clazz, String value) {
        try {
            Method m = clazz.getMethod("from", String.class);
            return (Comparable) m.invoke(null, (Object) value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error: ", e);
            return null;
        }
    }
}

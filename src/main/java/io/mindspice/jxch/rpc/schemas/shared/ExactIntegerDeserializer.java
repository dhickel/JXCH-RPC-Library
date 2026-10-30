package io.mindspice.jxch.rpc.schemas.shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.math.BigInteger;

/** Reads integer tokens without Jackson's floating-point-to-integer coercion. */
public final class ExactIntegerDeserializer extends JsonDeserializer<Number> implements ContextualDeserializer {
    private final Class<?> type;

    public ExactIntegerDeserializer() { this(Number.class); }
    private ExactIntegerDeserializer(Class<?> type) { this.type = type; }

    @Override public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
        var valueType = property.getType();
        if (valueType.isContainerType()) valueType = valueType.getContentType();
        return new ExactIntegerDeserializer(valueType.getRawClass());
    }

    @Override public Number deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            return (Number) context.handleUnexpectedToken(type, parser);
        }
        var value = parser.getBigIntegerValue();
        try {
            if (type == int.class || type == Integer.class) return value.intValueExact();
            if (type == long.class || type == Long.class) return value.longValueExact();
            if (type == BigInteger.class) return value;
            return parser.getNumberValue();
        } catch (ArithmeticException e) {
            return context.reportInputMismatch(type, "Integer outside %s range: %s", type.getSimpleName(), value);
        }
    }

    @Override public Number getNullValue(DeserializationContext context) {
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        return null;
    }

    public static Number requireIntegral(Number value) {
        if (value != null && !(value instanceof Byte || value instanceof Short || value instanceof Integer
                || value instanceof Long || value instanceof BigInteger)) {
            throw new IllegalArgumentException("Expected an exact integral Number");
        }
        return value;
    }
}

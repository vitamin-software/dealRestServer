package io.vitamin.deal.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import io.vitamin.deal.domain.Deal;


public class DealTypeIdResolver implements TypeIdResolver {

    private static final String DEAL_PACKAGE = Deal.class.getPackage().getName();
    private JavaType mBaseType;

    @Override
    public void init(JavaType baseType) {
        mBaseType = baseType;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    @Override
    public String idFromValue(Object obj) {
        return idFromValueAndType(obj, obj.getClass());
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, mBaseType.getRawClass());
    }

    @Override
    public String idFromValueAndType(Object obj, Class<?> clazz) {
        String name = clazz.getName();
        if (name.startsWith(DEAL_PACKAGE)) {
            return name.substring(DEAL_PACKAGE.length() + 1);
        }
        throw new IllegalStateException("class " + clazz + " is not in the package " + DEAL_PACKAGE);
    }

    @Override
    public JavaType typeFromId(String type) {
        Class<?> clazz;
        String clazzName = DEAL_PACKAGE + "." + type;
        try {
            clazz = ClassUtil.findClass(clazzName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("cannot find class '" + clazzName + "'");
        }
        return TypeFactory.defaultInstance().constructSpecializedType(mBaseType, clazz);
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String type) {
        return typeFromId(type);
    }
}

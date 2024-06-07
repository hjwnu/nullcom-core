package org.nullcom.nullcomcore.util;

import java.beans.FeatureDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.nullcom.nullcomcore.dto.BasicDto;
import org.nullcom.nullcomcore.dto.BasicEntity;
import org.nullcom.nullcomcore.dto.EntityDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

public class ObjectUtil extends ObjectUtils {
  public static boolean isString(Object object) {
    return equalsClass(String.class, object);
  }

  public static boolean isString(Class<?> cls) {
    return equalsClass(String.class, cls);
  }

  public static boolean isInt(Object object) {
    return equalsClass(Integer.class, object);
  }

  public static boolean isInt(Class<?> cls) {
    return equalsClass(Integer.class, cls);
  }

  public static boolean isLong(Object object) {
    return equalsClass(Long.class, object);
  }

  public static boolean isLong(Class<?> cls) {
    return equalsClass(Long.class, cls);
  }

  public static boolean isDouble(Object object) {
    return equalsClass(Double.class, object);
  }

  public static boolean isDouble(Class<?> cls) {
    return equalsClass(Double.class, cls);
  }

  public static boolean isBoolean(Object object) {
    return equalsClass(Boolean.class, object);
  }

  public static boolean isBoolean(Class<?> cls) {
    return equalsClass(Boolean.class, cls);
  }

  public static boolean isEntity(Object object) {
    return equalsClass(BasicEntity.class, object);
  }

  public static boolean isEntity(Class<?> cls) {
    return equalsClass(BasicEntity.class, cls);
  }

  public static boolean isDto(Object object) {
    return equalsClass(BasicDto.class, object);
  }

  public static boolean isDto(Class<?> cls) {
    return equalsClass(BasicDto.class, cls);
  }

  public static boolean isVoDto(Object object) {
    return (equalsClass(EntityDto.class, object));
  }

  public static boolean isVoDto(Class<?> cls) {
    return (equalsClass(EntityDto.class, cls));
  }

  public static boolean isOptional(Object object) {
    return equalsClass(Optional.class, object);
  }

  public static boolean equalsClass(Object object1, Object object2) {
    return equalsClass(object1.getClass(), object2.getClass());
  }

  public static boolean equalsClass(Object object1, Class<?> cls2) {
    return equalsClass(cls2, object1.getClass());
  }

  public static boolean equalsClass(Class<?> cls1, Object object2) {
    return equalsClass(cls1, object2.getClass());
  }

  public static boolean equalsClass(Class<?> cls1, Class<?> cls2) {
    if (cls1.isAssignableFrom((cls2))) {
      return true;
    }

    return false;
  }

  public static boolean isArray(Object object) {
    if (object instanceof Object[]) {
      return true;
    }

    return false;
  }

  public static boolean isStringArray(Object object) {
    if (object instanceof String[]) {
      return true;
    }

    return false;
  }

  public static boolean isList(Object object) {
    return isList(object.getClass());
  }

  public static boolean isList(Class<?> cls) {
    if (List.class.isAssignableFrom((cls))) {
      return true;
    }

    return false;
  }

  public static boolean isMap(Object object) {
    return isMap(object.getClass());
  }

  public static boolean isMap(Class<?> cls) {
    if (Map.class.isAssignableFrom((cls))) {
      return true;
    }

    return false;
  }

  public static List<Field> getAllFields(Object object) {
    return getAllFields(object.getClass());
  }

  public static List<Field> getAllFields(Class<?> cls) {
    List<Field> list = new ArrayList<>();

    ReflectionUtils.doWithFields(cls,
        field -> {
          if (!StringUtil.equalsIgnoreCase("serialVersionUID", field.getName())) {
            list.add(field);
          }
        });
    return list;
  }

  public static List<String> getAllFieldNames(Class<?> cls) {
    List<String> list = new ArrayList<>();

    ReflectionUtils.doWithFields(cls,
        field -> {
          if (!StringUtil.equalsIgnoreCase("serialVersionUID", field.getName())) {
            list.add(field.getName());
          }
        });
    return list;
  }

  public static boolean containsFields(Class<?> cls, String fieldName) {
    List<Field> fieldList = getAllFields(cls);

    for (Field field : fieldList) {
      if (field.getName().equals(fieldName)) {
        return true;
      }
    }
    return false;
  }

  public static String getValueToString(Object object, Field field) {
    return (String) getValue(object, field);
  }

  public static String getValueToString(Object object, String fieldName) {
    return (String) getValue(object, fieldName);
  }

  public static Object getValue(Object object, Field field) {
    ReflectionUtils.makeAccessible(field);
    return ReflectionUtils.getField(field, object);
  }

  public static Object getValue(Object object, String fieldName) {
    return getValue(object, ReflectionUtils.findField(object.getClass(), fieldName));
  }

  public static List<?> getValues(Object object, List<Field> fieldList) {
    List<Object> list = new ArrayList<>();

    fieldList.forEach(field -> {
      list.add(getValue(object, field));
    });

    return list;
  }

  public static List<?> getValuesByFieldName(Object object, String[] fieldNames) {
    return getValuesByFieldName(object, Arrays.asList(fieldNames));
  }

  public static List<?> getValuesByFieldName(Object object, List<String> fieldNameList) {
    List<Object> list = new ArrayList<>();

    fieldNameList.forEach(fieldName -> {
      list.add(getValue(object, fieldName));
    });

    return list;
  }

  public static Field getFieldByAnnotation(Object object, Class<? extends Annotation> annotationClass) {
    return getFieldByAnnotation(object.getClass(), annotationClass);
  }

  public static Field getFieldByAnnotation(Class<?> cls, Class<? extends Annotation> annotationClass) {
    List<Field> fieldList = getAllFields(cls);

    for (Field field : fieldList) {
      if (field.isAnnotationPresent(annotationClass)) {
        return field;
      }
    }

    return null;
  }

  public static List<Field> getAllFieldsByAnnotation(Object object, Class<? extends Annotation> annotationClass) {
    return getAllFieldsByAnnotation(object.getClass(), annotationClass);
  }

  public static List<Field> getAllFieldsByAnnotation(Class<?> cls, Class<? extends Annotation> annotationClass) {
    List<Field> allFieldList = new ArrayList<>();

    ReflectionUtils.doWithFields(cls,
        field -> {
          if (field.isAnnotationPresent(annotationClass)) {
            allFieldList.add(field);
          }
        });

    return allFieldList;
  }

  public static Object getValueByAnnotation(Object object, Class<? extends Annotation> annotationClass) {
    List<Field> fieldList = getAllFields(object);

    for (Field field : fieldList) {
      if (field.isAnnotationPresent(annotationClass)) {
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, object);
      }
    }

    return null;
  }

  public static void setValue(Object object, Field field, Object value) {
    if (!Modifier.isFinal(field.getModifiers())) {
      ReflectionUtils.makeAccessible(field);

      if (isString(field.getType()) && value != null) {
        ReflectionUtils.setField(field, object, String.valueOf(value));
      } else {
        ReflectionUtils.setField(field, object, value);
      }
    }
  }

  public static void setValue(Object object, String fieldName, Object value) {
    Field field = ReflectionUtils.findField(object.getClass(), fieldName);

    if (field != null) {
      setValue(object, field, value);
    }
  }

  public static Object copy(Object source) {
    Object target = null;
    Class<?> cls = source.getClass();
    Constructor<?> constructor;

    try {
      constructor = cls.getDeclaredConstructor();
      target = constructor.newInstance();
      BeanUtils.copyProperties(source, target);
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
             IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return target;
  }

  public static Object copy(Object source, Class<?> targetClass) {
    Object target = null;
    try {
      Constructor<?> constructor = targetClass.getDeclaredConstructor();
      target = constructor.newInstance();
      BeanUtils.copyProperties(source, target);
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
             IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return target;
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> copyList(List<?> sourceList, Class<T> targetClass) {
    List<T> list = new ArrayList<>();
    T target = null;

    try {
      Constructor<?> constructor = targetClass.getDeclaredConstructor();

      for(Object source : sourceList) {
        target = (T) constructor.newInstance();
        BeanUtils.copyProperties(source, target);
        list.add(target);
      }
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
             IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return list;
  }

  /**
   * source -> target으로 무조건 복사
   *
   * @param source
   * @param target
   */
  public static void copy(Object source, Object target) {
    BeanUtils.copyProperties(source, target);
  }

  /**
   * source -> target으로 무조건 복사 + ignoreFields 는 제외
   *
   * @param source
   * @param target
   */
  public static void copy(Object source, Object target, String... ignoreFields) {
    BeanUtils.copyProperties(source, target, ignoreFields);
  }

  /**
   * source -> target으로 복사 + source 에서 null인 것들은 제외
   *
   * @param source
   * @param target
   */
  public static Object copyNonNullValues(Object source, Object target) {
    if (source == null ||target == null || source.getClass() != target.getClass()) {
      return null;
    }

    final BeanWrapper src = new BeanWrapperImpl(source);
    final BeanWrapper trg = new BeanWrapperImpl(target);


    for (final Field property : source.getClass().getDeclaredFields()) {
      Object sourceProperty = src.getPropertyValue(property.getName());
      if (sourceProperty != null && !(sourceProperty instanceof Collection<?>)) {
        trg.setPropertyValue(property.getName(), sourceProperty);
      }
    }

    return target;
  }
  /**
   * copyFields 필드만 복사
   * @param source
   * @param target
   * @param copyFields
   */
  public static void copyAppointedFields(Object source, Object target, List<String> copyFields) {
    ReflectionUtils.doWithFields(target.getClass(),
        field -> {
          if(copyFields.contains(field.getName())) {
            setValue(target, field, getValue(source, field.getName()));
          }
        });
  }

  public static void copyAppointedFields(Map<String, ?> sourceMap, Object target, List<String> copyFields) {
    ReflectionUtils.doWithFields(target.getClass(),
        field -> {
          if(copyFields.contains(field.getName())) {
            setValue(target, field, sourceMap.get(field.getName()));
          }
        });
  }

  public static void copyAppointedFields(Map<String, ?> sourceMap, Object target, String... copyFields) {
    List<String> copyFieldList = Arrays.asList(copyFields);
    copyAppointedFields(sourceMap, target, copyFieldList);
  }

  /**
   * source 에서 null인 것들만 추출
   *
   * @param source
   * @return
   */
  private static String[] getNullPropertyNames(Object source) {
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }

  public static Class<?> getGenericType(Field field) {
    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
  }

  public static boolean equalsValues(Object object1, Object object2, String...fields) {
    List<Object> list1 = new ArrayList<>();
    List<Object> list2 = new ArrayList<>();

    for(String field : fields) {
      list1.add(getValue(object1, field));
      list2.add(getValue(object2, field));
    }

    Object value1 = null;
    Object value2 = null;

    for(int i = 0; i < list1.size(); i++) {
      value1 = list1.get(i);
      value2 = list2.get(i);

      if(!Objects.equals(value1, value2)) {
        return false;
      }
    }

    return true;
  }
}

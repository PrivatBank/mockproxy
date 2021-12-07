package com.pb.mockproxy.repository.util;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class UpdateUtil {

    public Update createUpdateFromObject(Object object) {
        Update update = new Update();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                String name = field.getName();
                Object value = field.get(object);
                if (value != null && !"id".equals(name)) {
                    update.set(name, value);
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        return update;
    }
}

/*
 *   Copyright 2016 Fabio Collini.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package it.codingjam.lifecyclebinder.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class TypeUtils {
    public static boolean isRawTypeEquals(TypeMirror type1, TypeMirror type2) {
        return getRawType(type1).equals(getRawType(type2));
    }

    public static ClassName getRawType(TypeMirror type) {
        TypeName typeName = TypeName.get(type);
        return getRawType(typeName);
    }

    public static ClassName getRawType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) typeName).rawType;
        } else {
            return (ClassName) typeName;
        }
    }

    public static List<TypeName> getTypeArguments(TypeMirror mirror) {
        TypeName typeName = ParameterizedTypeName.get(mirror);
        return getTypeArguments(typeName);
    }

    public static List<TypeName> getTypeArguments(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) typeName).typeArguments;
        } else {
            return Collections.emptyList();
        }
    }

    public static boolean isAssignable(Elements elements, TypeName t1, TypeName t2) {
        TypeElement e1 = elements.getTypeElement(getRawType(t1).toString());
        List<? extends TypeMirror> interfaces = e1.getInterfaces();
        for (TypeMirror anInterface : interfaces) {
            if (getRawType(anInterface).equals(getRawType(t2))) {
                return true;
            }
        }
        TypeMirror superclass = e1.getSuperclass();
        if (TypeName.get(superclass).equals(TypeName.get(Object.class))) {
            return false;
        } else {
            return isAssignable(elements, getRawType(superclass), t2);
        }
    }
}

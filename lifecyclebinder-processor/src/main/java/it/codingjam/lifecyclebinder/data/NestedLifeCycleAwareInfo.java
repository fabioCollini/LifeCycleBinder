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

package it.codingjam.lifecyclebinder.data;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import it.codingjam.lifecyclebinder.BinderGenerator;

public class NestedLifeCycleAwareInfo {

    public final Element field;

    public final RetainedObjectInfo retained;
    private final String fieldName;
    private final String bindMethodParameter;
    private final TypeName binderClassName;

    private NestedLifeCycleAwareInfo(Element field, RetainedObjectInfo retained, String fieldName, String bindMethodParameter, TypeName targetClassName) {
        this.field = field;
        this.retained = retained;
        this.fieldName = fieldName;
        this.bindMethodParameter = bindMethodParameter;
        List<TypeName> typeArguments = it.codingjam.lifecyclebinder.utils.TypeUtils.getTypeArguments(targetClassName);
        if (typeArguments.isEmpty()) {
            this.binderClassName = ClassName.bestGuess(targetClassName + BinderGenerator.LIFE_CYCLE_BINDER_SUFFIX);
        } else {
            this.binderClassName = ParameterizedTypeName.get(
                    ClassName.bestGuess(it.codingjam.lifecyclebinder.utils.TypeUtils.getRawType(targetClassName) + BinderGenerator.LIFE_CYCLE_BINDER_SUFFIX),
                    typeArguments.toArray(new TypeName[typeArguments.size()]));
        }
    }

    public static NestedLifeCycleAwareInfo createNestedLifeCycleAwareInfo(Element field) {
        return new NestedLifeCycleAwareInfo(field, null, field.getSimpleName().toString(), "view." + field.getSimpleName().toString(), TypeName.get(field.asType()));
    }

    public static NestedLifeCycleAwareInfo createRetainedObject(Element field, RetainedObjectInfo retained) {
        return new NestedLifeCycleAwareInfo(field, retained, field.getSimpleName().toString(), "view." + field.getSimpleName().toString(), retained.typeName);
    }

    public static NestedLifeCycleAwareInfo createSuperclass(TypeElement field) {
        return new NestedLifeCycleAwareInfo(field, null, "superClass$lifeCycleBinder", "view", TypeName.get(field.getSuperclass()));
    }

    public TypeName getBinderClassName() {
        return binderClassName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getBindMethodParameter() {
        return bindMethodParameter;
    }
}

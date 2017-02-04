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
import com.squareup.javapoet.TypeName;
import it.codingjam.lifecyclebinder.BinderGenerator;
import it.codingjam.lifecyclebinder.utils.TypeUtils;
import java.util.List;
import javax.lang.model.element.Element;

public class NestedLifeCycleAwareInfo {

    public final RetainedObjectInfo retained;
    private final String fieldName;
    private final TypeName binderClassName;

    private NestedLifeCycleAwareInfo(RetainedObjectInfo retained, String fieldName, TypeName targetClassName) {
        this.retained = retained;
        this.fieldName = fieldName;
        binderClassName = createBinderClassName(targetClassName);
    }

    public static ClassName createBinderClassName(TypeName targetClassName) {
        List<TypeName> typeArguments = TypeUtils.getTypeArguments(targetClassName);
        if (typeArguments.isEmpty()) {
            return ClassName.bestGuess(targetClassName + BinderGenerator.LIFE_CYCLE_BINDER_SUFFIX);
        } else {
            return ClassName.bestGuess(TypeUtils.getRawType(targetClassName) + BinderGenerator.LIFE_CYCLE_BINDER_SUFFIX);
        }
    }

    public static NestedLifeCycleAwareInfo createNestedLifeCycleAwareInfo(Element field) {
        return new NestedLifeCycleAwareInfo(null, field.getSimpleName().toString(), TypeName.get(field.asType()));
    }

    public static NestedLifeCycleAwareInfo createRetainedObject(Element field, RetainedObjectInfo retained) {
        return new NestedLifeCycleAwareInfo(retained, field.getSimpleName().toString(), retained.typeName);
    }

    public TypeName getBinderClassName() {
        return binderClassName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

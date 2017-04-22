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

import javax.lang.model.element.VariableElement;

import it.codingjam.lifecyclebinder.RetainedObjectProvider;
import it.codingjam.lifecyclebinder.utils.TypeUtils;

public class RetainedObjectInfo {
    public final String name;

    public final VariableElement field;

    public final TypeName typeName;

    public final TypeName binderClassName;

    public final String fieldToPopulate;

    public RetainedObjectInfo(VariableElement field) {
        this.name = field.getSimpleName().toString();
        this.field = field;
        this.typeName = TypeUtils.getTypeArguments(field.asType()).get(0);
        TypeName rawType;
        if (typeName instanceof ParameterizedTypeName) {
            rawType = ((ParameterizedTypeName) typeName).rawType;
        } else {
            rawType = typeName;
        }
        this.binderClassName = ClassName.bestGuess(rawType.toString() + "$LifeCycleBinder");
        this.fieldToPopulate = field.getAnnotation(RetainedObjectProvider.class).value();
    }
}

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

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

public class LifeCycleAwareInfo {
    public final TypeElement element;

    public final List<VariableElement> lifeCycleAwareElements = new ArrayList<>();

    public final List<ExecutableElement> eventsElements = new ArrayList<>();

    public final List<NestedLifeCycleAwareInfo> nestedElements = new ArrayList<>();

    public final List<RetainedObjectInfo> retainedObjects = new ArrayList<>();

    public LifeCycleAwareInfo(TypeElement element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return "LifeCycleAwareInfo{" +
                "element=" + element +
                ", lifeCycleAwareElements=" + lifeCycleAwareElements +
                ", nestedElements=" + nestedElements +
                ", retainedObjects=" + retainedObjects +
                '}';
    }

    public Element[] getLifeCycleAwareElementsArray() {
        return lifeCycleAwareElements.toArray(new Element[lifeCycleAwareElements.size()]);
    }

    public boolean isNested(String name) {
        for (NestedLifeCycleAwareInfo nestedElement : nestedElements) {
            if (nestedElement.getFieldName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsField(String field, Types typeUtils) {
        TypeElement currentElement = this.element;
        while (currentElement != null) {
            List<? extends Element> enclosedElements = currentElement.getEnclosedElements();
            for (Element e : enclosedElements) {
                if (e.getKind() == ElementKind.FIELD && e.getSimpleName().toString().equals(field)) {
                    return true;
                }
            }
            TypeMirror superclass = currentElement.getSuperclass();
            if (!superclass.toString().equals("java.lang.Object")) {
                currentElement = (TypeElement) typeUtils.asElement(superclass);
            } else {
                return false;
            }
        }
        return false;
    }
}

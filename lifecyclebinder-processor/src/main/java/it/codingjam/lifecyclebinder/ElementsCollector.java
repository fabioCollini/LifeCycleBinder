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

package it.codingjam.lifecyclebinder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import it.codingjam.lifecyclebinder.data.LifeCycleAwareInfo;
import it.codingjam.lifecyclebinder.data.NestedLifeCycleAwareInfo;
import it.codingjam.lifecyclebinder.data.RetainedObjectInfo;
import it.codingjam.lifecyclebinder.utils.TypeUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class ElementsCollector {

    private Messager messager;
    private Types types;
    private Elements elements;

    public static final TypeName LIFE_CYCLE_AWARE_TYPE = TypeName.get(LifeCycleAware.class);

    public ElementsCollector(Messager messager, Types types, Elements elements) {
        this.messager = messager;
        this.types = types;
        this.elements = elements;
    }

    List<LifeCycleAwareInfo> createLifeCycleAwareElements(Set<? extends Element> lifeCycleAwareElements, Set<? extends Element> retainedObjectElements,
            Set<? extends Element> eventsElements) {
        Map<Element, LifeCycleAwareInfo> elementsByClass = new HashMap<>();

        for (Element element : lifeCycleAwareElements) {
            //checkElementTypeIsLifeCycleAware(element);
            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, element);
            info.lifeCycleAwareElements.add((VariableElement) element);
        }

        for (Element element : retainedObjectElements) {
            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, element);
            info.retainedObjects.add(new RetainedObjectInfo((VariableElement) element));
        }

        for (Element element : eventsElements) {
            LifeCycleAwareInfo info = getLifeCycleAwareInfo(elementsByClass, element);
            info.eventsElements.add((ExecutableElement) element);
        }
        return new ArrayList<>(elementsByClass.values());
    }

    private void checkElementTypeIsLifeCycleAware(Element element) {
        TypeName variableType = TypeName.get(element.asType());
        if (!TypeUtils.isAssignable(elements, variableType, LIFE_CYCLE_AWARE_TYPE)) {
            error(element, "Class %s is annotated with %s, it must implement %s",
                    variableType, BindLifeCycle.class.getSimpleName(), LifeCycleAware.class.getSimpleName());
        }
    }

    public void calculateNestedElements(List<LifeCycleAwareInfo> elementsByClass) {
        for (LifeCycleAwareInfo lifeCycleAwareInfo : elementsByClass) {
            for (Element element : lifeCycleAwareInfo.lifeCycleAwareElements) {
                for (LifeCycleAwareInfo entry : elementsByClass) {
                    if (entry.element.asType().equals(element.asType())) {
                        lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createNestedLifeCycleAwareInfo(element));
                    }
                }
            }
            TypeMirror superclass = lifeCycleAwareInfo.element.getSuperclass();
            for (LifeCycleAwareInfo entry : elementsByClass) {
                if (TypeUtils.isRawTypeEquals(superclass, entry.element.asType())) {
                    lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createSuperclass(lifeCycleAwareInfo.element));
                    break;
                }
            }

            for (RetainedObjectInfo retainedEntry : lifeCycleAwareInfo.retainedObjects) {
                for (LifeCycleAwareInfo entry : elementsByClass) {
                    if (ClassName.get(entry.element.asType()).equals(retainedEntry.typeName)) {
                        lifeCycleAwareInfo.nestedElements.add(NestedLifeCycleAwareInfo.createRetainedObject(retainedEntry.field, retainedEntry));
                    }
                }
            }
        }
    }

    private LifeCycleAwareInfo getLifeCycleAwareInfo(Map<Element, LifeCycleAwareInfo> elementsByClass, Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        LifeCycleAwareInfo info = elementsByClass.get(enclosingElement);
        if (info == null) {
            info = new LifeCycleAwareInfo(enclosingElement);
            elementsByClass.put(enclosingElement, info);
        }
        return info;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}

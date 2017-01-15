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

import it.codingjam.lifecyclebinder.data.LifeCycleAwareInfo;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@SupportedAnnotationTypes({
        "it.codingjam.lifecyclebinder.BindLifeCycle",
        "it.codingjam.lifecyclebinder.RetainedObjectProvider",
        "it.codingjam.lifecyclebinder.BindEvent"
})
public class LifeCycleBinderProcessor extends AbstractProcessor {

    private Types types;
    private Elements elements;
    private Filer filer;
    private Messager messager;

    private ElementsCollector elementsCollector;

    private BinderGenerator binderGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementsCollector = new ElementsCollector(messager, types, elements);
        binderGenerator = new BinderGenerator(processingEnv, types, messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<LifeCycleAwareInfo> elementsByClass = elementsCollector.createLifeCycleAwareElements(
                roundEnv.getElementsAnnotatedWith(BindLifeCycle.class),
                roundEnv.getElementsAnnotatedWith(RetainedObjectProvider.class),
                roundEnv.getElementsAnnotatedWith(BindEvent.class)
        );

        if (elementsByClass == null) {
            return true;
        }

        elementsCollector.calculateNestedElements(elementsByClass);

        for (LifeCycleAwareInfo entry : elementsByClass) {
            binderGenerator.generateBinder(entry);
        }
        return false;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

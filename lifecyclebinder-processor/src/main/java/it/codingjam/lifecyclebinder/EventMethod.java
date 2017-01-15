package it.codingjam.lifecyclebinder;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.util.EnumMap;

import static com.squareup.javapoet.ClassName.BOOLEAN;
import static com.squareup.javapoet.ClassName.INT;
import static com.squareup.javapoet.ClassName.bestGuess;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.ACTIVITY_RESULT;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.CREATE_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.DESTROY_VIEW;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.HAS_OPTION_MENU;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.OPTION_ITEM_SELECTED;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.PAUSE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.RESUME;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.SAVE_INSTANCE_STATE;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.START;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.STOP;
import static it.codingjam.lifecyclebinder.LifeCycleEvent.VIEW_CREATED;

public class EventMethod {
    public static final EnumMap<LifeCycleEvent, EventMethod> EVENTS = createMap();

    private static EnumMap<LifeCycleEvent, EventMethod> createMap() {
        EnumMap<LifeCycleEvent, EventMethod> ret = new EnumMap<>(LifeCycleEvent.class);

        ClassName bundle = bestGuess("android.os.Bundle");
        ClassName intent = bestGuess("android.content.Intent");
        ClassName menu = bestGuess("android.view.Menu");
        ClassName menuInflater = bestGuess("android.view.MenuInflater");
        ClassName menuItem = bestGuess("android.view.MenuItem");

        ret.put(CREATE, voidMethod("onCreate", bundle, intent, bundle));
        ret.put(START, voidMethod("onStart"));
        ret.put(RESUME, voidMethod("onResume"));
        ret.put(HAS_OPTION_MENU, booleanMethod("hasOptionsMenu"));
        ret.put(CREATE_OPTION_MENU, voidMethod("onCreateOptionsMenu", menu, menuInflater));
        ret.put(OPTION_ITEM_SELECTED, booleanMethod("onOptionsItemSelected", menuItem));
        ret.put(PAUSE, voidMethod("onPause"));
        ret.put(STOP, voidMethod("onStop"));
        ret.put(SAVE_INSTANCE_STATE, voidMethod("onSaveInstanceState", bundle));
        ret.put(DESTROY, voidMethod("onDestroy", BOOLEAN));
        ret.put(ACTIVITY_RESULT, voidMethod("onActivityResult", INT, INT, intent));
        ret.put(VIEW_CREATED, voidMethod("onViewCreated", bundle));
        ret.put(DESTROY_VIEW, voidMethod("onDestroyView"));

        return ret;
    }

    public final String name;

    public final TypeName returnType;

    public final TypeName[] parameterTypes;

    private EventMethod(String name, TypeName returnType, TypeName... parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    private static EventMethod voidMethod(String name, TypeName... parameterTypes) {
        return new EventMethod(name, null, parameterTypes);
    }

    private static EventMethod booleanMethod(String name, TypeName... parameterTypes) {
        return new EventMethod(name, ClassName.BOOLEAN, parameterTypes);
    }
}

# LifeCycleBinder

[![](https://jitpack.io/v/fabioCollini/LifeCycleBinder.svg)](https://jitpack.io/#fabioCollini/LifeCycleBinder)

Using LifeCycleBinder you can create Java classes connected to the lifecycle of an Activity or a Fragment.

The usage is simple, you just need to create a class that implements `LifeCycleAware` and modify your Activity/Fragment 
adding a field annotated with `@BindLifeCycle` and invoking `LifeCycleBinder.bind(this)` in onCreate method.

If you want to create an object that survives on configuration changes you can annotate 
a `Callable` or a `Provider` field using `@RetainedObjectProvider` annotation.

## Usage

In this example we are using a field annotated with `@BindLifeCycle`. We don't need to extend
a custom base class, we just invoke the `LifeCycleBinder.bind` static method:


```java
public class MyActivity extends AppCompatActivity {
    @BindLifeCycle
    MyLifeCycleAware myLifeCycleAware = new MyLifeCycleAware();

    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);

        LifeCycleBinder.bind(this);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
```

The `MyLifeCycleAware` class must implement `LifeCycleAware` interface, the class 
`DefaultLifeCycleAware` can be used to override only some methods:

```java
public class MyLifeCycleAware extends DefaultLifeCycleAware<MyActivity> {
    @Override
    public void onCreate(MyActivity view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        view.setText("Hello world!");
    }
}
```

The first parameter of `LifeCycleAware` methods is always the
Activity or the Fragment. An interface or a base class can be used to avoid
dependency to Android classes.
The other parameters are usually the same parameters of the corresponding Activity/Fragment method,
there are two exceptions:

- `onCreate` method contains the Activity intent and a Bundle (it can be
 the Activity intent bundle or the Fragment arguments);
- `onDestroy` method contains a changingConfigurations parameters.

Using LifeCycleBinder it's easy to manage retained objects, we 
can create an Activity with a `Callable` field annotated with `@RetainedObjectProvider`: 

```java
@RetainedObjectProvider
Callable<MyRetainedLifeCycleAware> myFactory = () -> new MyRetainedLifeCycleAware();
```

In this example a `MyRetainedLifeCycleAware` object is created on the first start, all
the callbacks are invoked on this object. If the Activity/Fragment is destroyed
for a configuration change the object is retained.
In case we need to use this object directly we can create a field and add
the field name as parameter to the `@RetainedObjectProvider` annotation:

```java
MyRetainedLifeCycleAware myRetainedLifeCycleAware;

@RetainedObjectProvider("myRetainedLifeCycleAware")
Callable<MyRetainedLifeCycleAware> myFactory = () -> new MyRetainedLifeCycleAware();
```

`LifeCycleAware` interface contains the methods to manage option menu:

- boolean hasOptionsMenu(T view)
- void onCreateOptionsMenu(T view, Menu menu, MenuInflater inflater)
- boolean onOptionsItemSelected(T view, MenuItem item)

Activity navigation can be managed using LifeCycleBinder, `LifeCycleAware` interface contains 
`onActivityResult` method that it's invoked when an Activity returns.
`LifeCycleBinder` class contains a static method `startActivityForResult`, you need to use
this method passing the Activity/Fragment to receive the callback.

## How does it work under the hood?

LifeCycleBinder creates a new Fragment with no user interface and adds it to the Activity or the Fragment.
This new Fragment manages the lifecycle and invokes the methods on the registered objects.
Annotated objects are collected using an annotation processor.

Retained objects are managed using a Loader associated to the Fragment.

LifeCycleBinder depends on support-v4 v24, it uses the new method `commitNow`
to dynamically add the Fragment.

## Dagger support

LifeCycleBinder can be easily used on objects managed by Dagger 2. `@BindLifeCycle`
works on fields populated using `@Inject` annotation, `@RetainedObjectProvider`
can be used on `Provider` fields. The previous examples can be rewritten using Dagger:

```java
@Inject
@BindLifeCycle
MyLifeCycleAware myLifeCycleAware;

MyRetainedLifeCycleAware myRetainedLifeCycleAware;

@Inject
@RetainedObjectProvider("myRetainedLifeCycleAware")
Provider<MyRetainedLifeCycleAware> myFactory;
```

## JitPack configuration

LifeCycleBinder is available on [JitPack](https://jitpack.io/#fabioCollini/LifeCycleBinder),
add the JitPack repository in your build.gradle (in top level dir):

```gradle
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
```

and the dependency in the build.gradle of the module:

```gradle

buildscript {
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    apt 'com.github.fabioCollini.lifecyclebinder:lifecyclebinder-processor:0.3.2'
    compile 'com.github.fabioCollini.lifecyclebinder:lifecyclebinder-lib:0.3.2'
}
```


## License

    Copyright 2016 Fabio Collini

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
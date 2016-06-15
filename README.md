# LifeCycleBinder

Move code to standard testable Java classes

[![](https://jitpack.io/v/fabioCollini/LifeCycleBinder.svg)](https://jitpack.io/#fabioCollini/LifeCycleBinder)

Using LifeCycleBinder you can create Java classes connected to the lifecycle of an Activity or a Fragment.

The usage is simple, you just need to create a class that implements `LifeCycleAware`, add a field annotated with `@BindLifeCycle`
and invoke `LifeCycleBinder.bind(this)` in onCreate method.

If you want to create an object that survives on orientation change you can use `@RetainedObjectProvider`
on a Callable or a Provider field.

## Usage

In this example we are using a field annotated with `@BindLifeCycle`; we don't need to extend
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

The `MyLifeCycleAware` must implement `LifeCycleAware` interface, the class 
`DefaultLifeCycleAware` can be used to override only some methods:

```java
public class MyLifeCycleAware extends DefaultLifeCycleAware<MyActivity> {
    @Override
    public void onCreate(MyActivity view, Bundle savedInstanceState, Intent intent, Bundle arguments) {
        view.setText("Hello world!");
    }
}
```

The first parameter of the methods of LifeCycleAware interface is always the
Activity or the Fragment, an interface or a base class can be used.
The other parameters are usually the same parameters of the corresponding Activity/Fragment method,
there are two exceptions:

- `onCreate` method contains the Activity intent and a Bundle that contains
 the Activity intent bundle or the Fragment arguments;
- `onDestroy` method contains a changingConfigurations parameters.

Using LifeCycleBinder it's easy to manage retained objects, for example we 
can create an Activity with a Callable field annotated with `@RetainedObjectProvider`: 

```java
@RetainedObjectProvider
Callable<MyRetainedLifeCycleAware> myFactory = () -> new MyRetainedLifeCycleAware();
```

In this example a `MyRetainedLifeCycleAware` object is created on the first start, all
the callbacks are invoked on this object. If the Activity/Fragment is destroyed
for a configuration change the object is not destroyed.
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

<!--
onActivityResult

## Why another lib?

There are other library available similar to LifeCycleBinder (for example
[LighCycle](https://github.com/soundcloud/lightcycle) and [Navi](https://github.com/trello/navi)).
Using other libraries you need to LifeCycleBinder... 

-->

## How does it work under the hood?

LifeCycleBinder creates a new Fragment with no user interface and adds it to the Activity or the Fragment.
This new Fragment manages the lifecycle and invokes the methods on the registered objects.
Annotated objects are collected using an annotation processor.

Retained objects are managed using a Loader associated to the Fragment.

LifeCycleBinder depends on support-v4 v24, it uses the new method commitNow
to dynamically add the Fragment.

## Dagger support

LifeCycleBinder can be easily used together with Dagger 2. `@BindLifeCycle`
can be used on field populated using `@Inject` annotation. `@RetainedObjectProvider`
can be used on `Provider` fields.

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
    apt 'com.github.fabioCollini.lifecyclebinder:lifecyclebinder-processor:0.3'
    compile 'com.github.fabioCollini.lifecyclebinder:lifecyclebinder-lib:0.3'
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
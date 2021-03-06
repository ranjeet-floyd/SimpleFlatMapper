package org.sfm.reflect.meta;

import org.sfm.reflect.Getter;
import org.sfm.reflect.ReflectionService;
import org.sfm.reflect.Setter;

import java.lang.reflect.Type;


public abstract class PropertyMeta<O, P> {
	private final String name;

	protected final ReflectionService reflectService;
	
	private volatile Setter<O, P> setter;
    private volatile Getter<O, P> getter;
	private volatile ClassMeta<P> classMeta;

	public PropertyMeta(String name, ReflectionService reflectService) {
		this.name = name;
		this.reflectService = reflectService;
	}

	public final Setter<O, P> getSetter() {
		Setter<O, P> lSetter = setter;
		if (lSetter == null) {
			lSetter = newSetter();
			setter = lSetter;
		}
		return lSetter;
	}

    public final Getter<O, P> getGetter() {
        Getter<O, P> lGetter = getter;
        if (lGetter == null) {
            lGetter = newGetter();
            getter = lGetter;
        }
        return lGetter;
    }

	protected abstract Setter<O, P> newSetter();
    protected abstract Getter<O, P> newGetter();

	public final String getName() {
		return name;
	}

	public abstract Type getType();

	public final ClassMeta<P> getClassMeta() {
		ClassMeta<P> meta = classMeta;
		if (meta == null) {
			meta = newClassMeta();
			classMeta = meta;
		}
		return meta;
	}

	protected ClassMeta<P> newClassMeta() {
		return reflectService.getClassMeta(getType());
	}

	public boolean isConstructorProperty() {
		return false;
	}

	public abstract String getPath();

	public boolean isSubProperty() {
		return false;
	}



}

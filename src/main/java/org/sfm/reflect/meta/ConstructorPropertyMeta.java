package org.sfm.reflect.meta;

import org.sfm.reflect.*;
import org.sfm.reflect.impl.NullGetter;
import org.sfm.reflect.impl.NullSetter;

import java.lang.reflect.Type;

public class ConstructorPropertyMeta<T, P> extends PropertyMeta<T, P> {

    private final Class<T> owner;

    public ConstructorPropertyMeta(String name,
                                   ReflectionService reflectService,
                                   Parameter parameter,
                                   Class<T> owner) {
		super(name, reflectService);
		this.parameter = parameter;
        this.owner = owner;
	}

	private final Parameter parameter;
	
	@Override
	protected Setter<T, P> newSetter() {
		return new NullSetter<T, P>();
	}

    @Override
    protected Getter<T, P> newGetter() {
        final Getter<T, P> getter = reflectService.getObjectGetterFactory().getGetter(owner, getName());
        if (getter == null) {
            return new NullGetter<T, P>();
        }
        return getter;
    }

    @Override
	public Type getType() {
		return parameter.getGenericType();
	}

	public Parameter getParameter() {
		return parameter;
	}
	
	public boolean isConstructorProperty() {
		return true;
	}

	@Override
	public String getPath() {
		return getName();
	}

    @Override
    public String toString() {
        return "ConstructorPropertyMeta{" +
                "owner=" + owner +
                ", constructorParameter=" + parameter +
                '}';
    }
}

package org.sfm.csv.impl.primitive;

import org.sfm.csv.impl.CsvMapperCellHandler;
import org.sfm.reflect.Getter;
import org.sfm.reflect.primitive.FloatGetter;

public class FloatDelayedGetter<T> implements FloatGetter<CsvMapperCellHandler<T>>, Getter<CsvMapperCellHandler<T>, Float> {
	private final int index;
	
	public FloatDelayedGetter(int index) {
		this.index = index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public float getFloat(CsvMapperCellHandler<T> target) throws Exception {
		return ((FloatDelayedCellSetter<T>)target.getDelayedCellSetter(index)).consumeFloat();
	}

	@Override
	public Float get(CsvMapperCellHandler<T> target) throws Exception {
		return getFloat(target);
	}

    @Override
    public String toString() {
        return "FloatDelayedGetter{" +
                "index=" + index +
                '}';
    }
}

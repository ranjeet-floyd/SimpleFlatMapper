package org.sfm.jdbc.impl.getter;

import org.sfm.reflect.EnumHelper;
import org.sfm.reflect.Getter;

import java.sql.ResultSet;
import java.util.Arrays;

public final class OrdinalEnumResultSetGetter<E extends Enum<E>> implements  Getter<ResultSet, E> {

	private final int columnIndex;
	private final E[] values;
	
	public OrdinalEnumResultSetGetter(final int column, final Class<E> enumType)  {
		this.columnIndex = column;
		this.values = EnumHelper.getValues(enumType);
	}

	@Override
	public E get(final ResultSet target) throws Exception {
		return values[target.getInt(columnIndex)];
	}

    @Override
    public String toString() {
        return "OrdinalEnumResultSetGetter{" +
                "columnIndex=" + columnIndex +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}

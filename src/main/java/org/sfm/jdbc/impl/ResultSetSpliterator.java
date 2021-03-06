package org.sfm.jdbc.impl;

import org.sfm.jdbc.JdbcMapper;
import org.sfm.map.MappingContext;
import org.sfm.utils.ErrorHelper;
import org.sfm.utils.RowHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ResultSetSpliterator<T> implements Spliterator<T> {
    private final ResultSet resultSet;
    private final JdbcMapper<T> mapper;
    private final MappingContext<ResultSet> mappingContext;

    public ResultSetSpliterator(ResultSet resultSet, JdbcMapper<T> mapper, MappingContext<ResultSet> mappingContext) {
        this.resultSet = resultSet;
        this.mapper = mapper;
        this.mappingContext = mappingContext;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        try {
            if (resultSet.next()) {
                action.accept(mapper.map(resultSet, mappingContext));
                return true;
            }
        } catch (SQLException e) {
            ErrorHelper.rethrow(e);
        }
        return false;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        try {
            mapper.forEach(resultSet, new RowHandler<T>() {
                @Override
                public void handle(T t) throws Exception {
                    action.accept(t);
                }
            });
        } catch (SQLException e) {
            ErrorHelper.rethrow(e);
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return Spliterator.ORDERED | Spliterator.NONNULL;
    }
}

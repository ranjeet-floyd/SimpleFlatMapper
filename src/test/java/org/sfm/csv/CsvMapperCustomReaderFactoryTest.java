package org.sfm.csv;


import org.junit.Test;
import org.sfm.beans.DbObject;
import org.sfm.csv.impl.ParsingContext;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class CsvMapperCustomReaderFactoryTest {

    @Test
    public void testCustomReaderFactory() throws IOException {
        CsvMapper<DbObject> mapper = CsvMapperFactory.newInstance().failOnAsm(true).cellValueReaderFactory(new CellValueReaderFactory() {
            @Override
            public <P> CellValueReader<P> getReader(Type propertyType, int index, CsvColumnDefinition columnDefinition, ParsingContextFactoryBuilder builder) {
                return new CellValueReader<P>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public P read(char[] chars, int offset, int length, ParsingContext parsingContext) {
                        return (P) "Hello!";
                    }
                };
            }
        }).newBuilder(DbObject.class).addMapping("name").mapper();

        DbObject bop = mapper.iterator(new StringReader("bop")).next();

        assertEquals("Hello!", bop.getName());


    }
}

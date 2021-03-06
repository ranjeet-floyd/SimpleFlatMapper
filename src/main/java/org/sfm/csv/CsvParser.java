package org.sfm.csv;

import org.sfm.csv.impl.CsvColumnDefinitionProviderImpl;
import org.sfm.csv.impl.DynamicCsvMapper;
import org.sfm.csv.parser.*;
import org.sfm.map.CaseInsensitiveFieldKeyNamePredicate;
import org.sfm.reflect.ReflectionService;
import org.sfm.reflect.TypeReference;
import org.sfm.reflect.meta.ClassMeta;
import org.sfm.tuples.*;
import org.sfm.utils.Predicate;
import org.sfm.utils.RowHandler;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END

/**
 * CsvParser provides an fluent DSL to parse or map csv content.<p>
 * It is possible to customize the quote char, the separator, skip lines,and specified the size of the buffer<br>
 * <br>
 * <code>
 *     CsvParser
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.quote('\'')
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.separator(';')
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.skip(2)
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.bufferSize(256)
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.stream(new StringReader("1;1\n2;2\n3;3\n4;4\n5;5"))
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.map(Arrays::toString)
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;.forEach(System.out::println);
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;// output
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;// [3, 3]
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;// [4, 4]
 *     <br>&nbsp;&nbsp;&nbsp;&nbsp;// [5, 5]
 * </code>
 * <br>
 * <br>
 *     the limit settings does not affect streams or iterator, only parse on DSL and forEach on the mapTo/mapWith DSL.
 * <p>
 * The DSL provides way to mapTo an object <br><br>
 * <code>
 *     CsvParser.mapTo(MyClass.class).stream(reader).forEach(System.out::println);<br>
 * </code>
 * <p>
 *  using static mapping when no headers<br><br>
 * <code>
 *     CsvParser.mapTo(MyClass.class).addHeaders("id", "field").stream(reader).forEach(System.out::println);<br>
 *     // using the addMapping<br>
 *     CsvParser.mapTo(MyClass.class).addMapping("id").addMapping("field").stream(reader).forEach(System.out::println);<br>
 * </code>
 * <p>
 *  using static mapping and ignoring source header<br><br>
 * <code>
 *     CsvParser.mapTo(MyClass.class).overrideHeaders("id", "field").stream(reader).forEach(System.out::println);<br>
 *     // using the addMapping<br>
 *     CsvParser.skip(1).mapTo(MyClass.class).addMapping("id").addMapping("field").stream(reader).forEach(System.out::println);<br>
 * </code>
 * <p>
 *  or mapping with a predefined mapper.<br><br>
 * <code>
 *     CsvMapper&lt;MyClass&gt; mapper = CsvMapperFactory.newInstance().newMapper(MyClass.class);<br>
 *     CsvParser.mapWith(mapper).stream(reader).forEach(System.out::println);<br>
 * </code>
 *
 * <p>
 *  Each call to the DSL return an immutable representation of the current setup. So that it is possible to cache step of the DSL without side effect.<br><br>
 * <code>
 *     CsvParser.DSL semiColumnParser = CsvParser.separator(';');<br>
 *     <br>
 *     try (Reader r = new FileReader(file)) {<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;// the limit does not modify to the semiColumnParser object<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;semiColumnParser.limit(3).stream(r);<br>
 *     }
 * </code>
 *
 *
 */
public final class CsvParser {

	/**
	 *
	 * @param c the separator char
	 * @return the DSL object
	 */
	public static DSL separator(char c) {
		return schema().separator(c);
	}

	public static DSL bufferSize(int size) {
		return schema().bufferSize(size);
	}

	public static DSL quote(char c) {
		return schema().quote(c);
	}

	public static DSL skip(int skip) {
		return schema().skip(skip);
	}

	private static DSL schema() {
		return new DSL();
	}

	public static DSL limit(int limit) {
		return schema().limit(limit);
	}

	public static <T> MapToDSL<T> mapTo(Type type) {
		return schema().mapTo(type);
	}

	public static <T> MapToDSL<T> mapTo(Class<T> type) {
		return schema().mapTo(type);
	}

    public static <T> MapToDSL<T> mapTo(TypeReference<T> type) {
        return schema().mapTo(type);
    }

    public static <T1, T2> MapToDSL<Tuple2<T1, T2>> mapTo(Class<T1> class1, Class<T2> class2) {
		return  schema().mapTo(class1, class2);
	}

	public static <T1, T2, T3> MapToDSL<Tuple3<T1, T2, T3>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3) {
		return  schema().mapTo(class1, class2, class3);
	}

	public static <T1, T2, T3, T4> MapToDSL<Tuple4<T1, T2, T3, T4>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4) {
		return  schema().mapTo(class1, class2, class3, class4);
	}

	public static <T1, T2, T3, T4, T5> MapToDSL<Tuple5<T1, T2, T3, T4, T5>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5) {
		return  schema().mapTo(class1, class2, class3, class4, class5);
	}

    public static <T1, T2, T3, T4, T5, T6> MapToDSL<Tuple6<T1, T2, T3, T4, T5, T6>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6) {
        return  schema().mapTo(class1, class2, class3, class4, class5, class6);
    }

    public static <T1, T2, T3, T4, T5, T6, T7> MapToDSL<Tuple7<T1, T2, T3, T4, T5, T6, T7>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6, Class<T7> class7) {
        return  schema().mapTo(class1, class2, class3, class4, class5, class6, class7);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8> MapToDSL<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6, Class<T7> class7, Class<T8> class8) {
        return  schema().mapTo(class1, class2, class3, class4, class5, class6, class7, class8);
    }

    public static <T> MapWithDSL<T> mapWith(CsvMapper<T> mapper) {
		return schema().mapWith(mapper);
	}

	/**
	 * @param reader the reader
	 * @return a csv reader based on the default setup.
     * @throws java.io.IOException if an error occurs reading the data
	 */
	public static CsvReader reader(Reader reader) throws IOException {
		return schema().reader(reader);
	}

	@SuppressWarnings("deprecation")
    @Deprecated
	public static Iterator<String[]> iterate(Reader reader) throws IOException {
		return schema().iterate(reader);
	}

    @SuppressWarnings("deprecation")
	public static Iterator<String[]> iterator(Reader reader) throws IOException {
		return iterate(reader);
	}

	public static <CC extends CellConsumer> CC parse(Reader reader, CC cellConsumer) throws IOException {
		return schema().parse(reader, cellConsumer);
	}

	//IFJAVA8_START
	public static Stream<String[]> stream(Reader r) throws IOException {
		return schema().stream(r);
	}
	//IFJAVA8_END

    /**
     * DSL for csv parsing.
     * @see org.sfm.csv.CsvParser
     */
	public static final class DSL {
        private final char separatorChar;
        private final char quoteChar;
        private final int bufferSize;
        private final int skip;
        private final int limit;

		private DSL() {
			separatorChar = ',';
			quoteChar= '"';
			bufferSize = 8192;
			skip = 0;
			limit = -1;
		}

		public DSL(char separatorChar, char quoteChar, int bufferSize, int skip, int limit) {
			this.separatorChar = separatorChar;
			this.quoteChar = quoteChar;
			this.bufferSize = bufferSize;
			this.skip = skip;
			this.limit = limit;
		}

		/**
         * set the separator character. the default value is ','.
         * @param c the new separator character
         * @return this
         */
        public DSL separator(char c) {
			return new DSL(c, quoteChar, bufferSize, skip, limit);
        }

        /**
         * set the quote character. the default value is '"'.
         * @param c the quote character
         * @return this
         */
        public DSL quote(char c) {
			return new DSL(separatorChar, c, bufferSize, skip, limit);
        }

        /**
         * set the size of the char buffer to read from.
         * @param size the size in bytes
         * @return this
         */
        public DSL bufferSize(int size) {
			return new DSL(separatorChar, quoteChar, size, skip, limit);
        }

        /**
         * set the number of line to skip.
         * @param skip number of line to skip.
         * @return this
         */
        public DSL skip(int skip) {
			return new DSL(separatorChar, quoteChar, bufferSize, skip, limit);
        }

        /**
         * set the number of row to process. limit does not affect stream or iterator.
         * @param limit number of row to process
         * @return this
         */
        public DSL limit(int limit) {
			return new DSL(separatorChar, quoteChar, bufferSize, skip, limit);
        }

        /**
         * Parse the content from the reader as a csv and call back the cellConsumer with the cell values.
         * @param reader the reader
         * @param cellConsumer the callback object for each cell value
         * @param <CC> the type of the cell consumer
         * @return cellConsumer
         * @throws java.io.IOException if and error occurs in the reader
         */
        public <CC extends CellConsumer> CC parse(Reader reader, CC cellConsumer) throws IOException {
            CsvReader csvreader = reader(reader);

            if (limit == -1) {
                return csvreader.parseAll(cellConsumer);
            } else {
                return csvreader.parseRows(cellConsumer, limit);

            }
        }

        /**
         * Create a CsvReader and the specified reader. Will skip the number of specified rows.
         * @param reader the content
         * @return a CsvReader on the reader.
         * @throws java.io.IOException if an io error occurs
         */
        public CsvReader reader(Reader reader) throws IOException {
            CsvReader csvReader = new CsvReader(reader, charConsumer());
            csvReader.skipRows(skip);
            return csvReader;
        }

		@Deprecated
        public Iterator<String[]> iterate(Reader reader) throws IOException {
            return reader(reader).iterator();
        }

        @SuppressWarnings("deprecation")
		public Iterator<String[]> iterator(Reader reader) throws IOException {
			return iterate(reader);
		}

		public <T> MapToDSL<T> mapTo(Type target) {
			return new MapToDSL<T>(this, target);
		}

		public <T> MapToDSL<T> mapTo(Class<T> target) {
			return mapTo((Type)target);
		}

        public <T> MapToDSL<T> mapTo(TypeReference<T> target) {
            return mapTo(target.getType());
        }

		public <T1, T2> MapToDSL<Tuple2<T1, T2>> mapTo(Class<T1> class1, Class<T2> class2) {
			return new MapToDSL<Tuple2<T1, T2>>(this, Tuples.typeDef(class1, class2));
		}

		public <T1, T2, T3> MapToDSL<Tuple3<T1, T2, T3>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3) {
			return new MapToDSL<Tuple3<T1, T2, T3>>(this, Tuples.typeDef(class1, class2, class3));
		}

		public <T1, T2, T3, T4> MapToDSL<Tuple4<T1, T2, T3, T4>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4) {
			return new MapToDSL<Tuple4<T1, T2, T3, T4>>(this, Tuples.typeDef(class1, class2, class3, class4));
		}

		public <T1, T2, T3, T4, T5> MapToDSL<Tuple5<T1, T2, T3, T4, T5>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5) {
			return new MapToDSL<Tuple5<T1, T2, T3, T4, T5>>(this, Tuples.typeDef(class1, class2, class3, class4, class5));
		}

        public <T1, T2, T3, T4, T5, T6> MapToDSL<Tuple6<T1, T2, T3, T4, T5, T6>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6) {
            return new MapToDSL<Tuple6<T1, T2, T3, T4, T5, T6>>(this, Tuples.typeDef(class1, class2, class3, class4, class5, class6));
        }

        public <T1, T2, T3, T4, T5, T6, T7> MapToDSL<Tuple7<T1, T2, T3, T4, T5, T6, T7>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6, Class<T7> class7) {
            return new MapToDSL<Tuple7<T1, T2, T3, T4, T5, T6, T7>>(this, Tuples.typeDef(class1, class2, class3, class4, class5, class6, class7));
        }

        public <T1, T2, T3, T4, T5, T6, T7, T8> MapToDSL<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> mapTo(Class<T1> class1, Class<T2> class2, Class<T3> class3, Class<T4> class4, Class<T5> class5, Class<T6> class6, Class<T7> class7, Class<T8> class8) {
            return new MapToDSL<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>(this, Tuples.typeDef(class1, class2, class3, class4, class5, class6, class7, class8));
        }

        public <T> MapWithDSL<T> mapWith(CsvMapper<T> mapper) {
			return new MapWithDSL<T>(this, mapper);
		}

        //IFJAVA8_START
        public Stream<String[]> stream(Reader reader) throws IOException {
			return reader(reader).stream();
		}
        //IFJAVA8_END

        private CsvCharConsumer charConsumer() {
            CharBuffer charBuffer = new CharBuffer(bufferSize);

            if (separatorChar == ',' && quoteChar == '"') {
                return new StandardCsvCharConsumer(charBuffer);
            } else {
                return new ConfigurableCsvCharConsumer(charBuffer, separatorChar, quoteChar);
            }

        }


		public int bufferSize() {
			return bufferSize;
		}

		public int limit() {
			return limit;
		}

		public int skip() {
			return skip;
		}

		public char separator() {
			return separatorChar;
		}

		public char quote() {
			return quoteChar;
		}
	}

    /**
     * DSL for csv mapping to a dynamic mapper.
     * @see org.sfm.csv.CsvParser
     * @see org.sfm.csv.CsvMapper
     */
	public static final class MapToDSL<T> extends  MapWithDSL<T> {
		private final ClassMeta<T> classMeta;
		private final Type mapToClass;
		private final CsvColumnDefinitionProviderImpl columnDefinitionProvider;

		public MapToDSL(DSL dsl, Type mapToClass) {
			this(dsl, ReflectionService.newInstance().<T>getClassMeta(mapToClass), mapToClass, new CsvColumnDefinitionProviderImpl());
		}
		private MapToDSL(DSL dsl, ClassMeta<T> classMeta, Type mapToClass, CsvColumnDefinitionProviderImpl columnDefinitionProvider) {
			super(dsl, new DynamicCsvMapper<T>(mapToClass, classMeta, columnDefinitionProvider));
			this.mapToClass = mapToClass;
			this.classMeta = classMeta;
			this.columnDefinitionProvider = columnDefinitionProvider;
		}

		public StaticMapToDSL<T> headers(String... headers) {
			return new StaticMapToDSL<T>(getDsl(), classMeta, mapToClass, getColumnDefinitions(headers), columnDefinitionProvider);
		}

		public StaticMapToDSL<T> defaultHeaders() {
			return headers(classMeta.generateHeaders());
		}

		public StaticMapToDSL<T> overrideHeaders(String... headers) {
			List<Tuple2<String, CsvColumnDefinition>> columns = getColumnDefinitions(headers);
			return new StaticMapToDSL<T>(getDsl().skip(1), classMeta, mapToClass, columns, columnDefinitionProvider);
		}

		private List<Tuple2<String, CsvColumnDefinition>> getColumnDefinitions(String[] headers) {
			List<Tuple2<String,CsvColumnDefinition>> columns = new ArrayList<Tuple2<String, CsvColumnDefinition>>();
			for(String header : headers) {
				columns.add(new Tuple2<String, CsvColumnDefinition>(header, CsvColumnDefinition.IDENTITY));
			}
			return columns;
		}

		public MapToDSL<T> columnDefinition(String column, CsvColumnDefinition columnDefinition) {
			return columnDefinition(new CaseInsensitiveFieldKeyNamePredicate(column), columnDefinition);
		}

		public MapToDSL<T> columnDefinition(Predicate<? super CsvColumnKey> predicate, CsvColumnDefinition columnDefinition) {
			return new MapToDSL<T>(getDsl(), classMeta, mapToClass, newColumnDefinitionProvider(predicate, columnDefinition));
		}

        public MapWithDSL<T> addKeys(String... keys) {
            List<Tuple2<Predicate<? super CsvColumnKey>, CsvColumnDefinition>> definitions = columnDefinitionProvider.getDefinitions();

            for(String key : keys) {
                definitions.add(new Tuple2<Predicate<? super CsvColumnKey>, CsvColumnDefinition>(new CaseInsensitiveFieldKeyNamePredicate(key),
                        CsvColumnDefinition.key()));
            }

            return new MapToDSL<T>(getDsl(), classMeta, mapToClass, new CsvColumnDefinitionProviderImpl(definitions));
        }

        private CsvColumnDefinitionProviderImpl newColumnDefinitionProvider(Predicate<? super CsvColumnKey> predicate, CsvColumnDefinition columnDefinition) {
			List<Tuple2<Predicate<? super CsvColumnKey>, CsvColumnDefinition>> definitions = columnDefinitionProvider.getDefinitions();
			definitions.add(new Tuple2<Predicate<? super CsvColumnKey>, CsvColumnDefinition>(predicate, columnDefinition));
			return new CsvColumnDefinitionProviderImpl(definitions);
		}

		public StaticMapToDSL<T> overrideWithDefaultHeaders() {
			return overrideHeaders(classMeta.generateHeaders());
		}

		public StaticMapToDSL<T> addMapping(String column) {
			return staticMapper().addMapping(column);
		}

        public StaticMapToDSL<T> addKey(String key) {
            return staticMapper().addKey(key);
        }
		public StaticMapToDSL<T> addMapping(String column, CsvColumnDefinition columnDefinition) {
			return staticMapper().addMapping(column, columnDefinition);
		}

		private StaticMapToDSL<T> staticMapper() {
			return new StaticMapToDSL<T>(getDsl().skip(1), classMeta, mapToClass, Collections.<Tuple2<String,CsvColumnDefinition>>emptyList(), columnDefinitionProvider);
		}


    }

    /**
     * DSL for csv mapping to a static mapper.
     * @see org.sfm.csv.CsvParser
     * @see org.sfm.csv.CsvMapper
     */
	public static final class StaticMapToDSL<T> extends  MapWithDSL<T> {
		private final ClassMeta<T> classMeta;
		private final Type mapToClass;
		private final CsvColumnDefinitionProviderImpl columnDefinitionProvider;
		private final List<Tuple2<String, CsvColumnDefinition>> columns;


		private StaticMapToDSL(DSL dsl, ClassMeta<T> classMeta, Type mapToClass, List<Tuple2<String, CsvColumnDefinition>> columns, CsvColumnDefinitionProviderImpl columnDefinitionProvider) {
			super(dsl, newStaticMapper(mapToClass, classMeta, columns, columnDefinitionProvider));
			this.classMeta = classMeta;
			this.mapToClass = mapToClass;
			this.columns = columns;
			this.columnDefinitionProvider = columnDefinitionProvider;
		}

		private static <T> CsvMapper<T> newStaticMapper(Type mapToClass, ClassMeta<T> classMeta, List<Tuple2<String, CsvColumnDefinition>> columns, CsvColumnDefinitionProviderImpl columnDefinitionProvider) {
			CsvMapperBuilder<T> builder = new CsvMapperBuilder<T>(mapToClass, classMeta, columnDefinitionProvider);
			for(Tuple2<String, CsvColumnDefinition> col: columns) {
				builder.addMapping(col.first(), col.second());
			}
			return builder.mapper();
		}

		public StaticMapToDSL<T> addMapping(String column) {
			return addMapping(column, CsvColumnDefinition.IDENTITY);
		}

		public StaticMapToDSL<T> addMapping(String column, CsvColumnDefinition columnDefinition) {
			List<Tuple2<String, CsvColumnDefinition>> newColumns = new ArrayList<Tuple2<String, CsvColumnDefinition>>(columns);
			newColumns.add(new Tuple2<String, CsvColumnDefinition>(column, columnDefinition));
			return new StaticMapToDSL<T>(getDsl(), classMeta, mapToClass, newColumns, columnDefinitionProvider);
		}

        public StaticMapToDSL<T> addKey(String key) {
            return addMapping(key, CsvColumnDefinition.key());
        }
    }

    /**
     * DSL for csv mapping to a provided mapper.
     * @see org.sfm.csv.CsvParser
     * @see org.sfm.csv.CsvMapper
     */
    public static class MapWithDSL<T> {
		private final DSL dsl;
		private final CsvMapper<T> mapper;

		public MapWithDSL(DSL dsl, CsvMapper<T> mapper) {
			this.dsl = dsl;
			this.mapper = mapper;
		}

        protected final DSL getDsl() {
            return dsl;
        }

        protected final CsvMapper<T> getMapper() {
            return mapper;
        }

		@Deprecated
		public final Iterator<T> iterate(Reader reader) throws IOException {
			return mapper.iterator(dsl.reader(reader));
		}

        @SuppressWarnings("deprecation")
		public final Iterator<T> iterator(Reader reader) throws IOException {
			return iterate(reader);
		}

        public final <H extends RowHandler<T>> H forEach(Reader reader, H rowHandler) throws IOException {
            if (dsl.limit == -1) {
                mapper.forEach(dsl.reader(reader), rowHandler);
            } else {
                mapper.forEach(dsl.reader(reader), rowHandler, dsl.limit);
            }
            return rowHandler;
        }

		//IFJAVA8_START
		public final Stream<T> stream(Reader reader) throws IOException {
			return mapper.stream(dsl.reader(reader));
		}
		//IFJAVA8_END
	}


//    public static void main(String[] args) throws IOException {
//        CsvParser
//                .quote('\'')
//                .separator(';')
//                .skip(2)
//                .bufferSize(256)
//                .stream(new StringReader("1;1\n2;2\n3;3\n4;4\n5;5"))
//                .map(Arrays::toString)
//                .forEach(System.out::println);
//    }
}

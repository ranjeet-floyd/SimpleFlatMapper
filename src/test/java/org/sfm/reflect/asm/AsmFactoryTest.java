package org.sfm.reflect.asm;

import org.junit.Test;
import org.sfm.beans.DbFinalObject;
import org.sfm.beans.DbObject;
import org.sfm.beans.DbObject.Type;
import org.sfm.jdbc.JdbcColumnKey;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.JdbcMappingContextFactoryBuilder;
import org.sfm.jdbc.impl.getter.LongResultSetGetter;
import org.sfm.jdbc.impl.getter.OrdinalEnumResultSetGetter;
import org.sfm.jdbc.impl.getter.StringResultSetGetter;
import org.sfm.map.MappingContext;
import org.sfm.map.MappingException;
import org.sfm.map.FieldMapper;
import org.sfm.map.impl.RethrowRowHandlerErrorHandler;
import org.sfm.reflect.InstantiatorDefinition;
import org.sfm.reflect.Parameter;
import org.sfm.reflect.Getter;
import org.sfm.reflect.Instantiator;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsmFactoryTest {

	static AsmFactory asmFactory = new AsmFactory(Thread.currentThread().getContextClassLoader());
	
	@Test
	public void testCreateInstantiatorEmptyConstructor() throws Exception {
		Instantiator<ResultSet, DbObject> instantiator = asmFactory.createEmptyArgsInstantiator(ResultSet.class, DbObject.class);
		assertNotNull(instantiator.newInstance(null));
		assertSame(instantiator.getClass(), asmFactory.createEmptyArgsInstantiator(ResultSet.class, DbObject.class).getClass());
	}
	@Test
	public void testCreateInstantiatorFinalDbObjectInjectIdAndName() throws Exception {
		InstantiatorDefinition instantiatorDefinition = AsmInstantiatorDefinitionFactory.extractDefinitions(DbFinalObject.class).get(0);
		HashMap<Parameter, Getter<ResultSet, ?>> injections = new HashMap<Parameter, Getter<ResultSet, ?>>();
		injections.put(new Parameter("id", long.class), new LongResultSetGetter(1));
		injections.put(new Parameter("name", String.class), new StringResultSetGetter(2));
		Instantiator<ResultSet, DbFinalObject> instantiator = asmFactory.createInstantiator(ResultSet.class,
				instantiatorDefinition,
				injections
		);
		
		ResultSet rs= mock(ResultSet.class);
		when(rs.getLong(1)).thenReturn(33l);
		when(rs.getString(2)).thenReturn("fdo");
		
		
		DbFinalObject fdo = instantiator.newInstance(rs);
		
		assertNotNull(fdo);
		assertNull(fdo.getEmail());
		assertNull(fdo.getCreationTime());
		assertNull(fdo.getTypeName());
		assertNull(fdo.getTypeOrdinal());
		assertEquals(33l, fdo.getId());
		assertEquals("fdo", fdo.getName());


		assertSame(instantiator.getClass(), asmFactory.createInstantiator(ResultSet.class,
				instantiatorDefinition,
				injections
		).getClass());
	}
	
	@Test
	public void testCreateInstantiatorFinalDbObjectNameAndType() throws Exception {
		HashMap<Parameter, Getter<ResultSet, ?>> injections = new HashMap<Parameter, Getter<ResultSet, ?>>();
		injections.put(new Parameter("typeOrdinal", Type.class), new OrdinalEnumResultSetGetter<Type>(1, Type.class));
		injections.put(new Parameter("name", String.class), new StringResultSetGetter(2));

		Instantiator<ResultSet, DbFinalObject> instantiator = asmFactory.createInstantiator(ResultSet.class,
				AsmInstantiatorDefinitionFactory.<DbFinalObject>extractDefinitions(DbFinalObject.class).get(0),
				injections
		);
		
		ResultSet rs= mock(ResultSet.class);
		when(rs.getInt(1)).thenReturn(1);
		when(rs.getString(2)).thenReturn("fdo");
		
		
		DbFinalObject fdo = instantiator.newInstance(rs);
		
		assertNotNull(fdo);
		assertNull(fdo.getEmail());
		assertNull(fdo.getCreationTime());
		assertNull(fdo.getTypeName());
		assertEquals(0, fdo.getId());
		assertEquals("fdo", fdo.getName());
		assertEquals(Type.type2, fdo.getTypeOrdinal());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAsmJdbcMapperFailedInstantiator() throws Exception {
		JdbcMapper<DbObject> jdbcMapper = asmFactory.createJdbcMapper(new JdbcColumnKey[0],
				(FieldMapper<ResultSet, DbObject>[])new FieldMapper[] {},
                (FieldMapper<ResultSet, DbObject>[])new FieldMapper[] {},
				new Instantiator<ResultSet, DbObject>() {
					@Override
					public DbObject newInstance(ResultSet s) throws Exception {
						throw new IOException("Error");
					}
				}, 
				DbObject.class, new RethrowRowHandlerErrorHandler(), new JdbcMappingContextFactoryBuilder().newFactory());
		
		try {
			jdbcMapper.map(null);
		} catch(Exception e) {
            assertTrue(e instanceof IOException);
			// ok
		} 
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testAsmJdbcMapperFailedGetter() throws Exception {
		JdbcMapper<DbObject> jdbcMapper = asmFactory.createJdbcMapper(new JdbcColumnKey[0],
				(FieldMapper<ResultSet, DbObject>[])new FieldMapper[] {
						new FieldMapper<ResultSet, DbObject>() {
							@Override
							public void mapTo(ResultSet source, DbObject target, MappingContext<ResultSet> mappingContext)
									throws MappingException {
								throw new MappingException("Expected ");
							}
						}
				},
                (FieldMapper<ResultSet, DbObject>[])new FieldMapper[] {},
				new Instantiator<ResultSet, DbObject>() {
					@Override
					public DbObject newInstance(ResultSet s) throws Exception {
						return new DbObject();
					}
				}, 
				DbObject.class, new RethrowRowHandlerErrorHandler(), new JdbcMappingContextFactoryBuilder().newFactory());
		
		try {
			jdbcMapper.map(null);
		} catch(MappingException e) {
			// ok
		} 
	}
}

package org.sfm.jdbc;

import org.sfm.map.Mapper;
import org.sfm.map.MappingException;
import org.sfm.utils.RowHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
//IFJAVA8_START
import java.util.stream.Stream;
//IFJAVA8_END


/**
 * JdbcMapper will map from a {@link java.sql.ResultSet} to an object of the specified type T
 * <p>
 * JdbcMapper are instantiable via {@link org.sfm.jdbc.JdbcMapperFactory}.
 * <p>
 * <code>
 *     JdbcMapper&lt;MyClass&gt; mapper = JdbcMapperFactory.newInstance().newMapper(MyClass.class);<br>
 *         <br>
 *     ...<br>
 *         <br>
 *     try (ResultSet rs : ps.executeQuery()){<br>
 *     &nbsp;&nbsp;&nbsp;&nbsp;mapper.stream(rs).forEach(System.out::println);<br>
 *     }<br>
 * </code>
 *
 * @param <T> the type that the mapper is mapping to
 * @see org.sfm.jdbc.JdbcMapperFactory
 */
public interface JdbcMapper<T> extends Mapper<ResultSet, T> {
	
	/**
	 * Loop over the resultSet, map each row to a new instance of T and call back the handler
     *<p>
     * The method will return the handler passed as an argument so you can easily chain the calls like <br>
     * <code>
     *     List&lt;T&gt; list = mapper.forEach(rs, new ListHandler&lt;T&gt;()).getList();
     * </code>
     * <br>
     *
	 * @param rs the resultSet
	 * @param handle the handler that will get the callback
     * @param <H> the row handler type
	 * @return the handler passed in
	 * @throws SQLException if sql error occurs
	 * @throws MappingException if an error occurs during the mapping
     *
     */
	<H extends RowHandler<? super T>> H forEach(ResultSet rs, H handle) throws SQLException, MappingException;

	/**
	 * 
	 * @param rs the result set
	 * @return an iterator that will return a map object for each row of the result set.
     * @throws SQLException if sql error occurs
     * @throws MappingException if an error occurs during the mapping
	 */
	Iterator<T> iterator(ResultSet rs) throws SQLException, MappingException;

	@Deprecated
	Iterator<T> iterate(ResultSet rs) throws SQLException, MappingException;

	/**
	 * 
	 * @param rs the result set
	 * @return a stream that will contain a map object for each row of the result set.
     * @throws SQLException if sql error occurs
     * @throws MappingException if an error occurs during the mapping
	 */
	//IFJAVA8_START
	Stream<T> stream(ResultSet rs) throws SQLException, MappingException;
    //IFJAVA8_END
}

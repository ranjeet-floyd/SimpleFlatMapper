/**
 * {@link org.sfm.csv.CsvMapper} classes to map object from a csv file. It is instantiated using the {@link org.sfm.csv.CsvMapperFactory} 
 * <p>
 * <code>
 * CsvMapper&lt;MyObject&gt; mapper = CsvMapperFactory.newInstance().newMapper(MyObject.class);<br><br>
 * mapper.forEach(reader, (o) -&gt; writer.append(o.toString()).append("\n"));
 * </code>
 * </p>
 */
package org.sfm.csv;
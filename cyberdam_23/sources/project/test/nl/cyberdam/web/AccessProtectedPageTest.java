package nl.cyberdam.web;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


/*applicationContext (a ConfigurableApplicationContext): inherited from the AbstractSingleSpringContextTests superclass. Use this to perform explicit bean lookup or to test the state of the context as a whole.

jdbcTemplate: inherited from AbstractTransactionalDataSourceSpringContextTests. Useful for querying to confirm state. For example, you might query before and after testing application code that creates an object and persists it using an ORM tool, to verify that the data appears in the database. (Spring will ensure that the query runs in the scope of the same transaction.) You will need to tell your ORM tool to 'flush' its changes for this to work correctly, for example using the flush() method on Hibernate's Session interface.
*/

public class AccessProtectedPageTest extends AbstractTransactionalDataSourceSpringContextTests {

}

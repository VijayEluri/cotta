package net.sf.cotta.test;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class FixtureWrapperTest extends TestCase {
  public void testRunOnlyCalledOnce() throws Exception {
    Mockery context = new Mockery();
    final TestFixture fixture = context.mock(TestFixture.class);
    context.checking(new Expectations() {
      {
        one(fixture).setUp();
      }
    });
    FixtureWrapper wrapper = new FixtureWrapper(fixture);
    wrapper.setUp();
    wrapper.setUp();
    context.assertIsSatisfied();
  }

  public void testTearDownOnlyCalledOnce() throws Exception {
    Mockery context = new Mockery();
    final TestFixture fixture = context.mock(TestFixture.class);
    context.checking(new Expectations() {
      {
        one(fixture).tearDown();
      }
    });
    FixtureWrapper wrapper = new FixtureWrapper(fixture);
    wrapper.increaseCount();
    wrapper.increaseCount();
    wrapper.tearDown();
    wrapper.tearDown();
  }
}

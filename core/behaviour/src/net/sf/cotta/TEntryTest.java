package net.sf.cotta;

import net.sf.cotta.system.FileSystem;
import net.sf.cotta.test.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

import java.io.File;

public class TEntryTest extends TestCase {
  public void testAccessToFactory() {
    TFileFactory factory = new TFileFactory();
    TEntry entry = new TestEntry(factory, "/path", false);
    ensure.that(entry.factory()).sameAs(factory);
  }

  public void testToStringContainsEntryTypeAndPath() {
    TFileFactory factory = TFileFactory.inMemory();
    TEntry entry = new TestEntry(factory, "/path", false);
    ensure.that(entry.toString()).eq("TestEntry</path>");
  }

  public void testToStringDisplaysCurrentDirectoryIfPathIsRelative() {
    TFileFactory factory = TFileFactory.inMemory();
    TEntry entry = new TestEntry(factory, "./path", false);
    ensure.that(entry.toString()).eq("TestEntry<./path> relative to <memory://.>");
  }

  public void testHashCode() {
    TFileFactory factory = TFileFactory.physical();
    String path = "/test/one.txt";
    ensure.that(factory.file(path).hashCode()).eq(new File(path).hashCode());
  }

  public void testCompareByPath() {
    Mockery context = new Mockery();
    final FileSystem fileSystem = context.mock(FileSystem.class);
    context.checking(new Expectations() {
      {
        oneOf(fileSystem).compare(TPath.parse("one/two/a.txt"), TPath.parse("one/two/b.txt"));
        will(returnValue(-1));
      }
    });
    TFileFactory factory = new TFileFactory(fileSystem);
    TFile fileA = factory.file("one/two/a.txt");
    TFile fileB = factory.file("one/two/b.txt");
    ensure.that(fileA.compareTo(fileB)).eq(-1);
    context.assertIsSatisfied();
  }

  public void testTDirectorySortsBeforeTFile() {
    Mockery context = new Mockery();
    final FileSystem fileSystem = context.mock(FileSystem.class);
    TFileFactory factory = new TFileFactory(fileSystem);
    TFile file = factory.file("one/two/a.txt");
    TDirectory directory = factory.dir("one/two/a.txt");
    ensure.that(file.compareTo(directory)).eq(1);
    ensure.that(directory.compareTo(file)).eq(-1);
    context.assertIsSatisfied();
  }

  private static class TestEntry extends TEntry {
    private boolean exists;

    public TestEntry(TFileFactory factory, String path, boolean exists) {
      super(factory, TPath.parse(path));
      this.exists = exists;
    }

    public boolean exists() {
      return exists;
    }
  }
}

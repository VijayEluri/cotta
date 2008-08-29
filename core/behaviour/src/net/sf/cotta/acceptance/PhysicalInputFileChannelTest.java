package net.sf.cotta.acceptance;

import net.sf.cotta.FileSystem;
import net.sf.cotta.TFile;
import net.sf.cotta.io.InputManager;
import net.sf.cotta.io.InputProcessor;
import net.sf.cotta.memory.ByteArrayBuffer;
import net.sf.cotta.physical.PhysicalFileSystem;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PhysicalInputFileChannelTest extends InputFileChannelTestBase {
  protected FileSystem fileSystem() {
    return new PhysicalFileSystem();
  }

  public void shouldMapWithReadOption() throws Exception {
    TFile file = file();
    file.read(new InputProcessor() {
      public void process(InputManager inputManager) throws IOException {
        FileChannel channel = inputManager.channel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 10);
        ByteArrayBuffer target = new ByteArrayBuffer(10);
        int read = target.copyFrom(buffer);
        ensure().integer(read).eq(10);
        ensure().string(target.toString()).eq("1234567890");
        inputManager.clean(buffer);
        channel.close();
      }
    });
    file.delete();
  }
}

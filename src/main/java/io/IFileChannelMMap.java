package io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * memory map
 *   a way to share memory
 * */
public class IFileChannelMMap {
    public static void main(String[] args) {
        try {
            FileChannel fileChannel = new RandomAccessFile(
                    new File("src/main/resources/db.data"), "rw"
            ).getChannel();
            MappedByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
            while(fileChannel.read(buf) != -1){
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char)buf.get());
                }
                buf.compact();
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

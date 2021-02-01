package io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * file io with byteBuffer
 * faster than FileReader, is nio
 *
 * byteBuffer.flip() -> change buffer to read mode
 * byteBuffer.hasRemaining() -> if there has rest data
 * byteBuffer.compact() -> move position point
 *
 * fileChannel.force(true) will force write data on disk.
 * */
public class IFileChannel {
    public static void main(String[] args) {
        try {
            FileChannel fileChannel = new RandomAccessFile(
                    new File("src/main/resources/db.data"), "rw"
            ).getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
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

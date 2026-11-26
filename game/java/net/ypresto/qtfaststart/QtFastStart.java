package net.ypresto.qtfaststart;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/* loaded from: classes.dex */
public class QtFastStart {
    private static final int ATOM_PREAMBLE_SIZE = 8;
    public static boolean sDEBUG = false;
    private static final int FREE_ATOM = fourCcToInt(new byte[]{102, 114, 101, 101});
    private static final int JUNK_ATOM = fourCcToInt(new byte[]{106, 117, 110, 107});
    private static final int MDAT_ATOM = fourCcToInt(new byte[]{109, 100, 97, 116});
    private static final int MOOV_ATOM = fourCcToInt(new byte[]{109, 111, 111, 118});
    private static final int PNOT_ATOM = fourCcToInt(new byte[]{112, 110, 111, 116});
    private static final int SKIP_ATOM = fourCcToInt(new byte[]{115, 107, 105, 112});
    private static final int WIDE_ATOM = fourCcToInt(new byte[]{119, 105, 100, 101});
    private static final int PICT_ATOM = fourCcToInt(new byte[]{80, 73, 67, 84});
    private static final int FTYP_ATOM = fourCcToInt(new byte[]{102, 116, 121, 112});
    private static final int UUID_ATOM = fourCcToInt(new byte[]{117, 117, 105, 100});
    private static final int CMOV_ATOM = fourCcToInt(new byte[]{99, 109, 111, 118});
    private static final int STCO_ATOM = fourCcToInt(new byte[]{115, 116, 99, 111});
    private static final int CO64_ATOM = fourCcToInt(new byte[]{99, 111, 54, 52});

    public static void main(String[] args) {
        sDEBUG = true;
        if (args.length < 2) {
            printf("input file and output file is required.", new Object[0]);
            System.exit(1);
        }
        try {
            fastStart(new File(args[0]), new File(args[1]));
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                printe(e, "Failed to close file: ", new Object[0]);
            }
        }
    }

    static long uint32ToLong(int int32) {
        return int32 & 4294967295L;
    }

    static int uint32ToInt(int uint32) throws UnsupportedFileException {
        if (uint32 < 0) {
            throw new UnsupportedFileException("uint32 value is too large");
        }
        return uint32;
    }

    static int uint32ToInt(long uint32) throws UnsupportedFileException {
        if (uint32 > 2147483647L || uint32 < 0) {
            throw new UnsupportedFileException("uint32 value is too large");
        }
        return (int) uint32;
    }

    static long uint64ToLong(long uint64) throws UnsupportedFileException {
        if (uint64 < 0) {
            throw new UnsupportedFileException("uint64 value is too large");
        }
        return uint64;
    }

    private static int fourCcToInt(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private static void printf(String format, Object... args) {
        if (sDEBUG) {
            System.err.println("QtFastStart: " + String.format(format, args));
        }
    }

    private static void printe(Throwable e, String format, Object... args) {
        printf(format, args);
        if (sDEBUG) {
            e.printStackTrace();
        }
    }

    private static boolean readAndFill(FileChannel infile, ByteBuffer buffer) throws IOException {
        buffer.clear();
        int size = infile.read(buffer);
        buffer.flip();
        return size == buffer.capacity();
    }

    private static boolean readAndFill(FileChannel infile, ByteBuffer buffer, long position) throws IOException {
        buffer.clear();
        int size = infile.read(buffer, position);
        buffer.flip();
        return size == buffer.capacity();
    }

    public static boolean fastStart(File in, File out) throws IOException, MalformedFileException, UnsupportedFileException {
        FileChannel infile;
        FileOutputStream outStream;
        FileInputStream inStream = null;
        FileOutputStream outStream2 = null;
        try {
            FileInputStream inStream2 = new FileInputStream(in);
            try {
                infile = inStream2.getChannel();
                outStream = new FileOutputStream(out);
            } catch (Throwable th) {
                th = th;
                inStream = inStream2;
            }
            try {
                FileChannel outfile = outStream.getChannel();
                boolean ret = fastStartImpl(infile, outfile);
                safeClose(inStream2);
                safeClose(outStream);
                if (!ret) {
                    out.delete();
                }
                return ret;
            } catch (Throwable th2) {
                th = th2;
                outStream2 = outStream;
                inStream = inStream2;
                safeClose(inStream);
                safeClose(outStream2);
                if (0 == 0) {
                    out.delete();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x0048 A[EDGE_INSN: B:113:0x0048->B:34:0x0048 BREAK  A[LOOP:0: B:2:0x0012->B:114:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:114:? A[LOOP:0: B:2:0x0012->B:114:?, LOOP_END, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean fastStartImpl(java.nio.channels.FileChannel r28, java.nio.channels.FileChannel r29) throws java.io.IOException, net.ypresto.qtfaststart.QtFastStart.MalformedFileException, net.ypresto.qtfaststart.QtFastStart.UnsupportedFileException {
        /*
            Method dump skipped, instructions count: 643
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: net.ypresto.qtfaststart.QtFastStart.fastStartImpl(java.nio.channels.FileChannel, java.nio.channels.FileChannel):boolean");
    }

    public static class QtFastStartException extends Exception {
        private QtFastStartException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class MalformedFileException extends QtFastStartException {
        private MalformedFileException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class UnsupportedFileException extends QtFastStartException {
        private UnsupportedFileException(String detailMessage) {
            super(detailMessage);
        }
    }
}

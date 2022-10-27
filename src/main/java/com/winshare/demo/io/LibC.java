package com.winshare.demo.io;

import com.sun.jna.*;

public interface LibC extends Library {
    LibC INSTANCE = (LibC) Native.load(Platform.isWindows() ? "msvcrt" : "c", LibC.class);
    int MADV_WILLNEED = 3; // madvise 内存使用方式 LibC.INSTANCE.madvise(pointer, new NativeLong(1024 * 1024 * 64L), LibC.MADV_WILLNEED)
    int MADV_DONTNEED = 4;

    int MCL_CURRENT = 1;
    int MCL_FUTURE = 2;
    int MCL_ONFAULT = 4;
    /* sync memory asynchronously */
    int MS_ASYNC = 0x0001;
    /* invalidate mappings & caches */
    int MS_INVALIDATE = 0x0002;
    /* synchronous memory sync */
    int MS_SYNC = 0x0004;

    int mlock(Pointer var1, NativeLong var2);
    int munlock(Pointer var1, NativeLong var2);
    int madvise(Pointer var1, NativeLong var2, int var3);
    Pointer memset(Pointer p, int v, long len);
    int mlockall(int flags);
    int msync(Pointer p, NativeLong length, int flags);
}

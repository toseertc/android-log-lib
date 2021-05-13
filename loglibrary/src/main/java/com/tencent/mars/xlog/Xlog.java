package com.tencent.mars.xlog;




public class Xlog implements XLogWrapper.LogImp {

	public static final int LEVEL_ALL = 0;
	public static final int LEVEL_VERBOSE = 0;
	public static final int LEVEL_DEBUG = 1;
	public static final int LEVEL_INFO = 2;
	public static final int LEVEL_WARNING = 3;
	public static final int LEVEL_ERROR = 4;
	public static final int LEVEL_FATAL = 5;
	public static final int LEVEL_NONE = 6;

	public static final int COMPRESS_LEVEL1 = 1;
	public static final int COMPRESS_LEVEL2 = 2;
	public static final int COMPRESS_LEVEL3 = 3;
	public static final int COMPRESS_LEVEL4 = 4;
	public static final int COMPRESS_LEVEL5 = 5;
	public static final int COMPRESS_LEVEL6 = 6;
	public static final int COMPRESS_LEVEL7 = 7;
	public static final int COMPRESS_LEVEL8 = 8;
	public static final int COMPRESS_LEVEL9 = 9;

	public static final int AppednerModeAsync = 0;
	public static final int AppednerModeSync = 1;

	public static final int ZLIB_MODE = 0;
	public static final int ZSTD_MODE = 1;

	static class XLoggerInfo {
		public int level;
		public String tag;
		public String filename;
		public String funcname;
		public int line;
		public long pid;
		public long tid;
		public long maintid;
	}

	public static class XLogConfig {
		public int level = LEVEL_INFO;
		public int mode = AppednerModeAsync;
		public String logdir;
		public String nameprefix;
		public String pubkey = "";
		public int compressmode = ZLIB_MODE;
		public int compresslevel = 0;
		public String cachedir;
		public int cachedays = 0;


		public void setLevel(int level) {
			this.level = level;
		}

		public void setMode(int mode) {
			this.mode = mode;
		}

		public void setLogdir(String logdir) {
			this.logdir = logdir;
		}

		public void setNameprefix(String nameprefix) {
			this.nameprefix = nameprefix;
		}

		public void setPubkey(String pubkey) {
			this.pubkey = pubkey;
		}

		public void setCompressmode(int compressmode) {
			this.compressmode = compressmode;
		}

		public void setCompresslevel(int compresslevel) {
			this.compresslevel = compresslevel;
		}

		public void setCachedir(String cachedir) {
			this.cachedir = cachedir;
		}

		public void setCachedays(int cachedays) {
			this.cachedays = cachedays;
		}
	}

    public static void open(XLogConfig config) {
		appenderOpen(config);
	}

	private static String decryptTag(String tag) {
		return tag;
	}

	@Override
	public void logV(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_VERBOSE, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	@Override
	public void logD(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_DEBUG, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}

	@Override
	public void logI(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_INFO, decryptTag(tag), filename, funcname, line, pid, tid, maintid,  log);
	}

	@Override
	public void logW(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_WARNING, decryptTag(tag), filename, funcname, line, pid, tid, maintid,  log);
	}

	@Override
	public void logE(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_ERROR, decryptTag(tag), filename, funcname, line, pid, tid, maintid,  log);
	}

	@Override
	public void logF(long logInstancePtr, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log) {
		logWrite2(logInstancePtr, LEVEL_FATAL, decryptTag(tag), filename, funcname, line, pid, tid, maintid, log);
	}


	@Override
	public void appenderOpen(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {

		XLogConfig logConfig = new XLogConfig();
		logConfig.level = level;
		logConfig.mode = mode;
		logConfig.logdir = logDir;
		logConfig.nameprefix = nameprefix;
		logConfig.compressmode = ZLIB_MODE;
		logConfig.compresslevel = 1;
		logConfig.pubkey = "";
		logConfig.cachedir = cacheDir;
		logConfig.cachedays = cacheDays;

		appenderOpen(logConfig);
	}

	public static native void logWrite(XLoggerInfo logInfo, String log);

	public static void logWrite2(int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log){
		logWrite2(0, level, tag, filename ,funcname, line, pid, tid, maintid, log);
	}

	public static native void logWrite2(long logInstancePtr, int level, String tag, String filename, String funcname, int line, int pid, long tid, long maintid, String log);

	@Override
	public native int getLogLevel(long logInstancePtr);


	@Override
	public native void setAppenderMode(long logInstancePtr, int mode);

	@Override
	public long openLogInstance(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays) {
		XLogConfig logConfig = new XLogConfig();
		logConfig.level = level;
		logConfig.mode = mode;
		logConfig.logdir = logDir;
		logConfig.nameprefix = nameprefix;
		logConfig.compressmode = ZLIB_MODE;
		logConfig.pubkey = "";
		logConfig.cachedir = cacheDir;
		logConfig.cachedays = cacheDays;
		return newXlogInstance(logConfig);
	}

	@Override
	public native long getXlogInstance(String nameprefix);

	@Override
	public native void releaseXlogInstance(String nameprefix);

	public native long newXlogInstance(XLogConfig logConfig);

	@Override
	public native void setConsoleLogOpen(long logInstancePtr, boolean isOpen);	//set whether the console prints log

	private static native void appenderOpen(XLogConfig logConfig);

	@Override
	public native void appenderClose();

	@Override
	public native void appenderFlush(long logInstancePtr, boolean isSync);

	@Override
	public native void setMaxFileSize(long logInstancePtr, long size);

	@Override
	public native void setMaxAliveTime(long logInstancePtr, long aliveSeconds);

}
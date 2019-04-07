package nia.chapter13.my;

import java.net.InetSocketAddress;

/**
 * MyLogEvent
 *
 * @author pglc1026
 * @date 2019-04-07
 */
public class MyLogEvent {

    public static final byte SEPARATOR = (byte) ' ';

    private final InetSocketAddress source;

    private final String logFile;

    private final String msg;

    private final long received;

    /**
     * 接收端的构造
     *
     * @param source
     * @param logFile
     * @param msg
     * @param received
     */
    public MyLogEvent(InetSocketAddress source, String logFile, String msg, long received) {
        this.source = source;
        this.logFile = logFile;
        this.msg = msg;
        this.received = received;
    }

    /**
     * 发送端的构造
     *
     * @param logFile
     * @param msg
     */
    public MyLogEvent(String logFile, String msg) {
        this(null, logFile, msg, -1);
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceivedTimestamp() {
        return received;
    }
}

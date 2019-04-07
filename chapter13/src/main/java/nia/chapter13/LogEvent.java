package nia.chapter13;

import java.net.InetSocketAddress;

/**
 * LogEvent
 *
 * @author pglc1026
 * @date 2019-04-05
 */
public class LogEvent {

    public static final byte SEPORATOR = (byte) ':';

    private final InetSocketAddress source;

    private final String logFile;

    private final String msg;

    private final long received;


    /**
     * 用于传入消息的构造函数
     *
     * @param source
     * @param logFile
     * @param msg
     * @param received
     */
    public LogEvent(InetSocketAddress source, String logFile, String msg, long received) {
        this.source = source;
        this.logFile = logFile;
        this.msg = msg;
        this.received = received;
    }

    /**
     * 用于传出消息的构造函数
     *
     * @param logFile
     * @param msg
     * @param received
     */
    public LogEvent(String logFile, String msg) {
        this(null, logFile, msg, -1);
    }

    /**
     * @return 返回发送LogEvent的InetSocketAddress
     */
    public InetSocketAddress getSource() {
        return source;
    }

    /**
     * @return 返回发送LogEvent的日志文件的名称
     */
    public String getLogFile() {
        return logFile;
    }

    /**
     * @return 返回消息内容
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return 返回接收LogEvent的时间
     */
    public long getReceivedTimestamp() {
        return received;
    }
}

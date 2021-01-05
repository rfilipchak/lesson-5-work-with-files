package ua.mainacademy.model;

public class ConnectionInfo {
    private Integer id;
    private Long time;
    private String connectionIp;

    public ConnectionInfo() {
    }

    public ConnectionInfo(Integer id, Long time, String connectionIp) {
        this.id = id;
        this.time = time;
        this.connectionIp = connectionIp;
    }

    @Override
    public String toString() {
        return id + " " + time + " " + connectionIp;
    }

    public Long getTime() {
        return time;
    }
}

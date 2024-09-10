package mpp.Comunication;

import java.io.Serializable;

public class Request implements Serializable {

    private RequestType type;
    private Object data;

    public Request(RequestType type, Object data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public RequestType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}

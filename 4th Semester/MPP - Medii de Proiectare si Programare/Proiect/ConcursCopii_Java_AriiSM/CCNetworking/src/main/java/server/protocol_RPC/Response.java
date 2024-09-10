package server.protocol_RPC;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType type;
    private Object data;

    public Response(){};

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public ResponseType type(){
        return type;
    }

    public Object data(){
        return data;
    }

    private void type(ResponseType type){
        this.type=type;
    }

    private void data(Object data){
        this.data=data;
    }

    @Override
    public String toString(){
        return "Response{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }


    public static class Builder{
        private Response response=new Response();

        public Builder type(ResponseType type) {
            response.type(type);
            return this;
        }

        public Builder data(Object data) {
            response.data(data);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}

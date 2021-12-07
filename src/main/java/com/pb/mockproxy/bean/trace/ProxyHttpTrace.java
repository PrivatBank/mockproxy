package com.pb.mockproxy.bean.trace;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProxyHttpTrace {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime timestamp;
    private final Request request;
    private final Response response;
    private final Type type;

    public ProxyHttpTrace(Request request, Response response, Type type) {
        this.request = request;
        this.response = response;
        this.timestamp = LocalDateTime.now();
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public Type getType() {
        return type;
    }

    public static final class Request {
        private final String method;
        private final String uri;
        private final Map<String, List<String>> headers;
        private final String content;

        public Request(String method, String uri, Map<String, List<String>> headers, String content) {
            this.method = method;
            this.uri = uri;
            this.headers = headers;
            this.content = content;
        }

        public String getMethod() {
            return method;
        }

        public String getUri() {
            return uri;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public String getContent() {
            return content;
        }
    }

    public static final class Response {
        private final int status;
        private final Map<String, List<String>> headers;
        private String content;

        public Response(int status, Map<String, List<String>> headers, String content) {
            this.status = status;
            this.headers = headers;
            this.content = content;
        }

        public Response(int status, Map<String, List<String>> headers) {
            this.status = status;
            this.headers = headers;
        }

        public int getStatus() {
            return status;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public enum Type {

        ORIGIN("origin"),
        MOCK("mock");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }
}

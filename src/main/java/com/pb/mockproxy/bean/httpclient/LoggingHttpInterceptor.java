package com.pb.mockproxy.bean.httpclient;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.entity.GzipDecompressingEntity;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.apache.hc.core5.http.protocol.HttpProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class LoggingHttpInterceptor implements HttpProcessor {

    public static final String LOGGER_CONTEXT_ATTR = "logger";

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(LoggingHttpInterceptor.class);
    private static final List<String> TEXT_CONTENTS = Arrays.asList("text", "xml", "json", "x-www-form-urlencoded");
    private static final String CONTENT_LENGTH_TEMPLATE = "Length of the content: {0} bytes";
    private static final int MAX_BODY_LENGTH = 2500;

    private static final String REQUEST_URI_MDC_KEY = "REQUEST_URI";
    private static final String REQUEST_HOST_MDC_KEY = "REQUEST_HOST";
    private static final String REQUEST_PATH_MDC_KEY = "REQUEST_PATH";
    private static final String REQUEST_PARAMS_MDC_KEY = "REQUEST_PARAMS";
    private static final String REQUEST_METHOD_MDC_KEY = "REQUEST_METHOD";
    private static final String REQUEST_HEADERS_MDC_KEY = "REQUEST_HEADERS";
    private static final String REQUEST_BODY_MDC_KEY = "REQUEST_BODY";
    private static final String REQUEST_START_TIME_MDC_KEY = "REQUEST_START_TIME";
    private static final String REQUEST_DURATION_MDC_KEY = "REQUEST_DURATION_MILLIS";
    private static final String RESPONSE_CODE_MDC_KEY = "RESPONSE_CODE";
    private static final String RESPONSE_HEADERS_MDC_KEY = "RESPONSE_HEADERS";
    private static final String RESPONSE_BODY_MDC_KEY = "RESPONSE_BODY";

    public static HttpContext createContext(Logger logger) {
        HttpContext context = HttpClientContext.create();
        context.setAttribute(LoggingHttpInterceptor.LOGGER_CONTEXT_ATTR, logger);
        return context;
    }


    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws IOException {
        try {
            MDC.put(REQUEST_URI_MDC_KEY, request.getUri().toString());
            MDC.put(REQUEST_HOST_MDC_KEY, request.getUri().getHost());
            MDC.put(REQUEST_PATH_MDC_KEY, request.getUri().getPath());
            MDC.put(REQUEST_PARAMS_MDC_KEY, request.getUri().getQuery());
            MDC.put(REQUEST_METHOD_MDC_KEY, request.getMethod());
            MDC.put(REQUEST_HEADERS_MDC_KEY, Arrays.deepToString(request.getHeaders()));
            String loggingBody = getLoggingBody(entity);
            if (loggingBody != null) {
                MDC.put(REQUEST_BODY_MDC_KEY, loggingBody);
            }
            long startTime = System.currentTimeMillis();
            MDC.put(REQUEST_START_TIME_MDC_KEY, String.valueOf(startTime));
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getLoggingBody(EntityDetails details) throws Exception {
        if (details == null) {
            return null;
        }
        if (details instanceof HttpEntity) {
            HttpEntity entity = (HttpEntity) details;
            if (entity.isRepeatable()) {
                if (hasTextContent(entity)) {
                    return StringUtils.abbreviate(EntityUtils.toString(entity), MAX_BODY_LENGTH);
                }
            }
        }
        return MessageFormat.format(CONTENT_LENGTH_TEMPLATE, details.getContentLength());
    }

    private boolean hasTextContent(HttpEntity entity) {
        String contentType = entity.getContentType();
        if (Strings.isNullOrEmpty(contentType)) {
            return false;
        }
        for (String textContent : TEXT_CONTENTS) {
            if (contentType.toLowerCase().contains(textContent)) {
                return true;
            }
        }
        return false;
    }

    private HttpEntity createByteArrayEntity(HttpEntity entity) throws IOException {
        InputStream content = entity.getContent();
        ByteArrayOutputStream baContent = new ByteArrayOutputStream(content.available());
        IOUtils.copy(content, baContent);
        return new ByteArrayEntity(
                baContent.toByteArray(),
                ContentType.parse(entity.getContentType()),
                entity.getContentEncoding()
        );
    }

    @Override
    public void process(HttpResponse response, EntityDetails entity, HttpContext context) {
        long endTime = System.currentTimeMillis();
        String start = MDC.get(REQUEST_START_TIME_MDC_KEY);
        if (!Strings.isNullOrEmpty(start)) {
            try {
                long startTime = Long.parseLong(start);
                long duration = endTime - startTime;
                MDC.put(REQUEST_DURATION_MDC_KEY, String.valueOf(duration));
                MDC.remove(REQUEST_START_TIME_MDC_KEY);
            } catch (Exception ex) {
                // ignore
            }
        }
        Logger logger = getLogger(context);
        try {
            MDC.put(RESPONSE_CODE_MDC_KEY, String.valueOf(response.getCode()));
            MDC.put(RESPONSE_HEADERS_MDC_KEY, Arrays.deepToString(response.getHeaders()));
            String loggingBody = getResponseLoggingBody(response);
            if (loggingBody != null) {
                MDC.put(RESPONSE_BODY_MDC_KEY, loggingBody);
            }
            if (context instanceof HttpCoreContext) {
                //log all request headers including setted in HttpClient inner logic
                HttpCoreContext coreContext = (HttpCoreContext) context;
                Header[] headers = coreContext.getRequest().getHeaders();
                MDC.put(REQUEST_HEADERS_MDC_KEY, Arrays.deepToString(headers));
            }
            logger.info("http client request/response");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            clearMdc();
        }
    }

    private Logger getLogger(HttpContext context) {
        if (context != null) {
            Object logger = context.getAttribute(LOGGER_CONTEXT_ATTR);
            if (logger instanceof Logger) {
                return (Logger) logger;
            }
        }
        return DEFAULT_LOGGER;
    }

    private String getResponseLoggingBody(HttpResponse response) throws Exception {
        if (response instanceof ClassicHttpResponse) {
            ClassicHttpResponse chr = (ClassicHttpResponse) response;
            HttpEntity entity = chr.getEntity();
            if (entity != null) {
                if (hasTextContent(entity)) {
                    if (isGzipContentEncoding(entity)) {
                        entity = new GzipDecompressingEntity(entity);
                    }
                    entity = createByteArrayEntity(entity);
                    chr.setEntity(entity);
                    return getLoggingBody(entity);
                }
                return MessageFormat.format(CONTENT_LENGTH_TEMPLATE, entity.getContentLength());
            }
        }
        return null;
    }

    private void clearMdc() {
        MDC.remove(REQUEST_URI_MDC_KEY);
        MDC.remove(REQUEST_HOST_MDC_KEY);
        MDC.remove(REQUEST_PATH_MDC_KEY);
        MDC.remove(REQUEST_PARAMS_MDC_KEY);
        MDC.remove(REQUEST_METHOD_MDC_KEY);
        MDC.remove(REQUEST_HEADERS_MDC_KEY);
        MDC.remove(REQUEST_BODY_MDC_KEY);
        MDC.remove(REQUEST_START_TIME_MDC_KEY);
        MDC.remove(REQUEST_DURATION_MDC_KEY);
        MDC.remove(RESPONSE_CODE_MDC_KEY);
        MDC.remove(RESPONSE_HEADERS_MDC_KEY);
        MDC.remove(RESPONSE_BODY_MDC_KEY);
    }

    private boolean isGzipContentEncoding(HttpEntity entity) {
        String encoding = entity.getContentEncoding();
        if (Strings.isNullOrEmpty(encoding)) {
            return false;
        }
        return encoding.equalsIgnoreCase("gzip");
    }
}

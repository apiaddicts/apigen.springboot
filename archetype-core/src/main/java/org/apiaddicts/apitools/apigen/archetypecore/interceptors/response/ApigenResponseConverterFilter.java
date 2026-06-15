package org.apiaddicts.apitools.apigen.archetypecore.interceptors.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.JsonPatch;
import com.flipkart.zjsonpatch.JsonPatchApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.archetypecore.autoconfigure.ApigenProperties;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class ApigenResponseConverterFilter implements Filter {

    // zjsonpatch 0.4.16 uses com.fasterxml.jackson types in its public API
    private static final com.fasterxml.jackson.databind.ObjectMapper MAPPER =
            new com.fasterxml.jackson.databind.ObjectMapper();

    private final ApigenProperties props;
    private static final String PROP_PATH_ALL = "^[a-zA-z/]+[*]{1}[a-zA-z/]+";

    public ApigenResponseConverterFilter(ApigenProperties props) {
        this.props = props;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        List<String> operations = props.getStandardResponse().getOperations();
        if (operations == null || operations.isEmpty()) {
            chain.doFilter(request, response);
        } else {
            final ContentCachingResponseWrapper responseWrapper =
                    new ContentCachingResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, responseWrapper);
            try {
                final byte[] originalData = responseWrapper.getContentAsByteArray();
                String contentType = responseWrapper.getContentType();
                if (contentType.equals(MediaType.APPLICATION_JSON_VALUE) && originalData.length != 0) {
                    final String newData = convert(originalData, operations);
                    int status = ((HttpServletResponse) response).getStatus();
                    response.reset();
                    response.setContentLength(newData.length());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ((HttpServletResponse) response).setStatus(status);
                    PrintWriter out = response.getWriter();
                    out.print(newData);
                    out.flush();
                } else {
                    response.getOutputStream().write(responseWrapper.getContentAsByteArray());
                    response.flushBuffer();
                }
            } catch (Exception e) {
                log.error("Error applying response transformation", e);
                response.setContentLength(responseWrapper.getContentSize());
                response.getOutputStream().write(responseWrapper.getContentAsByteArray());
                response.flushBuffer();
            }
        }
    }

    private String convert(byte[] originalData, List<String> operations) throws IOException {
        final ObjectNode root = (ObjectNode) MAPPER.readTree(originalData);
        JsonNode target = root.deepCopy();

        for (String operation : operations) {
            JsonNode op = MAPPER.readTree(operation);
            try {
                String path = op.get("path").asText();
                String from = op.has("from") ? op.get("from").asText() : null;
                if (path != null && Pattern.matches(PROP_PATH_ALL, path)) {
                    String[] pathParts = path.split("\\*");
                    String parent = pathParts[0].replace("/", "");
                    int size = null != target.get(parent) ? target.get(parent).size() : 0;
                    for (int i = 0; i < size; i++) {
                        JsonNode op2 = op.deepCopy();
                        ObjectNode cp2 = ((ObjectNode) op2).put("path", path.replace("*", i + ""));
                        if (from != null) {
                            cp2.put("from", from.replace("*", i + ""));
                        }
                        log.debug("Applying response transformation {}", cp2);
                        target = JsonPatch.apply(MAPPER.readTree("[" + op2 + "]"), target);
                    }
                } else {
                    log.debug("Applying response transformation {}", op);
                    target = JsonPatch.apply(MAPPER.readTree("[" + op + "]"), target);
                }
            } catch (JsonPatchApplicationException e) {
                log.debug("Response transformation error: {} ", e.getMessage());
            }
        }
        return target.toString();
    }
}

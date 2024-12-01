package com.infinity.julien.security.encryption;
//! Not using due to some issues related to Instant Time
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import java.util.Objects;
//
//@RestControllerAdvice
//public class EncryptionResponseBodyAdvice implements ResponseBodyAdvice<Object> {
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return !returnType.getDeclaringClass().isAnnotationPresent(SkipEncryption.class) &&
//                !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(SkipEncryption.class);
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
//                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
//                                  org.springframework.http.server.ServerHttpRequest request,
//                                  org.springframework.http.server.ServerHttpResponse response) {
//        if (body == null) {
//            return null;
//        }
//
//        try {
//            String jsonResponse = new ObjectMapper().writeValueAsString(body);
//            return EncryptionUtil.encrypt(jsonResponse);
//        } catch (Exception e) {
//            throw new RuntimeException("Error encrypting response", e);
//        }
//    }
//}

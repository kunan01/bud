//package com.bud.code.config;
//
//import com.alibaba.fastjson.JSON;
//import com.bud.code.common.api.vo.Result;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.Charset;
//
///**
// * 统一返回报文格式
// */
//@Component
//public class WrapperResponseFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpResponse originalResponse = exchange.getResponse();
//        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
//        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//                if (body instanceof Flux) {
//                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
//                    return super.writeWith(fluxBody.map(dataBuffer -> {
//                        // probably should reuse buffers
//                        byte[] content = new byte[dataBuffer.readableByteCount()];
//                        dataBuffer.read(content);
//                        // 释放掉内存
//                        DataBufferUtils.release(dataBuffer);
//                        String rs = new String(content, Charset.forName("UTF-8"));
//                        Result<Object> result = Result.ok(rs);
//
//                        byte[] newRs = JSON.toJSONString(result).getBytes(Charset.forName("UTF-8"));
//                        originalResponse.getHeaders().setContentLength(newRs.length);//如果不重新设置长度则收不到消息。
//                        return bufferFactory.wrap(newRs);
//                    }));
//                }
//                // if body is not a flux. never got there.
//                return super.writeWith(body);
//            }
//        };
//        return null;
//    }
//
//    @Override
//    public int getOrder() {
//        // 注意的是order需要小于-1，需要先于NettyWriteResponseFilter过滤器执行。
//        return -2;
//    }
//}

package com.tencent.trpc.demo.server.impl;

import com.google.protobuf.TextFormat;
import com.tencent.trpc.core.rpc.RpcContext;
import com.tencent.trpc.demo.proto.ChenHaoRequestProtocol;
import com.tencent.trpc.demo.proto.ChenHaoRequestProtocol.ChenHaoRequest;
import com.tencent.trpc.demo.proto.ChenHaoRequestProtocol.ChenHaoResponse;
import com.tencent.trpc.demo.proto.ChenHaoRequestProtocol.ChenHaoResponse.Builder;
import com.tencent.trpc.demo.proto.ChenHaoServiceAPI;

/**
 * @author chaoccchen
 * @class ChenHaoServiceImpl
 * @create 2024-05-17 16:46
 */

public class ChenHaoServiceImpl implements ChenHaoServiceAPI {

    @Override
    public ChenHaoResponse sayHi(RpcContext context, ChenHaoRequest request) {
        String threadName = Thread.currentThread().getName();
        String msg = TextFormat.shortDebugString(request);
        System.out.println(threadName + ">>>[server]receive msg[" + msg + "]");
        String name = request.getName();
        Builder rspBuilder = ChenHaoResponse.newBuilder();
        rspBuilder.setSuccess(true);
        rspBuilder.setMessage("Hi " + name);
        System.out.println(threadName + ">>>[server]return msg[" + TextFormat.shortDebugString(rspBuilder) + "]");
        return rspBuilder.build();
    }
}

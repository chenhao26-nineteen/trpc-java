/*
 * Tencent is pleased to support the open source community by making tRPC available.
 *
 * Copyright (C) 2023 THL A29 Limited, a Tencent company.
 * All rights reserved.
 *
 * If you have downloaded a copy of the tRPC source code from Tencent,
 * please note that tRPC source code is licensed under the Apache 2.0 License,
 * A copy of the Apache 2.0 License can be found in the LICENSE file.
 */

package com.tencent.trpc.demo.example.config;

import com.tencent.trpc.core.common.ConfigManager;
import com.tencent.trpc.core.common.config.BackendConfig;
import com.tencent.trpc.core.common.config.ConsumerConfig;
import com.tencent.trpc.core.rpc.RpcClientContext;
import com.tencent.trpc.demo.proto.ChenHaoRequestProtocol.ChenHaoRequest;
import com.tencent.trpc.demo.proto.ChenHaoServiceAPI;
import com.tencent.trpc.demo.proto.GreeterServiceAPI;
import com.tencent.trpc.demo.proto.HelloRequestProtocol;
import java.util.concurrent.TimeUnit;

public class ClientTest {

    public static void main(String[] args) throws Exception {
        try {
            // start global config manager
            ConfigManager.getInstance().start();

            int times = 5;

            System.out.println("=================tcp test===================");
            testTrpc(times);
            System.out.println("=================tcp test done===================");

            System.out.println("=================http test===================");
            testHttp(times);
            System.out.println("=================http test done===================");

        } finally {
            // stop global config manager
            ConfigManager.getInstance().stop();
        }
    }

    private static void testTrpc(int times) throws Exception {
        // setup remote service interface
        ConsumerConfig<GreeterServiceAPI> consumerConfig = new ConsumerConfig<>();
        consumerConfig.setServiceInterface(GreeterServiceAPI.class);

        ConsumerConfig<ChenHaoServiceAPI> consumerConfig2 = new ConsumerConfig<>();
        consumerConfig2.setServiceInterface(ChenHaoServiceAPI.class);

        // setup trpc service backend
        BackendConfig backendConfig = new BackendConfig();
        backendConfig.setNamingUrl("ip://127.0.0.1:" + ServerTest.TCP_PORT);

        // create trpc service proxy
        GreeterServiceAPI proxy = backendConfig.getProxy(consumerConfig);
        ChenHaoServiceAPI proxy2 = backendConfig.getProxy(consumerConfig2);

        // run tests
        runTests(times, proxy);

        //run tests2
        runTests2(times, proxy2);
    }

    private static void testHttp(int times) throws Exception {
        // setup remote service interface
        ConsumerConfig<GreeterServiceAPI> consumerConfig = new ConsumerConfig<>();
        consumerConfig.setServiceInterface(GreeterServiceAPI.class);

        ConsumerConfig<ChenHaoServiceAPI> consumerConfig2 = new ConsumerConfig<>();
        consumerConfig2.setServiceInterface(ChenHaoServiceAPI.class);

        // setup http service backend
        BackendConfig backendConfig = new BackendConfig();
        backendConfig.setNamingUrl("ip://127.0.0.1:" + ServerTest.HTTP_PORT);
        backendConfig.setProtocol("http");

        // create trpc service proxy
        GreeterServiceAPI proxy = backendConfig.getProxy(consumerConfig);
        ChenHaoServiceAPI proxy2 = backendConfig.getProxy(consumerConfig2);

        // run tests
        runTests(times, proxy);

        //run tests2
        runTests2(times, proxy2);
    }

    private static void runTests(int times, GreeterServiceAPI proxy) throws Exception {
        for (int i = 0; i < times; i++) {
            String message = proxy
                    .sayHello(new RpcClientContext(),
                            HelloRequestProtocol.HelloRequest.newBuilder().setMessage("tRPC-HelloRequest").build())
                    .getMessage();
            System.out.println(Thread.currentThread().getName() + ">>>[client] receive msg: " + message);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void runTests2(int times, ChenHaoServiceAPI proxy) throws Exception {
        for (int i = 0; i < times; i++) {
            String message = proxy
                    .sayHi(new RpcClientContext(),
                            ChenHaoRequest.newBuilder().setName("tRPC-ChenHaoRequest").build())
                    .getMessage();
            System.out.println(Thread.currentThread().getName() + "x>>>[client] receive msg: " + message);
            TimeUnit.SECONDS.sleep(1);
        }
    }

}

# bud 
基于SpringCloud构建的学习入门项目，将持续更新。


> 项目结构为 maven 多模块工程 下面为各模块简单介绍：
1. bud 为父模块,不做代码上的操作
2. eureka-server 为注册中心，所有服务均在该项目上注册，才能被发现
3. bud-common 为 eureka-client 客户端1
4. bud-util 为 eureka-client 客户端2
5. bud-zuul 为 网关
6. bud-zipkin 为链路追踪（查看服务的调用情况）
7. bud-turbine 为集群监控（单节点监控可以在bud-util中查看）

> ribbon 和 feign 服务调用示例，请参照 bud-util 里面的测试代码

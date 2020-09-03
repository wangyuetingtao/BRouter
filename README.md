# BRouter
路由是组件化框架中重要的基础设施，解耦，做ab，通信，降级等等...它带来的好处不用多说，下面继续使用Apt实现一个简易的路由框架

   * 支持activity和fragment，后续可以拓展支持View， Provider，Broadcast
   * 支持拦截器
   * 支持传参
   * 统一配置activity转场动画
   * 支持requestcode和activity result
   * 暂不支持多module
   * RouteMeta路由上下文在拦截器中露出，方便拦截器对路由二次配置。如重定向，降级，ab等

![image](https://github.com/wangyuetingtao/BRouter/blob/master/screenshot/brouter1.gif)

路由框架的原理：需要使用到以下几方面技术
* apt生成路由表
* javapoet生成路由表所在类代码，参考模板类：[Template](https://github.com/wangyuetingtao/BRouter/blob/master/app/src/main/java/com/wyttlb/brouter/template/Template.java)
* 使用java spi扫描META-INF文件中的拦截器接口实现，用于运行时获取接口实现


## 踩到的坑

### Java 8中对象排序
对象排序，通常使用我们常用的Collections工具类
``` java
            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object t1, Object t2) {
                  //1:升序;0:不排序;-1:降序
                   return 0;
                }
            });
```

Debug了多次，发现居然不执行排序。必须要改成lambda表达式形式
```
                    Collections.sort(mInteceptors.get(key), (Comparator<Object>) (t1, t2) -> {
                    
                    return 0;
                });
```


### Java 中的SPI运行时获取某个接口类

> SPI即Service Provider Interfaces，做什么用的呢？

有时候一个接口可能有多种实现方式, 如果将特定实现写死在代码里面, 那么要更换实现的时候就要改动代码, 对原有代码进行修改. 这样非常麻烦, 而且也容易导致bug. 
Java提供了一种方式, 让我们可以对接口的实现进行动态替换, 这就是SPI机制. SPI机制非常简单, 步骤如下:

* 定义接口
* 定义接口的实现
* 创建resources/META-INF/services目录
* 在上一步创建的目录下创建一个以接口名(类的全名) 命名的文件, 文件的内容是实现类的类名 (类的全名), 如:
在services目录下创建的文件是com.stone.imageloader.ImageLoader 文件中的内容为ImageLoader接口的实现类, 可能是com.stone.imageloader.impl.FrescoImageLoader
* 使用ServiceLoader查找接口的实现

spi机制将接口和实现分开, 方便实现的动态替换. 这就是面向接口编程, 其好处是显而易见的.

 简单说就是将某个接口的实现配置到文件中，用哪个就配置哪个。运行时Java的ServiceLoader可以扫描这个文件，得到接口实现类。
 
 然鹅这个东西在Android上是有问题的：
 1. SPI是跟随Jar包发布，每个jar都有自己的配置，Android构建时，会将所有的jar合并，难免有相同的SPI冲突，造成`DuplicatedZipEntryException`。
 2. 性能问题，Android签名后，从jar中读取的时候要做签名校验，可能会影响速度。
 
 **待优化
   思路：可以编译完所有class文件后，使用apt生成配置文件，根据配置文件生成一个静态工具类，持有这些配置的接口实现类。
   
   在我们的Demo中，还是使用手动配置接口实现的方式，仅用于演示路由的原理。
   


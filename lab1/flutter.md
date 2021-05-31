# 初识flutter
## 1.什么是Flutter
Flutter 是 Google 开源的 UI 工具包，帮助开发者通过一套代码库高效构建多平台精美应用，支持移动、Web、桌面和嵌入式平台（来自官方介绍）
在给出的样本中，我确实看到了很多优秀的试图效果，很好奇我今后的学习时如何进行制作。

Flutter是免费开源的, 使用**Dart**语言, 并且是**禁止反射**的, 主要**用于移动端的Android和Ios开发, 不支持Web, 也不专注桌面应用**;

Flutter支持的系统版本在**Android 4.1.x（API16）/ iOS 8 及以上**; 支持的硬件在 **ARM Android / 64位iOS设备(iPhone 5S +)**, 目前不支持 **ARM32 iOS / x86 Android**; 并且目前**对平板的支持尚不明确**;

Flutter是一款移动应用程序SDK, 有很方便的图像引擎, Flutter遵循平台的规范和界面细节, 帮助开发者在 iOS 和 Android 两个平台开发高质量原生 UI 的移动 SDK. 但是**Flutter是不支持原生控件**的;

以下是Flutter展示的几个样本

<img src="https://flutterchina.club/images/homepage/screenshot-1.png" alt="Brand-first shopping design" style="zoom:25%;" /> <img src="https://flutterchina.club/images/homepage/screenshot-2.png" alt="Fitness app design" style="zoom:25%;" /> <img src="https://flutterchina.club/images/homepage/screenshot-3.png" alt="Contact app design" style="zoom:25%;" /> <img src="https://flutterchina.club/images/homepage/ios-friendlychat.png" alt="iOS chat app design" style="zoom:25%;" />

## 2.为什么使用Flutter

在网上能搜索到的介绍是：Flutter是谷歌的移动UI框架，可以快速在iOS和Android上构建高质量的原生用户界面。 Flutter可以与现有的代码一起工作。它是为保证快速开发而设计的, 并且自带一套现代的响应式框架的可定制控件, 代码直接被编译成ARM机器码;

Flutter项目主业上对Flutter的定义是：Flutter makes it easy and fast to build beautiful mobile apps.他可以轻松、快速的构建漂亮的移动应用（不过现阶段我还不清楚如何使用）。

以下列出几点Flutter的优点：

### 1.性能强大，流畅

Flutter基于dom树渲染原生组件，很难与直接在原生视图上绘图比肩性能，但Google直接在两个平台上重写了各自的UIKit，对接到平台底层，减少UI层的多层转换，UI性能可以比肩原生，这个优势在**滑动**和**播放动画**时尤为明显。

### 2.优秀的动画设计

Flutter的动画十分简单，动画对象会根据屏幕刷新率每秒产生很多个（一般是60个）浮点数，只需要将一个组件属性通过补间（Tween）关联到动画对象上。它会确保在每一帧渲染正确的组件，从而形成连贯的动画，同时不会出现卡顿。

### 3.UI跨平台稳定

CSS换个浏览器经常会出现表现效果有差距，而Flutter不需要依赖CSS等外部解释器，直接在在两个平台上在底层重写了UIKit，几乎不会出现UI效果不稳定现象。

## 3.使用Flutter的企业



<img src="https://flutter.dev/assets/homepage/garden-logos-color/google-5c9ef2841dda5d0247e53d56a91a70a1b961a08f1d2f0898d14441c3bc943586.png" alt="谷歌徽标" style="zoom: 25%;" />          ![易趣标志](https://flutter.dev/assets/homepage/garden-logos-color/ebay-f4a49fe64c5b6aedae0e8569f73e9162ae874c9d273464e5047cee3eb9388cff.png)<img src="https://flutter.dev/assets/homepage/garden-logos-color/bmw-e4981c2b2e2232677ae21defd00772860216b16e5c1e3fd96feb000e4f661f0c.jpg" alt="宝马标志" style="zoom:50%;" /><img src="https://flutter.dev/assets/homepage/garden-logos-color/square-1ad0f8048aac312d74648ce0bdc3b1bfd35725d8aa03822d5142e0eeec0353e7.png" alt="方形徽标" style="zoom:50%;" />   <img src="https://flutter.dev/assets/homepage/garden-logos-color/alibaba-97b7139685585cb9201f2b7b52bbef9f5b5df33349dd374738fad86a99a2ef01.png" alt="阿里巴巴标志" style="zoom:25%;" /> 

## 4.总结

Flutter主要的优势则在于动画流畅，很多开发者反应比原生安卓还流畅（存疑），至少在iOS上是看不到卡顿的，安卓上动画也很稳定，性能上展示了Google的硬实力。
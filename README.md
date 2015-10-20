# MyLover

## 写在前面
   `此程序目的设计目的是为了让女朋友开心，无任何商业用途..`
   
## 使用说明
 `程序设计很简单，一个WebView，一个RepaidFloationgActionButton`

  1. 由于程序中含有一个webview,所以需要自己搭建网站(我使用的为github pages建立的，具体信息点击请[github pages](github pages))
  2. 建立好网站后，替换掉程序中的网站链接
  ```
  //网站的链接
   private String path = "http://monsterlin.github.io/Lover_Time/"; 
  ```
 
  3. 替换手机号:
  ```
  //恋人手机号
  private String mobilePhone = "********" ;
  ```

  4. 在清单文件中替换高德地图的key(由于SHAI和包名的限制所以开发者需要去高德开发者平台申请key)

## 程序所使用的类库
  ```
    compile 'com.android.support:appcompat-v7:22.2.1'
    compile 'com.android.support:design:22.2.0'
    compile 'com.pnikosis:materialish-progress:1.7'
    compile 'com.github.pedrovgs:lynx:1.5'
    compile 'com.github.wangjiegulu:RapidFloatingActionButton:1.0.3'
    compile 'com.github.wangjiegulu:AndroidBucket:1.0.4'
    compile 'com.github.wangjiegulu:AndroidInject:1.0.6'
    compile 'com.github.wangjiegulu:ShadowViewHelper:1.0.1'
    compile 'com.nineoldandroids:library:2.4.0'
    compile files('libs/AMap_Location_v1.4.1_20150917.jar')
  ```
## 鸣谢
   @[angcyo](https://github.com/angcyo) 
## 效果展示
![](http://images.cnblogs.com/cnblogs_com/boy1025/689108/o_QQ%E6%88%AA%E5%9B%BE20151020103252.png)

![](http://images.cnblogs.com/cnblogs_com/boy1025/689108/o_QQ%E6%88%AA%E5%9B%BE20151020103300.png)

![](http://images.cnblogs.com/cnblogs_com/boy1025/689108/o_QQ%E6%88%AA%E5%9B%BE20151020103334.png)



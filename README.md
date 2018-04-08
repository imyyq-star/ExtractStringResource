# ExtractStringResource
 扩展 AS 的 “Extract string resource” 功能，自动翻译中文字符串。

# 为啥做这个？
英文不好的小朋友，当遇到字符串需要放到strings.xml中的时候，是否有过这样的操作：
![](http://osr1kvfkv.bkt.clouddn.com/18-4-8/17370762.jpg)

# 使用方法
下载Release的jar，打开Settings -> Plugins -> Install plugin from disk...，安装后重启AS。

去百度翻译申请自己的API ID和密钥。打开Settings -> Other Settings -> Auto extract string resource，填入相关信息。

然后这样使用：
![](http://osr1kvfkv.bkt.clouddn.com/18-4-8/12910954.jpg)

或者直接快捷键：Alt + Shift + A，也可以自定义。
原理就是获取了双引号内的字符串，通过百度翻译API自动翻译后，全部化为小写，单词用下划线连接作为string的name。

# 鸣谢
[AndroidResourceGeneratedPlugin](https://github.com/Androidyuan/AndroidResourceGeneratedPlugin)

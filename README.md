# developtools
运行效果是一个下拉刷新的随机图片查看器
框架特点：
1.图片加载glide，缓存okhttp，手势缩放PinchImageView，
2.网络请求封装MyOkhttpUtil(封装的鸿阳大神的)，
3.DialogUtils：showToast()(自定义CToast);showProgressBar：自定义LoadingDialog;
4.Url参数加密UrlDecryption.my
5.各种Utils：
BitmapUtils：提供图片saveToSd
SPUtil：快速使用SharePrefrence
Res：提供各种资源目录(value,drawable,raw,xml,anim..)id的值的读取
ScreenUtils:单位之间的转换dp px sp，隐藏键盘
CommonAdapter：通用的adapter(listview，gridview通用，recycleview通用的没加入，推荐鸿阳大神的CommonAdapter);
CrashHandler:处理app抛出的异常

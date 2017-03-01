# SpiderEverything
SpiderEverything各种爬虫合集，目前已实现对某电影资源网爬取。具有多线程和代理功能。核心代码来自于webmagic-core(感谢@code4craft大神)，所以本爬虫也具备多线程、代理池、请求自动重试功能，并在此基础上增加了一些小功能，你可以像爬取网页一样自动下图片和文件资源(支持自动重试)，也可以对单个请求设置不使用代理，并且整合了Spring（主要是为了方便使用Spring-data-jpa）。
## 目前已实现功能
* 1.实现对某电影资源网的爬取。
* 2.实现了一个简单的代理爬虫，可自动爬取网上可用的代理并保存到本地
* 3.对webmagic的Request功能做了拓展，目前可以像下载网页一样自动保存图片和网页
* 4.整合了Spring。具体例子可查看[com.wong.spider.movie](https://github.com/Wang-Juntao/SpiderEverything/tree/master/src/main/java/com/wong/spider/movie "com.wong.spider.movie")爬虫实例使用Spring-data-jpa实现了数据的保存
* 5.去掉了webmagic的pipeline概念，直接整合到PageProcessor中，并且可以同一网站对应多个processor。更灵活
* 6.请求可以设置优先级，也可以设置是否使用代理
* 7.支持热启动（beta），每隔10分钟可自动保存当前状态，下次再启动不必从头开始

## 待实现功能
* 1.代理自动发现
* 2.保存当前爬虫运行的状态，下次可以热启动
* 3.可定时更新一些已经请求过的网页


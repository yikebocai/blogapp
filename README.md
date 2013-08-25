blogapp
=======

这是用[Clojure](http://Clojure.org)写的一个静态博客程序，该系统本身没有像[WordPress](http://wordpress.com)这些博客系统标配的文本编辑框，所有的文章都是从Github上读取的，并自动生成博客。你可以用自己最喜欢的Markdown编辑器，比如[Mou](http://mouapp.com/)来写文章，写好之后可以Push到Github做托管，所有的编辑记录都会有完整而清晰的记录，不用担心会丢失什么东西。系统会自动抓取Github上的文章，并建立索引生成Tag，写文章就这么简单。

**功能**

* 在首页展示所有文章列表
* 文章详情页面支持源代码语法高亮
* 支持Tag标签
* 支持社会化分享和评论
* 支持手动和自动同步
* 支持数据库查询
* 支持Session功能
* 支持RSS订阅
* 支持搜索功能

**部署使用**

* 使用`git clone https://github.com/yikebocai/blogapp.git`下载应用源代码到你的服务器
* 进入到`blogapp/deploy`目录，执行`bin/deploy`打成war包并部署到`blogapp/deploy/myapp.war`目录下
* 修改Tomcat的`conf/server.xml`，添加Context，并把路径指到war目录下
* 重新回到`blogapp/deploy`目录，执行`bin/startup`启动Tomcat
* 在浏览器里输入`http://hostname:port`进入首页
* 此时首页还没有任何内容，需要做一些简单的配置。先用默认的系统帐号`admin:yikebocai`登陆到系统中，配置本地文章存储目录，Github上的仓库URL，新的用户名密码等
* 切换到`Sync`页面，点击`同步`，将自动读取Github上的所有文章并展示出来
* 配置完毕之后，就可以用新的用户名密码来管理配置和手动同步了，系统默认密码就自动失效
* 如果需要关闭请在`deploy`目录下执行`bin/shutdown`
* Linux上一般不允许使用1024以内的端口，为了使用80端口，可以修改iptables做NAT转发，当访问80端口时自动转到内部启动的端口，比如8080

**注意事项**

对文章格式有一定的要求

* 仓库的目录结构必须为`src`和`src/myimg`，其中前者下面放MD文件，后者放文章中引用到的图片
* 文章的命名必须是日期+英文名称，比如`20130615-hello_world.md`，系统会解析前面的日期，作为文章Post的日期
* 文章内容第一行必须为文章展示时的标题，可以是中文
* 第二行为标签，格式为`Tag:java,jvm`
* 文章中引用图片时，格式必须为`myimg/xxx.jpg`
* 请参考[我的博客仓库](https://github.com/yikebocai/blog)

**示例**

请访问我用这个应用程序搭建的博客[一棵波菜](http://yikebocai.com)

**更新日志**
0.1.4(2013-8-27)

* 增加搜索功能
* 调整blog页面部署，包括源代码上下填充、页脚横排、页脚和文章内容的宽度等
* 将页面模板引擎从[clabango](https://github.com/danlarkin/clabango)换成[selmer](https://github.com/yogthos/Selmer)

0.1.3(2013-7-4)

* 增加定时同步文章功能

0.1.2(2013-6-29)

* 增加RSS订阅功能
* 增加CNZZ网站统计功能
* 调用友言接口实现首页评论数据展示

0.1.1(2013-6-23)

* 优化首页文章列表排版，增加阅读次数和评论次数、文章摘要的展示
* 调整默认字体，从14px到15px，行高从20px调整到24px，更加清晰

0.1.0(2013-6-15)

* 在首页展示所有文章列表
* 文章详情页面支持源代码语法高亮
* 支持Tag标签
* 支持社会化分享和评论
* 支持手动同步
* 支持数据库查询
* 支持Session功能

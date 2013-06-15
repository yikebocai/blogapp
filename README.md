blogapp
=======

这是用[Clojure](http://Clojure.org)写的一个静态博客程序，该系统本身没有像[WordPress](http://wordpress.com)这些博客系统标配的文本编辑框，所有的文章都是从Github上读取的，并自动生成博客。你可以用自己最喜欢的Markdown编辑器，比如[Mou](http://mouapp.com/)来写文章，写好之后可以Push到Github做托管，所有的编辑记录都会有完整而清晰的记录，不用担心会丢失什么东西。系统会自动抓取Github上的文章，并建立索引生成Tag，写文章就这么简单。

**功能（0.1.0）**

* 在首页展示所有文章列表
* 文章详情页面支持源代码语法高亮
* 支持Tag标签
* 支持社会化分享和评论
* 支持手动同步
* 支持数据库查询
* 支持Session功能

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


**示例**

请访问我用这个应用程序搭建的博客[一棵波菜](http://yikebocai.com)

**TODO**

* 增加自动同步功能
* 增加搜索功能



#智能家居V1.0.3
---
【是以住宅为平台，利用综合布线技术、网络通信技术、 安全防范技术
 、自动控制技术、音视频技术将家居生活有关的设施集成，构建高效的住宅设施与家庭日程事务的管理系统， 提升家居安全性、便利性、舒适性、艺术性，并实现环保节能的居住环境【2015-06-01】】
#####【于2015-06-01开始做起】
---
####存在的问题【2015-06-11】
---
#####【修改部分界面显示以及跳转问题】

	一：加载图片太细.
	二：去掉右滑动，将添加设备挪到左边.
	三：将添加遥控和场景挪到主页显示在顶尖.
	四：将标题栏的左按钮修改.

	五：成功连接显示避免竟然没有成功.
	六：在点击场景编辑完成的时候竟然报异常了.
	七：场景的开始没编辑的时候，不能显示框<改为：将他改为可编辑的，然后。。。>.
    八：添加场景，将所有遥控显示在添加场景的界面，然后利用复选框来添加。点击全选添加.

###报错了【2015-06-12】
---
	竟然这里点击场景进行编辑的页面，重复加载了三次，导致数据变化。
	适配手机【挂起，暂时把文件跟建立了出来】




###【一：获取图片应该有一个完整的获取款图片的途径(2015-06-15)】



	魅族手机（1080*1800）出现弹出上下文的时候，
	contextview没有显示完整（且，拍照后不能返回
	）[重现：这里出现不能创建文件夹，若直接保存在内存第一级
	目录下，顺利完成，若，创建文件，则会出现创建不成功的现象。]

###【二：实现场景定时启动（2015-06-16）】
	
	对于定时启动【Fir：手机端这边自己定时启动；Sec：这边通过想MQTT发送信息里面有一个字段参数用来控制什么时候启动（delay），然后，由Mqtt自动启动】
     
	定时启动：编辑操作

	错误二：
	   在【添加遥控】的时候，出现了错误.
	   还有在场景编辑的时候，记得也可以添加图片.


【节点】：设置编辑场景里面的背景【总是不能让列表里面的edittext获得背景同步】


###【2015-06-18】
	这里应该把已经添加进来的遥控继续添加到遥控选择一面
	对于定时操作，应该重新建立一个表，然后把已经设置了定时操作的数据以及定时操作给insert进去


###【2015-06-19】
	Q1：点击遥控进去继续编辑的时候，没有获取图片。
	Q2：应该把用户的头像也存储下来吧！
###【添加了定时操作，但是还不完善，消灭弹框以及实现数据记录（根据ID），然后完善界面】
---


	【Added a time operation, but it is not perfect, the elimination of the bomb box and the realization of data records (according to ID), and then improve the interface】

###【2015-06-23】


	Q1:在长按场景的时候，查询数据库，然后如果数据库有相应的数据，那么，在点击【定时启动】的时候，不跳转，只是启动。
	
	Q2：如若单点【定时启动】则是启动定时启动，如果长按定时启动，则是进入定时启动里面进行设置？


###【三：检查更新那里利用mqtt来获得】

---	




###【修改】

1:进来连接wifi和2G/3G/4G进行判断，显示不同的配置界面
2:头像，背景深黑色。。。去掉遥控描述。。。。添加遥控的界面添加图片后就更新了，遥控显示的字体太大，遥控显示使用默认的遥控的图标，
3：将首页切换成Tab控制的界面。

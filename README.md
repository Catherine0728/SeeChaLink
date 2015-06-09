# SeeChaLink
this is a project about a smarthome

# 智能家居 
----
                                 2015-05-28   星期四 


 


1. 配备统一的热点名字_ID，明确，快捷
2. 界面正规化，以及用户体验合理化
3. 对于使用规则应该使之清晰明了


- 为了将多个开关区分开来，采取场景布局操作。对于开关的操作合理化
- 添加遥控就是就是添加学习的对象（eg:电视机、空调、音响、机顶盒、电视、TC、自定义、手势）按键。
- 添加场景。就是添加一系列的遥控命令然后组成的一个场景
- 在首页，对场景以及遥控命令都会有重新的编辑...对场景有可以定时开启以及关闭的功能，还可以复制场景...以及删除操作
- 对遥控有删除以及编辑的操作
- 对于场景就是执行，对于遥控命令单击就是进行编辑。对于可以场景长按就是编辑，再次进入操作的...对于遥控就是删除...



#####进入应用，然后连接到设备。


####订阅主题（pub）

	seecha_smarthome

对于每次射频所发出的信息：（seecha_smarthome）

eg:

	{"api": "v1.0.0", "service": "seecha_smarthome_mqtt", "id": 1}



####检查更新：（sub）

######订阅主题：
	seecha_version
eg:
	{"api": "v1.0.0", "service": "seecha_smarthome_mqtt","version":2}

######服务器（pub） 
	seecha_update
eg：

	{"api": "v1.0.0", "service": "seecha_smarthome_mqtt","version":2,"version_url":"http//www.baidu.com"}





















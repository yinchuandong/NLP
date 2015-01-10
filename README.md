

旅游路线自动规划
=============
Part3:（Genetic Algorithm） and some NLP algorithm
-------------
### Source code:
    https://github.com/yinchuandong/Traveljsp		(Search Engine)
    https://github.com/yinchuandong/Crawler  		(Theme Crawler)
    https://github.com/chuandongjiewen/NLP	  	(Genetic Algorithm)
### Introduction: 
    Implement automation of travel route planning and personalization of service by data mining technologies
### Tasks: 
    Theme Crawler/Keyword Auto-suggestion/Travel Routes Auto-planning
### Functions:
    1.	Implement vertical search engine of sceneries and cities
    2.	Implement travel route auto-planning
### Technologies:
    1.	Implement multi-thread and distributed theme crawler for JSON file by Java
    2.	Implement keyword auto-suggestion by Trie Tree
    3.	Implement vertical search engine by Lucene framework
    4.	Implement travel route auto-planning by Greedy Algorithm and Genetic Algorithm
    5.	Implement data exchange of JSP, PHP and JavaScript


### 目录结构树
    |—— dict 分词算法的词库目录
    |—— gadata 遗传算法的数据目录
    |—— src java文件


### java包结构
    |—— GA 遗传算法
    |  |—— City.java 城市的模型
    |  |—— CityGa.java 对城市进行ga运算
    |  |—— Ga.java ga算法的demo
    |  |—— Hotel.java 酒店的Model
    |  |—— HotelHelper.java 从文本中读取hotel
    |  |—— MyGa.java 对酒店进行ga运算
    |—— Hmm 隐马尔科夫分词
    |  |—— EmissionHelper.java 发射概率
    |  |—— TransitionHelper.java 转移概率
    |  |—— InitHelper.java 初始化
    |  |—— PosModel 位置的模型
    |—— ParsingCFG (Context Free Grammar)上下文无关算法，生成语法树
    |—— ParsingPCFG (Probabilistic Context Free Grammar)概率上下文无关算法，生成语法树
    |—— Regex 正则表达式
    

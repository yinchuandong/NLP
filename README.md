自然语言处理的算法demo
==============

NLP(Natural Language Process)
--------------

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
    

# 第三天

### 增加增加丢失，和拾取物品的功能，修bug
    在推出登录的时候没办法到达指定网页，是设置的路由字母大小写问题
    增加丢失物品和拾取物品的功能
    需要把包装类传回给后端，但是我不确定包装类是否可以又mutilfile字段，所以设置了一个新的参数
    分别用requestbody和requestparams注解传参
    但是这里我用的是把对象转换为json字符串，再传给后端
    后端在进行解析序列化
    继续把留言和举报功能实现
    对应后端接口比较容易实现，我用传参就可以，不用传包装类了
    前端秩序在对相应的弹窗进行处理就可以
    对于ai的引入实现对物品的描述，我用的是openai的api
    给ai传参并加以提示和限制，ai会返回物品的描述，在创建物品时，把描述赋值给物品的description字段
    另外发现，发请求给后端的参数必须在const params = new URLSearchParams()中
    单纯给param传字段，会将其识别为json字符串，而不是url参数
    在content-type中m默认设置为application/json而不是application/x-www-form-urlencoded

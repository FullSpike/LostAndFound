
# 第七天

### 增加拾取无认领功能

    在需要认领的物品中，我觉得失物不需要这个认领申请，，主要是因为，需要填写检验信息
    这个显然不太适合，所以我之给拾取物添加认领申请的功能
    我在数据库定义了一个claim的表，用于存储拾取物的认领申请
    在里面有senderId,receiverId,c_id字段用于外键关联
    申请认领的按钮在所有的拾取物页面有，点击先检索uer自己和拾取人和物品id之间在claim表中是否有记录
    在chaim表中的is_check的字段，有‘是’，‘否’，‘补’，null四种可能，符合要求的数据可能不只一条，
    所以我在后端包装了一个claimdto类，为了更好的返回数据，如果claim中所有的数据的is_check都是补，就返回null
    is_check是补，但凡有一个不是补，就返回是或否
    优先级即 是 = 否 > 补 > null 
    这样在前端能更好的展示数据如在认领的按钮，检测为是或否，都会提示错误信息，不能点击
    在主界面添加了给我的认领申请，用于检测user作为receiver的认领申请，在后台我让传递的数据为is_check为null的
    因为在处理认领申请的时候，点击相当与“删除”了这条申请
    而申请的状态我展示在申请人的信息一栏，即检索user作为sender的claim列表，
    但我发现一个问题，怎么将检索特定receiver和物品的claim列表，关键这个列表还不一定只有一个元素
    我尝试先用stream流对所有已user作为sender的claim列表进行分组，实现如下
    Map<Integer, List<Claim>> claimsByReceiver = allClaims.stream()
    .collect(Collectors.groupingBy(s->s.getReceiverId()));
    我这样在分组后的claim列表进行检索，包装给claimdto类,拼接成list
    但显然这样咋前端就有以下问题的出现
        您的玩具认领申请需要补充信息
        您的玩具认领申请需要补充信息
        您的玩具认领申请已被同意，请与user00联系
    即会有重复的认领的申请
    所以我给后端的分组后的claim列表在分组，这次以c_id为分组键
    这样在前端的信息就能避免重复展示
    
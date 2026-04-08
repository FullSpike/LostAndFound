<template>
  <div class="user-home">
    <!-- 侧边栏导航 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="avatar-container">
          <el-avatar :size="64" :src="userInfo.avatar || defaultAvatar" />
          <h3>{{ userInfo.username }}</h3>
        </div>
      </div>
      <nav class="sidebar-nav">
        <div class="nav-item" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">
          <el-icon><User /></el-icon>
          <span>个人资料</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'lost' }" @click="activeTab = 'lost'">
          <el-icon><Search /></el-icon>
          <span>所有失物</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'found' }" @click="activeTab = 'found'">
          <el-icon><TakeawayBox /></el-icon>
          <span>所有拾取</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'myLost' }" @click="activeTab = 'myLost'">
          <el-icon><Document /></el-icon>
          <span>我的失物</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'myFound' }" @click="activeTab = 'myFound'">
          <el-icon><Collection /></el-icon>
          <span>我的拾取</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'addItem' }" @click="activeTab = 'addItem'">
          <el-icon><Plus /></el-icon>
          <span>发布物品</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'messages' }" @click="activeTab = 'messages'">
          <el-icon><Message /></el-icon>
          <span>消息中心</span>
          <el-badge v-if="unreadCount" :value="unreadCount" class="badge" />
        </div>
      </nav>
      <div class="sidebar-footer">
        <div class="nav-item" @click="openChangePasswordDialog">
          <el-icon><Lock /></el-icon>
          <span>修改密码</span>
        </div>
        <div class="nav-item" @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 个人资料页 -->
      <div v-if="activeTab === 'profile'" class="content-panel">
        <div class="panel-header">
          <h2>个人资料</h2>
          <el-button type="primary" plain @click="openEditUserDialog">编辑资料</el-button>
        </div>
        <div class="profile-card">
          <div class="profile-avatar">
            <el-avatar :size="100" :src="userInfo.avatar || defaultAvatar" />
            <el-upload action="#" :auto-upload="false" :show-file-list="false" :on-change="handleAvatarChange">
              <el-button type="text" class="change-avatar-btn">更换头像</el-button>
            </el-upload>
          </div>
          <div class="profile-info">
            <div class="info-row"><span class="label">用户ID</span><span class="value">{{ userInfo.id }}</span></div>
            <div class="info-row"><span class="label">用户名</span><span class="value">{{ userInfo.username }}</span></div>
            <div class="info-row"><span class="label">邮箱</span><span class="value">{{ userInfo.email }}</span></div>
            <div class="info-row"><span class="label">手机号</span><span class="value">{{ userInfo.phone }}</span></div>
            <div class="info-row"><span class="label">状态</span><span class="value"><el-tag :type="userInfo.status === 'active' ? 'success' : 'danger'">{{ userInfo.status === 'active' ? '正常' : '禁用' }}</el-tag></span></div>
          </div>
        </div>


      </div>

      <!-- 所有失物 -->
      <div v-if="activeTab === 'lost'" class="content-panel">
        <div class="panel-header">
          <h2>所有失物</h2>
          <div class="filter-bar">
            <el-input v-model="lostFilters.search" placeholder="按名称搜索" clearable prefix-icon="Search" style="width: 160px" />
            <el-input v-model="lostFilters.location" placeholder="按地点筛选" clearable style="width: 160px" />
            <el-select v-model="lostFilters.sort" placeholder="时间排序" clearable style="width: 130px">
              <el-option label="最新优先" value="desc" />
              <el-option label="最早优先" value="asc" />
            </el-select>
          </div>
        </div>
        <div class="item-grid">
          <div v-for="item in filteredLostList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('lost', item)">详情</el-button>
                <el-button link type="warning" @click="openReportDialog(item)">举报</el-button>
                <el-button link type="success" @click="openNoteDialog('lost', item)">留言</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 所有拾取物 -->
      <div v-if="activeTab === 'found'" class="content-panel">
        <div class="panel-header">
          <h2>所有拾取物</h2>
          <div class="filter-bar">
            <el-input v-model="foundFilters.search" placeholder="按名称搜索" clearable prefix-icon="Search" style="width: 160px" />
            <el-input v-model="foundFilters.location" placeholder="按地点筛选" clearable style="width: 160px" />
            <el-select v-model="foundFilters.sort" placeholder="时间排序" clearable style="width: 130px">
              <el-option label="最新优先" value="desc" />
              <el-option label="最早优先" value="asc" />
            </el-select>
          </div>
        </div>
        <div class="item-grid">
          <div v-for="item in filteredFoundList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('found', item)">详情</el-button>
                <el-button link type="warning" @click="openReportDialog(item)">举报</el-button>
                <el-button link type="success" @click="openNoteDialog('found', item)">留言</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 我的失物 -->
      <div v-if="activeTab === 'myLost'" class="content-panel">
        <div class="panel-header">
          <h2>我的失物</h2>
        </div>
        <div class="item-grid">
          <div v-for="item in myLostList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('myLost', item)">详情</el-button>
                <el-button link type="danger" @click="openDeleteDialog('lost', item.id)">删除</el-button>
                <el-button link type="warning" @click="openEditItemDialog('lost', item)">修改</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 我的拾取物 -->
      <div v-if="activeTab === 'myFound'" class="content-panel">
        <div class="panel-header">
          <h2>我的拾取物</h2>
        </div>
        <div class="item-grid">
          <div v-for="item in myFoundList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('myFound', item)">详情</el-button>
                <el-button link type="danger" @click="openDeleteDialog('found', item.id)">删除</el-button>
                <el-button link type="warning" @click="openEditItemDialog('found', item)">修改</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 发布物品 -->
      <div v-if="activeTab === 'addItem'" class="content-panel">
        <div class="panel-header">
          <h2>发布物品</h2>
        </div>
        <div class="add-item-card">
          <el-radio-group v-model="newItemType" class="type-switch">
            <el-radio-button label="lost">发布失物</el-radio-button>
            <el-radio-button label="found">发布拾物</el-radio-button>
          </el-radio-group>
          <el-form :model="newItem" label-width="80px" class="add-form">
            <el-form-item label="物品名称">
              <el-input v-model="newItem.name" placeholder="请输入物品名称" />
            </el-form-item>
            <el-form-item label="地点">
              <el-input v-model="newItem.location" placeholder="丢失/拾取地点" />
            </el-form-item>
            <el-form-item label="时间">
              <el-input v-model="newItem.time" placeholder="YYYY-MM-DD" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="newItem.phone" placeholder="请填写联系电话" />
            </el-form-item>
            <el-form-item label="描述">
              <el-input v-model="newItem.description" type="textarea" rows="3" placeholder="物品描述..." />
            </el-form-item>
            <el-form-item label="图片">
              <el-upload action="#" :auto-upload="false" :show-file-list="false" :on-change="handleImageChange">
                <el-button>上传图片</el-button>
              </el-upload>
              <span v-if="newItem.pathName" class="upload-tip">已上传</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="addNewItem">立即发布</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 消息中心 -->
      <div v-if="activeTab === 'messages'" class="content-panel">
        <div class="panel-header">
          <h2>消息中心</h2>
        </div>
        <div class="message-container">
          <div class="message-sidebar">
            <div class="msg-tab" :class="{ active: msgActiveTab === 'note' }" @click="msgActiveTab = 'note'">
              <el-icon><Comment /></el-icon> 留言消息
            </div>
            <div class="msg-tab" :class="{ active: msgActiveTab === 'chat' }" @click="msgActiveTab = 'chat'">
              <el-icon><ChatDotRound /></el-icon> 聊天消息
            </div>
          </div>
          <div class="message-content">
            <div v-if="msgActiveTab === 'note'" class="note-list">
              <div v-for="msg in messageList" :key="msg.id" class="note-item">
                <el-icon><Bell /></el-icon>
                <span>{{ msg.content }}</span>
              </div>
              <div v-if="messageList.length === 0" class="empty-msg">暂无留言消息</div>
            </div>
            <div v-if="msgActiveTab === 'chat'" class="chat-area">
              <div class="chat-user-list">
                <div v-for="user in otherUsers" :key="user.id" class="chat-user-item" :class="{ active: currentChatUser?.id === user.id }" @click="selectChatUser(user)">
                  <el-avatar :size="36" :src="user.avatar || defaultAvatar" />
                  <span>{{ user.username }}</span>
                </div>
              </div>
              <div class="chat-conversation">
                <div v-if="currentChatUser" class="chat-header">
                  <span>与 {{ currentChatUser.username }} 的对话</span>
                </div>
                <div v-if="!currentChatUser" class="chat-placeholder">点击左侧用户开始聊天</div>
                <div v-else class="chat-messages">
                  <div v-for="(msg, idx) in currentMessages" :key="idx" class="message-bubble" :class="{ self: msg.senderId === currentUserId }">
                    {{ msg.senderId === currentUserId ? 'self: ' : `${currentChatUser.username}: ` }}{{ msg.content }}
                  </div>
                </div>
                <div v-if="currentChatUser" class="chat-input-area">
                  <el-input v-model="newMessage" placeholder="输入消息..." @keyup.enter="sendMessage" />
                  <el-button type="primary" @click="sendMessage">发送</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 弹窗区域 -->
    <el-dialog v-model="editUserDialogVisible" title="编辑资料" width="480px" class="custom-dialog">
      <el-form :model="editUserForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editUserForm.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editUserForm.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editUserForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editUserDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUserInfo">保存</el-button>
      </template>
    </el-dialog>

    <!-- 详情弹窗 - 新增联系电话显示 -->
    <el-dialog v-model="detailDialogVisible" title="物品详情" width="450px">
      <div v-if="detailItem" class="detail-content">
        <p><strong>描述：</strong>{{ detailItem.description }}</p>
        <p><strong>状态：</strong>{{ detailItem.status }}</p>
        <p><strong>联系电话：</strong>{{ detailItem.phone || '未提供' }}</p>
        <p><strong>留言：</strong>{{ detailItem.note || '暂无' }}</p>
      </div>
    </el-dialog>

    <el-dialog v-model="noteDialogVisible" title="留言" width="400px">
      <el-input v-model="noteContent" type="textarea" rows="4" placeholder="请输入留言内容..." />
      <template #footer>
        <el-button @click="noteDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitNote">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="reportDialogVisible" title="举报" width="400px">
      <el-input v-model="reportReason" type="textarea" rows="4" placeholder="请输入举报理由..." />
      <template #footer>
        <el-button @click="reportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReport">提交举报</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="deleteDialogVisible" title="删除确认" width="360px">
      <p>确定要删除该物品吗？</p>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确认删除</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="editItemDialogVisible" title="修改物品" width="500px">
      <el-form :model="editItemForm" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="editItemForm.name" />
        </el-form-item>
        <el-form-item label="地点">
          <el-input v-model="editItemForm.location" />
        </el-form-item>
        <el-form-item label="时间">
          <el-input v-model="editItemForm.time" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="editItemForm.phone" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editItemForm.description" type="textarea" />
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="editItemForm.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editItemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveItemEdit">保存修改</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="changePwdDialogVisible" title="修改密码" width="400px">
      <el-form :model="pwdForm" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPwd" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPwd" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="pwdForm.confirmPwd" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="changePwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Search, TakeawayBox, Document, Collection, Plus, Message, Lock, SwitchButton, Bell, ChatDotRound, Comment } from '@element-plus/icons-vue'
import router from "@/router/index.js";

// 当前用户ID
const currentUserId = ref(1)

// 用户信息
const userInfo = ref({
  id: 1,
  avatar: '',
  username: '张三',
  password: '123456',
  email: 'zhangsan@example.com',
  phone: '13800138000',
  status: 'active'
})

// 宿舍相关
const dormNumber = ref('')
const currentDorm = ref('744')

// 物品数据 - 新增 phone 字段
const allLostList = ref([
  { id: 1, name: '笔记本电脑', location: '图书馆', time: '2025-03-01', pathName: '', description: '银色MacBook Pro', status: '寻找中', note: '', phone: '13800138001' },
  { id: 2, name: '校园卡', location: '二食堂', time: '2025-03-10', pathName: '', description: '姓名：张三，学号2024001', status: '寻找中', note: '', phone: '13800138002' }
])

const allFoundList = ref([
  { id: 1, name: '手机', location: '体育馆', time: '2025-03-05', pathName: '', description: '黑色iPhone 14', status: '待认领', note: '', phone: '13800138003' },
  { id: 2, name: '钱包', location: '教学楼A座', time: '2025-03-12', pathName: '', description: '棕色短款钱包', status: '待认领', note: '', phone: '13800138004' }
])

const myLostList = ref([
  { id: 3, name: '耳机', location: '健身房', time: '2025-03-08', pathName: '', description: '白色AirPods', status: '寻找中', note: '', phone: '13800138005' }
])

const myFoundList = ref([
  { id: 3, name: '水杯', location: '咖啡厅', time: '2025-03-09', pathName: '', description: '保温杯，黑色', status: '待认领', note: '', phone: '13800138006' }
])

// 消息
const messageList = ref([
  { id: 1, content: '您的物品(笔记本电脑)有留言消息，请查收' },
  { id: 2, content: '您有未读消息来自李四，请查收' }
])
const unreadCount = ref(2)

// 聊天用户
const otherUsers = ref([
  { id: 2, username: '李四', avatar: '' },
  { id: 3, username: '王五', avatar: '' }
])

const chatMessages = ref({
  2: [{ senderId: 2, content: '你好，请问笔记本还在吗？' }],
  3: [{ senderId: 3, content: '我捡到了一个水杯' }]
})

// UI状态
const activeTab = ref('profile')
const msgActiveTab = ref('note')
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const defaultImage = 'https://via.placeholder.com/120'

// 筛选条件
const lostFilters = ref({ search: '', location: '', sort: '' })
const foundFilters = ref({ search: '', location: '', sort: '' })

// 计算属性 - 筛选后的失物列表
const filteredLostList = computed(() => {
  let list = [...allLostList.value]
  if (lostFilters.value.search) {
    list = list.filter(item => item.name.includes(lostFilters.value.search))
  }
  if (lostFilters.value.location) {
    list = list.filter(item => item.location.includes(lostFilters.value.location))
  }
  if (lostFilters.value.sort === 'asc') {
    list.sort((a, b) => new Date(a.time) - new Date(b.time))
  } else if (lostFilters.value.sort === 'desc') {
    list.sort((a, b) => new Date(b.time) - new Date(a.time))
  }
  return list
})

// 计算属性 - 筛选后的拾取物列表
const filteredFoundList = computed(() => {
  let list = [...allFoundList.value]
  if (foundFilters.value.search) {
    list = list.filter(item => item.name.includes(foundFilters.value.search))
  }
  if (foundFilters.value.location) {
    list = list.filter(item => item.location.includes(foundFilters.value.location))
  }
  if (foundFilters.value.sort === 'asc') {
    list.sort((a, b) => new Date(a.time) - new Date(b.time))
  } else if (foundFilters.value.sort === 'desc') {
    list.sort((a, b) => new Date(b.time) - new Date(a.time))
  }
  return list
})

// 当前聊天消息
const currentMessages = computed(() => {
  if (!currentChatUser.value) return []
  return chatMessages.value[currentChatUser.value.id] || []
})

// 新增物品 - 新增 phone 字段
const newItemType = ref('lost')
const newItem = ref({ name: '', location: '', time: '', description: '', pathName: '', phone: '' })

const handleImageChange = (file) => {
  newItem.value.pathName = URL.createObjectURL(file.raw)
}

const addNewItem = () => {
  const id = Date.now()
  const item = {
    id,
    ...newItem.value,
    status: newItemType.value === 'lost' ? '寻找中' : '待认领',
    note: ''
  }
  if (newItemType.value === 'lost') {
    myLostList.value.push(item)
    allLostList.value.push(item)
  } else {
    myFoundList.value.push(item)
    allFoundList.value.push(item)
  }
  ElMessage.success('发布成功')
  newItem.value = { name: '', location: '', time: '', description: '', pathName: '', phone: '' }
}

// 弹窗相关状态
const editUserDialogVisible = ref(false)
const editUserForm = ref({})

const openEditUserDialog = () => {
  editUserForm.value = { ...userInfo.value }
  editUserDialogVisible.value = true
}

const saveUserInfo = () => {
  Object.assign(userInfo.value, editUserForm.value)
  ElMessage.success('资料已更新')
  editUserDialogVisible.value = false
}

const detailDialogVisible = ref(false)
const detailItem = ref(null)

const openDetailDialog = (type, row) => {
  detailItem.value = row
  detailDialogVisible.value = true
}

const noteDialogVisible = ref(false)
const noteContent = ref('')
let currentNoteItem = null

const openNoteDialog = (type, row) => {
  currentNoteItem = row
  noteContent.value = ''
  noteDialogVisible.value = true
}

const submitNote = () => {
  if (currentNoteItem) {
    currentNoteItem.note = noteContent.value
    messageList.value.unshift({
      id: Date.now(),
      content: `您的物品(${currentNoteItem.name})有留言消息，请查收`
    })
    ElMessage.success('留言成功')
  }
  noteDialogVisible.value = false
}

const reportDialogVisible = ref(false)
const reportReason = ref('')
let reportTarget = null

const openReportDialog = (row) => {
  reportTarget = row
  reportReason.value = ''
  reportDialogVisible.value = true
}

const submitReport = () => {
  ElMessage.success(`已举报"${reportTarget.name}"，理由：${reportReason.value}`)
  reportDialogVisible.value = false
}

const deleteDialogVisible = ref(false)
let deleteItemId = null
let deleteItemType = ''

const openDeleteDialog = (type, id) => {
  deleteItemType = type
  deleteItemId = id
  deleteDialogVisible.value = true
}

const confirmDelete = () => {
  if (deleteItemType === 'lost') {
    const idx = myLostList.value.findIndex(i => i.id === deleteItemId)
    if (idx !== -1) myLostList.value.splice(idx, 1)
    const allIdx = allLostList.value.findIndex(i => i.id === deleteItemId)
    if (allIdx !== -1) allLostList.value.splice(allIdx, 1)
  } else {
    const idx = myFoundList.value.findIndex(i => i.id === deleteItemId)
    if (idx !== -1) myFoundList.value.splice(idx, 1)
    const allIdx = allFoundList.value.findIndex(i => i.id === deleteItemId)
    if (allIdx !== -1) allFoundList.value.splice(allIdx, 1)
  }
  ElMessage.success('删除成功')
  deleteDialogVisible.value = false
}

const editItemDialogVisible = ref(false)
const editItemForm = ref({})
let editingItem = null
let editingItemType = ''

const openEditItemDialog = (type, row) => {
  editingItemType = type
  editingItem = row
  editItemForm.value = { ...row }
  editItemDialogVisible.value = true
}

const saveItemEdit = () => {
  Object.assign(editingItem, editItemForm.value)
  if (editingItemType === 'lost') {
    const all = allLostList.value.find(i => i.id === editingItem.id)
    if (all) Object.assign(all, editItemForm.value)
  } else {
    const all = allFoundList.value.find(i => i.id === editingItem.id)
    if (all) Object.assign(all, editItemForm.value)
  }
  ElMessage.success('修改成功')
  editItemDialogVisible.value = false
}

// 聊天相关
const currentChatUser = ref(null)
const newMessage = ref('')

const selectChatUser = (user) => {
  currentChatUser.value = user
  if (!chatMessages.value[user.id]) {
    chatMessages.value[user.id] = []
  }
}

const sendMessage = () => {
  if (!newMessage.value.trim() || !currentChatUser.value) return
  const msg = { senderId: currentUserId.value, content: newMessage.value }
  chatMessages.value[currentChatUser.value.id].push(msg)
  // 模拟回复
  setTimeout(() => {
    const reply = {
      senderId: currentChatUser.value.id,
      content: '收到你的消息了'
    }
    chatMessages.value[currentChatUser.value.id].push(reply)
  }, 500)
  newMessage.value = ''
}

// 修改密码
const changePwdDialogVisible = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', confirmPwd: '' })

const openChangePasswordDialog = () => {
  pwdForm.value = { oldPwd: '', newPwd: '', confirmPwd: '' }
  changePwdDialogVisible.value = true
}

const changePassword = () => {
  if (pwdForm.value.newPwd !== pwdForm.value.confirmPwd) {
    ElMessage.error('两次密码不一致')
    return
  }
  ElMessage.success('密码修改成功')
  changePwdDialogVisible.value = false
}

// 宿舍绑定
const bindDorm = () => {
  if (dormNumber.value) {
    currentDorm.value = dormNumber.value
    ElMessage.success(`宿舍已绑定：${dormNumber.value}`)
    dormNumber.value = ''
  } else {
    ElMessage.warning('请输入房间号')
  }
}

// 退出登录
const handleLogout = () => {

  router.push('/login')
  ElMessage.success('已退出登录')

}

// 头像上传
const handleAvatarChange = (file) => {
  userInfo.value.avatar = URL.createObjectURL(file.raw)
  ElMessage.success('头像已更新')
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.user-home {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #f0f2f5 100%);
}

.sidebar {
  width: 280px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 0 24px 24px 0;
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  height: 100vh;
}

.sidebar-header {
  padding: 32px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.avatar-container h3 {
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #5a6874;
  font-weight: 500;
  position: relative;
}

.nav-item:hover {
  background: #f8f9fc;
  color: #409eff;
}

.nav-item.active {
  background: linear-gradient(135deg, #e8f0fe 0%, #d4e4fc 100%);
  color: #409eff;
  font-weight: 600;
}

.nav-item .el-icon {
  font-size: 20px;
}

.badge {
  position: absolute;
  right: 16px;
}

.sidebar-footer {
  padding: 20px 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.main-content {
  flex: 1;
  padding: 28px 32px;
  overflow-y: auto;
}

.content-panel {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  flex-wrap: wrap;
  gap: 16px;
}

.panel-header h2 {
  font-size: 26px;
  font-weight: 600;
  background: linear-gradient(135deg, #2c3e50, #3498db);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.filter-bar {
  display: flex;
  gap: 12px;
}

.profile-card {
  background: white;
  border-radius: 24px;
  padding: 32px;
  display: flex;
  gap: 40px;
  margin-bottom: 24px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.04);
}

.profile-avatar {
  text-align: center;
}

.change-avatar-btn {
  margin-top: 12px;
  display: block;
}

.profile-info {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px 32px;
}

.info-row {
  display: flex;
  align-items: baseline;
  padding: 8px 0;
  border-bottom: 1px dashed #eef2f6;
}

.info-row .label {
  width: 70px;
  font-weight: 600;
  color: #7f8c8d;
}

.info-row .value {
  color: #2c3e50;
  font-weight: 500;
}

.dorm-card {
  background: linear-gradient(135deg, #fff9f0 0%, #fff5e6 100%);
  border-radius: 24px;
  padding: 28px 32px;
  border: 1px solid #ffe4c4;
}

.dorm-card h3 {
  font-size: 20px;
  color: #e67e22;
  margin-bottom: 20px;
}

.dorm-form {
  display: flex;
  align-items: flex-end;
  gap: 24px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.form-group label {
  font-weight: 500;
  color: #5a6874;
}

.current-dorm {
  padding: 8px 16px;
  background: #fff0e0;
  border-radius: 20px;
  font-size: 14px;
}

.current-dorm strong {
  color: #e67e22;
  font-size: 18px;
  margin-left: 8px;
}

.dorm-tip {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #ffe0b5;
  color: #b8860b;
  font-style: italic;
  font-size: 14px;
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.item-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.item-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.1);
}

.item-image {
  height: 160px;
  background: #f8f9fc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.item-image :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  font-size: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f0f2f5;
}

.item-info {
  padding: 16px;
}

.item-info h4 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #2c3e50;
}

.item-info .location,
.item-info .time {
  font-size: 13px;
  color: #8e9eae;
  margin: 4px 0;
}

.item-actions {
  display: flex;
  gap: 16px;
  margin-top: 14px;
  padding-top: 12px;
  border-top: 1px solid #eef2f6;
}

.add-item-card {
  background: white;
  border-radius: 24px;
  padding: 32px;
}

.type-switch {
  margin-bottom: 28px;
  display: flex;
  justify-content: center;
}

.add-form {
  max-width: 500px;
  margin: 0 auto;
}

.upload-tip {
  margin-left: 12px;
  color: #67c23a;
  font-size: 13px;
}

.message-container {
  display: flex;
  background: white;
  border-radius: 24px;
  overflow: hidden;
  min-height: 500px;
}

.message-sidebar {
  width: 160px;
  background: #fafbfc;
  border-right: 1px solid #eef2f6;
  padding: 20px 0;
}

.msg-tab {
  padding: 14px 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.2s;
  color: #5a6874;
}

.msg-tab.active {
  background: linear-gradient(90deg, #e8f0fe, transparent);
  color: #409eff;
  border-right: 3px solid #409eff;
}

.message-content {
  flex: 1;
  padding: 20px;
}

.note-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.note-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  background: #f8f9fc;
  border-radius: 14px;
  color: #5a6874;
}

.empty-msg {
  text-align: center;
  color: #bdc3c7;
  padding: 60px 0;
}

.chat-area {
  display: flex;
  height: 500px;
}

.chat-user-list {
  width: 200px;
  border-right: 1px solid #eef2f6;
  overflow-y: auto;
}

.chat-user-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background 0.2s;
}

.chat-user-item:hover,
.chat-user-item.active {
  background: #f0f7ff;
}

.chat-conversation {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 16px;
  border-bottom: 1px solid #eef2f6;
  font-weight: 600;
  background: #fafbfc;
}

.chat-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #bdc3c7;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-bubble {
  max-width: 75%;
  padding: 10px 14px;
  border-radius: 18px;
  background: #f0f2f5;
  align-self: flex-start;
  font-size: 14px;
  word-break: break-word;
}

.message-bubble.self {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: white;
  align-self: flex-end;
}

.chat-input-area {
  display: flex;
  gap: 12px;
  padding: 16px;
  border-top: 1px solid #eef2f6;
  background: white;
}

.custom-dialog :deep(.el-dialog) {
  border-radius: 20px;
}

.detail-content p {
  margin: 12px 0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .sidebar {
    width: 80px;
  }
  .sidebar .avatar-container h3,
  .sidebar .nav-item span {
    display: none;
  }
  .main-content {
    padding: 16px;
  }
  .profile-card {
    flex-direction: column;
  }
  .profile-info {
    grid-template-columns: 1fr;
  }
}
</style>
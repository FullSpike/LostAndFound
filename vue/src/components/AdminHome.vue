<template>
  <div class="admin-home">
    <!-- 侧边栏导航 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="avatar-container">
          <el-avatar :size="64" :src="adminInfo.avatar || defaultAvatar" />
          <h3>{{ adminInfo.username }}</h3>
          <p class="role-tag">管理员</p>
        </div>
      </div>
      <nav class="sidebar-nav">
        <div class="nav-item" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">
          <el-icon><User /></el-icon>
          <span>个人资料</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'users' }" @click="activeTab = 'users'">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'lost' }" @click="activeTab = 'lost'">
          <el-icon><Search /></el-icon>
          <span>失物管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'found' }" @click="activeTab = 'found'">
          <el-icon><TakeawayBox /></el-icon>
          <span>拾取物管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'topApply' }" @click="activeTab = 'topApply'">
          <el-icon><Top /></el-icon>
          <span>失物置顶申请</span>
          <el-badge v-if="topApplyCount" :value="topApplyCount" class="badge" />
        </div>
        <div class="nav-item" :class="{ active: activeTab === 'aiChat' }" @click="activeTab = 'aiChat'">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI对话</span>
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
          <el-button type="primary" plain @click="openEditAdminDialog">编辑资料</el-button>
        </div>
        <div class="profile-card">
          <div class="profile-avatar">
            <el-avatar :size="100" :src="adminInfo.avatar || defaultAvatar" />
            <el-form-item label="更改头像">

              <input type="file" @change="handleAvatarChange" accept="image/*" />
              <el-button type="primary" @click="uploadAvatar">上传</el-button>
            </el-form-item>
          </div>
          <div class="profile-info">
            <div class="info-row"><span class="label">管理员ID</span><span class="value">{{ adminInfo.id }}</span></div>
            <div class="info-row"><span class="label">用户名</span><span class="value">{{ adminInfo.username }}</span></div>
            <div class="info-row"><span class="label">邮箱</span><span class="value">{{ adminInfo.email }}</span></div>
            <div class="info-row"><span class="label">手机号</span><span class="value">{{ adminInfo.phone }}</span></div>
            <div class="info-row"><span class="label">角色</span><span class="value"><el-tag type="danger">管理员</el-tag></span></div>
          </div>
        </div>

      </div>

      <!-- 用户管理 -->
      <div v-if="activeTab === 'users'" class="content-panel">
        <div class="panel-header">
          <h2>用户管理</h2>
          <div class="filter-bar">
            <el-select v-model="userStatusFilter" placeholder="状态筛选" clearable style="width: 120px">
              <el-option label="全部" value="" />
              <el-option label="正常" value="正常" />
              <el-option label="封禁" value="封禁" />
            </el-select>
          </div>
        </div>
        <div class="table-wrapper">
          <el-table :data="filteredUserList" stripe class="data-table">
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column label="头像" width="70">
              <template #default="{ row }">
                <el-avatar :size="36" :src="row.avatar || defaultAvatar" />
              </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '正常' ? 'success' : 'danger'">
                  {{ row.status === '正常' ? '正常' : '封禁' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button
                    v-if="row.status === '封禁'"
                    type="success"
                    size="small"
                    @click="toggleUserStatus(row)"
                >解封</el-button>
                <el-button
                    v-else
                    type="danger"
                    size="small"
                    @click="toggleUserStatus(row)"
                >封禁</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 失物管理 -->
      <div v-if="activeTab === 'lost'" class="content-panel">
        <div class="panel-header">
          <h2>失物管理</h2>
          <div class="filter-bar">
            <el-input v-model="lostFilters.search" placeholder="按名称搜索" clearable prefix-icon="Search" style="width: 160px" />
            <el-input v-model="lostFilters.location" placeholder="按地点筛选" clearable style="width: 160px" />
            <el-select v-model="lostFilters.is_report" placeholder="是否被举报" clearable style="width: 130px">
              <el-option label="全部" value="" />
              <el-option label="已举报" value="是" />
              <el-option label="未举报" value="否" />
            </el-select>
            <el-select v-model="lostFilters.sort" placeholder="时间排序" clearable style="width: 130px">
              <el-option label="最新优先" value="desc" />
              <el-option label="最早优先" value="asc" />
            </el-select>
          </div>
        </div>
        <div class="item-grid">
          <div v-for="item in filteredLostList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover"
                        @click="openImagePreview=true,previewimgurl=item.pathName || defaultImage">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="report-badge" :class="{ reported: item.is_report === '是' }">
                {{ item.is_report === '是' ? '⚠️ 已举报' : '✓ 未举报' }}
              </div>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('lost', item)">详情</el-button>
                <el-button link type="danger" @click="openDeleteDialog('lost', item.id)">删除</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 拾取物管理 -->
      <div v-if="activeTab === 'found'" class="content-panel">
        <div class="panel-header">
          <h2>拾取物管理</h2>
          <div class="filter-bar">
            <el-input v-model="foundFilters.search" placeholder="按名称搜索" clearable prefix-icon="Search" style="width: 160px" />
            <el-input v-model="foundFilters.location" placeholder="按地点筛选" clearable style="width: 160px" />
            <el-select v-model="foundFilters.is_report" placeholder="是否被举报" clearable style="width: 130px">
              <el-option label="全部" value="" />
              <el-option label="已举报" value="是" />
              <el-option label="未举报" value="否" />
            </el-select>
            <el-select v-model="foundFilters.sort" placeholder="时间排序" clearable style="width: 130px">
              <el-option label="最新优先" value="desc" />
              <el-option label="最早优先" value="asc" />
            </el-select>
          </div>
        </div>
        <div class="item-grid">
          <div v-for="item in filteredFoundList" :key="item.id" class="item-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover"
                        @click="openImagePreview=true,previewimgurl=item.pathName || defaultImage">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="report-badge" :class="{ reported: item.is_report === '是' }">
                {{ item.is_report === '是' ? '⚠️ 已举报' : '✓ 未举报' }}
              </div>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('found', item)">详情</el-button>
                <el-button link type="danger" @click="openDeleteDialog('found', item.id)">删除</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 失物置顶申请 -->
      <div v-if="activeTab === 'topApply'" class="content-panel">
        <div class="panel-header">
          <h2>失物置顶申请</h2>
          <div class="filter-bar">
            <el-input v-model="topApplyFilters.search" placeholder="按名称搜索" clearable prefix-icon="Search" style="width: 200px" />
          </div>
        </div>
        <div class="item-grid">
          <div v-for="item in filteredTopApplyList" :key="item.id" class="item-card apply-card">
            <div class="item-image">
              <el-image :src="item.pathName || defaultImage" fit="cover"
                        @click="openImagePreview=true,previewimgurl=item.pathName || defaultImage">
                <template #error><div class="image-placeholder">📷</div></template>
              </el-image>
            </div>
            <div class="item-info">
              <h4>{{ item.name }}</h4>
              <p class="location">📍 {{ item.location }}</p>
              <p class="time">🕒 {{ item.time }}</p>
              <div class="item-actions">
                <el-button link type="primary" @click="openDetailDialog('lost', item)">详情</el-button>
                <el-button type="success" size="small" @click="approveTopApply(item)">同意置顶</el-button>
              </div>
            </div>
          </div>
          <div v-if="filteredTopApplyList.length === 0" class="empty-state">
            <el-empty description="暂无置顶申请" />
          </div>
        </div>
      </div>

      <!-- AI对话 -->
      <div v-if="activeTab === 'aiChat'" class="content-panel">
        <div class="panel-header">
          <h2>AI对话助手</h2>
        </div>
        <div class="ai-chat-container">
          <div class="chat-messages-area" ref="chatMessagesRef">
            <div v-for="(msg, idx) in aiMessages" :key="idx" class="message-bubble" :class="{ self: msg.role === 'user' }">
              <span class="message-sender">{{ msg.role === 'user' ? 'self' : 'ai' }}:</span>
              <span class="message-content">{{ msg.content }}</span>
            </div>
            <div v-if="aiLoading" class="message-bubble ai">
              <span class="message-sender">ai:</span>
              <span class="message-content typing">正在思考中...</span>
            </div>
          </div>
          <div class="chat-input-area">
            <el-input
                v-model="aiInput"
                placeholder="输入消息，按Enter发送..."
                @keyup.enter="sendAIMessage"
                :disabled="aiLoading"
            />
            <el-button type="primary" @click="sendAIMessage" :loading="aiLoading">发送</el-button>
          </div>
        </div>
      </div>
    </main>

    <!-- 弹窗区域 -->
    <!-- 编辑管理员资料弹窗 -->
    <el-dialog v-model="editAdminDialogVisible" title="编辑资料" width="480px" class="custom-dialog">
      <el-form :model="editAdminForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editAdminForm.username" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editAdminForm.email" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editAdminForm.phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editAdminDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAdminInfo">保存</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
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

    <!-- 详情弹窗 - 包含举报理由 -->
    <el-dialog v-model="detailDialogVisible" title="物品详情" width="500px">
      <div v-if="detailItem" class="detail-content">
        <p><strong>名称：</strong>{{ detailItem.name }}</p>
        <p><strong>地点：</strong>{{ detailItem.location }}</p>
        <p><strong>时间：</strong>{{ detailItem.time }}</p>
        <p><strong>描述：</strong>{{ detailItem.description }}</p>
        <p><strong>状态：</strong>{{ detailItem.status }}</p>
        <p><strong>联系电话：</strong>{{ detailItem.phone || '未提供' }}</p>
        <p v-if="detailItem.reportReason"><strong>举报理由：</strong>{{ detailItem.reportReason }}</p>
        <p><strong>留言：</strong>{{ detailItem.note || '暂无' }}</p>
      </div>
    </el-dialog>

    <!-- 删除确认弹窗 -->
    <el-dialog v-model="deleteDialogVisible" title="删除确认" width="360px">
      <p>确定要删除该物品吗？此操作不可恢复。</p>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDelete">确认删除</el-button>
      </template>
    </el-dialog>

    <!-- 图片预览弹窗 -->
    <el-dialog v-model="openImagePreview" title="图片预览" width="600px">
      <div class="image-preview">
        <img :src="previewimgurl" alt="预览图片" style="width: 100%" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import { ElMessage } from 'element-plus'
import { User, UserFilled, Search, TakeawayBox, Top, ChatDotRound, Lock, SwitchButton } from '@element-plus/icons-vue'
import request from "@/utils/request.js";
import router from "@/router/index.js";


onMounted(async () => {
  //初始化所有用户数据
  try {
    await request.get('/users').then(res => {
      if(res.code === '200'){
        userList.value = res.data
      }else {
        ElMessage.error('初始化用户数据失败')
      }
    })
  }catch(error) {
    console.log(error)
    ElMessage.error('初始化用户数据失败')
  }


  //初始化列表，并将置顶物品添加到列表顶部

  try {

    await request.get('/losts/top').then(response => {
      if(response.code === '200'){
        topLostList.value= response.data
        lostList.value=lostList.value.concat(topLostList.value)
      }
    })
    await request.get('/losts').then(response => {
      if(response.code === '200'){
        lostList0.value = response.data
        lostList.value=lostList.value.concat(lostList0.value)
      }
    })


    await request.get('/founds').then(response => {
      if(response.code === '200'){
        foundList0.value= response.data
        foundList.value = [...foundList0.value]
      }
    })

  }catch(err) {
    ElMessage.error('初始化失败')
  }

  //可以不需要刷新
  //刷新AI对话消息
  try {
    await request.get('/aiMessages/'+adminInfo.value.id).then(response => {
      if(response.code === '200'){
        aiMessages.value = response.data
      }else {
        ElMessage.error('刷新AI对话消息失败')
      }
    })
  }catch(error) {
    console.log(error)
    ElMessage.error('刷新AI对话消息失败')
  }

})



// ==================== 管理员信息 ====================
const adminInfo = ref(JSON.parse(localStorage.getItem('user')))


// ==================== 模拟数据 ====================

// 用户列表
const userList = ref([])

//暂存物品数据
const topLostList=ref([])
const lostList0=ref([])
const foundList0=ref([])
// 失物列表 - 新增 is_report, report_reason, need_top, username
const lostList = ref([])

// 拾取物列表 - 新增 is_report, report_reason
const foundList = ref([])

// AI对话消息
const aiMessages = ref([])

// ==================== UI状态 ====================
const activeTab = ref('profile')
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const defaultImage = 'https://via.placeholder.com/120'


// 图片预览
let openImagePreview = ref(false)
let previewimgurl = ref('')

// 用户筛选
const userStatusFilter = ref('')
const filteredUserList = computed(() => {
  let list = [...userList.value]
  if (userStatusFilter.value) {
    list = list.filter(u => u.status === userStatusFilter.value)
  }
  return list
})

// 失物筛选
const lostFilters = ref({ search: '', location: '', is_report: '', sort: '' })
const filteredLostList = computed(() => {
  let list = [...lostList.value]
  if (lostFilters.value.search) {
    list = list.filter(i => i.name.includes(lostFilters.value.search))
  }
  if (lostFilters.value.location) {
    list = list.filter(i => i.location.includes(lostFilters.value.location))
  }
  if (lostFilters.value.is_report !== '') {
    list = list.filter(i => i.is_report === (lostFilters.value.is_report))
  }
  if (lostFilters.value.sort === 'asc') {
    list.sort((a, b) => new Date(a.time) - new Date(b.time))
  } else if (lostFilters.value.sort === 'desc') {
    list.sort((a, b) => new Date(b.time) - new Date(a.time))
  }
  return list
})

// 拾取物筛选
const foundFilters = ref({ search: '', location: '', is_report: '', sort: '' })
const filteredFoundList = computed(() => {
  let list = [...foundList.value]
  if (foundFilters.value.search) {
    list = list.filter(i => i.name.includes(foundFilters.value.search))
  }
  if (foundFilters.value.location) {
    list = list.filter(i => i.location.includes(foundFilters.value.location))
  }
  if (foundFilters.value.is_report !== '') {
    list = list.filter(i => i.is_report === (foundFilters.value.is_report))
  }
  if (foundFilters.value.sort === 'asc') {
    list.sort((a, b) => new Date(a.time) - new Date(b.time))
  } else if (foundFilters.value.sort === 'desc') {
    list.sort((a, b) => new Date(b.time) - new Date(a.time))
  }
  return list
})

// 置顶申请 - 筛选 need_top === '是' 的失物
const topApplyFilters = ref({ search: '' })
const topApplyList = computed(() => {
  return lostList.value.filter(i => i.need_top === '是')
})
const topApplyCount = computed(() => topApplyList.value.length)

const filteredTopApplyList = computed(() => {
  let list = [...topApplyList.value]
  if (topApplyFilters.value.search) {
    list = list.filter(i => i.name.includes(topApplyFilters.value.search))
  }
  return list
})

// ==================== 管理员个人资料方法 ====================
const editAdminDialogVisible = ref(false)
const editAdminForm = ref({})

const openEditAdminDialog = () => {
  editAdminForm.value = { ...adminInfo.value }
  editAdminDialogVisible.value = true
}

const saveAdminInfo = () => {
  Object.assign(adminInfo.value, editAdminForm.value)
  //与后端进行交互
  request.put('/admins/'+adminInfo.value.id,editAdminForm.value,{
    contentType: 'application/json'
  }).then(response => {
    if(response.code === '200'){
      ElMessage.success('资料已更新')
    }else {
      ElMessage.error(response.msg||'更新失败')
    }

  })
  editAdminDialogVisible.value = false
}


const avatarFile = ref(null)

const handleAvatarChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    avatarFile.value = file
  }
}

const uploadAvatar = () => {
  const formData = new FormData()
  formData.append('avatar',avatarFile.value)
  request.put('/admins/'+adminInfo.value.id+'/avatar',formData).then(response => {
    if(response.code === '200'){
      ElMessage.success('头像已更新')
      adminInfo.value.avatar=response.data
    }else {
      ElMessage.error(response.msg||'头像更新失败')
    }
  }).catch(error => {
    ElMessage.error('网络错误')
  })
}

// 修改密码
const changePwdDialogVisible = ref(false)
const pwdForm = ref({ oldPwd: '', newPwd: '', confirmPwd: '' })

const openChangePasswordDialog = () => {
  pwdForm.value = { oldPwd: '', newPwd: '', confirmPwd: '' }
  changePwdDialogVisible.value = true
}

const changePassword = async () => {
  const params1 = new URLSearchParams()
  params1.append('password',pwdForm.value.oldPwd)
  const response0 = await request.post('/admins/'+adminInfo.value.id+'/password',params1)
  if(response0.code !== '200'){
    ElMessage.error(response0.msg||'旧密码错误')
    return
  }

  if(!pwdForm.value.newPwd || !pwdForm.value.confirmPwd){
    ElMessage.error('请输入新密码')
    return
  }
  if (pwdForm.value.newPwd !== pwdForm.value.confirmPwd) {
    ElMessage.error('两次密码不一致')
    return
  }

  // 密码确认验证
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/
  if (!passwordRegex.test(pwdForm.value.newPwd)) {
    ElMessage.error('密码必须包含字母和数字，且长度在6-20位之间')
    return
  }

  const params = new URLSearchParams()
  params.append('password',pwdForm.value.newPwd)
  const response = await request.put('/admins/'+adminInfo.value.id+'/password',params)
  if(response.code === '200'){
    ElMessage.success('密码修改成功')
  }else {
    ElMessage.error(response.msg||'密码修改失败')
  }





  changePwdDialogVisible.value = false
}



// ==================== 用户管理方法 ====================
const toggleUserStatus = (user) => {
  if (user.status === '正常') {
    user.status = '封禁'
    try {
      request.put('/users/'+user.id+'/status').then(response => {
        if(response.code === '200'){
          ElMessage.success(`用户 ${user.username} 已封禁`)
        }else {
          ElMessage.error('封禁失败')
        }
      })
    }catch (error) {
      ElMessage.error('封禁失败')
    }
  } else {
    user.status = '正常'
    try {
      request.put('/users/'+user.id+'/status').then(response => {
        if(response.code === '200'){
          ElMessage.success(`用户 ${user.username} 已解封`)
        }else {
          ElMessage.error('解封失败')
        }
      })
    }catch (error) {
      ElMessage.error('解封失败')
    }
  }
}

// ==================== 物品管理方法 ====================

/*
* 物品详情弹窗
* */
const detailDialogVisible = ref(false)
const detailItem = ref(null)

const openDetailDialog = (type, row) => {
  detailItem.value = { ...row }
  detailDialogVisible.value = true
}


/*
* 物品删除弹窗
* */
const deleteDialogVisible = ref(false)
let deleteItemId = null
let deleteItemType = null

const openDeleteDialog = (type, id) => {
  deleteItemType = type
  deleteItemId = id
  deleteDialogVisible.value = true
}

const confirmDelete = () => {
  if (deleteItemType === 'lost') {
    const index = lostList.value.findIndex(i => i.id === deleteItemId)
    if (index !== -1) {
      lostList.value.splice(index, 1)
      //删除失物信息
      try {
        request.delete('/losts/' + deleteItemId).then(response => {
          if (response.code === '200') {
            ElMessage.success('失物已删除')
          } else {
            ElMessage.error(response.msg || '删除失败')
          }
        })
      } catch (error) {
        ElMessage.error('删除失败')
      }

    }
  } else if (deleteItemType === 'found') {
    const index = foundList.value.findIndex(i => i.id === deleteItemId)
    if (index !== -1) {
      foundList.value.splice(index, 1)
      try {
        request.delete('/founds/' + deleteItemId).then(response => {
          if (response.code === '200') {
            ElMessage.success('拾取物已删除')
          } else {
            ElMessage.error(response.msg || '删除失败')
          }
        })
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }
  }
  deleteDialogVisible.value = false
}

// ==================== 置顶申请方法 ====================
const approveTopApply = (row) => {
  const index = lostList0.value.findIndex(i => i.id === row.id)
  row.need_top = '无'
  row.is_top = '是'
  if (index !== -1) {
    lostList0.value.splice(index, 1)
    topLostList.value.push(row)
    try {
      request.put('/losts/'+row.id+'/toTop').then(response => {
        if(response.code === '200'){
          ElMessage.success('置顶成功')
        }else {
          ElMessage.error('置顶失败')
        }
      })
    }catch (error) {
      ElMessage.error('置顶失败')
    }
  }
}

// ==================== AI对话方法 ====================
const aiInput = ref('')
const aiLoading = ref(false)
const chatMessagesRef = ref(null)

const getAIResponse = async (userMessage) => {
  //发出请求，获得ai的回复

  const params = new URLSearchParams()
  params.append('userMessage',userMessage)

  try {
    const response = await request.post('/aiMessages/'+adminInfo.value.id,params)
    if(response.code === '200'){
      return response.data
    }else {
      ElMessage.error(response.msg||'获取AI回复失败')
    }
  }catch (error) {
    ElMessage.error('获取AI回复失败')
    return ''
  }

}

const sendAIMessage = async () => {
  if (!aiInput.value.trim() || aiLoading.value) return

  const userMessage = aiInput.value.trim()
  aiMessages.value.push({ role: 'user', content: userMessage })
  aiInput.value = ''

  aiLoading.value = true

  setTimeout(async () => {
    const aiResponse = await getAIResponse(userMessage)
    aiMessages.value.push({ role: 'ai', content: aiResponse })
    aiLoading.value = false

    setTimeout(() => {
      if (chatMessagesRef.value) {
        const container = chatMessagesRef.value
        container.scrollTop = container.scrollHeight
      }
    }, 100)
  }, 800)
}

// ==================== 退出登录 ====================
const handleLogout = () => {
  router.push('/Login')
  ElMessage.success('已退出登录')
  localStorage.removeItem('user')
  localStorage.removeItem('token')
}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.admin-home {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #f0f2f5 100%);
}

/* 侧边栏样式 */
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

.role-tag {
  margin-top: 6px;
  font-size: 12px;
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
  display: inline-block;
  padding: 2px 12px;
  border-radius: 20px;
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

/* 主内容区 */
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

/* 个人资料卡片 */
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
  width: 90px;
  font-weight: 600;
  color: #7f8c8d;
}

.info-row .value {
  color: #2c3e50;
  font-weight: 500;
}

/* 宿舍卡片 */
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

/* 表格包装器 */
.table-wrapper {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
}

.data-table {
  width: 100%;
}

/* 物品网格 */
.item-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
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

.apply-card {
  border-left: 4px solid #e67e22;
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
.item-info .time,
.item-info .publisher {
  font-size: 13px;
  color: #8e9eae;
  margin: 4px 0;
}

.report-badge {
  font-size: 12px;
  margin: 8px 0;
  padding: 4px 8px;
  border-radius: 12px;
  display: inline-block;
  background: #f0f2f5;
  color: #67c23a;
}

.report-badge.reported {
  background: #fef0f0;
  color: #f56c6c;
}

.item-actions {
  display: flex;
  gap: 16px;
  margin-top: 14px;
  padding-top: 12px;
  border-top: 1px solid #eef2f6;
}

/* AI对话区域 */
.ai-chat-container {
  background: white;
  border-radius: 24px;
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.chat-messages-area {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-bubble {
  max-width: 70%;
  padding: 12px 18px;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-bubble.self {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: white;
  align-self: flex-end;
  border-bottom-right-radius: 4px;
}

.message-bubble:not(.self) {
  background: #f0f2f5;
  color: #1a2a3a;
  align-self: flex-start;
  border-bottom-left-radius: 4px;
}

.message-sender {
  font-size: 12px;
  font-weight: 600;
  opacity: 0.8;
}

.message-bubble.self .message-sender {
  color: rgba(255, 255, 255, 0.9);
}

.message-content {
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.typing::after {
  content: '...';
  animation: typing 1.5s infinite;
  display: inline-block;
  width: 24px;
}

@keyframes typing {
  0%, 20% { content: '.'; }
  40%, 60% { content: '..'; }
  80%, 100% { content: '...'; }
}

.chat-input-area {
  display: flex;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #eef2f6;
  background: white;
}

.chat-input-area .el-input {
  flex: 1;
}

/* 详情弹窗 */
.detail-content {
  line-height: 1.8;
}

.detail-content p {
  margin: 12px 0;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.detail-content strong {
  width: 100px;
  color: #475569;
}

/* 空状态 */
.empty-state {
  padding: 60px 0;
  text-align: center;
}

/* 响应式 */
@media (max-width: 768px) {
  .sidebar {
    width: 80px;
  }
  .sidebar .avatar-container h3,
  .sidebar .role-tag,
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
  .message-bubble {
    max-width: 85%;
  }
  .filter-bar {
    width: 100%;
  }
  .item-grid {
    grid-template-columns: 1fr;
  }
}

.custom-dialog :deep(.el-dialog) {
  border-radius: 20px;
}
</style>
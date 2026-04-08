<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户/管理员登录系统</h2>
      <el-form>
        <el-form-item label="邮箱/手机号">
          <el-input placeholder="请输入邮箱/手机号" v-model="number" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" placeholder="请输入密码" v-model="password" show-password />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-radio-group v-model="userType">
            <el-radio label="user">用户</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button type="info" @click="$router.push('/Register')">注册</el-button>
        </el-form-item>
      </el-form>
      <p>默认测试账号：3125000000 / AB123456</p>
    </div>
  </div>
</template>

<script>

import { ElMessage, ElNotification } from 'element-plus'
import axios from 'axios'



export default {
  name: 'Login',

  data() {
    return {
      number: '',
      password: '',
      userType: 'user',
      loading: false,
      errorMsg: ''
    }
  },
  methods: {

    async handleLogin() {
      // 简单验证
      if (!this.number || !this.password) {
        this.errorMsg = '请输入邮箱/手机号和密码'
        ElMessage.error(this.errorMsg)
        return
      }



      try {
        const params = new URLSearchParams()
        params.append('emailOrPhone', this.number)
        params.append('password', this.password)

        const response = await axios.post('http://localhost:8081/login', params, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        if (response.data.code === "200") {
          // 保存token
          localStorage.setItem('token', response.data.data.token)
          localStorage.setItem('user', JSON.stringify(response.data.data))

          // 跳转到首页
          if(this.userType === 'user'&&'status' in response.data.data) {
            //判断账户是否被封禁
            if(response.data.data.status === '正常'){
              this.$router.push('/userHome')
              ElMessage.success('登录成功！')
            }else{
              ElMessage.error('账户已被封禁')
            }
          }else if(this.userType === 'admin'&&!'status' in response.data.data){
            this.$router.push('/adminHome')
            ElMessage.success('登录成功！')
          }else{
            ElMessage.error('用户类型错误')
          }

        } else {
          this.errorMsg = response.data.msg || '登录失败'
          ElMessage.error(this.errorMsg)
        }
      } catch (error) {
        this.errorMsg = '网络错误，请稍后重试'
        ElMessage.error(this.errorMsg)
      } finally {
        this.loading = false
      }
    }
  }
}






/*const handleLogin = () => {
  if (studentId.value === '2024001' && password.value === '123456') {
    ElMessage.success('登录成功！')
    // 登录成功后，跳转到欢迎页面

  } else {
    ElMessage.error('学号或密码错误')
  }
}*/



</script>

<style scoped>
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
      rgb(65, 168, 82),
      rgba(34, 0, 255, 0.5)
  );
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.login-form {
  max-width: 500px;
  width: 90%;
  padding: 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-form h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.login-form p {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

</style>

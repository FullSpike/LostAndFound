<template>
  <div class="register-container">
    <div class="register-form">
      <h2>注册系统</h2>
      <el-form>
        <el-form-item label="用户名">
          <el-input placeholder="请输入用户名" v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" placeholder="请输入密码" v-model="form.password" show-password />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input placeholder="请输入邮箱" v-model="form.email" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input placeholder="请输入电话" v-model="form.phone" />
        </el-form-item>
        <el-form-item label="用户类型">
          <el-radio-group v-model="form.userType">
            <el-radio label="user">用户</el-radio>
            <el-radio label="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button type="info" @click="$router.push('/Login')">返回</el-button>
        </el-form-item>
      </el-form>
      <p>默认测试账号：11@162.com / AB261216</p>
    </div>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

export default {
  name: 'Register',
  data() {
    return {
      form: {
        username: '',
        password: '',
        email: '',
        phone: '',
        userType: 'user',
      }
    }
  },
  methods: {
    handleRegister() {
      // 简单验证
      if (!this.form.username) {
        ElMessage.error('请输入用户名')
        return
      }
      if (!this.form.password) {
        ElMessage.error('请输入密码')
        return
      }
      if (!this.form.email) {
        ElMessage.error('请输入邮箱')
        return
      }
      if (!this.form.phone) {
        ElMessage.error('请输入电话')
        return
      }

      // 邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.form.email)) {
        ElMessage.error('请输入正确的邮箱格式')
        return
      }

      // 手机号简单验证
      const phoneRegex = /^1[3-9]\d{9}$/
      if (!phoneRegex.test(this.form.phone)) {
        ElMessage.error('请输入正确的11位手机号码')
        return
      }

      //开始注册
      if(this.form.userType === 'user') {
        // 注册用户
        try {
          request.post('/users', this.form).then(res=>{
            if(res.code === '200'){
              ElMessage.success('注册成功')
              // 跳转登录页
              this.$router.push('/login')
            }else{
              ElMessage.error(res.msg||'注册失败')
            }
          })
        }catch (error) {
          ElMessage.error('注册失败')
        }
      }else if(this.form.userType === 'admin') {
        // 注册管理员
        try {
          request.post('/admins', this.form).then(res=>{
            if(res.code === '200'){
              ElMessage.success('注册成功')
              // 跳转登录页
              this.$router.push('/login')
            }else{
              ElMessage.error(res.msg||'注册失败')
            }
          })
        }catch (error) {
          ElMessage.error('注册失败')
        }
      }else{
        ElMessage.error('用户类型错误')
      }



    }

  }
}
</script>

<style scoped>
.register-container {
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

.register-form {
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
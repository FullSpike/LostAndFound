import { createRouter, createWebHistory } from 'vue-router'



// 定义路由
const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/components/Login.vue'),
        meta: {requiresAuth: false}
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('@/components/Register.vue'),
        meta: {requiresAuth: false}
    },
    {
        path: '/userHome',
        name: 'userHome',
        component: () => import('@/components/UserHome.vue'),
        meta: {requiresAuth: true}
    },
    {
        path: '/adminHome',
        name: 'adminHome',
        component: () => import('@/components/AdminHome.vue'),
        meta: {requiresAuth: true}
    }



]

// 创建路由实例
const router = createRouter({
    history: createWebHistory(),
    routes
})

/*
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')

    // 需要登录的页面
    if (to.meta.requiresAuth) {
        if (!token) {
            // 未登录，跳转到登录页
            next({
                path: '/login',
                query: { redirect: to.fullPath }
            })
        } else {
            // 已登录，可以访问
            next()
        }
    }
})
*/





export default router
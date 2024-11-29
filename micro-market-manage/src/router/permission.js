import router from '@/router/index'
import { ElMessage } from 'element-plus'
const whiteList = ['/login']
router.beforeEach((to, from, next) => {
  const token = window.sessionStorage.getItem('token')
  console.log('token=' + token)
  console.log('to.path=' + to.path)
  if (token) {
    if (to.path === '/login') {
      // 如果是登录请求
      next('/') // 跳转后台管理页面
    } else {
      next() // 放行
    }
  } else {
    if (whiteList.includes(to.path)) {
      next() // 放行
    } else {
      ElMessage.error('登录状态过期,请重新登录')
      setTimeout(() => {
        next('/login') // 跳转登录页面
      }, 500)
    }
  }
})

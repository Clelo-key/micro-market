import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: '系统首页',
    component: () => import('../views/home/index.vue'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: '主页',
        component: () => import('../views/home/index/index.vue')
      },
      {
        path: '/bigType',
        name: '商品大类',
        component: () => import('../views/bigType/index')
      },
      {
        path: '/smallType',
        name: '商品小类',
        component: () => import('../views/smallType/index.vue')
      },
      {
        path: '/user',
        name: '用户管理',
        component: () => import('../views/user/index')
      },
      {
        path: '/modifyPassword',
        name: '系统管理',
        component: () => import('../views/modifyPassword/index')
      },
      {
        path: '/order',
        name: '订单管理',
        component: () => import('../views/order/index')
      },
      {
        path: '/product',
        name: '商品管理',
        component: () => import('../views/product/index')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login/index.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

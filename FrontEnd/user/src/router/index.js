import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import Home from '../components/Home.vue'
import Welcome from '../components/Welcome.vue'
import Approved from '../components/history/Approved.vue'
import History from '../components/history/History.vue'
import NotApproved from '../components/history/NotApproved.vue'
import Approving from '../components/history/Approving'
import Book from '../components/book/Book.vue'
import CancelBook from '../components/book/CancelBook.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
      path: '/home',
      component: Home,
      redirect: '/book',
      children: [
        { path: '/Welcome', component: Welcome },
        { path: '/approved', component: Approved },
        { path: '/history', component: History },
        { path: '/notApproved', component: NotApproved },
        { path: '/approving', component: Approving },
        { path: '/book', component: Book },
        { path: '/cancelBook', component: CancelBook }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 表示将要访问的路径
  // from 表示从哪个路径跳转而来
  // next 是一个函数，表示放行
  // next() 直接放行 next('/login') 强制跳转
  if (to.path === '/login') return next()
  // 获取token
  const tokenStr = window.sessionStorage.getItem('token')
  if (!tokenStr) return next('/login')
  next()
})

export default router

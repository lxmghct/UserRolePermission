import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login')
  }
]

export const asyncRoutes = [
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/Home'),
    permissions: ['TEST_MAIN']
  },
  {
    path: '/page1',
    name: 'Page1',
    component: () => import('@/views/Page1'),
    permissions: ['TEST_PAGE1']
  },
  {
    path: '/page2',
    name: 'Page2',
    component: () => import('@/views/Page2'),
    permissions: ['TEST_MAIN']
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router

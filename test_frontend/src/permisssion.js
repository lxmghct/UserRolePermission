import router, { asyncRoutes } from './router'
import { userStore, UserUtils } from './store/user'

function hasPermission (permissions, route) {
  if (route.permissions) {
    return permissions.some(permission => route.permissions.includes(permission))
  } else {
    return true
  }
}

function filterAsyncRoutes (routes, permissions) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(permissions, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, permissions)
      }
      res.push(tmp)
    }
  })

  return res
}

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  const loginInfo = sessionStorage.getItem('loginInformation')
  if (loginInfo) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/home' })
    } else {
      const hasPermission = userStore.permissions && userStore.permissions.length > 0
      if (hasPermission) {
        if (router.getRoutes().map(item => item.path).includes(to.path)) {
          next()
        } else {
          next({ path: '/login' })
        }
      } else {
        try {
          userStore.permissions = JSON.parse(loginInfo).user.permissions
          // generate accessible routes map
          const accessRoutes = filterAsyncRoutes(asyncRoutes, userStore.permissions)
          if (accessRoutes.length === 0 || userStore.permissions.length === 0) {
            throw new Error('No permission')
          }
          // dynamically add accessible routes
          accessRoutes.forEach(item => { router.addRoute(item) })

          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
        } catch (error) {
          sessionStorage.removeItem('loginInformation')
          UserUtils.resetUserStore()
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    /* not logged in */
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
    }
  }
})

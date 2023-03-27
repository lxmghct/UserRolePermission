import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param permissions
 * @param route
 */
function hasPermission(permissions, route) {
  if (route.meta && route.meta.permissions) {
    return permissions.some(permission => route.meta.permissions.includes(permission))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param permissions
 */
export function filterAsyncRoutes(routes, permissions) {
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

/**
 * Filter asynchronous routing tables in required list by recursion
 * @param routes asyncRoutes
 * @param roles
 * @param base
 */
export function filterAsyncRoutesInRequiredList(routes, routeList, base) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    let path = tmp.path.replace(/^\//, '').replace(/\/$/, '')
    path = base === '' ? path : base + '/' + path
    path = path.replace(/^\//, '').replace(/^\//, '')
    if (routeList.includes(path)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutesInRequiredList(tmp.children, routeList, base + '/' + path)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, permissions) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, permissions)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },

  generateRoutesByRequiredList({ commit }, routeList) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutesInRequiredList(asyncRoutes, routeList, '')
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

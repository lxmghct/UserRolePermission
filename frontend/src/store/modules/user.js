import { login } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
// import md5 from 'js-md5'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  id: null
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    setToken('admin-token')
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_ID: (state, id) => {
    state.id = id
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const params = new URLSearchParams()
    params.append('username', userInfo.username)
    params.append('password', userInfo.password)
    params.append('system', '数据管理系统')
    return new Promise((resolve, reject) => {
      login(params).then(response => {
        commit('SET_TOKEN', 'admin-token')
        commit('SET_ID', response.data.user.id)
        localStorage.setItem('userId', response.data.user.id)
        sessionStorage.setItem('permission', response.data.user.permissions || [])
        sessionStorage.setItem('loginInformation', JSON.stringify(response.data))
        commit('SET_NAME', response.data.user.trueName)
        setToken('admin-token')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
    //   getInfo(state.token).then(response => {
      const permissions = sessionStorage.getItem('permission')
      const data = {
        roles: permissions ? permissions.split(',') : [],
        introduction: 'I am a super administrator',
        avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
        name: 'Super Admin' }

      if (!data) {
        reject('Verification failed, please Login again.')
      }

      const { avatar, roles, introduction } = data

      // roles must be a non-empty array
      if (!roles || roles.length <= 0) {
        reject('getInfo: roles must be a non-null array!')
      }
      commit('SET_AVATAR', avatar)
      commit('SET_ROLES', roles)
      console.log(state.roles)
      commit('SET_INTRODUCTION', introduction)
      resolve(data)
    }).catch(error => {
      // eslint-disable-next-line no-undef
      // reject(error)
      console.log(error)
    })
    // })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
    //   logout(state.token).then(() => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resetRouter()

      // reset visited views and cached views
      // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
      dispatch('tagsView/delAllViews', null, { root: true })

      resolve()
    }).catch(error => {
      // eslint-disable-next-line no-undef
      reject(error)
    })
    // })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

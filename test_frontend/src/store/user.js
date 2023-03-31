import { resetRouter } from '@/router'

export const userStore = {
  roles: [],
  permissions: []
}

export const UserUtils = {
  resetUserStore () {
    userStore.roles = []
    userStore.permissions = []
    resetRouter()
  }
}

export const hasPermission = (permissions) => {
  if (Array.isArray(permissions)) {
    return permissions.some(permission => userStore.permissions.includes(permission))
  } else {
    return userStore.permissions.includes(permissions)
  }
}

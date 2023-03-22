import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getPermissions() {
  return request({
    url: '/vue-element-admin/permissions',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/vue-element-admin/permission',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/vue-element-admin/permission/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/vue-element-admin/permission/${id}`,
    method: 'delete'
  })
}

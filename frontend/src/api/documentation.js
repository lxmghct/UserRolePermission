import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getDocumentations() {
  return request({
    url: '/vue-element-admin/documentations',
    method: 'get'
  })
}

export function addDocumentation(data) {
  return request({
    url: '/vue-element-admin/documentation',
    method: 'post',
    data
  })
}

export function updateDocumentation(id, data) {
  return request({
    url: `/vue-element-admin/documentation/${id}`,
    method: 'put',
    data
  })
}

export function deleteDocumentation(id) {
  return request({
    url: `/vue-element-admin/documentation/${id}`,
    method: 'delete'
  })
}

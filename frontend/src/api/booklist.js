import request from '@/utils/request'

export function fetchClassificationTable() {
  return request({
    url: '/vue-element-admin/booklist/classificationTable',
    method: 'get'
  })
}

export function createClassification(data) {
  return request({
    url: '/vue-element-admin/booklist/createClassification',
    method: 'post',
    data
  })
}

export function updateClassification(data) {
  return request({
    url: '/vue-element-admin/booklist/updateClassification',
    method: 'post',
    data
  })
}

export function deleteClassification(data) {
  return request({
    url: '/vue-element-admin/booklist/deleteClassification',
    method: 'post',
    data
  })
}

export function fetchBook(query) {
  return request({
    url: '/vue-element-admin/booklist/book',
    method: 'get',
    params: query
  })
}

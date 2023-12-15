import request from '@/utils/request'

const api_name = '/admin/system/sysUser'

export default {

  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },

  save(role) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: role
    })
  },

  updateById(role) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: role
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  //更新用户状态
  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'put'
    })
  },
  //根据用户id查询用户已分配的角色
  getRolesByUserId(userId) {
    return request({
      url: `${api_name}/getRoles/${userId}`,
      method: 'get'
    })
  },
  //分配角色
  assignRoles(assginRoleVo) {
    return request({
      url: `${api_name}/assignRoles`,
      method: 'post',
      data: assginRoleVo
    })
  }
}
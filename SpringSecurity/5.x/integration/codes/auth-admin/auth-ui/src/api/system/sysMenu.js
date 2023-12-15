import request from '@/utils/request'

/*
菜单管理相关的API请求函数
*/
const api_name = '/admin/system/sysMenu'

export default {

  /*
  获取权限(菜单/功能)列表
  */
  list() {
    return request({
      url: `${api_name}/list`,
      method: 'get'
    })
  },

  /*
  删除
  */
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: "delete"
    })
  },

  /*
  保存
  */
  save(sysMenu) {
    return request({
      url: `${api_name}/save`,
      method: "post",
      data: sysMenu
    })
  },

  /*
  更新
  */
  updateById(sysMenu) {
    return request({
      url: `${api_name}/update`,
      method: "put",
      data: sysMenu
    })
  }
}
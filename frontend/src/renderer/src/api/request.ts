import axios, { AxiosError, type AxiosResponse } from 'axios'
import type { ApiResult } from '@/types/api'
import { useToast } from '@/composables/useToast'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
})

function handleUnauthorized() {
  localStorage.removeItem('examsystem_user')
  window.location.hash = '#/login'
}

request.interceptors.response.use(
  (response: AxiosResponse<ApiResult<unknown>>) => {
    const { data } = response
    if (data.code !== 200) {
      if (data.code === 401) {
        handleUnauthorized()
        return Promise.reject(new Error(data.message || '登录已过期，请重新登录'))
      }
      const message = data.message || `请求失败: ${data.code}`
      useToast().error(message)
      return Promise.reject(new Error(message))
    }
    return response
  },
  (error: AxiosError) => {
    const message = error.response?.data?.message || error.message || '网络错误'
    useToast().error(message)
    return Promise.reject(new Error(message))
  }
)

export default request

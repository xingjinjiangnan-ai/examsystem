import axios, { AxiosError, type AxiosResponse } from 'axios'
import type { ApiResult } from '@/types/api'

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
})

request.interceptors.response.use(
  (response: AxiosResponse<ApiResult<unknown>>) => {
    const { data } = response
    if (data.code !== 200) {
      return Promise.reject(new Error(data.message || `请求失败: ${data.code}`))
    }
    return response
  },
  (error: AxiosError) => {
    const message = error.response?.data?.message || error.message || '网络错误'
    return Promise.reject(new Error(message))
  }
)

export default request

import request from '../request'
import type { ApiResult } from '@/types/api'
import type { ChangePasswordReq, LoginReq, RegisterReq, UserProfile } from '@/types/user'

export function login(req: LoginReq) {
  return request.post<ApiResult<UserProfile>>('/api/v1/user/login', req).then(r => r.data.data)
}

export function register(req: RegisterReq) {
  return request.post<ApiResult<UserProfile>>('/api/v1/user/register', req).then(r => r.data.data)
}

export function changePassword(req: ChangePasswordReq) {
  return request.post<ApiResult<UserProfile>>('/api/v1/user/change-password', req).then(r => r.data.data)
}

export function logout() {
  return request.get<ApiResult<void>>('/api/v1/user/logout').then(r => r.data.data)
}

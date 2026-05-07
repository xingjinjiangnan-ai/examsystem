import request from '../request'
import type { ApiResult } from '@/types/api'
import type { ChangePasswordReq, LoginReq, RegisterReq, RegistrationType, UserProfile } from '@/types/user'

interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

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

export function listRegistrationRequests(params: { page?: number; size?: number; status?: RegistrationType }) {
  return request.get<ApiResult<PageResult<UserProfile>>>('/api/v1/user/registration-requests', { params }).then(r => r.data.data)
}

export function approveRegistration(id: number) {
  return request.post<ApiResult<UserProfile>>(`/api/v1/user/registration-requests/${id}/approve`).then(r => r.data.data)
}

export function rejectRegistration(id: number) {
  return request.post<ApiResult<UserProfile>>(`/api/v1/user/registration-requests/${id}/reject`).then(r => r.data.data)
}

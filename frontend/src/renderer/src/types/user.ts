export type RegistrationType = 'PENDING' | 'ACCEPTED' | 'REJECTED'

export type RoleType = 'SYSTEM_ADMIN' | 'TEACHER' | 'STUDENT'

export interface UserProfile {
  uid: number
  username: string
  realName: string
  studentId: string | null
  status: RegistrationType
  roles: string[]
  permissions: string[]
}

export interface LoginReq {
  username: string
  password: string
}

export interface RegisterReq {
  username: string
  password: string
  realName: string
  studentId: string
}

export interface ChangePasswordReq {
  username: string
  oldPassword: string
  newPassword: string
}

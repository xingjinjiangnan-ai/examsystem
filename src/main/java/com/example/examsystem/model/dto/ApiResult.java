package com.example.examsystem.model.dto;

import lombok.Data;

/**
 * 统一 API 响应结果封装类。
 * <p>
 * 约定错误码（基于 HTTP 状态码与业务扩展）：
 * </p>
 * <table border="1" cellpadding="5" cellspacing="0">
 *   <caption>错误码表</caption>
 *   <thead>
 *     <tr>
 *       <th>错误码</th>
 *       <th>含义</th>
 *       <th>说明</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr><td>200</td><td>成功</td><td>请求处理成功，返回 data 数据</td></tr>
 *     <tr><td>400</td><td>请求参数错误</td><td>请求参数缺失、格式错误或校验不通过</td></tr>
 *     <tr><td>401</td><td>未授权</td><td>未登录或 token 失效，需要身份认证</td></tr>
 *     <tr><td>403</td><td>禁止访问</td><td>已登录但无权限访问该资源</td></tr>
 *     <tr><td>404</td><td>资源不存在</td><td>请求的资源（如 API 路径、数据记录）未找到</td></tr>
 *     <tr><td>409</td><td>冲突</td><td>请求与当前资源状态冲突（如重复提交、唯一约束冲突）</td></tr>
 *     <tr><td>422</td><td>语义错误</td><td>请求格式正确但无法处理（如业务规则不满足）</td></tr>
 *     <tr><td>429</td><td>请求过频</td><td>超过限流阈值，请稍后重试</td></tr>
 *     <tr><td>500</td><td>服务器内部错误</td><td>服务器未知异常，请联系管理员</td></tr>
 *     <tr><td>502</td><td>网关错误</td><td>上游服务不可用或响应超时</td></tr>
 *     <tr><td>503</td><td>服务不可用</td><td>服务器暂时过载或维护中</td></tr>
 *   </tbody>
 * </table>
 * <p>
 * 业务模块可在此基础上扩展自定义错误码（如 1001 表示学号已存在），
 * 但建议将自定义码控制在 1000~9999 之间，避免与标准 HTTP 码冲突。
 * </p>
 *
 * @param <T> 响应数据的类型
 */
@Data
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(200, "success", data);
    }

    public static <T> ApiResult<T> ok() {
        return new ApiResult<>(200, "success", null);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(500, message, null);
    }
}
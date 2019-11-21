package com.github.config;

import lombok.Data;

@Data
public class ManagerConfig {

    /** 文件保存目录 */
    private String filePath;
    /** 文件访问前缀 */
    private String fileUrl;
}

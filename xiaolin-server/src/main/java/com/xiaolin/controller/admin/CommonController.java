package com.xiaolin.controller.admin;

import com.xiaolin.constant.MessageConstant;
import com.xiaolin.result.Result;
import com.xiaolin.utils.AliOssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author lzh
 * @description: 通用接口
 * @date 2025/11/25 19:19
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {

    private final AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        if (!file.isEmpty()) {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();

            // 填写Object完整路径，例如2025/11/a.png。Object完整路径中不能包含Bucket名称。
            //获取当前系统日期的字符串,格式为 yyyy/MM
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            //生成一个新的不重复的文件名
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = dir + "/" + newFileName;

            try {
                //文件的请求路径
                String filePath = aliOssUtil.upload(file.getBytes(), objectName);
                return Result.success(filePath);
            } catch (IOException e) {
                log.error("文件上传失败：{}", e.getMessage());
            }
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}

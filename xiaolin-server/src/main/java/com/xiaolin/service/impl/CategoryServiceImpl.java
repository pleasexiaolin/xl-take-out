package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.CategoryDTO;
import com.xiaolin.entity.CategoryDO;
import com.xiaolin.mapper.CategoryMapper;
import com.xiaolin.query.CategoryPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.CategoryService;
import com.xiaolin.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzh
 * @description: 分类服务
 * @date 2025/11/25 8:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {
    @Override
    public Result<Integer> save(CategoryDTO form) {
        // 幂等判断
        CategoryDO info = baseMapper.getByName(form.getName());
        if (info != null) {
            return Result.error("分类名称已存在，请修改后重试。");
        }

        try {
            baseMapper.insert(new CategoryDO(form));
        } catch (Exception e) {
            log.error("新增分类失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> remove(Long id) {
        // 幂等判断
        CategoryDO info = baseMapper.selectById(id);
        if (info != null && StatusConstant.ENABLE.equals(info.getStatus())) {
            return Result.error("启用后无法删除，请修改状态再删除！");
        }

        try {
            baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除分类失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }
        return Result.success();
    }

    @Override
    public Result<Integer> changeStatus(Integer status, Long id) {
        try {
            baseMapper.changeStatus(status, id, BaseContext.getCurrentUser());
        } catch (Exception e) {
            log.error("修改分类状态失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }
        return Result.success();
    }

    @Override
    public Page<CategoryVO> page(CategoryPageQuery condition, Page<CategoryVO> page) {
        return baseMapper.page(condition, page);
    }

    @Override
    public Result<Integer> update(CategoryDTO form) {
        // 幂等判断
        CategoryDO info = baseMapper.getByName(form.getName());
        if (info != null && !info.getName().equals(form.getName())) {
            return Result.error("改分类名称已存在，请修改后重试！");
        }

        try {
            baseMapper.updateById(form, BaseContext.getCurrentUser());
        } catch (Exception e) {
            log.error("更新分类失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }
        return Result.success();
    }

    @Override
    public Result<CategoryVO> info(Long id) {
        return Result.success(baseMapper.info(id));
    }

    @Override
    public Result<List<CategoryVO>> list(Integer type) {
        return Result.success(baseMapper.listByType(type));
    }
}
